package com.lsh.wms.rpc.service.performance;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.performance.IPerformanceRestService;
import com.lsh.wms.core.service.staff.StaffService;
import com.lsh.wms.model.baseinfo.BaseinfoStaffInfo;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lixin-mac on 16/8/24.
 */
@Service(protocol = "rest")
@Path("performance")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class PerformanceRestService implements IPerformanceRestService {
    @Autowired
    private PerformanceRpcService performanceRpcService;

    @Autowired
    private StaffService staffService;

    @POST
    @Path("getPerformance")
    public String getPerformance(Map<String, Object> mapQuery) throws BizCheckedException {
//        List<Map<String,Object>> result = new ArrayList<Map<String, Object>>();
//        List<BaseinfoStaffInfo> staffList =  staffService.getStaffList(mapQuery);
//        for(BaseinfoStaffInfo staff : staffList) {
//            mapQuery.put("staffId", staff.getStaffId());
//            List<Map<String, Object>> stat = performanceRpcService.getPerformance(mapQuery);
//            result.addAll(stat);
//        }
        return JsonUtils.SUCCESS(performanceRpcService.getPerformance(mapQuery));
    }

    @POST
    @Path("getPerformanceCount")
    public String getPerformanceCount(Map<String, Object> mapQuery) throws BizCheckedException {
        return JsonUtils.SUCCESS(performanceRpcService.getPerformanceCount(mapQuery));
    }



    @POST
    @Path("getPerformaceDetaile")
    public String getPerformaceDetaile(Map<String, Object> mapQuery) {
        return JsonUtils.SUCCESS(performanceRpcService.getPerformaceDetaile(mapQuery));
    }


    @POST
    @Path("getTaskInfo")
    public String getTaskInfo(Map<String, Object> mapQuery){
        return JsonUtils.SUCCESS(performanceRpcService.getTaskInfo(mapQuery));
    }


}
