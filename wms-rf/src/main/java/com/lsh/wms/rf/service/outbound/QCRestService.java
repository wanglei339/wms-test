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
import com.lsh.wms.api.service.pick.IRFQCRestService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.CsiConstan;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.baseinfo.BaseinfoContainer;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.csi.CsiSku;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.wave.WaveDetail;
import com.lsh.wms.model.wave.WaveQcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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

    @POST
    @Path("getQcTaskByPickTask")
    public String getQcTaskByPickTask() throws BizCheckedException{
        Map<String, Object> mapRequest = RequestUtils.getRequest();
        Long pickTaskId = Long.valueOf(mapRequest.get("pickTaskId").toString());
        //校验捡货任务
        TaskInfo pickTaskInfo = iTaskRpcService.getTaskInfo(pickTaskId);
        if(pickTaskInfo == null){
            //捡货任务不存在
        }
        if(pickTaskInfo.getStatus() != TaskConstant.Done){
            //捡货任务未完成
        }
        //获取QC任务
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("ext1", pickTaskId);
        List<TaskEntry> tasks = iTaskRpcService.getTaskHeadList(TaskConstant.TYPE_QC, mapQuery);
        if(tasks.size() != 1){
            //任务不存在
        }
        TaskInfo qcTaskInfo = tasks.get(0).getTaskInfo();
        //认领任务
        iTaskRpcService.assign(qcTaskInfo.getTaskId(), Long.valueOf(RequestUtils.getHeader("uid")));
        //获取捡货明细
        List<WaveDetail> details = waveService.getDetailsByPickTaskId(pickTaskId);
        //merge item_id 2 pick  qty
        Map<Long, BigDecimal> mapItem2PickQty = new HashMap<Long, BigDecimal>();
        for( WaveDetail d : details){
            if(mapItem2PickQty.get(d.getItemId())==null){
                mapItem2PickQty.put(d.getItemId(), new BigDecimal(d.getPickQty().toString()));
            }else{
                mapItem2PickQty.put(d.getItemId(), mapItem2PickQty.get(d.getItemId()).add(d.getPickQty()));
            }
        }

        List<Map<String, Object>> undoDetails = new LinkedList<Map<String, Object>>();
        for(Long itemId : mapItem2PickQty.keySet()) {
            Map<String, Object> detail = new HashMap<String, Object>();
            BaseinfoItem item = itemRpcService.getItem(itemId);
            if(item == null){
                //商品数据异常
            }
            detail.put("skuId", item.getSkuId());
            detail.put("itemId", item.getItemId());
            detail.put("code", item.getCode());
            detail.put("codeType", item.getCodeType());
            detail.put("pickQty", mapItem2PickQty.get(itemId));
            detail.put("packName", "EA");
            //TODO packName
            detail.put("itemName", item.getSkuName());
            undoDetails.add(detail);
        }
        //获取托盘信息
        BaseinfoContainer containerInfo = iContainerRpcService.getContainer(pickTaskInfo.getContainerId());
        if(containerInfo==null){
            throw new BizCheckedException("托盘码不存在");
        }
        //返回结果
        Map<String, Object> rstMap = new HashMap<String, Object>();
        rstMap.put("qcList", undoDetails);
        rstMap.put("containerType", containerInfo.getType());
        return JsonUtils.SUCCESS(rstMap);
    }

    @POST
    @Path("confirmByPickTask")
    public String confirmByPickTask() throws BizCheckedException{
        Map<String, Object> mapRequest = RequestUtils.getRequest();
        Long pickTaskId = Long.valueOf(mapRequest.get("pickTaskId").toString());
        //获取QC任务
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("ext1", pickTaskId);
        List<TaskEntry> tasks = iTaskRpcService.getTaskHeadList(TaskConstant.TYPE_QC, mapQuery);
        if(tasks.size() != 1){
            //任务不存在
        }
        List<Map> qcList = JSON.parseArray(mapRequest.get("qcList").toString(), Map.class);
        //获取捡货明细
        List<WaveDetail> details = waveService.getDetailsByPickTaskId(pickTaskId);
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
            if (cmpRet > 0) exceptionType = 1; //多货
            if (cmpRet < 0) exceptionType = 2; //少货

            BigDecimal curQty = new BigDecimal("0.0000");
            for(WaveDetail detail : matchDetails) {
                //split
                curQty = curQty.add(detail.getPickQty());
                detail.setQcQty(qty);
                detail.setQcAt(DateUtils.getCurrentSeconds());
                detail.setQcUid(Long.valueOf(RequestUtils.getHeader("uid")));
                detail.setQcException(exceptionType);
                detail.setQcAt(DateUtils.getCurrentSeconds());
                detail.setQcUid(Long.valueOf(RequestUtils.getHeader("uid")));
                if (exceptionType != 0) {
                    if(exceptionType == 1){
                        //多货
                        if(curQty.compareTo(qty)>0){
                            //标记错误
                            detail.setQcException(1L);
                            detail.setQcExceptionQty(BigDecimal.ZERO);
                            detail.setQcExceptionDone(0L);
                        }else{
                            //忽略
                            detail.setQcQty(detail.getPickQty());
                            detail.setQcException(0L);
                            detail.setQcExceptionQty(BigDecimal.ZERO);
                            detail.setQcExceptionDone(1L);
                        }
                    } else if (exceptionType == 2){

                    } else {
                        //未知类型
                    }
                    detail.setQcExceptionQty(BigDecimal.ZERO);
                    detail.setQcExceptionDone(0L);
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

    @POST
    @Path("scanContainer")
    public String scanContainer() throws BizCheckedException{
        Map<String, Object> mapRequest = RequestUtils.getRequest();
        Long containerId = Long.valueOf(mapRequest.get("containerId").toString());
        HttpSession session = RequestUtils.getSession();
        //get task  by containerId
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("containerId", containerId);
        //mapQuery.put("");
        List<TaskEntry> tasks = iTaskRpcService.getTaskHeadList(TaskConstant.TYPE_QC, mapQuery);
        if(tasks.size()>1){
            throw new BizCheckedException("2120006");
        }
        if(tasks.size()==0){
            throw new BizCheckedException("2120007");
        }
        TaskInfo info = tasks.get(0).getTaskInfo();
        iTaskRpcService.assign(info.getTaskId(), Long.valueOf(RequestUtils.getHeader("uid")));
        List<WaveDetail> details = waveService.getDetailsByContainerId(containerId);
        //merge item_id 2 pick  qty
        Map<Long, BigDecimal> mapItem2PickQty = new HashMap<Long, BigDecimal>();
        for( WaveDetail d : details){
            if(mapItem2PickQty.get(d.getItemId())==null){
                mapItem2PickQty.put(d.getItemId(), new BigDecimal(d.getPickQty().toString()));
            }else{
                mapItem2PickQty.put(d.getItemId(), mapItem2PickQty.get(d.getItemId()).add(d.getPickQty()));
            }
        }

        List<Map<String, Object>> undoDetails = new LinkedList<Map<String, Object>>();
        for(Long itemId : mapItem2PickQty.keySet()) {
            Map<String, Object> detail = new HashMap<String, Object>();
            BaseinfoItem item = itemRpcService.getItem(itemId);
            if(item == null){
                //商品数据异常
            }
            detail.put("skuId", item.getSkuId());
            detail.put("itemId", item.getItemId());
            detail.put("code", item.getCode());
            detail.put("codeType", item.getCodeType());
            detail.put("pickQty", mapItem2PickQty.get(itemId));
            detail.put("packName", "EA");
            //TODO packName
            detail.put("itemName", item.getSkuName());
            undoDetails.add(detail);
        }
        BaseinfoContainer containerInfo = iContainerRpcService.getContainer(containerId);
        if(containerInfo==null){
            throw new BizCheckedException("托盘码不存在");
        }
        Map<String, Object> rstMap = new HashMap<String, Object>();
        rstMap.put("qcList", undoDetails);
        rstMap.put("containerType", containerInfo.getType());
        return JsonUtils.SUCCESS(rstMap);
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
        /*
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
            WaveDetail detail = null;
            for (WaveDetail d : details) {
                if (d.getSkuId() != skuId) {
                    continue;
                }
                seekNum++;
                detail = d;
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
            if (seekNum > 1) {
                throw new BizCheckedException("2120003");
            }
            int cmpRet = detail.getPickQty().compareTo(qty);
            if (cmpRet > 0) exceptionType = 1;
            if (cmpRet < 0) exceptionType = 2;

            detail.setQcQty(qty);
            detail.setQcAt(DateUtils.getCurrentSeconds());
            detail.setQcUid(Long.valueOf(RequestUtils.getHeader("uid")));
            detail.setQcException(exceptionType);
            if (exceptionType != 0) {
                detail.setQcExceptionQty(BigDecimal.ZERO);
                detail.setQcExceptionDone(0L);
            } else {
                detail.setQcUid(Long.valueOf(RequestUtils.getHeader("uid")));
                detail.setQcExceptionQty(BigDecimal.ZERO);
                detail.setQcExceptionDone(1L);
            }
            waveService.updateDetail(detail);
        }
        if(qcList.size()-addExceptionNum!=details.size()){
            throw new BizCheckedException("2120004");
        }
        iTaskRpcService.done(tasks.get(0).getTaskInfo().getTaskId());
        return JsonUtils.SUCCESS(new HashMap<String, Boolean>() {
            {
                put("response", true);
            }
        });
        */
    }



    @POST
    @Path("scan")
    public String scan() throws  BizCheckedException{
        Map<String, Object> mapRequest = RequestUtils.getRequest();
        Long containerId = Long.valueOf(mapRequest.get("containerId").toString());
        HttpSession session = RequestUtils.getSession();
        //get task  by containerId
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("containerId", containerId);
        //mapQuery.put("");
        List<TaskEntry> tasks = iTaskRpcService.getTaskHeadList(TaskConstant.TYPE_QC, mapQuery);
        if(tasks.size()!=1){
            throw new BizCheckedException("");
        }
        TaskInfo info = tasks.get(0).getTaskInfo();
        iTaskRpcService.assign(info.getTaskId(), Long.valueOf(RequestUtils.getHeader("uid")));
        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("setResult")
    public String setResult() throws BizCheckedException{
        Map<String,Object> request = RequestUtils.getRequest();
        long containerId =  Long.valueOf((String)request.get("containerId"));
        String code = (String) request.get("code");
        CsiSku skuInfo = csiRpcService.getSkuByCode(CsiConstan.CSI_CODE_TYPE_BARCODE, code);
        if(skuInfo == null){
            throw new BizCheckedException("2120001");
        }
        long skuId = skuInfo.getSkuId();
        BigDecimal qty = new BigDecimal((String)request.get("qty"));
        long exceptionType = request.get("exceptionType")==null ? 0L : (Long) request.get("exceptionType");
        //获取当前的有效待QC container 任务列表
        List<WaveDetail> details = waveService.getDetailsByContainerId(containerId);
        //遍历
        int seekNum = 0;
        WaveDetail detail = null;
        for(WaveDetail d : details){
            if(d.getSkuId()!=skuId){
                continue;
            }
            seekNum++;
            detail = d;
        }
        if(seekNum == 0){
            throw new BizCheckedException("2120002");
        }
        if(seekNum > 1){
            throw new BizCheckedException("2120003");
        }
        HttpSession session = RequestUtils.getSession();
        detail.setQcQty(qty);
        detail.setQcAt(DateUtils.getCurrentSeconds());
        detail.setQcUid(1L);//Long.valueOf((String) session.getAttribute("uid")));
        detail.setQcException(exceptionType);
        if(exceptionType!=0) {
            detail.setQcExceptionQty(BigDecimal.ZERO);
            detail.setQcExceptionDone(0L);
        }else{
            detail.setQcUid(1L);//Long.valueOf((String) session.getAttribute("uid")));
            detail.setQcExceptionQty(BigDecimal.ZERO);
            detail.setQcExceptionDone(1L);
        }
        waveService.updateDetail(detail);
        return JsonUtils.SUCCESS(new HashMap<String, Boolean>() {
            {
                put("response", true);
            }
        });
    }

    @POST
    @Path("getUndoDetails")
    public String getUndoDetails() {
        Map<String, Object> mapRequest = RequestUtils.getRequest();
        Long containerId = Long.valueOf(mapRequest.get("containerId").toString());
        List<WaveDetail> details = waveService.getDetailsByContainerId(containerId);
        List<Map<String, Object>> undoDetails = new LinkedList<Map<String, Object>>();
        for (WaveDetail d : details){
            if(d.getQcAt()==0) {
                Map<String, Object> detail = new HashMap<String, Object>();
                detail.put("skuId", d.getSkuId());
                BaseinfoItem item = itemRpcService.getItem(d.getOwnerId(), d.getSkuId());
                detail.put("code", item.getCode());
                detail.put("codeType", item.getCodeType());
                detail.put("pickQty", d.getPickQty());
                detail.put("itemName", item.getSkuName());
                undoDetails.add(detail);
            }
        }
        return JsonUtils.SUCCESS(undoDetails);
    }

    @POST
    @Path("confirm")
    public String confirm() throws BizCheckedException {
        Map<String, Object> mapRequest = RequestUtils.getRequest();
        Long containerId = Long.valueOf(mapRequest.get("containerId").toString());
        List<WaveDetail> details = waveService.getDetailsByContainerId(containerId);
        Set<Long> tasks = new HashSet<Long>();
        //iTaskRpcService.
        for (WaveDetail d : details){
            //未qc行项目
            //已QC但异常未处理完成行项目
            if(d.getQcAt()==0
                || d.getQcExceptionDone()==0){
                //未qc或者异常未处理
                throw new BizCheckedException("2120004");
            }
            if(!tasks.contains(d.getQcTaskId())) {
                //qc task done;
                iTaskRpcService.done(d.getQcTaskId());
                tasks.add(d.getQcTaskId());
            }
        }
        return JsonUtils.SUCCESS();
    }


}
