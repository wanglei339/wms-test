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
import com.lsh.wms.core.service.store.StoreService;
import com.lsh.wms.core.service.tu.TuService;
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
     * 使用POST方式将TU发车
     *
     * @param tuId
     * @throws BizCheckedException
     */
    public Boolean postTuDetails(String tuId) throws BizCheckedException {
        TuHead tuHead = tuService.getHeadByTuId(tuId);
        Map<String, String> result = new HashMap<String, String>();
        String responseBody = "";
        if (tuHead == null) {
            throw new BizCheckedException("2990022");
        }
        if (!tuHead.getStatus().equals(TuConstant.SHIP_OVER)) {
            throw new BizCheckedException("2990037");
        }
        List<TuDetail> tuDetails = tuService.getTuDeailListByTuId(tuId);
        List<Map<String, Object>> details = new ArrayList<Map<String, Object>>();
        for (TuDetail tuDetail : tuDetails) {
            Map<String, Object> detail = BeanMapTransUtils.Bean2map(tuDetail);
            CsiCustomer csiCustomer = csiCustomerService.getCustomerByCustomerId(tuDetail.getStoreId());
            detail.put("customerCode", csiCustomer.getCustomerCode());
            detail.put("customerName", csiCustomer.getCustomerName());
            details.add(detail);
        }
        result.put("tuId", tuId);
        result.put("tuHead", JSON.toJSONString(tuHead));
        result.put("scale", tuHead.getScale().toString());
        result.put("tuDetails", JSON.toJSONString(details));
        String url = PropertyUtils.getString("tms_ship_over_url");
        int timeout = PropertyUtils.getInt("tms_timeout");
        String charset = PropertyUtils.getString("tms_charset");
        Map<String, String> headMap = new HashMap<String, String>();
        headMap.put("Content-type", "application/x-www-form-urlencoded; charset=utf-8");
        headMap.put("Accept", "*/*");
        logger.info("[SHIP OVER]Begin to transfer to TMS, " + "URL: " + url + ", Request body: " + JSON.toJSONString(result));
        try {
            responseBody = HttpClientUtils.post(url, result, timeout, charset, headMap);
        } catch (Exception e) {
            logger.info("[SHIP OVER]Transfer to TMS failed: " + responseBody);
            return false;
        }
        logger.info("[SHIP OVER]Transfer to TMS success: " + responseBody);
        return true;
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
        mapQuery.put("customerType", CustomerConstant.SUPER_MARKET); // 大店 TODO: 这个地方是字符串,目前数据量小先这样了,理论上应该为数字或者全部取出后遍历
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
