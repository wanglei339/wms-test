package com.lsh.wms.api.service.pick;

import com.lsh.base.common.exception.BizCheckedException;

/**
 * Created by zengwenjun on 16/8/20.
 */
public interface IQCRestService {
    String skipException() throws BizCheckedException; //忽略qc异常,照常发货
    String repairException() throws BizCheckedException; //修复异常,会设置pick_qty=qc_qty,同时保留qc遗迹
    String fallbackException() throws BizCheckedException; //回退异常,qc自身错误,会设置pick_qty=qc_qty,同时保留qc遗迹
}
