package com.lsh.wms.core.service.taking;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.BeanMapTransUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.*;
import com.lsh.wms.core.dao.stock.OverLossReportDao;
import com.lsh.wms.core.dao.taking.StockTakingDetailDao;
import com.lsh.wms.core.dao.taking.StockTakingHeadDao;
import com.lsh.wms.core.service.container.ContainerService;
import com.lsh.wms.core.service.csi.CsiSkuService;
import com.lsh.wms.core.service.datareport.DifferenceZoneReportService;
import com.lsh.wms.core.service.datareport.SkuMapService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.persistence.PersistenceProxy;
import com.lsh.wms.core.service.stock.StockLotService;
import com.lsh.wms.core.service.stock.StockMoveService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.csi.CsiSku;
import com.lsh.wms.model.datareport.DifferenceZoneReport;
import com.lsh.wms.model.datareport.SkuMap;
import com.lsh.wms.model.stock.OverLossReport;
import com.lsh.wms.model.stock.StockLot;
import com.lsh.wms.model.stock.StockMove;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.taking.DetailRequest;
import com.lsh.wms.model.taking.StockTakingDetail;
import com.lsh.wms.model.taking.StockTakingHead;
import com.lsh.wms.model.task.TaskInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by mali on 16/7/14.
 */
@Component
@Transactional(readOnly = true)
public class StockTakingService {
    private static final Logger logger = LoggerFactory.getLogger(StockTakingDetail.class);

    @Reference
    private ITaskRpcService iTaskRpcService;
    @Autowired
    private StockTakingHeadDao headDao;
    @Autowired
    private StockTakingDetailDao detailDao;
    @Autowired
    private PersistenceProxy persistenceProxy;
    @Autowired
    private ItemService itemService;
    @Autowired
    private StockMoveService moveService;
    @Autowired
    private ContainerService containerService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private OverLossReportDao overLossReportDao;
    @Autowired
    private BaseTaskService baseTaskService;
    @Autowired
    private SkuMapService skuMapService;
    @Autowired
    private StockLotService lotService;
    @Autowired
    private StockQuantService quantService;
    @Autowired
    private CsiSkuService skuService;
    @Autowired
    private DifferenceZoneReportService differenceZoneReportService;



    @Transactional(readOnly = false)
    public void insertHead(StockTakingHead head) {
        head.setCreatedAt(DateUtils.getCurrentSeconds());
        head.setUpdatedAt(DateUtils.getCurrentSeconds());
        headDao.insert(head);
    }

    @Transactional(readOnly = false)
    public void updateHead(StockTakingHead head) {
        head.setUpdatedAt(DateUtils.getCurrentSeconds());
        headDao.update(head);
    }
    @Transactional(readOnly = false)
    public void insertDetailList(List<StockTakingDetail> detailList) {
        for (StockTakingDetail detail : detailList) {
            if(detail.getRound().compareTo(1L)>0){
                StockTakingDetail takingDetail = this.getDetailByRoundAndDetailId(detail.getDetailId(), detail.getRound()-1);
                takingDetail.setStatus(StockTakingConstant.Done);
                takingDetail.setUpdatedAt(DateUtils.getCurrentSeconds());
                detailDao.update(takingDetail);
            }

            detail.setCreatedAt(DateUtils.getCurrentSeconds());
            detail.setUpdatedAt(DateUtils.getCurrentSeconds());
            detailDao.insert(detail);
        }
    }

    @Transactional(readOnly = false)
    public void insertDetail(StockTakingDetail detail) {

        if(detail.getRound().compareTo(1L)>0){
            StockTakingDetail takingDetail = this.getDetailByRoundAndDetailId(detail.getDetailId(), detail.getRound()-1);
            takingDetail.setStatus(StockTakingConstant.Done);
            takingDetail.setUpdatedAt(DateUtils.getCurrentSeconds());
            detailDao.update(takingDetail);
        }
        detail.setCreatedAt(DateUtils.getCurrentSeconds());
        detail.setUpdatedAt(DateUtils.getCurrentSeconds());
        detailDao.insert(detail);
    }

