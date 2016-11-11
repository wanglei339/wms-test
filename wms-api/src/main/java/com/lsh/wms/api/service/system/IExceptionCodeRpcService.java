package com.lsh.wms.api.service.system;

import com.lsh.wms.model.baseinfo.BassinfoExceptionCode;

import java.util.List;
import java.util.Map;

/**
 * Created by zhanghongling on 16/11/10.
 */
public interface IExceptionCodeRpcService {
     void insert(BassinfoExceptionCode bassinfoExceptionCode);

     void update(BassinfoExceptionCode bassinfoExceptionCode);

     BassinfoExceptionCode getBassinfoExceptionCodeById(Long id);

     Integer countBassinfoExceptionCode(Map<String, Object> params);

     List<BassinfoExceptionCode> getBassinfoExceptionCodeList(Map<String, Object> params);

     String  getExceptionCodeByName(String exceptioName);

}
