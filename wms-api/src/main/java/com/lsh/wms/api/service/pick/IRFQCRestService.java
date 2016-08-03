package com.lsh.wms.api.service.pick;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.wave.WaveDetail;

import javax.ws.rs.QueryParam;
import java.math.BigDecimal;

/**
 * Created by zengwenjun on 16/7/30.
 */
public interface IRFQCRestService {
    /*扫描托盘吗,领取任务*/
    public String scan(Long containerId);
    /*上报单个商品的qc结果,正常,少货,多货,错货,残次,日期异常等*/
    public String setResult() throws BizCheckedException;
    //public String skipException(long containerId, long skuId);
    /*获取qc任务的剩余未Q列表*/
    public String getUndoDetails(long containerId);
    /*确认完成此qc任务*/
    public String confirm(long containerId) throws BizCheckedException;
    /*创建任务,临时*/
    public String createTask(long containerId) throws BizCheckedException;
}
