package com.lsh.wms.task.service.task.pick;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsh.base.common.exception.BizCheckedException;
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
        TaskEntry pickEntry = this.getTask(taskId); //此处使用pick就是个代号,也代表其他QC前的任务
        //判断是直流QC任务,还是在库QC任务,现在只能是通过 前一个任务类型来判断
        Long containerId = pickEntry.getTaskInfo().getContainerId();
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("containerId", containerId);


        List<TaskEntry> qcTaskEntry = iTaskRpcService.getTaskList(TaskConstant.TYPE_QC, mapQuery);
        TaskInfo qcTaskinfo = null;
        if (qcTaskEntry != null) {
            qcTaskinfo = qcTaskEntry.get(0).getTaskInfo();
        }
        //如果存在 拣货任务和qc任务一对一, 对于收货后的qc按照商品维度,需要同一个托盘的话,更新就行了
        //是收货任务
//        if (pickEntry.getTaskInfo().getType() == TaskConstant.TYPE_PO && qcTaskinfo != null) {
//            List<WaveDetail> details = waveService.getDetailsByContainerId(containerId);
//            if (details.size() == 0) {
//                return;
//            }
//            // todo setEXt1字段设置的是QC的上一个任务,这里可以是 pickTaskId 和 直流集货任务id 等等
//            qcTaskinfo.setQcPreviousTaskId(pickEntry.getTaskInfo().getTaskId());
//            qcTaskinfo.setOrderId(details.get(0).getOrderId());
//            qcTaskinfo.setBusinessMode(pickEntry.getTaskInfo().getBusinessMode());
//            Set<Long> setItem = new HashSet<Long>();
//            for (WaveDetail detail : details) {  //又是一个坑,关于item的问题
//                setItem.add(detail.getItemId());
//            }
//            qcTaskinfo.setSubType(pickEntry.getTaskInfo().getBusinessMode());  //沿用上面的直流还是在库
//            qcTaskinfo.setQty(new BigDecimal(setItem.size()));    //创建QC任务不设定QC需要的QC数量,而是实际输出来的数量和上面的任务操作数量比对
//            qcTaskinfo.setWaveId(details.get(0).getWaveId());
//            qcTaskinfo.setPlanId(qcTaskinfo.getPlanId());
//            TaskEntry taskEntry = new TaskEntry();
//            taskEntry.setTaskDetailList((List<Object>) (List<?>) details);
//            taskEntry.setTaskInfo(qcTaskinfo);
//            this.update(taskEntry);
//            return;
//        }

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