package com.lsh.wms.task.service.task.pick;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.pick.PickTaskService;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.pick.PickTaskHead;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.wave.WaveDetail;
import com.lsh.wms.task.service.handler.AbsTaskHandler;
import com.lsh.wms.task.service.handler.TaskHandlerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by zengwenjun on 16/7/30.
 */
@Component
public class QCTaskHandler extends AbsTaskHandler {
    @Autowired
    private TaskHandlerFactory handlerFactory;
    @Autowired
    private WaveService waveService;
    @Reference
    private ITaskRpcService iTaskRpcService;

    @PostConstruct
    public void postConstruct() {
        handlerFactory.register(TaskConstant.TYPE_QC, this);
    }

    public void create(Long taskId) throws BizCheckedException {    //创建到另一张表中,然后CRUD操作在新表中进行
        //todo 一个同一个托盘,只能生成一个qc任务,如果QC存在了之前的qc任务,那么qc任务就更新
        TaskEntry pickEntry = iTaskRpcService.getTaskEntryById(taskId); //此处使用pick就是个代号,也代表其他QC前的任务
        //判断是直流QC任务,还是在库QC任务,现在只能是通过 前一个任务类型来判断
        Long containerId = pickEntry.getTaskInfo().getContainerId();
        //区wave_detail中找该托盘是否已存在QC(wave_detail中的托盘有生命周期,task中没有,可能托盘复用)
        List<WaveDetail> waveDetails = waveService.getAliveDetailsByContainerId(containerId);
        if (null == waveDetails || waveDetails.size() < 1) {
            return;
        }
        //同一托盘多商品,多收货任务,一个qc任务,之前生成的qc任务更新
        Long preQcTaskId = waveDetails.get(0).getQcTaskId();
        TaskInfo qcTaskinfo = iTaskRpcService.getTaskInfo(preQcTaskId);
        //如果存在 拣货任务和qc任务一对一, 对于收货后的qc按照商品维度,需要同一个托盘的话,更新就行了
        //是收货任务
        if (pickEntry.getTaskInfo().getType() == TaskConstant.TYPE_PO && qcTaskinfo != null) {
            List<WaveDetail> details = waveDetails;
            if (details.size() == 0) {
                return;
            }
            // todo setEXt1字段设置的是QC的上一个任务,这里可以是 pickTaskId 和 直流集货任务id 等等
            qcTaskinfo.setQcPreviousTaskId(pickEntry.getTaskInfo().getTaskId());
            qcTaskinfo.setOrderId(details.get(0).getOrderId());
            qcTaskinfo.setBusinessMode(pickEntry.getTaskInfo().getBusinessMode());
            Set<Long> setItem = new HashSet<Long>();
            for (WaveDetail detail : details) {
                setItem.add(detail.getItemId());
            }
            qcTaskinfo.setBusinessMode(pickEntry.getTaskInfo().getBusinessMode());  //沿用上面的直流还是在库
            qcTaskinfo.setQty(new BigDecimal(setItem.size()));    //创建QC任务不设定QC需要的QC数量,而是实际输出来的数量和上面的任务操作数量比对
            qcTaskinfo.setWaveId(details.get(0).getWaveId());
            qcTaskinfo.setPlanId(qcTaskinfo.getPlanId());
            TaskEntry taskEntry = new TaskEntry();
            taskEntry.setTaskDetailList((List<Object>) (List<?>) details);
            taskEntry.setTaskInfo(qcTaskinfo);
            this.update(taskEntry);
            //只更新,taskinfo并且waveDetai中的qcTaskId更新
            return;
        }


        List<WaveDetail> details = waveService.getDetailsByContainerId(containerId);
        if (details.size() == 0) {
            return;
        }
        TaskInfo info = new TaskInfo();
        info.setType(TaskConstant.TYPE_QC);
        info.setContainerId(containerId);
        // todo setEXt1字段设置的是QC的上一个任务,这里可以是 pickTaskId 和 直流集货任务id 等等
        info.setQcPreviousTaskId(pickEntry.getTaskInfo().getTaskId());
        info.setOrderId(details.get(0).getOrderId());
        info.setBusinessMode(pickEntry.getTaskInfo().getBusinessMode());
        Set<Long> setItem = new HashSet<Long>();
        for (WaveDetail detail : details) {
            setItem.add(detail.getItemId());
        }
        info.setSubType(pickEntry.getTaskInfo().getBusinessMode());  //沿用上面的直流还是在库
        info.setQty(new BigDecimal(setItem.size()));    //创建QC任务不设定QC需要的QC数量,而是实际输出来的数量和上面的任务操作数量比对
        info.setWaveId(details.get(0).getWaveId());
        info.setPlanId(info.getPlanId());
        //如果收货任务的大店收货任务才生成QC,大店收货可以跳过QC,task_info的qc_skip置为1,大店收货收到集货道
        if (pickEntry.getTaskInfo().getType() == TaskConstant.TYPE_PO) {
            info.setQcSkip(TaskConstant.QC_SKIP);
        }
        TaskEntry taskEntry = new TaskEntry();
        taskEntry.setTaskDetailList((List<Object>) (List<?>) details);
        taskEntry.setTaskInfo(info);
        this.create(taskEntry);
    }

    public void createConcrete(TaskEntry taskEntry) throws BizCheckedException {
        List<WaveDetail> details = (List<WaveDetail>) (List<?>) taskEntry.getTaskDetailList();
        for (WaveDetail detail : details) {
            detail.setQcTaskId(taskEntry.getTaskInfo().getTaskId());
        }
        waveService.updateDetails(details);
    }

    //之跟新 wave——detail
    public void updteConcrete(TaskEntry taskEntry) throws BizCheckedException {
        //如果taskEntry有detail就更新,detail,不然就只return
        List<WaveDetail> details = (List<WaveDetail>) (List<?>) taskEntry.getTaskDetailList();
        if (null == details || details.size() < 1) {
            return;    //直接return就不会更新
        }
        //更新组盘时间,写到wave_detail中
        long boxTime = DateUtils.getCurrentSeconds();
        for (WaveDetail detail : details) {
            detail.setQcTaskId(taskEntry.getTaskInfo().getTaskId());
            detail.setQcAt(boxTime);
        }
        waveService.updateDetails(details);
    }


    protected void getConcrete(TaskEntry taskEntry) {
        taskEntry.setTaskDetailList((List<Object>) (List<?>) waveService.getDetailsByQCTaskId(taskEntry.getTaskInfo().getTaskId()));
    }

    public void doneConcrete(Long taskId) {
        //这里做一些处理,做些啥呢?
        //--------------稍微注意一下下面两个操作会不会影响到性能,严格来讲,其实最好是异步的,呵呵.
        //更新订单状态
        TaskInfo info = baseTaskService.getTaskInfoById(taskId);
        if (info.getOrderId() > 0) {
            waveService.updateOrderStatus(info.getOrderId());
        }
        //更新波次状态
        waveService.updateWaveStatus(info.getWaveId());
    }

}