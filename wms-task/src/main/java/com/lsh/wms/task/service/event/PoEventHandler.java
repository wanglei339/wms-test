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
import java.beans.EventHandler;
import java.util.List;

/**
 * Created by mali on 16/8/19.
 */
@Component
public class PoEventHandler extends AbsEventHandler implements IEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(PoEventHandler.class);

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
        eventHandlerFactory.register(TaskConstant.TYPE_PO, this);
    }


    public void process(Long taskId) {
        TaskInfo taskInfo =  taskRpcService.getTaskEntryById(taskId).getTaskInfo();
        Long itemId = taskInfo.getItemId();
        List<BaseinfoItemLocation> itemLocations = itemLocationService.getItemLocationList(itemId);
        Long pickLocationId = itemLocations.get(0).getPickLocationid();
        BaseinfoLocation pickLocation = locationService.getLocation(pickLocationId);
        Long handlerType = 0L;
        if (pickLocation.getType().equals(LocationConstant.LOCATION_TYPE.get("loft_collection_bin"))) {
            // 阁楼上架任务
            handlerType = TaskConstant.TYPE_ATTIC_SHELVE;
        } else {
            handlerType = TaskConstant.TYPE_SHELVE;
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

    public void process(TaskMsg msg) {
    }

}
