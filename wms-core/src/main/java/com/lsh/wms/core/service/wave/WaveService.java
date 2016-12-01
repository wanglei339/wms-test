package com.lsh.wms.core.service.wave;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.core.constant.WaveConstant;
import com.lsh.wms.core.dao.so.ObdHeaderDao;
import com.lsh.wms.core.dao.wave.WaveDetailDao;
import com.lsh.wms.core.dao.wave.WaveHeadDao;
import com.lsh.wms.core.dao.wave.WaveQcExceptionDao;
import com.lsh.wms.core.service.csi.CsiCustomerService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.so.SoDeliveryService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.stock.StockMoveService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.utils.IdGenerator;
import com.lsh.wms.core.service.utils.PackUtil;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.csi.CsiCustomer;
import com.lsh.wms.model.so.OutbDeliveryDetail;
import com.lsh.wms.model.so.OutbDeliveryHeader;
import com.lsh.wms.model.so.ObdHeader;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.wave.WaveAllocDetail;
import com.lsh.wms.model.wave.WaveDetail;
import com.lsh.wms.model.wave.WaveHead;
import com.lsh.wms.model.wave.WaveQcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.prefs.BackingStoreException;

/**
 * Created by zengwenjun on 16/7/15.
 */

@Component
@Transactional(readOnly = true)
public class WaveService {
    private static final Logger logger = LoggerFactory.getLogger(WaveService.class);

    @Autowired
    private WaveHeadDao waveHeadDao;
    @Autowired
    private ObdHeaderDao soHeaderDao;
    @Autowired
    private WaveAllocService allocService;
    @Autowired
    private WaveDetailDao detailDao;
    @Autowired
    private WaveQcExceptionDao qcExceptionDao;
    @Autowired
    private StockQuantService stockQuantService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private SoDeliveryService soDeliveryService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private IdGenerator idGenerator;
    @Autowired
    private StockMoveService stockMoveService;
    @Autowired
    private SoOrderService soOrderService;
    @Autowired
    private CsiCustomerService csiCustomerService;

    @Transactional(readOnly = false)
    public void createWave(WaveHead head, List<Map> vOrders){
        //gen waveId
        head.setWaveId(idGenerator.genId("wave", true, false));
        head.setCreatedAt(DateUtils.getCurrentSeconds());
        head.setUpdatedAt(DateUtils.getCurrentSeconds());
        head.setReleaseAt(0L);
        waveHeadDao.insert(head);
        this.addToWave(head.getWaveId(), vOrders);
    }

    @Transactional(readOnly = false)
    public void addToWave(long iWaveId, List<Map> vOrders) {
        for(int i = 0; i < vOrders.size(); i++){
            //更新wave信息
            Map so = vOrders.get(i);
            ObdHeader obdHeader = new ObdHeader();
            obdHeader.setOrderId(Long.valueOf(so.get("orderId").toString()));
            //obdHeader.setOrderOtherId(so.get("orderId").toString());
            if(so.get("transPlan")!=null) {
                obdHeader.setTransPlan(so.get("transPlan").toString());
            }
            if(so.get("waveIndex")!=null) {
                obdHeader.setWaveIndex(Integer.valueOf(so.get("waveIndex").toString()));
            }
            if(so.get("transTime")!=null) {
                obdHeader.setTransTime(DateUtils.parse(so.get("transTime").toString()));
            }
            obdHeader.setWaveId(iWaveId);
            obdHeader.setOrderStatus(2);
            soHeaderDao.updateByOrderOtherIdOrOrderId(obdHeader);
        }
    }

    public List<WaveHead> getWaveList(Map<String, Object> mapQuery){
        return waveHeadDao.getWaveHeadList(mapQuery);
    }

    public int getWaveCount(Map<String, Object> mapQuery){
        return waveHeadDao.countWaveHead(mapQuery);
    }

