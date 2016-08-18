package com.lsh.wms.core.service.task;

import com.lsh.wms.core.dao.task.TaskTriggerDao;
import com.lsh.wms.model.task.TaskTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mali on 16/8/12.
 */
@Component
public class TaskTriggerService {
    @Autowired
    private TaskTriggerDao dao;

    public Map<String, List<TaskTrigger>> getAll() {
        Map<String, List<TaskTrigger>> m = new HashMap<String, List<TaskTrigger>>();
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("is_valid", 1L);
        List<TaskTrigger> triggerList = dao.getTaskTriggerList(condition);
        for (TaskTrigger trigger : triggerList) {
            String key = trigger.getOriType() + trigger.getOriSubType() + trigger.getOriMethod();
            if (trigger.getTimming().equals(3L)) {
                key += trigger.getException();
            } else {
                key += trigger.getTimming();
            }
            if (m.get(key) == null) {
                List<TaskTrigger> list = new ArrayList<TaskTrigger>();
                list.add(trigger);
                m.put(key, list);
            } else {
                List<TaskTrigger> list = m.get(key);
                list.add(trigger);
                m.put(key, list);
            }
        }
        return m;
    }
}
