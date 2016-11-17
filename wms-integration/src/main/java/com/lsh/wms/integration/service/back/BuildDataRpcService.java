package com.lsh.wms.integration.service.back;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.model.so.ObdOfcBackRequest;
import com.lsh.wms.api.model.so.ObdOfcItem;
import com.lsh.wms.api.model.wumart.CreateIbdDetail;
import com.lsh.wms.api.model.wumart.CreateIbdHeader;
import com.lsh.wms.api.model.wumart.CreateObdDetail;
import com.lsh.wms.api.model.wumart.CreateObdHeader;
import com.lsh.wms.api.service.back.IBuildDataRpcService;
import com.lsh.wms.api.service.back.IDataBackService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.po.PoOrderService;
import com.lsh.wms.core.service.po.ReceiveService;
import com.lsh.wms.core.service.so.SoDeliveryService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.utils.PackUtil;
import com.lsh.wms.integration.service.wumartsap.WuMart;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.po.*;
import com.lsh.wms.model.so.ObdDetail;
import com.lsh.wms.model.so.ObdHeader;
import com.lsh.wms.model.so.OutbDeliveryDetail;
import com.lsh.wms.model.so.OutbDeliveryHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by lixin-mac on 2016/11/17.
 */
@Service(protocol = "dubbo")
public class BuildDataRpcService implements IBuildDataRpcService {
    private static Logger logger = LoggerFactory.getLogger(BuildDataRpcService.class);

    @Autowired
    private ReceiveService receiveService;

    @Autowired
    private WuMart wuMart;

    @Autowired
    private DataBackService dataBackService;

    @Autowired
    private SoDeliveryService soDeliveryService;
    @Autowired
    private SoOrderService soOrderService;
    @Autowired
    private PoOrderService poOrderService;
    @Autowired
    private ItemService itemService;


    public void BuildIbdData(Long receiveId) {
        List<ReceiveDetail> receiveDetails = receiveService.getReceiveDetailListByReceiveId(receiveId);
        ReceiveHeader receiveHeader = receiveService.getReceiveHeaderByReceiveId(receiveId);
        // TODO: 2016/11/3 回传WMSAP 组装信息
        CreateIbdHeader createIbdHeader = new CreateIbdHeader();
        List<CreateIbdDetail> details = new ArrayList<CreateIbdDetail>();
        for(ReceiveDetail receiveDetail : receiveDetails){
            CreateIbdDetail detail = new CreateIbdDetail();
            detail.setPoNumber(receiveHeader.getOrderOtherId());
            detail.setPoItme(receiveDetail.getDetailOtherId());
            BigDecimal inboudQty =  receiveDetail.getInboundQty();

            BigDecimal orderQty = receiveDetail.getOrderQty();
            BigDecimal deliveQty = receiveHeader.getOrderType().equals(3) ? orderQty : inboudQty;
            if(deliveQty.compareTo(BigDecimal.ZERO) <= 0){
                continue;
            }
            detail.setDeliveQty(deliveQty.setScale(2,BigDecimal.ROUND_HALF_UP));
            detail.setUnit(receiveDetail.getUnitName());
            detail.setMaterial(receiveDetail.getSkuCode());
            detail.setOrderType(receiveHeader.getOrderType());
            detail.setVendMat(receiveHeader.getReceiveId().toString());

            details.add(detail);
        }
        createIbdHeader.setItems(details);

        if(receiveHeader.getOwnerUid() == 1){
            wuMart.sendIbd(createIbdHeader);

        }else{
            dataBackService.erpDataBack(JSON.toJSONString(createIbdHeader));
        }
    }

