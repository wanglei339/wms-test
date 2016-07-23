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
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.stock.StockMove;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.taking.StockTakingDetail;
import com.lsh.wms.model.taking.StockTakingHead;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.*;

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


//    @Autowired
//    private StockTakingTaskService takingTaskService;

//    @Autowired
//    private DispatcherRpcService dispather;

    @POST
    @Path("create")
    public String create(String stockTakingInfo) {
        StockTakingHead head = JSON.parseObject(stockTakingInfo, StockTakingHead.class);
        List<StockTakingDetail> detailList = prepareDetailList(head);
        stockTakingService.create(head, detailList);
       // this.createTask(head,detailList,1L,head.getDueTime());
        return JsonUtils.SUCCESS();
    }
    @GET
    @Path("genId")
    public String genId(){
        Long takingId=RandomUtils.genId();
        Map result=new HashMap();
        result.put("takingId",takingId);
        return JsonUtils.SUCCESS(result);
    }
    @GET
    @Path("getLocationList")
    public String getLocationList(int locationNum) {
        //Long skuId,Long AreaId,Long supplierId,int locationNum
        BaseinfoLocation location = locationService.getWarehouseLocation();
        List<Long> locationList = locationService.getStoreLocationIds(location.getLocationId());

        if(locationList.size()<=locationNum){
            locationNum = locationList.size();
        }
        long[] locations=new long[locationNum];
        for (int i = 0; i < locations.length; i++) {

            // 取出一个随机数
            int r = (int) (Math.random() * locationList.size());

            locations[i] = locationList.get(r);

            // 排除已经取过的值
            locationList.remove(r);
        }
        return JsonUtils.SUCCESS(JSON.toJSON(locations).toString());
    }
//    @GET
//    @Path("getList")
//    public String getList(Map<String, Object> mapQuery) {
//        List<StockTakingInfo> infos=new ArrayList<StockTakingInfo>();
//
//        List<StockTakingTask> takingTasks= takingTaskService.getTakingTask(mapQuery);
//        for(StockTakingTask task:takingTasks){
//            StockTakingHead head = stockTakingService.getHeadById(task.getTakingId());
//            StockTakingInfo info = new StockTakingInfo();
//            info.setTaskId(task.getTaskId());
//            info.setPlanType(head.getPlanType());
//            info.setViewType(head.getViewType());
//            info.setSupplierList(quantService.getSupplierByLocationAndSkuId(task.getLocationId(), task.getSkuId()));
//            info.setLocationCode(locationService.getLocation(task.getLocationId()).getLocationCode());
//            info.setAreaCode(locationService.getAreaFather(task.getLocationId()).getLocationCode());
//            info.setSkuId(task.getSkuId());
//            info.setOperator(task.getOperator());
//            info.setCreateAt(task.getCreatedAt());
//            info.setDueTime(task.getDueTime());
//            info.setStatus(task.getStatus());
//            infos.add(info);
//        }
//        return JsonUtils.SUCCESS(infos);
//    }
//    @GET
//    @Path("getCount")
//    public String getCount(Map<String, Object> mapQuery) {
//
//        Integer conut= takingTaskService.count(mapQuery);
//        return JsonUtils.SUCCESS(conut);
//    }
    @POST
    @Path("view")
    public String view(Long stockTakingId) {
        return JsonUtils.SUCCESS();
    }

    public void createNextDetail(Long stockTakingId,long roundTime) {
        Map queryMap = new HashMap();
        StockTakingHead head = stockTakingService.getHeadById(stockTakingId);
        List<StockTakingDetail> detailList = new ArrayList<StockTakingDetail>();
        queryMap.put("stockTakingId",stockTakingId);
        queryMap.put("round",roundTime);
        List<StockTakingDetail> details=stockTakingService.getDetailListByRound(stockTakingId,roundTime);
        for (StockTakingDetail stockTakingDetail:details){
            stockTakingDetail.setId(0L);
            BigDecimal qty=quantService.getQuantQtyByLocationIdAndSkuId(stockTakingDetail.getLocationId(), stockTakingDetail.getLotId());
            stockTakingDetail.setTheoreticalQty(qty);
            stockTakingDetail.setRound(roundTime+1);
            detailList.add(stockTakingDetail);
        }
        stockTakingService.insertDetailList(detailList);
        //this.createTask(head, detailList, 1L, head.getDueTime());
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
        List<Long> locationList= JSON.parseArray(head.getLocationList(), Long.class);
        List<Long> locations=new ArrayList<Long>();
        for(Long locationId:locationList) {
            if (locationId == null) {
                locationId = locationService.getWarehouseLocationId();
            }
            locations.addAll(locationService.getStoreLocationIds(locationId));
        }

            Map<String, Object> mapQuery = new HashMap<String, Object>();
            mapQuery.put("locationList", locations);
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

    private boolean chargeDifference(Long stockTakingId, Long round) {
        List<StockTakingDetail> details = stockTakingService.getDetailListByRound(stockTakingId, round);
        for (StockTakingDetail detail : details) {
            if (detail.getRealQty().compareTo(detail.getTheoreticalQty()) != 0) {
                return false;
            }
        }
       return true;
    }



//    public void createTask(StockTakingHead head, List<StockTakingDetail> detailList,Long round,Long dueTime) {
//        StockTakingTask task = new StockTakingTask();
//        task.setRound(round);
//        task.setTakingId(head.getTakingId());
//        task.setPlanner(head.getPlanner());
//        task.setDueTime(dueTime);
//        dispather.create(TaskConstant.TYPE_STOCK_TAKING, task, (List<Operation>) (List<?>)detailList);
//    }

//    @POST
//    @Path("cancel")
//    public void cancel(@QueryParam("stockTakingId") Long stockTakingId) {
//        Map<String, Object> mapQuery = new HashMap<String, Object>();
//        mapQuery.put("takingId", stockTakingId);
//        List<Task> taskList =  dispather.getTaskList(TaskConstant.TYPE_STOCK_TAKING, mapQuery);
//        for (Task task : taskList) {
//            dispather.cancel(TaskConstant.TYPE_STOCK_TAKING, task.getTaskId());
//        }
//    }

    public void confirm(Long stockTakingId) {
        // 获取stockingHead
        // 如果是临时盘点, 直接调用confirmDifference
        // 计划盘点,
        //      如果ruund == 1, 发起新一轮盘点
        //      如果round == 2, 获取第一轮,第二轮的差异明细列表, 如果非空, 发起第三轮盘点
        //      如果round == 3, 直接调用confirmDiffence

        StockTakingHead head=stockTakingService.getHeadById(stockTakingId);
        if (head.getPlanType()==1) {
            this.confirmDifference(stockTakingId,1L);
        }else {
            Long times = stockTakingService.chargeTime(stockTakingId);
            if (times == 1) {
                this.createNextDetail(stockTakingId,times);
            } else {
                if (times == 2) {
                    boolean isSame=this.chargeDifference(stockTakingId,times);
                    if (isSame) {
                        this.confirmDifference(stockTakingId, times);
                    } else {
                        this.createNextDetail(stockTakingId, times);
                    }
                } else {
                    this.confirmDifference(stockTakingId, times);
                }
            }
        }
    }


    public void confirmDifference(Long stockTakingId ,long roundTime) {
        List<StockTakingDetail> detailList = stockTakingService.getDetailListByRound(stockTakingId,roundTime);
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
