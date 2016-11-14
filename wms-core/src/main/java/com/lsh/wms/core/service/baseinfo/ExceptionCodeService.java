package com.lsh.wms.core.service.baseinfo;

import com.lsh.wms.core.dao.baseinfo.BaseinfoExceptionCodeDao;
import com.lsh.wms.model.baseinfo.BaseinfoExceptionCode;
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
    private BaseinfoExceptionCodeDao baseinfoExceptionCodeDao;

    @Transactional(readOnly = false)
    public void insert(BaseinfoExceptionCode baseinfoExceptionCode){
        baseinfoExceptionCodeDao.insert(baseinfoExceptionCode);
    }

    @Transactional(readOnly = false)
    public void update(BaseinfoExceptionCode baseinfoExceptionCode){
        baseinfoExceptionCodeDao.update(baseinfoExceptionCode);
    }

    public BaseinfoExceptionCode getBaseinfoExceptionCodeById(Long id){
        return baseinfoExceptionCodeDao.getBaseinfoExceptionCodeById(id);
    }

    public Integer countBaseinfoExceptionCode(Map<String, Object> params){
        return baseinfoExceptionCodeDao.countBaseinfoExceptionCode(params);
    }

    public List<BaseinfoExceptionCode> getBaseinfoExceptionCodeList(Map<String, Object> params){
        return baseinfoExceptionCodeDao.getBaseinfoExceptionCodeList(params);
    }

}
