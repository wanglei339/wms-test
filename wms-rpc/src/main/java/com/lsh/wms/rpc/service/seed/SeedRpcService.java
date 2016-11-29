package com.lsh.wms.rpc.service.seed;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.base.common.utils.StrUtils;
import com.lsh.wms.api.model.po.ReceiptItem;
import com.lsh.wms.api.model.po.ReceiptRequest;
import com.lsh.wms.api.model.so.ObdStreamDetail;
import com.lsh.wms.api.service.location.ILocationRpcService;
import com.lsh.wms.api.service.seed.ISeedRpcService;
import com.lsh.wms.core.constant.*;
import com.lsh.wms.core.service.csi.CsiCustomerService;
import com.lsh.wms.core.service.csi.CsiSkuService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.po.PoOrderService;
import com.lsh.wms.core.service.po.PoReceiptService;
import com.lsh.wms.core.service.po.ReceiveService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.csi.CsiCustomer;
import com.lsh.wms.model.csi.CsiSku;
import com.lsh.wms.model.po.*;
import com.lsh.wms.model.so.ObdHeader;
import com.lsh.wms.model.transfer.StockTransferPlan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by wuhao on 16/10/17.
 */
@Service(protocol = "dubbo")
public class SeedRpcService implements ISeedRpcService {
    private static final Logger logger = LoggerFactory.getLogger(SeedRpcService.class);


    @Autowired
    private PoReceiptService poReceiptService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private PoOrderService poOrderService;
    @Autowired
    SoOrderService soOrderService;

    @Reference
    private ILocationRpcService locationRpcService;

    @Autowired
    private CsiSkuService csiSkuService;

    @Autowired
    private ReceiveService receiveService;
    @Autowired
    private CsiCustomerService csiCustomerService;

