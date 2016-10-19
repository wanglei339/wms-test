package com.lsh.wms.integration.service.ibd;

import com.alibaba.dubbo.common.json.ParseException;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.BeanMapTransUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.api.model.base.BaseResponse;
import com.lsh.wms.api.model.base.ResUtils;
import com.lsh.wms.api.model.base.ResponseConstant;
import com.lsh.wms.api.model.po.*;
import com.lsh.wms.api.model.so.ObdOfcBackRequest;
import com.lsh.wms.api.model.so.ObdOfcItem;
import com.lsh.wms.api.service.po.IIbdService;
import com.lsh.wms.api.service.po.IPoRpcService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.core.constant.SysLogConstant;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.po.PoOrderService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.system.SysLogService;
import com.lsh.wms.integration.service.back.DataBackService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.po.IbdHeader;
import com.lsh.wms.model.po.IbdObdRelation;
import com.lsh.wms.model.so.ObdDetail;
import com.lsh.wms.model.so.ObdHeader;
import com.lsh.wms.model.system.SysLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
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

    @POST
    @Path("add")
    public BaseResponse add(IbdRequest request) throws BizCheckedException{
        //数量做转换 ea转化为外包装箱数
        List<IbdDetail> details = request.getDetailList();

        List<IbdDetail> newDetails = new ArrayList<IbdDetail>();
        List<PoItem> items = new ArrayList<PoItem>();

        for(IbdDetail ibdDetail : details){
            List<BaseinfoItem>  baseinfoItemList= itemService.getItemsBySkuCode(request.getOwnerUid(),ibdDetail.getSkuCode());
            if(null != baseinfoItemList && baseinfoItemList.size()>=1){
                BaseinfoItem baseinfoItem = baseinfoItemList.get(baseinfoItemList.size()-1);
                String unitName = baseinfoItem.getUnitName().toUpperCase();
                //基础数据中维护基本单位名称,物美下传的packName为基本单位名称, 如果两边不相等,抛异常
                if(!unitName.equals(ibdDetail.getPackName().toUpperCase())){
                    throw new BizCheckedException("2770002");
                }
                //转换为系统内部的PackName和packUnit
                ibdDetail.setPackName(baseinfoItem.getPackName());
                ibdDetail.setPackUnit(baseinfoItem.getPackUnit());
                ibdDetail.setBarCode(baseinfoItem.getCode());
            }else{
                throw new BizCheckedException("2770001");
            }

            BigDecimal qty = ibdDetail.getOrderQty().divide(ibdDetail.getPackUnit(),2);
            ibdDetail.setOrderQty(qty);
            PoItem poItem = new PoItem();
            ObjUtils.bean2bean(ibdDetail,poItem);
            newDetails.add(ibdDetail);
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

        poRpcService.insertOrder(poRequest);
        return ResUtils.getResponse(ResponseConstant.RES_CODE_1, ResponseConstant.RES_MSG_OK, null);
    }


    @POST
    @Path("addRelation")
    public BaseResponse addRelation() throws BizCheckedException,ParseException {
        Map<String, Object> request = RequestUtils.getRequest();
        List<LinkedHashMap> relationList = (List<LinkedHashMap>)request.get("relationList");
        Set<String> ibdOrderIds = new HashSet<String>();
        Set<ObdHeader> obdOrderIds = new HashSet<ObdHeader>();
        for(LinkedHashMap map : relationList){
            IbdObdRelation ibdObdRelation = BeanMapTransUtils.map2Bean(map, IbdObdRelation.class);
            //查询ibd是否已下传
            String ibdOrderId = ibdObdRelation.getIbdOtherId();
            if(!ibdOrderIds.contains(ibdOrderId)){
                IbdHeader ibdHeader = poOrderService.getInbPoHeaderByOrderOtherId(ibdOrderId);
                if(ibdHeader == null){
                    SysLog sysLog = new SysLog();
                    //2770006表示关系表有缺失
                    sysLog.setLogCode(2770004L);
                    sysLog.setLogType(SysLogConstant.LOG_TYPE_FRET);
                    sysLog.setLogMessage
                            ("没有找到ibd订单号为:"+ibdOrderId+"的订单!");
                    sysLogService.insertSysLog(sysLog);
                    throw new BizCheckedException("2770004","没有找到ibd订单号为:"+ibdOrderId+"的订单!");
                }
                ibdOrderIds.add(ibdOrderId);
            }
            //查询Obd是否下传
            String obdOrderId = ibdObdRelation.getObdOtherId();
            if(!obdOrderIds.contains(obdOrderId)){
                ObdHeader obdHeader = soOrderService.getOutbSoHeaderByOrderOtherId(obdOrderId);
                if(obdHeader == null){
                    SysLog sysLog = new SysLog();
                    //2770006表示关系表有缺失
                    sysLog.setLogCode(2770005L);
                    sysLog.setLogType(SysLogConstant.LOG_TYPE_FRET);
                    sysLog.setLogMessage("没有找到obd订单号为:"+obdOrderId+"的门店订货单");
                    throw new BizCheckedException("2770005","没有找到obd订单号为:"+obdOrderId+"的门店订货单");
                }
                obdOrderIds.add(obdHeader);
            }
            poOrderService.insertIbdObdRelation(ibdObdRelation);

        }
        //查询该批关系表数据中门店订货信息是否完整
        for(ObdHeader obdHeader : obdOrderIds){
            //根据细单条目数量来判断是否完整
            List<ObdDetail> obdDetails = soOrderService.getOutbSoDetailListByOrderId(obdHeader.getOrderId());
            List<String> detailOtherIds = new ArrayList<String>();
            for(ObdDetail obdDetail : obdDetails){
                detailOtherIds.add(obdDetail.getDetailOtherId());
            }
            Map<String,Object> mapQuery = new HashMap<String, Object>();
            mapQuery.put("obdOrderId",obdHeader.getOrderOtherId());
            List<IbdObdRelation> ibdObdRelations = poOrderService.getIbdObdRelationList(mapQuery);
            List<String> obddetailIds = new ArrayList<String>();
            for(IbdObdRelation ibdObdRelation : ibdObdRelations){
                obddetailIds.add(ibdObdRelation.getObdDetailId());
            }

            if(obdDetails.size() != ibdObdRelations.size()){
                // TODO: 2016/10/18 对比每一条明细 确定哪条关系表缺失
                detailOtherIds.removeAll(obddetailIds);
                for(String detailOtherId : detailOtherIds){
                    SysLog sysLog = new SysLog();
                    //2770006表示关系表有缺失
                    sysLog.setLogCode(2770006L);
                    sysLog.setLogType(SysLogConstant.LOG_TYPE_FRET);
                    sysLog.setLogMessage
                            ("obdOrderId:"+obdHeader.getOrderOtherRefId()
                                    + " obdDetailId :" + detailOtherId + "该obd订单明细没有对应的关系表记录!");
                    sysLogService.insertSysLog(sysLog);
                }
                throw new BizCheckedException("关系表缺失");
            }




        }

        return ResUtils.getResponse(ResponseConstant.RES_CODE_1, ResponseConstant.RES_MSG_OK, null);
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


        ObdHeader soHeader = soOrderService.getOutbSoHeaderByOrderId(76978698850361L);
        //组装OBD反馈信息
        ObdOfcBackRequest request = new ObdOfcBackRequest();
        request.setDeliveryTime("2016-09-20");
        request.setObdCode(soHeader.getOrderId().toString());
        request.setSoCode(soHeader.getOrderOtherId());
        //查询明细。
        List<ObdDetail> soDetails = soOrderService.getOutbSoDetailListByOrderId(76978698850361L);
        List<ObdOfcItem> items = new ArrayList<ObdOfcItem>();

        for(ObdDetail detail : soDetails){
            ObdOfcItem item = new ObdOfcItem();
            item.setPackNum(detail.getPackUnit());
            item.setSkuQty(detail.getOrderQty());
            item.setSupplySkuCode(detail.getSkuCode());
            items.add(item);

        }
        request.setDetails(items);
        String url = "http://api.ofc.lsh123.com/ofc/api/order/obd/push";
        return dataBackService.ofcDataBackByPost(request,url);


    }







}