    public void BuildLshOfcObdData(Long deliveryId)
    {
        OutbDeliveryHeader header = soDeliveryService.getOutbDeliveryHeaderByDeliveryId(deliveryId);
        List<OutbDeliveryDetail> details = soDeliveryService.getOutbDeliveryDetailListByDeliveryId(deliveryId);
        if(header == null){
            //error
        }
        if(details == null || details.size() == 0) {
            //error
        }
        ObdOfcBackRequest request = new ObdOfcBackRequest();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String now = sdf.format(date);
        request.setWms(2);//该字段写死 2
        request.setDeliveryTime(now);
        request.setObdCode(header.getOrderId().toString());
        ObdHeader obdHeader = soOrderService.getOutbSoHeaderByOrderId(header.getOrderId());
        request.setSoCode(obdHeader.getOrderOtherId());
        request.setWaybillCode(header.getTuId());//运单号
        request.setBoxNum(header.getBoxNum().intValue());
        request.setTurnoverBoxNum(header.getTurnoverBoxNum().intValue());
        request.setWarehouseCode("草,要我的id干什么");
        List<ObdOfcItem> items = new ArrayList<ObdOfcItem>();
        for (OutbDeliveryDetail detail : details) {
            ObdOfcItem item = new ObdOfcItem();
            item.setPackNum(detail.getPackUnit());
            BigDecimal outQty = detail.getDeliveryNum();
            item.setSkuQty(outQty);
            BaseinfoItem baseinfoItem = itemService.getItem(detail.getItemId());
            item.setSupplySkuCode(baseinfoItem.getSkuCode());
            items.add(item);
        }
        request.setDetails(items);
    }

    //根据一个obd生成需要的回传物美的信息
    public void BuildSapObdData(Long deliveryId)
    {
        OutbDeliveryHeader header = soDeliveryService.getOutbDeliveryHeaderByDeliveryId(deliveryId);
        List<OutbDeliveryDetail> details = soDeliveryService.getOutbDeliveryDetailListByDeliveryId(deliveryId);
        if(header == null){
            //error
        }
        if(details == null || details.size() == 0) {
            //error
        }
        List<CreateObdDetail> createObdDetails = new ArrayList<CreateObdDetail>();
        for (OutbDeliveryDetail detail : details) {
            CreateObdDetail createObdDetail = new CreateObdDetail();
            //TODO 这段代码是非常危险的,根据itemid去获取唯一的一条 有问题,稍后review影响
            ObdDetail obdDetail = soOrderService.getObdDetailByOrderIdAndItemId(detail.getOrderId(), detail.getItemId());
            if (null == obdDetail) {
                throw new BizCheckedException("2900004");
            }
            BigDecimal outQty = detail.getDeliveryNum();
            //ea转换为包装数量。
            ObdHeader obdHeader = soOrderService.getOutbSoHeaderByOrderId(detail.getOrderId());
            createObdDetail.setDlvQty(PackUtil.EAQty2UomQty(outQty, detail.getPackUnit()));
            createObdDetail.setRefItem(obdDetail.getDetailOtherId());
            createObdDetail.setMaterial(obdDetail.getSkuCode());
            createObdDetails.add(createObdDetail);
        }
    }

