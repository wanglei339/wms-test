package com.lsh.wms.api.service.pick;

import com.lsh.wms.model.wave.WaveDetail;
import java.math.BigDecimal;

/**
 * Created by zengwenjun on 16/7/30.
 */
public interface IQCRpcService {
    /*上报单个商品的qc结果,正常,少货,多货,错货,残次,日期异常等*/
    public void setResult(long containerId, long skuId, BigDecimal qty, long exceptionType);
    public void skipException(long containerId, long skuId);
    /*获取qc任务的剩余未Q列表*/
    public WaveDetail getUndoDetails(long containerId);
    /*确认完成此qc任务*/
    public void confirm(long containerId);
}
