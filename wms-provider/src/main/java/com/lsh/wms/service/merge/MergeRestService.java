package com.lsh.wms.service.merge;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.merge.IMergeRestService;
import com.lsh.wms.api.service.request.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.Map;

/**
 * Created by fengkun on 2016/10/14.
 */
@Service(protocol = "rest")
@Path("outbound/merge")
public class MergeRestService implements IMergeRestService {
    private static Logger logger = LoggerFactory.getLogger(MergeRestService.class);

    @Autowired
    private MergeRpcService mergeRpcService;

    @POST
    @Path("getMergeList")
    public String getMergeList() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        return JsonUtils.SUCCESS(mergeRpcService.getMergeList(mapQuery));
    }

    @POST
    @Path("getMergeCount")
    public String getMergeCount() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        return JsonUtils.SUCCESS(mergeRpcService.countMergeList(mapQuery));
    }
}
