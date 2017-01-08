package com.lsh.wms.service.outbound;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.pick.IPCPickRestService;
import com.lsh.wms.api.service.pick.IPCPickRpcService;
import com.lsh.wms.api.service.pick.IPickRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.utils.PackUtil;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.core.service.zone.WorkZoneService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.model.pick.PickTaskHead;
import com.lsh.wms.model.so.ObdHeader;
import com.lsh.wms.model.zone.WorkZone;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.wave.WaveDetail;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by zengwenjun on 16/7/15.
 */
@Service(protocol = "rest")
@Path("outbound/pick")
public class PickRestService implements IPCPickRestService {
    @Reference
    ITaskRpcService iTaskRpcService;
    @Autowired
    LocationService locationService;
    @Autowired
    SoOrderService orderService;
    @Autowired
    WorkZoneService workZoneService;
    @Autowired
    WaveService waveService;
    @Reference
    IPCPickRpcService ipcPickRpcService;


    @POST
    @Path("getPickTaskInfo")
    public String getPickTaskInfo(Map<String, Object> mapInput) {
        long waveId = Long.valueOf(mapInput.get("waveId").toString());
        List pickTaskIds = (List) mapInput.get("pickTaskIds");
        if (waveId != 0) {
            Map<String, Object> mapQuery = new HashMap<String, Object>();
            mapQuery.put("waveId", waveId);
            long pickType = Long.valueOf(mapInput.get("pickType").toString());
            long pickZoneId = Long.valueOf(mapInput.get("pickZoneId").toString());
            if(pickZoneId != 0){
                mapQuery.put("ext1", pickZoneId);
            }
            //if(pickType)
            List<TaskEntry> taskHeadList = iTaskRpcService.getTaskHeadList(TaskConstant.TYPE_PICK, mapQuery);
            pickTaskIds = new LinkedList();
            for (TaskEntry entry : taskHeadList) {
                pickTaskIds.add(entry.getTaskInfo().getTaskId());
            }
        }
        List<Map<String, Object>> taskInfoList = new LinkedList<Map<String, Object>>();
        Map<Long, WorkZone> mapZone = new HashMap<Long, WorkZone>();
        for (Object id : pickTaskIds) {
            Long taskId = Long.valueOf(id.toString());
            TaskEntry entry = iTaskRpcService.getOldTaskEntryById(taskId);  //FIXME 前端展示使用,不考虑生命周期
            List<Map<String, Object>> details = (List<Map<String, Object>>) (List<?>) entry.getTaskDetailList();
            Map<String, Object> head = (Map<String, Object>) entry.getTaskHead();
            head.put("lineCount", details.size());
            ObdHeader obdHeader = orderService.getOutbSoHeaderByOrderId(Long.valueOf(details.get(0).get("orderId").toString()));
            head.put("deliveryName", obdHeader.getDeliveryName());
            head.put("deliveryCode", obdHeader.getDeliveryCode());
            Long pickZoneId = head.get("pickZoneId") == null ? 0 : Long.valueOf(head.get("pickZoneId").toString());
            WorkZone zone = mapZone.get(pickZoneId);
            if (pickZoneId != 0 && zone == null) {
                zone = workZoneService.getWorkZone(pickZoneId);
                mapZone.put(pickZoneId, zone);
            }
            head.put("pickZoneName", zone == null ? "" : zone.getZoneName());
            BigDecimal totalUomQty = BigDecimal.ZERO;
            List<WaveDetail> waveDetails = waveService.getDetailsByPickTaskId(taskId);
            for (WaveDetail waveDetail: waveDetails) {
                totalUomQty = totalUomQty.add(PackUtil.EAQty2UomQty(waveDetail.getAllocQty(), waveDetail.getAllocUnitName()));
            }
            head.put("totalUomQty", totalUomQty);
            taskInfoList.add(head);
        }
        return JsonUtils.SUCCESS(taskInfoList);
    }

    @GET
    @Path("expensiveGoodsList")
    public String getContainerExpensiveGoods(@QueryParam("containerId") Long containerId) throws BizCheckedException {
        return JsonUtils.SUCCESS(ipcPickRpcService.getContainerGoods(containerId));
    }

}
