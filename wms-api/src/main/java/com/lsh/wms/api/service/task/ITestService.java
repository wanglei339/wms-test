package com.lsh.wms.api.service.task;

import com.lsh.wms.model.task.Test;

/**
 * Created by mali on 16/8/11.
 */
public interface ITestService {
    String add(Long val);
    String update(Long id, Long val);
    String get(Long id);
    String test(Long id, Long val);
}
