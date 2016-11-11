package com.lsh.wms.api.service.system;

import com.lsh.wms.model.baseinfo.BassinfoExceptionCode;

import java.util.Map;

/**
 * Created by zhanghongling on 16/11/10.
 */
public interface IExceptionCodeRestService {
    String insert(BassinfoExceptionCode bassinfoExceptionCode);
    String update(BassinfoExceptionCode bassinfoExceptionCode);
    String getExceptonCodeList(Map<String, Object> mapQuery);
    String  getExceptonCodeListCount(Map<String, Object> mapQuery);
}
