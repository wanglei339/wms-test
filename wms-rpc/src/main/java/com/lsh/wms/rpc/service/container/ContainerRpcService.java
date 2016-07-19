package com.lsh.wms.rpc.service.container;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.api.service.container.IContainerRpcService;
import com.lsh.wms.core.service.container.ContainerService;
import com.lsh.wms.model.baseinfo.BaseinfoContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by fengkun on 16/7/8.
 */

@Service(protocol = "dubbo")
public class ContainerRpcService implements IContainerRpcService {
    private static Logger logger = LoggerFactory.getLogger(ContainerRpcService.class);

    @Autowired
    private ContainerService containerService;

    public BaseinfoContainer getContainer(long containerId) {
        return containerService.getContainer(containerId);
    }

    public BaseinfoContainer insertContainer(BaseinfoContainer container) {
        return container;
    }

    public BaseinfoContainer createTray() {
        return containerService.createContainerByType(1L);
    }
}
