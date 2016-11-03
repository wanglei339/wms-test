package com.lsh.wms.service.merge;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.merge.IMergeRestService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.wave.WaveDetail;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.List;
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
    @Autowired
    private WaveService waveService;

    @POST
    @Path("getMergeList")
    public String getMergeList() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        return JsonUtils.SUCCESS(mergeRpcService.getMergeList(mapQuery));
    }

    @POST
    @Path("countMergeList")
    public String countMergeList() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        return JsonUtils.SUCCESS(mergeRpcService.countMergeList(mapQuery));
    }

    @POST
    @Path("getMergeDetail")
    public String getMergeDetail() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        String storeNo = mapQuery.get("storeNo").toString();
        return JsonUtils.SUCCESS(mergeRpcService.getMergeDetailByStoreNo(storeNo));
    }

    @GET
    @Path("getWaveDetailByMergeConatinerId")
    public String getWaveDetailByMergeConatinerId(@QueryParam("mergeContainerId") Long mergeContainerId) throws BizCheckedException {
        //既可以用物理吗,也可以用托盘码
        List<WaveDetail> details = waveService.getWaveDetailsByMergedContainerId(mergeContainerId);
        if (null == details || details.size() < 1) {
            details = waveService.getAliveDetailsByContainerId(mergeContainerId);
            if (null == details || details.size() < 1) {
                throw new BizCheckedException("2990039");
            }
        }
        return JsonUtils.SUCCESS(details);
    }
}
