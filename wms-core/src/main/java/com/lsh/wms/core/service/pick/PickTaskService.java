package com.lsh.wms.core.service.pick;

import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.core.dao.wave.WaveDetailDao;
import com.lsh.wms.core.dao.pick.PickTaskHeadDao;
import com.lsh.wms.core.dao.wave.WaveDetailDao;
import com.lsh.wms.model.pick.PickTaskHead;
import com.lsh.wms.model.wave.WaveDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zengwenjun on 16/7/15.
 */
@Component
@Transactional(readOnly = true)
public class PickTaskService {
    private static final Logger logger = LoggerFactory.getLogger(PickTaskService.class);

    @Autowired
    private PickTaskHeadDao taskHeadDao;
    @Autowired
    private WaveDetailDao taskDetailDao;

    @Transactional(readOnly = false)
    public int createPickTask(PickTaskHead head, List<WaveDetail> details){
        List<PickTaskHead> heads = new ArrayList<PickTaskHead>();
        heads.add(head);
        return this.createPickTasks(heads, details);
    }

    @Transactional(readOnly = false)
    public int createPickTasks(List<PickTaskHead> heads, List<WaveDetail> details){
        for(PickTaskHead head : heads){
            head.setCreatedAt(DateUtils.getCurrentSeconds());
            head.setUpdatedAt(DateUtils.getCurrentSeconds());
            taskHeadDao.insert(head);
        }
        for(WaveDetail detail : details){
            detail.setCreatedAt(DateUtils.getCurrentSeconds());
            detail.setUpdatedAt(DateUtils.getCurrentSeconds());
            taskDetailDao.insert(detail);
        }
        return 0;
    }

    public PickTaskHead getPickTaskHead(long iPickTaskId){
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("pickTaskId", iPickTaskId);
        List<PickTaskHead> pickTaskHeadList = taskHeadDao.getPickTaskHeadList(mapQuery);
        return pickTaskHeadList.size() == 0 ? null : pickTaskHeadList.get(0);
    }

    public List<WaveDetail> getPickTaskDetails(long iPickTaskId){
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("pickTaskId", iPickTaskId);
        return taskDetailDao.getWaveDetailList(mapQuery);
    }
}
