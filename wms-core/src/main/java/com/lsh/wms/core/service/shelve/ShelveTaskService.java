package com.lsh.wms.core.service.shelve;

import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.dao.shelve.ShelveTaskHeadDao;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.model.shelve.ShelveTaskHead;
import com.lsh.wms.model.task.TaskInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * Created by fengkun on 16/7/25.
 */
@Component
@Transactional(readOnly = true)
public class ShelveTaskService extends BaseTaskService {
    private static final Logger logger = LoggerFactory.getLogger(ShelveTaskService.class);

    @Autowired
    private ShelveTaskHeadDao taskHeadDao;

    @Transactional(readOnly = false)
    public void create(ShelveTaskHead taskHead) {
        taskHead.setCreatedAt(DateUtils.getCurrentSeconds());
        taskHead.setUpdatedAt(DateUtils.getCurrentSeconds());
        taskHeadDao.insert(taskHead);
    }

    @Transactional(readOnly = false)
    public void assign(Long taskId, Long staffId) {
        ShelveTaskHead taskHead = taskHeadDao.getShelveTaskHeadById(taskId);
        taskHead.setShelveUid(staffId);
        taskHead.setUpdatedAt(DateUtils.getCurrentSeconds());
        taskHeadDao.update(taskHead);
    }

    @Transactional(readOnly = false)
    public void done(Long taskId, Long locationId) {
        ShelveTaskHead taskHead = taskHeadDao.getShelveTaskHeadById(taskId);
        taskHead.setRealLocationId(locationId);
        taskHead.setShelveAt(DateUtils.getCurrentSeconds());
        taskHeadDao.update(taskHead);
    }

    public ShelveTaskHead getShelveTaskHead(Long taskId) {
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("taskId", taskId);
        List<ShelveTaskHead> taskHeads = taskHeadDao.getShelveTaskHeadList(mapQuery);
        if (taskHeads.size() != 1) {
            return null;
        }
        return taskHeads.get(0);
    }
}
