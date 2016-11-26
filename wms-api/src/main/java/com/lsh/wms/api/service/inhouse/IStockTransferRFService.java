package com.lsh.wms.api.service.inhouse;

import com.lsh.base.common.exception.BizCheckedException;

/**
 * Created by zengwenjun on 16/11/24.
 */
public interface IStockTransferRFService {
    //查看库位详情,进入到信息展示和输入环节,可以发起即时移库
    String viewLocation() throws BizCheckedException;
    //领取任务,进入到信息展示和输入环节,开始计划移库
    String fetchTask() throws BizCheckedException;
    //扫描库位,动作分为:
        //第一阶段-即时移库(无任务id)
        //第一阶段-计划移库(有任务id)
        //第二阶段-库位移入(有任务id)
    String scanLocation() throws BizCheckedException;
}
