package com.lsh.wms.core.dao.task;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.task.TaskTrigger;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface TaskTriggerDao {

	void insert(TaskTrigger taskTrigger);
	
	void update(TaskTrigger taskTrigger);
	
	TaskTrigger getTaskTriggerById(Long id);

    Integer countTaskTrigger(Map<String, Object> params);

    List<TaskTrigger> getTaskTriggerList(Map<String, Object> params);
	
}