package com.lsh.wms.core.service.shelve;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.core.dao.shelve.AtticShelveTaskDetailDao;
import com.lsh.wms.model.shelve.AtticShelveTaskDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wuhao on 16/8/16.
 */
@Component
@Transactional(readOnly = true)
public class AtticShelveTaskDetailService {
    @Autowired
    private AtticShelveTaskDetailDao detailDao;

    @Transactional(readOnly = false)
    public void create(AtticShelveTaskDetail detail) {
        detail.setCreatedAt(DateUtils.getCurrentSeconds());
        detail.setUpdatedAt(DateUtils.getCurrentSeconds());
        detailDao.insert(detail);
    }
    @Transactional(readOnly = false)
    public void batchCreate(List<AtticShelveTaskDetail> details) {
        for(AtticShelveTaskDetail detail:details) {
            detail.setCreatedAt(DateUtils.getCurrentSeconds());
            detail.setUpdatedAt(DateUtils.getCurrentSeconds());
            detailDao.insert(detail);
        }
    }

    @Transactional(readOnly = false)
    public void assign(Long taskId, Long staffId) {
        Map<String,Object> queryMap = new HashMap<String, Object>();
        queryMap.put("taskId", taskId);
        List<AtticShelveTaskDetail> details = detailDao.getAtticShelveTaskDetailList(queryMap);
        for(AtticShelveTaskDetail detail:details) {
            detail.setOperator(staffId);
            detail.setUpdatedAt(DateUtils.getCurrentSeconds());
            detailDao.update(detail);
        }
    }

    public List<AtticShelveTaskDetail> getShelveTaskDetail(Long taskId) {
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("taskId", taskId);
        List<AtticShelveTaskDetail> taskDetails = detailDao.getAtticShelveTaskDetailList(mapQuery);

        return taskDetails;
    }
    public AtticShelveTaskDetail getShelveTaskDetail(Long taskId,Long allocLocationId) {
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("taskId", taskId);
        mapQuery.put("allocLocationId",allocLocationId);
        List<AtticShelveTaskDetail> taskDetails = detailDao.getAtticShelveTaskDetailList(mapQuery);
        if(taskDetails ==null ||taskDetails.size()==0){
            return null;
        }else {
            return taskDetails.get(0);
        }

    }
    @Transactional(readOnly = false)
    public void updateDetail(AtticShelveTaskDetail detail) {
        detail.setUpdatedAt(DateUtils.getCurrentSeconds());
        detailDao.update(detail);

    }
}
