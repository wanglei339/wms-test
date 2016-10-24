package com.lsh.wms.task.service.task.back;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.ContainerConstant;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.back.InStorageService;
import com.lsh.wms.core.service.container.ContainerService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.stock.StockMoveService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.model.back.BackTaskDetail;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.so.ObdDetail;
import com.lsh.wms.model.stock.StockLot;
import com.lsh.wms.model.stock.StockMove;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.task.service.handler.AbsTaskHandler;
import com.lsh.wms.task.service.handler.TaskHandlerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wuhao on 16/10/17.
 */
@Component
public class BackShelveTaskHandler extends AbsTaskHandler {
    @Autowired
    private TaskHandlerFactory handlerFactory;
    @Autowired
    private InStorageService inStorageService;
    @Reference
    ITaskRpcService taskRpcService;
    @Autowired
    SoOrderService soOrderService;
    @Autowired
    private ContainerService containerService;
    @Autowired
    LocationService locationService;
    @Autowired
    private StockMoveService moveService;
    @Autowired
    private StockQuantService quantService;

    private static Logger logger = LoggerFactory.getLogger(BackShelveTaskHandler.class);


    @PostConstruct
    public void postConstruct() {
        handlerFactory.register(TaskConstant.TYPE_BACK_SHELVE, this);
    }


}
