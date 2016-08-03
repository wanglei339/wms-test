package com.lsh.wms.rf.service.inhouse;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.item.IItemRpcService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.stock.IStockQuantRfRestService;
import com.lsh.wms.api.service.stock.IStockQuantRpcService;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.stock.StockQuantCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.*;

/**
 * Created by mali on 16/8/2.
 */
@Service(protocol = "rest")
@Path("inhouse/stock")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class StockQuantRfRestService implements IStockQuantRfRestService {

    private static Logger logger = LoggerFactory.getLogger(StockQuantRfRestService.class);

    @Reference
    private IStockQuantRpcService stockQuantRpcService;

    @Reference
    private IItemRpcService itemRpcService;

    @POST
    @Path("getItemList")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String getItemByLocation() throws BizCheckedException {
        Map<String, Object> params = RequestUtils.getRequest();
        Long locationId = Long.valueOf(params.get("locationId").toString());
        StockQuantCondition condition = new StockQuantCondition();
        condition.setLocationId(locationId);
        List<StockQuant> quantList = stockQuantRpcService.getQuantList(condition);
        Set<Long> itemSet = new HashSet<Long>();
        List<Object> resultList = new ArrayList<Object>();
        for (StockQuant quant : quantList) {
            if (itemSet.contains(quant.getItemId())) {
                continue;
            }
            itemSet.add(quant.getItemId());
            Map<String, Object> m = new HashMap<String, Object>();
            m.put("itemId", quant.getItemId());
            m.put("name", itemRpcService.getItem(quant.getItemId()).getSkuName());
            m.put("packName", quant.getPackName());
            resultList.add(m);
        }
        Map<String, List<Object>> result = new HashMap<String, List<Object>>();
        result.put("list",resultList);
        return JsonUtils.SUCCESS(result);
    }
}
