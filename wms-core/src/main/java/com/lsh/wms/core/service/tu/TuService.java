package com.lsh.wms.core.service.tu;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.core.constant.*;
import com.lsh.wms.core.dao.tu.TuDetailDao;
import com.lsh.wms.core.dao.tu.TuHeadDao;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.key.KeyLockService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.persistence.PersistenceManager;
import com.lsh.wms.core.service.persistence.PersistenceProxy;
import com.lsh.wms.core.service.so.SoDeliveryService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.stock.StockMoveService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.stock.StockSummaryService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.core.service.utils.IdGenerator;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.so.ObdDetail;
import com.lsh.wms.model.so.ObdHeader;
import com.lsh.wms.model.so.OutbDeliveryDetail;
import com.lsh.wms.model.so.OutbDeliveryHeader;
import com.lsh.wms.model.stock.StockMove;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
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

    @Autowired
    private PersistenceManager persistenceManager;
    @Autowired
    private PersistenceProxy persistenceProxy;
    @Autowired
    private StockSummaryService stockSummaryService;
    @Autowired
    private KeyLockService keyLockService;
    @Autowired
    private IdGenerator idGenerator;
    @Autowired
    private BaseTaskService baseTaskService;

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
    public boolean moveItemToConsumeArea(Set<Long> containerIds, Long taskId) {
        if (containerIds.size() == 0) {
            return true;
        }
        stockMoveService.moveToConsume(containerIds, taskId);
        return true;
    }

    /**
     * 销库存,出库
     *
     * @param tuDetails
     * @param totalWaveDetails
     * @return
     */
    //作废
    /*
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
        // 调用库存同步服务
        inventoryRedisService.onDelivery(totalWaveDetails);
        //todo 更新wave有波次,更新波次的状态
//        this.setStatus(waveHead.getWaveId(), WaveConstant.STATUS_SUCC);
        return true;
    }
    */

    //获取订单级别的箱数数据
    private static Map<Long, Map<String, Object>> _getOrderBoxInfo(List<TuDetail> tuDetails, List<WaveDetail> totalWaveDetails) {
        Map<Long, Object> containerInfo = new HashMap<Long, Object>();
        for (TuDetail detail : tuDetails) {
            Long containerId = detail.getMergedContainerId();
            Map<String, Object> containerMap = new HashMap<String, Object>();
            containerMap.put("boxNum", detail.getBoxNum());
            containerMap.put("turnoverBoxNum", detail.getTurnoverBoxNum());
            containerInfo.put(containerId, containerMap);
        }

        //结果集里按照orderId聚类托盘,给出箱子数
        //这里看下,扫码的是哪个.
        Map<Long, Set<Long>> orderContainerSet = new HashMap<Long, Set<Long>>();
        for (WaveDetail waveDetail : totalWaveDetails) {
            Long containerId = waveDetail.getContainerId();
            if (containerInfo.get(containerId) == null) {
                containerId = waveDetail.getMergedContainerId();
            }
            if (containerInfo.get(containerId) == null) {
                //出大事了,这个是怎么来的?jb不可能啊.
                continue;
            }
            if (orderContainerSet.containsKey(waveDetail.getOrderId())) {
                orderContainerSet.get(waveDetail.getOrderId()).add(containerId);
            } else {
                Set<Long> contaienrIds = new HashSet<Long>();
                contaienrIds.add(containerId);
                orderContainerSet.put(waveDetail.getOrderId(), contaienrIds);
            }
        }
        //封装so单子和箱子数
        Map<Long, Map<String, Object>> orderBoxInfo = new HashMap<Long, Map<String, Object>>();
        //按照
        for (Long key : orderContainerSet.keySet()) {
            Set<Long> containersInOneOrder = orderContainerSet.get(key);
            BigDecimal boxNum = new BigDecimal("0");
            Long turnoverBoxNum = 0L;
            for (Long one : containersInOneOrder) {
                Map<String, Object> oneContainer = (Map<String, Object>) containerInfo.get(one);
                boxNum = boxNum.add(new BigDecimal(oneContainer.get("boxNum").toString()));
                turnoverBoxNum += Long.parseLong(oneContainer.get("turnoverBoxNum").toString());
            }
            Map<String, Object> orderBoxMap = new HashMap<String, Object>();
            orderBoxMap.put("boxNum", (int) boxNum.floatValue());
            orderBoxMap.put("turnoverBoxNum", turnoverBoxNum);
            orderBoxInfo.put(key, orderBoxMap);
        }
        return orderBoxInfo;
    }

    @Transactional(readOnly = false)
    public List<WaveDetail> createObdAndMoveStockQuant(TuHead tuHead,
                                                       List<TuDetail> tuDetails, Long taskId) {
        Set<Long> totalContainers = new HashSet<Long>();
        //获取全量的wave_detail
        List<WaveDetail> totalWaveDetails = new ArrayList<WaveDetail>();
        for (TuDetail detail : tuDetails) {
            List<WaveDetail> waveDetails = waveService.getAliveDetailsByContainerId(detail.getMergedContainerId());
            if (null == waveDetails || waveDetails.size() < 1) {
                waveDetails = waveService.getWaveDetailsByMergedContainerId(detail.getMergedContainerId());
            }
            if (waveDetails != null) {
                totalWaveDetails.addAll(waveDetails);
                for (WaveDetail waveDetail : waveDetails) {
                    totalContainers.add(waveDetail.getContainerId());
                }
            }
        }
        //计算订单级别的箱数信息
        //这个的前提基本上是要一个出库托盘上只能有一个container,呵呵,这里是有可能会出错的,但是其实不要紧,我门还可以接受.
        Map<Long, Map<String, Object>> orderBoxInfo = this._getOrderBoxInfo(tuDetails, totalWaveDetails);
        //订单维度聚类
        Map<Long, OutbDeliveryHeader> mapHeader = new HashMap<Long, OutbDeliveryHeader>();
        //Map<Long, List<OutbDeliveryDetail>> mapDetails = new HashMap<Long, List<OutbDeliveryDetail>>();
        Map<Long, Map<String, OutbDeliveryDetail>> mapMergedDeliveryDetails = new HashMap<Long, Map<String, OutbDeliveryDetail>>();
        Map<Long, ObdHeader> mapObdHeader = new HashMap<Long, ObdHeader>();
        List<StockMove> orderTaskStockMoveList = new LinkedList<StockMove>();
        for (WaveDetail waveDetail : totalWaveDetails) {    //没生成
            if (null == mapHeader.get(waveDetail.getOrderId())) {
                OutbDeliveryHeader header = new OutbDeliveryHeader();
                header.setWarehouseId(0L);
                header.setShippingAreaCode("" + waveDetail.getRealCollectLocation());
                header.setWaveId(waveDetail.getWaveId());
                header.setTransPlan(tuHead.getTuId());  //FIXME 17/1/3 路线号会有修改
                header.setTransTime(new Date());
                ObdHeader obdHeader = mapObdHeader.get(waveDetail.getOrderId());
                if (obdHeader == null) {
                    obdHeader = soOrderService.getOutbSoHeaderByOrderId(waveDetail.getOrderId());
                    if (null == obdHeader) {
                        throw new BizCheckedException("2900007");
                    }
                    mapObdHeader.put(waveDetail.getOrderId(), obdHeader);
                }
                header.setDeliveryCode(obdHeader.getDeliveryCode());
                header.setDeliveryUser(tuHead.getLoadUid().toString());
                header.setDeliveryType(obdHeader.getOrderType());
                header.setDeliveryTime(new Date());
                header.setInserttime(new Date());
                header.setOrderId(waveDetail.getOrderId());
                header.setTuId(tuHead.getTuId());
                Map<String, Object> boxInfo = orderBoxInfo.get(waveDetail.getOrderId());
                if (boxInfo != null) {
                    header.setBoxNum(Long.valueOf(boxInfo.get("boxNum").toString()));
                    header.setTurnoverBoxNum(Long.valueOf(boxInfo.get("turnoverBoxNum").toString()));
                } else {
                    header.setBoxNum(0L);
                    header.setTurnoverBoxNum(0L);
                }
                mapHeader.put(waveDetail.getOrderId(), header);
                //mapDetails.put(waveDetail.getOrderId(), new LinkedList<OutbDeliveryDetail>());
                mapMergedDeliveryDetails.put(waveDetail.getOrderId(), new HashMap<String, OutbDeliveryDetail>());
            }
            //List<OutbDeliveryDetail> deliveryDetails = mapDetails.get(waveDetail.getOrderId());
            Map<String, OutbDeliveryDetail> rowDeliverys = mapMergedDeliveryDetails.get(waveDetail.getOrderId());
            String key = waveDetail.getItemId().toString();
            OutbDeliveryDetail deliveryDetail = rowDeliverys.get(key);
            if (deliveryDetail == null) {
                //同订单聚合detail
                deliveryDetail = new OutbDeliveryDetail();
                deliveryDetail.setOrderId(waveDetail.getOrderId());
                deliveryDetail.setItemId(waveDetail.getItemId());
                deliveryDetail.setSkuId(waveDetail.getSkuId());
                //BaseinfoItem item = itemService.getItem(waveDetail.getItemId());
                //deliveryDetail.setSkuName(item.getSkuName());
                //deliveryDetail.setBarCode(item.getCode());
                //deliveryDetail.setOrderQty(waveDetail.getReqQty());
                deliveryDetail.setOrderQty(BigDecimal.valueOf(0L));
                deliveryDetail.setPackUnit(BigDecimal.valueOf(1L));
                deliveryDetail.setSkuName("");
                deliveryDetail.setBarCode("");
                //TODO 现在直接干成"EA"吧.
                //deliveryDetail.setPackUnit(PackUtil.Uom2PackUnit(waveDetail.getAllocUnitName()));
                //通过stock quant获取到对应的lot信息
                Map<String, Object> mapQuery = new HashMap<String, Object>();
                mapQuery.put("containerId", waveDetail.getContainerId());
                mapQuery.put("itemId", waveDetail.getItemId());
                //List<StockQuant> stockQuants = stockQuantService.getQuantsByContainerId(waveDetail.getContainerId());
                List<StockQuant> stockQuants = stockQuantService.getQuants(mapQuery);
                StockQuant stockQuant = (null != stockQuants && stockQuants.size() > 0) ? stockQuants.get(0) : null;
                deliveryDetail.setLotId(stockQuant == null ? 0L : stockQuant.getLotId());
                deliveryDetail.setLotNum(stockQuant == null ? "" : stockQuant.getLotCode());
                //deliveryDetail.setDeliveryNum(waveDetail.getQcQty());
                deliveryDetail.setDeliveryNum(BigDecimal.valueOf(0L));
                deliveryDetail.setInserttime(new Date());
                deliveryDetail.setUpdatetime(new Date());
            }
            deliveryDetail.setDeliveryNum(waveDetail.getQcQty().add(deliveryDetail.getDeliveryNum()));
            rowDeliverys.put(key, deliveryDetail);

            //deliveryDetails.add(deliveryDetail);
        }
        for (Long key : mapHeader.keySet()) {
            OutbDeliveryHeader header = mapHeader.get(key);
            List<OutbDeliveryDetail> realDetails = new LinkedList<OutbDeliveryDetail>();
            Map<String, OutbDeliveryDetail> details = mapMergedDeliveryDetails.get(key);
            //List<OutbDeliveryDetail> details = mapDetails.get(key);
            if (details.size() == 0) {
                continue;
            }
            header.setDeliveryId(RandomUtils.genId());
            List<ObdDetail> orderDetails = soOrderService.getOutbSoDetailListByOrderId(header.getOrderId());
            logger.info("size1 " + orderDetails.size());
            Collections.sort(orderDetails, new Comparator<ObdDetail>() {
                //此处可以设定一个排序规则,对波次中的订单优先级进行排序
                public int compare(ObdDetail o1, ObdDetail o2) {
                    return o1.getId().compareTo(o2.getId());
                }
            });
            for (String itemKey : details.keySet()) {
                OutbDeliveryDetail detail = details.get(itemKey);
                detail.setDeliveryId(header.getDeliveryId());
                //进行运算,首先取得订单里的item订货列表信息
                List<ObdDetail> itemOrderList = new LinkedList<ObdDetail>();
                for (ObdDetail obdDetail : orderDetails) {
                    logger.info("caogod " + obdDetail.getItemId() + " " + detail.getItemId());
                    if (obdDetail.getItemId().equals(detail.getItemId())) {
                        itemOrderList.add(obdDetail);
                    }
                }
                logger.info("size2 " + itemOrderList.size());
                //再从发货单里取得曾经发过货的详情,因为可能又多次发货问题,这是需要处理的.
                List<OutbDeliveryDetail> oldDeliverys = soDeliveryService.getOutbDeliveryDetailsByOrderId(header.getOrderId());
                if (oldDeliverys == null) oldDeliverys = new LinkedList<OutbDeliveryDetail>();
                BigDecimal oldDeliveryQty = new BigDecimal("0.0000");
                for (OutbDeliveryDetail oldDelivery : oldDeliverys) {
                    if (oldDelivery.getItemId().equals(detail.getItemId())) {
                        oldDeliveryQty = oldDeliveryQty.add(oldDelivery.getDeliveryNum());
                    }
                }
                BigDecimal leftQty = detail.getDeliveryNum();
                logger.info("cao3 " + oldDeliveryQty + " now qty " + leftQty);
                int idx = 0;
                for (ObdDetail obdDetail : itemOrderList) {
                    idx++;
                    OutbDeliveryDetail newDetail = new OutbDeliveryDetail();
                    ObjUtils.bean2bean(detail, newDetail);
                    BigDecimal orderQty = obdDetail.getOrderQty().multiply(obdDetail.getPackUnit());
                    logger.info("cao4 " + orderQty + " " + oldDeliveryQty);
                    //这里先来先得,先把老的除去先
                    if (orderQty.compareTo(oldDeliveryQty) <= 0) {
                        oldDeliveryQty = oldDeliveryQty.subtract(orderQty);
                        continue;
                    } else {
                        orderQty = orderQty.subtract(oldDeliveryQty);
                    }
                    BigDecimal qty = null;
                    if (idx == itemOrderList.size() || leftQty.compareTo(orderQty) <= 0) {
                        qty = leftQty;
                    } else {
                        qty = orderQty;
                    }
                    logger.info("cao " + qty + " " + leftQty + " " + orderQty);
                    newDetail.setDeliveryNum(qty);
                    newDetail.setRefDetailOtherId(obdDetail.getDetailOtherId());
                    leftQty = leftQty.subtract(qty);
                    logger.info("cao2 " + qty + " " + leftQty + " " + orderQty);
                    realDetails.add(newDetail);
                    if (leftQty.compareTo(BigDecimal.ZERO) == 0) {
                        break;
                    }
                }
                if (leftQty.compareTo(BigDecimal.ZERO) > 0) {
                    //出事了,怎么来的?
                    logger.error(String.format("delivery item out of bound order[%d] item[%d] qty[%s]",
                            header.getOrderId(), detail.getItemId(), detail.getDeliveryNum().toString()));
                    throw new BizCheckedException("2990045");
                }
            }
            if (realDetails.size() == 0) {
                throw new BizCheckedException("2990038");
            }
            soDeliveryService.insertOrder(header, realDetails);
            /*
            这里做obd占用数量扣减
             */
            for (OutbDeliveryDetail deliveryDetail : realDetails) {
                ObdHeader obdHeader = mapObdHeader.get(deliveryDetail.getOrderId());
                StockMove move = new StockMove();
                move.setToLocationId(locationService.getNullArea().getLocationId());
                if (obdHeader.getOrderType() == SoConstant.ORDER_TYPE_DIRECT) {
                    move.setFromLocationId(locationService.getSoAreaDirect().getLocationId());
                } else {
                    move.setFromLocationId(locationService.getSoAreaInbound().getLocationId());
                }
                move.setItemId(deliveryDetail.getItemId());
                //todo 兼容供商退货的没有taskId的方法
                if (null == taskId) {
                    move.setTaskId(header.getDeliveryId());
                } else {
                    move.setTaskId(header.getDeliveryId());
                }
                move.setQty(deliveryDetail.getDeliveryNum());
                orderTaskStockMoveList.add(move);
            }
            waveService.updateOrderStatus(header.getOrderId());
            persistenceProxy.doOne(SysLogConstant.LOG_TYPE_OBD, header.getDeliveryId(), 0);
            //如果是物美的so单 则新增一条日志
            Integer type = header.getDeliveryType();
            ObdHeader obdHeader = soOrderService.getOutbSoHeaderByOrderId(header.getOrderId());
            if (SoConstant.ORDER_TYPE_SO == type && CsiConstan.OWNER_WUMART == obdHeader.getOwnerUid()) {
                persistenceProxy.doOne(SysLogConstant.LOG_TYPE_OBD, header.getDeliveryId(), SysLogConstant.LOG_TARGET_LSHOFC);
            }
        }
        //回写发货单的单号
        Set<Long> waveIds = new HashSet<Long>();
        for (WaveDetail detail : totalWaveDetails) {
            if (detail.getDeliveryId() != 0) {
                logger.info("delivery not not 0 " + detail.getId());
                continue;
            }
            detail.setDeliveryId(mapHeader.get(detail.getOrderId()).getDeliveryId());
            detail.setShipAt(DateUtils.getCurrentSeconds());
            detail.setDeliveryQty(detail.getQcQty());
            detail.setIsAlive(0L);
            waveService.updateDetail(detail);
            waveIds.add(detail.getWaveId());
        }

        //todo 更新wave有波次,更新波次的状态
        for (Object waveId : waveIds.toArray()) {
            Long iWaveId = (Long) waveId;
            if (iWaveId > 1) {
                waveService.updateWaveStatus(iWaveId);
                //trik
                //TODO HEHE
                waveService.setStatus(iWaveId, WaveConstant.STATUS_SUCC);
            }
        }
        //这里做obd占用数量扣减
        stockMoveService.move(orderTaskStockMoveList);
        //做真实库存移动出库
        this.moveItemToConsumeArea(totalContainers, taskId);
        return totalWaveDetails;
    }


    /**
     * 生成发货单,效库存
     *
     * @param tuHead
     * @param tuDetails
     * @return
     */
    /*
    作废

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
    */
    @Transactional(readOnly = false)
    public List<WaveDetail> createObdAndMoveStockQuantV2(TuHead tuHead,
                                                         List<TuDetail> tuDetails, Long taskId) throws BizCheckedException {

        List<WaveDetail> totalWaveDetails = this.createObdAndMoveStockQuant(tuHead, tuDetails, taskId);
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
                BaseinfoLocation curLocation = new BaseinfoLocation();
                curLocation.setLocationId(locationId);
                locationService.unlockLocationAndSetCanUse(curLocation);
            }
        }
        //设置发货人和发货时间
        tuHead.setDeliveryAt(DateUtils.getCurrentSeconds());
        tuHead.setStatus(TuConstant.SHIP_OVER);
        this.update(tuHead);
        //返回
        return totalWaveDetails;
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

    /**
     * 创建tuHead 和tuDetailList
     *
     * @param totalWaveDetails
     * @param tuHead
     * @param shipTaskId
     */
    @Transactional(readOnly = false)
    public void createShipTask(List<WaveDetail> totalWaveDetails,TuHead tuHead,Long shipTaskId) throws BizCheckedException{
        //创建发货任务
        {
//            TaskEntry taskEntry = new TaskEntry();
            TaskInfo shipTaskInfo = new TaskInfo();
            shipTaskInfo.setType(TaskConstant.TYPE_TU_SHIP);
            shipTaskInfo.setTaskId(shipTaskId);
            shipTaskInfo.setTaskName("优供的发货任务[" + totalWaveDetails.get(0).getContainerId() + "]");
            shipTaskInfo.setContainerId(totalWaveDetails.get(0).getContainerId());
            shipTaskInfo.setOperator(tuHead.getLoadUid()); //一个人装车
            shipTaskInfo.setBusinessMode(TaskConstant.MODE_INBOUND);
            shipTaskInfo.setLocationId(totalWaveDetails.get(0).getRealCollectLocation());


            baseTaskService.createShipTu(shipTaskInfo, totalWaveDetails);
            shipTaskInfo.setStatus(TaskConstant.Done);
            shipTaskInfo.setAssignTime(DateUtils.getCurrentSeconds());
            shipTaskInfo.setFinishTime(DateUtils.getCurrentSeconds());
            shipTaskInfo.setUpdatedAt(DateUtils.getCurrentSeconds());
            if (true){
                throw new BizCheckedException("2990048");
            }
            //设置done状态
//            taskEntry.setTaskInfo(shipTaskInfo);
//            taskEntry.setTaskDetailList((List<Object>) (List<?>) totalWaveDetails);
            baseTaskService.update(shipTaskInfo);
        }
    }

    @Transactional(readOnly = false)
    public void createTaskAndObdMove(TuHead tuHead,
                                     List<TuDetail> tuDetails, Long shipTaskId){
        //销库存 写在同个事务中,生成发货单 osd的托盘生命结束,因此只能运行一次
        List<WaveDetail> totalWaveDetails = this.createObdAndMoveStockQuantV2(tuHead, tuDetails, shipTaskId);
        this.createShipTask(totalWaveDetails,tuHead,shipTaskId);
    }


}
