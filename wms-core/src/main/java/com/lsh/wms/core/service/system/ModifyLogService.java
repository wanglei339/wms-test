package com.lsh.wms.core.service.system;

import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.core.dao.system.ModifyLogDao;
import com.lsh.wms.model.system.ModifyLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by lixin-mac on 2016/12/23.
 */
@Component
@Transactional(readOnly = true)
public class ModifyLogService {
    private static Logger logger = LoggerFactory.getLogger(ModifyLogService.class);

    @Autowired
    private ModifyLogDao modifyLogDao;

    @Transactional(readOnly = false)
    public void addModifyLog(ModifyLog modifyLog){
        modifyLog.setModifyId(RandomUtils.genId());
        modifyLog.setUpdatedAt(DateUtils.getCurrentSeconds());
        modifyLog.setCreatedAt(DateUtils.getCurrentSeconds());
        modifyLogDao.insert(modifyLog);
    }

}
