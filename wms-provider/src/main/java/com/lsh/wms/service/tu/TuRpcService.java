package com.lsh.wms.service.tu;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.config.PropertyUtils;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.net.HttpClientUtils;
import com.lsh.base.common.utils.BeanMapTransUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.api.model.wumart.CreateIbdDetail;
import com.lsh.wms.api.model.wumart.CreateIbdHeader;
import com.lsh.wms.api.model.wumart.CreateObdDetail;
import com.lsh.wms.api.model.wumart.CreateObdHeader;
import com.lsh.wms.api.service.so.ISoRpcService;
import com.lsh.wms.api.service.tu.ITuRpcService;
import com.lsh.wms.api.service.wumart.IWuMartSap;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.constant.TuConstant;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.po.PoOrderService;
import com.lsh.wms.core.service.so.SoDeliveryService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.stock.StockMoveService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.store.StoreService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.core.service.tu.TuService;
import com.lsh.wms.core.service.utils.PackUtil;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoStore;
import com.lsh.wms.model.po.IbdDetail;
import com.lsh.wms.model.po.IbdHeader;
import com.lsh.wms.model.po.IbdObdRelation;
import com.lsh.wms.model.so.ObdDetail;
import com.lsh.wms.model.so.ObdHeader;
import com.lsh.wms.model.so.OutbDeliveryDetail;
import com.lsh.wms.model.so.OutbDeliveryHeader;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.tu.TuDetail;
import com.lsh.wms.model.tu.TuHead;
import com.lsh.wms.model.wave.WaveDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 2016/10/20 下午2:12
 */
@Service(protocol = "dubbo")
public class TuRpcService implements ITuRpcService {

    private static Logger logger = LoggerFactory.getLogger(TuRpcService.class);

    @Autowired
    private TuService tuService;
    @Autowired
    private StockMoveService stockMoveService;
    @Autowired
    private BaseTaskService baseTaskService;
    @Autowired
    private WaveService waveService;
    @Autowired
    private StoreService storeService;
    @Reference
    private ISoRpcService iSoRpcService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private StockQuantService stockQuantService;
    @Autowired
    private SoDeliveryService soDeliveryService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private SoOrderService soOrderService;
    @Autowired
    private PoOrderService poOrderService;

    @Reference
    private IWuMartSap wuMartSap;


    public TuHead create(TuHead tuHead) throws BizCheckedException {
        //先查有无,有的话,不能创建
        TuHead preHead = this.getHeadByTuId(tuHead.getTuId());
        if (preHead != null) {
            throw new BizCheckedException("2990020");
        }
        tuService.create(tuHead);
        return tuHead;
    }

    public TuHead update(TuHead tuHead) throws BizCheckedException {
        tuService.update(tuHead);
        return tuHead;
    }

    public TuHead getHeadByTuId(String tuId) throws BizCheckedException {
        if (null == tuId || tuId.equals("")) {
            throw new BizCheckedException("2990021");
        }
        TuHead tuHead = tuService.getHeadByTuId(tuId);
        return tuHead;
    }

    public List<TuHead> getTuHeadList(Map<String, Object> mapQuery) throws BizCheckedException {
        return tuService.getTuHeadList(mapQuery);
    }

    /**
     * PC上筛选tuList的方法,涉及时间区间的传入
     *
     * @param params
     * @return
     * @throws BizCheckedException
     */
    public List<TuHead> getTuHeadListOnPc(Map<String, Object> params) throws BizCheckedException {
        return tuService.getTuHeadListOnPc(params);
    }

    /**
     * PC上筛选tuList的方法,涉及时间区间的传入
     *
     * @param mapQuery
     * @return
     * @throws BizCheckedException
     */
    public Integer countTuHeadOnPc(Map<String, Object> mapQuery) throws BizCheckedException {
        return tuService.countTuHeadOnPc(mapQuery);
    }