    public WaveHead getWave(long iWaveId){
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("waveId", iWaveId);
        List<WaveHead> WaveHeadList = waveHeadDao.getWaveHeadList(mapQuery);
        return WaveHeadList.size() == 0 ? null : WaveHeadList.get(0);
    }

    public List<WaveDetail> getAliveDetailsByContainerId (Long containerId) {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("containerId", containerId);
        mapQuery.put("isValid", 1);
        mapQuery.put("isAlive", 1);
        List<WaveDetail> details = detailDao.getWaveDetailList(mapQuery);
        return details.size() == 0 ? null : details;
    }

    public List<WaveDetail> getWaveDetailsByMergedContainerId (Long mergedContainerId) {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("mergedContainerId", mergedContainerId);
        mapQuery.put("isValid", 1);
        mapQuery.put("isAlive", 1);
        List<WaveDetail> details = detailDao.getWaveDetailList(mapQuery);
        return details.size() == 0 ? null : details;
    }
    public List<WaveDetail> getWaveDetails (Map<String, Object> mapQuery) {
        List<WaveDetail> details = detailDao.getWaveDetailList(mapQuery);
        return details.size() == 0 ? null : details;
    }

    @Transactional(readOnly = false)
    public void update(WaveHead head){
        head.setUpdatedAt(DateUtils.getCurrentSeconds());
        waveHeadDao.update(head);
    }
    @Transactional(readOnly = false)
    public WaveDetail getDetailByContainerIdAndItemId(Long containerId ,Long itemId){
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("containerId",containerId);
        mapQuery.put("itemId", itemId);
        mapQuery.put("isValid", 1);
        mapQuery.put("isAlive", 1);
        List<WaveDetail> waveDetails = detailDao.getWaveDetailList(mapQuery);
        if(waveDetails== null || waveDetails.size()==0){
            return null;
        }else {
            return waveDetails.get(0);
        }
    }

    /**
     * 通过托盘码和skuId获取detail
     * @param containerId
     * @param skuId
     * @return
     */
    public List<WaveDetail> getDetailByContainerIdAndSkuId(Long containerId ,Long skuId){
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("containerId",containerId);
        mapQuery.put("skuId", skuId);
        mapQuery.put("isValid", 1);
        mapQuery.put("isAlive", 1);
        List<WaveDetail> waveDetails = detailDao.getWaveDetailList(mapQuery);
        if(waveDetails== null || waveDetails.size()==0){
            return null;
        }else {
            return waveDetails;
        }
    }

    @Transactional(readOnly = false)
    public void setStatus(long iWaveId, int iStatus){
        WaveHead head = this.getWave(iWaveId);
        if(head == null) return ;
        head.setStatus((long) iStatus);
        if(iStatus == WaveConstant.STATUS_RELEASE_SUCC){
            head.setReleaseAt(DateUtils.getCurrentSeconds());
        }
        this.update(head);
    }

    @Transactional(readOnly = false)
    public void setStatus(long iWaveId, int iStatus, boolean isAllAlloc){
        WaveHead head = this.getWave(iWaveId);
        if(head == null) return ;
        head.setIsAllAlloc(isAllAlloc ? 1L : 0L);
        head.setStatus((long) iStatus);
        this.update(head);
    }

    @Transactional(readOnly = false)
    public void storeAlloc(WaveHead head, List<WaveAllocDetail> details){
        head.setIsResAlloc(1L);
        this.update(head);
        allocService.addAllocDetails(details);
    }

    @Transactional(readOnly = true)
    public List<WaveDetail> getDetailsByPickTaskId(long pickTaskId){
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("pickTaskId", pickTaskId);
        mapQuery.put("isValid", 1);
        mapQuery.put("isAlive", 1);
        return detailDao.getWaveDetailList(mapQuery);
    }

    @Transactional(readOnly = true)
    public List<WaveDetail> getOrderedDetailsByPickTaskIds(List<Long> pickTaskIds){
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("pickTaskIds", pickTaskIds);
        mapQuery.put("isValid", 1);
        mapQuery.put("isAlive", 1);
        return detailDao.getOrderedWaveDetailList(mapQuery);
    }

