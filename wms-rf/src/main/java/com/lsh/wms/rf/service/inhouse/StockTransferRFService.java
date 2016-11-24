package com.lsh.wms.rf.service.inhouse;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.inhouse.IStockTransferRFService;
import com.lsh.wms.api.service.inhouse.IStockTransferRpcService;
import com.lsh.wms.api.service.item.IItemRpcService;
import com.lsh.wms.api.service.location.ILocationRpcService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.stock.IStockQuantRpcService;
import com.lsh.wms.api.service.system.ISysUserRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.BinUsageConstant;
import com.lsh.wms.core.constant.CsiConstan;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.dao.task.TaskInfoDao;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.utils.PackUtil;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.csi.CsiSku;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.stock.StockQuantCondition;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.transfer.StockTransferPlan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zengwenjun on 16/11/24.
 */
@Service(protocol = "rest")
@Path("inhouse/transfer")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class StockTransferRFService implements IStockTransferRFService{
    private static Logger logger = LoggerFactory.getLogger(StockTransferRFService.class);

    @Reference
    private IStockTransferRpcService iStockTransferRpcService;

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
    private LocationService locationService;

    @Autowired
    private TaskInfoDao taskInfoDao;

    @Reference
    private ITaskRpcService iTaskRpcService;

    @POST
    @Path("viewLocation")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String viewLocation() throws BizCheckedException {
        Map<String, Object> params = RequestUtils.getRequest();
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            String locationCode = params.get("locationCode").toString();
            BaseinfoLocation location = locationRpcService.getLocationByCode(locationCode);
            if (location == null) {
                throw new BizCheckedException("2060012");
            }
            StockQuantCondition condition = new StockQuantCondition();
            condition.setLocationId(location.getLocationId());
            List<StockQuant> quantList = stockQuantRpcService.getQuantList(condition);
            if (quantList == null || quantList.isEmpty()) {
                throw new BizCheckedException("2550032");
            }
            StockQuant quant = quantList.get(0);
            result.put("locationId", location.getLocationId());
            result.put("locationCode", location.getLocationCode());
            result.put("itemId", quant.getItemId());
            result.put("itemName", itemRpcService.getItem(quant.getItemId()).getSkuName());
            result.put("lotId", quant.getLotId());
            condition.setItemId(quant.getItemId());
            condition.setLotId(quant.getLotId());
            condition.setReserveTaskId(0L);
            BigDecimal qty = stockQuantRpcService.getQty(condition);
            if (location.getRegionType().equals(LocationConstant.SPLIT_AREA) && location.getBinUsage().equals(BinUsageConstant.BIN_PICK_STORE)) {
                result.put("packName", "EA");
                result.put("uom", "EA");
                result.put("uomQty", qty);
            } else {
                result.put("packName", quant.getPackName());
                result.put("uom", quant.getPackName());
                result.put("uomQty", PackUtil.EAQty2UomQty(qty, quant.getPackName()));
            }
        } catch (BizCheckedException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JsonUtils.TOKEN_ERROR(e.getMessage());
        }
        return JsonUtils.SUCCESS(result);
    }

    @POST
    @Path("fetchTask")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String fetchTask() throws BizCheckedException {
        Map<String, Object> params = RequestUtils.getRequest();
        try {
            Long uid;
            try {
                uid = iSysUserRpcService.getSysUserById(Long.valueOf(RequestUtils.getHeader("uid"))).getUid();
            } catch (Exception e) {
                throw new BizCheckedException("2550013");
            }
            params.put("uid", uid);
            final Long taskId = iStockTransferRpcService.assign(uid);
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
            final String fromLocationCode = locationRpcService.getLocation(taskInfo.getFromLocationId()).getLocationCode();

            if (taskInfo.getStep()==0) {
                type = 1L;
                uomQty = taskInfo.getQty();
            } else {
                type = 2L;
                uomQty = taskInfo.getQtyDone();
            }
            return JsonUtils.SUCCESS(new HashMap<String, Object>() {
                {
                    put("type", type);
                    put("taskId", taskId.toString());
                    put("locationCode", fromLocationCode);
                    put("itemId", taskInfo.getItemId());
                    put("itemName", itemRpcService.getItem(taskInfo.getItemId()).getSkuName());
                    put("packName", taskInfo.getPackName());
                    put("uom", taskInfo.getPackName());
                    put("uomQty", taskInfo.getSubType().compareTo(1L) == 0 ? "整托" : uomQty);
                    put("subType", taskInfo.getSubType());
                }
            });
        } catch (BizCheckedException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JsonUtils.TOKEN_ERROR(e.getMessage());
        }
    }

    @POST
    @Path("scanLocation")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String scanLocation() throws BizCheckedException {
        Map<String, Object> params = RequestUtils.getRequest();
        Map<String, Object> result;
        Long type = Long.valueOf(params.get("type").toString());
        BigDecimal uomQty = null;

        logger.info(params.toString());
        Long uid;
        try {
            uid = iSysUserRpcService.getSysUserById(Long.valueOf(RequestUtils.getHeader("uid"))).getUid();
        } catch (Exception e) {
            throw new BizCheckedException("2550013");
        }
        String locationCode = params.get("locationCode").toString();
        BaseinfoLocation location = locationRpcService.getLocationByCode(locationCode);
        params.put("uid", uid);
        Long taskId = Long.valueOf(params.get("taskId").toString());
        if(taskId == 0 && type == 1){
            //创建一个任务
            uomQty = new BigDecimal(params.get("uomQty").toString());
            StockTransferPlan plan = new StockTransferPlan();
            if (location == null) {
                throw new BizCheckedException("2060012");
            }
            plan.setFromLocationId(location.getLocationId());
            plan.setToLocationId(0L);
            plan.setTargetDesc("");
            plan.setUomQty(uomQty);
            plan.setPackName(params.get("uom").toString());
            String barCode = params.get("barcode").toString();
            CsiSku csiSku = itemRpcService.getSkuByCode(CsiConstan.CSI_CODE_TYPE_BARCODE, barCode);
            if (csiSku == null) {
                throw new BizCheckedException("2550032");
            }
            StockQuantCondition condition = new StockQuantCondition();
            condition.setLocationId(location.getLocationId());
            condition.setSkuId(csiSku.getSkuId());
            List<StockQuant> quantList = stockQuantRpcService.getQuantList(condition);
            if (quantList == null || quantList.isEmpty()) {
                throw new BizCheckedException("2550032");
            }
            StockQuant quant = quantList.get(0);
            plan.setItemId(quant.getItemId());
            Long subType = 2L;
            if (location.getRegionType().equals(LocationConstant.SPLIT_AREA)) {
                subType = 3L;
            }
            plan.setSubType(subType);
            taskId = iStockTransferRpcService.addPlan(plan);
            iTaskRpcService.assign(taskId, uid);
        }
        final TaskEntry taskEntry = taskRpcService.getTaskEntryById(taskId);
        if (taskEntry == null) {
            throw new BizCheckedException("3040001");
        }
        if (!taskEntry.getTaskInfo().getType().equals(TaskConstant.TYPE_STOCK_TRANSFER)) {
            throw new BizCheckedException("2550021");
        }
        Map<String, Object> next = new HashMap<String, Object>();
        if (type.equals(1L)) {
            uomQty = new BigDecimal(params.get("uomQty").toString());
            iStockTransferRpcService.scanFromLocation(taskEntry, location, uomQty);
            final TaskInfo taskInfo = taskEntry.getTaskInfo();
            if(taskInfo.getStatus() == TaskConstant.Cancel){
                next.put("response", true);
            }else{
                final String toLocationDesc = String.format("%s%s",
                        taskInfo.getToLocationId()!=0?locationRpcService.getLocation(taskInfo.getToLocationId()).getLocationCode():"",
                        taskInfo.getExt9());
                next.put("type", 2L);
                next.put("taskId", taskId.toString());
                next.put("locationCode", toLocationDesc);
                next.put("itemId", taskInfo.getItemId());
                next.put("itemName", itemRpcService.getItem(taskInfo.getItemId()).getSkuName());
                next.put("packName", taskInfo.getPackName());
                next.put("uomQty", taskInfo.getSubType().compareTo(1L) == 0 ? "整托" : taskInfo.getQtyUom());
                next.put("subType", taskInfo.getSubType());
            }
        } else {
            iStockTransferRpcService.scanToLocation(taskEntry, location);
            next.put("response", true);
        }
        return JsonUtils.SUCCESS(next);
    }
}