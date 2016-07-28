package com.lsh.wms.core.service.container;

import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.BeanMapTransUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.core.dao.baseinfo.BaseinfoContainerDao;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.model.baseinfo.BaseinfoContainer;
import com.lsh.wms.model.stock.StockQuant;
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
    @Autowired
    private StockQuantService stockQuantService;

    // container类型定义
    public static final Map<Long, Map<String, Object>> containerConfigs = new HashMap<Long, Map<String, Object>>() {
        {
            put(1L, new HashMap<String, Object>() { // 托盘
                {
                    put("typeName", "托盘");
                }
            });
        }
    };

    public BaseinfoContainer getContainer (Long containerId) {
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

    @Transactional(readOnly = false)
    public BaseinfoContainer createContainerByType(Long type) {
        Map<String, Object> config = this.containerConfigs.get(type);
        if (config == null || config.isEmpty()) {
            return null;
        }
        BaseinfoContainer container = BeanMapTransUtils.map2Bean(config, BaseinfoContainer.class);
        container.setContainerId(RandomUtils.genId());
        container.setCreatedAt(DateUtils.getCurrentSeconds());
        container.setUpdatedAt(DateUtils.getCurrentSeconds());
        container.setType(type);
        this.insertContainer(container);
        return container;
    }

    public Boolean isContainerInUse(Long containerId) {
        List<StockQuant> quants = stockQuantService.getQuantsByLocationId(containerId);
        if (quants.size() > 0) {
            return true;
        }
        return false;
    }
}

