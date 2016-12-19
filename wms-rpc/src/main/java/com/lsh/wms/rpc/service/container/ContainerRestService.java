package com.lsh.wms.rpc.service.container;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.api.service.container.IContainerRestService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.core.service.container.ContainerService;
import com.lsh.wms.core.service.utils.IdGenerator;
import com.lsh.wms.model.baseinfo.BaseinfoContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

/**
 * Created by fengkun on 16/7/8.
 */

@Service(protocol = "rest")
@Path("container")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class ContainerRestService implements IContainerRestService {
    private static Logger logger = LoggerFactory.getLogger(ContainerRestService.class);

    @Autowired
    private ContainerRpcService containerRpcService;
    @Autowired
    private ContainerService containerService;

    @GET
    @Path("getContainer")
    public String getContainer(@QueryParam("containerId") long containerId) throws BizCheckedException {
        BaseinfoContainer containerInfo = containerRpcService.getContainer(containerId);
        if (containerInfo==null){
            //查找的容器不存在
            throw new BizCheckedException("2190001");
        }
        return JsonUtils.SUCCESS(containerInfo);
    }

    @POST
    @Path("insertContainer")
    public String insertContainer(BaseinfoContainer container) throws BizCheckedException {
        try {
            BaseinfoContainer containerInfo = containerRpcService.insertContainer(container);
            return JsonUtils.SUCCESS(containerInfo);
        } catch (Exception e){
            logger.error(e.getCause()!=null ? e.getCause().getMessage():e.getMessage());
            throw new BizCheckedException("2190002");
        }
    }

    @GET
    @Path("createContainerByType")
    public String createContainerByType(@QueryParam("type") Long type) {
        try{
            BaseinfoContainer container = containerService.createContainerByType(type);
            return JsonUtils.SUCCESS(container);
        }catch (Exception e){
            logger.error(e.getCause()!=null ? e.getCause().getMessage():e.getMessage());
            throw new BizCheckedException("2190003");
        }
    }
}