    @Transactional(readOnly = true)
    public WaveDetail getWaveDetailById(long id){
        return detailDao.getWaveDetailById(id);
    }

    public List<WaveDetail> getDetailsByWaveId(long waveId){
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("waveId", waveId);
        mapQuery.put("isValid", 1);
        mapQuery.put("isAlive", 1);
        return detailDao.getWaveDetailList(mapQuery);
    }

    public List<WaveDetail> getDetailsSpecial(HashMap<String, Object> mapQuery){
        return detailDao.getWaveDetailList(mapQuery);
    }

    @Transactional(readOnly = true)
    public WaveDetail getDetailByPickTaskIdAndPickOrder(Long pickTaskId, Long pickOrder) {
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("pickTaskId", pickTaskId);
        mapQuery.put("pickOrder", pickOrder);
        mapQuery.put("isValid", 1);
        mapQuery.put("isAlive", 1);
        return detailDao.getOrderedWaveDetailList(mapQuery).get(0);
    }

    @Transactional(readOnly = true)
    public List<WaveDetail> getDetailsByQCTaskId(long qcTaskId){
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("qcTaskId", qcTaskId);
        mapQuery.put("isValid", 1);
        mapQuery.put("isAlive", 1);
        return detailDao.getWaveDetailList(mapQuery);
    }

    @Transactional(readOnly = true)
    public List<WaveDetail> getDetailsByContainerId(long containerId){
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("containerId", containerId);
        mapQuery.put("isValid", 1);
        mapQuery.put("isAlive", 1);
        return detailDao.getWaveDetailList(mapQuery);
    }

    @Transactional(readOnly = true)
    public List<WaveDetail> getDetailsByShipTaskId(long shipTaskId){
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("shipTaskId", shipTaskId);
        mapQuery.put("isValid", 1);
        mapQuery.put("isAlive", 1);
        return detailDao.getWaveDetailList(mapQuery);
    }

    @Transactional(readOnly = true)
    public List<WaveDetail> getDetailsByLocationId(long locationId){
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("locationId", locationId);
        mapQuery.put("isValid", 1);
        mapQuery.put("isAlive", 1);
        return detailDao.getWaveDetailList(mapQuery);
    }

    @Transactional(readOnly = true)
    public List<WaveDetail> getDetailsByCollectionLocation(BaseinfoLocation collectLocation){
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("collectLocationObj", collectLocation);
        mapQuery.put("isValid", 1);
        mapQuery.put("isAlive", 1);
        return detailDao.getWaveDetailList(mapQuery);
    }

    @Transactional(readOnly = false)
    public void insertDetail(WaveDetail detail){
        detail.setUpdatedAt(DateUtils.getCurrentSeconds());
        detail.setCreatedAt(DateUtils.getCurrentSeconds());
        detailDao.insert(detail);
    }

    @Transactional(readOnly = false)
    public void updateDetail(WaveDetail detail){
        detail.setUpdatedAt(DateUtils.getCurrentSeconds());
        detailDao.update(detail);
    }

    @Transactional(readOnly = false)
    public void updateDetails(List<WaveDetail> details){
        for(WaveDetail detail : details) {
            this.updateDetail(detail);
        }
    }

    @Transactional(readOnly = true)
    public BigDecimal getUnPickedQty(Map<String, Object> mapQuery)
    {
        BigDecimal unPickedQty = detailDao.getUnPickedQty(mapQuery);
        if ( unPickedQty == null ){
            unPickedQty = new BigDecimal("0.0000");
        }
        return unPickedQty;
    }

