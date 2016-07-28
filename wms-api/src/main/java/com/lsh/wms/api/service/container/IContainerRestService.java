package com.lsh.wms.api.service.container;

import com.lsh.wms.model.baseinfo.BaseinfoContainer;

/**
 * Created by fengkun on 16/7/8.
 */
public interface IContainerRestService {
    public String getContainer(long containerId);
    public String insertContainer(BaseinfoContainer container);
    public String createContainerByType(Long type);

}