    @Transactional(readOnly = false)
    public void updateDetail(StockTakingDetail detail) {
        detail.setUpdatedAt(DateUtils.getCurrentSeconds());
        detailDao.update(detail);
    }
    @Transactional(readOnly = false)
    public void doneDetail(StockTakingDetail detail) {
        detail.setStatus(StockTakingConstant.PendingAudit);
        if(detail.getOwnerId().compareTo(1L)==0 || detail.getOwnerId().compareTo(2L)==0){
            if(!detail.getSkuCode().equals("")) {
                SkuMap skuMap = skuMapService.getSkuMapBySkuCodeAndOwner(detail.getSkuCode(),detail.getOwnerId());
                if (skuMap == null) {
                    throw new BizCheckedException("2880022", detail.getSkuCode(), "");
                }
                detail.setPrice(skuMap.getMovingAveragePrice());
                detail.setDifferencePrice((detail.getUmoQty().multiply(detail.getPackUnit()).add(detail.getRealQty()).subtract(detail.getTheoreticalQty())).multiply(detail.getPrice()));
            }
        }
        this.updateDetail(detail);

        //获取该任务的所有的detail,判断是否都done,是的话,done整个task
        List<StockTakingDetail> stockTakingDetailList = this.getDetailByTaskId(detail.getTaskId());
        //所有detail是否完成
        boolean isAllTaskDone = true;
        for(StockTakingDetail stockTakingDetail: stockTakingDetailList){
            if(stockTakingDetail.getStatus().compareTo(StockTakingConstant.Assigned) == 0 ){
                isAllTaskDone = false;
            }
        }

        if(isAllTaskDone){
            TaskInfo info = baseTaskService.getTaskInfoById(detail.getTaskId());
            info.setStatus(TaskConstant.Done);
            info.setFinishTime(DateUtils.getCurrentSeconds());
            info.setUpdatedAt(DateUtils.getCurrentSeconds());
            baseTaskService.update(info);

            //判断计划下任务是否完成
            Map<String,Object> queryMap = new HashMap<String, Object>();
            queryMap.put("planId",detail.getTakingId());
            queryMap.put("valid",1);
            List<TaskInfo> infos = baseTaskService.getTaskInfoList(queryMap);
            if(infos==null || infos.size()==0 || (infos.size()==0 && infos.get(0).getTaskId().equals(detail.getTaskId()))){
                //计划已完成
                StockTakingHead head = this.getHeadById(detail.getTakingId());
                head.setStatus(StockTakingConstant.Done);
                this.updateHead(head);
            }
        }

    }

    public List<StockTakingDetail> getDetailListByRound(Long stockTakingId, Long round) {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("takingId", stockTakingId);
        mapQuery.put("round", round);
        mapQuery.put("isValid", 1);
        List<StockTakingDetail> detailList = detailDao.getStockTakingDetailList(mapQuery);
        return detailList;
    }
    public List<StockTakingDetail> getValidDetailList() {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("valid", 1);
        List<StockTakingDetail> detailList = detailDao.getStockTakingDetailList(mapQuery);
        return detailList;
    }

