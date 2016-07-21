package com.lsh.wms.rpc.service.po;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.config.PropertyUtils;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.api.model.base.BaseResponse;
import com.lsh.wms.api.model.base.ResUtils;
import com.lsh.wms.api.model.base.ResponseConstant;
import com.lsh.wms.api.model.po.ReceiptItem;
import com.lsh.wms.api.model.po.ReceiptRequest;
import com.lsh.wms.api.service.po.IReceiptRestService;
import com.lsh.wms.core.constant.BusiConstant;
import com.lsh.wms.core.constant.CsiConstan;
import com.lsh.wms.core.constant.PoConstant;
import com.lsh.wms.core.service.csi.CsiSkuService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.po.PoOrderService;
import com.lsh.wms.core.service.po.PoReceiptService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.csi.CsiSku;
import com.lsh.wms.model.po.InbPoDetail;
import com.lsh.wms.model.po.InbPoHeader;
import com.lsh.wms.model.po.InbReceiptDetail;
import com.lsh.wms.model.po.InbReceiptHeader;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.rpc.service.item.ItemRestService;
import com.lsh.wms.rpc.service.location.LocationRpcService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.math.BigDecimal.ROUND_HALF_EVEN;

/**
 * Project Name: lsh-wms
 * Created by fuhao
 * Date: 16/7/12
 * Time: 16/7/12.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.service.po.
 * desc:类功能描述
 */