    public void insertReceipt(ReceiptRequest request) throws BizCheckedException {
        //查询inbReceiptHeader是否存在 根据托盘查询
        Map<String,Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("containerId",request.getContainerId());
        Map<String,Long> orderMap = request.getOrderMap();
        InbReceiptHeader inbReceiptHeader = poReceiptService.getInbReceiptHeaderByParams(mapQuery);
        if(inbReceiptHeader == null){
            //初始化InbReceiptHeader
            inbReceiptHeader = new InbReceiptHeader();
            ObjUtils.bean2bean(request, inbReceiptHeader);
            //设置receiptOrderId
            inbReceiptHeader.setReceiptOrderId(RandomUtils.genId());
            //设置门店以及收货类型
            inbReceiptHeader.setReceiptType(ReceiptContant.RECEIPT_TYPE_STORE);
            inbReceiptHeader.setStoreCode(request.getStoreId());
            //设置InbReceiptHeader状态
            inbReceiptHeader.setReceiptStatus(BusiConstant.EFFECTIVE_YES);
            //设置InbReceiptHeader插入时间
            inbReceiptHeader.setInserttime(new Date());
        }
        // TODO: 16/8/19 设置门店暂存区
        BaseinfoLocation baseinfoLocation = locationRpcService.assignTemporary();
        inbReceiptHeader.setLocation(baseinfoLocation.getLocationId());// TODO: 16/7/20  暂存区信息

        //初始化List<InbReceiptDetail>
        List<InbReceiptDetail> inbReceiptDetailList = new ArrayList<InbReceiptDetail>();
        List<IbdDetail> updateIbdDetailList = new ArrayList<IbdDetail>();
        //验收单
        List<ReceiveDetail> updateReceiveDetailList = new ArrayList<ReceiveDetail>();

        //生成出库detail
        List<ObdStreamDetail> obdStreamDetailList = new ArrayList<ObdStreamDetail>();

        Map<Long,Long> locationMap = new HashMap<Long, Long>();
        List<StockTransferPlan> planList = new ArrayList<StockTransferPlan>();


        for(ReceiptItem receiptItem : request.getItems()){
//            if(System.currentTimeMillis() - receiptItem.getProTime().getTime() <= 0) {
//                throw new BizCheckedException("2020009");
//            }

            if(receiptItem.getInboundQty().compareTo(BigDecimal.ZERO) < 0) {
                throw new BizCheckedException("2020007");
            }

            InbReceiptDetail inbReceiptDetail = new InbReceiptDetail();

            ObjUtils.bean2bean(receiptItem, inbReceiptDetail);
            inbReceiptDetail.setProTime(new Date());

            //根据request中的orderOtherId查询InbPoHeader
            IbdHeader ibdHeader = poOrderService.getInbPoHeaderByOrderOtherId(request.getOrderOtherId());
            if (ibdHeader == null) {
                throw new BizCheckedException("2020001");
            }
            // TODO: 2016/10/8 查询验收单是否存在,如果不存在,则根据ibd重新生成
            ReceiveHeader receiveHeader = receiveService.getReceiveHeader(ibdHeader.getOrderId());
            Long receiveId = 0l;
            if(receiveHeader == null){
                receiveId = this.genReceive(ibdHeader,request.getItems());

            }else{
                receiveId = receiveHeader.getReceiveId();
            }

            //设置receiptOrderId
            inbReceiptDetail.setReceiptOrderId(inbReceiptHeader.getReceiptOrderId());
            inbReceiptDetail.setOrderOtherId(ibdHeader.getOrderOtherId());
            inbReceiptDetail.setOrderId(ibdHeader.getOrderId());

            boolean isCanReceipt = ibdHeader.getOrderStatus() == PoConstant.ORDER_THROW || ibdHeader.getOrderStatus() == PoConstant.ORDER_RECTIPT_PART || ibdHeader.getOrderStatus() == PoConstant.ORDER_RECTIPTING;
            if (!isCanReceipt) {
                throw new BizCheckedException("2020002");
            }

            //根据InbPoHeader中的OwnerUid及InbReceiptDetail中的SkuId获取Item
            CsiSku csiSku = csiSkuService.getSkuByCode(CsiConstan.CSI_CODE_TYPE_BARCODE, inbReceiptDetail.getBarCode());
            if (null == csiSku || csiSku.getSkuId() == null) {
                throw new BizCheckedException("2020022");
            }
            inbReceiptDetail.setSkuId(csiSku.getSkuId());
            BaseinfoItem baseinfoItem = itemService.getItem(ibdHeader.getOwnerUid(), csiSku.getSkuId());
            inbReceiptDetail.setItemId(baseinfoItem.getItemId());

            //根据OrderId及SkuCode获取InbPoDetail
            IbdDetail ibdDetail = poOrderService.getInbPoDetailByOrderIdAndSkuCode(ibdHeader.getOrderId(), baseinfoItem.getSkuCode());

            //写入InbReceiptDetail中的OrderQty
            inbReceiptDetail.setOrderQty(ibdDetail.getOrderQty());

            // 判断是否超过订单总数
            BigDecimal poInboundQty = null != ibdDetail.getInboundQty() ? ibdDetail.getInboundQty() : new BigDecimal(0);

            if (poInboundQty.add(inbReceiptDetail.getInboundQty()).compareTo(ibdDetail.getOrderQty()) > 0) {
                throw new BizCheckedException("2020005");
            }

            // 批量修改ibd 实收数量
            IbdDetail updateIbdDetail = new IbdDetail();
            updateIbdDetail.setInboundQty(inbReceiptDetail.getInboundQty());
            updateIbdDetail.setOrderId(inbReceiptDetail.getOrderId());
            //updateIbdDetail.setSkuId(inbReceiptDetail.getSkuId());
            updateIbdDetail.setDetailOtherId(ibdDetail.getDetailOtherId());
            updateIbdDetailList.add(updateIbdDetail);

            //根据receiveId及SkuCode获取receiveDetail
            ReceiveDetail receiveDetail = receiveService.getReceiveDetailByReceiveIdAndSkuCode(receiveId, baseinfoItem.getSkuCode());


            //批量修改receive 实收数量
            ReceiveDetail updateReceiveDetail = new ReceiveDetail();
            updateReceiveDetail.setDetailOtherId(receiveDetail.getDetailOtherId());
            updateReceiveDetail.setReceiveId(receiveDetail.getReceiveId());
            updateReceiveDetail.setInboundQty(inbReceiptDetail.getInboundQty());
            updateReceiveDetail.setCode(baseinfoItem.getCode());//更新国条码
            updateReceiveDetailList.add(updateReceiveDetail);

            //生成出库detail信息
            String key = StrUtils.formatString(RedisKeyConstant.PO_STORE, ibdHeader.getOrderId(), inbReceiptHeader.getStoreCode());


            BigDecimal [] decimals = inbReceiptDetail.getInboundQty().divideAndRemainder(inbReceiptDetail.getPackUnit());

            Long obdOrderId = orderMap.get(key);
            ObdHeader obdHeader = soOrderService.getOutbSoHeaderByOrderId(obdOrderId);
            CsiCustomer csiCustomer = csiCustomerService.getCustomerByCustomerCode(obdHeader.getDeliveryCode());
            ObdStreamDetail obdStreamDetail = new ObdStreamDetail();
            obdStreamDetail.setItemId(inbReceiptDetail.getItemId());
            obdStreamDetail.setContainerId(inbReceiptHeader.getContainerId());
            obdStreamDetail.setOwnerId(ibdHeader.getOwnerUid());
            obdStreamDetail.setAllocQty(inbReceiptDetail.getInboundQty());
            obdStreamDetail.setPickQty(inbReceiptDetail.getInboundQty());
            obdStreamDetail.setAllocCollectLocation(csiCustomer.getCollectRoadId());
            obdStreamDetail.setRealCollectLocation(csiCustomer.getCollectRoadId());
            if(decimals[1].compareTo(BigDecimal.ZERO)==0) {
                obdStreamDetail.setAllocUnitQty(decimals[0]);
                obdStreamDetail.setAllocUnitName(inbReceiptDetail.getPackName());
            }else {
                obdStreamDetail.setAllocUnitQty(inbReceiptDetail.getInboundQty());
                obdStreamDetail.setAllocUnitName("EA");
            }

            obdStreamDetail.setSkuId(inbReceiptDetail.getSkuId());
            obdStreamDetailList.add(obdStreamDetail);
            obdStreamDetail.setOrderId(obdOrderId);

            inbReceiptDetailList.add(inbReceiptDetail);


        }
        poReceiptService.insertOrder(inbReceiptHeader, inbReceiptDetailList, updateIbdDetailList,updateReceiveDetailList,obdStreamDetailList);
    }

