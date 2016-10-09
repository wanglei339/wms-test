package com.lsh.wms.task.service.event;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.item.ItemLocationService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.task.TaskHandler;
import com.lsh.wms.model.baseinfo.BaseinfoItemLocation;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.task.TaskMsg;
import com.lsh.wms.task.service.TaskRpcService;
import com.lsh.wms.task.service.handler.TaskHandlerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by mali on 16/8/19.
 */
@Component
public class TaskFinishEventHandler extends AbsEventHandler implements IEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(TaskFinishEventHandler.class);

    @Autowired
    private TaskRpcService taskRpcService;

    @Autowired
    private ItemLocationService itemLocationService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private TaskHandlerFactory taskHandlerFactory;

    @Autowired
    private EventHandlerFactory eventHandlerFactory;

    @PostConstruct
    public void postConstruct() {
        eventHandlerFactory.register(TaskConstant.EVENT_TASK_FINISH, this);
    }

    public void process(TaskMsg msg) {
        Long sourceTaskType = taskRpcService.getTaskTypeById(msg.getSourceTaskId());
        if (TaskConstant.TYPE_PO == sourceTaskType) {
            this.createShelveTask(msg.getSourceTaskId());
        } else if (TaskConstant.TYPE_PICK == sourceTaskType) {
            taskHandlerFactory.getTaskHandler(TaskConstant.TYPE_QC).create(msg.getSourceTaskId());
        }
        // todo 集货任务完成后qc任务出现
    }

    private void createShelveTask(Long taskId) throws BizCheckedException {
        TaskInfo taskInfo =  taskRpcService.getTaskEntryById(taskId).getTaskInfo();
        Long itemId = taskInfo.getItemId();
        List<BaseinfoItemLocation> itemLocations = itemLocationService.getItemLocationList(itemId);
        if (itemLocations == null || itemLocations.size()  == 0){
            logger.warn("item "+itemId+ " have no pick location");
            throw new BizCheckedException("2030010");
        }
        Long pickLocationId = itemLocations.get(0).getPickLocationid();
        BaseinfoLocation pickLocation = locationService.getLocation(pickLocationId);
        Long handlerType = 0L;
        if (pickLocation.getType().equals(LocationConstant.LOFT_PICKING_BIN)) {
            // 阁楼上架任务
            handlerType = TaskConstant.TYPE_ATTIC_SHELVE;
        } else if (pickLocation.getType().equals(LocationConstant.SHELF_PICKING_BIN)){
            handlerType = TaskConstant.TYPE_SHELVE;
        } else {
            handlerType = TaskConstant.TYPE_PICK_UP_SHELVE;
        }
        try {
            TaskHandler taskHandler = taskHandlerFactory.getTaskHandler(handlerType);
            taskHandler.create(taskRpcService.getTaskEntryById(taskId));
        } catch (BizCheckedException e) {
            logger.warn(e.getMessage());
        } catch (Exception e) {
            logger.error("Exception",e);
            logger.warn(e.getCause().getMessage());
        }
    }
}
