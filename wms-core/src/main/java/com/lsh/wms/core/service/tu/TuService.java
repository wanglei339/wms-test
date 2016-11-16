package com.lsh.wms.core.service.tu;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.api.model.so.ObdOfcBackRequest;
import com.lsh.wms.api.model.so.ObdOfcItem;
import com.lsh.wms.api.model.wumart.CreateObdDetail;
import com.lsh.wms.api.model.wumart.CreateObdHeader;
import com.lsh.wms.api.service.back.IDataBackService;
import com.lsh.wms.api.service.wumart.IWuMart;
import com.lsh.wms.core.constant.IntegrationConstan;
import com.lsh.wms.core.constant.TuConstant;
import com.lsh.wms.core.constant.WaveConstant;
import com.lsh.wms.core.dao.tu.TuDetailDao;
import com.lsh.wms.core.dao.tu.TuHeadDao;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.so.SoDeliveryService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.stock.StockMoveService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.utils.PackUtil;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.so.ObdDetail;
import com.lsh.wms.model.so.ObdHeader;
import com.lsh.wms.model.so.OutbDeliveryDetail;
import com.lsh.wms.model.so.OutbDeliveryHeader;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.tu.TuDetail;
import com.lsh.wms.model.tu.TuEntry;
import com.lsh.wms.model.tu.TuHead;
import com.lsh.wms.model.wave.WaveDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 2016/10/19 下午8:42
 */
@Component
@Transactional(readOnly = true)
public class TuService {
    private static final Logger logger = LoggerFactory.getLogger(TuService.class);

    @Autowired
    private TuHeadDao tuHeadDao;
    @Autowired
    private TuDetailDao tuDetailDao;
    @Autowired
    private StockMoveService stockMoveService;
    @Autowired
    private WaveService waveService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private StockQuantService stockQuantService;
    @Autowired
    private SoDeliveryService soDeliveryService;
    @Autowired
    private SoOrderService soOrderService;
    @Autowired
    private LocationService locationService;

    @Transactional(readOnly = false)
    public void create(TuHead head) {
        head.setUpdatedAt(DateUtils.getCurrentSeconds());
        head.setCreatedAt(DateUtils.getCurrentSeconds());
        tuHeadDao.insert(head);
    }

    @Transactional(readOnly = false)
    public void createBatchhead(List<TuHead> heads) {
        for (TuHead head : heads) {
            head.setUpdatedAt(DateUtils.getCurrentSeconds());
            head.setCreatedAt(DateUtils.getCurrentSeconds());
            tuHeadDao.insert(head);
        }
    }

    @Transactional(readOnly = false)
    public void update(TuHead head) {
        head.setUpdatedAt(DateUtils.getCurrentSeconds());
        tuHeadDao.update(head);
    }

    public TuHead getHeadByTuId(String tuId) {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("isValid", 1);
        mapQuery.put("tuId", tuId);
        List<TuHead> tuHeads = tuHeadDao.getTuHeadList(mapQuery);
        return (tuHeads != null && tuHeads.size() > 0) ? tuHeads.get(0) : null;
    }

    public List<TuHead> getTuHeadList(Map<String, Object> mapQuery) {
        mapQuery.put("isValid", 1);
        return tuHeadDao.getTuHeadList(mapQuery);
    }

    public List<TuHead> getTuHeadListOnPc(Map<String, Object> params) {
        params.put("isValid", 1);
        return tuHeadDao.getTuHeadListOnPc(params);
    }

    public Integer countTuHeadOnPc(Map<String, Object> mapQuery) {
        mapQuery.put("isValid", 1);
        return tuHeadDao.countTuHeadOnPc(mapQuery);
    }

    public Integer countTuHead(Map<String, Object> mapQuery) {
        mapQuery.put("isValid", 1);
        return tuHeadDao.countTuHead(mapQuery);
    }

    @Transactional(readOnly = false)
    public TuHead removeTuHead(TuHead tuHead) {
        tuHead.setIsValid(0);   //无效
        this.update(tuHead);
        return tuHead;
    }

    @Transactional(readOnly = false)
    public void create(TuDetail detail) {
        detail.setUpdatedAt(DateUtils.getCurrentSeconds());
        detail.setCreatedAt(DateUtils.getCurrentSeconds());
        tuDetailDao.insert(detail);
    }

    @Transactional(readOnly = false)
    public void createBatchDetail(List<TuDetail> details) {
        for (TuDetail detail : details) {
            detail.setUpdatedAt(DateUtils.getCurrentSeconds());
            detail.setCreatedAt(DateUtils.getCurrentSeconds());
            tuDetailDao.insert(detail);
        }
    }


