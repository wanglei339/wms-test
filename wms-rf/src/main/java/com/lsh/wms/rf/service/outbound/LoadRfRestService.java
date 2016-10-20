package com.lsh.wms.rf.service.outbound;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.service.tu.ILoadRfRestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 2016/10/20 下午8:18
 */
@Service(protocol = "rest")
@Path("outbound/load")
@Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class LoadRfRestService implements ILoadRfRestService{
    private static Logger logger = LoggerFactory.getLogger(LoadRfRestService.class);

    public String getTuHeadListByLoadStatus(Integer status) throws BizCheckedException {
        return null;
    }

    public String loadBoard() throws BizCheckedException {
        return null;
    }

    public String confirmLoad() throws BizCheckedException {
        return null;
    }
}
