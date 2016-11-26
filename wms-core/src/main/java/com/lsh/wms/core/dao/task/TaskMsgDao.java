package com.lsh.wms.core.dao.task;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.task.TaskMsg;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface TaskMsgDao {

	void insert(TaskMsg taskMsg);
	
	void update(TaskMsg taskMsg);
	
	TaskMsg getTaskMsgByBusinessId(Long businessId);

    Integer countTaskMsg(Map<String, Object> params);

    List<TaskMsg> getTaskMsgList(Map<String, Object> params);
	
}