    //根据一个obd同时生成回传的obd,ibd,呵呵,不过这个是有问题的,应该先生成一个ibd在库里面.......
    public void BuildSapIbdObdData(Long deliveryId)
    {
        OutbDeliveryHeader header = soDeliveryService.getOutbDeliveryHeaderByDeliveryId(deliveryId);
        List<OutbDeliveryDetail> details = soDeliveryService.getOutbDeliveryDetailListByDeliveryId(deliveryId);
        if(header == null){
            //error
        }
        if(details == null || details.size() == 0) {
            //error
        }
        List<CreateObdDetail> createObdDetailList = new ArrayList<CreateObdDetail>();
        List<CreateIbdDetail> createIbdDetailList = new ArrayList<CreateIbdDetail>();
        List<CreateObdDetail>  createStoObdDetails = new LinkedList<CreateObdDetail>();
        for(OutbDeliveryDetail detail : details) {
            Long itemId = detail.getItemId();
            Long orderId = detail.getOrderId();
            CreateObdDetail createObdDetail = new CreateObdDetail();
            //obd的detail
            ObdDetail obdDetail = soOrderService.getObdDetailByOrderIdAndItemId(orderId, itemId);
            if (null == obdDetail) {
                throw new BizCheckedException("2900004");
            }
            //sto obd order_other_id
            ObdHeader obdHeader = soOrderService.getOutbSoHeaderByOrderId(obdDetail.getOrderId());
            createObdDetail.setRefDoc(obdHeader.getOrderOtherId());
            //销售单位
            createObdDetail.setSalesUnit(obdDetail.getPackName());
            //交货量 qc的ea/销售单位
            createObdDetail.setDlvQty(PackUtil.EAQty2UomQty(detail.getDeliveryNum(), obdDetail.getPackName()).setScale(2, BigDecimal.ROUND_HALF_UP));
            //sto obd detail detail_other_id
            createObdDetail.setRefItem(obdDetail.getDetailOtherId());
            createObdDetail.setOrderType(obdHeader.getOrderType());
            //找关系 sto和cpo
            List<IbdObdRelation> ibdObdRelations = poOrderService.getIbdObdRelationListByObd(obdHeader.getOrderOtherId(), obdDetail.getDetailOtherId());
            if (null == ibdObdRelations || ibdObdRelations.size() < 1) {
                createStoObdDetails.add(createObdDetail);
                continue;
                //throw new BizCheckedException("2900005");
            }
            createObdDetailList.add(createObdDetail);
            IbdObdRelation ibdObdRelation = ibdObdRelations.get(0);
            IbdHeader ibdHeader = poOrderService.getInbPoHeaderByOrderOtherId(ibdObdRelation.getIbdOtherId());
            IbdDetail ibdDetail = poOrderService.getInbPoDetailByOrderIdAndDetailOtherId(ibdHeader.getOrderId(), ibdObdRelation.getIbdDetailId());
            //拼装CreateIbdDetail
            CreateIbdDetail createIbdDetail = new CreateIbdDetail();
            //采购凭证号
            createIbdDetail.setPoNumber(ibdHeader.getOrderOtherId());
            //采购订单的计量单位
            createIbdDetail.setUnit(ibdDetail.getPackName());
            //实际出库数量
            createIbdDetail.setDeliveQty(PackUtil.EAQty2UomQty(detail.getDeliveryNum(), ibdDetail.getPackName()).setScale(2, BigDecimal.ROUND_HALF_UP));
            //行项目号
            createIbdDetail.setPoItme(ibdDetail.getDetailOtherId());
            createIbdDetail.setVendMat(ibdHeader.getOrderId().toString());
            createIbdDetail.setOrderType(ibdHeader.getOrderType());
            createIbdDetailList.add(createIbdDetail);
        }
        CreateObdHeader createObdHeader = new CreateObdHeader();
        createObdHeader.setItems(createObdDetailList);
        CreateIbdHeader createIbdHeader = new CreateIbdHeader();
        createIbdHeader.setItems(createIbdDetailList);
        CreateObdHeader createStoObdHeader = new CreateObdHeader();

        logger.info("+++++++++++++++++++++++++++++++++maqidi+++++++++++++++++++++++" + JSON.toJSONString(createObdHeader));
        logger.info("+++++++++++++++++++++++++++++++++maqidi++++++++++++++" + JSON.toJSONString(createObdHeader));

        //鑫哥服务
//        wuMartSap.ibd2Sap(createIbdHeader);
//        wuMartSap.obd2Sap(createObdHeader);
        Map<String, Object> ibdObdMap = new HashMap<String, Object>();
        ibdObdMap.put("createIbdHeader", createIbdHeader);
        ibdObdMap.put("createObdHeader", createObdHeader);
        ibdObdMap.put("createStoObdHeader", createStoObdHeader);
       // return ibdObdMap;
//        wuMart.sendIbd(createIbdHeader);
//        wuMart.sendObd(createObdHeader);
    }
}