    public Integer countTuHead(Map<String, Object> mapQuery) throws BizCheckedException {
        return tuService.countTuHead(mapQuery);
    }

    public TuHead removeTuHead(String tuId) throws BizCheckedException {
        if (null == tuId || tuId.equals("")) {
            throw new BizCheckedException("2990021");
        }
        TuHead tuHead = tuService.getHeadByTuId(tuId);
        if (null == tuHead) {
            throw new BizCheckedException("2990022");
        }
        return tuService.removeTuHead(tuHead);
    }

    public TuDetail create(TuDetail tuDetail) throws BizCheckedException {
        //先查有无,boardId是唯一的key
        TuDetail preDetail = this.getDetailByBoardId(tuDetail.getMergedContainerId());
        if (preDetail != null) {
            throw new BizCheckedException("2990023");
        }
        tuService.create(tuDetail);
        return tuDetail;
    }

    public TuDetail update(TuDetail tuDetail) throws BizCheckedException {
        tuService.update(tuDetail);
        return tuDetail;
    }

    public TuDetail getDetailByBoardId(Long boardId) throws BizCheckedException {
        if (null == boardId) {
            throw new BizCheckedException("2990024");
        }
        TuDetail tuDetail = tuService.getDetailByBoardId(boardId);
        return tuDetail;
    }

    public List<TuDetail> getTuDeailListByTuId(String tuId) throws BizCheckedException {
        if (null == tuId || tuId.equals("")) {
            throw new BizCheckedException("2990021");
        }
        List<TuDetail> tuDetails = tuService.getTuDeailListByTuId(tuId);
        return tuDetails;
    }

    public TuDetail getDetailById(Long id) throws BizCheckedException {
        if (null == id) {
            throw new BizCheckedException("2990025");
        }
        return tuService.getDetailById(id);
    }

    public List<TuDetail> getTuDeailList(Map<String, Object> mapQuery) throws BizCheckedException {
        return tuService.getTuDeailList(mapQuery);
    }

    public TuDetail removeTuDetail(Long boardId) throws BizCheckedException {
        if (null == boardId) {
            throw new BizCheckedException("2990024");
        }
        TuDetail tuDetail = tuService.getDetailByBoardId(boardId);
        if (null == tuDetail) {
            throw new BizCheckedException("2990026");
        }
        return tuService.removeTuDetail(tuDetail);
    }

    public Integer countTuDetail(Map<String, Object> mapQuery) throws BizCheckedException {
        return tuService.countTuDetail(mapQuery);
    }

    public List<TuDetail> getTuDetailByStoreCode(String tuId, String deliveryCode) throws BizCheckedException {
        if (null == tuId || null == deliveryCode) {
            throw new BizCheckedException("2990027");
        }
        return tuService.getTuDetailByStoreCode(tuId, deliveryCode);
    }

    public TuHead changeTuHeadStatus(String tuId, Integer status) throws BizCheckedException {
        if (null == tuId || tuId.equals("")) {
            throw new BizCheckedException("2990021");
        }
        TuHead tuHead = this.getHeadByTuId(tuId);
        if (null == tuHead) {
            throw new BizCheckedException("2990022");
        }
        tuHead.setStatus(status);
        this.update(tuHead);
        return tuHead;
    }

    public TuHead changeTuHeadStatus(TuHead tuHead, Integer status) throws BizCheckedException {
        tuHead.setStatus(status);
        this.update(tuHead);
        return tuHead;
    }

