package com.lsh.wms.service.outbound;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.api.model.so.ObdOfcBackRequest;
import com.lsh.wms.api.model.so.ObdOfcItem;
import com.lsh.wms.api.model.wumart.CreateObdDetail;
import com.lsh.wms.api.model.wumart.CreateObdHeader;
import com.lsh.wms.api.service.back.IDataBackService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.stock.IStockQuantRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.api.service.tu.ITuRpcService;
import com.lsh.wms.api.service.wave.IShipRestService;
import com.lsh.wms.api.service.wumart.IWuMart;
import com.lsh.wms.core.constant.IntegrationConstan;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.constant.TuConstant;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.so.SoDeliveryService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.tu.TuService;
import com.lsh.wms.core.service.utils.IdGenerator;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.so.ObdDetail;
import com.lsh.wms.model.so.ObdHeader;
import com.lsh.wms.model.so.OutbDeliveryDetail;
import com.lsh.wms.model.so.OutbDeliveryHeader;
import com.lsh.wms.model.stock.StockQuantCondition;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.tu.TuDetail;
import com.lsh.wms.model.tu.TuHead;
import com.lsh.wms.model.wave.WaveDetail;
import com.lsh.wms.service.tu.TuRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zengwenjun on 16/8/20.
 */
@Service(protocol = "rest")
@Path("outbound/ship")
public class ShipRestService implements IShipRestService {
    private static Logger logger = LoggerFactory.getLogger(ShipRestService.class);
    @Reference
    private ITuRpcService iTuRpcService;
    @Reference
    private ITaskRpcService iTaskRpcService;
    @Autowired
    private IdGenerator idGenerator;
    @Autowired
    private WaveService waveService;
    @Autowired
    private TuService tuService;
    @Reference
    IStockQuantRpcService stockQuantRpcService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private SoDeliveryService soDeliveryService;

    @Autowired
    private SoOrderService soOrderService;

    @Reference
    private IDataBackService dataBackService;

    @Reference
    private IWuMart wuMart;

    /**
     * 波次的发货操作
     * 1.托盘 2.销库存 3.生成发货单 4.todo 回传物美obd
     * 5.释放集货道
     *
     * @return
     * @throws BizCheckedException
     */


    @POST
    @Path("shipTu")
    public String ShipTu() throws BizCheckedException {
        Map<String, Object> mapRequest = RequestUtils.getRequest();
        String tuId = mapRequest.get("tuId").toString();
        Set<Long> totalContainers = new HashSet<Long>();
        TuHead tuHead = iTuRpcService.getHeadByTuId(tuId);
        //拿tuhead,判断状态
        //TODO 但是这里其实是可能出错的,当网络异常,在前一个请求没有完成数据库更新的时候,这里是可以运行第二次的,非常的危险.
        if (null == tuHead) {
            throw new BizCheckedException("2990022");
        }
        if(tuHead.getStatus().equals(TuConstant.SHIP_OVER)){
            throw new BizCheckedException("2990044");
        }
        //拿托盘
        List<TuDetail> details = iTuRpcService.getTuDeailListByTuId(tuId);
        //事务操作,创建任务,发车状态改变 生成任务群
        if (null == details || details.size() < 1) {
            throw new BizCheckedException("2990041");
        }
        //销库存 写在同个事务中,生成发货单 osd的托盘生命结束,因此只能运行一次
        List<WaveDetail> totalWaveDetails = tuService.createObdAndMoveStockQuantV2(tuHead, details);

        //创建发货任务
        {
            TaskEntry taskEntry = new TaskEntry();
            TaskInfo shipTaskInfo = new TaskInfo();
            shipTaskInfo.setType(TaskConstant.TYPE_DIRECT_SHIP);
            shipTaskInfo.setTaskName("优供的发货任务[" + totalWaveDetails.get(0).getContainerId() + "]");
            shipTaskInfo.setContainerId(totalWaveDetails.get(0).getContainerId()); //小店没和板子,就是原来了物理托盘码
            shipTaskInfo.setOperator(tuHead.getLoadUid()); //一个人装车
            shipTaskInfo.setBusinessMode(TaskConstant.MODE_INBOUND);
            shipTaskInfo.setLocationId(totalWaveDetails.get(0).getRealCollectLocation());
            taskEntry.setTaskInfo(shipTaskInfo);
            taskEntry.setTaskDetailList((List<Object>) (List<?>) totalWaveDetails);
            Long taskId = iTaskRpcService.create(TaskConstant.TYPE_SHIP, taskEntry);
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("response", true);
        return JsonUtils.SUCCESS(result);
    }
}