    @Transactional(readOnly = false)
    public void done(Long stockTakingId, List<StockTakingDetail> stockTakingDetails) {
        for (StockTakingDetail stockTakingDetail : stockTakingDetails) {
            stockTakingDetail.setIsFinal(1);
            this.updateDetail(stockTakingDetail);
        }
        StockTakingHead head = this.getHeadById(stockTakingId);
        head.setStatus(3L);
        List<StockMove> moveList = new ArrayList<StockMove>();
//        //盘亏 盘盈的分成两个list items为盘亏 items1盘盈
//        List<StockItem> itemsLoss = new ArrayList<StockItem>();
//        List<StockItem> itemsWin = new ArrayList<StockItem>();
//        StockRequest request = new StockRequest();
        List<OverLossReport> overLossReports = new ArrayList<OverLossReport>();
        for (StockTakingDetail detail : stockTakingDetails) {
            if (detail.getItemId() == 0L) {
                continue;
            }
            OverLossReport overLossReport = new OverLossReport();
            //StockItem stockItem = new StockItem();
            BigDecimal realQty = detail.getUmoQty().multiply(detail.getPackUnit()).add(detail.getRealQty());
            if (detail.getSkuId().equals(detail.getRealSkuId())) {
                if(detail.getTheoreticalQty().compareTo(realQty)==0){
                    continue;
                }

                Long containerId = containerService.createContainerByType(ContainerConstant.CAGE).getContainerId();
                StockMove move = new StockMove();
                move.setTaskId(detail.getTaskId());
                move.setSkuId(detail.getSkuId());
                move.setItemId(detail.getItemId());
                move.setStatus(TaskConstant.Done);

                BaseinfoItem item = itemService.getItem(move.getItemId());
                overLossReport.setItemId(detail.getItemId());
                overLossReport.setOwnerId(detail.getOwnerId());
                overLossReport.setLotId(detail.getLotId());
                overLossReport.setPackName(detail.getPackName());
                overLossReport.setSkuCode(item.getSkuCode());
                overLossReport.setRefTaskId(detail.getTaskId());

                if (detail.getTheoreticalQty().compareTo(realQty) > 0) {
                    BigDecimal qty = detail.getTheoreticalQty().subtract(realQty);
                    overLossReport.setMoveType(OverLossConstant.LOSS_REPORT);
                    overLossReport.setQty(qty);


                    move.setQty(qty);
                    move.setFromLocationId(detail.getLocationId());
                    move.setToLocationId(locationService.getInventoryLostLocation().getLocationId());
                    move.setToContainerId(containerId);
                    //组装回传物美的数据

                } else {
                    BigDecimal qty = realQty.subtract(detail.getTheoreticalQty());
                    overLossReport.setMoveType(OverLossConstant.OVER_REPORT);
                    overLossReport.setQty(qty);

                    StockLot lot = lotService.getStockLotByLotId(detail.getLotId());
                    move.setLot(lot);
                    move.setQty(qty);
                    move.setFromLocationId(locationService.getInventoryLostLocation().getLocationId());
                    move.setToLocationId(detail.getLocationId());
                    move.setToContainerId(detail.getContainerId());
                }
                overLossReports.add(overLossReport);
                moveList.add(move);
            } else {
                StockMove moveWin = new StockMove();
                moveWin.setTaskId(detail.getTakingId());
                moveWin.setSkuId(detail.getSkuId());
                moveWin.setToLocationId(locationService.getInventoryLostLocation().getLocationId());
                moveWin.setFromLocationId(detail.getLocationId());
                moveWin.setQty(realQty);
                moveList.add(moveWin);

                StockMove moveLoss = new StockMove();
                moveLoss.setTaskId(detail.getTakingId());
                moveLoss.setSkuId(detail.getSkuId());
                moveLoss.setFromLocationId(locationService.getInventoryLostLocation().getLocationId());
                moveLoss.setToLocationId(detail.getLocationId());
                moveLoss.setQty(realQty);
                moveList.add(moveLoss);
            }
        }
        try {
            this.insertLossOrOver(overLossReports);
            moveService.move(moveList);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BizCheckedException("2550099");
        }
        this.updateHead(head);
        return;
    }
    @Transactional(readOnly = false)
    public void confirm(List<StockTakingDetail> stockTakingDetails) {
        List<StockMove> moveList = new ArrayList<StockMove>();
//        //盘亏 盘盈的分成两个list items为盘亏 items1盘盈
//        List<StockItem> itemsLoss = new ArrayList<StockItem>();
//        List<StockItem> itemsWin = new ArrayList<StockItem>();
//        StockRequest request = new StockRequest();
        List<OverLossReport> overLossReports = new ArrayList<OverLossReport>();
        for (StockTakingDetail detail : stockTakingDetails) {
            BigDecimal realQty = detail.getUmoQty().multiply(detail.getPackUnit()).add(detail.getRealQty());
            if (realQty.compareTo(BigDecimal.ZERO)!=0 && detail.getItemId().equals(0L)) {
                throw new BizCheckedException("2550098","任务号:"+detail.getTaskId()+",库位:"+detail.getLocationCode()+"");
            }
            detail.setStatus(StockTakingConstant.Done);
            detail.setIsFinal(1);
            Long containerId = containerService.createContainerByType(ContainerConstant.CAGE).getContainerId();
            if(detail.getContainerId().equals(0L)){
                detail.setContainerId(containerId);
            }
            this.updateDetail(detail);
            OverLossReport overLossReport = new OverLossReport();
            //StockItem stockItem = new StockItem();
            if (detail.getSkuId().equals(detail.getRealSkuId()) || detail.getRealSkuId().compareTo(0l)==0) {

                if(detail.getTheoreticalQty().compareTo(detail.getRealQty())==0){
                    continue;
                }
                StockMove move = new StockMove();
                move.setTaskId(detail.getTaskId());
                move.setSkuId(detail.getSkuId());
                move.setItemId(detail.getItemId());
                move.setStatus(TaskConstant.Done);

                BaseinfoItem item = itemService.getItem(move.getItemId());
                overLossReport.setItemId(detail.getItemId());
                overLossReport.setOwnerId(detail.getOwnerId());
                overLossReport.setLotId(detail.getLotId());
                overLossReport.setPackName(detail.getPackName());
                overLossReport.setSkuCode(item.getSkuCode());
                overLossReport.setRefTaskId(detail.getTaskId());

                if (detail.getTheoreticalQty().compareTo(realQty) > 0) {
                    BigDecimal qty = detail.getTheoreticalQty().subtract(realQty);
                    overLossReport.setMoveType(OverLossConstant.LOSS_REPORT);
                    overLossReport.setQty(qty);


                    move.setQty(qty);
                    move.setFromLocationId(detail.getLocationId());
                    move.setToLocationId(locationService.getInventoryLostLocation().getLocationId());
                    move.setToContainerId(containerId);
                    //组装回传物美的数据

                } else {
                    BigDecimal qty = realQty.subtract(detail.getTheoreticalQty());
                    overLossReport.setMoveType(OverLossConstant.OVER_REPORT);
                    overLossReport.setQty(qty);

                    StockLot lot = lotService.getStockLotByLotId(detail.getLotId());
                    move.setLot(lot);
                    move.setQty(qty);
                    move.setFromLocationId(locationService.getInventoryLostLocation().getLocationId());
                    move.setToLocationId(detail.getLocationId());
                    move.setToContainerId(detail.getContainerId());
                }
                overLossReports.add(overLossReport);
                moveList.add(move);
            } else {
//                StockMove moveWin = new StockMove();
//                moveWin.setTaskId(detail.getTaskId());
//                moveWin.setSkuId(detail.getSkuId());
//                moveWin.setToLocationId(locationService.getInventoryLostLocation().getLocationId());
//                moveWin.setFromLocationId(detail.getLocationId());
//                moveWin.setQty(detail.getRealQty());
//                moveList.add(moveWin);
//
//                StockMove moveLoss = new StockMove();
//                moveLoss.setTaskId(detail.getTaskId());
//                moveLoss.setSkuId(detail.getSkuId());
//                moveLoss.setFromLocationId(locationService.getInventoryLostLocation().getLocationId());
//                moveLoss.setToLocationId(detail.getLocationId());
//                moveLoss.setQty(detail.getRealQty());
//                moveList.add(moveLoss);
            }
        }
        try {
            this.insertLossOrOver(overLossReports);
            moveService.move(moveList);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BizCheckedException("2550099");
        }
    }

