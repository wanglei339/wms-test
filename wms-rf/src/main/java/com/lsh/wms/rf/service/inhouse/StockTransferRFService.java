package com.lsh.wms.rf.service.inhouse;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.csi.ICsiRpcService;
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
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.csi.CsiOwner;
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
import java.security.acl.Owner;
import java.util.*;

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

    @Reference
    private ICsiRpcService iCsiRpcService;

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
            if(params.get("barcode")!=null && params.get("barcode").toString().trim().length()>0){
                CsiSku csiSku = itemRpcService.getSkuByCode(CsiConstan.CSI_CODE_TYPE_BARCODE, params.get("barcode").toString().trim());
                if (csiSku == null) {
                    throw new BizCheckedException("2550032");
                }
                condition.setSkuId(csiSku.getSkuId());
            }
            if(params.get("ownerId")!=null && StringUtils.isNumeric(params.get("ownerId").toString().trim())){
                condition.setOwnerId(Long.valueOf(params.get("ownerId").toString().trim()));
            }
            List<StockQuant> quantList = stockQuantRpcService.getQuantList(condition);
            if (quantList == null || quantList.isEmpty()) {
                return JsonUtils.TOKEN_ERROR("查询不到商品库存");
            }
            //是否需要扫描商品码才能确定准确的商品.
            Set<Long> items = new HashSet<Long>();
            Set<Long> owners = new HashSet<Long>();
            for(StockQuant stockQuant : quantList){
                items.add(stockQuant.getItemId());
                owners.add(stockQuant.getOwnerId());
            }
            result.put("needBarcode", items.size() > 1 ? 1 : 0);
            result.put("needOwner", owners.size() > 1 ? 1 : 0);
            {
                StockQuant quant = quantList.get(0);
                result.put("locationId", location.getLocationId());
                result.put("locationCode", location.getLocationCode());
                result.put("itemId", quant.getItemId());
                BaseinfoItem item = itemRpcService.getItem(quant.getItemId());
                result.put("itemName", item.getSkuName());
                result.put("barcode", item.getCode());
                result.put("skuCode", item.getSkuCode());
                result.put("lotId", quant.getLotId());
                condition.setItemId(quant.getItemId());
                //condition.setLotId(quant.getLotId());
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
                result.put("owner", String.format("%d[%s]", quant.getOwnerId(), iCsiRpcService.getOwner(quant.getOwnerId()).getOwnerName()));
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
            BaseinfoLocation fromLocation =  locationRpcService.getLocation(taskInfo.getFromLocationId());
            final String fromLocationCode = fromLocation.getLocationCode();
            final String toLocationDesc = String.format("%s%s",
                    taskInfo.getToLocationId()!=0?locationRpcService.getLocation(taskInfo.getToLocationId()).getLocationCode():"",
                    taskInfo.getExt9());
            if (taskInfo.getStep()==1) {
                type = 1L;
                uomQty = PackUtil.EAQty2UomQty(taskInfo.getQty(), taskInfo.getPackName());

            } else {
                type = 2L;
                uomQty = PackUtil.EAQty2UomQty(taskInfo.getQtyDone(), taskInfo.getPackName());
            }
            //是否需要扫描托盘码才能做移出操作.
            final long needScanContainer = (taskInfo.getSubType() == 1 &&
                    (fromLocation.getRegionType() == LocationConstant.FLOOR
                            || fromLocation.getRegionType() == LocationConstant.BACK_AREA
                            || fromLocation.getRegionType() == LocationConstant.DEFECTIVE_AREA)) ? 1 : 0;
            final BaseinfoItem item = itemRpcService.getItem(taskInfo.getItemId());
            return JsonUtils.SUCCESS(new HashMap<String, Object>() {
                {
                    put("type", type);
                    put("taskId", taskId.toString());
                    put("locationCode", type == 1 ? fromLocationCode : toLocationDesc);
                    put("itemId", taskInfo.getItemId());
                    put("itemName", item.getSkuName());
                    put("barcode", item.getCode());
                    put("skuCode", item.getSkuCode());
                    put("packName", taskInfo.getPackName());
                    put("uom", taskInfo.getPackName());
                    put("uomQty", taskInfo.getSubType().compareTo(1L) == 0 ? "整托" : uomQty);
                    put("subType", taskInfo.getSubType());
                    put("needScanContainer", needScanContainer);
                    put("owner", String.format("%d[%s]",taskInfo.getOwnerId(),iCsiRpcService.getOwner(taskInfo.getOwnerId()).getOwnerName()));
                }
            });
        } catch (BizCheckedException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getCause()!=null ? e.getCause().getMessage():e.getMessage());
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
            if (location == null) {
                throw new BizCheckedException("2060012");
            }
            String barCode = params.get("barcode").toString();
            Long subType = params.get("subType")!=null ? Long.valueOf(params.get("subType").toString()) : 2L;
            StockTransferPlan plan = new StockTransferPlan();
            plan.setFromLocationId(location.getLocationId());
            plan.setToLocationId(0L);
            plan.setTargetDesc("");
            if(subType != 1) {
                uomQty = new BigDecimal(params.get("uomQty").toString());
                String uom = params.get("uom").toString();
                plan.setUomQty(uomQty);
                plan.setPackName(uom);
                if (location.getRegionType().equals(LocationConstant.SPLIT_AREA)) {
                    subType = 3L;
                }
            }
            CsiSku csiSku = itemRpcService.getSkuByCode(CsiConstan.CSI_CODE_TYPE_BARCODE, barCode);
            if (csiSku == null) {
                throw new BizCheckedException("2550032");
            }
            StockQuantCondition condition = new StockQuantCondition();
            condition.setLocationId(location.getLocationId());
            condition.setSkuId(csiSku.getSkuId());
            if(params.get("ownerId")!=null && StringUtils.isNumeric(params.get("ownerId").toString().trim())){
                Long ownerId = Long.valueOf(params.get("ownerId").toString().trim());
                condition.setOwnerId(ownerId);
            }
            List<StockQuant> quantList = stockQuantRpcService.getQuantList(condition);
            if (quantList == null || quantList.isEmpty()) {
                throw new BizCheckedException("2550032");
            }
            Set<Long> owners = new HashSet<Long>();
            for(StockQuant stockQuant : quantList){
                owners.add(stockQuant.getOwnerId());
            }
            if(owners.size()>1) {
                return JsonUtils.TOKEN_ERROR("需要输入正确货主编号");
            }
            StockQuant quant = quantList.get(0);
            plan.setItemId(quant.getItemId());
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
            TaskInfo taskInfo = taskEntry.getTaskInfo();
            logger.info(String.format("QTY DONE 2 %s", taskInfo.getQtyDone().toString()));
            uomQty = new BigDecimal(params.get("uomQty").toString());
            if(params.get("subType")!=null && StringUtils.isNumeric(params.get("subType").toString())){
                taskInfo.setSubType(Long.valueOf(params.get("subType").toString()));
            }
            BaseinfoLocation fromLocation =  locationRpcService.getLocation(taskInfo.getFromLocationId());
            if(taskInfo.getSubType() == 1 && (fromLocation.getRegionType() == LocationConstant.FLOOR
                    || fromLocation.getRegionType() == LocationConstant.BACK_AREA
                    || fromLocation.getRegionType() == LocationConstant.DEFECTIVE_AREA)){
                //虚拟区域,需要用一个托盘码
                //Long containerId = Long.valueOf(params.get("containerId").toString());
                //我TM的不支持该行了吧
                return JsonUtils.TOKEN_ERROR("当前不支持此区域的整托移动");
            }
            iStockTransferRpcService.scanFromLocation(taskEntry, location, uomQty);
            BaseinfoItem item = itemRpcService.getItem(taskInfo.getItemId());
            logger.info(String.format("QTY DONE 3 %s", taskInfo.getQtyDone().toString()));
            if(taskInfo.getStatus() == TaskConstant.Cancel){
                next.put("response", true);
            }else{
                String toLocationDesc = String.format("%s%s",
                        taskInfo.getToLocationId()!=0?locationRpcService.getLocation(taskInfo.getToLocationId()).getLocationCode():"",
                        taskInfo.getExt9());
                next.put("type", 2L);
                next.put("taskId", taskId.toString());
                next.put("locationCode", toLocationDesc);
                next.put("itemId", taskInfo.getItemId());
                next.put("itemName", item.getSkuName());
                next.put("barcode", item.getCode());
                next.put("skuCode", item.getSkuCode());
                next.put("packName", taskInfo.getSubType().compareTo(1L) == 0 ? "整托" : taskInfo.getPackName());
                next.put("uomQty", taskInfo.getSubType().compareTo(1L) == 0 ? "整托" : PackUtil.EAQty2UomQty(taskInfo.getQtyDone(), taskInfo.getPackName()));
                next.put("subType", taskInfo.getSubType());
                next.put("owner", String.format("%d[%s]",taskInfo.getOwnerId(),iCsiRpcService.getOwner(taskInfo.getOwnerId()).getOwnerName()));
            }
        } else {
            iStockTransferRpcService.scanToLocation(taskEntry, location);
            next.put("response", true);
        }
        return JsonUtils.SUCCESS(next);
    }
}
