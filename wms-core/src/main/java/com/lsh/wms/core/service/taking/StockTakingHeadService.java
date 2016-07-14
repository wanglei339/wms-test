package com.lsh.wms.core.service.taking;

import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.core.dao.taking.StockTakingDetailDao;
import com.lsh.wms.core.dao.taking.StockTakingHeadDao;
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
public class StockTakingHeadService {
    private static final Logger logger = LoggerFactory.getLogger(StockTakingHeadService.class);

    @Autowired
    private StockTakingHeadDao headDao;

    @Transactional(readOnly = false)
    public void create(StockTakingHead head) {
        head.setUpdatedAt(DateUtils.getCurrentSeconds());
        head.setCreatedAt(DateUtils.getCurrentSeconds());
        headDao.insert(head);
    }


}


