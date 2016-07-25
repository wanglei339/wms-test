package com.lsh.wms.service.pick;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.api.service.pick.IWaveRestService;
import com.lsh.wms.core.constant.WaveConstant;
import com.lsh.wms.core.service.pick.PickModelService;
import com.lsh.wms.core.service.pick.PickWaveService;
import com.lsh.wms.core.service.pick.PickZoneService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.model.pick.*;
import com.lsh.wms.service.pick.wave.WaveCore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private PickWaveService pickWaveService;
    @Autowired
    private SoOrderService soOrderService;
    @Autowired
    private PickZoneService zoneService;
    @Autowired
    private PickModelService modelService;
    @Autowired
    private WaveCore core;


    @POST
    @Path("getList")
    public String getList(Map<String, Object> mapQuery) {
        return JsonUtils.SUCCESS(pickWaveService.getWaveList(mapQuery));
    }

    @POST
    @Path("getListCount")
    public String  getListCount(Map<String, Object> mapQuery) {
        return JsonUtils.SUCCESS(pickWaveService.getWaveCount(mapQuery));
    }

    @GET
    @Path("getWave")
    public String getWave(@QueryParam("waveId") long iWaveId) {
        PickWaveHead wave = pickWaveService.getWave(iWaveId);
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
    @Path("releaseWave")
    public String releaseWave(@QueryParam("waveId") long iWaveId,
                              @QueryParam("uid") long iUid) throws BizCheckedException {
        PickWaveHead head = pickWaveService.getWave(iWaveId);
        if(head==null){
            throw new BizCheckedException("2040001");
        }
        if(head.getStatus() == WaveConstant.STATUS_NEW
                || head.getStatus() == WaveConstant.STATUS_RELEASE_FAIL
                || (head.getStatus() == WaveConstant.STATUS_RELEASE_START && DateUtils.getCurrentSeconds()-head.getReleaseAt() > 300))
        {

        } else {
            throw new BizCheckedException("2040002");
        }
        head.setReleaseUid(iUid);
        head.setReleaseAt(DateUtils.getCurrentSeconds());
        head.setStatus((long) WaveConstant.STATUS_RELEASE_START);
        try{
            pickWaveService.update(head);
        }catch (Exception e){
            throw  new BizCheckedException("2040003");
        }
        boolean bNeedRollBack = true;
        try {
            int ret = core.release(iWaveId);
            if ( ret == 0 ) {
                bNeedRollBack = false;
            }else{
                logger.error("wave release fail, ret %d", ret);
            }
        }catch (BizCheckedException e){
            logger.error("Wave release fail, wave id %d msg %s", iWaveId, e.getMessage());
            throw e;
        } finally {
            if(bNeedRollBack) {
                head.setStatus((long) WaveConstant.STATUS_RELEASE_FAIL);
                pickWaveService.update(head);
            }
        }
        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("createWave")
    public String createWave(WaveRequest request) {
        PickWaveHead pickWaveHead = new PickWaveHead();
        ObjUtils.bean2bean(request,pickWaveHead);
        List<Long> orderIds = request.getOrderIds();
        try{
            pickWaveService.createWave(pickWaveHead,orderIds);
        }catch (Exception e){
            logger.error(e.getCause().getMessage());
            return JsonUtils.EXCEPTION_ERROR("Create failed");
        }


        return JsonUtils.SUCCESS();
    }
    @GET
    @Path("setStatus")
    public String setStatus(@QueryParam("waveId") long iWaveId,@QueryParam("status") int iStatus) {
        try{
            pickWaveService.setStatus(iWaveId,iStatus);

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
    public String getPickModelList(@QueryParam("getPickModelList") long iPickModelTplId) {
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


}