    public Long genReceive(IbdHeader ibdHeader,List<ReceiptItem> receiptItemList){
        // TODO: 16/11/9 保存skucode和barcode的映射关系
        Map<String,String> skuMap = new HashMap<String, String>();
        for(ReceiptItem rt:receiptItemList){
            BaseinfoItem baseinfoItem = itemService.getItem(ibdHeader.getOwnerUid(), rt.getSkuId());
            if(baseinfoItem == null){
                continue;
            }
            skuMap.put(baseinfoItem.getSkuCode(),rt.getBarCode());
        }

        //增加receiveHeader总单
        Long receiveId = RandomUtils.genId();
        ReceiveHeader receiveHeader = new ReceiveHeader();
        ObjUtils.bean2bean(ibdHeader,receiveHeader);
        receiveHeader.setReceiveId(receiveId);
        receiveHeader.setOrderStatus(1);
        receiveHeader.setCreatedAt(DateUtils.getCurrentSeconds());
        List<IbdDetail> ibdList = poOrderService.getInbPoDetailListByOrderId(ibdHeader.getOrderId());
        List<ReceiveDetail> receiveDetails = new ArrayList<ReceiveDetail>();
        for (IbdDetail ibdDetail : ibdList){
            ReceiveDetail receiveDetail = new ReceiveDetail();
            ObjUtils.bean2bean(ibdDetail,receiveDetail);
            receiveDetail.setCode(skuMap.get(ibdDetail.getSkuCode()));// TODO: 16/11/9 增加国条
            receiveDetail.setReceiveId(receiveId);
            receiveDetail.setInboundQty(BigDecimal.ZERO);
            receiveDetail.setCreatedAt(DateUtils.getCurrentSeconds());
            receiveDetails.add(receiveDetail);
        }
        receiveService.insertReceive(receiveHeader,receiveDetails);
        return receiveId;
    }
}
