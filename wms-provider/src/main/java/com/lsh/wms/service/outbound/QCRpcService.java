package com.lsh.wms.service.outbound;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.service.pick.IQCRpcService;
import com.lsh.wms.core.constant.PickConstant;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.wave.WaveDetail;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by zengwenjun on 16/7/30.
 */
@Service(protocol = "dubbo")
public class QCRpcService implements IQCRpcService{
    @Autowired
    private WaveService waveService;

    public void skipException(long id) throws BizCheckedException {
        WaveDetail detail = waveService.getWaveDetailById(id);
        if(detail == null){
            throw new BizCheckedException("2070001");
        }
        detail.setQcExceptionDone(PickConstant.QC_EXCEPTION_DONE_SKIP);
        waveService.updateDetail(detail);
    }

    public void repairException(long id) throws BizCheckedException {
        WaveDetail detail = waveService.getWaveDetailById(id);
        if(detail == null){
            throw new BizCheckedException("2070001");
        }
        detail.setQcExceptionDone(PickConstant.QC_EXCEPTION_DONE_DONE);
        detail.setQcQty(detail.getPickQty());
        waveService.updateDetail(detail);
    }

    public void fallbackException(long id) throws BizCheckedException {
        WaveDetail detail = waveService.getWaveDetailById(id);
        if(detail == null){
            throw new BizCheckedException("2070001");
        }
        detail.setQcExceptionDone(PickConstant.QC_EXCEPTION_DONE_NORMAL);
        detail.setQcQty(detail.getPickQty());
        waveService.updateDetail(detail);
    }
}