    /**
     * 使用POST方式将TU发车
     *
     * @param tuId
     * @throws BizCheckedException
     */
    public Boolean postTuDetails(String tuId) throws BizCheckedException {
        TuHead tuHead = tuService.getHeadByTuId(tuId);
        Map<String, String> result = new HashMap<String, String>();
        String responseBody = "";
        if (tuHead == null) {
            throw new BizCheckedException("2990022");
        }
        if (!tuHead.getStatus().equals(TuConstant.SHIP_OVER)) {
            throw new BizCheckedException("2990037");
        }
        List<TuDetail> tuDetails = tuService.getTuDeailListByTuId(tuId);
        List<Map<String, Object>> details = new ArrayList<Map<String, Object>>();
        for (TuDetail tuDetail : tuDetails) {
            Map<String, Object> detail = BeanMapTransUtils.Bean2map(tuDetail);
            BaseinfoStore store = storeService.getBaseinfoStore(tuDetail.getStoreId());
            detail.put("storeNo", store.getStoreNo());
            detail.put("storeName", store.getStoreName());
            details.add(detail);
        }
        result.put("tuId", tuId);
        result.put("scale", tuHead.getScale().toString());
        result.put("tuDetails", JSON.toJSONString(details));
        String url = PropertyUtils.getString("tms_ship_over_url");
        int timeout = PropertyUtils.getInt("tms_timeout");
        String charset = PropertyUtils.getString("tms_charset");
        Map<String, String> headMap = new HashMap<String, String>();
        headMap.put("Content-type", "application/x-www-form-urlencoded; charset=utf-8");
        headMap.put("Accept", "*/*");
        logger.info("[SHIP OVER]Begin to transfer to TMS, " + "URL: " + url + ", Request body: " + JSON.toJSONString(result));
        try {
            responseBody = HttpClientUtils.post(url, result, timeout, charset, headMap);
        } catch (Exception e) {
            logger.info("[SHIP OVER]Transfer to TMS failed: " + responseBody);
            return false;
        }
        logger.info("[SHIP OVER]Transfer to TMS success: " + responseBody);
        return true;
    }

    /**
     * 接收TU头信息
     *
     * @param mapRequest
     * @return
     * @throws BizCheckedException
     */
    public TuHead receiveTuHead(Map<String, Object> mapRequest) throws BizCheckedException {
        logger.info("[RECEIVE TU]Receive TU: " + JSON.toJSONString(mapRequest));
        String tuId = mapRequest.get("tu_id").toString();
        TuHead tuHead = tuService.getHeadByTuId(tuId);
        Boolean newTu = true;
        if (tuHead != null) {
            logger.info("[RECEIVE TU]Receive TU: " + tuId + " is duplicated");
            newTu = false;
        } else {
            tuHead = new TuHead();
        }
        tuHead.setTuId(tuId);
        tuHead.setTransUid(Long.valueOf(mapRequest.get("trans_uid").toString()));
        tuHead.setCellphone(mapRequest.get("cellphone").toString());
        tuHead.setName(mapRequest.get("name").toString());
        tuHead.setCarNumber(mapRequest.get("car_number").toString());
        tuHead.setStoreIds(mapRequest.get("store_ids").toString());
        tuHead.setPreBoard(Long.valueOf(mapRequest.get("pre_board").toString()));
        tuHead.setCommitedAt(Long.valueOf(mapRequest.get("commited_at").toString()));
        tuHead.setScale(Integer.valueOf(mapRequest.get("scale").toString()));
        tuHead.setStatus(TuConstant.UNLOAD);
        if (newTu) {
            tuService.create(tuHead);
        } else {
            tuService.update(tuHead);
        }
        logger.info("[RECEIVE TU]Receive TU success: " + JSON.toJSONString(tuHead));
        return tuHead;
    }

    /**
     * 关闭尾货开关
     *
     * @param tuId
     * @return
     * @throws BizCheckedException
     */
    public void closeRfRestSwitch(String tuId) throws BizCheckedException {
        TuHead tuHead = tuService.getHeadByTuId(tuId);
        if (null == tuHead) {
            throw new BizCheckedException("2990022");
        }
        tuHead.setRfSwitch(TuConstant.RF_CLOSE_REST);
        this.update(tuHead);
    }

