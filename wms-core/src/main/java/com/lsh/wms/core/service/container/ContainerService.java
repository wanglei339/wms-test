package com.lsh.wms.core.service.container;

import com.lsh.wms.core.dao.baseinfo.BaseinfoContainerDao;
import com.lsh.wms.model.baseinfo.BaseinfoContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fengkun on 16/7/8.
 */

@Service
@Transactional(readOnly = true)
public class ContainerService {
    private static final Logger logger = LoggerFactory.getLogger(ContainerService.class);
    @Autowired
    private BaseinfoContainerDao containerDao;

    public BaseinfoContainer getContainer (long containerId) {
        Map<String, Object> params = new HashMap<String, Object>();
        BaseinfoContainer container;
        params.put("containerId", containerId);
        List<BaseinfoContainer> containers = containerDao.getBaseinfoContainerList(params);
        if (containers.size() == 1) {
            container = containers.get(0);
        } else {
            return null;
        }
        return container;
    }

    @Transactional(readOnly = false)
    public void insertContainer (BaseinfoContainer container) {
        containerDao.insert(container);
    }

    @Transactional(readOnly = false)
    public void updateContainer (BaseinfoContainer container) {
        containerDao.update(container);
    }
}

