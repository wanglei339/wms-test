package com.lsh.wms.core.service.transfer;

import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.core.dao.transfer.StockTransferTaskDetailDao;
import com.lsh.wms.model.transfer.StockTransferTaskDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Ming on 8/16/16.
 */

@Component
@Transactional(readOnly = false)

public class StockTransferTaskDetailService {
    private static final Logger logger = LoggerFactory.getLogger(StockTransferTaskDetailService.class);

    @Autowired
    private StockTransferTaskDetailDao taskDetailDao;

    @Transactional(readOnly = false)
    public void create(List<StockTransferTaskDetail> taskDetailList) {
        for (StockTransferTaskDetail detail : taskDetailList) {
            detail.setCreatedAt(DateUtils.getCurrentSeconds());
            detail.setUpdatedAt(DateUtils.getCurrentSeconds());
        }
        taskDetailDao.batchInsert(taskDetailList);
    }
}