    /**
     * 开启rf尾货开关
     *
     * @param tuId
     * @return
     * @throws BizCheckedException
     */
    public void openRfRestSwitch(String tuId) throws BizCheckedException {
        TuHead tuHead = tuService.getHeadByTuId(tuId);
        if (null == tuHead) {
            throw new BizCheckedException("2990022");
        }
        tuHead.setRfSwitch(TuConstant.RF_OPEN_REST);
        this.update(tuHead);
    }

    /**
     * 移动板子库存到消费区
     *
     * @param waveDetails
     * @return
     * @throws BizCheckedException
     */
    public boolean moveItemToConsumeArea(List<WaveDetail> waveDetails) throws BizCheckedException {
        if (null == waveDetails || waveDetails.size() < 1) {
            throw new BizCheckedException("2880012");
        }
        for (WaveDetail detail : waveDetails) {
            stockMoveService.moveToConsume(detail.getContainerId());
        }
        return true;
    }

    /**
     * 消除物理托盘的库存
     *
     * @param containerId
     * @return
     * @throws BizCheckedException
     */
    public boolean moveItemToConsumeArea(Long containerId) throws BizCheckedException {
        if (null == containerId) {
            throw new BizCheckedException("2880010");
        }
        stockMoveService.moveToConsume(containerId);
        return true;
    }

