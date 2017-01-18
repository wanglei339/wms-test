package com.lsh.wms.service.wave;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.model.wumart.CreateObdDetail;
import com.lsh.wms.api.model.wumart.CreateObdHeader;
import com.lsh.wms.api.service.back.IDataBackService;
import com.lsh.wms.api.service.wave.IWaveRestService;
import com.lsh.wms.api.service.wumart.IWuMart;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.constant.WaveConstant;
import com.lsh.wms.core.service.location.BaseinfoLocationWarehouseService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.pick.PickModelService;
import com.lsh.wms.core.service.zone.WorkZoneService;
import com.lsh.wms.core.service.so.SoDeliveryService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.core.service.wave.WaveTemplateService;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.pick.*;
import com.lsh.wms.model.so.ObdDetail;
import com.lsh.wms.model.so.ObdHeader;
import com.lsh.wms.model.so.OutbDeliveryDetail;
import com.lsh.wms.model.wave.WaveDetail;
import com.lsh.wms.model.wave.WaveHead;
import com.lsh.wms.model.wave.WaveRequest;
import com.lsh.wms.model.wave.WaveTemplate;
import com.lsh.wms.model.zone.WorkZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by zengwenjun on 16/7/15.
 */


@Service(protocol = "rest")
@Path("wave")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class WaveRestService implements IWaveRestService {
    private static final Logger logger = LoggerFactory.getLogger(WaveRestService.class);

    @Autowired
    private WaveService waveService;
    @Autowired
    private SoOrderService soOrderService;
    @Autowired
    private WorkZoneService workZoneService;
    @Autowired
    private PickModelService modelService;
    @Autowired
    private WaveTemplateService waveTemplateService;
    @Autowired
    private WaveRpcService waveRpcService;

    @Autowired
    private BaseinfoLocationWarehouseService baseinfoLocationWarehouseService;

    @Reference
    private IDataBackService dataBackService;

    @Autowired
    private SoDeliveryService soDeliveryService;

    @Autowired
    private WaveGenerator waveGenerator;

    @Autowired
    private LocationService locationService;

//    @Reference
//    private IWuMartSap wuMartSap;

    @Reference
    private IWuMart wuMart;

    @POST
    @Path("getList")
    public String getList(Map<String, Object> mapQuery) {
        return JsonUtils.SUCCESS(waveService.getWaveList(mapQuery));
    }

    @POST
    @Path("getListCount")
    public String  getListCount(Map<String, Object> mapQuery) {
        return JsonUtils.SUCCESS(waveService.getWaveCount(mapQuery));
    }

    @GET
    @Path("getWave")
    public String getWave(@QueryParam("waveId") long iWaveId) {
        WaveHead wave = waveService.getWave(iWaveId);
        if(wave == null){
            return JsonUtils.TOKEN_ERROR("not exist");
        }
        return JsonUtils.SUCCESS(wave);
    }

    @GET
    @Path("getWaveOrders")
    public String getWaveOrders(@QueryParam("waveId") long iWaveId) {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("waveId", iWaveId);
        return JsonUtils.SUCCESS(soOrderService.getOutbSoHeaderList(mapQuery));
    }

    @GET
    @Path("shipWave")
    public String shipWave(@QueryParam("waveId") long iWaveId,
                           @QueryParam("uid") long iUid) throws BizCheckedException {
        WaveHead head = waveService.getWave(iWaveId);
        if(head==null){
            throw new BizCheckedException("2040001");
        }
        if(head.getStatus() == WaveConstant.STATUS_RELEASE_SUCC
                || head.getStatus() == WaveConstant.STATUS_SUCC
                || head.getStatus() == WaveConstant.STATUS_PICK_SUCC
                || head.getStatus() == WaveConstant.STATUS_QC_SUCC
                ){
            //可以发
        }else{
            throw new BizCheckedException("2040013");
        }
        List<WaveDetail> detailList = waveService.getDetailsByWaveId(iWaveId);
        Set<Long> orderIds = new HashSet<Long>();
        //将orderId取出 放入set集合中
        for(WaveDetail detail : detailList){
            if ( detail.getQcExceptionDone() == 0){
                throw new BizCheckedException("2040014");
            }
            orderIds.add(detail.getOrderId());
        }
        //发起来
        //必须保证数据只能发货一次,保证方法为生成发货单完成标示在行项目中,调用时将忽略已经标记生成的行项目
        //如此做将可以允许重复发货
//        waveService.shipWave(head, detailList);
//        //更新可用库存,直流忽略    直流不用此发车了
//        if(!head.getStatus().equals(SoConstant.ORDER_TYPE_DIRECT)) {
//            inventoryRedisService.onDelivery(detailList);
//        }
        //传送给外部系统,其实比较好的方式是扔出来到队列里,外部可以选择性处理.

        // TODO: 16/9/7 回传物美 根据货主区分回传obd
        for(Long orderId : orderIds){
            ObdHeader obdHeader = soOrderService.getOutbSoHeaderByOrderId(orderId);
            //查询明细。
            List<ObdDetail> obdDetails = soOrderService.getOutbSoDetailListByOrderId(orderId);
            // TODO: 2016/9/23  组装OBD反馈信息 根据货主区分回传lsh或物美
            if(obdHeader.getOwnerUid() == 1){
//                ObdBackRequest request = new ObdBackRequest();
//                BaseinfoLocationWarehouse warehouse = (BaseinfoLocationWarehouse) baseinfoLocationWarehouseService.getBaseinfoItemLocationModelById(0L);
//                String warehouseName = warehouse.getWarehouseName();
//                request.setPlant(warehouseName);//仓库
//                request.setBusinessId(obdHeader.getOrderOtherId());
//                request.setOfcId(obdHeader.getOrderOtherRefId());//参考单号
//                request.setAgPartnNumber(obdHeader.getOrderUser());//用户

                // TODO: 2016/11/3 回传WMSAP 组装信息
                CreateObdHeader createObdHeader = new CreateObdHeader();

                List<CreateObdDetail> details = new ArrayList<CreateObdDetail>();
                for (ObdDetail obdDetail : obdDetails){
                    CreateObdDetail detail = new CreateObdDetail();

                    // TODO: 16/9/18 目前根据orderId与itemId来确定一条发货单。
                    OutbDeliveryDetail outbDeliveryDetail = soDeliveryService.getOutbDeliveryDetail(obdDetail.getOrderId(),obdDetail.getItemId());
                    //实际出库数量
                    detail.setDlvQty(outbDeliveryDetail.getDeliveryNum().setScale(2, BigDecimal.ROUND_HALF_UP));
                    detail.setMaterial(obdDetail.getSkuCode());
                    detail.setRefDoc(obdHeader.getOrderOtherId());
                    detail.setRefItem(obdDetail.getDetailOtherId());
                    detail.setSalesUnit(obdDetail.getUnitName());
                    detail.setOrderType(obdHeader.getOrderType());
                    details.add(detail);
                }
                createObdHeader.setItems(details);
                //wuMartSap.obd2Sap(createObdHeader);
                //wuMart.sendObd(createObdHeader);
                //wuMart.sendSo2Sap(createObdHeader);

                // TODO: 2016/11/3 回传WMSAP 组装信息
            }

        }

        return JsonUtils.SUCCESS();
    }

    @GET
    @Path("releaseWave")
    public String releaseWave(@QueryParam("waveId") long iWaveId,
                              @QueryParam("uid") long iUid) throws BizCheckedException {
        waveRpcService.releaseWave(iWaveId, iUid);
        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("createWave")
    public String createWave(WaveRequest request) throws BizCheckedException {
        final Long waveId = waveRpcService.createWave(request);
        return JsonUtils.SUCCESS(new HashMap<String, Object>() {
            {
                put("waveId", waveId);
            }
        });
    }

    @GET
    @Path("setStatus")
    public String setStatus(@QueryParam("waveId") long iWaveId,@QueryParam("status") int iStatus) {
        try{
            waveService.setStatus(iWaveId,iStatus);

        }catch (Exception e){
            logger.error(e.getCause()!=null ? e.getCause().getMessage():e.getMessage());
            return JsonUtils.TOKEN_ERROR("failed");
        }

        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("getPickModelTplList")
    public String getPickModelTplList(Map<String, Object> mapQuery) {
        return JsonUtils.SUCCESS(modelService.getPickModelTemplateList(mapQuery));
    }

    @POST
    @Path("getPickModelTplCount")
    public String getPickModelTplCount(Map<String, Object> mapQuery) {
        return JsonUtils.SUCCESS(modelService.getPickModelTemplateCount(mapQuery));
    }

    @GET
    @Path("getPickModelTpl")
    public String getPickModelTpl(@QueryParam("pickModelTemplateId") long iPickModelTplId) {
        return JsonUtils.SUCCESS(modelService.getPickModelTemplate(iPickModelTplId));
    }

    @POST
    @Path("createPickModelTpl")
    public String createPickModelTpl(PickModelTemplate tpl) {
        try{
            modelService.createPickModelTemplate(tpl);
        }catch (Exception e){
            logger.error(e.getCause()!=null ? e.getCause().getMessage():e.getMessage());
            return JsonUtils.TOKEN_ERROR("create failed");
        }
        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("updatePickModelTpl")
    public String updatePickModelTpl(PickModelTemplate tpl) {
        try{
            modelService.updatePickModelTpl(tpl);
        }catch (Exception e){
            logger.error(e.getCause()!=null ? e.getCause().getMessage():e.getMessage());
            return JsonUtils.TOKEN_ERROR("update failed");
        }
        return JsonUtils.SUCCESS();
    }

    @GET
    @Path("getPickModelList")
    public String getPickModelList(@QueryParam("pickModelTemplateId") long iPickModelTplId) {
        return JsonUtils.SUCCESS(modelService.getPickModelsByTplId(iPickModelTplId));
    }

    @GET
    @Path("getPickModel")
    public String getPickModel(@QueryParam("pickModelId") long iPickModelId) {
        return JsonUtils.SUCCESS(modelService.getPickModel(iPickModelId));
    }

    @POST
    @Path("createPickModel")
    public String createPickModel(PickModel model) throws BizCheckedException {
        //检查该PickZone是否存在
        long zoneId = model.getPickZoneId();
        if(workZoneService.getWorkZone(zoneId) == null){
            throw new BizCheckedException("2040004");
        }
        try{
            modelService.createPickModel(model);
        }catch (Exception e ){
            logger.error(e.getCause()!=null ? e.getCause().getMessage():e.getMessage());
            return JsonUtils.TOKEN_ERROR("create failed");
        }
        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("updatePickModel")
    public String updatePickModel(PickModel model) {
        try{
            // 根据拣货分区设置pickType
            Long pickZoneId = model.getPickZoneId();
            WorkZone workZone = workZoneService.getWorkZone(pickZoneId);
            String locations = workZone.getLocations();
            String[] locationIds = locations.split(",");
            if (locationIds.length > 0) {
                Long locationId = Long.valueOf(locationIds[0]);
                BaseinfoLocation location = locationService.getLocation(locationId);
                if (null != location) {
                    // 暂时写死了
                    if (location.getType().equals(LocationConstant.LOFTS)) {
                        model.setPickType(2L);
                    } else if (location.getType().equals(LocationConstant.SPLIT_AREA)) {
                        model.setPickType(3L);
                    } else  {
                        model.setPickType(1L);
                    }
                }
            }
            modelService.updatePickModel(model);
        }catch (Exception e){
            logger.error(e.getCause()!=null ? e.getCause().getMessage():e.getMessage());
            JsonUtils.TOKEN_ERROR("update failed");
        }
        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("deletePickModel")
    public String deletePickModel(long iPickModelId) {
        return null;
    }

    @POST
    @Path("getWaveDetailList")
    public String getWaveDetailList(HashMap<String, Object> mapQuery){
        return JsonUtils.SUCCESS(waveService.getDetailsSpecial(mapQuery));
    }

    @GET
    @Path("getWaveDetailListByCollectLocation")
    public String getWaveDetailListByLocation(@QueryParam("collectLocationCode") String collectLocationCode){
        BaseinfoLocation location = locationService.getLocationByCode(collectLocationCode);
        if(location == null){
            return JsonUtils.TOKEN_ERROR("集货道不存在");
        }
        return JsonUtils.SUCCESS(waveService.getDetailsByCollectionLocation(location));
    }

    @GET
    @Path("getWaveDetailByTuDetailId")
    public String getWaveDetailByTuDetailId(@QueryParam("tuDetailId")Long tuDetailId) throws BizCheckedException{
        if (null == tuDetailId){
            throw new BizCheckedException("2990056");
        }
        return JsonUtils.SUCCESS(waveService.getWaveDetailByTuDetailId(tuDetailId));
    }

    @GET
    @Path("getWaveQcExceptionList")
    public String getWaveQcExceptionList(@QueryParam("waveId") long iWaveId) {
        return JsonUtils.SUCCESS(waveService.getExceptionsByWaveId(iWaveId));
    }

    @POST
    @Path("getWaveTemplateList")
    public String getWaveTemplateList(Map<String, Object> mapQuery){
        return JsonUtils.SUCCESS(waveTemplateService.getWaveTemplateList(mapQuery));
    }

    @POST
    @Path("getWaveTemplateCount")
    public String getWaveTemplateCount(Map<String, Object> mapQuery){
        return JsonUtils.SUCCESS(waveTemplateService.getWaveTemplateCount(mapQuery));
    }

    @GET
    @Path("getWaveTemplate")
    public String getWaveTemplate(@QueryParam("waveTemplateId") long waveTemplateId ){
        return JsonUtils.SUCCESS(waveTemplateService.getWaveTemplate(waveTemplateId));
    }

    @POST
    @Path("createWaveTemplate")
    public String createWaveTemplate(WaveTemplate tpl) throws BizCheckedException {
        if (modelService.getPickModelTemplate(tpl.getPickModelTemplateId()) == null) {
            throw new BizCheckedException("2040015");
        }
        if (locationService.getLocation(tpl.getCollectLocations()) == null
                || locationService.getLocation(tpl.getCollectLocations()).getType() != LocationConstant.COLLECTION_ROAD_GROUP) {
            throw new BizCheckedException("2040016");
        }
        try {
            waveTemplateService.createWaveTemplate(tpl);
        } catch (Exception e) {
            logger.error(e.getCause()!=null ? e.getCause().getMessage():e.getMessage());
            throw new BizCheckedException("2040017");
        }
        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("updateWaveTemplate")
    public String updateWaveTemplate(WaveTemplate tpl) throws BizCheckedException {
        if (modelService.getPickModelTemplate(tpl.getPickModelTemplateId()) == null) {
            throw new BizCheckedException("2040015");
        }
        if (locationService.getLocation(tpl.getCollectLocations()) == null
                || locationService.getLocation(tpl.getCollectLocations()).getType() != LocationConstant.COLLECTION_ROAD_GROUP) {
            throw new BizCheckedException("2040016");
        }
        try {
            waveTemplateService.updateWaveTemplate(tpl);
        } catch (Exception e) {
            logger.error(e.getCause()!=null ? e.getCause().getMessage():e.getMessage());
            throw new BizCheckedException("2040017");
        }
        return JsonUtils.SUCCESS();
    }

    public static void main(String[] args) {
        throw new BizCheckedException("2041001","1111111");
    }

    @GET
    @Path("runWaveGenerator")
    public String runWaveGenerator() throws BizCheckedException{
        waveGenerator.autoCluster();
        return JsonUtils.SUCCESS();
    }

    @GET
    @Path("getWavePreviewList")
    public String getWavePreviewList(){
        return JsonUtils.SUCCESS(waveGenerator.getWavePreviewList());
    }

    @POST
    @Path("createWaveByPreview")
    public String createWaveByPreview(Map<String, Object> mapData) throws BizCheckedException{
        List<Long> orderIds = waveGenerator.getOrderIdsByWavePreviewId(mapData.get("wavePreviewId").toString());
        Long waveTemplateId = waveGenerator.getWaveTemplateIdByWavePreviewId(mapData.get("wavePreviewId").toString());

        if(orderIds == null || waveTemplateId == null){
            throw new BizCheckedException("2040018");
        }
        List<Map> orders = new LinkedList<Map>();
        for(Long orderId : orderIds){
            Map<String, Object> order = new HashMap<String, Object>();
            order.put("orderId", orderId);
            orders.add(order);
        }
        WaveRequest request = new WaveRequest(orders, Long.valueOf(WaveConstant.STATUS_NEW), "", "PREVIEW", waveTemplateId, 1L);
        waveRpcService.createWave(request);
        try{
            //存储redis说这个哥们已经释放了

        }catch (Exception e){

        }
        return JsonUtils.SUCCESS();
    }


}
