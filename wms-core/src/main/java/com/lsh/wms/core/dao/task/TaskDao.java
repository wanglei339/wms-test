package com.lsh.wms.core.dao.task;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.task.Task;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface TaskDao {

	void insert(Task task);
	
	void update(Task task);
	
	Task getTaskById(Long id);

    Integer countTask(Map<String, Object> params);

    List<Task> getTaskList(Map<String, Object> params);
	
}