    /**
     * 板子的托盘码,和运单号,判断该tu门店下的板子获取板子详细信息的方法
     *
     * @param containerId 物理托盘码
     * @param tuId        运单号
     * @return
     * @throws BizCheckedException
     */
    public Map<String, Object> getBoardDetailBycontainerId(Long containerId, String tuId) throws BizCheckedException {
        TuHead tuHead = this.getHeadByTuId(tuId);
        //大店装车的前置条件是合板,小店是组盘完成
        Long mergedContainerId = null;  //需要存入detail的id, 大店是合板的id,小店是物理托盘码
        if (TuConstant.SCALE_STORE.equals(tuHead.getScale())) {    //小店看组盘
            //QC+done+containerId 找到mergercontaierId
            Map<String, Object> qcMapQuery = new HashMap<String, Object>();
            qcMapQuery.put("containerId", containerId);
            qcMapQuery.put("type", TaskConstant.TYPE_QC);
            qcMapQuery.put("status", TaskConstant.Done);
            List<TaskInfo> qcInfos = baseTaskService.getTaskInfoList(qcMapQuery);
            if (null == qcInfos || qcInfos.size() < 1) {
                throw new BizCheckedException("2870034");
            }
            mergedContainerId = qcInfos.get(0).getMergedContainerId();  //没合板,托盘码和板子码,qc后两者相同
        } else { //大店也是组盘完毕就能装车
            Map<String, Object> qcMapQuery = new HashMap<String, Object>();
            qcMapQuery.put("containerId", containerId);
            qcMapQuery.put("type", TaskConstant.TYPE_QC);
            qcMapQuery.put("status", TaskConstant.Done);
            List<TaskInfo> qcInfos = baseTaskService.getTaskInfoList(qcMapQuery);
            if (null == qcInfos || qcInfos.size() < 1) {
                throw new BizCheckedException("2870034");
            }
            mergedContainerId = qcInfos.get(0).getMergedContainerId();
        }
        //获取门店信息
        List<Map<String, Object>> stores = storeService.analyStoresIds2Stores(tuHead.getStoreIds());
        List<WaveDetail> waveDetails = null;    //查找板子的detail
        //板子聚类
        if (mergedContainerId.equals(containerId)) { //没合板
            mergedContainerId = containerId;
            waveDetails = waveService.getAliveDetailsByContainerId(mergedContainerId);
        } else {
            waveDetails = waveService.getWaveDetailsByMergedContainerId(mergedContainerId);   //已经合板
        }
        //一个板上的是一个门店的
        Long orderId = waveDetails.get(0).getOrderId();
        ObdHeader obdHeader = iSoRpcService.getOutbSoHeaderDetailByOrderId(orderId);
        if (null == obdHeader) {
            throw new BizCheckedException("2870006");
        }
        String storeCode = obdHeader.getDeliveryCode();
        Long storeId = storeService.getStoreIdByCode(storeCode).get(0).getStoreId();    //获取storeId
        boolean isSameStrore = false;
        for (Map<String, Object> store : stores) {
            if (store.get("storeNo").toString().equals(storeCode)) {  //相同门店
                isSameStrore = true;
                break;
            }
        }
        if (false == isSameStrore) {
            throw new BizCheckedException("2990032");
        }
        //聚类板子的箱数,以QC聚类
        Map<String, Object> taskQuery = new HashMap<String, Object>();
        taskQuery.put("mergedContainerId", mergedContainerId);
        taskQuery.put("type", TaskConstant.TYPE_QC);
        taskQuery.put("status", TaskConstant.Done);
        List<TaskInfo> taskInfos = baseTaskService.getTaskInfoList(taskQuery);
        if (null == taskInfos || taskInfos.size() < 1) {
            throw new BizCheckedException("2870034");
        }
        BigDecimal allboxNum = new BigDecimal("0.00");
        Long turnoverBoxNum = new Long("0");
        Set<Long> containerSet = new HashSet<Long>();
        for (TaskInfo taskinfo : taskInfos) {
            BigDecimal one = taskinfo.getTaskPackQty(); //总箱数
            turnoverBoxNum += taskinfo.getExt3();    //总周转周转箱
            allboxNum = allboxNum.add(one);   //总箱子
            containerSet.add(taskinfo.getMergedContainerId());
        }
        //结果集
        Integer containerNum = containerSet.size(); //以板子为维度
        Map<String, Object> result = new HashMap<String, Object>();
        //预估剩余板数,预装-已装
        Long preBoards = tuHead.getPreBoard();
        Long preRestBoard = null;   //预估剩余可装板子数
        List<TuDetail> tuDetails = this.getTuDeailListByTuId(tuId);
        if (null == tuDetails || tuDetails.size() < 1) {  //一个板子都没装
            preRestBoard = preBoards;
        }
        preRestBoard = preBoards - tuDetails.size();
        result.put("preRestBoard", preRestBoard);    //预估剩余板数
        result.put("containerNum", containerNum);
        result.put("boxNum", allboxNum);    //总箱数
        result.put("turnoverBoxNum", turnoverBoxNum);
        //是否已装车
        boolean isLoaded = false;
        TuDetail tuDetail = this.getDetailByBoardId(mergedContainerId);
        if (null != tuDetail) {
            isLoaded = true;
        }
        result.put("storeId", storeId);
        result.put("isLoaded", isLoaded);
        result.put("containerId", mergedContainerId);
        result.put("isRest", false); //非余货
        result.put("isExpensive", false);    //非贵品
        return result;
    }

    /**
     * 按照订单的维度生成发货单
     * 所有的tuDetail聚合
     *
     * @param tuHead
     * @throws BizCheckedException
     */
    public void creatDeliveryOrderAndDetail(TuHead tuHead) throws BizCheckedException {
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
                header.setDeliveryCode("");
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
            deliveryDetail.setOrderQty(waveDetail.getReqQty());
            //todo 箱子规则
            deliveryDetail.setPackUnit(waveDetail.getAllocUnitQty());
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
    }

