package com.lsh.wms.rf.service.outbound;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.merge.IMergeRestService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.core.service.merge.MergeService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by fengkun on 2016/10/11.
 */
@Service(protocol = "rest")
@Path("outbound/merge")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class MergeRestService implements IMergeRestService {
    private static Logger logger = LoggerFactory.getLogger(PickRestService.class);

    @Autowired
    private MergeService mergeService;

    /**
     * 扫描托盘码进行合板
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("mergeContainers")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String mergeContainers() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        Long staffId = Long.valueOf(RequestUtils.getHeader("uid"));
        List<Long> containerIds = new ArrayList<Long>();
        List<Long> queryContainerIds = new ArrayList<Long>();
        if (mapQuery.get("containerIds") instanceof ArrayList<?>) {
            queryContainerIds = (ArrayList<Long>) mapQuery.get("containerIds");
        } else {
            throw new BizCheckedException("2870001");
        }
        for (Object objContainerId: queryContainerIds) {
            Long containerId = Long.valueOf(objContainerId.toString());
            if (!containerIds.contains(containerId)) {
                containerIds.add(containerId);
            }
        }
        if (containerIds.size() <= 1) {
            throw new BizCheckedException("2870005");
        }
        // 合板
        mergeService.mergeContainers(containerIds, staffId);
        return JsonUtils.SUCCESS();
    }
}