    @Transactional(readOnly = true)
    public BigDecimal getUnAllocQty(long itemId, BaseinfoLocation location)
    {
        //可用库存=仓位有效stock_quant求和(怎么求)-wave_detail的有效alloc数量+wave_detail中的捡货数量
        BigDecimal stockQty = stockQuantService.getRealtimeQty(location, itemId);
        Map<String, Object> mapSumQuery = new HashMap<String, Object>();
        mapSumQuery.put("pickLocationObj", location);
        mapSumQuery.put("itemId", itemId);
        mapSumQuery.put("isLive", 1);
        mapSumQuery.put("isValid", 1);
        BigDecimal unPickedQty = this.getUnPickedQty(mapSumQuery);
        return stockQty.subtract(unPickedQty);
    }

    public Map<Long, BigDecimal> getLocationUnAllocQty(BaseinfoLocation location, long itemId){
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("location", location);
        mapQuery.put("itemId", itemId);
        List<StockQuant> quants = stockQuantService.getQuants(mapQuery);
        Map<Long, BigDecimal> locationInventory = new HashMap<Long, BigDecimal>();
        for(StockQuant quant : quants){
            if(locationInventory.get(quant.getLocationId())==null){
                locationInventory.put(quant.getLocationId(), this.getUnAllocQty(itemId, locationService.getLocation(quant.getLocationId())));
            }
        }
        return locationInventory;
    }

    @Transactional(readOnly = true)
    public List<Map> allocByLocation(Map<Long, BigDecimal> locationInventory, BigDecimal qty, long exceptLocation){
        BigDecimal leftSplitQty = qty;
        BigDecimal realSplitQty = new BigDecimal("0.0000");
        List<Map> allocInfos = new LinkedList<Map>();
        for ( Long key : locationInventory.keySet()){
            if(exceptLocation > 0 && key == exceptLocation){
                continue;
            }
            if(leftSplitQty.compareTo(BigDecimal.ZERO)<=0){
                break;
            }
            if(locationInventory.get(key).compareTo(BigDecimal.ZERO) > 0){
                BigDecimal tmpSplitQty = locationInventory.get(key).compareTo(leftSplitQty) > 0 ? leftSplitQty : locationInventory.get(key);
                //new detail
                leftSplitQty = leftSplitQty.subtract(tmpSplitQty);
                realSplitQty = realSplitQty.add(tmpSplitQty);
                Map<String, Object> mapInfo = new HashMap<String, Object>();
                mapInfo.put("locationId", key);
                mapInfo.put("allocQty", tmpSplitQty);
                locationInventory.put(key, locationInventory.get(key).subtract(tmpSplitQty));
                allocInfos.add(mapInfo);
            }
        }
        return allocInfos;
    }

