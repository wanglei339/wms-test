package com.lsh.wms.rpc.service.stock;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.api.service.stock.IStockQuantRpcService;
import com.lsh.wms.core.constant.*;
import com.lsh.wms.core.service.container.ContainerService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.po.PoReceiptService;
import com.lsh.wms.core.service.stock.StockLotService;
import com.lsh.wms.core.service.stock.StockMoveService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.stock.StockSummaryService;
import com.lsh.wms.core.service.taking.StockTakingService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.csi.CsiSku;
import com.lsh.wms.model.po.InbReceiptDetail;
import com.lsh.wms.model.po.InbReceiptHeader;
import com.lsh.wms.model.stock.*;
import net.sf.json.JSONArray;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by mali on 16/7/28.
 */

@Service(protocol = "dubbo")
public class StockQuantRpcService implements IStockQuantRpcService {
    private static Logger logger = LoggerFactory.getLogger(StockQuantRpcService.class);

    @Autowired
    private StockQuantService quantService;

    @Autowired
    private StockMoveService moveService;
    @Autowired
    private StockTakingService stockTakingService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private ItemService itemService;
    @Autowired
    private ContainerService containerService;

    @Autowired
    private StockLotService lotService;
    @Autowired
    private StockSummaryService stockSummaryService;
    @Autowired
    private PoReceiptService receiptService;


    private Map<String, Object> getQueryCondition(StockQuantCondition condition) throws BizCheckedException {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        try {
            mapQuery = PropertyUtils.describe(condition);
        } catch (Exception e) {
            logger.error(e.getCause()!=null ? e.getCause().getMessage():e.getMessage());
            throw new BizCheckedException("3040001");
        }
        return mapQuery;
    }

    public BigDecimal getQty(StockQuantCondition condition) throws BizCheckedException {
        Map<String, Object> mapQuery = this.getQueryCondition(condition);
        BigDecimal total = quantService.getQty(mapQuery);
        return total;
    }

    public List<StockQuant> getQuantList(StockQuantCondition condition) throws BizCheckedException {
        Map<String, Object> mapQuery = this.getQueryCondition(condition);
        this.setExcludeLocationList(mapQuery);
        List<StockQuant> quantList = quantService.getQuants(mapQuery);
        return quantList == null ? new ArrayList<StockQuant>() : quantList;
    }
    public Integer countQuantList(StockQuantCondition condition) throws BizCheckedException {
        Map<String, Object> mapQuery = this.getQueryCondition(condition);
        this.setExcludeLocationList(mapQuery);
        Integer count = quantService.countStockQuant(mapQuery);
        return count;
    }

    public String freeze(Map<String, Object> mapCondition) throws BizCheckedException {
        mapCondition.put("isFrozen", 0);
        mapCondition.put("isDefect", 0);
        mapCondition.put("isRefund", 0);
        BigDecimal total = quantService.getQty(mapCondition);
        BigDecimal requiredQty = BigDecimal.ZERO;
        if (mapCondition.get("qty") != null) {
            requiredQty = new BigDecimal(mapCondition.get("qty").toString());
        } else {
            requiredQty = total;
        }
        if (total.compareTo(requiredQty) < 0 || total.compareTo(BigDecimal.ZERO) == 0) {
            throw new BizCheckedException("2550001");
        }

        mapCondition.put("requiredQty", requiredQty);
        quantService.process(mapCondition, "freeze");

        return JsonUtils.SUCCESS();
    }

    public String unfreeze(Map<String, Object> mapCondition) throws BizCheckedException {
        mapCondition.put("canUnFreeze", true);
        BigDecimal total = quantService.getQty(mapCondition);
        BigDecimal requiredQty = BigDecimal.ZERO;
        if (mapCondition.get("qty") != null) {
            requiredQty = new BigDecimal(mapCondition.get("qty").toString());
        } else {
            requiredQty = total;
        }
        if (total.compareTo(requiredQty) == -1 || total.compareTo(BigDecimal.ZERO) == 0) {
            throw new BizCheckedException("2550001");
        }

        mapCondition.put("requiredQty", requiredQty);
        quantService.process(mapCondition, "unFreeze");

        return JsonUtils.SUCCESS();
    }

