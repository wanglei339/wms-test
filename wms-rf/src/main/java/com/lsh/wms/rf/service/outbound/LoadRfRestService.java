package com.lsh.wms.rf.service.outbound;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.api.service.merge.IMergeRpcService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.so.ISoRpcService;
import com.lsh.wms.api.service.store.IStoreRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.api.service.tu.ILoadRfRestService;
import com.lsh.wms.api.service.tu.ITuRpcService;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.constant.TuConstant;
import com.lsh.wms.core.service.store.StoreService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.core.service.task.TaskHandler;
import com.lsh.wms.core.service.tu.TuRedisService;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.baseinfo.BaseinfoStore;
import com.lsh.wms.model.so.ObdHeader;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.tu.TuDetail;
import com.lsh.wms.model.tu.TuHead;
import com.lsh.wms.model.wave.WaveDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 2016/10/20 下午8:18
 */
@Service(protocol = "rest")
@Path("outbound/load")
@Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class LoadRfRestService implements ILoadRfRestService {
    private static Logger logger = LoggerFactory.getLogger(LoadRfRestService.class);
    @Reference
    private ITuRpcService iTuRpcService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private WaveService waveService;
    @Reference
    private ISoRpcService iSoRpcService;
    @Reference
    private ITaskRpcService iTaskRpcService;
    @Autowired
    private BaseTaskService baseTaskService;
    @Reference
    private IMergeRpcService iMergeRpcService;
    @Reference
    private IStoreRpcService iStoreRpcService;
    @Autowired
    private TuRedisService tuRedisService;

    /**
     * rf获取所有待装车或者已装车的结果集
     *
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("getTuList")
    public String getTuHeadListByLoadStatus() throws BizCheckedException {
        Map<String, Object> mapRequest = RequestUtils.getRequest();
        Integer status = Integer.valueOf(mapRequest.get("status").toString());
        Long loadUid = Long.valueOf(RequestUtils.getHeader("uid").toString());
        //根据传入要的tu单的状态,显示不同list
        if (null == status || null == loadUid) {
            throw new BizCheckedException("2990028");
        }
        if (!TuConstant.UNLOAD.equals(status) && !TuConstant.LOAD_OVER.equals(status)) {
            throw new BizCheckedException("2990029");
        }
        List<TuHead> tuHeads = null;
        //结果集封装 序号,运单号,tu,装车数;//
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<TuHead> headList = new ArrayList<TuHead>();
        //待装车
        if (TuConstant.UNLOAD.equals(status)) {
            //将该人的状态是装车中的也列出来
            Map<String, Object> tuMapQuery = new HashMap<String, Object>();
            tuMapQuery.put("loadUid", loadUid);
            tuMapQuery.put("status", TuConstant.IN_LOADING);
            List<TuHead> doingHeads = iTuRpcService.getTuHeadList(tuMapQuery);
            if (null != doingHeads && doingHeads.size() > 0) {
                headList.addAll(doingHeads);
            }
            //待装车的
            Map<String, Object> mapQuery = new HashMap<String, Object>();
            mapQuery.put("status", TuConstant.UNLOAD);
            mapQuery.put("orderBy", "createdAt");    //按照createAt排序
            mapQuery.put("orderType", "asc");    //按照createAt排序
            tuHeads = iTuRpcService.getTuHeadList(mapQuery);   //时间的降序
            //有待装车单子
            if (null != tuHeads && tuHeads.size() > 0) {
                headList.addAll(tuHeads);
            }
            for (int i = 0; i < headList.size(); i++) {
                Map<String, Object> one = new HashMap<String, Object>();
                one.put("number", i + 1);   //序号
                one.put("tu", headList.get(i).getTuId());   //tu号
                one.put("cellphone", headList.get(i).getCellphone()); //司机的电话号
                one.put("preBoard", headList.get(i).getPreBoard());   //预装板数
                one.put("carNumber", headList.get(i).getCarNumber());   //预装板数
                one.put("driverName", headList.get(i).getName());   //预装板数
                //门店信息
                //List<map<"code":,"name">>
                List<Map<String, Object>> storeList = storeService.analyStoresIds2Stores(headList.get(i).getStoreIds());
                one.put("stores", storeList);
                resultList.add(one);
            }
        }
        //已装车代发货
        if (TuConstant.LOAD_OVER.equals(status)) {
            Map<String, Object> mapQuery = new HashMap<String, Object>();
            mapQuery.put("status", TuConstant.LOAD_OVER);
            mapQuery.put("loadUid", loadUid);   //该人的装车记录
            mapQuery.put("orderBy", "loadedAt");    //按照createAt排序
            mapQuery.put("orderType", "asc");    //按照loadedAt排序
            tuHeads = iTuRpcService.getTuHeadList(mapQuery);
            //无tu单
            if (null == tuHeads || tuHeads.size() < 1) {
                return null;
            }
            for (int i = 0; i < tuHeads.size(); i++) {
                Map<String, Object> one = new HashMap<String, Object>();
                one.put("number", i + 1);   //序号
                one.put("tu", tuHeads.get(i).getTuId());
                one.put("cellphone", tuHeads.get(i).getCellphone()); //司机的电话号
                one.put("realBoard", tuHeads.get(i).getRealBoard());
                one.put("carNumber", tuHeads.get(i).getCarNumber());   //预装板数
                one.put("driverName", tuHeads.get(i).getName());   //预装板数
                //门店
                //List<map<"code":,"name">>
                List<Map<String, Object>> storeList = storeService.analyStoresIds2Stores(tuHeads.get(i).getStoreIds());
                one.put("stores", storeList);
                resultList.add(one);
            }
        }
        resultMap.put("result", resultList);
        return JsonUtils.SUCCESS(resultMap);
    }

    /**
     * 领取Tu单子,改变tu单状态为装车中,并列出当前尾货
     * 大店有尾货,小店没有尾货
     *
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("getTuJob")
    public String getTuJob() throws BizCheckedException {
        Map<String, Object> mapRequest = RequestUtils.getRequest();
        String tuId = mapRequest.get("tuId").toString();
        Long loadUid = Long.valueOf(RequestUtils.getHeader("uid").toString());
        TuHead tuHead = iTuRpcService.getHeadByTuId(tuId);
        tuHead.setLoadedAt(DateUtils.getCurrentSeconds());
        tuHead.setLoadUid(loadUid);
        iTuRpcService.changeTuHeadStatus(tuHead, TuConstant.IN_LOADING);    //改成装车中
        //门店信息
        List<Map<String, Object>> stores = storeService.analyStoresIds2Stores(tuHead.getStoreIds());
        //rf结果
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //大门店还是小门店
        if (TuConstant.SCALE_HYPERMARKET.equals(tuHead.getScale())) {   //大店尾货
            Map<Long, Map<String, Object>> storesRestMap = new HashMap<Long, Map<String, Object>>();
            //循环门店获取尾货信息(没合板,合板日期就是零)
            for (Map<String, Object> store : stores) {
                String storeNo = store.get("storeNo").toString();
                Map<Long, Map<String, Object>> storeMap = iMergeRpcService.getMergeDetailByStoreNo(storeNo);
                storesRestMap.putAll(storeMap);
            }
            List<Map<String, Object>> storeRestList = new ArrayList<Map<String, Object>>();
            //尾货板子的物理托盘码展示
            for (Long key : storesRestMap.keySet()) {
                Map<String, Object> boardMap = storesRestMap.get(key);
                boardMap.put("containerNum", boardMap.get("containerCount"));
                boardMap.put("boxNum", boardMap.get("packCount"));
                boardMap.put("turnoverBoxNum", boardMap.get("turnoverBoxCount"));
                boardMap.put("containerNum", boardMap.get("containerCount"));
                //这里显示的物理托盘码,一个板子上的随机一个托盘码
                Long boardId = Long.valueOf(boardMap.get("containerId").toString());
                Long randContainerId = Long.valueOf(boardMap.get("markContainerId").toString());

                //尾货
                if (Boolean.valueOf(boardMap.get("isRest").toString())) {   //需要合板
                    //合过板子的尾货,才叫尾货
                    Map<String, Object> mergeQuery = new HashMap<String, Object>();
                    mergeQuery.put("containerId", boardId);   //查合板, 合板就是板子id,没合板就是物理托盘码
                    List<WaveDetail> waveDetailList = waveService.getWaveDetailsByMergedContainerId(boardId);
                    if (null == waveDetailList || waveDetailList.size() < 1) {  //查不到,就是没合板  不用担心肯定有detail
                        continue;
                    }
                    //是否已经装车
                    boolean isLoaded = false;
                    TuDetail tuDetail = iTuRpcService.getDetailByBoardId(Long.valueOf(boardMap.get("containerId").toString()));
                    if (null != tuDetail) {
                        isLoaded = true;
                        continue;   //已装车尾货不显示
                    }
                    Long storeId = iStoreRpcService.getStoreIdByCode(boardMap.get("storeNo").toString());
                    boardMap.put("isLoaded", isLoaded);
                    boardMap.put("storeId", storeId);
                    //markContainerId是物理的通过它来看尾货的托盘
                    //结果插入redis
                    tuRedisService.insertTuContainerRedis(boardMap);
                    storeRestList.add(storesRestMap.get(key));
                }
            }
            resultMap.put("result", storeRestList);
        } else {
            //小店无尾货
        }

        boolean OpenSwith = TuConstant.RF_OPEN_REST.equals(tuHead.getRfSwitch()) ? true : false;
        resultMap.put("openSwith", OpenSwith);
        return JsonUtils.SUCCESS(resultMap);
    }


    /**
     * 扫托盘码,装板子,插入tu_detail
     * 有板子写板子,没板子写container
     * 大店传的是mergeContainerId
     *
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("OneSubmit")
    public String loadBoard() throws BizCheckedException {
        //获取参数
        Map<String, Object> request = RequestUtils.getRequest();
        String tuId = request.get("tuId").toString();
        TuHead tuHead = iTuRpcService.getHeadByTuId(tuId);
        //合板的托盘码
        Long mergedContainerId = Long.valueOf(request.get("containerId").toString());
        //直流的大店小店的校验区别,直流大店,没合板,不许发货
        if (TuConstant.SCALE_HYPERMARKET.equals(tuHead.getScale())) {//大店  把控传的是板子的托盘码
            //合过板子的尾货,才叫尾货
            Map<String, Object> mergeQuery = new HashMap<String, Object>();
            mergeQuery.put("containerId", mergedContainerId);   //大店传的是板子码
            mergeQuery.put("type", TaskConstant.TYPE_MERGE);
            mergeQuery.put("status", TaskConstant.Done);
            List<TaskInfo> mergeInfos = baseTaskService.getTaskInfoList(mergeQuery);
            if (null == mergeInfos || mergeInfos.size() < 1) {
                throw new BizCheckedException("2870037");   //未合板不能装车
            }
        } else {
            //QC+done+containerId 找到mergercontaierId
            Map<String, Object> qcMapQuery = new HashMap<String, Object>();
            qcMapQuery.put("containerId", mergedContainerId);   //小店,不合板
            qcMapQuery.put("type", TaskConstant.TYPE_QC);
            qcMapQuery.put("status", TaskConstant.Done);
            List<TaskInfo> qcInfos = baseTaskService.getTaskInfoList(qcMapQuery);
            if (null == qcInfos || qcInfos.size() < 1) {
                throw new BizCheckedException("2870034");
            }
        }
        //redis中取结果集
        Map<String, String> containerDetailMap = tuRedisService.getRedisTuContainerDetail(mergedContainerId);
        if (containerDetailMap == null || containerDetailMap.isEmpty()) {
            throw new BizCheckedException("2990036");
        }
        Long storeId = Long.valueOf(containerDetailMap.get("storeId").toString());
        Long uid = Long.valueOf(RequestUtils.getHeader("uid"));
        Boolean isLoaded = Boolean.valueOf(containerDetailMap.get("isLoaded").toString());
        Boolean isRest = Boolean.valueOf(containerDetailMap.get("isRest").toString());
        Boolean isExpensive = Boolean.valueOf(containerDetailMap.get("isExpensive").toString());
        if (isLoaded) { //已装车
            throw new BizCheckedException("2990031");
        }
        BigDecimal boxNum = new BigDecimal(containerDetailMap.get("boxNum").toString());
        Integer containerNum = Integer.valueOf(containerDetailMap.get("containerNum").toString());
        Long turnoverBoxNum = Long.valueOf(containerDetailMap.get("turnoverBoxNum").toString());
        //校验板子已扫过|是否是目标门店的
//        if (iTuRpcService.getDetailByBoardId(containerId) != null) {
//            throw new BizCheckedException("2990031");
//        }

//        //确定是组盘完成的才能装箱子
//        //QC+done+containerId 找到mergercontaierId
//        Map<String, Object> qcMapQuery = new HashMap<String, Object>();
//        qcMapQuery.put("containerId", containerId);
//        qcMapQuery.put("type", TaskConstant.TYPE_QC);
//        qcMapQuery.put("status", TaskConstant.Done);
//        List<TaskInfo> qcInfos = baseTaskService.getTaskInfoList(qcMapQuery);
//        if (null == qcInfos) {
//            throw new BizCheckedException("2870034");
//        }
//        List<WaveDetail> waveDetails = null;
//        Long mergedContainerId = qcInfos.get(0).getMergedContainerId();
//        if (mergedContainerId.equals(containerId)) { //没合板
//            mergedContainerId = containerId;
//            waveDetails = waveService.getAliveDetailsByContainerId(mergedContainerId);
//        } else {
//            waveDetails = waveService.getWaveDetailsByMergedContainerId(mergedContainerId);   //已经合板
//        }
//        //一个板上的是一个门店的
//        Long orderId = waveDetails.get(0).getOrderId();
//        ObdHeader obdHeader = iSoRpcService.getOutbSoHeaderDetailByOrderId(orderId);
//        if (null == obdHeader) {
//            throw new BizCheckedException("2870006");
//        }
//        String storeCode = obdHeader.getDeliveryCode();
//        Long storeId = storeService.getStoreIdByCode(storeCode).get(0).getStoreId();    //获取storeId
//        boolean isSameStrore = false;
//        for (BaseinfoStore store : stores) {
//            if (store.getStoreNo().equals(storeCode)) {  //相同门店
//                isSameStrore = true;
//                break;
//            }
//        }
//        if (false == isSameStrore) {
//            throw new BizCheckedException("2990032");
//        }
//        //插入detail  箱数聚类taskInfo的mergerContainerID
//        //托盘数、总箱数、周转箱数
//        //然后task_info中取所有托盘的加和?
//        Map<String, Object> taskQuery = new HashMap<String, Object>();
//        taskQuery.put("mergedContainerId", mergedContainerId);
//        taskQuery.put("type", TaskConstant.TYPE_QC);
//        taskQuery.put("status", TaskConstant.Done);
//        List<TaskInfo> taskInfos = baseTaskService.getTaskInfoList(taskQuery);
//        if (null == taskInfos) {
//            throw new BizCheckedException("2870034");
//        }
//        Integer containerNum = Integer.valueOf(String.valueOf(taskInfos.size()));   //一个托盘一个QC组盘任务
//        BigDecimal boxNum = new BigDecimal("0.00");
//        Long turnoverBoxNum = new Long("0");
//        for (TaskInfo taskinfo : taskInfos) {
//            BigDecimal one = new BigDecimal(taskinfo.getExt4());
//            turnoverBoxNum += taskinfo.getExt3();    //周转箱
//            boxNum = boxNum.add(one);   //总箱子
//        }

        TuDetail tuDetail = new TuDetail();
        tuDetail.setTuId(tuId);
        tuDetail.setMergedContainerId(mergedContainerId);
        tuDetail.setBoxNum(boxNum);
        tuDetail.setContainerNum(containerNum);
        tuDetail.setTurnoverBoxNum(turnoverBoxNum);
        tuDetail.setStoreId(storeId);
        tuDetail.setLoadAt(DateUtils.getCurrentSeconds());
        tuDetail.setIsValid(1);
        // todo 贵品还是余货插入
        tuDetail.setIsRest(isExpensive ? 1 : 0);    //贵品为1
        tuDetail.setIsExpresive(isRest ? 1 : 0);    //余货为1
        iTuRpcService.create(tuDetail);

        return JsonUtils.SUCCESS(new HashMap<String, Boolean>() {
            {
                put("response", true);
            }
        });
    }

    /**
     * 显示板子上总箱数、周转箱数、状态:待装车|已装车
     *
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("showContainer")
    public String showBoardDetail() throws BizCheckedException {
        //获取参数
        Map<String, Object> request = RequestUtils.getRequest();
        //获取tu单号查找还能装多少
        String tuId = request.get("tuId").toString();
        TuHead tuHead = iTuRpcService.getHeadByTuId(tuId);
        //合板的托盘码
        Long containerId = Long.valueOf(request.get("containerId").toString());
        if (null == tuHead) {
            throw new BizCheckedException("2990022");
        }
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
        } else { //大店看合板
            Map<String, Object> mergeQuery = new HashMap<String, Object>();
            List<WaveDetail> waveDetailList = waveService.getAliveDetailsByContainerId(containerId);    //物理托盘码
            if (null == waveDetailList || waveDetailList.size() < 1) {
                throw new BizCheckedException("2990039");
            }
            if (waveDetailList.get(0).getMergedContainerId().equals(0L)) {   //没合板
                throw new BizCheckedException("2990037");
            }
            mergedContainerId = waveDetailList.get(0).getMergedContainerId();   //合板取一个托盘合板后的id
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
        BigDecimal boxNum = new BigDecimal("0.00");
        Long turnoverBoxNum = new Long("0");
        Set<Long> containerSet = new HashSet<Long>();
        for (TaskInfo taskinfo : taskInfos) {
            BigDecimal one = new BigDecimal(taskinfo.getExt4());
            turnoverBoxNum += taskinfo.getExt3();    //周转箱
            boxNum = boxNum.add(one);   //总箱子
            containerSet.add(taskinfo.getMergedContainerId());
        }
        //结果集
        Integer containerNum = containerSet.size(); //以板子为维度
        Map<String, Object> result = new HashMap<String, Object>();
        //预估剩余板数,预装-已装
        Long preBoards = tuHead.getPreBoard();
        Long preRestBoard = null;   //预估剩余可装板子数
        List<TuDetail> tuDetails = iTuRpcService.getTuDeailListByTuId(tuId);
        if (null == tuDetails || tuDetails.size() < 1) {  //一个板子都没装
            preRestBoard = preBoards;
        }
        preRestBoard = preBoards - tuDetails.size();
        result.put("preRestBoard", preRestBoard);    //预估剩余板数
        result.put("containerNum", containerNum);
        result.put("boxNum", boxNum);
        result.put("turnoverBoxNum", turnoverBoxNum);
        //是否已装车
        boolean isLoaded = false;
        TuDetail tuDetail = iTuRpcService.getDetailByBoardId(mergedContainerId);
        if (null != tuDetail) {
            isLoaded = true;
        }
        result.put("storeId", storeId);
        result.put("isLoaded", isLoaded);
        result.put("containerId", mergedContainerId);
        result.put("isRest", false); //非余货
        result.put("isExpensive", false);    //非贵品
        //结果放在缓存中
        tuRedisService.insertTuContainerRedis(result);
        return JsonUtils.SUCCESS(result);
    }

    /**
     * 确认装车
     * 传tuId号
     * 设置实际装车板数和装车完毕时间
     *
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("confirm")
    public String confirmLoad() throws BizCheckedException {
        Map<String, Object> mapRequest = RequestUtils.getRequest();
        String tuId = mapRequest.get("tuId").toString();
        //先查在更改状态
        TuHead tuHead = iTuRpcService.getHeadByTuId(tuId);
        if (null == tuHead) {
            throw new BizCheckedException("2990022");
        }
        //只有是装车中的tu才能装车完毕,其他状态不能直接流转
        if (!TuConstant.IN_LOADING.equals(tuHead.getStatus())) {
            throw new BizCheckedException("2990035");
        }
        tuHead.setStatus(TuConstant.LOAD_OVER);
        //统计装车板树real_board 确认装车完毕时间
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("tuId", tuId);
        List<TuDetail> tuDetails = iTuRpcService.getTuDeailList(mapQuery);
        //装车板数
        if (null == tuDetails || tuDetails.size() < 1) {    //什么也没装
            throw new BizCheckedException("2990033");
        }
        Long realBoardNum = Long.valueOf(String.valueOf(tuDetails.size()));
        tuHead.setRealBoard(realBoardNum);
        iTuRpcService.update(tuHead);

        return JsonUtils.SUCCESS(new HashMap<String, Boolean>() {
            {
                put("response", true);
            }
        });
    }

    /**
     * 继续装车,装车的状态变更
     *
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("reload")
    public String reloadByTuId() throws BizCheckedException {
        Map<String, Object> mapRequest = RequestUtils.getRequest();
        String tuId = mapRequest.get("tuId").toString();
        TuHead tuHead = iTuRpcService.getHeadByTuId(tuId);
        if (null == tuHead) {
            throw new BizCheckedException("2990022");
        }
        //发货的不能继续装车
        if (TuConstant.SHIP_OVER.equals(tuHead.getStatus())) {
            throw new BizCheckedException("2990035");
        }
        tuHead.setStatus(TuConstant.IN_LOADING);
        iTuRpcService.update(tuHead);
        return JsonUtils.SUCCESS(new HashMap<String, Boolean>() {
            {
                put("chooseDone", true);
            }
        });
    }


}