    @Transactional(readOnly = false)
    public List<WaveDetail> splitWaveDetail(WaveDetail detail, BigDecimal splitQty){
        //pick area location
        BaseinfoLocation pickArea = locationService.getLocation(detail.getPickAreaLocation());
        //getLocationUnAllocQty
        Map<Long, BigDecimal> locationInventory = this.getLocationUnAllocQty(pickArea, detail.getItemId());

        List<WaveDetail> splitDetails = new ArrayList<WaveDetail>();
        List<Map> allocInfos = this.allocByLocation(locationInventory, splitQty, detail.getAllocPickLocation());
        BigDecimal realSplitQty = new BigDecimal("0.0000");
        for(Map info : allocInfos){
            WaveDetail newDetail = new WaveDetail();
            ObjUtils.bean2bean(detail, newDetail);
            newDetail.setAllocQty((BigDecimal) info.get("allocQty"));
            realSplitQty = realSplitQty.add((BigDecimal) info.get("allocQty"));
            newDetail.setAllocPickLocation((Long) info.get("locationId"));
            newDetail.setRefDetailId(detail.getId());
            this.insertDetail(newDetail);
            splitDetails.add(newDetail);
        }
        if(allocInfos.size()>0) {
            //-detail
            BigDecimal allocQty = detail.getAllocQty();
            detail.setAllocQty(allocQty.subtract(realSplitQty));
            detail.setAllocUnitQty(PackUtil.EAQty2UomQty(detail.getAllocQty(), detail.getAllocUnitName()));
            this.updateDetail(detail);
        }
        return splitDetails;
    }
    @Transactional(readOnly = false)
    public void splitWaveDetail(WaveDetail detail, BigDecimal requiredQty,Long containerId,Long soOrderId,BigDecimal packUnit,String storeNo){
        Map<String,Object> queryMap = new HashMap<String, Object>();
        queryMap.put("itemId", detail.getItemId());
        queryMap.put("containerId",detail.getContainerId());
        List<WaveDetail> details = detailDao.getOrderedWaveDetailList(queryMap);
        for(WaveDetail waveDetail:details){
            this.split(waveDetail,requiredQty,containerId,soOrderId,packUnit,storeNo);
            requiredQty = requiredQty.subtract(waveDetail.getPickQty());
            if(requiredQty.compareTo(BigDecimal.ZERO)<=0){
                break;
            }
        }
        if(requiredQty.compareTo(BigDecimal.ZERO)>0){
            throw new BizCheckedException("2880011");
        }
    }
    @Transactional(readOnly = false)
    public void split(WaveDetail detail, BigDecimal splitQty , Long containerId,Long soOrderId,BigDecimal packUnit,String storeNo) {
        CsiCustomer customer = csiCustomerService.getCustomerByCustomerCode(storeNo); // 门店对应的集货道

        if(detail.getPickQty().compareTo(splitQty)<=0)
        {
            detail.setContainerId(containerId);
            detail.setOrderId(soOrderId);
            detail.setAllocCollectLocation(customer.getCollectRoadId());
            detail.setRealCollectLocation(customer.getCollectRoadId());
        }else {
            BigDecimal [] decimals = splitQty.divideAndRemainder(packUnit);
            WaveDetail newDetail = new WaveDetail();
            ObjUtils.bean2bean(detail, newDetail);
            newDetail.setContainerId(containerId);
            newDetail.setOrderId(soOrderId);
            newDetail.setPickQty(splitQty);
            newDetail.setAllocQty(splitQty);

            if(decimals[1].compareTo(BigDecimal.ZERO)!=0){
                newDetail.setAllocUnitName("EA");
                newDetail.setAllocUnitQty(splitQty);

                detail.setAllocUnitQty(detail.getPickQty().subtract(splitQty));
                detail.setAllocUnitName("EA");
            }else {
                newDetail.setAllocUnitQty(decimals[1]);
                if(detail.getAllocUnitName().equals("EA")){
                    detail.setAllocUnitQty(detail.getAllocQty().subtract(splitQty));
                }else {
                    newDetail.setAllocUnitName(PackUtil.PackUnit2Uom(packUnit,"EA"));
                    detail.setAllocUnitQty(detail.getAllocQty().subtract(decimals[0]));
                }
            }


            detail.setPickQty(detail.getPickQty().subtract(splitQty));
            detail.setAllocQty(detail.getAllocQty().subtract(splitQty));
            detailDao.insert(newDetail);
        }
        detailDao.update(detail);
    }

    @Transactional(readOnly = false)
    public WaveDetail splitShelfWaveDetail(WaveDetail detail, BigDecimal splitQty, Long order) {
        WaveDetail splitDetail = new WaveDetail();
        BigDecimal restQty = stockQuantService.getQuantQtyByLocationIdAndItemId(detail.getAllocPickLocation(), detail.getItemId());
        // 判断分配拣货位上是否又有库存了
        if (restQty.compareTo(BigDecimal.ZERO) == 1) {
            BigDecimal realSplitQty = BigDecimal.ZERO;
            if (restQty.compareTo(splitQty) >= 0) {
                realSplitQty = splitQty;
            } else {
                realSplitQty = restQty;
            }
            ObjUtils.bean2bean(detail, splitDetail);
            splitDetail.setAllocQty(realSplitQty);
            splitDetail.setRefDetailId(detail.getId());
            splitDetail.setPickOrder(order + 1);
            splitDetail.setPickAt(0L);
            splitDetail.setRealPickLocation(0L);
            splitDetail.setPickQty(BigDecimal.ZERO);
            this.insertDetail(splitDetail);
            detail.setAllocQty(detail.getPickQty());
            detail.setAllocUnitQty(PackUtil.EAQty2UomQty(detail.getAllocQty(), detail.getAllocUnitName()));
            this.updateDetail(detail);
        }
        return splitDetail;
    }

