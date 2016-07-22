package com.lsh.wms.core.service.taking;

import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.core.dao.taking.StockTakingDetailDao;
import com.lsh.wms.core.dao.taking.StockTakingHeadDao;
import com.lsh.wms.model.taking.StockTakingDetail;
import com.lsh.wms.model.taking.StockTakingHead;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by mali on 16/7/14.
 */
@Component
@Transactional(readOnly = true)

public class StockTakingService {
    private static final Logger logger = LoggerFactory.getLogger(StockTakingDetail.class);

    @Autowired
    private StockTakingHeadDao headDao;

    @Autowired
    private StockTakingDetailDao detailDao;

    @Transactional (readOnly = false)
    public void createDetailList(List<StockTakingDetail> detailList) {
        for (StockTakingDetail detail : detailList) {
            detail.setCreatedAt(DateUtils.getCurrentSeconds());
            detail.setUpdatedAt(DateUtils.getCurrentSeconds());
        }
        detailDao.batchInsert(detailList);
    }

    @Transactional(readOnly = false)
    public void create(StockTakingHead head, List<StockTakingDetail> detailList) {
        head.setCreatedAt(DateUtils.getCurrentSeconds());
        head.setUpdatedAt(DateUtils.getCurrentSeconds());
        headDao.insert(head);

        this.createDetailList(detailList);
    }

    public List<StockTakingDetail> getFinalDetailList(Long stockTakingId,Long roundTime) {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("takingId", stockTakingId);
        mapQuery.put("round",roundTime);
        List<StockTakingDetail> detailList = detailDao.getStockTakingDetailList(mapQuery);

        List<StockTakingDetail> finalDetailList = new ArrayList<StockTakingDetail>();
        Long lastDetailId = 0L;
        for (StockTakingDetail detail : detailList) {
            if (detail.getDetailId().equals(lastDetailId)) {
                continue;
            }
            finalDetailList.add(detail);
        }
        return finalDetailList;
    }

    public StockTakingHead getHeadById(Long takingId) {
        return headDao.getStockTakingHeadById(takingId);
    }
    public Long chargeTime(Long stockTakingId) {
        Map queryMap = new HashMap();
        queryMap.put("stockTakingId",stockTakingId);
        queryMap.put("round", 3L);
        int i = detailDao.countStockTakingDetail(queryMap);
        if (i!=0){
            return 3L;
        }else {
            queryMap.put("round",2L);
            i=detailDao.countStockTakingDetail(queryMap);
            if (i!=0){
                return 2L;
            }
            return 1L;
        }
    }
}