    @Transactional(readOnly = false)
    public void update(TuDetail head) {
        head.setUpdatedAt(DateUtils.getCurrentSeconds());
        tuDetailDao.update(head);
    }

    public TuDetail getDetailById(Long id) {
        return tuDetailDao.getTuDetailById(id);
    }

    /**
     * 根据合板的板id查找detail
     * 板子的id是tuDetail表的唯一key
     *
     * @param boardId
     * @return
     */
    public TuDetail getDetailByBoardId(Long boardId) {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("isValid", 1);
        mapQuery.put("mergedContainerId", boardId);
        List<TuDetail> tuDetails = tuDetailDao.getTuDetailList(mapQuery);
        return (tuDetails != null && tuDetails.size() > 0) ? tuDetails.get(0) : null;
    }

    @Transactional(readOnly = false)
    public TuDetail removeTuDetail(TuDetail detail) {
        detail.setIsValid(0);   //无效
        this.update(detail);
        return detail;
    }

    public List<TuDetail> getTuDeailList(Map<String, Object> mapQuery) {
        mapQuery.put("isValid", 1);
        return tuDetailDao.getTuDetailList(mapQuery);
    }

    public List<TuDetail> getTuDeailListByMergedContainerId(Long mergedContainerId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("mergedContainerId", mergedContainerId);
        params.put("isValid", 1);
        return tuDetailDao.getTuDetailList(params);
    }

    public List<TuDetail> getTuDeailListByTuId(String tuId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("tuId", tuId);
        params.put("isValid", 1);
        return tuDetailDao.getTuDetailList(params);
    }

    public Integer countTuDetail(Map<String, Object> mapQuery) {
        mapQuery.put("isValid", 1);
        return tuDetailDao.countTuDetail(mapQuery);
    }

    /**
     * 通过tu号和门店编码找详情(多条,多板子)
     *
     * @param tuId    运单号
     * @param storeId
     * @return
     */
    public List<TuDetail> getTuDetailByStoreCode(String tuId, Long storeId) {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("tuId", tuId);
        mapQuery.put("storeId", storeId);  //门店号
        mapQuery.put("isValid", 1);
        return this.getTuDeailList(mapQuery);
    }

    /**
     * 销库存,出库
     *
     * @param containerIds
     * @return
     */
    @Transactional(readOnly = false)
    public boolean moveItemToConsumeArea(Set<Long> containerIds) {

        if (null == containerIds || containerIds.size() < 1) {
            throw new BizCheckedException("2880010");
        }
        stockMoveService.moveToConsume(containerIds);
        return true;
    }

