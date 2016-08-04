package com.lsh.wms.api.service.pick;

import com.lsh.base.common.exception.BizCheckedException;

import javax.ws.rs.QueryParam;

/**
 * Created by zengwenjun on 16/7/30.
 */
public interface IShipRestService {
    /*上报单个商品的qc结果,正常,少货,多货,错货,残次,日期异常等*/
    public String scanContainer(long containerId) throws BizCheckedException;
   /*扫描集货道,领取任务*/
    public String scan(long locationId) throws BizCheckedException;
    /*确认完成此发货任务*/
    public String confirm(long taskId) throws BizCheckedException;
    /*创建任务,临时*/
    public String createTask(long locationId) throws BizCheckedException;
}
