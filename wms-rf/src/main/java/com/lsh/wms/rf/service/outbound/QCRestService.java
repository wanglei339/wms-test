package com.lsh.wms.rf.service.outbound;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.api.service.container.IContainerRpcService;
import com.lsh.wms.api.service.csi.ICsiRpcService;
import com.lsh.wms.api.service.item.IItemRpcService;
import com.lsh.wms.api.service.location.ILocationRpcService;
import com.lsh.wms.api.service.pick.IRFQCRestService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.so.ISoRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.CsiConstan;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.baseinfo.BaseinfoContainer;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.csi.CsiSku;
import com.lsh.wms.model.so.OutbSoDetail;
import com.lsh.wms.model.so.OutbSoHeader;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.wave.WaveDetail;
import com.lsh.wms.model.wave.WaveQcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.Task;

import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by zengwenjun on 16/7/30.
 */


@Service(protocol = "rest")
@Path("outbound/qc")
@Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class QCRestService implements IRFQCRestService{
    private static Logger logger = LoggerFactory.getLogger(QCRestService.class);
    @Reference
    private ICsiRpcService csiRpcService;
    @Autowired
    private WaveService waveService;
    @Reference
    private IItemRpcService itemRpcService;
    @Reference
    private ITaskRpcService iTaskRpcService;
    @Reference
    private IContainerRpcService iContainerRpcService;
    @Reference
    private ISoRpcService iSoRpcService;
    @Reference
    private ILocationRpcService iLocationRpcService;

    /**
     * 扫码获取qc任务详情
     * 输入捡货签或者托盘嘛,捡货签优先,托盘码其次
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("scan")
    public String scan() throws BizCheckedException{
        Map<String, Object> mapRequest = RequestUtils.getRequest();
        Long pickTaskId = 0L;
        TaskInfo pickTaskInfo = null;
        Long containerId = 0L;
        TaskInfo qcTaskInfo = null;
        //参数获取和初始化
        if(mapRequest.get("pickTaskId") != null && mapRequest.get("pickTaskId").toString().compareTo("") != 0 ){
            //根据捡货签做初始化
            pickTaskId = Long.valueOf(mapRequest.get("pickTaskId").toString());
            pickTaskInfo = iTaskRpcService.getTaskInfo(pickTaskId);
            if(pickTaskInfo == null){
                throw new BizCheckedException("2060003");
            }
            containerId = pickTaskInfo.getContainerId();
        }else{
            //根据托盘码做初始化
            containerId = Long.valueOf(mapRequest.get("containerId").toString());
            Map<String, Object> mapQuery = new HashMap<String, Object>();
            mapQuery.put("containerId", containerId);
            List<TaskEntry> tasks = iTaskRpcService.getTaskHeadList(TaskConstant.TYPE_PICK, mapQuery);
            if(tasks.size() == 0) {
                throw new BizCheckedException("2060003");
            }else if (tasks.size() > 1){
                //捡货任务冲突
                throw new BizCheckedException("2120012");
            }
            pickTaskInfo = tasks.get(0).getTaskInfo();
            pickTaskId = pickTaskInfo.getTaskId();
        }
        //获取QC任务
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("containerId", containerId);
        List<TaskEntry> tasks = iTaskRpcService.getTaskHeadList(TaskConstant.TYPE_QC, mapQuery);
        if(tasks.size()>1){
            throw new BizCheckedException("2120006");
        }
        if(tasks.size()==0){
            throw new BizCheckedException("2120007");
        }
        qcTaskInfo = tasks.get(0).getTaskInfo();
        if(qcTaskInfo.getStatus() == TaskConstant.Draft){
            iTaskRpcService.assign(qcTaskInfo.getTaskId(), Long.valueOf(RequestUtils.getHeader("uid")));
        }
        List<WaveDetail> details = waveService.getDetailsByContainerId(containerId);
        if(details.size() == 0){
            //空托盘
            throw new BizCheckedException("2120005");
        }
        //merge item_id 2 pick  qty
        Map<Long, BigDecimal> mapItem2PickQty = new HashMap<Long, BigDecimal>();
        Map<Long, Long> mapItem2QCStatus = new HashMap<Long, Long>();
        for( WaveDetail d : details){
            if(mapItem2PickQty.get(d.getItemId())==null){
                mapItem2PickQty.put(d.getItemId(), new BigDecimal(d.getPickQty().toString()));
            }else{
                mapItem2PickQty.put(d.getItemId(), mapItem2PickQty.get(d.getItemId()).add(d.getPickQty()));
            }
            mapItem2QCStatus.put(d.getItemId(), d.getQcExceptionDone());
        }

        List<Map<String, Object>> undoDetails = new LinkedList<Map<String, Object>>();
        for(Long itemId : mapItem2PickQty.keySet()) {
            Map<String, Object> detail = new HashMap<String, Object>();
            BaseinfoItem item = itemRpcService.getItem(itemId);
            detail.put("skuId", item.getSkuId());
            detail.put("itemId", item.getItemId());
            detail.put("code", item.getCode());
            detail.put("codeType", item.getCodeType());
            detail.put("pickQty", mapItem2PickQty.get(itemId));
            detail.put("packName", "EA");
            //TODO packName
            detail.put("itemName", item.getSkuName());
            detail.put("qcDone", mapItem2PickQty.get(itemId).intValue()==0 ? 0 : 1);
            undoDetails.add(detail);
        }
        //获取托盘信息
        BaseinfoContainer containerInfo = iContainerRpcService.getContainer(containerId);
        if(containerInfo==null){
            throw new BizCheckedException("托盘码不存在");
        }
        //获取客户信息
        OutbSoHeader soInfo = iSoRpcService.getOutbSoHeaderDetailByOrderId(details.get(0).getOrderId());
        //获取集货道信息
        BaseinfoLocation collectLocaion = iLocationRpcService.getLocation(details.get(0).getAllocCollectLocation());
        Map<String, Object> rstMap = new HashMap<String, Object>();
        rstMap.put("qcList", undoDetails);
        rstMap.put("containerType", containerInfo.getType());
        rstMap.put("pickTaskId", pickTaskId);
        rstMap.put("soUserId", soInfo.getOrderUser());
        //TODO SO USER ID
        rstMap.put("soUserName", soInfo.getOrderUser());
        rstMap.put("collectionRoadCode", collectLocaion.getLocationCode());
        rstMap.put("itemLineNum", mapItem2PickQty.size());
        //TODO BOX NUM
        rstMap.put("itemBoxNum", mapItem2PickQty.size());
        rstMap.put("taskStatus", qcTaskInfo.getStatus());
        rstMap.put("qcTaskId", qcTaskInfo.getTaskId());
        return JsonUtils.SUCCESS(rstMap);
    }


    @POST
    @Path("qcOneItem")
    public String qcOneItem() throws BizCheckedException{
        //获取参数
        Map<String,Object> request = RequestUtils.getRequest();
        long qcTaskId =  Long.valueOf(request.get("qcTaskId").toString());
        BigDecimal qty = new BigDecimal(request.get("qty").toString());
        long exceptionType = request.get("exceptionType")==null ? 0L : Long.valueOf(request.get("exceptionType").toString());
        BigDecimal exceptionQty = request.get("exceptionQty")==null ? BigDecimal.ZERO : new BigDecimal(request.get("exceptionQty").toString());
        //初始化QC任务
        TaskInfo qcTaskInfo = iTaskRpcService.getTaskInfo(qcTaskId);
        if(qcTaskInfo == null){
            throw new BizCheckedException("2120007");
        }
        //转换商品条形码为sku码
        String code = (String) request.get("code");
        CsiSku skuInfo = csiRpcService.getSkuByCode(CsiConstan.CSI_CODE_TYPE_BARCODE, code);
        if(skuInfo == null){
            throw new BizCheckedException("2120001");
        }
        long skuId = skuInfo.getSkuId();
        List<WaveDetail> details = waveService.getDetailsByContainerId(qcTaskInfo.getContainerId());
        int seekNum = 0;
        List<WaveDetail> matchDetails = new LinkedList<WaveDetail>();
        BigDecimal pickQty = new BigDecimal("0.0000");
        for (WaveDetail d : details) {
            if (d.getSkuId() != skuId) {
                continue;
            }
            seekNum++;
            matchDetails.add(d);
            pickQty = pickQty.add(d.getPickQty());
        }
        if (seekNum == 0) {
            if (exceptionType != 3) {
                throw new BizCheckedException("2120009");
            }
            WaveQcException qcException = new WaveQcException();
            qcException.setSkuId(skuInfo.getSkuId());
            qcException.setExceptionQty(exceptionQty);
            qcException.setExceptionType(exceptionType);
            qcException.setQcTaskId(qcTaskId);
            qcException.setWaveId(qcTaskInfo.getWaveId());
            waveService.insertQCException(qcException);
        }
        int cmpRet = pickQty.compareTo(qty);
        if (cmpRet > 0) exceptionType = 2; //多货
        if (cmpRet < 0) exceptionType = 1; //少货
        BigDecimal curQty = new BigDecimal("0.0000");
        for(int i = 0; i < matchDetails.size(); ++i){
            WaveDetail detail = matchDetails.get(i);
            BigDecimal lastQty = curQty;
            curQty = curQty.add(detail.getPickQty());
            detail.setQcQty(qty);
            detail.setQcAt(DateUtils.getCurrentSeconds());
            detail.setQcUid(Long.valueOf(RequestUtils.getHeader("uid")));
            detail.setQcException(exceptionType);
            detail.setQcAt(DateUtils.getCurrentSeconds());
            detail.setQcUid(Long.valueOf(RequestUtils.getHeader("uid")));
            if (exceptionType != 0) {
                //多货
                if (i == matchDetails.size() - 1) {
                    detail.setQcException(exceptionType);
                    detail.setQcExceptionQty(qty.subtract(curQty));
                    detail.setQcExceptionDone(0L);
                    detail.setQcQty(qty.subtract(lastQty));

                } else {
                    //忽略
                    detail.setQcQty(detail.getPickQty());
                    detail.setQcException(0L);
                    detail.setQcExceptionQty(BigDecimal.ZERO);
                    detail.setQcExceptionDone(1L);
                    detail.setQcQty(detail.getPickQty());
                }
            } else {
                detail.setQcQty(detail.getPickQty());
                detail.setQcException(0L);
                detail.setQcExceptionQty(BigDecimal.ZERO);
                detail.setQcExceptionDone(1L);
            }
            waveService.updateDetail(detail);
        }
        //校验qc任务是否完全完成;
        boolean bSucc = true;
        for(WaveDetail d : details){
            if(d.getQcExceptionDone() == 0){
                bSucc = false;
                break;
            }
        }
        if(bSucc){
            //成功
            iTaskRpcService.done(qcTaskId, qcTaskInfo.getLocationId(), Long.valueOf(RequestUtils.getHeader("uid")));
        }
        //返回结果
        Map<String, Object> rstMap = new HashMap<String, Object>();
        rstMap.put("qcDone", bSucc ? 1 : 0);
        return JsonUtils.SUCCESS(rstMap);
        //这里其实遗留了一个问题,就是到底出去的是一些什么样的商品物理信息,比如是一箱还是一堆ea
        //TODO OUTBOUND ITEM INFO;
    }


    @POST
    @Path("confirmAll")
    public String confirmAll() throws BizCheckedException{
        Map<String, Object> mapRequest = RequestUtils.getRequest();
        HttpSession session = RequestUtils.getSession();
        Long containerId = Long.valueOf(mapRequest.get("containerId").toString());
        //获取当前的有效待QC container 任务列表
        //get task  by containerId
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("containerId", containerId);
        //mapQuery.put("");
        List<TaskEntry> tasks = iTaskRpcService.getTaskHeadList(TaskConstant.TYPE_QC, mapQuery);
        if(tasks.size()==0) {
            throw new BizCheckedException("2120007");
        }
        else if ( tasks.size()>1){
            throw new BizCheckedException("2120006");
        }
        /*
        if(tasks.get(0).getTaskInfo().getStatus()==TaskConstant.Done
                || tasks.get(0).getTaskInfo().getStatus() == TaskConstant.Cancel){
            throw new BizCheckedException("2120011");
        }
        */
        List<Map> qcList = JSON.parseArray(mapRequest.get("qcList").toString(), Map.class);
        List<WaveDetail> details = waveService.getDetailsByContainerId(containerId);
        int addExceptionNum = 0;
        for(Map<String, Object> qcItem : qcList) {
            long exceptionType = 0;
            String code = qcItem.get("code").toString().trim();
            BigDecimal qty = new BigDecimal(qcItem.get("qty").toString());
            CsiSku skuInfo = csiRpcService.getSkuByCode(CsiConstan.CSI_CODE_TYPE_BARCODE, code);
            if (skuInfo == null) {
                throw new BizCheckedException("2120001");
            }
            long skuId = skuInfo.getSkuId();
            int seekNum = 0;
            List<WaveDetail> matchDetails = new LinkedList<WaveDetail>();
            BigDecimal pickQty = new BigDecimal("0.0000");
            for (WaveDetail d : details) {
                if (d.getSkuId() != skuId) {
                    continue;
                }
                seekNum++;
                matchDetails.add(d);
                pickQty = pickQty.add(d.getPickQty());
            }
            if (seekNum == 0) {
                exceptionType = 3;
                long tmpExceptionType = qcItem.get("exceptionType") == null ? 0L : Long.valueOf(qcItem.get("exceptionType").toString());
                if (tmpExceptionType != exceptionType) {
                    throw new BizCheckedException("2120009");
                }
                if (qcItem.get("exceptionQty") == null) {
                    throw new BizCheckedException("2120010");
                }
                WaveQcException qcException = new WaveQcException();
                qcException.setSkuId(skuInfo.getSkuId());
                BigDecimal exctpionQty = new BigDecimal(qcItem.get("exceptionQty").toString());
                qcException.setExceptionQty(exctpionQty);
                qcException.setExceptionType(exceptionType);
                qcException.setQcTaskId(0L);
                qcException.setWaveId(0L);
                waveService.insertQCException(qcException);
                addExceptionNum++;
                continue;
            }
            int cmpRet = pickQty.compareTo(qty);
            if (cmpRet > 0) exceptionType = 2; //多货
            if (cmpRet < 0) exceptionType = 1; //少货

            BigDecimal curQty = new BigDecimal("0.0000");
            for(int i = 0; i < matchDetails.size(); ++i){
                WaveDetail detail = matchDetails.get(i);
                BigDecimal lastQty = curQty;
                curQty = curQty.add(detail.getPickQty());
                detail.setQcQty(qty);
                detail.setQcAt(DateUtils.getCurrentSeconds());
                detail.setQcUid(Long.valueOf(RequestUtils.getHeader("uid")));
                detail.setQcException(exceptionType);
                detail.setQcAt(DateUtils.getCurrentSeconds());
                detail.setQcUid(Long.valueOf(RequestUtils.getHeader("uid")));
                if (exceptionType != 0) {
                    //多货
                    if (i == matchDetails.size() - 1) {
                        detail.setQcException(exceptionType);
                        detail.setQcExceptionQty(qty.subtract(curQty));
                        detail.setQcExceptionDone(0L);
                        detail.setQcQty(qty.subtract(lastQty));

                    } else {
                        //忽略
                        detail.setQcQty(detail.getPickQty());
                        detail.setQcException(0L);
                        detail.setQcExceptionQty(BigDecimal.ZERO);
                        detail.setQcExceptionDone(1L);
                        detail.setQcQty(detail.getPickQty());
                    }
                } else {
                    detail.setQcQty(detail.getPickQty());
                    detail.setQcException(0L);
                    detail.setQcExceptionQty(BigDecimal.ZERO);
                    detail.setQcExceptionDone(1L);
                }
                waveService.updateDetail(detail);
            }
        }
        Set<Long> setItem = new HashSet<Long>();
        for(WaveDetail detail : details){
            setItem.add(detail.getItemId());
        }
        if(qcList.size()-addExceptionNum!=setItem.size()){
            throw new BizCheckedException("2120004");
        }
        iTaskRpcService.done(tasks.get(0).getTaskInfo().getTaskId());
        return JsonUtils.SUCCESS(new HashMap<String, Boolean>() {
            {
                put("response", true);
            }
        });
    }
}
