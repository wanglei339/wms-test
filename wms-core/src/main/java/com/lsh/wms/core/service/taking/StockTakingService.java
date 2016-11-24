package com.lsh.wms.core.service.taking;

import com.alibaba.fastjson.JSON;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.core.constant.ContainerConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.dao.taking.StockTakingDetailDao;
import com.lsh.wms.core.dao.taking.StockTakingHeadDao;
import com.lsh.wms.core.service.container.ContainerService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.location.BaseinfoLocationService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.persistence.PersistenceProxy;
import com.lsh.wms.core.service.stock.StockLotService;
import com.lsh.wms.core.service.stock.StockMoveService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.stock.StockLot;
import com.lsh.wms.model.stock.StockMove;
import com.lsh.wms.model.taking.StockTakingDetail;
import com.lsh.wms.model.taking.StockTakingHead;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional (readOnly = false)
    public void insertHead(StockTakingHead head) {
        head.setCreatedAt(DateUtils.getCurrentSeconds());
        head.setUpdatedAt(DateUtils.getCurrentSeconds());
        headDao.insert(head);
    }

    @Transactional (readOnly = false)
    public void updateHead(StockTakingHead head) {
        head.setUpdatedAt(DateUtils.getCurrentSeconds());

        headDao.update(head);
    }


    @Transactional (readOnly = false)
    public void insertDetailList(List<StockTakingDetail> detailList) {
        for (StockTakingDetail detail : detailList) {
            detail.setCreatedAt(DateUtils.getCurrentSeconds());
            detail.setUpdatedAt(DateUtils.getCurrentSeconds());
        }
        detailDao.batchInsert(detailList);
    }
    @Transactional (readOnly = false)
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
    @Transactional (readOnly = false)
    public void done(Long stockTakingId,List<StockTakingDetail> stockTakingDetails) {
        for(StockTakingDetail stockTakingDetail:stockTakingDetails){
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

        for (StockTakingDetail detail : stockTakingDetails) {
            if(detail.getItemId()==0L){
                continue;
            }
            //StockItem stockItem = new StockItem();
            if (detail.getSkuId().equals(detail.getRealSkuId())) {
                Long containerId = containerService.createContainerByType(ContainerConstant.CAGE).getContainerId();
                StockMove move = new StockMove();
                move.setTaskId(detail.getTaskId());
                move.setSkuId(detail.getSkuId());
                move.setItemId(detail.getItemId());
                move.setStatus(TaskConstant.Done);


                BaseinfoItem item = itemService.getItem(move.getItemId());
                if (detail.getTheoreticalQty().compareTo(detail.getRealQty()) > 0) {
                    move.setQty(detail.getTheoreticalQty().subtract(detail.getRealQty()));
                    move.setFromLocationId(detail.getLocationId());
                    move.setToLocationId(locationService.getInventoryLostLocationId());
                    move.setToContainerId(containerId);
                    //组装回传物美的数据

//                    stockItem.setEntryQnt(detail.getTheoreticalQty().subtract(detail.getRealQty()).toString());
//                    stockItem.setEntryUom("EA");
//
//                    stockItem.setMaterialNo(item.getSkuCode());
//                    itemsLoss.add(stockItem);
                } else {
                    StockLot lot = lotService.getStockLotByLotId(detail.getLotId());
                    move.setLot(lot);
                    move.setQty(detail.getRealQty().subtract(detail.getTheoreticalQty()));
                    move.setFromLocationId(locationService.getInventoryLostLocationId());
                    move.setToLocationId(detail.getLocationId());
                    move.setToContainerId(detail.getContainerId());

//                    stockItem.setEntryQnt(detail.getRealQty().subtract(detail.getTheoreticalQty()).toString());
//                    stockItem.setMaterialNo(item.getSkuCode());
//                    stockItem.setEntryUom("EA");
//                    itemsWin.add(stockItem);
                }
                moveList.add(move);
            } else {
                StockMove moveWin = new StockMove();
                moveWin.setTaskId(detail.getTakingId());
                moveWin.setSkuId(detail.getSkuId());
                moveWin.setToLocationId(locationService.getInventoryLostLocationId());
                moveWin.setFromLocationId(detail.getLocationId());
                moveWin.setQty(detail.getRealQty());
                moveList.add(moveWin);

                StockMove moveLoss = new StockMove();
                moveLoss.setTaskId(detail.getTakingId());
                moveLoss.setSkuId(detail.getSkuId());
                moveLoss.setFromLocationId(locationService.getInventoryLostLocationId());
                moveLoss.setToLocationId(detail.getLocationId());
                moveLoss.setQty(detail.getRealQty());
                moveList.add(moveLoss);
            }
        }
        try {
            moveService.move(moveList,stockTakingId);
        }catch (Exception e) {
            logger.error(e.getMessage());
            throw  new BizCheckedException("2550099");
        }
        this.updateHead(head);
        return;
    }

    public StockTakingHead getHeadById(Long takingId) {
        return headDao.getStockTakingHeadById(takingId);
    }
    public Long chargeTime(Long stockTakingId) {
        Map queryMap = new HashMap();
        queryMap.put("takingId",stockTakingId);
        queryMap.put("round", 3L);
        int i = detailDao.countStockTakingDetail(queryMap);
        if (i!=0){
            return 3L;
        }else {
            queryMap.put("round",2L);
            i=detailDao.countStockTakingDetail(queryMap);
            if (i!=0){
                return 2L;
            }
            return 1L;
        }
    }
    public List<StockTakingHead> queryTakingHead(Map queryMap) {
        return headDao.getStockTakingHeadList(queryMap);
    }
    public List<StockTakingDetail> getDetailByTaskId(Long taskId){
        Map<String,Object> queryMap = new HashMap<String, Object>();
        queryMap.put("taskId", taskId);
        return detailDao.getStockTakingDetailList(queryMap);

    }
    public List<StockTakingDetail> getDetailByTakingId(Long takingId){
        Map<String,Object> queryMap = new HashMap<String, Object>();
        queryMap.put("takingId", takingId);
        return detailDao.getStockTakingDetailList(queryMap);
    }


    public Integer countHead(Map queryMap) {
        return headDao.countStockTakingHead(queryMap);

    }
    public List queryTakingDetail(Map queryMap) {
        return detailDao.getStockTakingDetailList(queryMap);

    }
    @Transactional (readOnly = false)
    public void confirmDifference(Long stockTakingId, long roundTime) {
        List<StockTakingDetail> detailList = this.getDetailListByRound(stockTakingId, roundTime);
        this.done(stockTakingId, detailList);
    }
}

