package com.lsh.wms.rf.service.inbound;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.BeanMapTransUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.api.service.location.ILocationRpcService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.shelve.IShelveRestService;
import com.lsh.wms.api.service.system.ISysUserRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.shelve.ShelveTaskService;
import com.lsh.wms.core.service.stock.StockLotService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.shelve.ShelveTaskHead;
import com.lsh.wms.model.stock.StockLot;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.system.SysUser;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fengkun on 16/7/28.
 */

@Service(protocol = "rest")
@Path("inbound/shelve")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class ShelveRestService implements IShelveRestService {
    @Reference
    private ITaskRpcService iTaskRpcService;
    @Reference
    private ISysUserRpcService iSysUserRpcService;
    @Reference
    private ILocationRpcService iLocationRpcService;
    @Autowired
    private BaseTaskService baseTaskService;
    @Autowired
    private StockQuantService stockQuantService;
    @Autowired
    private StockLotService stockLotService;
    @Autowired
    private ShelveTaskService shelveTaskService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private ItemService itemService;

    private Long taskType = TaskConstant.TYPE_SHELVE;

    /**
     * 创建上架任务
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("createTask")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String createTask() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        Long containerId = Long.valueOf(mapQuery.get("containerId").toString());
        // 检查容器信息
        if (containerId == null || containerId.equals("")) {
            throw new BizCheckedException("2030003");
        }
        // 检查该容器是否已创建过任务
        if (baseTaskService.checkTaskByContainerId(containerId)) {
            throw new BizCheckedException("2030008");
        }
        // 获取quant
        List<StockQuant> quants = stockQuantService.getQuantsByContainerId(containerId);
        if (quants.size() < 1) {
            throw new BizCheckedException("2030001");
        }
        StockQuant quant = quants.get(0);

        TaskInfo taskInfo = new TaskInfo();
        ShelveTaskHead taskHead = new ShelveTaskHead();
        TaskEntry entry = new TaskEntry();

        ObjUtils.bean2bean(quant, taskInfo);
        ObjUtils.bean2bean(quant, taskHead);

        taskInfo.setType(taskType);
        taskInfo.setFromLocationId(quant.getLocationId());

        entry.setTaskInfo(taskInfo);
        entry.setTaskHead(taskHead);

        final Long taskId = iTaskRpcService.create(taskType, entry);
        return JsonUtils.SUCCESS(new HashMap<String, Long>() {
            {
                put("taskId", taskId);
            }
        });
    }

    /**
     * 扫描需上架的容器id
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("scanContainer")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String scanContainer() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        Long staffId = Long.valueOf(RequestUtils.getHeader("uid"));
        Long containerId = Long.valueOf(mapQuery.get("containerId").toString());
        Long taskId = baseTaskService.getDraftTaskIdByContainerId(containerId);
        // 判断用户是否存在
        SysUser sysUser = iSysUserRpcService.getSysUserById(staffId);
        if (sysUser == null) {
            throw new BizCheckedException("2000003");
        }
        // 回溯
        List<TaskInfo> taskInfos = baseTaskService.getAssignedTaskByOperator(staffId, TaskConstant.TYPE_SHELVE);
        if (!taskInfos.isEmpty() && taskInfos != null) {
            TaskInfo taskInfo = taskInfos.get(0);
            if (taskInfo.getContainerId().equals(containerId)) {
                ShelveTaskHead taskHead = shelveTaskService.getShelveTaskHead(taskInfos.get(0).getTaskId());
                BaseinfoLocation allocLocation = locationService.getLocation(taskHead.getAllocLocationId());
                StockLot stockLot = stockLotService.getStockLotByLotId(taskHead.getLotId());
                BaseinfoItem item = itemService.getItem(stockLot.getItemId());
                Map<String, Object> result = BeanMapTransUtils.Bean2map(taskHead);
                result.put("allocLocationCode", allocLocation.getLocationCode());
                result.put("itemId", item.getItemId());
                result.put("skuName", item.getSkuName());
                return JsonUtils.SUCCESS(result);
            } else {
                throw new BizCheckedException("2030016");
            }
        }
        // 检查是否有已分配的任务
        if (taskId == null && baseTaskService.checkTaskByContainerId(containerId)) {
            throw new BizCheckedException("2030008");
        }

        TaskEntry entry = iTaskRpcService.getTaskEntryById(taskId);
        if(entry.getTaskInfo().getType().compareTo(TaskConstant.TYPE_SHELVE)!=0){
            return JsonUtils.TOKEN_ERROR("任务类型不匹配");
        }

        iTaskRpcService.assign(taskId, staffId);
        ShelveTaskHead taskHead = shelveTaskService.getShelveTaskHead(taskId);
        BaseinfoLocation allocLocation = locationService.getLocation(taskHead.getAllocLocationId());
        StockLot stockLot = stockLotService.getStockLotByLotId(taskHead.getLotId());
        BaseinfoItem item = itemService.getItem(stockLot.getItemId());
        Map<String, Object> result = BeanMapTransUtils.Bean2map(taskHead);
        result.put("allocLocationCode", allocLocation.getLocationCode());
        result.put("itemId", item.getItemId());
        result.put("skuName", item.getSkuName());
        return JsonUtils.SUCCESS(result);
    }

    /**
     * 扫描上架目标location_id
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("scanTargetLocation")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String scanTargetLocation() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        Long taskId = Long.valueOf(mapQuery.get("taskId").toString());
        String locationCode = mapQuery.get("locationCode").toString();
        Long locationId = iLocationRpcService.getLocationIdByCode(locationCode);
        TaskEntry entry = iTaskRpcService.getTaskEntryById(taskId);
        if(entry == null){
            return JsonUtils.EXCEPTION_ERROR();
        }
        iTaskRpcService.done(taskId, locationId);
        return JsonUtils.SUCCESS(new HashMap<String, Boolean>() {
            {
                put("response", true);
            }
        });
    }

    /**
     * 回溯上架任务
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("restore")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String restore() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        Long staffId = Long.valueOf(RequestUtils.getHeader("uid"));
        List<TaskInfo> taskInfos = baseTaskService.getAssignedTaskByOperator(staffId, TaskConstant.TYPE_SHELVE);
        if ( taskInfos == null || taskInfos.isEmpty() ) {
            return JsonUtils.SUCCESS(new HashMap<String, Object>() {
                {
                    put("response", false);
                }
            });
        }
        ShelveTaskHead taskHead = shelveTaskService.getShelveTaskHead(taskInfos.get(0).getTaskId());
        BaseinfoLocation allocLocation = locationService.getLocation(taskHead.getAllocLocationId());
        StockLot stockLot = stockLotService.getStockLotByLotId(taskHead.getLotId());
        BaseinfoItem item = itemService.getItem(stockLot.getItemId());
        Map<String, Object> result = BeanMapTransUtils.Bean2map(taskHead);
        result.put("allocLocationCode", allocLocation.getLocationCode());
        result.put("itemId", item.getItemId());
        result.put("skuName", item.getSkuName());
        return JsonUtils.SUCCESS(result);
    }
}
