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
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
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
    public String scan(@QueryParam("locationId") long locationId) throws BizCheckedException {
        HttpSession session = RequestUtils.getSession();
        //get task  by containerId
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("locationId", locationId);
        //mapQuery.put("");
        List<TaskEntry> tasks = iTaskRpcService.getTaskHeadList(TaskConstant.TYPE_SHIP, mapQuery);
        if(tasks.size()!=1){
            throw new BizCheckedException("");
        }
        TaskInfo info = tasks.get(0).getTaskInfo();
        //if(info.)
        iTaskRpcService.assign(info.getTaskId(), Long.valueOf((String) session.getAttribute("uid")));
        return JsonUtils.SUCCESS();
    }

    @Path("scanContainer")
    @POST
    public String scanContainer(@QueryParam("containerId") long containerId) throws BizCheckedException {
        HttpSession session = RequestUtils.getSession();
        Long uid = Long.valueOf((String) session.getAttribute("uid"));
        List<WaveDetail> details = waveService.getDetailsByContainerId(containerId);
        for(WaveDetail d : details){
            d.setShipAt(DateUtils.getCurrentSeconds());
            d.setShipUid(uid);
        }
        if(details.size()==0){
            throw new BizCheckedException("2130001");
        }
        waveService.updateDetails(details);
        return JsonUtils.SUCCESS();
    }

    @Path("confirm")
    @POST
    public String confirm(@QueryParam("taskId") long taskId) throws BizCheckedException {
        List<WaveDetail> details = waveService.getDetailsByShipTaskId(taskId);
        for(WaveDetail d : details){
            if(d.getShipAt()==0){
                throw new BizCheckedException("2130002");
            }
        }
        iTaskRpcService.done(taskId);
        return JsonUtils.SUCCESS();
    }

    public String createTask(long locationId) throws BizCheckedException {
        List<BaseinfoLocation> locations = iLocationRpcService.getChildrenLocations(locationId);
        List<WaveDetail> details = waveService.getDetailsByLocationId(locationId);
        for (BaseinfoLocation location : locations) {
            List<WaveDetail> subDetails = waveService.getDetailsByLocationId(location.getLocationId());
            details.addAll(subDetails);
        }
        TaskEntry entry = new TaskEntry();
        TaskInfo info = new TaskInfo();
        info.setType(TaskConstant.TYPE_QC);
        info.setLocationId(locationId);
        entry.setTaskDetailList((List<Object>)(List<?>)details);
        entry.setTaskInfo(info);
        Long taskId = iTaskRpcService.create(TaskConstant.TYPE_SHIP, entry);
        return JsonUtils.SUCCESS();
    }
}