    public String toDefect(Map<String, Object> mapCondition) throws BizCheckedException {
        mapCondition.put("isDefect", 0);
        BigDecimal total = quantService.getQty(mapCondition);
        BigDecimal requiredQty = BigDecimal.ZERO;
        if (mapCondition.get("qty") != null) {
            requiredQty = new BigDecimal(mapCondition.get("qty").toString());
        } else {
            requiredQty = total;
        }

        if (total.compareTo(requiredQty) == -1 || total.compareTo(BigDecimal.ZERO) == 0) {
            throw new BizCheckedException("2550001");
        }
        mapCondition.put("requiredQty", requiredQty);
        quantService.process(mapCondition, "toDefect");

        return JsonUtils.SUCCESS();
    }

    public String toRefund(Map<String, Object> mapCondition) throws BizCheckedException {
        mapCondition.put("isRefund", 0);
        BigDecimal total = quantService.getQty(mapCondition);
        BigDecimal requiredQty = BigDecimal.ZERO;
        if (mapCondition.get("qty") != null) {
            requiredQty = new BigDecimal(mapCondition.get("qty").toString());
        } else {
            requiredQty = total;
        }
        if (total.compareTo(requiredQty) == -1 || total.compareTo(BigDecimal.ZERO) == 0) {
            throw new BizCheckedException("2550001");
        }
        mapCondition.put("requiredQty", requiredQty);
        quantService.process(mapCondition, "toRefund");

        return JsonUtils.SUCCESS();
    }
    public void writeOffQuant(Long quantId, BigDecimal realQty,Long operator)throws BizCheckedException{
        StockQuant quant = quantService.getQuantById(quantId);
        StockMove move = new StockMove();
        move.setOperator(operator);
        StockLot lot = lotService.getStockLotByLotId(quant.getLotId());
        BigDecimal qty = quantService.getQuantQtyByContainerId(quant.getContainerId());
        if(realQty.compareTo(BigDecimal.ZERO)<0){
            throw new BizCheckedException("2550085");
        }
        BigDecimal differenceQty= realQty.subtract(qty);
        if(differenceQty.compareTo(realQty)==0){
            return;
        }
        if(differenceQty.compareTo(BigDecimal.ZERO) < 0) {
            move.setTaskId(WriteOffConstant.WRITE_OFF_TASK_ID);
            move.setSkuId(quant.getSkuId());
            move.setItemId(quant.getItemId());
            move.setOwnerId(quant.getOwnerId());
            move.setStatus(TaskConstant.Done);

            move.setQty(differenceQty.abs());
            move.setFromLocationId(quant.getLocationId());
            move.setFromContainerId(quant.getContainerId());
            move.setToLocationId(locationService.getNullArea().getLocationId());
            move.setToContainerId(containerService.createContainerByType(ContainerConstant.CAGE).getContainerId());
        }else {
            move.setTaskId(WriteOffConstant.WRITE_OFF_TASK_ID);
            move.setSkuId(quant.getSkuId());
            move.setItemId(quant.getItemId());
            move.setOwnerId(quant.getOwnerId());
            move.setStatus(TaskConstant.Done);
            move.setQty(differenceQty);
            move.setFromLocationId(locationService.getNullArea().getLocationId());
            move.setToLocationId(quant.getLocationId());
            move.setToContainerId(quant.getContainerId());
            move.setLot(lot);
        }
        moveService.move(move);
    }
    public int getItemStockCount(Map<String, Object> mapQuery) {
        return itemService.countItem(mapQuery);
    }