    @Transactional(readOnly = false)
    public void insertQCException(WaveQcException exception){
        exception.setCreatedAt(DateUtils.getCurrentSeconds());
        exception.setUpdatedAt(DateUtils.getCurrentSeconds());
        qcExceptionDao.insert(exception);
    }

    @Transactional(readOnly = true)
    public List<WaveQcException> getExceptionsByWaveId(long waveId){
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("waveId", waveId);
        return qcExceptionDao.getWaveQcExceptionList(mapQuery);
    }

    @Transactional(readOnly = true)
    public List<WaveQcException> getExceptionsByQcTaskId(long taskId){
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("qcTaskId", taskId);
        return qcExceptionDao.getWaveQcExceptionList(mapQuery);
    }

    @Transactional(readOnly = false)
    public void shipWave(WaveHead waveHead, List<WaveDetail> waveDetails){
        Map<Long, OutbDeliveryHeader> mapHeader = new HashMap<Long, OutbDeliveryHeader>();
        Map<Long, List<OutbDeliveryDetail>> mapDetails = new HashMap<Long, List<OutbDeliveryDetail>>();
        for(WaveDetail detail : waveDetails){
            if(detail.getDeliveryId()!=0){
                continue;
            }
            if ( mapHeader.get(detail.getOrderId()) == null ){
                OutbDeliveryHeader header = new OutbDeliveryHeader();
                header.setWarehouseId(0L);
                header.setShippingAreaCode(""+detail.getRealCollectLocation());
                header.setWaveId(waveHead.getWaveId());
                header.setTransPlan("");
                header.setTransTime(new Date());
                header.setDeliveryCode("");
                header.setDeliveryUser("");
                header.setDeliveryType(1);
                header.setDeliveryTime(new Date());
                header.setInserttime(new Date());
                mapHeader.put(detail.getOrderId(), header);
                mapDetails.put(detail.getOrderId(), new LinkedList<OutbDeliveryDetail>());
            }
            List<OutbDeliveryDetail> deliveryDetails = mapDetails.get(detail.getOrderId());
            OutbDeliveryDetail deliveryDetail = new OutbDeliveryDetail();
            deliveryDetail.setOrderId(detail.getOrderId());
            deliveryDetail.setItemId(detail.getItemId());
            deliveryDetail.setSkuId(detail.getSkuId());
            BaseinfoItem item = itemService.getItem(detail.getItemId());
            deliveryDetail.setSkuName(item.getSkuName());
            deliveryDetail.setBarCode(item.getCode());
            deliveryDetail.setOrderQty(detail.getReqQty());
            deliveryDetail.setPackUnit(item.getPackUnit());
            //通过stock quant获取到对应的lot信息
            List<StockQuant> stockQuants = stockQuantService.getQuantsByContainerId(detail.getContainerId());
            StockQuant stockQuant = stockQuants.size() > 0 ? stockQuants.get(0) : null;
            deliveryDetail.setLotId(stockQuant == null ? 0L : stockQuant.getLotId());
            deliveryDetail.setLotNum(stockQuant == null ? "" : stockQuant.getLotCode());
            deliveryDetail.setDeliveryNum(detail.getQcQty());
            deliveryDetail.setInserttime(new Date());
            deliveryDetails.add(deliveryDetail);
        }
        for(Long key : mapHeader.keySet()){
            OutbDeliveryHeader header = mapHeader.get(key);
            List<OutbDeliveryDetail> details = mapDetails.get(key);
            if ( details.size() == 0 ){
                continue;
            }
            header.setDeliveryId(RandomUtils.genId());
            for(OutbDeliveryDetail detail : details){
                detail.setDeliveryId(header.getDeliveryId());
            }
            soDeliveryService.insertOrder(header, details);
            this.updateOrderStatus(key);
        }
        Set<Long> locations = new HashSet<Long>();
        for(WaveDetail detail : waveDetails){
            if(detail.getDeliveryId()!=0) {
                continue;
            }
            detail.setDeliveryId(mapHeader.get(detail.getOrderId()).getDeliveryId());
            detail.setShipAt(DateUtils.getCurrentSeconds());
            detail.setDeliveryQty(detail.getQcQty());
            detail.setIsAlive(0L);
            this.updateDetail(detail);
            locations.add(detail.getRealCollectLocation());
        }
        //库存移动,移出仓库;
        for(Object locationId : locations.toArray()) {
            stockMoveService.moveToConsume((Long)locationId, 0L, 0L);
        }
        //detailDao.shipWave(waveHead.getWaveId());
        this.setStatus(waveHead.getWaveId(), WaveConstant.STATUS_SUCC);
    }

