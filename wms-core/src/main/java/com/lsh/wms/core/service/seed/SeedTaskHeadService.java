package com.lsh.wms.core.service.seed;

import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.core.dao.seed.SeedingTaskHeadDao;
import com.lsh.wms.core.dao.taking.StockTakingHeadDao;
import com.lsh.wms.model.seed.SeedingTaskHead;
import com.lsh.wms.model.taking.StockTakingHead;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mali on 16/7/14.
 */

@Component
@Transactional(readOnly = true)
public class SeedTaskHeadService {
    private static final Logger logger = LoggerFactory.getLogger(SeedTaskHeadService.class);

    @Autowired
    private SeedingTaskHeadDao headDao;

    @Transactional(readOnly = false)
    public void create(SeedingTaskHead head) {
        head.setUpdatedAt(DateUtils.getCurrentSeconds());
        head.setCreatedAt(DateUtils.getCurrentSeconds());
        headDao.insert(head);
    }
    @Transactional(readOnly = false)
    public void update(SeedingTaskHead head) {
        head.setUpdatedAt(DateUtils.getCurrentSeconds());
        headDao.update(head);
    }
    public List<SeedingTaskHead> getHeadByOrderIdAndStoreNo(Long orderId,String storeNo ) {
        Map<String,Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("orderId",orderId);
        mapQuery.put("storeNo",storeNo);
        return headDao.getSeedingTaskHeadList(mapQuery);
    }
    public SeedingTaskHead getHeadByTaskId(Long  taskId) {
       return headDao.getSeedingTaskHeadByTaskId(taskId);
    }

}