    /**
     * 销库存,出库
     *
     * @param tuHead tu头
     * @return
     */
    @Transactional(readOnly = false)
    public boolean creatDeliveryOrderAndDetail(TuHead tuHead) {
        List<TuDetail> tuDetails = this.getTuDeailListByTuId(tuHead.getTuId());
        if (null == tuDetails || tuDetails.size() < 1) {
            throw new BizCheckedException("2990026");
        }
        //找详情
        List<WaveDetail> totalWaveDetails = new ArrayList<WaveDetail>();
        for (TuDetail tuDetail : tuDetails) {
            Long containerId = tuDetail.getMergedContainerId();//可能合板或者没合板
            List<WaveDetail> waveDetails = waveService.getWaveDetailsByMergedContainerId(containerId); //合板
            if (null == waveDetails || waveDetails.size() < 1) {
                waveDetails = waveService.getAliveDetailsByContainerId(containerId);    //没合板
                if (null == waveDetails || waveDetails.size() < 1) {
                    throw new BizCheckedException("2880012");
                }
            }
            totalWaveDetails.addAll(waveDetails);
        }
        //订单维度聚类
        Map<Long, OutbDeliveryHeader> mapHeader = new HashMap<Long, OutbDeliveryHeader>();
        Map<Long, List<OutbDeliveryDetail>> mapDetails = new HashMap<Long, List<OutbDeliveryDetail>>();
        for (WaveDetail waveDetail : totalWaveDetails) {    //没生成
            if (null == mapHeader.get(waveDetail.getOrderId())) {
                OutbDeliveryHeader header = new OutbDeliveryHeader();
                header.setWarehouseId(0L);
                header.setShippingAreaCode("" + waveDetail.getRealCollectLocation());
                header.setWaveId(0L);
                header.setTransPlan(tuHead.getTuId());
                header.setTransTime(new Date());
                ObdHeader obdHeader = soOrderService.getOutbSoHeaderByOrderId(waveDetail.getOrderId());
                if (null == obdHeader) {
                    throw new BizCheckedException("2900007");
                }
                header.setDeliveryCode(obdHeader.getDeliveryCode());
                header.setDeliveryUser(tuHead.getLoadUid().toString());
                header.setDeliveryType(1);
                header.setDeliveryTime(new Date());
                header.setInserttime(new Date());
                mapHeader.put(waveDetail.getOrderId(), header);
                mapDetails.put(waveDetail.getOrderId(), new LinkedList<OutbDeliveryDetail>());
            }
            List<OutbDeliveryDetail> deliveryDetails = mapDetails.get(waveDetail.getOrderId());
            //同订单聚合detail
            OutbDeliveryDetail deliveryDetail = new OutbDeliveryDetail();
            deliveryDetail.setOrderId(waveDetail.getOrderId());
            deliveryDetail.setItemId(waveDetail.getItemId());
            deliveryDetail.setSkuId(waveDetail.getSkuId());
            BaseinfoItem item = itemService.getItem(waveDetail.getItemId());
            deliveryDetail.setSkuName(item.getSkuName());
            deliveryDetail.setBarCode(item.getCode());
            deliveryDetail.setOrderQty(waveDetail.getReqQty()); //todo 哪里会写入
            deliveryDetail.setPackUnit(PackUtil.Uom2PackUnit(waveDetail.getAllocUnitName()));
            //通过stock quant获取到对应的lot信息
            List<StockQuant> stockQuants = stockQuantService.getQuantsByContainerId(waveDetail.getContainerId());
            StockQuant stockQuant = stockQuants.size() > 0 ? stockQuants.get(0) : null;
            deliveryDetail.setLotId(stockQuant == null ? 0L : stockQuant.getLotId());
            deliveryDetail.setLotNum(stockQuant == null ? "" : stockQuant.getLotCode());
            deliveryDetail.setDeliveryNum(waveDetail.getQcQty());
            deliveryDetail.setInserttime(new Date());
            deliveryDetails.add(deliveryDetail);
        }
        for (Long key : mapHeader.keySet()) {
            OutbDeliveryHeader header = mapHeader.get(key);
            List<OutbDeliveryDetail> details = mapDetails.get(key);
            if (details.size() == 0) {
                continue;
            }
            header.setDeliveryId(RandomUtils.genId());
            for (OutbDeliveryDetail detail : details) {
                detail.setDeliveryId(header.getDeliveryId());
            }
            soDeliveryService.insertOrder(header, details);
        }
        //回写发货单的单号
        for (WaveDetail detail : totalWaveDetails) {
            if (detail.getDeliveryId() != 0) {
                continue;
            }
            detail.setDeliveryId(mapHeader.get(detail.getOrderId()).getDeliveryId());
            detail.setShipAt(DateUtils.getCurrentSeconds());
            detail.setDeliveryQty(detail.getQcQty());
            detail.setIsAlive(0L);
            waveService.updateDetail(detail);
        }

        //todo 更新wave有波次,更新波次的状态
//        this.setStatus(waveHead.getWaveId(), WaveConstant.STATUS_SUCC);
        return true;
    }

    /**
     * 生成发货单,效库存
     *
     * @param map
     * @return
     */
    @Transactional(readOnly = false)
    public void createObdAndMoveStockQuant(IWuMart wuMart, Map<String, Object> map, Map<String, Object> ibdObdMap) {
        Set<Long> containerIds = (Set<Long>) map.get("containerIds");
        TuHead tuHead = (TuHead) map.get("tuHead");
        this.moveItemToConsumeArea(containerIds);
        this.creatDeliveryOrderAndDetail(tuHead);
        wuMart.sendSap(ibdObdMap);
        //改变发车状态
        tuHead.setDeliveryAt(DateUtils.getCurrentSeconds());    //发车时间
        tuHead.setStatus(TuConstant.SHIP_OVER);
        this.update(tuHead);

    }


