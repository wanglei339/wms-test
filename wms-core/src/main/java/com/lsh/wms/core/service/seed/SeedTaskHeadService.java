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


}


