package com.lsh.wms.service.sms;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.baidubce.util.DateUtils;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.BeanMapTransUtils;
import com.lsh.wms.api.model.so.ObdDetail;
import com.lsh.wms.api.service.sms.ISmsRestService;
import com.lsh.wms.api.service.stock.IStockQuantRpcService;
import com.lsh.wms.core.constant.StockConstant;
import com.lsh.wms.core.dao.redis.RedisSortedSetDao;
import com.lsh.wms.core.dao.stock.StockSummaryDao;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.stock.*;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.core.service.task.MessageService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.so.ObdHeader;
import com.lsh.wms.model.stock.StockMove;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.stock.StockSummary;
import com.lsh.wms.service.so.SoRpcService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(protocol = "rest")
@Path("sms")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class SmsRestService implements ISmsRestService {

    @Autowired
    private LocationService locationService;

    @Autowired
    private SmsService smsService;

    @Autowired
    private StockMoveService moveService;

    @Autowired
    private StockQuantService quantService;

    @Autowired
    private StockSummaryDao stockSummaryDao;

    @Autowired
    private StockSummaryService stockSummaryService;

    @Autowired
    private SoOrderService soOrderService;

    @Autowired
    private SoRpcService soRpcService;

    @Autowired
    private ItemService itemService;

    public void setSmsService(SmsService smsService) {
        this.smsService = smsService;
    }


    @GET
    @Path("sendMsg")
    public String sendMsg (@QueryParam("item_id") String itemId,
                          @QueryParam("location_code") String locationCode) throws BizCheckedException  {
        soRpcService.eliminateDiff(Long.valueOf(itemId));
        return JsonUtils.SUCCESS();
    }

    @GET
    @Path("inventory")
    public String inventory(@QueryParam("item_id") Long itemId,
                            @QueryParam("from_location_id") Long fromLocationId,
                            @QueryParam("to_location_id") Long toLocationId,
                            @QueryParam("qty")BigDecimal qty) throws BizCheckedException {
        StockMove move = new StockMove();
        move.setItemId(itemId);
        move.setFromLocationId(fromLocationId);
        move.setToLocationId(toLocationId);
        move.setQty(qty);
        moveService.move(move);
        return JsonUtils.SUCCESS();
    }

    @GET
    @Path("correctAvailQty")
    public String correctAvailQty() throws BizCheckedException {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        List<BaseinfoItem> itemList = itemService.searchItem(mapQuery);
        for (BaseinfoItem item : itemList) {
            Map<String, Object> quantMapQuery = new HashMap<String, Object>();
            quantMapQuery.put("itemId", item.getItemId());
            List<StockQuant> stockQuants = quantService.getQuants(quantMapQuery);
            for (StockQuant quant : stockQuants) {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("itemId", item.getItemId());
                params.put("skuCode", item.getSkuCode());
                params.put("ownerId", item.getOwnerId());
                params.put(StockConstant.REGION_TO_FIELDS.get(locationService.getLocation(quant.getLocationId()).getRegionType()), quant.getQty());
                StockSummary summary = BeanMapTransUtils.map2Bean(params, StockSummary.class);
                summary.setCreatedAt(com.lsh.base.common.utils.DateUtils.getCurrentSeconds());
                summary.setUpdatedAt(com.lsh.base.common.utils.DateUtils.getCurrentSeconds());
                stockSummaryDao.changeStock(summary);
            }
        }
        return JsonUtils.SUCCESS();
    }

    @GET
    @Path("so")
    public String alloc(@QueryParam("order_id") String orderId) throws BizCheckedException {
        return JsonUtils.SUCCESS();
    }

    @GET
    @Path("diff")
    public String diff(@QueryParam("order_id") String orderId) throws BizCheckedException {
        return JsonUtils.SUCCESS();
    }

}
