package com.lsh.wms.service.tu;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.api.service.tu.ITuRestService;
import com.lsh.wms.api.service.tu.ITuRpcService;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.constant.TuConstant;
import com.lsh.wms.core.service.utils.IdGenerator;
import com.lsh.wms.core.service.wave.WaveService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Reference
    private ITuRpcService iTuRpcService;
    @Autowired
    private IdGenerator idGenerator;
    @Reference
    private ITaskRpcService iTaskRpcService;
    @Autowired
    private WaveService waveService;

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
     * todo 发货
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
            Boolean postResult = iTuRpcService.postTuDetails(tuId);
            Map<String, Boolean> resultMap = new HashMap<String, Boolean>();
            resultMap.put("response", postResult);
            return JsonUtils.SUCCESS(resultMap);
        }
        List<TuDetail> details = iTuRpcService.getTuDeailListByTuId(tuId);
        //事务操作,创建任务,发车状态改变 生成任务群
        if (null == details || details.size() < 1) {
            throw new BizCheckedException("2990041");
        }
        //大小店
        if (TuConstant.SCALE_STORE.equals(tuHead.getScale())) {  //小店 合板
            for (TuDetail detail : details) {
                String idKey = "task_" + TaskConstant.TYPE_DIRECT_SHIP.toString();
                Long shipTaskId = idGenerator.genId(idKey, true, true);
                TaskEntry taskEntry = new TaskEntry();
                TaskInfo shipTaskInfo = new TaskInfo();
                shipTaskInfo.setTaskId(shipTaskId);
                shipTaskInfo.setType(TaskConstant.TYPE_DIRECT_SHIP);
                shipTaskInfo.setTaskName("小店直流发货任务[" + detail.getMergedContainerId() + "]");
                shipTaskInfo.setContainerId(detail.getMergedContainerId()); //小店没和板子,就是原来了物理托盘码
                shipTaskInfo.setOperator(tuHead.getLoadUid()); //一个人装车
                shipTaskInfo.setBusinessMode(TaskConstant.MODE_DIRECT);
                shipTaskInfo.setSubType(TaskConstant.TASK_DIRECT_SMALL_SHIP);
                taskEntry.setTaskInfo(shipTaskInfo);
                iTaskRpcService.create(TaskConstant.TYPE_DIRECT_SHIP, taskEntry);
                // 直接完成
                iTaskRpcService.done(shipTaskId);
                //销库存移到consumer位置
                iTuRpcService.moveItemToConsumeArea(detail.getMergedContainerId());
            }
            //生成发货单
            iTuRpcService.creatDeliveryOrderAndDetail(tuHead);
            //拼接物美sap
            iTuRpcService.bulidSapDate(tuHead.getTuId());
        } else {
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
                shipTaskInfo.setTaskName("大店直流发货任务[" + detail.getMergedContainerId() + "]");
                shipTaskInfo.setContainerId(detail.getMergedContainerId());
                shipTaskInfo.setOperator(tuHead.getLoadUid()); //一个人装车
                shipTaskInfo.setBusinessMode(TaskConstant.MODE_DIRECT);
                shipTaskInfo.setSubType(TaskConstant.TASK_DIRECT_LARGE_SHIP);
                taskEntry.setTaskInfo(shipTaskInfo);
                iTaskRpcService.create(TaskConstant.TYPE_DIRECT_SHIP, taskEntry);
                // 直接完成
                iTaskRpcService.done(shipTaskId);
                List<WaveDetail> waveDetails = waveService.getWaveDetailsByMergedContainerId(detail.getMergedContainerId());
                if (null == waveDetails || waveDetails.size() < 1) {
                    waveDetails = waveService.getAliveDetailsByContainerId(detail.getMergedContainerId());
                }
                //移库存
                iTuRpcService.moveItemToConsumeArea(waveDetails);
            }
            //生成发货单
            iTuRpcService.creatDeliveryOrderAndDetail(tuHead);
            //拼接物美SAP
            iTuRpcService.bulidSapDate(tuHead.getTuId());
        }
        //改变发车状态
        tuHead.setStatus(TuConstant.SHIP_OVER);
        iTuRpcService.update(tuHead);
        // 传给TMS运单发车信息,此过程可以重复调用
        Boolean postResult = iTuRpcService.postTuDetails(tuId);
        if (postResult) {
            Map<String, Boolean> resultMap = new HashMap<String, Boolean>();
            resultMap.put("response", postResult);
            return JsonUtils.SUCCESS(resultMap);
        } else {
            throw new BizCheckedException("2990042");
        }
    }

    /**
     * 接收TU头信息
     *
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("receiveTuHead")
    public String receiveTuHead() throws BizCheckedException {
        Map<String, Object> mapRequest = RequestUtils.getRequest();
        TuHead tuHead = iTuRpcService.receiveTuHead(mapRequest);
        return JsonUtils.SUCCESS("");
    }

    /**
     * 关闭尾货开关
     *
     * @param tuId
     * @return
     * @throws BizCheckedException
     */
    @GET
    @Path("closeRfRestSwitch")
    public String closeRfRestSwitch(@QueryParam("tuId") String tuId) throws BizCheckedException {
        iTuRpcService.closeRfRestSwitch(tuId);
        return JsonUtils.SUCCESS();
    }

    /**
     * 开启rf尾货开关
     *
     * @param tuId
     * @return
     * @throws BizCheckedException
     */
    @GET
    @Path("openRfRestSwitch")
    public String openRfRestSwitch(@QueryParam("tuId") String tuId) throws BizCheckedException {
        iTuRpcService.openRfRestSwitch(tuId);
        return JsonUtils.SUCCESS();
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
