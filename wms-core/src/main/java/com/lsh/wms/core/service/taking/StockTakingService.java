package com.lsh.wms.core.service.taking;

import com.alibaba.fastjson.JSON;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.RandomUtils;
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
    private void insertHead(StockTakingHead head) {
        head.setCreatedAt(DateUtils.getCurrentSeconds());
        head.setUpdatedAt(DateUtils.getCurrentSeconds());
        headDao.insert(head);
    }

    @Transactional (readOnly = false)
    private void updateHead(StockTakingHead head) {
        head.setUpdatedAt(DateUtils.getCurrentSeconds());
        headDao.update(head);
    }

    @Transactional (readOnly = false)
    public void insertDetailList(List<StockTakingDetail> detailList) {
        for (StockTakingDetail detail : detailList) {
            detail.setCreatedAt(DateUtils.getCurrentSeconds());
            detail.setUpdatedAt(DateUtils.getCurrentSeconds());
        }
        detailDao.batchInsert(detailList);
    }

    @Transactional(readOnly = false)
    public void updateDetail(StockTakingDetail detail) {
        detail.setUpdatedAt(DateUtils.getCurrentSeconds());
        detailDao.update(detail);
    }

    @Transactional(readOnly = false)
    public void create(StockTakingHead head, List<StockTakingDetail> detailList) {
        Long takingId = RandomUtils.genId();
        head.setTakingId(takingId);
        this.insertHead(head);
        for (StockTakingDetail detail : detailList) {
            detail.setTakingId(takingId);
        }
        this.insertDetailList(detailList);
    }

    public List<StockTakingDetail> getDetailListByRound(Long stockTakingId, Long round) {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("takingId", stockTakingId);
        mapQuery.put("round", round);
        List<StockTakingDetail> detailList = detailDao.getStockTakingDetailList(mapQuery);
        return detailList;
    }

    public StockTakingHead getHeadById(Long takingId) {
        return headDao.getStockTakingHeadById(takingId);
    }
    public Long chargeTime(Long stockTakingId) {
        Map queryMap = new HashMap();
        queryMap.put("takingId",stockTakingId);
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
    public List<StockTakingHead> queryTakingHead(Map queryMap) {
        return headDao.getStockTakingHeadList(queryMap);
    }
    public List<StockTakingDetail> getDetailByTaskId(Long taskId){
        Map<String,Object> queryMap = new HashMap<String, Object>();
        queryMap.put("taskId", taskId);
        return detailDao.getStockTakingDetailList(queryMap);

    }
    public Integer countHead(Map queryMap) {
        return headDao.countStockTakingHead(queryMap);

    }
}

