package com.lsh.wms.rf.service.inhouse;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.api.service.inhouse.IStockTransferRestService;
import com.lsh.wms.api.service.inhouse.IStockTransferRpcService;
import com.lsh.wms.api.service.item.IItemRpcService;
import com.lsh.wms.api.service.location.ILocationRpcService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.system.ISysUserRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.system.SysUserService;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.stock.StockQuantCondition;
import com.lsh.wms.model.system.SysUser;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.transfer.StockTransferPlan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mali on 16/8/1.
 */
@Service(protocol = "rest")
@Path("inhouse/stock_transfer")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class StockTransferRestService implements IStockTransferRestService {
    private static Logger logger = LoggerFactory.getLogger(StockTransferRestService.class);

    @Reference
    private IStockTransferRpcService rpcService;

    @Reference
    private ITaskRpcService taskRpcService;

    @Reference
    private IItemRpcService itemRpcService;

    @Reference
    private ILocationRpcService locationRpcService;

    @Reference
    private ISysUserRpcService iSysUserRpcService;

    @POST
    @Path("view")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String taskView() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        Long taskId = Long.valueOf(mapQuery.get("taskId").toString());
        try {
            TaskEntry taskEntry = taskRpcService.getTaskEntryById(taskId);
            if (taskEntry == null) {
                throw new BizCheckedException("2040001");
            }
            TaskInfo taskInfo = taskEntry.getTaskInfo();
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("itemId", taskInfo.getItemId());
            resultMap.put("itemName", itemRpcService.getItem(taskInfo.getItemId()).getSkuName());
            resultMap.put("fromLocationId", taskInfo.getFromLocationId());
            resultMap.put("fromLocationName", locationRpcService.getLocation(taskInfo.getFromLocationId()).getLocationCode());
            resultMap.put("toLocationId", taskInfo.getToLocationId());
            resultMap.put("toLocationName", locationRpcService.getLocation(taskInfo.getToLocationId()).getLocationCode());
            resultMap.put("packName", taskInfo.getPackName());
            resultMap.put("uomQty", taskInfo.getQty().divide(taskInfo.getPackUnit()));
            return JsonUtils.SUCCESS(resultMap);
        } catch (Exception e) {
            logger.error(e.getCause().getMessage());
            return JsonUtils.EXCEPTION_ERROR(e.getCause().getMessage());
        }
    }

    @POST
    @Path("createReturn")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String createReturn() throws BizCheckedException {
        try {
            Map<String, Object> params = RequestUtils.getRequest();
            StockTransferPlan plan = new StockTransferPlan();
            plan.setFromLocationId(Long.valueOf(params.get("locationId").toString()));

            plan.setToLocationId(locationRpcService.getBackLocation().getLocationId());
            plan.setUomQty(new BigDecimal(params.get("uomQty").toString()));
            plan.setPackName(params.get("packName").toString());
            plan.setPlanner(Long.valueOf(params.get("planner").toString()));
            plan.setItemId(Long.valueOf(params.get("itemId").toString()));

            rpcService.addPlan(plan);

            return JsonUtils.SUCCESS(new HashMap<String, Boolean>() {
                {
                    put("response", true);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.EXCEPTION_ERROR(e.getMessage());
        }
    }

    @POST
    @Path("createScrap")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String createScrap() throws BizCheckedException {
        try {
            Map<String, Object> params = RequestUtils.getRequest();
            StockTransferPlan plan = new StockTransferPlan();
            plan.setFromLocationId(Long.valueOf(params.get("locationId").toString()));
            plan.setToLocationId(locationRpcService.getDefectiveLocation().getLocationId());
            plan.setUomQty(new BigDecimal(params.get("uomQty").toString()));
            plan.setPackName(params.get("packName").toString());
            plan.setPlanner(Long.valueOf(params.get("planner").toString()));
            plan.setItemId(Long.valueOf(params.get("itemId").toString()));

            rpcService.addPlan(plan);
            return JsonUtils.SUCCESS(new HashMap<String, Boolean>() {
                {
                    put("response", true);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.EXCEPTION_ERROR(e.getMessage());
        }
    }

    @POST
    @Path("scanFromLocation")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String scanFromLocation() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        try {
            rpcService.scanFromLocation(mapQuery);
        } catch (BizCheckedException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getCause().getMessage());
            return JsonUtils.EXCEPTION_ERROR(e.getCause().getMessage());
        }
        return JsonUtils.SUCCESS(new HashMap<String, Boolean>() {
            {
                put("response", true);
            }
        });
    }

    @POST
    @Path("fetchTask")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String fetchTask() throws BizCheckedException {
        Map<String, Object> params = RequestUtils.getRequest();
        //Long locationId = Long.valueOf(params.get("locationId").toString());
        Long uId = Long.valueOf(params.get("uId").toString());
        SysUser sysUser = iSysUserRpcService.getSysUserById(uId);
        Long staffId = sysUser.getStaffId();
        try {
            final Long taskId = rpcService.assign(staffId);
            TaskEntry taskEntry = taskRpcService.getTaskEntryById(taskId);
            if (taskEntry == null) {
                throw new BizCheckedException("2040001");
            }
            final TaskInfo taskInfo = taskEntry.getTaskInfo();
            return JsonUtils.SUCCESS(new HashMap<String, Long>() {
                {
                    put("taskId", taskId);
                    put("fromLocationId", taskInfo.getFromLocationId());
                }
            });
        } catch (BizCheckedException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getCause().getMessage());
            return JsonUtils.EXCEPTION_ERROR(e.getCause().getMessage());
        }
    }

    @POST
    @Path("scanToLocation")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String scanToLocation() throws BizCheckedException {
        Map<String, Object> params = RequestUtils.getRequest();
        try {
            rpcService.scanToLocation(params);
        } catch (BizCheckedException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getCause().getMessage());
            return JsonUtils.EXCEPTION_ERROR(e.getCause().getMessage());
        }
        return JsonUtils.SUCCESS(new HashMap<String, Boolean>() {
            {
                put("response", true);
            }
        });
    }


}
