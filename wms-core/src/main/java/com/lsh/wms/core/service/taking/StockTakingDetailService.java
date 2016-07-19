package com.lsh.wms.core.service.taking;

import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.core.dao.taking.StockTakingDetailDao;
import com.lsh.wms.model.taking.StockTakingDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by mali on 16/7/14.
 */

@Component
@Transactional(readOnly = false)

public class StockTakingDetailService {
    private static final Logger logger = LoggerFactory.getLogger(StockTakingDetailService.class);

    @Autowired
    private StockTakingDetailDao detailDao;

    @Transactional(readOnly = false)
    public void create(List<StockTakingDetail> detailList) {
        for (StockTakingDetail detail : detailList) {
            detail.setUpdatedAt(DateUtils.getCurrentSeconds());
            detail.setCreatedAt(DateUtils.getCurrentSeconds());
        }
        detailDao.batchInsert(detailList);
    }
}
