package com.lsh.wms.core.service.demo;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.dao.task.TaskInfoDao;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.model.task.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Project Name: lsh-wms
 * Created by fuhao
 * Date: 16/7/30
 * Time: 16/7/30.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.core.service.demo.
 * desc:类功能描述
 */
@Component
@Transactional(readOnly = true)
public class DemoService {
    @Autowired
    private TaskInfoDao taskInfoDao;
    @Autowired
    private BaseTaskService baseTaskService;

    @Transactional(readOnly = false)
    public void create(TaskInfo taskInfo) throws BizCheckedException {
        taskInfo.setType(1L);
        taskInfo.setItemId(1L);
        taskInfo.setTaskName("fuhaotest");
        taskInfo.setDraftTime(DateUtils.getCurrentSeconds());
        taskInfo.setStatus(TaskConstant.Draft);
        taskInfo.setCreatedAt(DateUtils.getCurrentSeconds());
        taskInfo.setUpdatedAt(DateUtils.getCurrentSeconds());
        taskInfoDao.insert(taskInfo);
        t(taskInfo);
    }

    public void t(TaskInfo taskInfo)throws BizCheckedException{
        // baseTaskService.create(taskInfo);
        // throw  new BizCheckedException("123","234");
    }
}
