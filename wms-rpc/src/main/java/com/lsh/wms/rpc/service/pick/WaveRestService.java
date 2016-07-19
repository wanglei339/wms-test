package com.lsh.wms.rpc.service.pick;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.api.service.pick.IWaveRestService;
import com.lsh.wms.core.service.pick.PickWaveService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.model.pick.PickWaveHead;
import com.lsh.wms.model.pick.WaveRequest;
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
                              @QueryParam("uid") long iUid,
                              @QueryParam("uname") String iUName) {
        PickWaveHead head = new PickWaveHead();
        head.setWaveId(iWaveId);
        head.setReleaseUid(iUid);
        head.setReleaseUname(iUName);
        head.setReleaseAt(DateUtils.getCurrentSeconds());
        try{
            pickWaveService.update(head);
        }catch (Exception e){
            logger.error(e.getCause().getMessage());
            return JsonUtils.EXCEPTION_ERROR("Release failed");
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


}
