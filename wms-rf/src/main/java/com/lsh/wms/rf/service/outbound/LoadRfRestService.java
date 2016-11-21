package com.lsh.wms.rf.service.outbound;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.api.service.merge.IMergeRpcService;
import com.lsh.wms.api.service.pick.IQCRpcService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.so.ISoRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.api.service.tu.ILoadRfRestService;
import com.lsh.wms.api.service.tu.ITuRpcService;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.constant.TuConstant;
import com.lsh.wms.core.service.csi.CsiCustomerService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.core.service.wave.WaveService;
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
    private IQCRpcService iqcRpcService;
    @Autowired
    private CsiCustomerService csiCustomerService;

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
        if (!TuConstant.UNLOAD.equals(status)
                && !TuConstant.LOAD_OVER.equals(status)
                && !TuConstant.IN_LOADING.equals(status)) {
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
            tuMapQuery.put("type", TuConstant.TYPE_STORE);
            tuMapQuery.put("status", TuConstant.IN_LOADING);
            List<TuHead> doingHeads = iTuRpcService.getTuHeadList(tuMapQuery);
            if (null != doingHeads && doingHeads.size() > 0) {
                headList.addAll(doingHeads);
            }
        }
            //待装车的
            Map<String, Object> mapQuery = new HashMap<String, Object>();
            mapQuery.put("status", status);
            mapQuery.put("type", TuConstant.TYPE_STORE);
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
                List<Map<String, Object>> storeList = csiCustomerService.ParseCustomerIds2Customers(headList.get(i).getStoreIds());
                one.put("stores", storeList);
                resultList.add(one);
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
        List<Map<String, Object>> stores = csiCustomerService.ParseCustomerIds2Customers(tuHead.getStoreIds()); //少用map不易解读
        //rf结果
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //大门店还是小门店
        if (TuConstant.SCALE_HYPERMARKET.equals(tuHead.getScale())) {   //大店尾货
            Map<Long, Map<String, Object>> storesRestMap = new HashMap<Long, Map<String, Object>>();
            //循环门店获取尾货信息(没合板,合板日期就是零)  todo 未合板的贵品也可能是尾货
            for (Map<String, Object> store : stores) {
                String storeNo = store.get("customerCode").toString();
                Map<Long, Map<String, Object>> storeMap = iMergeRpcService.getMergeDetailByCustomerCode(storeNo);
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
                    //合板未装车的才叫尾货
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
                    boardMap.put("isLoaded", isLoaded);

                    //获取一板多托(这里一板多托盘的需要都是合板以后的)
                    Map<String, Object> mergerMap = new HashMap<String, Object>();
                    mergerMap.put("containerId", boardId);
                    mergerMap.put("status", TaskConstant.Done);
                    mergerMap.put("type", TaskConstant.TYPE_MERGE);
                    List<TaskInfo> mergeInfos = baseTaskService.getTaskInfoList(mergerMap);
                    if (null == mergeInfos || mergeInfos.size() < 1) {
                        throw new BizCheckedException("2120020");
                    }
                    BigDecimal taskBoardQty = mergeInfos.get(0).getTaskBoardQty();
                    Long boardNum = taskBoardQty.longValue();
                    boardMap.put("taskBoardQty", boardNum);
                    //markContainerId是物理的通过它来看尾货的托盘
                    storeRestList.add(storesRestMap.get(key));
                }
            }
            resultMap.put("result", storeRestList);
        } else {
            //小店组盘未装车的叫尾货   markContainer和container相同
            //循环门店获取尾货信息(没合板,合板日期就是零)
            Map<Long, Map<String, Object>> storesRestMap = new HashMap<Long, Map<String, Object>>();
            for (Map<String, Object> store : stores) {
                String storeNo = store.get("customerCode").toString();
                Map<Long, Map<String, Object>> storeMap = iqcRpcService.getGroupDetailByStoreNo(storeNo);
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
                //这里显示的物理托盘码
                Long containerId = Long.valueOf(boardMap.get("containerId").toString());
                Long randContainerId = Long.valueOf(boardMap.get("markContainerId").toString());
                //尾货
                if (Boolean.valueOf(boardMap.get("isRest").toString())) {   //需要合板
                    //合板未装车的才叫尾货
                    Map<String, Object> mergeQuery = new HashMap<String, Object>();
                    mergeQuery.put("containerId", containerId);   //组盘后物理托盘码
                    List<WaveDetail> waveDetailList = waveService.getAliveDetailsByContainerId(containerId);
                    if (null == waveDetailList || waveDetailList.size() < 1) {
                        continue;
                    }
                    //是否已经装车
                    boolean isLoaded = false;
                    TuDetail tuDetail = iTuRpcService.getDetailByBoardId(Long.valueOf(boardMap.get("containerId").toString()));
                    if (null != tuDetail) {
                        isLoaded = true;
                        continue;   //已装车尾货不显示
                    }
                    boardMap.put("isLoaded", isLoaded);
                    //markContainerId是物理的通过它来看尾货的托盘
                    storeRestList.add(storesRestMap.get(key));
                }
            }
        }

        boolean OpenSwith = TuConstant.RF_OPEN_STATE.equals(tuHead.getRfSwitch()) ? true : false;
        resultMap.put("openSwitch", OpenSwith);
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
        //合板的托盘码或者托盘码(校验用的)
        Long mergedContainerId = Long.valueOf(request.get("containerId").toString());
        //物理托盘码(扫码的)
        Long realContainerId = Long.valueOf(request.get("realContainerId").toString());
        //直流的大店小店的校验区别,直流大店,没合板,不许发货
        if (TuConstant.SCALE_HYPERMARKET.equals(tuHead.getScale())) {//大店可能是托盘码或者板子码
            //大店传板子或者托盘码
            //QC+done+containerId 未合板子,taskinfo中板子码和托盘码相同,能查到就是组盘了
            Map<String, Object> qcMapQuery = new HashMap<String, Object>();
            qcMapQuery.put("containerId", realContainerId);
            qcMapQuery.put("type", TaskConstant.TYPE_QC);
            qcMapQuery.put("status", TaskConstant.Done);
            List<TaskInfo> qcInfos = baseTaskService.getTaskInfoList(qcMapQuery);
            if (null == qcInfos || qcInfos.size() < 1) {
                throw new BizCheckedException("2870034");
            }
        } else {
            //QC+done+containerId 找到mergercontaierId
            Map<String, Object> qcMapQuery = new HashMap<String, Object>();
            qcMapQuery.put("containerId", realContainerId);   //小店,不合板
            qcMapQuery.put("type", TaskConstant.TYPE_QC);
            qcMapQuery.put("status", TaskConstant.Done);
            List<TaskInfo> qcInfos = baseTaskService.getTaskInfoList(qcMapQuery);
            if (null == qcInfos || qcInfos.size() < 1) {
                throw new BizCheckedException("2870034");
            }
        }
        //传物理托盘码
        Map<String, Object> containerDetailMap = iTuRpcService.getBoardDetailBycontainerId(realContainerId, tuId);
        if (containerDetailMap == null || containerDetailMap.isEmpty()) {
            throw new BizCheckedException("2990036");
        }
        Long storeId = Long.valueOf(containerDetailMap.get("customerId").toString());
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
        BigDecimal taskBoardQty = new BigDecimal(containerDetailMap.get("taskBoardQty").toString());
        Long boardNum = taskBoardQty.longValue();
        TuDetail tuDetail = new TuDetail();
        tuDetail.setTuId(tuId);
        tuDetail.setMergedContainerId(Long.valueOf(containerDetailMap.get("containerId").toString()));
        tuDetail.setBoxNum(boxNum);
        tuDetail.setContainerNum(containerNum);
        tuDetail.setTurnoverBoxNum(turnoverBoxNum);
        tuDetail.setBoardNum(boardNum); //一板多托数量 todo 贵品不算板数
        tuDetail.setStoreId(storeId);
        tuDetail.setLoadAt(DateUtils.getCurrentSeconds());
        tuDetail.setIsValid(1);
        // todo 贵品还是余货插入
        tuDetail.setIsRest(isExpensive ? 1 : 0);    //贵品为1
        tuDetail.setIsExpensive(isRest ? 1 : 0);    //余货为1
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
        //物理托盘码
        Long containerId = Long.valueOf(request.get("containerId").toString());
        if (null == tuHead) {
            throw new BizCheckedException("2990022");
        }
        Map<String, Object> result = iTuRpcService.getBoardDetailBycontainerId(containerId, tuId);
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
        //实际装车板数
        Long realBoardNum = 0L;
        for (TuDetail tuDetail : tuDetails) {
            realBoardNum += tuDetail.getBoardNum();
        }
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
