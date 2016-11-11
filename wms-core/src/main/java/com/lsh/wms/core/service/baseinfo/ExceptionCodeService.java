package com.lsh.wms.core.service.baseinfo;

import com.lsh.wms.core.dao.baseinfo.BassinfoExceptionCodeDao;
import com.lsh.wms.model.baseinfo.BassinfoExceptionCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 例外代码
 * Created by zhanghongling on 16/11/10.
 */
@Component
@Transactional(readOnly = true)
public class ExceptionCodeService {
    @Autowired
    private BassinfoExceptionCodeDao bassinfoExceptionCodeDao;

    public void insert(BassinfoExceptionCode bassinfoExceptionCode){
        bassinfoExceptionCodeDao.insert(bassinfoExceptionCode);
    }

    public void update(BassinfoExceptionCode bassinfoExceptionCode){
        bassinfoExceptionCodeDao.update(bassinfoExceptionCode);
    }

    public BassinfoExceptionCode getBassinfoExceptionCodeById(Long id){
        return bassinfoExceptionCodeDao.getBassinfoExceptionCodeById(id);
    }

    public Integer countBassinfoExceptionCode(Map<String, Object> params){
        return bassinfoExceptionCodeDao.countBassinfoExceptionCode(params);
    }

    public List<BassinfoExceptionCode> getBassinfoExceptionCodeList(Map<String, Object> params){
        return bassinfoExceptionCodeDao.getBassinfoExceptionCodeList(params);
    }

}
