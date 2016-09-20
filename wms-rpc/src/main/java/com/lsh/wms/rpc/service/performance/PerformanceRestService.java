package com.lsh.wms.rpc.service.performance;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.performance.IPerformanceRestService;
import com.lsh.wms.core.service.staff.StaffService;
import com.lsh.wms.model.baseinfo.BaseinfoStaffInfo;
import com.lsh.wms.model.system.SysUser;
import com.lsh.wms.rpc.service.system.SysUserRpcService;
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
//        List<Map<String,Object>> result = new ArrayList<Map<String, Object>>();
//        List<BaseinfoStaffInfo> staffList =  staffService.getStaffList(mapQuery);
//        for(BaseinfoStaffInfo staff : staffList) {
//            mapQuery.put("staffId", staff.getStaffId());
//            List<Map<String, Object>> stat = performanceRpcService.getPerformance(mapQuery);
//            result.addAll(stat);
//        }
        //将查询条件的员工工号staffNo 先查staff表转为staffId,根据staffId去SysUser表,根据的staffId转化为uid,然后去task表中查
        //staffNo->staffId
        String staffNo = (String) mapQuery.get("staffNo");
        List<Map<String, Object>> listTemp = new ArrayList<Map<String, Object>>();
        if (staffNo == null) {
            listTemp = performanceRpcService.getPerformance(mapQuery);
        } else {
            Map<String, Object> staffQuery = new HashMap<String, Object>();
            staffQuery.put("staffNo", staffNo);
            List<BaseinfoStaffInfo> staffList = staffService.getStaffList(staffQuery);
            List<Long> uidList = new ArrayList<Long>();
            if (staffList != null && !staffList.isEmpty()) {
                //staffId -> uidList
                Map<String, Object> userQuery = new HashMap<String, Object>();
                userQuery.put("staffId", staffList.get(0).getStaffId());
                List<SysUser> userList = sysUserRpcService.getSysUserList(userQuery);
                if (userList != null && !userList.isEmpty()) {
                    for (SysUser user : userList) {
                        uidList.add(user.getUid());
                    }
                    //拿到了uidList
                    mapQuery.put("uidList", uidList);
                    listTemp = performanceRpcService.getPerformance(mapQuery);
                }
            }
        }
        return JsonUtils.SUCCESS(listTemp);
    }

    @POST
    @Path("getPerformanceCount")
    public String getPerformanceCount(Map<String, Object> mapQuery) throws BizCheckedException {
        String staffNo = (String) mapQuery.get("staffNo");
        Integer count = 0;
        if (staffNo == null) {
            count = performanceRpcService.getPerformanceCount(mapQuery);
        } else {
            Map<String, Object> staffQuery = new HashMap<String, Object>();
            staffQuery.put("staffNo", staffNo);
            List<BaseinfoStaffInfo> staffList = staffService.getStaffList(staffQuery);
            List<Long> uidList = new ArrayList<Long>();
            if (staffList != null && !staffList.isEmpty()) {
                Map<String, Object> userQuery = new HashMap<String, Object>();
                userQuery.put("staffId", staffList.get(0).getStaffId());
                List<SysUser> userList = sysUserRpcService.getSysUserList(userQuery);
                if (userList != null && !userList.isEmpty()) {
                    for (SysUser user : userList) {
                        uidList.add(user.getUid());
                    }
                    mapQuery.put("uidList", uidList);
                    count = performanceRpcService.getPerformanceCount(mapQuery);
                }
            }
        }
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

}
