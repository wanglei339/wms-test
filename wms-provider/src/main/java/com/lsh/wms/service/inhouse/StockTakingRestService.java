package com.lsh.wms.service.inhouse;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.inhouse.IStockTakingRestService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.taking.StockTakingService;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.taking.StockTakingDetail;
import com.lsh.wms.model.taking.StockTakingHead;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mali on 16/7/14.
 */

@Service(protocol = "rest")
@Path("inhouse/stock_taking")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class StockTakingRestService implements IStockTakingRestService {

    @Autowired
    private StockTakingService stockTakingService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private StockQuantService quantService;

    @POST
    @Path("create")
    public String create(String stockTakingInfo) {
        StockTakingHead head = JSON.parseObject(stockTakingInfo,StockTakingHead.class);
        List<StockTakingDetail> detailList = JSON.parseArray(head.getDetails(), StockTakingDetail.class);
        if (detailList.isEmpty()) {
            detailList = this.prepare(head);
        }
        stockTakingService.create(head, detailList);
        return JsonUtils.SUCCESS();
    }

    private List<StockTakingDetail> prepareByLocation(StockTakingHead head, List<Long> locationList, List<StockQuant> quantList){
        Map<Long, StockQuant> mapLoc2Quant = new HashMap<Long, StockQuant>();
        for (StockQuant quant : quantList) {
            mapLoc2Quant.put(quant.getLocationId(), quant);
        }

        Long idx = 0L;
        List<StockTakingDetail> detailList = new ArrayList<StockTakingDetail>();
        for (Long locationId : locationList) {
            StockTakingDetail detail = new StockTakingDetail();
            detail.setLocationId(locationId);
            detail.setTakingId(head.getTakingId());
            detail.setDetailId(idx);

            StockQuant quant = mapLoc2Quant.get(locationId);
            if (quant != null ) {
                detail.setTheoreticalQty(quant.getQty());
                detail.setSkuId(quant.getSkuId());
                detail.setRealSkuId(detail.getSkuId());
            }
            idx++;
            detailList.add(detail);
        }
        return detailList;
    }

    private List<StockTakingDetail> prepareBySku(StockTakingHead head, List<StockQuant> quantList) {
        Long idx = 0L;
        List<StockTakingDetail> detailList = new ArrayList<StockTakingDetail>();
        for (StockQuant quant : quantList) {
            StockTakingDetail detail = new StockTakingDetail();
            detail.setTakingId(head.getTakingId());
            detail.setDetailId(idx);
            detail.setLocationId(quant.getLocationId());
            detail.setSkuId(quant.getSkuId());
            detail.setRealSkuId(detail.getSkuId());
            detail.setTheoreticalQty(quant.getQty());
            detailList.add(detail);
            idx++;
        }
        return detailList;
    }

    private List<StockTakingDetail> prepare(StockTakingHead head) {
        List<Long> locationList = locationService.getStoreLocationIds(head.getLocationId());

        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("locationList", locationList);
        mapQuery.put("skuId", head.getSkuId());
        mapQuery.put("lotId", head.getLotId());
        mapQuery.put("ownerId", head.getOwnerId());
        mapQuery.put("supplierId", head.getSupplierId());
        List<StockQuant> quantList = quantService.getQuants(mapQuery);

        if (head.getTakingType().equals(0L)) {
            return this.prepareBySku(head, quantList);
        }
        else {
            return this.prepareByLocation(head, locationList, quantList);
        }
    }
}
