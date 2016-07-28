package com.lsh.wms.task.service.task.shelve;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.api.service.shelve.IShelveRpcService;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.container.ContainerService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.model.baseinfo.BaseinfoContainer;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.shelve.ShelveTaskHead;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.task.service.handler.AbsTaskHandler;
import com.lsh.wms.task.service.handler.TaskHandlerFactory;
import com.lsh.wms.core.service.shelve.ShelveTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by fengkun on 16/7/25.
 */
@Component
public class ShelveTaskHandler extends AbsTaskHandler {
    @Autowired
    private TaskHandlerFactory handlerFactory;
    @Autowired
    private ShelveTaskService taskService;
    @Autowired
    private ContainerService containerService;
    @Reference
    private IShelveRpcService iShelveRpcService;
    @Autowired
    private LocationService locationService;

    @PostConstruct
    public void postConstruct() {
        handlerFactory.register(TaskConstant.TYPE_SHELVE, this);
    }

    protected void createConcrete(TaskEntry taskEntry) throws BizCheckedException {
        ShelveTaskHead taskHead = (ShelveTaskHead) taskEntry.getTaskHead();
        taskHead.setTaskId(taskEntry.getTaskInfo().getTaskId());
        Long containerId = taskHead.getContainerId();
        BaseinfoContainer container = containerService.getContainer(containerId);
        if (container == null) {
            throw new BizCheckedException("2030003");
        }
        // 获取目标location
        BaseinfoLocation targetLocation = iShelveRpcService.assginShelveLocation(container);
        if (targetLocation == null) {
            throw new BizCheckedException("2030004");
        }
        taskHead.setAllocLocationId(targetLocation.getLocationId());
        taskService.create(taskHead);
    }

    protected void assignConcrete(Long taskId, Long staffId) {
        taskService.assign(taskId, staffId);
    }

    protected void doneConcrete(Long taskId, Long locationId) throws BizCheckedException{
        ShelveTaskHead taskHead = taskService.getShelveTaskHead(taskId);
        // 实际上架位置和分配位置不一致
        if (!locationId.equals(taskHead.getAllocLocationId())) {
            BaseinfoLocation realLocation = locationService.getLocation(locationId);
            if (realLocation == null) {
                throw new BizCheckedException("2030005");
            }
            if (locationService.isLocationInUse(locationId)) {
                throw new BizCheckedException("2030006");
            }
        }
        taskService.done(taskId, locationId);
    }

    protected void getConcrete(TaskEntry taskEntry) {
        taskEntry.setTaskHead(taskService.getShelveTaskHead(taskEntry.getTaskInfo().getTaskId()));
    }

    protected void getHeadConcrete(TaskEntry taskEntry) {
        taskEntry.setTaskHead(taskService.getShelveTaskHead(taskEntry.getTaskInfo().getTaskId()));
    }
}
