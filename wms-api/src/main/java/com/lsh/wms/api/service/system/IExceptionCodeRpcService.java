package com.lsh.wms.api.service.system;

import com.lsh.wms.model.baseinfo.BaseinfoExceptionCode;

import java.util.List;
import java.util.Map;

/**
 * Created by zhanghongling on 16/11/10.
 */
public interface IExceptionCodeRpcService {
     void insert(BaseinfoExceptionCode baseinfoExceptionCode);

     void update(BaseinfoExceptionCode baseinfoExceptionCode);

     BaseinfoExceptionCode getBaseinfoExceptionCodeById(Long id);

     Integer countBaseinfoExceptionCode(Map<String, Object> params);

     List<BaseinfoExceptionCode> getBaseinfoExceptionCodeList(Map<String, Object> params);

     String  getExceptionCodeByName(String exceptioName);

}
