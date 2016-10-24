package com.lsh.wms.rf.service.back;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.api.service.back.IBackShelveRfRestService;
import com.lsh.wms.api.service.back.IInStorageRfRestService;
import com.lsh.wms.api.service.inhouse.IProcurementProveiderRpcService;
import com.lsh.wms.api.service.location.ILocationRpcService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.store.IStoreRpcService;
import com.lsh.wms.api.service.system.ISysUserRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.csi.CsiSupplierService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.model.back.BackTaskDetail;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.so.ObdDetail;
import com.lsh.wms.model.so.ObdHeader;
import com.lsh.wms.model.system.SysUser;
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
@Path("back/shelve")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class BackShelveRfRestService implements IBackShelveRfRestService {
    private static Logger logger = LoggerFactory.getLogger(BackShelveRfRestService.class);

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
    @Autowired
    private StockQuantService quantService;

    @Reference
    private ILocationRpcService locationRpcService;

    @Reference
    private IProcurementProveiderRpcService procurementProveiderRpcService;

    @Reference
    private ISysUserRpcService iSysUserRpcService;
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
        Object fromTaskId = params.get("taskId");
        Long uId =  Long.valueOf(RequestUtils.getHeader("uid"));

        BaseinfoLocation location = locationRpcService.getLocationByCode(locationCode);
        if(location ==null ) {
            return JsonUtils.TOKEN_ERROR("扫描库位非法");
        }
        TaskInfo info = null;
        if(fromTaskId==null) {
            info =  baseTaskService.getDraftTaskIdBylocationId(location.getLocationId());
            List<Long> containerIdList = quantService.getContainerIdByLocationId(location.getLocationId());
            if (info == null || info.getStep() == 1) {
                if (containerIdList == null || containerIdList.size() == 0) {
                    return JsonUtils.TOKEN_ERROR("该库位无库存");
                }
            }
            if (info == null) {
                if (location.getType().compareTo(LocationConstant.SUPPLIER_RETURN_IN_BIN) != 0) {
                    return JsonUtils.TOKEN_ERROR("扫描库位非法");
                }
                info = new TaskInfo();
                info.setTaskName("退货上架任务[ " + locationCode + "]");
                info.setStatus(TaskConstant.Draft);
                info.setFromLocationId(location.getLocationId());
                info.setCreatedAt(DateUtils.getCurrentSeconds());
                info.setType(TaskConstant.TYPE_BACK_SHELVE);
                info.setContainerId(containerIdList.get(0));
                info.setStep(1);
                info.setSubType(1L);
                TaskEntry entry = new TaskEntry();
                entry.setTaskInfo(info);
                Long taskId = iTaskRpcService.create(TaskConstant.TYPE_BACK_SHELVE, entry);
                info.setTaskId(taskId);
            }
            iTaskRpcService.assign(info.getTaskId(), uId);
        }else {
            info =  baseTaskService.getTaskInfoById(Long.valueOf(fromTaskId.toString().trim()));
        }
        if(info.getStep()==1){
            if(location.getType().compareTo(LocationConstant.SUPPLIER_RETURN_IN_BIN)!=0){
                return JsonUtils.TOKEN_ERROR("扫描库位非法");
            }
        }else {
            if(location.getType().compareTo(LocationConstant.SUPPLIER_RETURN_STORE_BIN)!=0){
                return JsonUtils.TOKEN_ERROR("扫描库位非法");
            }
        }
        if(info.getStep()==1) {
            params.put("uid", uId);
            params.put("taskId", info.getTaskId());
            procurementProveiderRpcService.scanFromLocation(params);
            final TaskInfo finalInfo = info;
            return JsonUtils.SUCCESS(new HashMap<String, Object>() {
                {
                    put("taskId", finalInfo.getTaskId().toString());
                    put("type",2);
                }
            });
        }else {
            List<Long> longs = quantService.getContainerIdByLocationId(location.getLocationId());
            if(longs!=null && longs.size()!=0 ){
                return JsonUtils.TOKEN_ERROR("该库位已有商品，不能存入该库位中");
            }
            BigDecimal qty = new BigDecimal(params.get("qty").toString());
            info.setQty(qty);
            info.setToLocationId(location.getLocationId());
            baseTaskService.update(info);
            params.put("uid", uId);
            params.put("taskId", info.getTaskId());
            procurementProveiderRpcService.scanToLocation(params);
            iTaskRpcService.done(info.getTaskId());
            return JsonUtils.SUCCESS(new HashMap<String, Boolean>() {
                {
                    put("response", true);
                }
            });
        }

    }
    /**
     * 回溯任务
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("restore")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String restore() throws BizCheckedException {
        Long uId=0L;
        Long taskId = 0L;
        Map<String,Object> result = new HashMap<String, Object>();
        try {
            uId =  Long.valueOf(RequestUtils.getHeader("uid"));
        }catch (Exception e) {
            logger.error(e.getMessage());
            return JsonUtils.TOKEN_ERROR("参数传递格式有误");
        }

        SysUser user =  iSysUserRpcService.getSysUserById(uId);
        if(user==null){
            return JsonUtils.TOKEN_ERROR("用户不存在");
        }
        // 检查是否有已分配的任务
        taskId = baseTaskService.getAssignTaskIdByOperatorAndType(uId, TaskConstant.TYPE_BACK_SHELVE);
        if(taskId==null) {
            return JsonUtils.SUCCESS(new HashMap<String, Boolean>() {
                {
                    put("response", false);
                }
            });
        }
        TaskEntry entry = iTaskRpcService.getTaskEntryById(taskId);
        TaskInfo info = entry.getTaskInfo();
        result.put("taskId",info.getTaskId());
        result.put("type",info.getType());
        return JsonUtils.SUCCESS(result);
    }
}
