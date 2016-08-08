package com.lsh.wms.rf.service.outbound;

import com.alibaba.dubbo.common.json.ParseException;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.api.service.csi.ICsiRpcService;
import com.lsh.wms.api.service.item.IItemRpcService;
import com.lsh.wms.api.service.pick.IRFQCRestService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.CsiConstan;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.csi.CsiSkuService;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.csi.CsiSku;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.wave.WaveDetail;
import net.sf.json.util.JSONUtils;
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
        iTaskRpcService.assign(info.getTaskId(), 123123L);
        //iTaskRpcService.assign(info.getTaskId(), Long.valueOf((String) session.getAttribute("uid")));
        List<WaveDetail> details = waveService.getDetailsByContainerId(containerId);
        List<Map<String, Object>> undoDetails = new LinkedList<Map<String, Object>>();
        for (WaveDetail d : details){
            if(true) {
                Map<String, Object> detail = new HashMap<String, Object>();
                detail.put("skuId", d.getSkuId());
                BaseinfoItem item = itemRpcService.getItem(d.getOwnerId(), d.getSkuId());
                detail.put("code", item.getCode());
                detail.put("codeType", item.getCodeType());
                detail.put("pickQty", d.getPickQty());
                detail.put("skuName", item.getSkuName());
                undoDetails.add(detail);
            }
        }
        Map<String, Object> rstMap = new HashMap<String, Object>();
        rstMap.put("qc_list", undoDetails);
        return JsonUtils.SUCCESS(rstMap);
    }

    @POST
    @Path("confirmAll")
    public String confirmAll() throws BizCheckedException{
        Map<String, Object> mapRequest = RequestUtils.getRequest();
        HttpSession session = RequestUtils.getSession();
        Long containerId = Long.valueOf(mapRequest.get("containerId").toString());
        //获取当前的有效待QC container 任务列表

        List<Map> qcList = JSON.parseArray(mapRequest.get("qc_list").toString(), Map.class);
        List<WaveDetail> details = waveService.getDetailsByContainerId(containerId);
        //Map<Long, WaveDetail> sku2Detail = new HashMap<Long, WaveDetail>();
        //for(WaveDetail detail : details){
        //    sku2Detail.put(detail.getSkuId(), detail);
        //}
        for(Map<String, Object> qcItem : qcList){
            String code = qcItem.get("code").toString().trim();
            BigDecimal qty = new BigDecimal(qcItem.get("qty").toString());
            CsiSku skuInfo = csiRpcService.getSkuByCode(CsiConstan.CSI_CODE_TYPE_BARCODE, code);
            if(skuInfo == null){
                throw new BizCheckedException("2120001");
            }
            long skuId = skuInfo.getSkuId();
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
            long exceptionType = 0;
            int cmpRet = detail.getPickQty().compareTo(qty);
            if( cmpRet > 0 ) exceptionType = 1;
            if( cmpRet < 0 ) exceptionType = 2;
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
        }
        if(qcList.size()!=details.size()){
            throw new BizCheckedException("2120004");
        }
        //get task  by containerId
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("containerId", containerId);
        //mapQuery.put("");
        List<TaskEntry> tasks = iTaskRpcService.getTaskHeadList(TaskConstant.TYPE_QC, mapQuery);
        if(tasks.size()!=1){
            throw new BizCheckedException("2120006");
        }
        iTaskRpcService.done(tasks.get(0).getTaskInfo().getTaskId());
        return JsonUtils.SUCCESS();
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
        iTaskRpcService.assign(info.getTaskId(), 123123L);
        //iTaskRpcService.assign(info.getTaskId(), Long.valueOf((String) session.getAttribute("uid")));
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
        return JsonUtils.SUCCESS();
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
                detail.put("skuName", item.getSkuName());
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

    @GET
    @Path("createTask")
    public String createTask() throws BizCheckedException {
        Map<String, Object> mapRequest = RequestUtils.getRequest();
        Long containerId = Long.valueOf(mapRequest.get("containerId").toString());
        List<WaveDetail> details = waveService.getDetailsByContainerId(containerId);
        if(details.size()==0){
            throw new BizCheckedException("2120005");
        }
        TaskEntry entry = new TaskEntry();
        TaskInfo info = new TaskInfo();
        info.setType(TaskConstant.TYPE_QC);
        info.setContainerId(containerId);
        entry.setTaskDetailList((List<Object>)(List<?>)details);
        entry.setTaskInfo(info);
        Long taskId = iTaskRpcService.create(TaskConstant.TYPE_QC, entry);
        return JsonUtils.SUCCESS();
    }
}
