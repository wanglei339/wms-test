package com.lsh.wms.service.outbound;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.service.pick.IQCRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.PickConstant;
import com.lsh.wms.core.constant.WaveConstant;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.wave.WaveDetail;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

/**
 * Created by zengwenjun on 16/7/30.
 */
@Service(protocol = "dubbo")
public class QCRpcService implements IQCRpcService {
    @Autowired
    private WaveService waveService;
    @Reference
    private ITaskRpcService iTaskRpcService;

    public void skipException(long id) throws BizCheckedException {
        WaveDetail detail = waveService.getWaveDetailById(id);
        if (detail == null) {
            throw new BizCheckedException("2070001");
        }
        detail.setQcExceptionDone(PickConstant.QC_EXCEPTION_DONE_SKIP);
        Long taskId = detail.getQcTaskId();
        TaskInfo qcTaskInfo = iTaskRpcService.getTaskInfo(taskId);
        if (null == qcTaskInfo) {
            throw new BizCheckedException("2070002");
        }
        iTaskRpcService.done(taskId, qcTaskInfo.getLocationId());
        waveService.updateDetail(detail);
    }

    public void repairException(long id) throws BizCheckedException {
        WaveDetail detail = waveService.getWaveDetailById(id);
        if (detail == null) {
            throw new BizCheckedException("2070001");
        }
        detail.setQcExceptionDone(PickConstant.QC_EXCEPTION_DONE_DONE);
        detail.setQcQty(detail.getPickQty());
        detail.setQcExceptionQty(new BigDecimal("0.0000"));
        detail.setQcException(WaveConstant.QC_EXCEPTION_NORMAL);
        waveService.updateDetail(detail);

        Long taskId = detail.getQcTaskId();
        TaskInfo qcTaskInfo = iTaskRpcService.getTaskInfo(taskId);
        if (null == qcTaskInfo) {
            throw new BizCheckedException("2070002");
        }
        iTaskRpcService.done(taskId, qcTaskInfo.getLocationId());
    }

    public void fallbackException(long id) throws BizCheckedException {
        WaveDetail detail = waveService.getWaveDetailById(id);
        if (detail == null) {
            throw new BizCheckedException("2070001");
        }
        detail.setQcExceptionDone(PickConstant.QC_EXCEPTION_DONE_NORMAL);
        detail.setQcQty(detail.getPickQty());
        detail.setQcException(WaveConstant.QC_EXCEPTION_NORMAL);
        detail.setQcExceptionQty(new BigDecimal("0.0000"));
        waveService.updateDetail(detail);

        Long taskId = detail.getQcTaskId();
        TaskInfo qcTaskInfo = iTaskRpcService.getTaskInfo(taskId);
        if (null == qcTaskInfo) {
            throw new BizCheckedException("2070002");
        }
        iTaskRpcService.done(taskId, qcTaskInfo.getLocationId());
    }
}
