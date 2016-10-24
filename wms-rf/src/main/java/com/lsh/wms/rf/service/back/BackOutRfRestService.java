package com.lsh.wms.rf.service.back;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.back.IBackOutRfRestService;
import com.lsh.wms.api.service.back.IInStorageRfRestService;
import com.lsh.wms.api.service.location.ILocationRpcService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.stock.IStockQuantRpcService;
import com.lsh.wms.api.service.store.IStoreRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.back.InStorageService;
import com.lsh.wms.core.service.csi.CsiSupplierService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.model.back.BackTaskDetail;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.so.ObdDetail;
import com.lsh.wms.model.so.ObdHeader;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.stock.StockQuantCondition;
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
public class BackOutRfRestService implements IBackOutRfRestService {
    private static Logger logger = LoggerFactory.getLogger(BackOutRfRestService.class);

    @Autowired
    SoOrderService soOrderService;

    @Autowired
    private StockQuantService quantService;

    @Reference
    private ITaskRpcService iTaskRpcService;

    @Autowired
    private BaseTaskService baseTaskService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private CsiSupplierService supplierService;

    @Autowired
    private InStorageService inStorageService;

    @Reference
    private ILocationRpcService locationRpcService;
    ;

    /**
     * 获得商品信息
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("getInfo")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String getInfo() throws BizCheckedException {
        Map<String, Object> request = RequestUtils.getRequest();
        String soOtherId = "";
        Long uId = 0L;
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
        if(header.getOrderStatus()==1){
            return JsonUtils.TOKEN_ERROR("该退货签未投单");
        }
        if(header.getOrderStatus()==2){
            return JsonUtils.TOKEN_ERROR("该退货签未开始退货");
        }

        Map<String,Object> queryMap = new HashMap<String, Object>();
        queryMap.put("orderId", header.getOrderId());
        queryMap.put("type",TaskConstant.TYPE_BACK_OUT);
        List<TaskInfo> infos =  baseTaskService.getTaskInfoList(queryMap);
        if(infos== null || infos.size()==0 ){
            return JsonUtils.TOKEN_ERROR("出库任务不存在");
        }
        TaskInfo info = infos.get(0);
        if(info.getStatus().compareTo(TaskConstant.Done)==0){
            return JsonUtils.TOKEN_ERROR("该出库任务已完成");
        }
        List<ObdDetail> details = soOrderService.getOutbSoDetailList(queryMap);
        iTaskRpcService.assign(info.getTaskId(),uId);
        Map<String,Object> result = new HashMap<String, Object>();
        List<Map> list = new ArrayList<Map>();
        result.put("taskId", info.getTaskId());
        for(ObdDetail detail:details){
            Map<String,Object> one = new HashMap<String, Object>();
            one.put("skuName",detail.getSkuName());
            one.put("packName",detail.getPackName());
            one.put("qty",detail.getOrderQty());
            one.put("skuId",detail.getSkuId());
            list.add(one);
        }
        request.put("list",list);
        return JsonUtils.SUCCESS(result);
    }
    /**
     * 确认任务
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("outConfirm")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String outConfirm() throws BizCheckedException {
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
        iTaskRpcService.update(TaskConstant.TYPE_BACK_OUT, entry);
        iTaskRpcService.done(taskId);
        return JsonUtils.SUCCESS();
    }
}