    public Map<Long, Map<String, BigDecimal>> getItemStockList(Map<String, Object> mapQuery) {
        Map<Long, Map<String, BigDecimal>> itemQuant = new HashMap<Long, Map<String, BigDecimal>>();

        List<BaseinfoItem> itemList = itemService.searchItem(mapQuery);
        if (CollectionUtils.isEmpty(itemList)) {
            return itemQuant;
        }
        for (BaseinfoItem item:itemList) {
            Long itemId = item.getItemId();
            StockSummary summary = stockSummaryService.getStockSummaryByItemId(itemId);
            Map<String, BigDecimal> result = new HashMap<String, BigDecimal>();
            result.put("total", summary == null ? BigDecimal.ZERO : summary.getInhouseQty());
            result.put("reserved", summary == null ? BigDecimal.ZERO : summary.getAllocQty());
            result.put("available", summary == null ? BigDecimal.ZERO : summary.getAvailQty());
            result.put("defect", summary == null ? BigDecimal.ZERO : summary.getDefectQty());
            result.put("refund", summary == null ? BigDecimal.ZERO : summary.getBackQty());
            itemQuant.put(itemId, result);
        }
        return itemQuant;
    }
    public void updateStockInfoList() {
        Long beginDate = DateUtils.getCurrentSeconds();
        logger.info("stock_begin_time"+beginDate);
        Map<String,Object> mapQuery = new HashMap<String, Object>();
        Map<Long,Integer> stockInfoMap = new HashMap<Long, Integer>();
        List<StockQuantInfo> insertStockQuantInfos = new ArrayList<StockQuantInfo>();
        List<StockQuantInfo> updateStockQuantInfos = new ArrayList<StockQuantInfo>();

        //获取到info中的所有信息
        List<StockQuantInfo> stockQuantInfos = quantService.getStockInfoList(new HashMap<String, Object>());
        //初始化信息
        if(stockQuantInfos!=null){
            for(StockQuantInfo stockQuantInfo :stockQuantInfos){
                stockInfoMap.put(stockQuantInfo.getItemId(),1);
            }
        }

        List<BaseinfoItem> itemList = itemService.searchItem(mapQuery);
        if(itemList!=null){
            Map<String,Object> queryStockMap = new HashMap<String, Object>();
            this.setExcludeLocationList(queryStockMap);
            for(BaseinfoItem baseinfoItem: itemList){
                StockQuantInfo stockQuantInfo = new StockQuantInfo();
                stockQuantInfo.setItemId(baseinfoItem.getItemId());
                stockQuantInfo.setCodeType(baseinfoItem.getCodeType());
                stockQuantInfo.setCode(baseinfoItem.getCode());
                stockQuantInfo.setSkuCode(baseinfoItem.getSkuCode());
                stockQuantInfo.setPackName(baseinfoItem.getPackName());
                stockQuantInfo.setPackUnit(baseinfoItem.getPackUnit());
                stockQuantInfo.setShelfLife(baseinfoItem.getShelfLife());
                stockQuantInfo.setOwnerId(baseinfoItem.getOwnerId());

                StockSummary summary = stockSummaryService.getStockSummaryByItemId(baseinfoItem.getItemId());
                stockQuantInfo.setAllQty(summary == null ? BigDecimal.ZERO : summary.getInhouseQty());
                stockQuantInfo.setSoCloseQty(summary == null ? BigDecimal.ZERO : summary.getAllocQty());
                stockQuantInfo.setCanUseQty(summary == null ? BigDecimal.ZERO : summary.getAvailQty());
                stockQuantInfo.setDefectQty(summary == null ? BigDecimal.ZERO : summary.getDefectQty());
                stockQuantInfo.setReturnQty(summary == null ? BigDecimal.ZERO : summary.getBackQty());
                //剩余保质期多个

                queryStockMap.put("itemId",baseinfoItem.getItemId());
                List<StockQuant> quants =  quantService.getQuants(queryStockMap);
                double minLifeRet = 1;
                Map<Integer,Integer> remainShelfLifeMap = new HashMap<Integer, Integer>();
                List<Integer> remainShelfLifeList = new ArrayList<Integer>();
                if(quants!=null){
                    for(StockQuant quant:quants){
                        Long now = DateUtils.getCurrentSeconds();
                        int remainDate = (int)((quant.getExpireDate() - now) / (24.0 * 60.0 * 60.0)) ;
                        if(!remainShelfLifeMap.containsKey(remainDate)){
                            double lifeRet = 1;
                            if(stockQuantInfo.getShelfLife().compareTo(BigDecimal.ZERO)!=0){
                                lifeRet = (remainDate) / stockQuantInfo.getShelfLife().doubleValue();
                            }

                            if(lifeRet < minLifeRet){
                                minLifeRet = lifeRet;
                            }
                            remainShelfLifeList.add(remainDate);
                            remainShelfLifeMap.put(remainDate,1);
                        }
                    }
                }

                stockQuantInfo.setRemainShelfLife(JSONArray.fromObject(remainShelfLifeList).toString());
                stockQuantInfo.setMinLifeRet(minLifeRet);
                if(!stockInfoMap.containsKey(stockQuantInfo.getItemId())){
                    insertStockQuantInfos.add(stockQuantInfo);
                }else {
                    updateStockQuantInfos.add(stockQuantInfo);
                }
            }
        }
        quantService.initializeStockInfoList(updateStockQuantInfos,insertStockQuantInfos);

        logger.info("stock_begin_time"+beginDate+",stock_end_time" + DateUtils.getCurrentSeconds());

    }
    public void updateStockLocationInfoList() {
        Long beginDate = DateUtils.getCurrentSeconds();
        logger.info("stock_location_begin_time"+beginDate);
        Map<String,Object> mapQuery = new HashMap<String, Object>();

        //目标表已存在的数据
        Map<String,StockQuantLocationInfo> stockLocationMap = new HashMap<String, StockQuantLocationInfo>();

        //需要倒入的数据
        Map<String,StockQuantLocationInfo> stockQuantInfos = new LinkedHashMap<String, StockQuantLocationInfo>();

        List<StockQuantLocationInfo> insertStockQuantInfos = new ArrayList<StockQuantLocationInfo>();
        List<StockQuantLocationInfo> updateStockQuantInfos = new ArrayList<StockQuantLocationInfo>();
        List<StockQuantLocationInfo> delStockQuantInfos = new ArrayList<StockQuantLocationInfo>();



        List<StockQuantLocationInfo> stockQuantLocationInfos = quantService.getStockLocationInfoList(mapQuery);
        //初始化map
        if(stockQuantLocationInfos!=null){
            for(StockQuantLocationInfo stockQuantLocationInfo:stockQuantLocationInfos){
                String key = String.format(" locationId:[%d],itemId:[%d],supplierId:[%d]",stockQuantLocationInfo.getLocationId(),stockQuantLocationInfo.getItemId(),stockQuantLocationInfo.getSupplierId());
                stockLocationMap.put(key,stockQuantLocationInfo);
            }
        }

        Map<String,Object> queryStockMap = new HashMap<String, Object>();
        this.setExcludeLocationList(queryStockMap);
        //获取到所有可展示的quant
        List<StockQuant> quants =  quantService.getQuants(queryStockMap);
        if(quants!=null){
            for(StockQuant quant: quants){
                StockQuantLocationInfo locationInfo = null;
                String key = String.format(" locationId:[%d],itemId:[%d],supplierId:[%d]",quant.getLocationId(),quant.getItemId(),quant.getSupplierId());
                if(stockQuantInfos.get(key)!=null){
                    locationInfo = stockQuantInfos.get(key);
                    locationInfo.setQty(locationInfo.getQty().add(quant.getQty()));
//                    if(quant.getExpireDate().compareTo(locationInfo.getMinLife()) ==0) {
//                        Long now = DateUtils.getCurrentSeconds();
//                        int remainDate = (int)((quant.getExpireDate() - now) / (24 * 60 * 60));
//                        locationInfo.setMinLifeRet(locationInfo.getShelfLife().compareTo(BigDecimal.ZERO) == 0 ? 1 : ((double) remainDate) / locationInfo.getShelfLife().doubleValue());
//                        locationInfo.setMinLife(quant.getExpireDate());
//                        stockQuantInfos.put(key,locationInfo);
//                    }
                }else {
                    locationInfo = new StockQuantLocationInfo();
                    BaseinfoItem item = itemService.getItem(quant.getItemId());
                    locationInfo.setLocationId(quant.getLocationId());
                    locationInfo.setSkuCode(item.getSkuCode());
                    locationInfo.setCode(item.getCode());
                    locationInfo.setItemId(item.getItemId());
                    locationInfo.setCodeType(item.getCodeType());
                    locationInfo.setShelfLife(item.getShelfLife());
                    locationInfo.setPackName(item.getPackName());
                    locationInfo.setPackUnit(item.getPackUnit());
                    locationInfo.setSupplierId(quant.getSupplierId());
                    locationInfo.setOwnerId(quant.getOwnerId());
                    locationInfo.setQty(quant.getQty());

                    BaseinfoLocation location = locationService.getLocation(quant.getLocationId());

                    //捡货位和区级别的都是默认为1
                    Long now = DateUtils.getCurrentSeconds();
                    int remainDate = (int)(((quant.getExpireDate() - now) / (24.0 * 60.0 * 60.0)));

                    if(location.getBinUsage().compareTo(BinUsageConstant.BIN_UASGE_PICK)==0 || location.getBinUsage().compareTo(0)==0){
                        locationInfo.setMinLifeRet(1.0);
                        locationInfo.setMinLife(0);
                    }else {

                        locationInfo.setMinLifeRet(item.getShelfLife().compareTo(BigDecimal.ZERO) == 0 ? 1 : (remainDate) / locationInfo.getShelfLife().doubleValue());
                        locationInfo.setMinLife(remainDate);
                    }
                }
                if(stockLocationMap.containsKey(key)){
                    stockLocationMap.put(key, null);
                }
                stockQuantInfos.put(key,locationInfo);
            }
        }

        for (String key : stockQuantInfos.keySet()){
            if(stockLocationMap.containsKey(key)){
                updateStockQuantInfos.add(stockQuantInfos.get(key));
            }else {
                insertStockQuantInfos.add(stockQuantInfos.get(key));
            }
        }
        for(String key : stockLocationMap.keySet()){
            if(stockLocationMap.get(key) !=null){
                delStockQuantInfos.add(stockLocationMap.get(key));
            }
        }

        quantService.initializeStockLocationInfoList(updateStockQuantInfos, insertStockQuantInfos, delStockQuantInfos);

        logger.info("stock_location_begin_time"+beginDate+",stock_location__end_time" + DateUtils.getCurrentSeconds());

    }

