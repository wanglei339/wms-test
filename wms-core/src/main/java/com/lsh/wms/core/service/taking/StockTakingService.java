package com.lsh.wms.core.service.taking;

import com.alibaba.fastjson.JSON;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.core.constant.*;
import com.lsh.wms.core.dao.stock.OverLossReportDao;
import com.lsh.wms.core.dao.taking.StockTakingDetailDao;
import com.lsh.wms.core.dao.taking.StockTakingHeadDao;
import com.lsh.wms.core.service.container.ContainerService;
import com.lsh.wms.core.service.datareport.DifferenceZoneReportService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.location.BaseinfoLocationService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.persistence.PersistenceProxy;
import com.lsh.wms.core.service.stock.StockLotService;
import com.lsh.wms.core.service.stock.StockMoveService;
import com.lsh.wms.core.service.stock.StockSummaryService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.datareport.DifferenceZoneReport;
import com.lsh.wms.model.stock.*;
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

    @Autowired
    private StockTakingHeadDao headDao;

    @Autowired
    private StockTakingDetailDao detailDao;

    @Autowired
    private PersistenceProxy persistenceProxy;
    @Autowired
    private StockLotService lotService;
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
    private StockSummaryService stockSummaryService;
    @Autowired
    private BaseTaskService baseTaskService;
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
            detail.setCreatedAt(DateUtils.getCurrentSeconds());
            detail.setUpdatedAt(DateUtils.getCurrentSeconds());
        }
        detailDao.batchInsert(detailList);
    }

    @Transactional(readOnly = false)
    public void insertDetail(StockTakingDetail detail) {
        detail.setCreatedAt(DateUtils.getCurrentSeconds());
        detail.setUpdatedAt(DateUtils.getCurrentSeconds());
        detailDao.insert(detail);
    }

    @Transactional(readOnly = false)
    public void updateDetail(StockTakingDetail detail) {
        detail.setUpdatedAt(DateUtils.getCurrentSeconds());
        detailDao.update(detail);
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
            if (detail.getSkuId().equals(detail.getRealSkuId())) {

                if(detail.getTheoreticalQty().compareTo(detail.getRealQty())==0){
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

                if (detail.getTheoreticalQty().compareTo(detail.getRealQty()) > 0) {
                    BigDecimal qty = detail.getTheoreticalQty().subtract(detail.getRealQty());
                    overLossReport.setMoveType(OverLossConstant.LOSS_REPORT);
                    overLossReport.setQty(qty);


                    move.setQty(qty);
                    move.setFromLocationId(detail.getLocationId());
                    move.setToLocationId(locationService.getInventoryLostLocation().getLocationId());
                    move.setToContainerId(containerId);
                    //组装回传物美的数据

                } else {
                    BigDecimal qty = detail.getRealQty().subtract(detail.getTheoreticalQty());
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
                moveWin.setQty(detail.getRealQty());
                moveList.add(moveWin);

                StockMove moveLoss = new StockMove();
                moveLoss.setTaskId(detail.getTakingId());
                moveLoss.setSkuId(detail.getSkuId());
                moveLoss.setFromLocationId(locationService.getInventoryLostLocation().getLocationId());
                moveLoss.setToLocationId(detail.getLocationId());
                moveLoss.setQty(detail.getRealQty());
                moveList.add(moveLoss);
            }
        }
        try {
            this.insertLossOrOver(overLossReports);
            moveService.move(moveList);
            for (StockMove move : moveList) {
                StockDelta delta = new StockDelta();
                delta.setItemId(move.getItemId());
                BigDecimal qty = BigDecimal.ZERO;
                if (locationService.getLocation(move.getFromLocationId()).getType().equals(LocationConstant.INVENTORYLOST)) {
                    qty = move.getQty();
                } else {
                    qty = qty.subtract(move.getQty());
                }
                delta.setInhouseQty(qty);
                delta.setBusinessId(stockTakingId);
                delta.setType(StockConstant.TYPE_STOCK_TAKING);
                stockSummaryService.changeStock(delta);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BizCheckedException("2550099");
        }
        this.updateHead(head);
        return;
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

    public List<StockTakingHead> queryTakingHead(Map queryMap) {
        return headDao.getStockTakingHeadList(queryMap);
    }

    public List<StockTakingDetail> getDetailByTaskId(Long taskId) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("taskId", taskId);
        return detailDao.getStockTakingDetailList(queryMap);

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


    public Integer countHead(Map queryMap) {
        return headDao.countStockTakingHead(queryMap);

    }

    public List queryTakingDetail(Map queryMap) {
        return detailDao.getStockTakingDetailList(queryMap);

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
            if(1 == overLossReport.getOwnerId()){
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
        if(1 == overLossReport.getOwnerId()){
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

    @Transactional(readOnly = false)
    public void doQcPickDifference(StockMove move) throws BizCheckedException {
//        OverLossReport overLossReport = new OverLossReport();
        DifferenceZoneReport differenceZoneReport = new DifferenceZoneReport();
        //插入差异报表
        BaseinfoItem item = itemService.getItem(move.getItemId());
//        overLossReport.setItemId(item.getItemId());
//        overLossReport.setOwnerId(item.getOwnerId());
//        overLossReport.setLotId(0L);
//        overLossReport.setPackName(item.getPackName());
//        overLossReport.setSkuCode(item.getSkuCode());
//        overLossReport.setRefTaskId(move.getTaskId());
//        overLossReport.setMoveType(OverLossConstant.LOSS_REPORT);
//        overLossReport.setQty(move.getQty());
//        overLossReport.setStorageLocation(move.getFromLocationId().toString());
        differenceZoneReport.setItemId(item.getItemId());
        differenceZoneReport.setSkuCode(item.getSkuCode());
        differenceZoneReport.setFromLocationId(move.getFromLocationId());
        differenceZoneReport.setSourceType(ReportConstant.SOURCE_TYPE_QC);
        differenceZoneReport.setUnitName("EA");
        differenceZoneReport.setQty(move.getQty().abs());
        differenceZoneReport.setDirect(ReportConstant.DIRECT_IN);
        TaskInfo qcInfo = baseTaskService.getTaskInfoById(move.getTaskId());
        differenceZoneReport.setOperator(qcInfo.getOperator());

        try {
            //插入差异表
            differenceZoneReportService.insertReport(differenceZoneReport);
            //移到差异区
            moveService.move(move);

            StockDelta delta = new StockDelta();
            delta.setItemId(move.getItemId());
            BigDecimal qty = new BigDecimal(move.getQty().toString());
            //拣货缺交的,是负数
            qty = BigDecimal.ZERO.subtract(qty.abs());
            delta.setInhouseQty(qty);
            delta.setBusinessId(move.getTaskId());
            delta.setType(StockConstant.TYPE_PICK_DEFECT);
            //同步库存判断是直流还是在库的
            Long businessMode = qcInfo.getBusinessMode();
            if (TaskConstant.MODE_INBOUND.equals(businessMode)){
                stockSummaryService.changeStock(delta);
            }
        } catch (Exception e) {
            logger.error("MOVE STOCK FAIL , containerId is " + move.getToContainerId() + "taskId is " + move.getTaskId() + e.getMessage());
            throw new BizCheckedException("2550051");
        }
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
//            stockMoveService.move(move);
            StockDelta delta = new StockDelta();
            delta.setItemId(move.getItemId());
            BigDecimal qty = new BigDecimal(move.getQty().toString());
            if (move.getToLocationId().compareTo(locationId) == 0) {
                qty = BigDecimal.ZERO.subtract(qty);
            }
            if(quant.getIsInhouse().compareTo(1L)==0) {
                delta.setInhouseQty(qty);
                delta.setBusinessId(move.getTaskId());
                delta.setType(StockConstant.TYPE_WRITE_OFF);
                stockSummaryService.changeStock(delta);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BizCheckedException("2550051");
        }
    }
}

