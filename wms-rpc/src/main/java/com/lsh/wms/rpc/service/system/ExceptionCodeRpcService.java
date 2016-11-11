package com.lsh.wms.rpc.service.system;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.wms.api.service.system.IExceptionCodeRpcService;
import com.lsh.wms.core.service.baseinfo.ExceptionCodeService;
import com.lsh.wms.model.baseinfo.BassinfoExceptionCode;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhanghongling on 16/11/10.
 */
@Service(protocol = "dubbo")
public class ExceptionCodeRpcService implements IExceptionCodeRpcService{
    @Autowired
    private ExceptionCodeService exceptionCodeService;

    public void insert(BassinfoExceptionCode bassinfoExceptionCode){

        exceptionCodeService.insert(bassinfoExceptionCode);
    }

    public void update(BassinfoExceptionCode bassinfoExceptionCode){
        bassinfoExceptionCode.setExceptionName(null);//例外代码名称不可修改
        exceptionCodeService.update(bassinfoExceptionCode);
    }

    public BassinfoExceptionCode getBassinfoExceptionCodeById(Long id){
        return exceptionCodeService.getBassinfoExceptionCodeById(id);
    }

    public Integer countBassinfoExceptionCode(Map<String, Object> params){
        return exceptionCodeService.countBassinfoExceptionCode(params);
    }

    public List<BassinfoExceptionCode> getBassinfoExceptionCodeList(Map<String, Object> params){
        return exceptionCodeService.getBassinfoExceptionCodeList(params);
    }

    public String getExceptionCodeByName(String exceptioName){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("exceptionName",exceptioName);
        params.put("status",1);
        List<BassinfoExceptionCode> list = exceptionCodeService.getBassinfoExceptionCodeList(params);
        if(list == null || list.size() == 0){
            return null;
        }else{
            return list.get(0).getExceptionCode();
        }

    }

}