    private void setExcludeLocationList(Map<String, Object> mapQuery) {
        List<BaseinfoLocation> excludeLocationList = new ArrayList<BaseinfoLocation>();
        excludeLocationList.add(locationService.getNullArea());
        excludeLocationList.add(locationService.getSupplyArea());
        excludeLocationList.add(locationService.getConsumerArea());
        excludeLocationList.add(locationService.getSoAreaDirect());
        excludeLocationList.add(locationService.getSoAreaInbound());
        excludeLocationList.add(locationService.getDiffAreaLocation());
        excludeLocationList.add(locationService.getInventoryLostLocation());
        mapQuery.put("excludeLocationList", excludeLocationList);
    }
    public List<StockQuantInfo> getStockInfo(Map<String, Object> mapQuery) {
        return quantService.getStockInfoList(mapQuery);
    }
    public Integer countStockInfo(Map<String, Object> mapQuery) {
        return quantService.countStockInfoList(mapQuery);
    }

    public int getLocationStockCount(Map<String, Object> mapQuery) {
        setExcludeLocationList(mapQuery);
        return quantService.countStockQuant(mapQuery);
    }
    public Integer countStockLocationInfo(Map<String, Object> mapQuery) {
        return quantService.countStockLocationInfoList(mapQuery);
    }

