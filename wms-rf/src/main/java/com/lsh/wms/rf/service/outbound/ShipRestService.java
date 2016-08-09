package com.lsh.wms.rf.service.outbound;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.api.service.location.ILocationRpcService;
import com.lsh.wms.api.service.pick.IShipRestService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.wave.WaveDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by zengwenjun on 16/7/30.
 */
@Service(protocol = "rest")
@Path("outbound/ship")
@Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class ShipRestService implements IShipRestService {
    private static Logger logger = LoggerFactory.getLogger(ShipRestService.class);

    @Reference
    ITaskRpcService iTaskRpcService;
    @Autowired
    WaveService waveService;
    @Reference
    ILocationRpcService iLocationRpcService;

    @Path("scan")
    @POST
    public String scan() throws BizCheckedException {
        Map<String, Object> mapRequest = RequestUtils.getRequest();
        Long locationId = Long.valueOf(mapRequest.get("locationId").toString());
        HttpSession session = RequestUtils.getSession();
        //get task  by containerId
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("locationId", locationId);
        //mapQuery.put("");
        List<TaskEntry> tasks = iTaskRpcService.getTaskHeadList(TaskConstant.TYPE_SHIP, mapQuery);
        if(tasks.size()!=1){
            throw new BizCheckedException("2130008");
        }
        TaskInfo info = tasks.get(0).getTaskInfo();
        //if(info.)
        iTaskRpcService.assign(info.getTaskId(), 0L);//Long.valueOf((String) session.getAttribute("uid")));
        return JsonUtils.SUCCESS();
    }

    @Path("scanContainer")
    @POST
    public String scanContainer() throws BizCheckedException {
        Map<String, Object> mapRequest = RequestUtils.getRequest();
        Long containerId = Long.valueOf(mapRequest.get("containerId").toString());
        HttpSession session = RequestUtils.getSession();
        Long uid = 0L;//Long.valueOf((String) session.getAttribute("uid"));
        List<WaveDetail> details = waveService.getDetailsByContainerId(containerId);
        for(WaveDetail d : details){
            d.setShipAt(DateUtils.getCurrentSeconds());
            d.setShipUid(uid);
        }
        if(details.size()==0){
            throw new BizCheckedException("2130001");
        }
        waveService.updateDetails(details);
        //get task  by containerId
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("containerId", containerId);
        //mapQuery.put("");
        List<TaskEntry> tasks = iTaskRpcService.getTaskHeadList(TaskConstant.TYPE_SHIP, mapQuery);
        if(tasks.size()>1){
            throw new BizCheckedException("2130008");
        }
        if(tasks.size()==0){
            throw new BizCheckedException("2130009");
        }
        TaskInfo info = tasks.get(0).getTaskInfo();
        iTaskRpcService.assign(info.getTaskId(), 123123L);
        /*
        获取发货码头
         */
        Map<String, Object> rstMap = new HashMap<String, Object>();
        rstMap.put("dockName", "TESTDOCK");
        return JsonUtils.SUCCESS(rstMap);
    }

    @Path("confirm")
    @POST
    public String confirm() throws BizCheckedException {
        Map<String, Object> mapRequest = RequestUtils.getRequest();
        Long locationId = Long.valueOf(mapRequest.get("locationId").toString());
        List<WaveDetail> details = waveService.getDetailsByLocationId(locationId);
        for(WaveDetail d : details){
            if(d.getShipAt()==0){
                throw new BizCheckedException("2130002");
            }
        }
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("locationId", locationId);
        List<TaskEntry> tasks = iTaskRpcService.getTaskHeadList(TaskConstant.TYPE_SHIP, mapQuery);
        if(tasks.size()!=1){
            throw new BizCheckedException("2130008");
        }
        iTaskRpcService.done(tasks.get(0).getTaskInfo().getTaskId());
        return JsonUtils.SUCCESS();
    }

    @Path("createTask")
    @POST
    public String createTask() throws BizCheckedException {
        Map<String, Object> mapRequest = RequestUtils.getRequest();
        Long containerId = Long.valueOf(mapRequest.get("containerId").toString());
        List<WaveDetail> details = waveService.getDetailsByContainerId(containerId);
        for(WaveDetail detail : details){
            if(detail.getQcExceptionDone()==0){
                throw new BizCheckedException("2130005");
            }
        }
        if(details.size()==0){
            throw new BizCheckedException("2130007");
        }
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("containerId", containerId);
        final List<TaskEntry> taskList = iTaskRpcService.getTaskHeadList(TaskConstant.TYPE_SHIP, mapQuery);
        if(taskList.size()>0){
            throw new BizCheckedException("2130003");
        }
        TaskEntry entry = new TaskEntry();
        TaskInfo info = new TaskInfo();
        info.setType(TaskConstant.TYPE_SHIP);
        info.setContainerId(containerId);
        info.setLocationId(details.get(0).getRealCollectLocation());
        entry.setTaskDetailList((List<Object>)(List<?>)details);
        entry.setTaskInfo(info);
        Long taskId = iTaskRpcService.create(TaskConstant.TYPE_SHIP, entry);
        return JsonUtils.SUCCESS(new HashMap<String, Boolean>() {
            {
                put("response", true);
            }
        });
    }

    /*
    @Path("createTask")
    @POST
    public String createTask() throws BizCheckedException {
        Map<String, Object> mapRequest = RequestUtils.getRequest();
        Long locationId = Long.valueOf(mapRequest.get("locationId").toString());
        if(iLocationRpcService.getLocation(locationId)==null){
            throw new BizCheckedException("2130006");
        }
        List<BaseinfoLocation> locations = iLocationRpcService.getChildrenLocations(locationId);
        List<WaveDetail> details = waveService.getDetailsByLocationId(locationId);
        if(locations != null) {
            for (BaseinfoLocation location : locations) {
                List<WaveDetail> subDetails = waveService.getDetailsByLocationId(location.getLocationId());
                details.addAll(subDetails);
            }
        }
        for(WaveDetail detail : details){
            if(detail.getQcExceptionDone()==0){
                throw new BizCheckedException("2130005");
            }
        }
        if(details.size()==0){
            throw new BizCheckedException("2130007");
        }
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("locationId", locationId);
        final List<TaskEntry> taskList = iTaskRpcService.getTaskHeadList(TaskConstant.TYPE_SHIP, mapQuery);
        if(taskList.size()>0){
            throw new BizCheckedException("2130003");
        }
        TaskEntry entry = new TaskEntry();
        TaskInfo info = new TaskInfo();
        info.setType(TaskConstant.TYPE_SHIP);
        info.setLocationId(locationId);
        entry.setTaskDetailList((List<Object>)(List<?>)details);
        entry.setTaskInfo(info);
        Long taskId = iTaskRpcService.create(TaskConstant.TYPE_SHIP, entry);
        return JsonUtils.SUCCESS();
    }
    */
}
