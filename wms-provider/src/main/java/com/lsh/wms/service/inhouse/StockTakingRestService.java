package com.lsh.wms.service.inhouse;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.json.JsonUtils;
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
import com.lsh.wms.model.task.Operation;
import com.lsh.wms.model.task.StockTakingTask;
import com.lsh.wms.model.task.Task;
import com.lsh.wms.task.service.DispatcherRpcService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
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

    @Reference
    private DispatcherRpcService dispather;

    @POST
    @Path("create")
    public String create(String stockTakingInfo) {
        StockTakingHead head = JSON.parseObject(stockTakingInfo,StockTakingHead.class);
        List<StockTakingDetail> detailList = prepareDetailList(head);
        stockTakingService.create(head, detailList);
        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("review")
    public String review(Long stockTakingId) {
        
        return JsonUtils.SUCCESS();
    }

    private List<StockTakingDetail> prepareDetailListByLocation(List<Long> locationList, List<StockQuant> quantList){
        Map<Long, StockQuant> mapLoc2Quant = new HashMap<Long, StockQuant>();
        for (StockQuant quant : quantList) {
            mapLoc2Quant.put(quant.getLocationId(), quant);
        }

        Long idx = 0L;
        List<StockTakingDetail> detailList = new ArrayList<StockTakingDetail>();
        for (Long locationId : locationList) {
            StockTakingDetail detail = new StockTakingDetail();
            detail.setLocationId(locationId);
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

    private List<StockTakingDetail> prepareDetailListBySku(List<StockQuant> quantList) {
        Long idx = 0L;
        List<StockTakingDetail> detailList = new ArrayList<StockTakingDetail>();
        for (StockQuant quant : quantList) {
            StockTakingDetail detail = new StockTakingDetail();
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
        if (locationId == null) {
            locationId = locationService.getWarehouseLocationId();
        }
        List<Long> locationList = locationService.getStoreLocationIds(locationId);

        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("locationList", locationList);
        mapQuery.put("skuId", head.getSkuId());
        mapQuery.put("lotId", head.getLotId());
        mapQuery.put("ownerId", head.getOwnerId());
        mapQuery.put("supplierId", head.getSupplierId());
        List<StockQuant> quantList = quantService.getQuants(mapQuery);

        if (head.getTakingType().equals(0L)) {
            return this.prepareDetailListBySku(quantList);
        }
        else {
            return this.prepareDetailListByLocation(locationList, quantList);
        }
    }

    public void doOne(String detailInfo) {
        StockTakingDetail detail = JSON.parseObject(detailInfo, StockTakingDetail.class);
        stockTakingService.updateDetail(detail);
    }

    private List<StockTakingDetail> getDifference(Long stockTakingId, Long round) {
        List<StockTakingDetail> differenceList = new ArrayList<StockTakingDetail>();
        // TODO
        // 找出本轮有差异的detail插入列表中
        return differenceList;
    }


    public void createTask(StockTakingHead head, List<StockTakingDetail> detailList) {
        StockTakingTask task = new StockTakingTask();
        dispather.create(TaskConstant.TYPE_STOCK_TAKING, task, detailList);
    }

    @POST
    @Path("cancel")
    public void cancel(@QueryParam("stockTakingId") Long stockTakingId) {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("takingId", stockTakingId);
        List<Task> taskList =  dispather.getTaskList(TaskConstant.TYPE_STOCK_TAKING, mapQuery);
        for (Task task : taskList) {
            dispather.cancel(TaskConstant.TYPE_STOCK_TAKING, task.getTaskId());
        }
    }

    public void confirm(Long stockTakingId) {
        // TODO
        // 获取stockingHead
        // 如果是临时盘点, 直接调用confirmDifference
        // 计划盘点,
        //      如果ruund == 1, 发起新一轮盘点
        //      如果round == 2, 获取第一轮,第二轮的差异明细列表, 如果非空, 发起第三轮盘点
        //      如果round == 3, 直接调用confirmDiffence
    }

    public void confirmDifference(Long stockTakingId) {
        StockTakingHead head = stockTakingService.getHeadById(stockTakingId);
        Long findRound = head.getMaxChkRnd();
        // TODO 获取
        List<StockTakingDetail> detailList = stockTakingService.getDetailListByRound(stockTakingId, findRound);
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
