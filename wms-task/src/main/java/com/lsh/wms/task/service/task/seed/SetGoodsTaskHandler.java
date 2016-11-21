package com.lsh.wms.task.service.task.seed;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.location.ILocationRpcService;
import com.lsh.wms.api.service.stock.IStockMoveRpcService;
import com.lsh.wms.api.service.stock.IStockQuantRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.csi.CsiCustomerService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.csi.CsiCustomer;
import com.lsh.wms.model.so.ObdHeader;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.stock.StockQuantCondition;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.wave.WaveDetail;
import com.lsh.wms.task.service.handler.AbsTaskHandler;
import com.lsh.wms.task.service.handler.TaskHandlerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by mali on 16/8/2.
 */
@Component
public class SetGoodsTaskHandler extends AbsTaskHandler {
    @Autowired
    private TaskHandlerFactory handlerFactory;
    @Autowired
    BaseTaskService baseTaskService;
    @Reference
    ITaskRpcService taskRpcService;
    @Reference
    IStockQuantRpcService stockQuantRpcService;
    @Reference
    ILocationRpcService locationRpcService;
    @Autowired
    LocationService locationService;
    @Reference
    private IStockMoveRpcService moveRpcService;

    @Autowired
    SoOrderService soOrderService;
    @Autowired
    WaveService waveService;
    @Autowired
    private CsiCustomerService csiCustomerService;


    @PostConstruct
    public void postConstruct() {
        handlerFactory.register(TaskConstant.TYPE_SET_GOODS, this);
    }

    public void doneConcrete(Long taskId) {
        TaskEntry entry = taskRpcService.getTaskEntryById(taskId);
        TaskInfo info = entry.getTaskInfo();
        Long containerId = info.getContainerId();


        List<WaveDetail> details = waveService.getAliveDetailsByContainerId(containerId);
        if(details==null || details.size()==0){
            throw new BizCheckedException("2880003");
        }
        ObdHeader header = soOrderService.getOutbSoHeaderByOrderId(details.get(0).getOrderId());

        //获取location的id
        CsiCustomer customer = csiCustomerService.getCustomerByCustomerCode(header.getOwnerUid(),header.getDeliveryCode()); // 门店对应的集货道
        if (null == customer) {
            throw new BizCheckedException("2180023");
        }
        if (null == customer.getCollectRoadId()) {
            throw new BizCheckedException("2180024");
        }
        BaseinfoLocation location = locationService.getLocation(customer.getCollectRoadId());

        //移动库存
        moveRpcService.moveWholeContainer(containerId, taskId, info.getOperator(), location.getLocationId(), location.getLocationId());

    }
}
