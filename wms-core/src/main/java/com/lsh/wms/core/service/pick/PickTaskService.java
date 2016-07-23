package com.lsh.wms.core.service.pick;

import com.lsh.wms.core.dao.pick.PickTaskDetailDao;
import com.lsh.wms.core.dao.pick.PickTaskHeadDao;
import com.lsh.wms.model.pick.PickTaskDetail;
import com.lsh.wms.model.pick.PickTaskHead;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zengwenjun on 16/7/15.
 */
@Component
@Transactional(readOnly = true)
public class PickTaskService {
    private static final Logger logger = LoggerFactory.getLogger(PickTaskService.class);

    @Resource(name = "pickTaskHeadDao")
    private PickTaskHeadDao taskHeadDao;
    @Autowired
    private PickTaskDetailDao taskDetailDao;

    public int createPickTask(PickTaskHead head, List<PickTaskDetail> details){
        taskHeadDao.insert(head);
        for(int i = 0; i < details.size(); ++i){
            taskDetailDao.insert(details.get(i));
        }
        return 0;
    }

    public int createPickTasks(List<PickTaskHead> heads, List<PickTaskDetail> details){
        for(int i = 0; i < heads.size(); i++){
            taskHeadDao.insert(heads.get(i));
        }
        for(int i = 0; i < details.size(); ++i){
            taskDetailDao.insert(details.get(i));
        }
        return 0;
    }

    public PickTaskHead getPickTaskHead(long iPickTaskId){
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("pickTaskId", iPickTaskId);
        List<PickTaskHead> pickTaskHeadList = taskHeadDao.getPickTaskHeadList(mapQuery);
        return pickTaskHeadList.size() == 0 ? null : pickTaskHeadList.get(0);
    }

    public List<PickTaskDetail> getPickTaskDetails(long iPickTaskId){
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("pickTaskId", iPickTaskId);
        return taskDetailDao.getPickTaskDetailList(mapQuery);
    }
}
