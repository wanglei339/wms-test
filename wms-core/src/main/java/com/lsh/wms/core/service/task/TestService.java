package com.lsh.wms.core.service.task;

import com.lsh.wms.core.dao.task.TestDao;
import com.lsh.wms.model.task.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by mali on 16/8/10
 */
@Component
@Transactional(readOnly = true)
public class TestService {

    @Autowired
    private TestDao dao;

    @Transactional(readOnly = false)
    public void create(Test test) {
        dao.insert(test);
    }

    @Transactional(readOnly = false)
    public Test get(Long id) {
        dao.lock();
        return dao.getTestById(id);
    }

    @Transactional(readOnly = false)
    public void update(Test test)  {
        dao.lock();
        try {
            Thread.sleep(5000L);
        } catch (Exception e) {
        }
        dao.update(test);
    }
}
