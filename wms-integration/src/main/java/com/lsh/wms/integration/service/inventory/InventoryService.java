package com.lsh.wms.integration.service.inventory;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.CollectionUtils;
import com.lsh.wms.api.service.inventory.IInventoryService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.stock.StockSummaryService;
import com.lsh.wms.model.stock.StockSummary;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mali on 16/12/14.
 */
@Service(protocol = "rest", validation = "true")
@Path("inventory")
@Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class InventoryService implements IInventoryService {

    private static Logger logger = LoggerFactory.getLogger(InventoryService.class);

    @Autowired
    private StockSummaryService stockSummaryService;

    @Autowired
    private LocationService locationService;

    @POST
    @Path("getAvailQty")
    public String getAvailQty() throws BizCheckedException {
        Map<String, Object> request = RequestUtils.getRequest();
        if (request.get("ownerId") == null) {
            return JsonUtils.TOKEN_ERROR("ownerId不得为空！");
        }
        Long ownerId = Long.valueOf(request.get("ownerId").toString());
        List<String> skuCodeList = (List<String>) request.get("skuCodeList");
        if (CollectionUtils.isEmpty(skuCodeList)) {
            return JsonUtils.TOKEN_ERROR("skuCodeList不得为空");
        }
        if (skuCodeList.size() > 100) {
            return JsonUtils.TOKEN_ERROR("skuCodeList不得超过100");
        }

        Map<String, Object> resultMap = new HashMap<String, Object>();
        for (String skuCode : skuCodeList) {
            resultMap.put(skuCode, new HashMap<String, BigDecimal>(){{put("avail_qty", BigDecimal.ZERO);}});
        }

        List<StockSummary> stockSummaries = stockSummaryService.getAvailQty(ownerId, skuCodeList);

        for (final StockSummary stockSummary : stockSummaries) {
            resultMap.put(stockSummary.getSkuCode(), new HashMap<String, BigDecimal>(){{put("avail_qty", stockSummary.getAvailQty());}});
        }

        resultMap.put("warehouseCode", locationService.getWarehouseLocation().getLocationCode());
        return JsonUtils.SUCCESS(resultMap);
    }
}
