package com.lsh.wms.rf.service.back;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.back.IInStorageRfRestService;
import com.lsh.wms.api.service.location.ILocationRpcService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.store.IStoreRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.csi.CsiSupplierService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.model.back.BackTaskDetail;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.so.ObdDetail;
import com.lsh.wms.model.so.ObdHeader;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
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
 * Created by wuhao on 2016/10/21.
 */

@Service(protocol = "rest")
@Path("back/in_storage")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class InStorageRfRestService  implements IInStorageRfRestService {
    private static Logger logger = LoggerFactory.getLogger(InStorageRfRestService.class);

    @Autowired
    SoOrderService soOrderService;

    @Reference
    private IStoreRpcService storeRpcService;

    @Reference
    private ITaskRpcService iTaskRpcService;

    @Autowired
    private BaseTaskService baseTaskService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private CsiSupplierService supplierService;

    @Reference
    private ILocationRpcService locationRpcService;
    /**
     * 获得供商库位信息
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("getSupplierInfo")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String getSupplierInfo() throws BizCheckedException {
        String soOtherId = "";
        Long uId = 0L;
        Map<String, Object> request = RequestUtils.getRequest();
        try {
            uId =  Long.valueOf(RequestUtils.getHeader("uid"));
            soOtherId =request.get("soOtherId").toString().trim();
        }catch (Exception e) {
            logger.error(e.getMessage());
            return JsonUtils.TOKEN_ERROR("参数传递格式有误");
        }
        ObdHeader header = soOrderService.getOutbSoHeaderByOrderOtherId(soOtherId);
        if(header == null){
            return JsonUtils.TOKEN_ERROR("该退货供货签不存在");
        }
        Map<String,Object> queryMap = new HashMap<String, Object>();
        queryMap.put("orderId", header.getOrderId());
        queryMap.put("type",TaskConstant.TYPE_BACK_IN_STORAGE);
        List<TaskInfo> infos =  baseTaskService.getTaskInfoList(queryMap);
        if(infos== null || infos.size()==0 ){
            return JsonUtils.TOKEN_ERROR("入库任务不存在");
        }
        TaskInfo info = infos.get(0);
        if(info.getStatus().compareTo(TaskConstant.Done)==0){
            return JsonUtils.TOKEN_ERROR("该入库任务已完成");
        }
        iTaskRpcService.assign(info.getTaskId(), uId);
        Map<String,Object> result = new HashMap<String, Object>();
        result.put("taskId", info.getTaskId().toString());
        List<BaseinfoLocation> locations = locationService.getLocationBySupplierNo(LocationConstant.SUPPLIER_RETURN_IN_BIN, header.getSupplierNo());
        if(locations==null || locations.size()==0){
            return JsonUtils.TOKEN_ERROR("该供商没有配入库位");
        }
        result.put("supplierName",supplierService.getSuppler(header.getSupplierNo(),header.getOwnerUid()).getSupplierName());
        result.put("locationCode",locations.get(0).getLocationCode());
        return JsonUtils.SUCCESS(result);
    }
    /**
     * 获得so detail信息
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("scanLocation")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String scanLocation() throws BizCheckedException {
        Map<String, Object> params = RequestUtils.getRequest();
        String locationCode = params.get("locationCode").toString().trim();
        Long taskId = Long.valueOf(params.get("taskId").toString().trim());
        BaseinfoLocation location = locationRpcService.getLocationByCode(locationCode);
        TaskInfo info = baseTaskService.getTaskInfoById(taskId);
        if(info==null){
            return JsonUtils.TOKEN_ERROR("任务不存在");
        }
        ObdHeader header = soOrderService.getOutbSoHeaderByOrderId(info.getOrderId());
        if(!location.getSupplierNo().equals(header.getSupplierNo()) || location.getType().compareTo(LocationConstant.SUPPLIER_RETURN_IN_BIN)!=0) {
            return JsonUtils.TOKEN_ERROR("扫描库位不属于该供商入库位");
        }
        Map<String,Object> query = new HashMap<String, Object>();
        query.put("orderId",header.getOrderId());
        List<ObdDetail> details = soOrderService.getOutbSoDetailList(query);
        Map<String,Object> result = new HashMap<String, Object>();
        List<Map> list = new ArrayList<Map>();
        info.setLocationId(location.getLocationId());
        baseTaskService.update(info);
        result.put("taskId",taskId);
        for(ObdDetail detail:details){
            Map<String,Object> one = new HashMap<String, Object>();
            one.put("skuName",detail.getSkuName());
            one.put("packName",detail.getPackName());
            one.put("qty",detail.getOrderQty());
            one.put("skuId",detail.getSkuId());
            list.add(one);
        }
        result.put("list",list);
        return JsonUtils.SUCCESS(result);
    }
    /**
     * 确认入库任务
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("backConfirm")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String backConfirm() throws BizCheckedException {
        Map<String, Object> params = RequestUtils.getRequest();
        Long taskId = Long.valueOf(params.get("taskId").toString().trim());
        Map<String,String> result = (Map)params.get("map");

        TaskEntry entry = iTaskRpcService.getTaskEntryById(taskId);
        if(entry==null){
            return JsonUtils.TOKEN_ERROR("任务不存在");
        }
        List<Object> objectList = entry.getTaskDetailList();
        List<BackTaskDetail> details = new ArrayList<BackTaskDetail>();
        for(Object one:objectList){
            BackTaskDetail detail = (BackTaskDetail)one;
            if(result.get(detail.getSkuId().toString())!=null){
                detail.setRealQty(new BigDecimal(result.get(detail.getSkuId().toString())));
            }
            details.add(detail);
        }
        entry.setTaskDetailList((List<Object>) (List<?>)details);
        iTaskRpcService.update(TaskConstant.TYPE_BACK_IN_STORAGE,entry);
        iTaskRpcService.done(taskId);
        return JsonUtils.SUCCESS(new HashMap<String, Boolean>() {
            {
                put("response", true);
            }
        });
    }
}
