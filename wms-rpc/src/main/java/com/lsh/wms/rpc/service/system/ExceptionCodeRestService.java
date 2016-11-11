package com.lsh.wms.rpc.service.system;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.system.IExceptionCodeRestService;
import com.lsh.wms.model.baseinfo.BaseinfoExceptionCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Map;

/**
 * Created by zhanghongling on 16/11/10.
 */
@Service(protocol = "rest")
@Path("exceptionCode")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class ExceptionCodeRestService implements IExceptionCodeRestService{
    private static Logger logger = LoggerFactory.getLogger(ExceptionCodeRestService.class);

    @Autowired
    private ExceptionCodeRpcService exceptionCodeRpcService;

    @POST
    @Path("insertExceptonCode")
    public String insert(BaseinfoExceptionCode baseinfoExceptionCode) {
        try {
            exceptionCodeRpcService.insert(baseinfoExceptionCode);
        }catch (Exception e){
            logger.error(e.getMessage());
            return JsonUtils.EXCEPTION_ERROR("insert failed");
        }

        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("updateExceptonCode")
    public String update(BaseinfoExceptionCode baseinfoExceptionCode) {
        try {
            exceptionCodeRpcService.update(baseinfoExceptionCode);
        }catch (Exception e){
            logger.error(e.getMessage());
            return JsonUtils.EXCEPTION_ERROR("update failed");
        }

        return JsonUtils.SUCCESS();
    }
    @POST
    @Path("getExceptonCodeList")
    public String getExceptonCodeList(Map<String, Object> mapQuery) {
            return JsonUtils.SUCCESS(exceptionCodeRpcService.getBaseinfoExceptionCodeList(mapQuery));
    }
    @POST
    @Path("getExceptonCodeListCount")
    public String  getExceptonCodeListCount(Map<String, Object> mapQuery) {
        return JsonUtils.SUCCESS(exceptionCodeRpcService.countBaseinfoExceptionCode(mapQuery));
    }
}
