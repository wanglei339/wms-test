package com.lsh.wms.core.service.kanban;


import com.lsh.wms.core.dao.task.TaskInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by lixin-mac on 16/8/26.
 */
@Component
@Transactional(readOnly = true)
public class KanBanService {
    @Autowired
    private TaskInfoDao taskInfoDao;

    public List<Map<String,Object>> getKanBanCount(Long type){
        return taskInfoDao.getKanBanCount(type);
    }
}
