package com.lsh.wms.api.service.container;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.wms.model.baseinfo.BaseinfoContainer;

import java.util.List;
import java.util.Map;

/**
 * Created by fengkun on 16/7/8.
 */
public interface IContainerRpcService {
    public BaseinfoContainer getContainer(long containerId);
    public BaseinfoContainer insertContainer(BaseinfoContainer container);
    public BaseinfoContainer createTray();
    public BaseinfoContainer createTrolley();   //笼车
    public BaseinfoContainer createCirculatingBox();    //周转箱
}