    /*在捡货\QC任务\发货任务完成时调用*/
    public void updateOrderStatus(long orderId){
        //取出order的head
        ObdHeader header = soOrderService.getOutbSoHeaderByOrderId(orderId);
        if(header == null){
            logger.warn("so get fail "+orderId);
            return;
        }
        if(header.getWaveId()<=1){
            return;
            //波次都没进,呵呵
        }
        //取出order的所有detail
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("orderId", orderId);
        mapQuery.put("isValid", 1);
        mapQuery.put("isAlive", 1);
        List<WaveDetail> details = detailDao.getWaveDetailList(mapQuery);
        if(details.size()==0){
            //配货失败,理论上可以细化
            return;
        }
        int pick_num = 0;
        int qc_num = 0;
        int ship_num = 0;
        for(WaveDetail detail : details){
            if(detail.getPickAt()>0){
                pick_num++;
            }
            if(detail.getQcExceptionDone()!=0){
                qc_num++;
            }
            if(detail.getDeliveryId()!=0){
                ship_num++;
            }
        }
        int status = 2;
        if(ship_num==details.size()){
            status = 5;
        }else if ( qc_num == details.size()){
            status = 4;
        }else if ( pick_num == details.size()){
            status = 3;
        }else{
            status = 2;
        }
        if(status == header.getOrderStatus()){
            return;
        }else{
            header.setOrderStatus(status);
            soOrderService.update(header);
        }
    }

    /*在捡货\QC任务\发货任务完成时调用*/
    public void updateWaveStatus(long waveId){
        WaveHead header = this.getWave(waveId);
        if(header == null){
            logger.warn("wave get fail "+waveId);
            return;
        }
        List<WaveDetail> details = this.getDetailsByWaveId(waveId);
        if(details.size()==0){
            logger.warn("wave null "+waveId);
            return;
        }
        int pick_num = 0;
        int qc_num = 0;
        int ship_num = 0;
        for(WaveDetail detail : details){
            if(detail.getPickAt()>0){
                pick_num++;
            }
            if(detail.getQcExceptionDone()!=0){
                qc_num++;
            }
            if(detail.getDeliveryId()!=0){
                ship_num++;
            }
        }
        int status = 30;
        if(ship_num==details.size()){
            status = 50;
        }else if ( qc_num == details.size()){
            status = 32;
        }else if ( pick_num == details.size()){
            status = 31;
        }else{
            status = 30;
        }
        if(status == header.getStatus()){
            return;
        }else{
            header.setStatus((long)status);
            this.update(header);
        }
    }

    @Transactional(readOnly = false)
    public void increaseReleaseQtyByWaveDeTails(List<WaveDetail> details){
        for(WaveDetail detail : details){
            if(detail.getRefObdDetailOtherId() != null) {
                soOrderService.increaseReleaseQty(detail.getAllocQty(), detail.getOrderId(), detail.getRefObdDetailOtherId());
            }
        }
    }

}
