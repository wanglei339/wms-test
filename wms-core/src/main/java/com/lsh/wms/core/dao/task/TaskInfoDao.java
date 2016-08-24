package com.lsh.wms.core.dao.task;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.po.InbPoDetail;
import com.lsh.wms.model.task.TaskInfo;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface TaskInfoDao {

	void insert(TaskInfo taskInfo);
	
	void update(TaskInfo taskInfo);
	
	TaskInfo getTaskInfoById(Long id);

    Integer countTaskInfo(Map<String, Object> params);

    List<TaskInfo> getTaskInfoList(Map<String, Object> params);

	List<Map<String, Object>> getPerformance(Map<String, Object> params);

	Integer getPerformanceCount(Map<String, Object> params);
}