    @Transactional(readOnly = false)
    public void createObdAndMoveStockQuantV2(IDataBackService dataBackService, IWuMart wuMart, Map<String, Object> map, List<WaveDetail> totalWaveDetails) {
        Set<Long> containerIds = (Set<Long>) map.get("containerIds");
        TuHead tuHead = (TuHead) map.get("tuHead");
        this.moveItemToConsumeArea(containerIds);
        this.creatDeliveryOrderAndDetail(tuHead);

        //释放已经没有库存的集货道
        Set<Long> locationIds = new HashSet<Long>();
        for (WaveDetail detail : totalWaveDetails) {
            locationIds.add(detail.getRealCollectLocation());
        }
        //查库存,释放集货道
        for (Long locationId : locationIds) {
            Map<String, Object> mapQuery = new HashMap<String, Object>();
            mapQuery.put("locationId", locationId);
            java.math.BigDecimal qty = stockQuantService.getQty(mapQuery);
            if (0 == qty.compareTo(BigDecimal.ZERO)) {
                //释放集货导
                locationService.unlockLocation(locationId);
                locationService.setLocationUnOccupied(locationId);
            }
        }
        //获取发货单的header
        List<OutbDeliveryHeader> outbDeliveryHeaders = soDeliveryService.getOutbDeliveryHeaderByTmsId(tuHead.getTuId());
        //发货单的detail
        // TODO: 2016/11/14 回传obd
        List<Long> deliveryIds = new ArrayList<Long>();
        for (OutbDeliveryHeader header : outbDeliveryHeaders) {
            deliveryIds.add(header.getDeliveryId());
        }

        List<OutbDeliveryDetail> deliveryDetails = soDeliveryService.getOutbDeliveryDetailList(deliveryIds);
        Set<Long> orderIds = new HashSet<Long>();
        for (OutbDeliveryDetail deliveryDetail : deliveryDetails) {
            orderIds.add(deliveryDetail.getOrderId());
        }

        for (Long orderId : orderIds) {
            ObdHeader obdHeader = soOrderService.getOutbSoHeaderByOrderId(orderId);
            //查询明细。
            List<ObdDetail> obdDetails = soOrderService.getOutbSoDetailListByOrderId(orderId);
            // TODO: 2016/9/23  组装OBD反馈信息 根据货主区分回传lsh或物美

            //组装ofc OBD反馈信息
            ObdOfcBackRequest request = new ObdOfcBackRequest();
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String now = sdf.format(date);
            request.setWms(2);//该字段写死 2
            request.setDeliveryTime(now);
            request.setObdCode(obdHeader.getOrderId().toString());
            request.setSoCode(obdHeader.getOrderOtherId());

            //组装物美反馈信息
            CreateObdHeader createObdHeader = new CreateObdHeader();
            createObdHeader.setOrderOtherId(obdHeader.getOrderOtherId());
            //查询明细。
            List<ObdDetail> soDetails = soOrderService.getOutbSoDetailListByOrderId(orderId);
            List<ObdOfcItem> items = new ArrayList<ObdOfcItem>();

            List<CreateObdDetail> createObdDetails = new ArrayList<CreateObdDetail>();

            for (ObdDetail detail : soDetails) {

                ObdOfcItem item = new ObdOfcItem();

                CreateObdDetail createObdDetail = new CreateObdDetail();

                item.setPackNum(detail.getPackUnit());
                //
                OutbDeliveryDetail deliveryDetail = soDeliveryService.getOutbDeliveryDetail(orderId, detail.getItemId());
                if (deliveryDetail == null) {
                    continue;
                }
                BigDecimal outQty = deliveryDetail.getDeliveryNum();
                item.setSkuQty(outQty);

                //ea转换为包装数量。
                createObdDetail.setDlvQty(PackUtil.EAQty2UomQty(outQty, detail.getPackUnit()));
                createObdDetail.setRefItem(detail.getDetailOtherId());
                createObdDetail.setMaterial(detail.getSkuCode());
                createObdDetails.add(createObdDetail);

                item.setSupplySkuCode(detail.getSkuCode());
                items.add(item);
            }
            request.setDetails(items);
            //TODO 瞎逼判断
            if (obdHeader.getOwnerUid() == 1) {
                wuMart.sendSo2Sap(createObdHeader);
            } else if (obdHeader.getOwnerUid() == 2) {
                dataBackService.ofcDataBackByPost(JSON.toJSONString(request), IntegrationConstan.URL_LSHOFC_OBD);
            }
        }

        //同步库存 todo 力哥
        Set<Long> waveIds = new HashSet<Long>();
        for (WaveDetail detail : totalWaveDetails) {
            waveIds.add(detail.getWaveId());
        }
        //设置发货人和发货时间
        tuHead.setDeliveryAt(DateUtils.getCurrentSeconds());
        tuHead.setStatus(TuConstant.SHIP_OVER);
        this.update(tuHead);
    }

    /**
     * 创建tuHead 和tuDetailList
     *
     * @param tuEntry
     * @return
     */
    @Transactional(readOnly = false)
    public TuEntry createTuEntry(TuEntry tuEntry) {
        TuHead tuHead = tuEntry.getTuHead();
        List<TuDetail> tuDetails = tuEntry.getTuDetails();
        this.create(tuHead);
        this.createBatchDetail(tuDetails);
        return tuEntry;
    }

}
