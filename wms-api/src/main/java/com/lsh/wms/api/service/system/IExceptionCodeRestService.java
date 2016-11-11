package com.lsh.wms.api.service.system;

import com.lsh.wms.model.baseinfo.BaseinfoExceptionCode;

import java.util.Map;

/**
 * Created by zhanghongling on 16/11/10.
 */
public interface IExceptionCodeRestService {
    String insert(BaseinfoExceptionCode baseinfoExceptionCode);
    String update(BaseinfoExceptionCode baseinfoExceptionCode);
    String getExceptonCodeList(Map<String, Object> mapQuery);
    String  getExceptonCodeListCount(Map<String, Object> mapQuery);
}
