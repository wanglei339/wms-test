package com.lsh.wms.rf.service.outbound;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.so.ISoRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.api.service.tu.ILoadRfRestService;
import com.lsh.wms.api.service.tu.ITuRpcService;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.constant.TuConstant;
import com.lsh.wms.core.service.store.StoreService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.core.service.task.TaskHandler;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        //根据传入要的tu单的状态,显示不同list
        if (null == status) {
            throw new BizCheckedException("2990028");
        }
        if (!TuConstant.UNLOAD.equals(status) && !TuConstant.LOAD_OVER.equals(status)) {
            throw new BizCheckedException("2990029");
        }
        List<TuHead> tuHeads = null;
        //结果集封装 序号,运单号,tu,装车数;//
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        //待装车
        if (TuConstant.UNLOAD.equals(status)) {
            Map<String, Object> mapQuery = new HashMap<String, Object>();
            mapQuery.put("status", TuConstant.UNLOAD);
            mapQuery.put("orderBy", "createdAt");    //按照createAt排序
            mapQuery.put("orderType", "asc");    //按照createAt排序
            tuHeads = iTuRpcService.getTuHeadList(mapQuery);   //时间的降序
            //无tu单
            if (null == tuHeads || tuHeads.size() < 1) {
                return "";
            }
            for (int i = 0; i < tuHeads.size(); i++) {
                Map<String, Object> one = new HashMap<String, Object>();
                one.put("number", i + 1);   //序号
                one.put("tu", tuHeads.get(i).getTuId());   //tu号
                one.put("preBoard", tuHeads.get(i).getPreBoard());   //预装板数
                one.put("carNumber", tuHeads.get(i).getCarNumber());   //预装板数
                one.put("driverName", tuHeads.get(i).getName());   //预装板数
                //门店
                String[] storeIdsStr = tuHeads.get(i).getStoreIds().split("\\|"); //门店id以|分割
                //List<map<"code":,"name">>
                List<Map<String, Object>> storeList = new ArrayList<Map<String, Object>>();
                for (String storeIdStr : storeIdsStr) {
                    Long storeId = Long.valueOf(storeIdStr);
                    BaseinfoStore store = storeService.getStoreByStoreId(storeId);
                    if (null == store) {
                        throw new BizCheckedException("2180018");
                    }
                    Map<String, Object> storeMap = new HashMap<String, Object>();
                    storeMap.put("storeNo", store.getStoreNo());
                    storeMap.put("storeName", store.getStoreName());
                    storeMap.put("storeId", store.getStoreId());
                    storeList.add(storeMap);
                }
                one.put("stores", storeList);
                resultList.add(one);
            }
        }
        //已装车代发货
        if (TuConstant.LOAD_OVER.equals(status)) {
            Map<String, Object> mapQuery = new HashMap<String, Object>();
            mapQuery.put("status", TuConstant.LOAD_OVER);
            mapQuery.put("orderBy", "loadedAt");    //按照createAt排序
            mapQuery.put("orderType", "asc");    //按照loadedAt排序
            tuHeads = iTuRpcService.getTuHeadList(mapQuery);
            //无tu单
            if (null == tuHeads || tuHeads.size() < 1) {
                return "";
            }
            for (int i = 0; i < tuHeads.size(); i++) {
                Map<String, Object> one = new HashMap<String, Object>();
                one.put("number", i + 1);   //序号
                one.put("tu", tuHeads.get(i).getTuId());
                one.put("realBoard", tuHeads.get(i).getRealBoard());
                one.put("carNumber", tuHeads.get(i).getCarNumber());   //预装板数
                one.put("driverName", tuHeads.get(i).getName());   //预装板数
                //门店
                String[] storeIdsStr = tuHeads.get(i).getStoreIds().split("\\|"); //门店id以|分割
                //List<map<"code":,"name">>
                List<Map<String, Object>> storeList = new ArrayList<Map<String, Object>>();
                for (String storeIdStr : storeIdsStr) {
                    Long storeId = Long.valueOf(storeIdStr);
                    BaseinfoStore store = storeService.getStoreByStoreId(storeId);
                    if (null == store) {
                        throw new BizCheckedException("2180018");
                    }
                    Map<String, Object> storeMap = new HashMap<String, Object>();
                    storeMap.put("storeNo", store.getStoreNo());
                    storeMap.put("storeName", store.getStoreName());
                    storeMap.put("storeId", store.getStoreId());
                    storeList.add(storeMap);
                }
                one.put("stores", storeList);
                resultList.add(one);
            }
        }
        return JsonUtils.SUCCESS(resultList);
    }

    /**
     * 下一步,tuhead的状态变更,status变为发车中。。。
     *
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("choseLoad")
    public String changeLoadStatus() throws BizCheckedException {
        Map<String, Object> mapRequest = RequestUtils.getRequest();
        String tuId = mapRequest.get("tuId").toString();
        //更新任务的状态,让其他的人无法领该待装车记录
        if (null == tuId || tuId.equals("")) {
            throw new BizCheckedException("2990021");
        }
        TuHead tuHead = iTuRpcService.getHeadByTuId(tuId);
        if (null == tuHead) {
            throw new BizCheckedException("2990022");
        }
        tuHead.setStatus(TuConstant.IN_LOADING);
        iTuRpcService.update(tuHead);
        return JsonUtils.SUCCESS(new HashMap<String, Boolean>() {
            {
                put("chooseDone", true);
            }
        });
    }

//    /**
//     * 获取tu单,查找门店信息
//     *
//     * @return
//     * @throws BizCheckedException
//     */
//    @POST
//    @Path("getTuList")
//    public String getTuHead() throws BizCheckedException {
//        Map<String, Object> mapRequest = RequestUtils.getRequest();
//        String tuId = mapRequest.get("tuId").toString();
//        TuHead tuHead = iTuRpcService.getHeadByTuId(tuId);
//        if (null == tuHead) {
//            throw new BizCheckedException("2990022");
//        }
//        //门店
//        String[] storeIdsStr = tuHead.getStoreIds().split("\\|"); //门店id以|分割
//        //List<map<"code":,"name">>
//        List<Map<String, Object>> storeList = new ArrayList<Map<String, Object>>();
//        for (String storeIdStr : storeIdsStr) {
//            Long storeId = Long.valueOf(storeIdStr);
//            BaseinfoStore store = storeService.getStoreByStoreId(storeId);
//            if (null == store) {
//                throw new BizCheckedException("2180018");
//            }
//            Map<String, Object> storeMap = new HashMap<String, Object>();
//            storeMap.put("storeCode", store.getStoreNo());
//            storeMap.put("storeName", store.getStoreName());
//            //状态修改装车任务已经被领取?
//
//        }
//        return null;
//    }


    /**
     * 扫托盘码,装板子,插入tu_detail
     * 有板子写板子,没板子写container
     *
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("scan")
    public String loadBoard() throws BizCheckedException {
        //获取参数
        Map<String, Object> request = RequestUtils.getRequest();
        //合板的托盘码
        Long mergedContainerId = Long.valueOf(request.get("containerId").toString());
        String tuId = request.get("tuId").toString();
        //获取门店信息
        List<BaseinfoStore> stores = JSON.parseArray((String) request.get("stores"), BaseinfoStore.class);
        //获取扫码人
        Long uid = Long.valueOf(RequestUtils.getHeader("uid"));
        TuHead tuHead = iTuRpcService.getHeadByTuId(tuId);
        //校验板子已扫过|是否是目标门店的
        if (iTuRpcService.getDetailByBoardId(mergedContainerId) != null) {
            throw new BizCheckedException("2990031");
        }
        // 没合过板子的是否是目标门店的,合过板子的是否是目标门店的  detail查orderid,找目标用户
        List<WaveDetail> waveDetails = waveService.getWaveDetailsByMergedContainerId(mergedContainerId);   //已经合板
        if (null == waveDetails || waveDetails.size() < 1) {    //没合板
            waveDetails = waveService.getAliveDetailsByContainerId(mergedContainerId);
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
        for (BaseinfoStore store : stores) {
            if (store.getStoreNo().equals(storeCode)) {  //相同门店
                isSameStrore = true;
                break;
            }
        }
        if (false == isSameStrore) {
            throw new BizCheckedException("2990032");
        }
        //插入detail  箱数聚类taskInfo的mergerContainerID
        //托盘数、总箱数、周转箱数
        //然后task_info中取所有托盘的加和?
        Map<String, Object> taskQuery = new HashMap<String, Object>();
        taskQuery.put("mergedContainerId", mergedContainerId);
        taskQuery.put("type", TaskConstant.TYPE_QC);
        taskQuery.put("status", TaskConstant.Done);
        List<TaskInfo> taskInfos = baseTaskService.getTaskInfoList(taskQuery);
        if (null == taskInfos) {
            throw new BizCheckedException("2870034");
        }
        Integer containerNum = Integer.valueOf(String.valueOf(taskInfos.size()));   //一个托盘一个QC组盘任务
        BigDecimal boxNum = new BigDecimal("0.00");
        Long turnoverBoxNum = new Long("0");
        for (TaskInfo taskinfo : taskInfos) {
            BigDecimal one = new BigDecimal(taskinfo.getExt4());
            turnoverBoxNum += taskinfo.getExt3();    //周转箱
            boxNum = boxNum.add(one);   //总箱子
        }

        TuDetail tuDetail = new TuDetail();
        tuDetail.setTuId(tuId);
        tuDetail.setMergedContainerId(mergedContainerId);
        tuDetail.setBoxNum(boxNum);
        tuDetail.setContainerNum(containerNum);
        tuDetail.setTurnoverBoxNum(turnoverBoxNum);
        tuDetail.setStoreId(storeId);
        tuDetail.setLoadAt(DateUtils.getCurrentSeconds());
        tuDetail.setLoadUid(uid);
        tuDetail.setIsValid(1);
        // todo 贵品还是余货插入
        tuDetail.setIsRest(0);
        tuDetail.setIsExpresive(0);
        iTuRpcService.create(tuDetail);
        return JsonUtils.SUCCESS(new HashMap<String, Boolean>() {
            {
                put("response", true);
            }
        });
    }

    /**
     * 确认装车
     * 传tuId号
     *  设置实际装车板数和装车完毕时间
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
        tuHead.setStatus(TuConstant.LOAD_OVER);
        //统计装车板树real_board 确认装车完毕时间
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("tuId", tuId);
        List<TuDetail> tuDetails = iTuRpcService.getTuDeailList(mapQuery);
        //装车板数
        if (null == tuDetails) {
            throw new BizCheckedException("2990033");
        }
        Long realBoardNum = Long.valueOf(String.valueOf(tuDetails.size()));
        tuHead.setRealBoard(realBoardNum);
        tuHead.setLoadedAt(DateUtils.getCurrentSeconds());
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
        tuHead.setStatus(TuConstant.IN_LOADING);
        iTuRpcService.update(tuHead);
        return JsonUtils.SUCCESS(new HashMap<String, Boolean>() {
            {
                put("chooseDone", true);
            }
        });
    }


}
