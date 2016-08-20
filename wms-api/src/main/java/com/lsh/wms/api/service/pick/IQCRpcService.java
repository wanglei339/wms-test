package com.lsh.wms.api.service.pick;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.wave.WaveDetail;
import java.math.BigDecimal;

/**
 * Created by zengwenjun on 16/7/30.
 */
public interface IQCRpcService {
    void skipException(long id) throws BizCheckedException; //忽略qc异常,照常发货
    void repairException(long id) throws BizCheckedException; //修复异常,会设置pick_qty=qc_qty,同时保留qc遗迹
    void fallbackException(long id) throws BizCheckedException; //回退异常,qc自身错误,会设置pick_qty=qc_qty,同时保留qc遗迹
}
