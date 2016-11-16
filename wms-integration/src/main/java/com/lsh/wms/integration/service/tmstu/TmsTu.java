package com.lsh.wms.integration.service.tmstu;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.config.PropertyUtils;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.net.HttpClientUtils;
import com.lsh.base.common.utils.BeanMapTransUtils;
import com.lsh.wms.api.service.merge.IMergeRpcService;
import com.lsh.wms.api.service.pick.IQCRpcService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.tmstu.ITmsTuService;
import com.lsh.wms.api.service.tu.ITuRpcService;
import com.lsh.wms.core.constant.CustomerConstant;
import com.lsh.wms.core.constant.TuConstant;
import com.lsh.wms.core.service.csi.CsiCustomerService;
import com.lsh.wms.core.service.tu.TuService;
import com.lsh.wms.core.service.utils.HttpUtils;
import com.lsh.wms.model.baseinfo.BaseinfoStore;
import com.lsh.wms.model.csi.CsiCustomer;
import com.lsh.wms.model.tu.TuDetail;
import com.lsh.wms.model.tu.TuHead;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fengkun on 2016/11/14.
 */
@Service(protocol = "rest")
@Path("tu")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class TmsTu implements ITmsTuService {
    private static Logger logger = LoggerFactory.getLogger(TmsTu.class);

    @Autowired
    private TuService tuService;
    @Autowired
    private CsiCustomerService csiCustomerService;

    @Reference
    private ITuRpcService iTuRpcService;
    @Reference
    private IMergeRpcService iMergeRpcService;
    @Reference
    private IQCRpcService iqcRpcService;


    /**
     * 接收TU头信息
     *
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("receiveTuHead")
    public String receiveTuHead() throws BizCheckedException {
        Map<String, Object> mapRequest = RequestUtils.getRequest();
        TuHead tuHead = iTuRpcService.receiveTuHead(mapRequest);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("response", true);
        return JsonUtils.SUCCESS(result);
    }

    /**
     * 大店的未装车板数列表
     *
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("superMarketUnloadList")
    public String superMarketUnloadList() throws BizCheckedException {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        return JsonUtils.SUCCESS(iMergeRpcService.getMergeList(mapQuery));
    }

    /**
     * 小店的未装车箱数列表
     *
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("storeUnloadList")
    public String storeUnloadList() throws BizCheckedException {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("status", 1); // 生效状态的 TODO: 待改为constant
        mapQuery.put("customerType", CustomerConstant.STORE); // 大店 TODO: 这个地方是字符串,目前数据量小先这样了,理论上应该为数字或者全部取出后遍历
        List<CsiCustomer> customers = csiCustomerService.getCustomerList(mapQuery);
        List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
        for (CsiCustomer customer: customers) {
            Map<String, Object> result = new HashMap<String, Object>();
            Map<Long, Map<String, Object>> qcResults = iqcRpcService.getGroupDetailByStoreNo(customer.getCustomerCode());
            BigDecimal packCount = BigDecimal.ZERO;
            Integer containerCounts = qcResults.size();
            Integer restContainers = 0;
            for (Map<String, Object> qcResult: qcResults.values()) {
                packCount = packCount.add(new BigDecimal(qcResult.get("packCount").toString()));
                if (Boolean.parseBoolean(qcResult.get("isRest").toString())) {
                    restContainers++;
                }
            }
            result.put("customerCode", customer.getCustomerCode());
            result.put("customerName", customer.getCustomerName());
            result.put("address", customer.getAddress());
            result.put("packCount", packCount);
            result.put("containerCounts", containerCounts);
            result.put("restContainers", restContainers);
            results.add(result);
        }
        return JsonUtils.SUCCESS(results);
    }
}
