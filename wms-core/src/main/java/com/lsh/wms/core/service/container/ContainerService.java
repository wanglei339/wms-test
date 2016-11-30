package com.lsh.wms.core.service.container;

import com.lsh.base.common.utils.BeanMapTransUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.core.constant.ContainerConstant;
import com.lsh.wms.core.dao.baseinfo.BaseinfoContainerDao;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.core.service.utils.IdGenerator;
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
    @Autowired
    private LocationService locationService;
    @Autowired
    private BaseTaskService baseTaskService;
    @Autowired
    private IdGenerator idGenerator;

    public BaseinfoContainer getContainer(Long containerId) {
        Map<String, Object> params = new HashMap<String, Object>();
        BaseinfoContainer container;
        params.put("containerId", containerId);
        List<BaseinfoContainer> containers = containerDao.getBaseinfoContainerList(params);
        if (containers != null && containers.size() == 1) {
            container = containers.get(0);
        } else {
            return null;
        }
        return container;
    }

    public BaseinfoContainer getContainerByCode(String containerCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        BaseinfoContainer container;
        params.put("containerCode", containerCode);
        List<BaseinfoContainer> containers = containerDao.getBaseinfoContainerList(params);
        if (containers != null && containers.size() == 1) {
            container = containers.get(0);
        } else {
            return null;
        }
        return container;
    }

    @Transactional(readOnly = false)
    public void insertContainer(BaseinfoContainer container) {
        containerDao.insert(container);
    }

    @Transactional(readOnly = false)
    public void updateContainer(BaseinfoContainer container) {
        containerDao.update(container);
    }

    @Transactional(readOnly = false)
    public BaseinfoContainer createContainerByType(Long type) {
        Map<String, Object> config = ContainerConstant.containerConfigs.get(type);
        if (config == null || config.isEmpty()) {
            return null;
        }

        BaseinfoContainer container = BeanMapTransUtils.map2Bean(config, BaseinfoContainer.class);
        container.setContainerId(idGenerator.genId("containerCode", false, true));
        container.setContainerCode(container.getContainerId().toString());
        container.setCreatedAt(DateUtils.getCurrentSeconds());
        container.setUpdatedAt(DateUtils.getCurrentSeconds());
        container.setType(type);
        this.insertContainer(container);
        return container;
    }

    /**
     * 容器是否在使用,使用的是库存的管理
     *
     * @param containerId
     * @return
     */
    public Boolean isContainerInUse(Long containerId) {
        List<StockQuant> quants = stockQuantService.getQuantsByContainerId(containerId);
        if (quants != null && quants.size() > 0) {

            return true;
        }
        return false;
    }

    /**
     * 判断容器能否使用,考虑库存和任务两个维度
     *
     * @param containerId
     * @return
     */
    public boolean isContainerCanUse(Long containerId) {
//      是否存在托盘和是否在使用
        List<StockQuant> quants = stockQuantService.getQuantsByContainerId(containerId);
        if (this.getContainer(containerId) != null && quants.size() <= 0 && !baseTaskService.checkTaskByContainerId(containerId)) {
            return true;
        }
        return false;
    }

    @Transactional(readOnly = false)
    public Long getContaierIdByLocationId(Long locationId) {
        locationService.lockLocationById(locationId);
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("locationId", locationId);
        List<StockQuant> quantList = stockQuantService.getQuants(condition);
        return (quantList == null || quantList.isEmpty()) ? null : quantList.get(0).getContainerId();
    }
}

