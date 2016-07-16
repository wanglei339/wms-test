package com.lsh.wms.rpc.service.pick;

import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.pick.IWaveRestService;
import com.lsh.wms.core.service.pick.PickWaveService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.model.pick.PickWaveHead;
import com.lsh.wms.model.so.OutbSoHeader;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zengwenjun on 16/7/15.
 */
public class WaveRestService implements IWaveRestService {

    @Autowired
    private PickWaveService pickWaveService;
    @Autowired
    private SoOrderService soOrderService;

    @GET
    @Path("getList")
    public String getList(Map<String, Object> mapQuery) {
        return JsonUtils.SUCCESS(pickWaveService.getWaveList(mapQuery));
    }

    @GET
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

    @POST
    @Path("releaseWave")
    public String releaseWave(@QueryParam("waveId") long iWaveId,
                              @QueryParam("uid") long iUid,
                              @QueryParam("uname") String iUName) {
        return null;
    }
}
