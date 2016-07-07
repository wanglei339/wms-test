package com.lsh.wms.core.dao.system;


import com.lsh.wms.model.system.SysFunction;
import com.lsh.wms.core.dao.MyBatisRepository;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface SysFunctionDao {

    void insert(SysFunction sysFunction);

    void update(SysFunction sysFunction);

    SysFunction getSysFunctionById(Integer id);

    Integer countSysFunction(Map<String, Object> params);

    List<SysFunction> getSysFunctionList(Map<String, Object> params);

    List<SysFunction> getUserSysFunctionList(Map<String, Object> params);

}