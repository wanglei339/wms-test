package com.lsh.wms.rpc.service.system;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.zone.IWorkZoneRestService;
import com.lsh.wms.core.service.zone.WorkZoneService;
import com.lsh.wms.model.zone.WorkZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Map;

/**
 * Created by zengwenjun on 16/12/5.
 */
@Service(protocol = "rest")
@Path("zone")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class WorkZoneRestService implements IWorkZoneRestService{
    private static Logger logger = LoggerFactory.getLogger(WorkZoneRestService.class);
    @Autowired
    private WorkZoneService workZoneService;

    @Path("getWorkZoneList")
    @POST
    public String getWorkZoneList(Map<String, Object> mapQuery) {
        return JsonUtils.SUCCESS(workZoneService.getWorkZoneList(mapQuery));
    }

    @Path("getWorkZoneCount")
    @POST
    public String getWorkZoneCount(Map<String, Object> mapQuery) {
        return JsonUtils.SUCCESS(workZoneService.getWorkZoneCount(mapQuery));
    }

    @Path("getWorkZone")
    @GET
    public String getWorkZone(@QueryParam("zoneId") long zoneId) {
        return JsonUtils.SUCCESS(workZoneService.getWorkZone(zoneId));
    }

    @Path("createWorkZone")
    @POST
    public String createWorkZone(WorkZone zone) {
        return JsonUtils.SUCCESS(workZoneService.insertWorkZone(zone));
    }

    @Path("updateWorkZone")
    @POST
    public String updateWorkZone(WorkZone zone) {
        try {
            workZoneService.updateWorkZone(zone);
        }catch (Exception e){
            e.printStackTrace();
            return JsonUtils.TOKEN_ERROR("更新错误");
        }
        return JsonUtils.SUCCESS();
    }

    @Path("deleteWorkZone")
    @GET
    public String deleteWorkZone(@QueryParam("zoneId") long zoneId) {
        try{
            workZoneService.deleteWorkZone(zoneId);
        }catch (Exception e){
            e.printStackTrace();
            return JsonUtils.TOKEN_ERROR("删除错误");
        }
        return JsonUtils.SUCCESS();
    }
}
