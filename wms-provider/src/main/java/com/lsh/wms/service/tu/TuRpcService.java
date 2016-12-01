package com.lsh.wms.service.tu;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.api.model.wumart.CreateIbdDetail;
import com.lsh.wms.api.model.wumart.CreateIbdHeader;
import com.lsh.wms.api.model.wumart.CreateObdDetail;
import com.lsh.wms.api.model.wumart.CreateObdHeader;
import com.lsh.wms.api.service.so.ISoRpcService;
import com.lsh.wms.api.service.tu.ITuRpcService;
import com.lsh.wms.api.service.wumart.IWuMart;
import com.lsh.wms.core.constant.ItemConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.constant.TuConstant;
import com.lsh.wms.core.service.csi.CsiCustomerService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.po.PoOrderService;
import com.lsh.wms.core.service.so.SoDeliveryService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.stock.StockMoveService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.core.service.tu.TuService;
import com.lsh.wms.core.service.utils.PackUtil;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.csi.CsiCustomer;
import com.lsh.wms.model.po.IbdDetail;
import com.lsh.wms.model.po.IbdHeader;
import com.lsh.wms.model.po.IbdObdRelation;
import com.lsh.wms.model.so.ObdDetail;
import com.lsh.wms.model.so.ObdHeader;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.tu.TuDetail;
import com.lsh.wms.model.tu.TuHead;
import com.lsh.wms.model.wave.WaveDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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
    @Reference
    private ISoRpcService iSoRpcService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private SoDeliveryService soDeliveryService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private SoOrderService soOrderService;
    @Autowired
    private PoOrderService poOrderService;
    @Autowired
    private CsiCustomerService csiCustomerService;


    //    @Reference
