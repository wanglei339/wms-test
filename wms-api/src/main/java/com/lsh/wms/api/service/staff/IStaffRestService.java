package com.lsh.wms.api.service.staff;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.wms.model.baseinfo.BaseinfoDepartment;

/**
 * Created by wulin on 16/7/9.
 */

@Service(protocol = 'dubbo')
public interface IStaffRestService {
    public List<BaseinfoDepartment> getDepartmentList(Map<String, Object> mapQuery);
}
