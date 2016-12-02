package com.lsh.wms.service.tu;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.api.model.wumart.CreateIbdHeader;
import com.lsh.wms.api.model.wumart.CreateObdHeader;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.stock.IStockQuantRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.api.service.tmstu.ITmsTuRpcService;
import com.lsh.wms.api.service.tmstu.ITmsTuService;
import com.lsh.wms.api.service.tu.ITuRestService;
import com.lsh.wms.api.service.tu.ITuRpcService;
import com.lsh.wms.api.service.wumart.IWuMart;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.constant.TuConstant;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.tu.TuService;
import com.lsh.wms.core.service.utils.IdGenerator;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.stock.StockQuantCondition;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.tu.TuDetail;
import com.lsh.wms.model.tu.TuHead;
import com.lsh.wms.model.wave.WaveDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 2016/10/25 下午9:25
 */
@Service(protocol = "rest")
@Path("outbound/tu")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class TuRestService implements ITuRestService {

    private static Logger logger = LoggerFactory.getLogger(TuRestService.class);
    @Autowired
    private TuRpcService iTuRpcService;
    @Autowired
    private IdGenerator idGenerator;
    @Autowired
    private WaveService waveService;
    @Autowired
    private TuService tuService;
    @Autowired
    private StockQuantService stockQuantService;
    @Autowired
    private LocationService locationService;

    @Reference
    private IStockQuantRpcService stockQuantRpcService;
    @Reference
    private ITaskRpcService iTaskRpcService;
    @Reference
    private ITmsTuRpcService iTmsTuRpcService;
    @Reference
    private IWuMart wuMart;

    @POST
    @Path("getTuheadList")
    public String getTuheadList() throws BizCheckedException {
        Map<String, Object> mapRequest = RequestUtils.getRequest();
        //可传入起始时间和结束时间
        return JsonUtils.SUCCESS(iTuRpcService.getTuHeadListOnPc(mapRequest));
    }

    @GET
    @Path("getTuDeailListByTuId")
    public String getTuDeailListByTuId(@QueryParam("tuId") String tuId) throws BizCheckedException {
        return JsonUtils.SUCCESS(iTuRpcService.getTuDeailListByTuId(tuId));
    }

    @GET
    @Path("getDetailById")
    public String getDetailById(@QueryParam("id") Long id) throws BizCheckedException {
        return JsonUtils.SUCCESS(iTuRpcService.getDetailById(id));
    }

    @POST
    @Path("getTuDetailList")
    public String getTuDetailList(Map<String, Object> mapQuery) throws BizCheckedException {
        return JsonUtils.SUCCESS(iTuRpcService.getTuHeadList(mapQuery));
    }

    @GET
    @Path("getTuheadByTuId")
    public String getTuheadByTuId(@QueryParam("tuId") String tuId) throws BizCheckedException {
        return JsonUtils.SUCCESS(iTuRpcService.getHeadByTuId(tuId));
    }

    @POST
    @Path("countTuheadList")
    public String countTuHeadOnPc(Map<String, Object> mapQuery) throws BizCheckedException {
        return JsonUtils.SUCCESS(iTuRpcService.countTuHeadOnPc(mapQuery));
    }


    /**
     * 减库存|写入task的绩效
     *
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("shipTu")
    public String shipTu() throws BizCheckedException {
        //进来先校验是否发车,已发车,不需要继续做java层事务,直接跳过传数据,  未发车全部进行
        Map<String, Object> mapRequest = RequestUtils.getRequest();
        String tuId = mapRequest.get("tuId").toString();
        TuHead tuHead = iTuRpcService.getHeadByTuId(tuId);
        if (null == tuHead) {
            throw new BizCheckedException("2990022");
        }
        //事务成功 tms失败
        if (TuConstant.SHIP_OVER.equals(tuHead.getStatus())) {
            try {
                Boolean postResult = iTmsTuRpcService.postTuDetails(tuId);
                Map<String, Boolean> resultMap = new HashMap<String, Boolean>();
                resultMap.put("response", postResult);
                return JsonUtils.SUCCESS(resultMap);
            } catch (Exception e) {
                logger.error(e.getCause()!=null ? e.getCause().getMessage():e.getMessage());
                throw new BizCheckedException("2990042");
            }
        }
        List<TuDetail> details = iTuRpcService.getTuDeailListByTuId(tuId);
        //事务操作,创建任务,发车状态改变 生成任务群
        if (null == details || details.size() < 1) {
            throw new BizCheckedException("2990041");
        }
        //生成发货单 osd的托盘生命结束并销库存
        tuService.createObdAndMoveStockQuantV2(tuHead, details);
        //TODO 后面一旦失败,用户的绩效就记不住了,这里是非常不严谨的 <写成一个任务,记个数字以后> 其实这也是个事务
        for (TuDetail detail : details) {
            //贵品不记录绩效
            if (detail.getIsExpensive().equals(TuConstant.IS_EXPENSIVE)) {
                continue;   //贵品不记录绩效
            }
            String idKey = "task_" + TaskConstant.TYPE_DIRECT_SHIP.toString();
            Long shipTaskId = idGenerator.genId(idKey, true, true);
            TaskEntry taskEntry = new TaskEntry();
            TaskInfo shipTaskInfo = new TaskInfo();
            shipTaskInfo.setTaskId(shipTaskId);
            shipTaskInfo.setType(TaskConstant.TYPE_DIRECT_SHIP);
            shipTaskInfo.setTaskName("门店发货任务[" + detail.getMergedContainerId() + "]");
            shipTaskInfo.setContainerId(detail.getMergedContainerId()); //小店没和板子,就是原来了物理托盘码
            shipTaskInfo.setOperator(tuHead.getLoadUid()); //一个人装车
            shipTaskInfo.setBusinessMode(TaskConstant.MODE_DIRECT);
            shipTaskInfo.setSubType(TuConstant.SCALE_STORE.equals(tuHead.getScale()) ? TaskConstant.TASK_DIRECT_SMALL_SHIP : TaskConstant.TASK_DIRECT_LARGE_SHIP);
            taskEntry.setTaskInfo(shipTaskInfo);
            iTaskRpcService.create(TaskConstant.TYPE_DIRECT_SHIP, taskEntry);
            // 直接完成
            iTaskRpcService.done(shipTaskId);

        }
        try {
            // 传给TMS运单发车信息,此过程可以重复调用
            Boolean postResult = iTmsTuRpcService.postTuDetails(tuId);
            if (postResult) {
                Map<String, Boolean> resultMap = new HashMap<String, Boolean>();
                resultMap.put("response", postResult);
                return JsonUtils.SUCCESS(resultMap);
            } else {
                throw new BizCheckedException("2990042");
            }
        } catch (Exception e) {
            throw new BizCheckedException("2990042");
        }

    }

    /**
     * 改变rf的开关
     *
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("changeRfRestSwitch")
    public String changeRfRestSwitch() throws BizCheckedException {
        Map<String, Object> mapRequest = RequestUtils.getRequest();
        return JsonUtils.SUCCESS(iTuRpcService.changeRfRestSwitch(mapRequest));
    }


    /**
     * 移除板子
     *
     * @return
     * @throws BizCheckedException
     */
    @GET
    @Path("removeTuDetail")
    public String removeTuDetail(@QueryParam("mergedContainerId") Long mergedContainerId) throws BizCheckedException {
        iTuRpcService.removeTuDetail(mergedContainerId);
        return JsonUtils.SUCCESS();
    }

}
