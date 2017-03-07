package com.lsh.wms.rpc.service.performance;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.api.service.performance.IPerformanceRestService;
import com.lsh.wms.core.service.staff.StaffService;
import com.lsh.wms.model.baseinfo.BaseinfoStaffInfo;
import com.lsh.wms.model.system.StaffPerformance;
import com.lsh.wms.model.system.SysUser;
import com.lsh.wms.rpc.service.system.SysUserRpcService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.HashMap;
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

    private static Logger logger = LoggerFactory.getLogger(PerformanceRestService.class);

    @Autowired
    private PerformanceRpcService performanceRpcService;

    @Autowired
    private StaffService staffService;
    @Autowired
    private SysUserRpcService sysUserRpcService;

    @POST
    @Path("getPerformance")
    public String getPerformance(Map<String, Object> mapQuery) throws BizCheckedException {

        List<StaffPerformance> listTemp = new ArrayList<StaffPerformance>();

        if(mapQuery.get("startDate") == null || mapQuery.get("endDate") == null){
            throw new BizCheckedException("1800001");//需指定查询区间
        }
        Long startDate = Long.parseLong(mapQuery.get("startDate").toString());
        Long endDate = Long.parseLong(mapQuery.get("endDate").toString());
        Long currentDate = DateUtils.getTodayBeginSeconds();
        if(startDate >= currentDate || endDate < currentDate){
        }else{
            throw new BizCheckedException("1800002");//当天数据与历史数据不能混合查询
        }

        listTemp = performanceRpcService.getPerformance(mapQuery);
        return JsonUtils.SUCCESS(listTemp);
    }

    @POST
    @Path("getPerformanceCount")
    public String getPerformanceCount(Map<String, Object> mapQuery) throws BizCheckedException {
        Integer count = 0;
        if(mapQuery.get("startDate") == null || mapQuery.get("endDate") == null){
            throw new BizCheckedException("1800001");//需指定查询区间
        }
        Long startDate = Long.parseLong(mapQuery.get("startDate").toString());
        Long endDate = Long.parseLong(mapQuery.get("endDate").toString());
        Long currentDate = DateUtils.getTodayBeginSeconds();
        if(startDate >= currentDate || endDate < currentDate){
            //开始时间大于等于当前时间,或结束时间小于当前时间
        }else{
            throw new BizCheckedException("1800002");//当天数据历史数据不能混合查询
        }
        count = performanceRpcService.getPerformanceCount(mapQuery);
        return JsonUtils.SUCCESS(count);
    }


    @POST
    @Path("getPerformaceDetaile")
    public String getPerformaceDetaile(Map<String, Object> mapQuery) {
        return JsonUtils.SUCCESS(performanceRpcService.getPerformaceDetaile(mapQuery));
    }


    @POST
    @Path("getTaskInfo")
    public String getTaskInfo(Map<String, Object> mapQuery) {
        return JsonUtils.SUCCESS(performanceRpcService.getTaskInfo(mapQuery));
    }

    @POST
    @Path("createPerformance")
    public String createPerformance(Map<String, Object> mapQuery) {
        logger.info("生成员工绩效记录");
        performanceRpcService.createPerformance(mapQuery);
        return JsonUtils.SUCCESS();
    }

}
