package com.lsh.wms.rf.service.inhouse;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.api.service.stock.IStockQuantRpcService;
import com.lsh.wms.core.constant.CsiConstan;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.api.service.inhouse.IStockTransferRestService;
import com.lsh.wms.api.service.inhouse.IStockTransferRpcService;
import com.lsh.wms.api.service.item.IItemRpcService;
import com.lsh.wms.api.service.location.ILocationRpcService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.system.ISysUserRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.system.SysUserService;
import com.lsh.wms.model.csi.CsiSku;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.stock.StockQuantCondition;
import com.lsh.wms.model.system.SysUser;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.transfer.StockTransferPlan;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.ArrayList;
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

    @Reference
    private IStockQuantRpcService stockQuantRpcService;

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
            resultMap.put("fromLocationCode", locationRpcService.getLocation(taskInfo.getFromLocationId()).getLocationCode());
            resultMap.put("toLocationId", taskInfo.getToLocationId());
            resultMap.put("toLocationCode", locationRpcService.getLocation(taskInfo.getToLocationId()).getLocationCode());
            resultMap.put("packName", taskInfo.getPackName());
            resultMap.put("uomQty", taskInfo.getQty().divide(taskInfo.getPackUnit()));
            return JsonUtils.SUCCESS(resultMap);
        } catch (BizCheckedException e){
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JsonUtils.EXCEPTION_ERROR("System Busy!");
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
            Long uId = Long.valueOf(params.get("uId").toString());
            Long staffId = iSysUserRpcService.getSysUserById(uId).getStaffId();
            plan.setPlanner(staffId);
            Long locationId = Long.valueOf(params.get("locationId").toString());
            plan.setFromLocationId(locationId);
            plan.setToLocationId(locationRpcService.getBackLocation().getLocationId());
            plan.setUomQty(new BigDecimal(params.get("uomQty").toString()));
            String barCode = params.get("barcode").toString();
            CsiSku csiSku = itemRpcService.getSkuByCode(CsiConstan.CSI_CODE_TYPE_BARCODE,barCode);
            if(csiSku == null) {
                throw new BizCheckedException("2550003");
            }
            StockQuantCondition condition = new StockQuantCondition();
            condition.setLocationId(locationId);
            condition.setSkuId(csiSku.getSkuId());
            List<StockQuant> quantList = stockQuantRpcService.getQuantList(condition);
            if(quantList.isEmpty()) {
                throw new BizCheckedException("2550003");
            }
            StockQuant quant = quantList.get(0);
            plan.setItemId(quant.getItemId());
            plan.setPackName(quant.getPackName());
            rpcService.addPlan(plan);

            return JsonUtils.SUCCESS(new HashMap<String, Boolean>() {
                {
                    put("response", true);
                }
            });

        } catch (BizCheckedException e){
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JsonUtils.EXCEPTION_ERROR("System Busy!");
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
            Long uId = Long.valueOf(params.get("uId").toString());
            Long staffId = iSysUserRpcService.getSysUserById(uId).getStaffId();
            plan.setPlanner(staffId);
            Long locationId = Long.valueOf(params.get("locationId").toString());
            plan.setFromLocationId(locationId);
            plan.setToLocationId(locationRpcService.getDefectiveLocation().getLocationId());
            plan.setUomQty(new BigDecimal(params.get("uomQty").toString()));

            String barCode =params.get("barcode").toString();
            CsiSku csiSku = itemRpcService.getSkuByCode(CsiConstan.CSI_CODE_TYPE_BARCODE,barCode);
            if(csiSku == null) {
                throw new BizCheckedException("2550003");
            }
            StockQuantCondition condition = new StockQuantCondition();
            condition.setLocationId(locationId);
            condition.setSkuId(csiSku.getSkuId());
            List<StockQuant> quantList = stockQuantRpcService.getQuantList(condition);
            if(quantList.isEmpty()) {
                throw new BizCheckedException("2550003");
            }
            List<Object> resultList = new ArrayList<Object>();
            StockQuant quant = quantList.get(0);
            plan.setItemId(quant.getItemId());
            plan.setPackName(quant.getPackName());
            rpcService.addPlan(plan);
            return JsonUtils.SUCCESS(new HashMap<String, Boolean>() {
                {
                    put("response", true);
                }
            });

        } catch (BizCheckedException e){
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JsonUtils.EXCEPTION_ERROR("System Busy!");
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
        } catch (BizCheckedException e){
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JsonUtils.EXCEPTION_ERROR("System Busy!");
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
        Long staffId = iSysUserRpcService.getSysUserById(uId).getStaffId();
        try {
            final Long taskId = rpcService.assign(staffId);
            if(taskId == 0) {
                throw new BizCheckedException("2040001");
            }
            TaskEntry taskEntry = taskRpcService.getTaskEntryById(taskId);
            if (taskEntry == null) {
                throw new BizCheckedException("2040001");
            }
            final TaskInfo taskInfo = taskEntry.getTaskInfo();
            final Long fromLocationId = taskInfo.getFromLocationId();
            final String fromLocationCode =  locationRpcService.getLocation(fromLocationId).getLocationCode();
            return JsonUtils.SUCCESS(new HashMap<String, Object>() {
                {
                    put("taskId", taskId);
                    put("fromLocationId", fromLocationId);
                    put("fromLocationCode",fromLocationCode);
                }
            });
        } catch (BizCheckedException e){
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JsonUtils.EXCEPTION_ERROR("System Busy!");
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
        } catch (BizCheckedException e){
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JsonUtils.EXCEPTION_ERROR("System Busy!");
        }
        return JsonUtils.SUCCESS(new HashMap<String, Boolean>() {
            {
                put("response", true);
            }
        });
    }

}
