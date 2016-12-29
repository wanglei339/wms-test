package com.lsh.wms.service.sync;

import com.google.common.eventbus.Subscribe;
import com.lsh.wms.service.inhouse.ProcurementProviderRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Project Name: lsh-wms
 * Created by wuhao
 * Date: 16/12/29
 * 北京链商电子商务有限公司
 * Package com.lsh.wms.service.sync.AsyncEventListener
 * desc:异步处理监听类
 */
@Component
public class AsyncEventListener {

    @Autowired
    private ProcurementProviderRpcService procurementProviderRpcService;


    /**
     * 生成补货任务
     * @param canMax 是否按最大值补货
     */
    @Subscribe
    public void createProcurement(final Boolean canMax) {
        procurementProviderRpcService.createProcurement(canMax);

    }
}
