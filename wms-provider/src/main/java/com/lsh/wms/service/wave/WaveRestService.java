package com.lsh.wms.service.wave;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.api.model.so.ObdBackRequest;
import com.lsh.wms.api.model.so.ObdItem;
import com.lsh.wms.api.model.so.SoItem;
import com.lsh.wms.api.model.so.SoRequest;
import com.lsh.wms.api.service.po.IIbdBackService;
import com.lsh.wms.api.service.wave.IWaveRestService;
import com.lsh.wms.core.constant.IntegrationConstan;
import com.lsh.wms.core.constant.WaveConstant;
import com.lsh.wms.core.service.inventory.InventoryRedisService;
import com.lsh.wms.core.service.location.BaseinfoLocationWarehouseService;
import com.lsh.wms.core.service.pick.PickModelService;
import com.lsh.wms.core.service.pick.PickZoneService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.core.service.wave.WaveTemplateService;
import com.lsh.wms.model.baseinfo.BaseinfoLocationWarehouse;
import com.lsh.wms.model.pick.*;
import com.lsh.wms.model.so.OutbSoDetail;
import com.lsh.wms.model.so.OutbSoHeader;
import com.lsh.wms.model.wave.WaveDetail;
import com.lsh.wms.model.wave.WaveHead;
import com.lsh.wms.model.wave.WaveRequest;
import com.lsh.wms.model.wave.WaveTemplate;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ResponseStatus;

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
    private PickZoneService zoneService;
    @Autowired
    private PickModelService modelService;
    @Autowired
    private WaveTemplateService waveTemplateService;
    @Autowired
    private WaveRpcService waveRpcService;
    @Autowired
    private InventoryRedisService inventoryRedisService;

    @Autowired
    private BaseinfoLocationWarehouseService baseinfoLocationWarehouseService;


    @Reference(check = false)
    private IIbdBackService ibdBackService;


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
            return JsonUtils.EXCEPTION_ERROR("not exist");
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
        Map<Long,Object> map = new HashMap<Long, Object>();
        for(WaveDetail detail : detailList){
            if ( detail.getQcExceptionDone() == 0){
                throw new BizCheckedException("2040014");
            }
            map.put(detail.getRefDetailId(),detail.getQcQty());
            orderIds.add(detail.getOrderId());
        }
        //发起来
        //必须保证数据只能发货一次,保证方法为生成发货单完成标示在行项目中,调用时将忽略已经标记生成的行项目
        //如此做将可以允许重复发货
        waveService.shipWave(head, detailList);
        //更新可用库存
        inventoryRedisService.onDelivery(detailList);
        //传送给外部系统,其实比较好的方式是扔出来到队列里,外部可以选择性处理.

        // TODO: 16/9/7 回传物美
        for(Long orderId : orderIds){
            OutbSoHeader soHeader = soOrderService.getOutbSoHeaderByOrderId(orderId);
            //组装OBD反馈信息
            ObdBackRequest request = new ObdBackRequest();
            BaseinfoLocationWarehouse warehouse = (BaseinfoLocationWarehouse) baseinfoLocationWarehouseService.getBaseinfoItemLocationModelById(1L);
            String warehouseName = warehouse.getWarehouseName();
            request.setPlant(warehouseName);//仓库
            request.setBusinessId(soHeader.getOrderOtherId());
            request.setOfcId(soHeader.getOrderOtherRefId());//参考单号
            request.setAgPartnNumber(soHeader.getOrderUser());//用户

            //查询明细。
            List<OutbSoDetail> soDetails = soOrderService.getOutbSoDetailListByOrderId(orderId);
            List<ObdItem> items = new ArrayList<ObdItem>();
            for (OutbSoDetail soDetail : soDetails){
                ObdItem soItem = new ObdItem();
                soItem.setMaterialNo(soDetail.getSkuCode());//skuCode
                soItem.setMeasuringUnit("EA");
                soItem.setPrice(soDetail.getPrice());
                //转化成ea
                soItem.setQuantity(soDetail.getOrderQty().multiply(soDetail.getPackUnit()).toString());
                //实际出库数量
                soItem.setSendQuantity((String) map.get(soDetail.getDetailOtherId()));

                //查询waveDetail找出实际出库的数量
                items.add(soItem);
            }
            //查询waveDetail找出实际出库的数量
            request.setItems(items);
            ibdBackService.createOrderByPost(request, IntegrationConstan.URL_OBD);
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
            logger.error(e.getCause().getMessage());
            return JsonUtils.EXCEPTION_ERROR("failed");
        }

        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("getPickzoneList")
    public String getPickzoneList(Map<String, Object> mapQuery) {
        return JsonUtils.SUCCESS(zoneService.getPickZoneList(mapQuery));
    }

    @POST
    @Path("getPickzoneCount")
    public String getPickzoneCount(Map<String, Object> mapQuery) {
        return JsonUtils.SUCCESS(zoneService.getPickZoneCount(mapQuery));
    }

    @GET
    @Path("getPickzone")
    public String getPickzone(@QueryParam("pickZoneId") long iPickZoneId) {
        return JsonUtils.SUCCESS(zoneService.getPickZone(iPickZoneId));
    }

    @POST
    @Path("createPickzone")
    public String createPickzone(PickZone zone) {
        return JsonUtils.SUCCESS(zoneService.insertPickZone(zone));
    }

    @POST
    @Path("updatePickzone")
    public String updatePickzone(PickZone zone) {
        try{
            zoneService.updatePickZone(zone);
        }catch (Exception e){
            logger.error(e.getCause().getMessage());
            return JsonUtils.EXCEPTION_ERROR("Update failed");
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
            logger.error(e.getCause().getMessage());
            return JsonUtils.EXCEPTION_ERROR("create failed");
        }
        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("updatePickModelTpl")
    public String updatePickModelTpl(PickModelTemplate tpl) {
        try{
            modelService.updatePickModelTpl(tpl);
        }catch (Exception e){
            logger.error(e.getCause().getMessage());
            return JsonUtils.EXCEPTION_ERROR("update failed");
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
        if(zoneService.getPickZone(zoneId) == null){
            throw new BizCheckedException("2040004");
        }
        try{
            modelService.createPickModel(model);
        }catch (Exception e ){
            logger.error(e.getCause().getMessage());
            return JsonUtils.EXCEPTION_ERROR("create failed");
        }
        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("updatePickModel")
    public String updatePickModel(PickModel model) {
        try{
            modelService.updatePickModel(model);
        }catch (Exception e){
            logger.error(e.getCause().getMessage());
            JsonUtils.EXCEPTION_ERROR("update failed");
        }
        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("deletePickModel")
    public String deletePickModel(long iPickModelId) {
        return null;
    }

    @GET
    @Path("getWaveDetailList")
    public String getWaveDetailList(@QueryParam("waveId") long iWaveId){
        return JsonUtils.SUCCESS(waveService.getDetailsByWaveId(iWaveId));
    }

    @GET
    @Path("getWaveQcExceptionList")
    public String getWaveQcExceptionList(@QueryParam("waveId") long iWaveId) {
        return JsonUtils.SUCCESS(waveService.getExceptionsByWaveId(iWaveId));
    }

}
