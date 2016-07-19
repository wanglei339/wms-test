package com.lsh.wms.service.inhouse;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.api.service.inhouse.IStockTakingRestService;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.stock.StockMoveService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.taking.StockTakingService;
import com.lsh.wms.model.stock.StockMove;
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

    @Autowired
    private StockMoveService moveService;

    @POST
    @Path("create")
    public String create(String stockTakingInfo) {
        StockTakingHead head = JSON.parseObject(stockTakingInfo,StockTakingHead.class);
        List<StockTakingDetail> detailList = JSON.parseArray(head.getDetails(), StockTakingDetail.class);
        // TODO 随机ID生成器
        head.setTakingId(RandomUtils.genId());
        if (detailList == null || detailList.isEmpty()) {
            detailList = this.prepareDetailList(head);
        }
        stockTakingService.create(head, detailList);
        return JsonUtils.SUCCESS();
    }

    private List<StockTakingDetail> prepareDetailListByLocation(StockTakingHead head, List<Long> locationList, List<StockQuant> quantList){
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

    private List<StockTakingDetail> prepareDetailListBySku(StockTakingHead head, List<StockQuant> quantList) {
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

    private List<StockTakingDetail> prepareDetailList(StockTakingHead head) {
        Long locationId = head.getLocationId();
        List<Long> locationList = locationService.getStoreLocationIds(locationId);

        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("locationList", locationList);
        mapQuery.put("skuId", head.getSkuId());
        mapQuery.put("lotId", head.getLotId());
        mapQuery.put("ownerId", head.getOwnerId());
        mapQuery.put("supplierId", head.getSupplierId());
        List<StockQuant> quantList = quantService.getQuants(mapQuery);

        if (head.getTakingType().equals(0L)) {
            return this.prepareDetailListBySku(head, quantList);
        }
        else {
            return this.prepareDetailListByLocation(head, locationList, quantList);
        }
    }

    public void confirmDifference(Long stockTakingId) {
        StockTakingHead head = stockTakingService.getHeadById(stockTakingId);
        List<StockTakingDetail> detailList = stockTakingService.getFinalDetailList(stockTakingId);
        List<StockMove> moveList = new ArrayList<StockMove>();

        for (StockTakingDetail detail : detailList) {
            if (detail.getSkuId().equals(detail.getRealSkuId())) {
                StockMove move = new StockMove();
                move.setTaskId(detail.getTakingId());
                move.setSkuId(detail.getSkuId());
                move.setStatus(TaskConstant.Done);
                if (detail.getTheoreticalQty().compareTo(detail.getRealQty()) < 0) {
                    move.setQty(detail.getRealQty().subtract(detail.getTheoreticalQty()));
                    move.setQtyDone(move.getQty());
                    move.setFromLocationId(detail.getLocationId());
                    move.setToLocationId(locationService.getInventoryLostLocationId());
                }
                else {
                    move.setQty(detail.getTheoreticalQty().subtract(detail.getRealQty()));
                    move.setQtyDone(move.getQty());
                    move.setFromLocationId(locationService.getInventoryLostLocationId());
                    move.setToLocationId(detail.getLocationId());
                }
                moveList.add(move);
            }
            else {
                StockMove moveWin= new StockMove();
                moveWin.setTaskId(detail.getTakingId());
                moveWin.setSkuId(detail.getSkuId());
                moveWin.setToLocationId(locationService.getInventoryLostLocationId());
                moveWin.setFromLocationId(detail.getLocationId());
                moveWin.setQty(detail.getRealQty());
                moveWin.setQtyDone(detail.getRealQty());
                moveList.add(moveWin);

                StockMove moveLoss= new StockMove();
                moveLoss.setTaskId(detail.getTakingId());
                moveLoss.setSkuId(detail.getSkuId());
                moveLoss.setFromLocationId(locationService.getInventoryLostLocationId());
                moveLoss.setToLocationId(detail.getLocationId());
                moveLoss.setQty(detail.getRealQty());
                moveLoss.setQtyDone(detail.getRealQty());
                moveList.add(moveLoss);
            }
        }
        moveService.create(moveList);
    }
}
