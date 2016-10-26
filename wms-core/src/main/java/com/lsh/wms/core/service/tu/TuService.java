package com.lsh.wms.core.service.tu;

import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.core.dao.tu.TuDetailDao;
import com.lsh.wms.core.dao.tu.TuHeadDao;
import com.lsh.wms.model.tu.TuDetail;
import com.lsh.wms.model.tu.TuHead;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 2016/10/19 下午8:42
 */
@Component
@Transactional(readOnly = true)
public class TuService {
    private static final Logger logger = LoggerFactory.getLogger(TuService.class);

    @Autowired
    private TuHeadDao tuHeadDao;
    @Autowired
    private TuDetailDao tuDetailDao;

    @Transactional(readOnly = false)
    public void create(TuHead head) {
        head.setUpdatedAt(DateUtils.getCurrentSeconds());
        head.setCreatedAt(DateUtils.getCurrentSeconds());
        tuHeadDao.insert(head);
    }

    @Transactional(readOnly = false)
    public void update(TuHead head) {
        head.setUpdatedAt(DateUtils.getCurrentSeconds());
        tuHeadDao.update(head);
    }

    public TuHead getHeadByTuId(String tuId) {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("isValid", 1);
        mapQuery.put("tuId", tuId);
        List<TuHead> tuHeads = tuHeadDao.getTuHeadList(mapQuery);
        return (tuHeads != null && tuHeads.size() > 0) ? tuHeads.get(0) : null;
    }

    public List<TuHead> getTuHeadList(Map<String, Object> mapQuery) {
        mapQuery.put("isValid", 1);
        return tuHeadDao.getTuHeadList(mapQuery);
    }

    public List<TuHead> getTuHeadListOnPc(Map<String, Object> params) {
        params.put("isValid", 1);
        return tuHeadDao.getTuHeadListOnPc(params);
    }

    public Integer countTuHeadOnPc(Map<String, Object> mapQuery) {
        mapQuery.put("isValid", 1);
        return tuHeadDao.countTuHeadOnPc(mapQuery);
    }

    public Integer countTuHead(Map<String, Object> mapQuery) {
        mapQuery.put("isValid", 1);
        return tuHeadDao.countTuHead(mapQuery);
    }

    @Transactional(readOnly = false)
    public TuHead removeTuHead(TuHead tuHead) {
        tuHead.setIsValid(0);   //无效
        this.update(tuHead);
        return tuHead;
    }

    @Transactional(readOnly = false)
    public void create(TuDetail head) {
        head.setUpdatedAt(DateUtils.getCurrentSeconds());
        head.setCreatedAt(DateUtils.getCurrentSeconds());
        tuDetailDao.insert(head);
    }

    @Transactional(readOnly = false)
    public void update(TuDetail head) {
        head.setUpdatedAt(DateUtils.getCurrentSeconds());
        tuDetailDao.update(head);
    }

    public TuDetail getDetailById(Long id) {
        return tuDetailDao.getTuDetailById(id);
    }

    /**
     * 根据合板的板id查找detail
     * 板子的id是tuDetail表的唯一key
     *
     * @param boardId
     * @return
     */
    public TuDetail getDetailByBoardId(Long boardId) {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("isValid", 1);
        mapQuery.put("mergedContainerId", boardId);
        List<TuDetail> tuDetails = tuDetailDao.getTuDetailList(mapQuery);
        return (tuDetails != null && tuDetails.size() > 0) ? tuDetails.get(0) : null;
    }

    @Transactional(readOnly = false)
    public TuDetail removeTuDetail(TuDetail detail) {
        detail.setIsValid(0);   //无效
        this.update(detail);
        return detail;
    }

    public List<TuDetail> getTuDeailList(Map<String, Object> mapQuery) {
        mapQuery.put("isValid", 1);
        return tuDetailDao.getTuDetailList(mapQuery);
    }

    public List<TuDetail> getTuDeailListByMergedContainerId(Long mergedContainerId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("mergedContainerId", mergedContainerId);
        params.put("isValid", 1);
        return tuDetailDao.getTuDetailList(params);
    }

    public Integer countTuDetail(Map<String, Object> mapQuery) {
        mapQuery.put("isValid", 1);
        return tuDetailDao.countTuDetail(mapQuery);
    }

    /**
     * 通过tu号和门店编码找详情(多条,多板子)
     *
     * @param tuId         运单号
     * @param deliveryCode 门店号
     * @return
     */
    public List<TuDetail> getTuDetailByStoreCode(String tuId, String deliveryCode) {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("tuId", tuId);
        mapQuery.put("deliveryCode", deliveryCode);  //门店号
        return this.getTuDeailList(mapQuery);
    }


}
