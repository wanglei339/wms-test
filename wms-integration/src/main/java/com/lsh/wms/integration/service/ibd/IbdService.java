package com.lsh.wms.integration.service.ibd;

import com.alibaba.dubbo.common.json.ParseException;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.BeanMapTransUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.base.q.Utilities.Json.JSONObject;
import com.lsh.wms.api.model.base.BaseResponse;
import com.lsh.wms.api.model.base.ResUtils;
import com.lsh.wms.api.model.base.ResponseConstant;
import com.lsh.wms.api.model.po.*;
import com.lsh.wms.api.model.po.IbdDetail;
import com.lsh.wms.api.model.so.ObdOfcBackRequest;
import com.lsh.wms.api.model.so.ObdOfcItem;
import com.lsh.wms.api.model.stock.StockItem;
import com.lsh.wms.api.model.stock.StockRequest;
import com.lsh.wms.api.model.wumart.CreateIbdDetail;
import com.lsh.wms.api.model.wumart.CreateIbdHeader;
import com.lsh.wms.api.model.wumart.CreateObdDetail;
import com.lsh.wms.api.model.wumart.CreateObdHeader;
import com.lsh.wms.api.service.po.IIbdService;
import com.lsh.wms.api.service.po.IPoRpcService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.wumart.IWuMartSap;
import com.lsh.wms.core.constant.IntegrationConstan;
import com.lsh.wms.core.constant.PoConstant;
import com.lsh.wms.core.constant.SoConstant;
import com.lsh.wms.core.constant.SysLogConstant;
import com.lsh.wms.core.service.csi.CsiSupplierService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.po.PoOrderService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.system.SysLogService;
import com.lsh.wms.integration.service.back.DataBackService;
import com.lsh.wms.integration.service.common.utils.HttpUtil;
import com.lsh.wms.integration.service.wumartsap.WuMart;
import com.lsh.wms.integration.service.wumartsap.WuMartSap;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.csi.CsiSupplier;
import com.lsh.wms.model.po.*;
import com.lsh.wms.model.so.ObdDetail;
import com.lsh.wms.model.so.ObdHeader;
import com.lsh.wms.model.system.SysLog;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import net.sf.json.util.JSONUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by mali on 16/9/2.
 */
