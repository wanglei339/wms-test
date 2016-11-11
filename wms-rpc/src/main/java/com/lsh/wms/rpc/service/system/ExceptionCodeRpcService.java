package com.lsh.wms.rpc.service.system;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.wms.api.service.system.IExceptionCodeRpcService;
import com.lsh.wms.core.service.baseinfo.ExceptionCodeService;
import com.lsh.wms.model.baseinfo.BaseinfoExceptionCode;
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

    public void insert(BaseinfoExceptionCode baseinfoExceptionCode){

        exceptionCodeService.insert(baseinfoExceptionCode);
    }

    public void update(BaseinfoExceptionCode baseinfoExceptionCode){
        baseinfoExceptionCode.setExceptionName(null);//例外代码名称不可修改
        exceptionCodeService.update(baseinfoExceptionCode);
    }

    public BaseinfoExceptionCode getBaseinfoExceptionCodeById(Long id){
        return exceptionCodeService.getBaseinfoExceptionCodeById(id);
    }

    public Integer countBaseinfoExceptionCode(Map<String, Object> params){
        return exceptionCodeService.countBaseinfoExceptionCode(params);
    }

    public List<BaseinfoExceptionCode> getBaseinfoExceptionCodeList(Map<String, Object> params){
        return exceptionCodeService.getBaseinfoExceptionCodeList(params);
    }

    public String getExceptionCodeByName(String exceptioName){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("exceptionName",exceptioName);
        params.put("status",1);
        List<BaseinfoExceptionCode> list = exceptionCodeService.getBaseinfoExceptionCodeList(params);
        if(list == null || list.size() == 0){
            return null;
        }else{
            return list.get(0).getExceptionCode();
        }

    }

}
