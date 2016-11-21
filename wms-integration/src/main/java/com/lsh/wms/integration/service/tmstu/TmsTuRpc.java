package com.lsh.wms.integration.service.tmstu;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.config.PropertyUtils;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.BeanMapTransUtils;
import com.lsh.wms.api.service.tmstu.ITmsTuRpcService;
import com.lsh.wms.core.constant.TuConstant;
import com.lsh.wms.core.service.csi.CsiCustomerService;
import com.lsh.wms.core.service.tu.TuService;
import com.lsh.wms.core.service.utils.HttpUtils;
import com.lsh.wms.model.csi.CsiCustomer;
import com.lsh.wms.model.tu.TuDetail;
import com.lsh.wms.model.tu.TuHead;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fengkun on 2016/11/16.
 */
@Service(protocol = "dubbo")
public class TmsTuRpc implements ITmsTuRpcService{
    private static Logger logger = LoggerFactory.getLogger(TmsTu.class);

    @Autowired
    private TuService tuService;
    @Autowired
    private CsiCustomerService csiCustomerService;

    /**
     * 使用POST方式将TU发车
     *
     * @param tuId
     * @throws BizCheckedException
     */
    public Boolean postTuDetails(String tuId) throws BizCheckedException {
        TuHead tuHead = tuService.getHeadByTuId(tuId);
        Map<String, Object> result = new HashMap<String, Object>();
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
        result.put("tuHead", tuHead);
        result.put("scale", tuHead.getScale().toString());
        result.put("tuDetails", details);
        String url = PropertyUtils.getString("tms_ship_over_url");
        /*int timeout = PropertyUtils.getInt("tms_timeout");
        String charset = PropertyUtils.getString("tms_charset");
        Map<String, String> headMap = new HashMap<String, String>();
        headMap.put("Content-type", "application/x-www-form-urlencoded; charset=utf-8");*/
        // headMap.put("Accept", "**/*//*");
        logger.info("[SHIP OVER]Begin to transfer to TMS, " + "URL: " + url + ", Request body: " + JSON.toJSONString(result));
        try {
            // responseBody = HttpClientUtils.post(url, result, timeout, charset, headMap);
            responseBody = HttpUtils.doPostByForm(url, result);
        } catch (Exception e) {
            logger.info("[SHIP OVER]Transfer to TMS failed: " + responseBody);
            return false;
        }
        logger.info("[SHIP OVER]Transfer to TMS success: " + responseBody);
        return true;
    }
}