@Service(protocol = "rest")
@Path("order/po/receipt")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class ReceiptRestService implements IReceiptRestService {

    @Autowired
    private PoReceiptService poReceiptService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private PoOrderService poOrderService;


    @Autowired
    private LocationRpcService locationRpcService;

    @Autowired
    private StockQuantService stockQuantService;


    @Autowired
    private CsiSkuService csiSkuService;


    @POST
    @Path("init")
    public String init(String poReceiptInfo) {
        InbReceiptHeader inbReceiptHeader = JSON.parseObject(poReceiptInfo,InbReceiptHeader.class);
        List<InbReceiptDetail> inbReceiptDetailList = JSON.parseArray(inbReceiptHeader.getReceiptDetails(),InbReceiptDetail.class);
        poReceiptService.orderInit(inbReceiptHeader,inbReceiptDetailList);
        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("throw")
    public String throwOrder(String orderOtherId) throws BizCheckedException {
        InbPoHeader inbPoHeader = new InbPoHeader();
        inbPoHeader.setOrderOtherId(orderOtherId);
        inbPoHeader.setOrderStatus(PoConstant.ORDER_THROW);
        poOrderService.updateByOrderOtherId(inbPoHeader);
        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("insert")
    public BaseResponse insertOrder(ReceiptRequest request) throws BizCheckedException{
        //初始化InbReceiptHeader
        InbReceiptHeader inbReceiptHeader = new InbReceiptHeader();
        ObjUtils.bean2bean(request, inbReceiptHeader);

        //设置receiptOrderId
        inbReceiptHeader.setReceiptOrderId(RandomUtils.genId());

        //设置托盘码,暂存区,分配库位;实际库位由他人写入
        BaseinfoLocation baseinfoLocation = locationRpcService.assignTemporary();
        inbReceiptHeader.setLocation(baseinfoLocation.getLocationId());// TODO: 16/7/20  暂存区信息

        //设置InbReceiptHeader状态
        inbReceiptHeader.setReceiptStatus(BusiConstant.EFFECTIVE_YES);

        //设置InbReceiptHeader插入时间
        inbReceiptHeader.setInserttime(new Date());

        //初始化List<InbReceiptDetail>
        List<InbReceiptDetail> inbReceiptDetailList = new ArrayList<InbReceiptDetail>();
        List<InbPoDetail> updateInbPoDetailList = new ArrayList<InbPoDetail>();

        for(ReceiptItem receiptItem : request.getItems()) {
            InbReceiptDetail inbReceiptDetail = new InbReceiptDetail();

            ObjUtils.bean2bean(receiptItem, inbReceiptDetail);

            //设置receiptOrderId
            inbReceiptDetail.setReceiptOrderId(inbReceiptHeader.getReceiptOrderId());

            //根据request中的orderOtherId查询InbPoHeader
            InbPoHeader inbPoHeader = poOrderService.getInbPoHeaderByOrderOtherId(request.getOrderOtherId());
            if(inbPoHeader == null) {
                throw  new BizCheckedException("2020001");
            }

            if(inbPoHeader.getOrderStatus() == PoConstant.ORDER_THROW){
                throw  new BizCheckedException("2020002");
            }

            //写入InbReceiptDetail中的OrderId
            inbReceiptDetail.setOrderId(inbPoHeader.getOrderId());


            //根据InbPoHeader中的OwnerUid及InbReceiptDetail中的SkuId获取Item
            CsiSku csiSku = csiSkuService.getSkuByCode(CsiConstan.CSI_CODE_TYPE_BARCODE,inbReceiptDetail.getBarCode());
            if(null == csiSku || csiSku.getSkuId() ==null){
                throw  new BizCheckedException("2020004");
            }
            inbReceiptDetail.setSkuId(csiSku.getSkuId());
            BaseinfoItem baseinfoItem = itemService.getItem(inbPoHeader.getOwnerUid(), csiSku.getSkuId());
            inbReceiptDetail.setItemId(baseinfoItem.getItemId());

            //根据OrderId及SkuId获取InbPoDetail
            InbPoDetail inbPoDetail = poOrderService.getInbPoDetailByOrderIdAndSkuId(inbReceiptDetail.getOrderId(),inbReceiptDetail.getSkuId());
            //写入InbReceiptDetail中的OrderQty
            inbReceiptDetail.setOrderQty(inbPoDetail.getOrderQty());
            // 判断是否超过订单总数
            if(inbPoDetail.getInboundQty()+ inbPoDetail.getInboundQty() > inbPoDetail.getOrderQty()){
                throw  new BizCheckedException("2020005");
            }

            // TODO: 16/7/20   商品信息是否完善,怎么排查.2,保质期例外怎么验证?
            //保质期判断,如果失败抛出异常
            BigDecimal shelLife = baseinfoItem.getShelfLife();
            String producePlace = baseinfoItem.getProducePlace();
            Double shelLife_CN= Double.parseDouble(PropertyUtils.getString("shelLife_CN"));
            Double shelLife_Not_CN=Double.parseDouble(PropertyUtils.getString("shelLife_Not_CN"));
            String produceChina=PropertyUtils.getString("produceChina");

            if(producePlace.contains(produceChina)){ // TODO: 16/7/20  产地是否存的是CN
                BigDecimal left_day = new BigDecimal(DateUtils.getYearMonthDay(new Date()) - DateUtils.getYearMonthDay(inbReceiptDetail.getProTime()));
                if(left_day.divide(shelLife,2,ROUND_HALF_EVEN).doubleValue() < shelLife_CN){
                    throw new BizCheckedException("2020003");
                }
            }else {
                BigDecimal left_day = new BigDecimal(DateUtils.getYearMonthDay(new Date()) - DateUtils.getYearMonthDay(inbReceiptDetail.getProTime()));
                if(left_day.divide(shelLife,2,ROUND_HALF_EVEN).doubleValue() < shelLife_Not_CN){
                    throw new BizCheckedException("2020003");
                }
            }

            InbPoDetail updateInbPoDetail = new InbPoDetail();
            updateInbPoDetail.setInboundQty(inbPoDetail.getInboundQty());
            updateInbPoDetail.setOrderId(inbReceiptDetail.getOrderId());
            updateInbPoDetail.setSkuId(inbReceiptDetail.getSkuId());
            updateInbPoDetailList.add(updateInbPoDetail);
            inbReceiptDetailList.add(inbReceiptDetail);


            /***
             * skuId 商品码
             * locationId 存储位id
             * containerId 容器设备id
             * qty 商品数量
             * supplierId 货物供应商id
             * ownerId 货物所属公司id
             * inDate 入库时间
             * expireDate 保质期失效时间
             * itemId
             *
             */
            // TODO: 16/7/21  如何形成上架任务 
            StockQuant quant = new StockQuant();
            quant.setSkuId(inbReceiptDetail.getSkuId());
            quant.setItemId(inbReceiptDetail.getItemId());
            quant.setLocationId(inbReceiptHeader.getLocation());
            quant.setContainerId(inbReceiptHeader.getContainerId());
            quant.setSupplierId(inbPoHeader.getSupplierCode());
            quant.setOwnerId(inbPoHeader.getOwnerUid());
            Date receiptTime = inbReceiptHeader.getReceiptTime();
            quant.setInDate(receiptTime.getTime());
            Long expireDate =  inbReceiptDetail.getProTime().getTime()+shelLife.longValue(); // 生产日期+保质期=保质期失效时间
            quant.setExpireDate(expireDate);

            stockQuantService.create(quant);
        }

        //插入订单
        poReceiptService.insertOrder(inbReceiptHeader, inbReceiptDetailList,updateInbPoDetailList );


        return ResUtils.getResponse(ResponseConstant.RES_CODE_0,ResponseConstant.RES_MSG_OK,null);
    }
}