    /**
     * 拼接物美数据
     *
     * @param tuId
     * @throws BizCheckedException
     */
    public void bulidSapDate(String tuId) throws BizCheckedException {

        //找详情
        List<WaveDetail> totalWaveDetails = this.combineWaveDetailsByTuId(tuId);
        List<CreateObdDetail> createObdDetailList = new ArrayList<CreateObdDetail>();
        List<CreateIbdDetail> createIbdDetailList = new ArrayList<CreateIbdDetail>();
        for (WaveDetail oneDetail : totalWaveDetails) {
            Long itemId = oneDetail.getItemId();
            Long orderId = oneDetail.getOrderId();
            CreateObdDetail createObdDetail = new CreateObdDetail();
            //obd的detail
            ObdDetail obdDetail = soOrderService.getObdDetailByOrderIdAndItemId(orderId, itemId);
            if (null == obdDetail) {
                throw new BizCheckedException("2900004");
            }
            //sto obd order_other_id
            ObdHeader obdHeader = soOrderService.getOutbSoHeaderByOrderId(obdDetail.getOrderId());
            createObdDetail.setRefDoc(obdHeader.getOrderOtherId());
            //销售单位
            createObdDetail.setSalesUnit(obdDetail.getPackName());
            //交货量 qc的ea/销售单位
            createObdDetail.setDlvQty(PackUtil.EAQty2UomQty(oneDetail.getQcQty(), obdDetail.getPackName()));
            //sto obd detail detail_other_id
            createObdDetail.setRefItem(obdDetail.getDetailOtherId());
            createObdDetailList.add(createObdDetail);

            //找关系 sto和cpo
            List<IbdObdRelation> ibdObdRelations = poOrderService.getIbdObdRelationListByObd(obdHeader.getOrderOtherId(), obdDetail.getDetailOtherId());
            if (null == ibdObdRelations || ibdObdRelations.size() < 1) {
                throw new BizCheckedException("2900005");
            }
            IbdObdRelation ibdObdRelation = ibdObdRelations.get(0);
            IbdHeader ibdHeader = poOrderService.getInbPoHeaderByOrderOtherId(ibdObdRelation.getIbdOtherId());
            IbdDetail ibdDetail = poOrderService.getInbPoDetailByOrderIdAndDetailOtherId(ibdHeader.getOrderId(), ibdObdRelation.getIbdDetailId());
            //拼装CreateIbdDetail
            CreateIbdDetail createIbdDetail = new CreateIbdDetail();
            //采购凭证号
            createIbdDetail.setPoNumber(ibdHeader.getOrderOtherId());
            //采购订单的计量单位
            createIbdDetail.setUnit(ibdDetail.getPackName());
            //实际出库数量
            createIbdDetail.setDeliveQty(PackUtil.EAQty2UomQty(oneDetail.getQcQty(), ibdDetail.getPackName()));
            //行项目号
            createIbdDetail.setPoItme(ibdDetail.getDetailOtherId());
            createIbdDetailList.add(createIbdDetail);
        }
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        XMLGregorianCalendar gcTime = null;
        try {
            gcTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        } catch (Exception e) {
            throw new BizCheckedException("2900006");
        }
        CreateObdHeader createObdHeader = new CreateObdHeader();
        createObdHeader.setDueDate(gcTime);
        createObdHeader.setItems(createObdDetailList);
        CreateIbdHeader createIbdHeader = new CreateIbdHeader();
        createIbdHeader.setDeliveDate(gcTime);
        createIbdHeader.setItems(createIbdDetailList);
        logger.info("+++++++++++++++++++++++++++++++++maqidi+++++++++++++++++++++++"+JSON.toJSONString(createObdHeader));
        logger.info("+++++++++++++++++++++++++++++++++maqidi++++++++++++++"+JSON.toJSONString(createObdHeader));
        //鑫哥服务
        wuMartSap.ibd2Sap(createIbdHeader);
        wuMartSap.obd2Sap(createObdHeader);
    }

    /**
     * 根据tuid聚类waveDetail
     *
     * @param tuId
     * @return
     * @throws BizCheckedException
     */
    public List<WaveDetail> combineWaveDetailsByTuId(String tuId) throws BizCheckedException {
        TuHead tuHead = this.getHeadByTuId(tuId);
        if (null == tuHead) {
            throw new BizCheckedException("2990022");
        }
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
        return totalWaveDetails;
    }
}