    public List<StockQuantLocationInfo> getStockLocationInfo(Map<String, Object> mapQuery) {
        return quantService.getStockLocationInfoList(mapQuery);
    }

    public List<StockQuant> getLocationStockList(Map<String, Object> mapQuery) {
        setExcludeLocationList(mapQuery);
        return quantService.getQuants(mapQuery);
    }

    public List<StockMove> traceQuant(Long quantId) {
        return moveService.traceQuant(quantId);
    }

    public List<StockQuant> getItemLocationList(Map<String, Object> mapQuery) {
        return quantService.getItemLocationList(mapQuery);
    }
    //获取商品可退货的库存
    public List<StockQuant> getBackItemLocationList(Map<String, Object> mapQuery) {
        List<BaseinfoLocation> includeLocationList = new ArrayList<BaseinfoLocation>();
        includeLocationList.addAll(locationService.getLocationsByType(LocationConstant.SHELFS));//货架
        includeLocationList.addAll(locationService.getLocationsByType(LocationConstant.LOFTS));//阁楼
        includeLocationList.addAll(locationService.getLocationsByType(LocationConstant.FLOOR));//地堆
        includeLocationList.addAll(locationService.getLocationsByType(LocationConstant.SPLIT_AREA));//拆零
        mapQuery.put("includeLocationList", includeLocationList);
        return quantService.getItemLocationList(mapQuery);
    }

    public Long getLotByReceiptContainerId(Long containerId) throws BizCheckedException {
        //根据托盘码查找 InbReceiptHeader
        Map<String,Object> queryMap = new HashMap<String, Object>();
        queryMap.put("containerId",containerId);
        InbReceiptHeader receiptHeader = receiptService.getInbReceiptHeaderByParams(queryMap);
        if(receiptHeader==null){
            return 0L;
        }
        List<InbReceiptDetail> details = receiptService.getInbReceiptDetailListByReceiptId(receiptHeader.getReceiptOrderId());
        return details.get(0).getLotId();
    }

}
