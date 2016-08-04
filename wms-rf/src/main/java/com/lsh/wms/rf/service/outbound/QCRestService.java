package com.lsh.wms.rf.service.outbound;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
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
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
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
    @Path("scan")
    public String scan(@QueryParam("containerId") Long containerId){
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
        //if(info.)
        iTaskRpcService.assign(info.getTaskId(), Long.valueOf((String) session.getAttribute("uid")));
        return JsonUtils.SUCCESS();
    }
    @POST
    @Path("setResult")
    public String setResult() throws BizCheckedException{
        Map<String,Object> request = RequestUtils.getRequest();
        long containerId = (Long) request.get("containerId");
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
        detail.setQcUid(Long.valueOf((String) session.getAttribute("uid")));
        detail.setQcException(exceptionType);
        if(exceptionType!=0) {
            detail.setQcExceptionQty(BigDecimal.ZERO);
            detail.setQcExceptionDone(0L);
        }else{
            detail.setQcUid(Long.valueOf((String) session.getAttribute("uid")));
            detail.setQcExceptionQty(BigDecimal.ZERO);
            detail.setQcExceptionDone(1L);
        }
        waveService.updateDetail(detail);
        return JsonUtils.SUCCESS();
    }

    @GET
    @Path("getUndoDetails")
    public String getUndoDetails(@QueryParam("containerId") long containerId) {
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
    public String confirm(@QueryParam("containerId") long containerId) throws BizCheckedException {
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

    public String createTask(long containerId) throws BizCheckedException {
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