//    private IWuMartSap wuMartSap;
//
    @Reference
    private IWuMart wuMart;


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

    public List<TuDetail> getTuDetailByStoreCode(String tuId, Long storeId) throws BizCheckedException {
        if (null == tuId || null == storeId) {
            throw new BizCheckedException("2990027");
        }
        return tuService.getTuDetailByStoreCode(tuId, storeId);
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
        tuHead.setStoreIds(mapRequest.get("customer_ids").toString());
        tuHead.setPreBoard(Long.valueOf(mapRequest.get("pre_board").toString()));
        tuHead.setCommitedAt(Long.valueOf(mapRequest.get("commited_at").toString()));
        tuHead.setScale(Integer.valueOf(mapRequest.get("scale").toString()));
        tuHead.setWarehouseId(mapRequest.get("warehouse_id").toString());
        tuHead.setCompanyName(mapRequest.get("company_name").toString());
        tuHead.setType(TuConstant.TYPE_STORE);  //直流门店
        tuHead.setStatus(TuConstant.UNLOAD);
        if (newTu) {
            tuService.create(tuHead);
        } else {
            tuService.update(tuHead);
        }
        logger.info("[RECEIVE TU]Receive TU success: " + JSON.toJSONString(tuHead));
        return tuHead;
    }

    public TuHead changeRfRestSwitch(Map<String, Object> mapRequest) throws BizCheckedException {
        TuHead tuHead = tuService.getHeadByTuId(mapRequest.get("tuId").toString());
        Integer rfSwitch = Integer.valueOf(mapRequest.get("rfSwitch").toString());
        if (null == tuHead) {
            throw new BizCheckedException("2990022");
        }
        tuHead.setRfSwitch(rfSwitch);
        this.update(tuHead);
        return tuHead;
    }

    /**
     * 板子的托盘码,和运单号,判断该tu门店下的板子获取板子详细信息的方法,写入贵品的判断
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
        BigDecimal boardNum = BigDecimal.ZERO;   //一板子多托的数量
        // 这里要改成同一都一组盘为前置条件
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
        List<CsiCustomer> customers = csiCustomerService.parseCustomerIds2Customers(tuHead.getStoreIds());
        List<WaveDetail> waveDetails = null;    //查找板子的detail
        //板子聚类
        //查看板子的数量 写的有点臃肿,可优化
        if (mergedContainerId.equals(containerId)) { //没合板
            mergedContainerId = containerId;
            waveDetails = waveService.getAliveDetailsByContainerId(mergedContainerId);
            boardNum = new BigDecimal("1");  //托盘拣货就缺交 没合板数量为1
        } else {
            waveDetails = waveService.getWaveDetailsByMergedContainerId(mergedContainerId);   //已经合板
            //计费用的板子数量
            Map<String, Object> mergerQuery = new HashMap<String, Object>();
            mergerQuery.put("containerId", mergedContainerId);
            mergerQuery.put("type", TaskConstant.TYPE_MERGE);
            mergerQuery.put("status", TaskConstant.Done);
            List<TaskInfo> mergeInfos = baseTaskService.getTaskInfoList(mergerQuery);
            if (null == mergeInfos || mergeInfos.size() < 1) {
                throw new BizCheckedException("2870038");
            }
            TaskInfo mergerInfo = mergeInfos.get(0);
            boardNum = mergerInfo.getTaskBoardQty();
        }
        if (waveDetails == null || waveDetails.size() == 0) {
            throw new BizCheckedException("2870040");
        }
        //一个板上的是一个门店的,只用来取店名字(托盘上和板子上只能是相同货主)
        Long orderId = waveDetails.get(0).getOrderId();
        ObdHeader obdHeader = iSoRpcService.getOutbSoHeaderDetailByOrderId(orderId);
        if (null == obdHeader) {
            throw new BizCheckedException("2870006");
        }
        String storeCode = obdHeader.getDeliveryCode();
        Long ownerId = obdHeader.getOwnerUid();
        //货主
        CsiCustomer csiCustomer = csiCustomerService.getCustomerByCustomerCode(storeCode);    //获取storeId
        boolean isSameStrore = false;
        for (CsiCustomer customer : customers) {
            if (customer.getCustomerCode().equals(storeCode)) {  //相同门店
                isSameStrore = true;
                break;
            }
        }
        if (false == isSameStrore) {
            throw new BizCheckedException("2990032");
        }

        //余货看qc的完成时间
        boolean isRest = false;
        //贵品判断逻辑
        Set<Long> itemIds = new HashSet<Long>();
        for (WaveDetail one : waveDetails) {
            //商品判断
            itemIds.add(one.getItemId());
            if (one.getQcAt() < DateUtils.getTodayBeginSeconds()) {
                isRest = true;
            }
        }


        //一个是贵品就是贵品托盘
        boolean isExpensive = false;
        for (Long itemId : itemIds) {
            BaseinfoItem item = itemService.getItem(itemId);
            if (null == item) {
                throw new BizCheckedException("2870041");
            }
            if (item.getIsValuable().equals(ItemConstant.TYPE_IS_VALUABLE)) {
                isExpensive = true;
            }
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
        BigDecimal allboxNum = BigDecimal.ZERO;
        Long turnoverBoxNum = 0L;
        Set<Long> containerSet = new HashSet<Long>();   //可以是板子也可以是托盘
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
        Long curBoards = 0L;
        List<TuDetail> tuDetails = this.getTuDeailListByTuId(tuId);
        if (null == tuDetails || tuDetails.size() < 1) {  //一个板子都没装
            preRestBoard = preBoards;
        } else {
            for (TuDetail one : tuDetails) {
                curBoards += one.getBoardNum();
            }
        }


        preRestBoard = preBoards - curBoards;
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

        if (isExpensive) {
            boardNum = BigDecimal.ZERO;
        }

        result.put("customerId", csiCustomer.getCustomerId());
        result.put("isLoaded", isLoaded);
        result.put("containerId", mergedContainerId);   //板子码
        result.put("taskBoardQty", (int) boardNum.floatValue());    //一个板子的板子数
        result.put("isRest", isRest);
        result.put("isExpensive", isExpensive);    //非贵品
        return result;
    }


    /**
     * 拼接物美数据
     *
     * @param tuId
     * @throws BizCheckedException
     */
    public Map<String, Object> bulidSapDate(String tuId) throws BizCheckedException {

        //找详情
        List<WaveDetail> totalWaveDetails = this.combineWaveDetailsByTuId(tuId);
        List<CreateObdDetail> createObdDetailList = new ArrayList<CreateObdDetail>();
        List<CreateIbdDetail> createIbdDetailList = new ArrayList<CreateIbdDetail>();

        List<CreateObdDetail> createStoObdDetails = new ArrayList<CreateObdDetail>();
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
            createObdDetail.setDlvQty(PackUtil.EAQty2UomQty(oneDetail.getQcQty(), obdDetail.getPackName()).setScale(2, BigDecimal.ROUND_HALF_UP));
            //sto obd detail detail_other_id
            createObdDetail.setRefItem(obdDetail.getDetailOtherId());

            createObdDetail.setOrderType(obdHeader.getOrderType());


            //找关系 sto和cpo
            List<IbdObdRelation> ibdObdRelations = poOrderService.getIbdObdRelationListByObd(obdHeader.getOrderOtherId(), obdDetail.getDetailOtherId());
            if (null == ibdObdRelations || ibdObdRelations.size() < 1) {
                createStoObdDetails.add(createObdDetail);
                continue;
                //throw new BizCheckedException("2900005");
            }
            createObdDetailList.add(createObdDetail);


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
            createIbdDetail.setDeliveQty(PackUtil.EAQty2UomQty(oneDetail.getQcQty(), ibdDetail.getPackName()).setScale(2, BigDecimal.ROUND_HALF_UP));
            //行项目号
            createIbdDetail.setPoItme(ibdDetail.getDetailOtherId());

            createIbdDetail.setVendMat(ibdHeader.getOrderId().toString());

            createIbdDetail.setOrderType(ibdHeader.getOrderType());
            createIbdDetailList.add(createIbdDetail);
        }
        CreateObdHeader createObdHeader = new CreateObdHeader();
        createObdHeader.setItems(createObdDetailList);
        CreateIbdHeader createIbdHeader = new CreateIbdHeader();
        createIbdHeader.setItems(createIbdDetailList);
        CreateObdHeader createStoObdHeader = new CreateObdHeader();

        logger.info("+++++++++++++++++++++++++++++++++maqidi+++++++++++++++++++++++" + JSON.toJSONString(createObdHeader));
        logger.info("+++++++++++++++++++++++++++++++++maqidi++++++++++++++" + JSON.toJSONString(createObdHeader));

        //鑫哥服务
//        wuMartSap.ibd2Sap(createIbdHeader);
//        wuMartSap.obd2Sap(createObdHeader);
        Map<String, Object> ibdObdMap = new HashMap<String, Object>();
        ibdObdMap.put("createIbdHeader", createIbdHeader);
        ibdObdMap.put("createObdHeader", createObdHeader);
        ibdObdMap.put("createStoObdHeader", createStoObdHeader);
        return ibdObdMap;
//        wuMart.sendIbd(createIbdHeader);
//        wuMart.sendObd(createObdHeader);
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

    public void createBatchDetail(List<TuDetail> details) throws BizCheckedException {
        tuService.createBatchDetail(details);
    }

    public void createBatchhead(List<TuHead> heads) throws BizCheckedException {
        tuService.createBatchhead(heads);
    }
}