@Service(protocol = "rest", validation = "true")
@Path("ibd")
@Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class IbdService implements IIbdService {
    private static Logger logger = LoggerFactory.getLogger(IbdService.class);

    @Reference
    private IPoRpcService poRpcService;

    @Autowired
    private PoOrderService poOrderService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private DataBackService dataBackService;
    @Autowired
    private SoOrderService soOrderService;

    @Autowired
    private SysLogService sysLogService;

    @Autowired
    private WuMartSap wuMartSap;
//    @Reference
//    private IWuMartSap wuMartSap;

    @Autowired
    private WuMart wuMart;

    @Autowired
    private CsiSupplierService supplierService;

    @POST
    @Path("add")
    public BaseResponse add(IbdRequest request) throws BizCheckedException{
        //数量做转换 ea转化为外包装箱数
        List<IbdDetail> details = request.getDetailList();

//        List<IbdDetail> newDetails = new ArrayList<IbdDetail>();
        List<PoItem> items = new ArrayList<PoItem>();

        if(request.getWarehouseCode().equals("DC41")){

            String jsonStr = HttpUtil.doPost(IntegrationConstan.URL_PO,request);
            if(jsonStr == null || jsonStr.equals("")){
                return ResUtils.getResponse(ResponseConstant.RES_CODE_0, ResponseConstant.RES_MSG_ERROR, null);
            }
            JSONObject obj = new JSONObject(jsonStr);
            BaseResponse response = new BaseResponse();
            ObjUtils.bean2bean(obj,response);
            return response;
        }

        for(IbdDetail ibdDetail : details){
            List<BaseinfoItem>  baseinfoItemList= itemService.getItemsBySkuCode(request.getOwnerUid(),ibdDetail.getSkuCode());
            if(baseinfoItemList == null || baseinfoItemList.size() <= 0) {
                throw new BizCheckedException("2770001");
            }

//            BigDecimal qty = ibdDetail.getOrderQty().divide(ibdDetail.getPackUnit(),2);
//            ibdDetail.setOrderQty(qty);
            PoItem poItem = new PoItem();
            ObjUtils.bean2bean(ibdDetail,poItem);
//            newDetails.add(ibdDetail);
            poItem.setSkuName(baseinfoItemList.get(baseinfoItemList.size()-1).getSkuName());
            items.add(poItem);
        }
        //request.setDetailList(newDetails);

        //初始化PoRequest
        if (StringUtils.isContains(request.getSupplierCode(), "DC")) {
            request.setSupplierCode(request.getSupplierCode().substring(2));
        }
        PoRequest poRequest = new PoRequest();
        ObjUtils.bean2bean(request,poRequest);
        //将IbdDetail转化为poItem
        // TODO: 16/9/5  warehouseCode 转换为warehouseId 如何转化 重复的order_other_id 校验
        String orderOtherId = request.getOrderOtherId();
        Integer orderType = request.getOrderType();
        Map<String,Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("orderOtherId" , orderOtherId);
        mapQuery.put("orderType",orderType);
        List<IbdHeader> lists = poOrderService.getInbPoHeaderList(mapQuery);
        if(lists.size() > 0){
            throw new BizCheckedException("2020088");
        }

        poRequest.setItems(items);

        if(poRequest.getOrderType() == PoConstant.ORDER_TYPE_PO){
            CsiSupplier supplier = supplierService.getSupplier(poRequest.getSupplierCode().toString(),poRequest.getOwnerUid());
            if(supplier == null){
                throw new BizCheckedException("2021111");

            }
            poRequest.setSupplierName(supplier.getSupplierName());

        }

        Long orderId = poRpcService.insertOrder(poRequest);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("orderId",orderId);
        map.put("orderOtherId",request.getOrderOtherId());
        map.put("orderOtherRefId",request.getOrderOtherRefId());


        return ResUtils.getResponse(ResponseConstant.RES_CODE_1, ResponseConstant.RES_MSG_OK, map);
    }


    @POST
    @Path("addRelation")
    public BaseResponse addRelation() throws BizCheckedException,ParseException {
        Map<String, Object> request = RequestUtils.getRequest();
        List<LinkedHashMap> relationList = (List<LinkedHashMap>)request.get("relationList");
        Set<String> ibdOtherIds = new HashSet<String>();
        Set<ObdHeader> obdOtherIds = new HashSet<ObdHeader>();

        Map<String,Long> obdMap = new HashMap<String, Long>();
        Map<String,Long> ibdMap = new HashMap<String, Long>();
        for(LinkedHashMap map : relationList){
            IbdObdRelation ibdObdRelation = BeanMapTransUtils.map2Bean(map, IbdObdRelation.class);
            //查询ibd是否已下传
            String ibdOtherId = ibdObdRelation.getIbdOtherId();
            if(!ibdOtherIds.contains(ibdOtherId)){
                IbdHeader ibdHeader = poOrderService.getInbPoHeaderByOrderOtherId(ibdOtherId);
                if(ibdHeader == null){
                    SysLog sysLog = new SysLog();
                    //2770006表示关系表有缺失
                    sysLog.setLogCode("2770004");
                    sysLog.setLogType(SysLogConstant.LOG_TYPE_FRET);
                    sysLog.setLogMessage
                            ("没有找到ibd订单号为:"+ibdOtherId+"的订单!");
                    sysLogService.insertSysLog(sysLog);
                    throw new BizCheckedException("2770004","没有找到ibd订单号为:"+ibdOtherId+"的订单!");
                }
                ibdMap.put(ibdOtherId,ibdHeader.getOrderId());
                ibdOtherIds.add(ibdOtherId);
            }
            //查询Obd是否下传
            String obdOtherId = ibdObdRelation.getObdOtherId();

            if(!obdOtherIds.contains(obdOtherId)){
                ObdHeader obdHeader = soOrderService.getOutbSoHeaderByOrderOtherIdAndType(obdOtherId, SoConstant.ORDER_TYPE_DIRECT);

                if(obdHeader == null){
                    SysLog sysLog = new SysLog();
                    //2770006表示关系表有缺失
                    sysLog.setLogCode("2770005");
                    sysLog.setLogType(SysLogConstant.LOG_TYPE_FRET);
                    sysLog.setLogMessage("没有找到obd订单号为:"+obdOtherId+"的门店订货单");
                    throw new BizCheckedException("2770005","没有找到obd订单号为:"+obdOtherId+"的门店订货单");
                }
                obdMap.put(obdOtherId,obdHeader.getOrderId());
                obdOtherIds.add(obdHeader);
            }

            //updated by zhl
            String ibdDetailId = ibdObdRelation.getIbdDetailId();
            String obdDetailId = ibdObdRelation.getObdDetailId();

            //验证商品是否对应
            ObdDetail obdDetail = soOrderService.getObdDetailByOrderIdAndDetailOtherId(obdMap.get(obdOtherId),obdDetailId);
            com.lsh.wms.model.po.IbdDetail ibdDetail = poOrderService.getInbPoDetailByOrderIdAndDetailOtherId(ibdMap.get(ibdOtherId),ibdDetailId);
            if (!obdDetail.getSkuCode().equals(ibdDetail.getSkuCode())){
                throw new BizCheckedException("2770009");
            }

            poOrderService.insertIbdObdRelation(ibdObdRelation);

        }
        //查询该批关系表数据中门店订货信息是否完整
//        for(ObdHeader obdHeader : obdOtherIds){
//            //根据细单条目数量来判断是否完整
//            List<ObdDetail> obdDetails = soOrderService.getOutbSoDetailListByOrderId(obdHeader.getOrderId());
//            List<String> detailOtherIds = new ArrayList<String>();
//            for(ObdDetail obdDetail : obdDetails){
//                detailOtherIds.add(obdDetail.getDetailOtherId());
//            }
//            Map<String,Object> mapQuery = new HashMap<String, Object>();
//            mapQuery.put("obdOtherId",obdHeader.getOrderOtherId());
//            List<IbdObdRelation> ibdObdRelations = poOrderService.getIbdObdRelationList(mapQuery);
//            List<String> obddetailIds = new ArrayList<String>();
//            for(IbdObdRelation ibdObdRelation : ibdObdRelations){
//                obddetailIds.add(ibdObdRelation.getObdDetailId());
//            }
//
//            if(obdDetails.size() != ibdObdRelations.size()){
//                // TODO: 2016/10/18 对比每一条明细 确定哪条关系表缺失
//                detailOtherIds.removeAll(obddetailIds);
//                List<String> messages = new ArrayList<String>();
//                for(String detailOtherId : detailOtherIds){
//                    SysLog sysLog = new SysLog();
//                    //2770006表示关系表有缺失
//                    sysLog.setLogCode(2770006L);
//                    sysLog.setLogType(SysLogConstant.LOG_TYPE_FRET);
//                    String message = "obdOtherId:"+obdHeader.getOrderOtherRefId()
//                            + " obdDetailId :" + detailOtherId + "该obd订单明细没有对应的关系表记录!";
//                    sysLog.setLogMessage(message);
//                    messages.add(message);
//                    sysLogService.insertSysLog(sysLog);
//                }
//                throw new BizCheckedException("2770006",messages.toString());
//            }
//
//
//
//
//        }

        return ResUtils.getResponse(ResponseConstant.RES_CODE_1, ResponseConstant.RES_MSG_OK, null);
    }


    @POST
    @Path("bdSendIbd2Sap")
    public String bdSendIbd2Sap(CreateIbdHeader createIbdHeader) {
        CreateIbdHeader backData = wuMartSap.ibd2Sap(createIbdHeader);
        String mess = "";
        if(backData != null){
            mess =  wuMartSap.ibd2SapAccount(backData);
            if("E".equals(mess)){
                JsonUtils.EXCEPTION_ERROR("创建ibd成功,过账失败");
            }else{
                mess = "创建并过账成功";
            }
        }else{
            return JsonUtils.EXCEPTION_ERROR("创建ibd失败");
        }
        return JsonUtils.SUCCESS(mess);
    }

    @POST
    @Path("test")
    public String Test() {
//        StockRequest request = new StockRequest();
//        request.setPlant("DC37");
//        request.setMoveType("551");
//        request.setStorageLocation("0001");
//        List<StockItem> items = new ArrayList<StockItem>();
//        StockItem item = new StockItem();
//        item.setEntryQnt("5");
//        item.setMaterialNo("000000000000207274");
//        item.setEntryUom("EA");
//        items.add(item);
//        request.setItems(items);
//        return dataBackService.wmDataBackByPost(JSON.toJSONString(request), IntegrationConstan.URL_STOCKCHANGE,5);

        //return wuMartSap.ibd2SapBack(new CreateIbdHeader());

//        OutbSoHeader soHeader = soOrderService.getOutbSoHeaderByOrderId(214580861081622l);
//        //组装OBD反馈信息
//        ObdBackRequest request = new ObdBackRequest();
//        request.setPlant("DC37");//仓库
//        request.setBusinessId(soHeader.getOrderOtherId());
//        request.setOfcId(soHeader.getOrderOtherRefId());//参考单号
//        request.setAgPartnNumber(soHeader.getOrderUserCode());//用户
//
//        //查询明细。
//        List<OutbSoDetail> soDetails = soOrderService.getOutbSoDetailListByOrderId(214580861081622l);
//        List<ObdItem> items = new ArrayList<ObdItem>();
//
//
//        for (OutbSoDetail soDetail : soDetails){
//            ObdItem soItem = new ObdItem();
//            soItem.setMaterialNo(soDetail.getSkuCode());//skuCode
//            soItem.setMeasuringUnit("EA");
//            //soItem.setPrice(soDetail.getPrice());
//            //转化成ea
//            soItem.setQuantity(soDetail.getOrderQty().multiply(soDetail.getPackUnit()).setScale(3));
//            soItem.setSendQuantity(soDetail.getOrderQty());
//            //查询waveDetail找出实际出库的数量
//            items.add(soItem);
//        }
//        //查询waveDetail找出实际出库的数量
//        request.setItems(items);
//
//        return ibdBackService.createOrderByPost(request, IntegrationConstan.URL_OBD);


        ObdHeader soHeader = soOrderService.getOutbSoHeaderByOrderId(175578263067222L);
        //组装OBD反馈信息
        ObdOfcBackRequest request = new ObdOfcBackRequest();
        request.setDeliveryTime("2016-09-20");
        request.setObdCode(soHeader.getOrderId().toString());
        request.setSoCode(soHeader.getOrderOtherId());
        request.setWms(2);
        //查询明细。
        List<ObdDetail> soDetails = soOrderService.getOutbSoDetailListByOrderId(175578263067222L);
        List<ObdOfcItem> items = new ArrayList<ObdOfcItem>();

        for(ObdDetail detail : soDetails){
            ObdOfcItem item = new ObdOfcItem();
            item.setPackNum(detail.getPackUnit());
            item.setSkuQty(detail.getOrderQty());
            item.setSupplySkuCode(detail.getSkuCode());
            items.add(item);

        }
        request.setDetails(items);

        //return dataBackService.ofcDataBackByPost(JSON.toJSONString(request),IntegrationConstan.URL_LSHOFC_OBD);

        return "";

    }


    @GET
    @Path("sendSap")
    public String sendSap(){
        CreateIbdHeader header = new CreateIbdHeader();

//        Calendar calendar = Calendar.getInstance();
//
//        XMLGregorianCalendar date = new XMLGregorianCalendarImpl();
//        date.setYear(calendar.get(Calendar.YEAR));
//        date.setDay(calendar.get(Calendar.DATE));
//        date.setMonth(calendar.get(Calendar.MONTH));
//        header.setDeliveDate(date);
//        List<CreateIbdDetail> details = new ArrayList<CreateIbdDetail>();
//
//        CreateIbdDetail detail = new CreateIbdDetail();
//        detail.setDeliveQty(new BigDecimal("2.000"));
//        detail.setPoItme("10");
//        detail.setPoNumber("4500027501");
//        detail.setUnit("EA");
//        //detail.setMaterial("000000000000110978");
//        detail.setOrderType(4);
//        detail.setVendMat("222222");
//        details.add(detail);

//        CreateIbdDetail detail1 = new CreateIbdDetail();
//        detail1.setDeliveQty(new BigDecimal("1.000"));
//        detail1.setPoItme("10");
//        detail1.setPoNumber("4500027509");
//        detail1.setUnit("EA");
//        detail1.setMaterial("000000000000110978");
//        detail.setOrderType(1);
//        details.add(detail1);

//        CreateIbdDetail detail2 = new CreateIbdDetail();
//        detail2.setDeliveQty(new BigDecimal("1.000"));
//        detail2.setPoItme("20");
//        detail2.setPoNumber("180011198");
//        detail2.setUnit("EA");
//        //detail2.setMaterial("000000000000138248");
//        detail.setOrderType(1);
//        details.add(detail2);

//        header.setItems(details);
//        wuMartSap.ibd2Sap(header);
        wuMartSap.soObd2Sap(new CreateObdHeader());
        return "";
        //return wuMartSap.ibd2SapAccount(header,null);
        //wuMart.sendIbd(header);
        //wuMartSap.ibd2SapAccount(header,"");
    }

    @GET
    @Path("sendSapObd")
    public String sendSapObd() {
        CreateObdHeader header = new CreateObdHeader();
//        XMLGregorianCalendar date = new XMLGregorianCalendarImpl();
//        date.setYear(2016);
//        date.setDay(31);
//        date.setMonth(10);
//        header.setDueDate(date);

        List<CreateObdDetail> details = new ArrayList<CreateObdDetail>();
        CreateObdDetail detail1 = new CreateObdDetail();
        detail1.setRefDoc("4940031832");
        detail1.setRefItem("70");
        detail1.setDlvQty(new BigDecimal("1.00"));
        detail1.setSalesUnit("EA");
        detail1.setMaterial("000000000000110978");
        detail1.setOrderType(4);
        details.add(detail1);


        CreateObdDetail detail2 = new CreateObdDetail();
        detail2.setRefDoc("4940031832");
        detail2.setRefItem("80");
        detail2.setDlvQty(new BigDecimal("1.00"));
        detail2.setSalesUnit("EA");
        detail2.setMaterial("000000000000109787");
        detail2.setOrderType(4);
        details.add(detail2);
//
//        CreateObdDetail detail3 = new CreateObdDetail();
//        detail3.setRefDoc("4900011207");
//        detail3.setRefItem("30");
//        detail3.setDlvQty(new BigDecimal("500.00"));
//        detail3.setSalesUnit("EA");
//        detail3.setMaterial("138248");
//        details.add(detail3);

        header.setItems(details);

       // wuMart.sendObd(header);
        //wuMartSap.obd2SapAccount(header);
         wuMartSap.obd2Sap(header);
        //return wuMartSap.obd2SapAccount(header);
        return "success";

    }

    @POST
    @Path("seachSoBack")
    public String seachSoBackStatus() {
        Map<String, Object> request = RequestUtils.getRequest();
        String orderOtherId = (String) request.get("orderOtherId");
        IbdHeader ibdHeader = poOrderService.getInbPoHeaderByOrderOtherId(orderOtherId);
        List<com.lsh.wms.model.po.IbdDetail> ibdDetails = poOrderService.getInbPoDetailListByOrderId(ibdHeader.getOrderId());
        ibdHeader.setOrderDetails(ibdDetails);
        return JsonUtils.SUCCESS(ibdHeader);
    }


}