    public StockTakingHead getHeadById(Long takingId) {
        return headDao.getStockTakingHeadById(takingId);
    }

    public Long chargeTime(Long stockTakingId) {
        Map queryMap = new HashMap();
        queryMap.put("takingId", stockTakingId);
        queryMap.put("round", 3L);
        int i = detailDao.countStockTakingDetail(queryMap);
        if (i != 0) {
            return 3L;
        } else {
            queryMap.put("round", 2L);
            i = detailDao.countStockTakingDetail(queryMap);
            if (i != 0) {
                return 2L;
            }
            return 1L;
        }
    }
    public StockTakingDetail getDetailByRoundAndDetailId(Long detailId ,Long round) {
        Map <String,Object> queryMap = new HashMap<String, Object>();
        queryMap.put("detailId", detailId);
        queryMap.put("round", round);
        queryMap.put("valid", 1);

        List<StockTakingDetail> details = detailDao.getStockTakingDetailList(queryMap);

        if(details==null || details.size()==0){
            return null;
        }
        return details.get(0);
    }

    public List<StockTakingHead> queryTakingHead(Map queryMap) {
        return headDao.getStockTakingHeadList(queryMap);
    }

    public List<StockTakingDetail> getDetailByTaskId(Long taskId) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("taskId", taskId);
        return detailDao.getStockTakingDetailList(queryMap);

    }
    public List<StockTakingDetail> getDraftDetailByTaskId(Long taskId) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("taskId", taskId);
        queryMap.put("status",StockTakingConstant.Draft);
        return detailDao.getStockTakingDetailList(queryMap);

    }
    public StockTakingDetail getDetailByTaskIdAndLocation(Long taskId,Long locationId) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("taskId", taskId);
        queryMap.put("locationId", locationId);
        List<StockTakingDetail> list = detailDao.getStockTakingDetailList(queryMap);
        if(list == null || list.size() <= 0){
            return null;
        }else{
            return list.get(0);
        }

    }

    public List<StockTakingDetail> getDetailByTakingId(Long takingId) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("takingId", takingId);
        return detailDao.getStockTakingDetailList(queryMap);
    }
    public List<StockTakingDetail> getDetailByTakingIdAndStatus(Long takingId,Long status) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("takingId", takingId);
        queryMap.put("status", status);
        return detailDao.getStockTakingDetailList(queryMap);
    }

    public List<StockTakingDetail> getDetails(Map<String, Object> queryMap) {
        List<StockTakingDetail> details =  detailDao.getStockTakingDetailList(queryMap);
        return details==null ? new ArrayList<StockTakingDetail>() : details;
    }
    public Integer countDetails(Map<String, Object> queryMap) {
        return detailDao.countStockTakingDetail(queryMap);
    }


    public List<StockTakingDetail> getItemDetails(Map<String, Object> queryMap) {
        return detailDao.getStockTakingItemList(queryMap);
    }
    public Integer countItemDetails(Map<String, Object> queryMap) {
        return detailDao.conutStockTakingItemList(queryMap);
    }

    public Integer countHead(Map queryMap) {
        return headDao.countStockTakingHead(queryMap);

    }
    public StockTakingDetail getDetaiByTaskIdAndDetailId(Long taskId,Long detailId) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("taskId", taskId);
        queryMap.put("detailId", detailId);
        List<StockTakingDetail> details = detailDao.getStockTakingDetailList(queryMap);
        if(details==null || details.size()==0){
            return null;
        }
        return details.get(0);
    }

    public List queryTakingDetail(Map queryMap) {
        return detailDao.getStockTakingDetailList(queryMap);

    }
    public Double getDiffPrice(Map queryMap) {
        queryMap.put("status",StockTakingConstant.PendingAudit);
        Double diffPrice =  detailDao.getDiffPrice(queryMap);
        if(diffPrice==null){
            return 0.0;
        }
        return diffPrice;
    }
    public Double getLossPrice(Map queryMap) {
        queryMap.put("status",StockTakingConstant.Done);
        queryMap.put("isFinal",1L);
        Double lossPrice =  detailDao.getDiffPrice(queryMap);
        if(lossPrice==null){
            return 0.0;
        }
        return lossPrice;
    }
    public Double getAllPrice(Map queryMap) {
        queryMap.put("status", StockTakingConstant.PendingAudit);
        Double allprice = detailDao.getAllPrice(queryMap);
        if(allprice==null){
            return 0.0;
        }
        return allprice;
    }

    @Transactional(readOnly = false)
    public void confirmDifference(Long stockTakingId, long roundTime) {
        List<StockTakingDetail> detailList = this.getDetailListByRound(stockTakingId, roundTime);
        this.done(stockTakingId, detailList);
    }

    @Transactional(readOnly = false)
    public void insertLossOrOver(List<OverLossReport> overLossReports) {
        for (OverLossReport overLossReport : overLossReports) {
            Long reportId = RandomUtils.genId();
            overLossReport.setLossReportId(reportId);
            overLossReport.setUpdatedAt(DateUtils.getCurrentSeconds());
            overLossReport.setCreatedAt(DateUtils.getCurrentSeconds());
            overLossReportDao.insert(overLossReport);
            //物美的商品 增加日志
            if(overLossReport.getOwnerId().compareTo(1L)==0 || overLossReport.getOwnerId().compareTo(2L)==0){
                persistenceProxy.doOne(SysLogConstant.LOG_TYPE_LOSS_WIN, reportId,0);
            }

        }
    }

    @Transactional(readOnly = false)
    public void insertLossOrOver(OverLossReport overLossReport) {
        Long reportId = RandomUtils.genId();
        overLossReport.setLossReportId(reportId);
        overLossReport.setUpdatedAt(DateUtils.getCurrentSeconds());
        overLossReport.setCreatedAt(DateUtils.getCurrentSeconds());
        overLossReportDao.insert(overLossReport);
        //物美的商品 增加日志
        if(overLossReport.getOwnerId().compareTo(1L)==0 || overLossReport.getOwnerId().compareTo(2L)==0){
            persistenceProxy.doOne(SysLogConstant.LOG_TYPE_LOSS_WIN, reportId,0);
        }
    }

    public OverLossReport getOverLossReportById(Long reportId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("lossReportId", reportId);
        List<OverLossReport> list = overLossReportDao.getOverLossReportList(map);
        if (list == null || list.size() == 0) {
            return null;
        }
        return list.get(0);
    }
    public StockTakingDetail getDetailByTaskIdAndLocationCode(Long taskId,String locationCode) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("taskId", taskId);
        map.put("locationCode", locationCode);
        List<StockTakingDetail> list = detailDao.getStockTakingDetailList(map);
        if (list == null || list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    @Transactional(readOnly = false)
    public void doQcPickDifference(StockMove move) throws BizCheckedException {

        try {
            TaskInfo qcInfo = baseTaskService.getTaskInfoById(move.getTaskId());
            //同步库存判断是直流还是在库的
            Long businessMode = qcInfo.getBusinessMode();
            if (TaskConstant.MODE_DIRECT.equals(businessMode)){
                doDirectQcDifference(move);
            } else {
                doInboundQcDifference(move);
            }
        } catch (Exception e) {
            logger.error("MOVE STOCK FAIL , containerId is " + move.getToContainerId() + "taskId is " + move.getTaskId() + e.getMessage());
            throw new BizCheckedException("2550051");
        }
    }

    @Transactional(readOnly = false)
    private void doInboundQcDifference(StockMove move) throws BizCheckedException {
        DifferenceZoneReport differenceZoneReport = new DifferenceZoneReport();
        //插入差异报表
        BaseinfoItem item = itemService.getItem(move.getItemId());
        differenceZoneReport.setItemId(item.getItemId());
        differenceZoneReport.setSkuCode(item.getSkuCode());
        differenceZoneReport.setFromLocationId(move.getFromLocationId());
        differenceZoneReport.setSourceType(ReportConstant.SOURCE_TYPE_QC);
        differenceZoneReport.setUnitName("EA");
        differenceZoneReport.setQty(move.getQty().abs());
        differenceZoneReport.setDirect(ReportConstant.DIRECT_IN);
        TaskInfo qcInfo = baseTaskService.getTaskInfoById(move.getTaskId());
        differenceZoneReport.setOperator(qcInfo.getOperator());

        //插入差异表
        differenceZoneReportService.insertReport(differenceZoneReport);
        //移到差异区
        moveService.move(move);
    }

    @Transactional(readOnly = false)
    private void doDirectQcDifference(StockMove move) throws BizCheckedException {
        // 移动直流差异库存到供商区
        move.setToLocationId(locationService.getSupplyArea().getLocationId());

        StockMove diff = new StockMove();
        diff.setFromContainerId(locationService.getSoAreaDirect().getLocationId());
        diff.setToLocationId(locationService.getNullArea().getLocationId());
        diff.setQty(move.getQty());
        diff.setItemId(move.getItemId());
        diff.setTaskId(move.getTaskId());
        List<StockMove> diffList = Arrays.asList(diff);
        moveService.move(diffList);
    }

    @Transactional(readOnly = false)
    public void writeOffQuant(StockMove move,StockQuant quant) {
        OverLossReport overLossReport = new OverLossReport();
        //插入报损
        BaseinfoItem item = itemService.getItem(move.getItemId());
        overLossReport.setItemId(item.getItemId());
        overLossReport.setOwnerId(item.getOwnerId());
        overLossReport.setLotId(move.getLot().getLotId());
        overLossReport.setPackName(item.getPackName());
        overLossReport.setSkuCode(item.getSkuCode());
        overLossReport.setRefTaskId(move.getTaskId());
        overLossReport.setMoveType(OverLossConstant.LOSS_REPORT);
        overLossReport.setQty(move.getQty());
        overLossReport.setStorageLocation(move.getFromLocationId().toString());

        try {
            this.insertLossOrOver(overLossReport);
            Long locationId = locationService.getInventoryLostLocation().getLocationId();
            //移到盘亏盘盈区
            moveService.move(move);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BizCheckedException("2550051");
        }
    }
    @Transactional(readOnly = false)
    public void fillEmptyDetail(StockTakingDetail detail,StockLot lot) {
        lotService.insertLot(lot);
        this.updateDetail(detail);
    }
    @Transactional(readOnly = false)
    public void fillDetails(List<StockTakingDetail> details) {
        for (StockTakingDetail detail : details) {
            List<StockQuant> quants = quantService.getQuantsByLocationId(detail.getLocationId());
            if (quants != null && quants.size() != 0) {
                StockQuant quant = quants.get(0);
                BaseinfoItem item = itemService.getItem(quant.getItemId());
                CsiSku sku = skuService.getSku(quant.getSkuId());
                BaseinfoLocation location = locationService.getLocation(quant.getLocationId());
                detail.setTheoreticalQty(quantService.getQuantQtyByContainerId(quant.getContainerId()));
                detail.setSkuId(quant.getSkuId());
                detail.setContainerId(quant.getContainerId());
                detail.setItemId(quant.getItemId());
                detail.setRealItemId(quant.getItemId());
                detail.setRealSkuId(detail.getSkuId());
                detail.setPackName(quant.getPackName());
                detail.setPackUnit(quant.getPackUnit());
                detail.setOwnerId(quant.getOwnerId());
                detail.setRealSkuId(sku.getSkuId());
                detail.setPackCode(item.getPackCode());
                detail.setLotId(quant.getLotId());
                detail.setSkuCode(item.getSkuCode());
                detail.setSkuName(item.getSkuName());
                detail.setBarcode(sku.getCode());
                detail.setLocationCode(location.getLocationCode());
            }
            this.updateDetail(detail);
        }
    }
    @Transactional(readOnly = false)
    public void doneDetails(List detailRequestList) {
        //存储任务List,避免任务重复判断
        Map<Long,Long> taskMap = new HashMap<Long, Long>();
        //存储错误任务信息
        Map<Long,String> taskErrMap = new HashMap<Long, String>();
        //存储待完成任务location
        Map<Long,List<String>> locationCodeMap = new HashMap<Long, List<String>>();
        StringBuilder errorStr = new StringBuilder();
        boolean isTrue = true;
        //详情行项目号
        int index = 2;

        for(Object detailMap :detailRequestList){
            DetailRequest detailRequest = null;
            try {
                detailRequest = BeanMapTransUtils.map2Bean((Map) detailMap, DetailRequest.class);
            }catch (Exception e){
                throw new BizCheckedException("2550097","第"+index+"倒入格式有误");
            }

            Long taskId = detailRequest.getTaskId();
            //判断任务
            if(!taskMap.containsKey(taskId)) {
                TaskInfo info = baseTaskService.getTaskInfoById(taskId);
                if (info == null) {
                    taskErrMap.put(taskId,"任务号"+taskId+"不存在");
                }else if(info.getStatus().compareTo(TaskConstant.Assigned)!=0 && info.getStatus().compareTo(TaskConstant.Draft)!=0){
                    taskErrMap.put(taskId,"任务号"+taskId+"已完成或已取消");
                }
            }
            taskMap.put(taskId,taskId);

            if(locationCodeMap.containsKey(taskId)){
                List<String> locationCodeList = locationCodeMap.get(taskId);
                locationCodeList.add(detailRequest.getLocationCode());
                locationCodeMap.put(taskId,locationCodeList);
            }else {
                List<String> locationCodeList = new ArrayList<String>();
                locationCodeList.add(detailRequest.getLocationCode());
                locationCodeMap.put(taskId,locationCodeList);
            }

            //判断任务是否违法
            if(taskErrMap.containsKey(taskId)){
                isTrue = false;
                errorStr.append("第"+index+"行,").append(taskErrMap.get(taskId)).append(System.getProperty("line.separator"));
            }
            //判断详情状态是否违法
            StockTakingDetail detail = this.getDetailByTaskIdAndLocationCode(detailRequest.getTaskId(), detailRequest.getLocationCode());
            if(detail==null){
                isTrue = false;
                errorStr.append("第"+index+"行,").append("任务详情不存在").append(System.getProperty("line.separator"));
            }else if(detail.getStatus().compareTo(StockTakingConstant.Cancel)==0 || detail.getStatus().compareTo(StockTakingConstant.Done)==0){
                isTrue = false;
                errorStr.append("第"+index+"行,").append("任务详情已取消或已完成").append(System.getProperty("line.separator"));
            }else if(detail.getTheoreticalQty().compareTo(BigDecimal.ZERO)==0 && (detailRequest.getUmoQty().compareTo(BigDecimal.ZERO)!=0 || detailRequest.getScatterQty().compareTo(BigDecimal.ZERO)!=0)){
                //目前不支持从0盘有库存
                isTrue = false;
                errorStr.append("第"+index+"行,").append("系统记录库存数量为零,但是导入数量不为零").append(System.getProperty("line.separator"));
            }
            if(isTrue){
                detail.setRealQty(detailRequest.getScatterQty());
                detail.setUmoQty(detailRequest.getUmoQty());
                detail.setStatus(StockTakingConstant.PendingAudit);
                if(!detail.getSkuCode().equals("")) {
                    SkuMap skuMap = skuMapService.getSkuMapBySkuCodeAndOwner(detail.getSkuCode(),detail.getOwnerId());
                    if (skuMap == null) {
                        isTrue = false;
                        errorStr.append("第"+index+"行,").append("货码"+detail.getSkuCode()+"无移动平均价").append(System.getProperty("line.separator"));
                    }else {
                        detail.setPrice(skuMap.getMovingAveragePrice());
                        detail.setDifferencePrice((detail.getRealQty().add(detail.getUmoQty().multiply(detail.getPackUnit())).subtract(detail.getTheoreticalQty())).multiply(detail.getPrice()));
                    }
                }
                this.updateDetail(detail);
            }
            index++;
        }
        if(isTrue) {
            //判断任务是否完成
            Map<String,Object> queryMap = new HashMap<String, Object>();
            queryMap.put("doingTask",1);
            for (Map.Entry<Long, List<String>> entry : locationCodeMap.entrySet()) {
                queryMap.put("taskId", entry.getKey());
                List<StockTakingDetail> details = this.getDetails(queryMap);
                List<String> locationCodeList = new ArrayList<String>();
                for(StockTakingDetail detail:details){
                    locationCodeList.add(detail.getLocationCode());
                }
                locationCodeList.removeAll(locationCodeMap.get(entry.getKey()));

                //任务已完成
                if(locationCodeList.isEmpty()){
                    TaskInfo info = baseTaskService.getTaskInfoById(entry.getKey());
                    info.setFinishTime(DateUtils.getCurrentSeconds());
                    info.setStatus(TaskConstant.Done);
                    info.setTaskAmount(BigDecimal.valueOf(info.getTaskOrder()));
                    baseTaskService.update(info);

                    //判断计划下任务是否完成
                    queryMap.put("planId",info.getPlanId());
                    queryMap.put("valid",1);
                    List<TaskInfo> infos = baseTaskService.getTaskInfoList(queryMap);

                    boolean isTakingDone = true;

                    if(infos!=null){
                        for (TaskInfo taskInfo : infos) {
                            if (!taskMap.containsKey(taskInfo.getTaskId())) {
                                isTakingDone = false;
                            }
                        }
                    }
                    if(isTakingDone){
                        //计划已完成
                        StockTakingHead head = this.getHeadById(info.getPlanId());
                        head.setStatus(StockTakingConstant.Done);
                        this.updateHead(head);
                    }
                }

            }
        }else {
            throw new BizCheckedException("2550097",errorStr.toString());
        }
    }
}

