package com.lsh.wms.core.service.back;

import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.core.dao.back.BackTaskDetailDao;
import com.lsh.wms.model.back.BackTaskDetail;
import com.lsh.wms.model.baseinfo.BaseinfoDepartment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by wuhao on 16/10/22.
 */

@Component
@Transactional(readOnly = true)
public class InStorageService {
    private static final Logger logger = LoggerFactory.getLogger(InStorageService.class);


    @Autowired
    private BackTaskDetailDao detailDao;

    @Transactional(readOnly = false)
    public void insertDetail(BackTaskDetail backTaskDetail){
        //增加新增时间
        backTaskDetail.setCreatedAt(DateUtils.getCurrentSeconds());
        backTaskDetail.setUpdatedAt(DateUtils.getCurrentSeconds());
        detailDao.insert(backTaskDetail);

    }
    @Transactional(readOnly = false)
    public void BatchInsertDetail(List<BackTaskDetail> details){
        for(BackTaskDetail backTaskDetail:details) {
            //增加新增时间
            backTaskDetail.setCreatedAt(DateUtils.getCurrentSeconds());
            backTaskDetail.setUpdatedAt(DateUtils.getCurrentSeconds());
            detailDao.insert(backTaskDetail);
        }

    }

    @Transactional(readOnly = false)
    public void updatedetail(BackTaskDetail backTaskDetail){
        //增加更新时间
        backTaskDetail.setUpdatedAt(DateUtils.getCurrentSeconds());
        detailDao.update(backTaskDetail);
    }
    public List<BackTaskDetail> getDetailByTaskId(Long TaskId) {
        Map<String,Object> queryMap = new HashMap<String, Object>();
        queryMap.put("taskId", TaskId);
         return detailDao.getBackTaskDetailList(queryMap);
    }


}
