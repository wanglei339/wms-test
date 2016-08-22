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
import com.lsh.wms.core.dao.task.TaskInfoDao;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.Task;

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

    @Autowired
    private TaskInfoDao taskInfoDao;

    @POST
    @Path("view")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String taskView() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        Long taskId = Long.valueOf(mapQuery.get("taskId").toString());
        try {
            TaskEntry taskEntry = taskRpcService.getTaskEntryById(taskId);
            if (taskEntry == null) {
                throw new BizCheckedException("2040001");
            }
            Map<String, Object> resultMap = new HashMap<String, Object>();
            TaskInfo taskInfo = taskEntry.getTaskInfo();
            resultMap.put("itemId", taskInfo.getItemId());
            resultMap.put("itemName", itemRpcService.getItem(taskInfo.getItemId()).getSkuName());
            resultMap.put("fromLocationId", taskInfo.getFromLocationId());
            resultMap.put("fromLocationCode", locationRpcService.getLocation(taskInfo.getFromLocationId()).getLocationCode());
            resultMap.put("toLocationId", taskInfo.getToLocationId());
            resultMap.put("toLocationCode", locationRpcService.getLocation(taskInfo.getToLocationId()).getLocationCode());
            resultMap.put("packName", taskInfo.getPackName());
            resultMap.put("uomQty", taskInfo.getQty().divide(taskInfo.getPackUnit()));
            return JsonUtils.SUCCESS(resultMap);
        } catch (BizCheckedException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JsonUtils.EXCEPTION_ERROR(e.getMessage());
        }
    }

    @POST
    @Path("createReturn")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON})
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
            CsiSku csiSku = itemRpcService.getSkuByCode(CsiConstan.CSI_CODE_TYPE_BARCODE, barCode);
            if (csiSku == null) {
                throw new BizCheckedException("2550003");
            }
            StockQuantCondition condition = new StockQuantCondition();
            condition.setLocationId(locationId);
            condition.setSkuId(csiSku.getSkuId());
            List<StockQuant> quantList = stockQuantRpcService.getQuantList(condition);
            if (quantList.isEmpty()) {
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

        } catch (BizCheckedException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JsonUtils.EXCEPTION_ERROR(e.getMessage());
        }
    }

    @POST
    @Path("createScrap")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON})
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

            String barCode = params.get("barcode").toString();
            CsiSku csiSku = itemRpcService.getSkuByCode(CsiConstan.CSI_CODE_TYPE_BARCODE, barCode);
            if (csiSku == null) {
                throw new BizCheckedException("2550003");
            }
            StockQuantCondition condition = new StockQuantCondition();
            condition.setLocationId(locationId);
            condition.setSkuId(csiSku.getSkuId());
            List<StockQuant> quantList = stockQuantRpcService.getQuantList(condition);
            if (quantList.isEmpty()) {
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

        } catch (BizCheckedException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JsonUtils.EXCEPTION_ERROR(e.getMessage());
        }
    }

    @POST
    @Path("scanLocation")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String scanLocation() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        Map<String, Object> result;
        Long type = Long.valueOf(mapQuery.get("type").toString());
        try {
            Long taskId = Long.valueOf(mapQuery.get("taskId").toString());
            TaskEntry taskEntry = taskRpcService.getTaskEntryById(taskId);
            if (taskEntry == null) {
                throw new BizCheckedException("3040001");
            }
            if (!taskEntry.getTaskInfo().getType().equals(TaskConstant.TYPE_STOCK_TRANSFER)) {
                throw new BizCheckedException("2550021");
            }
            if (type.equals(1L)) {
                result = rpcService.scanFromLocation(mapQuery);
            } else {
                result = rpcService.scanToLocation(mapQuery);
            }
        } catch (BizCheckedException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JsonUtils.EXCEPTION_ERROR(e.getMessage());
        }
        return JsonUtils.SUCCESS(result);
    }

    @POST
    @Path("fetchTask")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String fetchTask() throws BizCheckedException {
        Map<String, Object> params = RequestUtils.getRequest();
        //Long locationId = Long.valueOf(params.get("locationId").toString());
        Long staffId = iSysUserRpcService.getSysUserById(Long.valueOf(params.get("uId").toString())).getStaffId();
        try {
            final Long taskId = rpcService.assign(staffId);
            logger.info("assign finish");
            if (taskId == 0) {
                throw new BizCheckedException("2040001");
            }
            TaskEntry taskEntry = taskRpcService.getTaskEntryById(taskId);
            if (taskEntry == null) {
                throw new BizCheckedException("2040001");
            }
            final TaskInfo taskInfo = taskEntry.getTaskInfo();
            final Long locationId, type;
            final BigDecimal uomQty;
            //outbound
            if (taskInfo.getExt3().equals(0L)) {
                type = 1L;
                locationId = taskInfo.getFromLocationId();
                uomQty = taskInfo.getQty().divide(taskInfo.getPackUnit());
            } else {
                type = 2L;
                locationId = taskInfo.getToLocationId();
                uomQty = taskInfo.getQtyDone().divide(taskInfo.getPackUnit());
            }
            final String locationCode = locationRpcService.getLocation(locationId).getLocationCode();
            return JsonUtils.SUCCESS(new HashMap<String, Object>() {
                {
                    put("type", type);
                    put("taskId", taskId);
                    put("locationId", locationId);
                    put("locationCode", locationCode);
                    put("itemId", taskInfo.getItemId());
                    put("itemName", itemRpcService.getItem(taskInfo.getItemId()).getSkuName());
                    put("packName", taskInfo.getPackName());
                    put("uomQty", uomQty);
                }
            });
        } catch (BizCheckedException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JsonUtils.EXCEPTION_ERROR(e.getMessage());
        }
    }

    @POST
    @Path("unFetchTask")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String unFetchTask() throws BizCheckedException {
        Map<String, Object> params = RequestUtils.getRequest();
        //Long locationId = Long.valueOf(params.get("locationId").toString());
        Long staffId = iSysUserRpcService.getSysUserById(Long.valueOf(params.get("uId").toString())).getStaffId();
        try {
            Long taskId = Long.valueOf(params.get("taskId").toString());
            TaskEntry taskEntry = taskRpcService.getTaskEntryById(taskId);
            if (taskEntry == null) {
                throw new BizCheckedException("2040001");
            }
            TaskInfo taskInfo = taskEntry.getTaskInfo();
            Map<String, Object> response = new HashMap<String, Object>();
            if (taskInfo.getOperator().equals(staffId) && taskInfo.getStatus().equals(TaskConstant.Assigned) && taskInfo.getExt3() == 0) {
                taskInfo.setOperator(1L);
                taskInfo.setStatus(TaskConstant.Draft);
                taskInfoDao.update(taskInfo);
                response.put("unFetch", true);
            } else {
                response.put("unFetch", false);
            }
            return JsonUtils.SUCCESS(response);

        } catch (BizCheckedException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JsonUtils.EXCEPTION_ERROR(e.getMessage());
        }
    }

    @POST
    @Path("confirmTask")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String confirmTask() throws BizCheckedException {
        Map<String, Object> params = RequestUtils.getRequest();
        //Long locationId = Long.valueOf(params.get("locationId").toString());
        Long staffId = iSysUserRpcService.getSysUserById(Long.valueOf(params.get("uId").toString())).getStaffId();
        try {
            return JsonUtils.SUCCESS(rpcService.sortTask(staffId));
        } catch (BizCheckedException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JsonUtils.EXCEPTION_ERROR(e.getMessage());
        }
    }
}
