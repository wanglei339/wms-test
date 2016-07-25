package com.lsh.wms.core.dao.task;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.task.TaskInfo;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface TaskInfoDao {

	void insert(TaskInfo task);
	
	void update(TaskInfo task);
	
	TaskInfo getTaskInfoById(Long taskId);

    Integer countTaskInfo(Map<String, Object> params);

    List<TaskInfo> getTaskInfoList(Map<String, Object> params);
	
}