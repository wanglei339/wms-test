package com.lsh.wms.service.inbound;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.api.service.inhouse.IProcurementRpcService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.shelve.IAtticShelveRestService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.item.ItemLocationService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.location.BaseinfoLocationBinService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.shelve.AtticShelveTaskDetailService;
import com.lsh.wms.core.service.stock.StockLotService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoItemLocation;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.baseinfo.BaseinfoLocationBin;
import com.lsh.wms.model.shelve.AtticShelveTaskDetail;
import com.lsh.wms.model.stock.StockLot;
import com.lsh.wms.model.stock.StockMove;
import com.lsh.wms.model.stock.StockQuant;
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
 * Created by wuhao on 16/8/16.
 */
@Service(protocol = "rest")
@Path("inbound/attic_shelve")
public class AtticShelveRestService implements IAtticShelveRestService{
    private static Logger logger = LoggerFactory.getLogger(AtticShelveRestService.class);
    @Reference
    private ITaskRpcService iTaskRpcService;
    @Reference
    private IProcurementRpcService rpcService;
    @Autowired
    private BaseinfoLocationBinService locationBinService;
    @Autowired
    private StockLotService lotService;
    @Autowired
    private BaseTaskService baseTaskService;
    @Autowired
    private StockQuantService stockQuantService;
    @Autowired
    private AtticShelveTaskDetailService shelveTaskService;
    @Autowired
    private ItemLocationService itemLocationService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private ItemService itemService;

    private Long taskType = TaskConstant.TYPE_ATTIC_SHELVE;

    /**
     * 创建上架任务
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("createTask")
    public String createTask() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        Long containerId = 0L;
        try {
            containerId = Long.valueOf(mapQuery.get("containerId").toString());
        }catch (Exception e){
            logger.error(e.getMessage());
            return JsonUtils.TOKEN_ERROR("参数传递格式有误");
        }

        // 检查容器信息
        if (containerId == null || containerId.equals("")) {
            throw new BizCheckedException("2030003");
        }
        // 检查该容器是否已创建过任务
        if (baseTaskService.checkTaskByContainerId(containerId)) {
            throw new BizCheckedException("2030008");
        }
        // 获取quant
        List<StockQuant> quants = stockQuantService.getQuantsByContainerId(containerId);
        if (quants.size() < 1) {
            throw new BizCheckedException("2030001");
        }
        StockQuant quant = quants.get(0);

        TaskInfo taskInfo = new TaskInfo();
        TaskEntry entry = new TaskEntry();

        ObjUtils.bean2bean(quant, taskInfo);

        taskInfo.setType(taskType);
        taskInfo.setFromLocationId(quant.getLocationId());

        entry.setTaskInfo(taskInfo);

        final Long taskId = iTaskRpcService.create(taskType, entry);


        return JsonUtils.SUCCESS(new HashMap<String, Object>() {
            {
                put("taskId", taskId);
            }
        });
    }

    /**
     * 扫描需上架的容器id
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("scanContainer")
    public String scanContainer() throws BizCheckedException {
        List<Map> list = new ArrayList<Map>();
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        Long staffId = 0L;
        Long containerId = 0L;
        Long taskId = 0L;
        try {
            staffId = Long.valueOf(mapQuery.get("operator").toString());
            containerId = Long.valueOf(mapQuery.get("containerId").toString());
            taskId = baseTaskService.getDraftTaskIdByContainerId(containerId);
        }catch (Exception e) {
            logger.error(e.getMessage());
            return JsonUtils.TOKEN_ERROR("参数传递格式有误");
        }

        // 检查是否有已分配的任务
        if (taskId == null && baseTaskService.checkTaskByContainerId(containerId)) {
            throw new BizCheckedException("2030008");
        }
        Map result = this.getResultMap(taskId);
        if(result == null) {
            return JsonUtils.TOKEN_ERROR("阁楼上架库存错误");
        }else {
            iTaskRpcService.assign(taskId, staffId);
            return JsonUtils.SUCCESS(result);
        }

    }

    /**
     * 扫描上架目标location_id
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("scanTargetLocation")
    public String scanTargetLocation() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        Long taskId = 0L;
        Long allocLocationId = 0L;
        Long realLocationId = 0L;
        BigDecimal realQty = BigDecimal.ZERO;
        try {
            taskId= Long.valueOf(mapQuery.get("taskId").toString());
            allocLocationId= Long.valueOf(mapQuery.get("allocLocationId").toString());
            realLocationId = Long.valueOf(mapQuery.get("realLocationId").toString());
            realQty = new BigDecimal(mapQuery.get("qty").toString());
        }catch (Exception e){
            logger.error(e.getMessage());
            return JsonUtils.TOKEN_ERROR("参数传递格式有误");
        }

        TaskEntry entry = iTaskRpcService.getTaskEntryById(taskId);
        if(entry==null){
            return JsonUtils.TOKEN_ERROR("任务不存在");
        }
        TaskInfo info = entry.getTaskInfo();
        // 获取quant
        List<StockQuant> quants = stockQuantService.getQuantsByContainerId(info.getContainerId());
        if (quants.size() < 1) {
            throw new BizCheckedException("2030001");
        }
        StockQuant quant = quants.get(0);

        BaseinfoLocation location = locationService.getLocation(allocLocationId);
        BaseinfoLocation realLocation = locationService.getLocation(realLocationId);
        if(location.getType().compareTo(LocationConstant.LOFT_PICKING_BIN)==0){
            if(realLocationId.compareTo(allocLocationId)!=0){
                return JsonUtils.TOKEN_ERROR("扫描货位与系统所提供货位不符");
            }

        }else if(realLocation.getType().compareTo(LocationConstant.LOFT_STORE_BIN)==0){
            if(locationService.isLocationInUse(realLocationId)){
                return JsonUtils.TOKEN_ERROR("扫描库位已被占用");
            }
            if(location.getType().compareTo((LocationConstant.LOFT_STORE_BIN))!=0){
                return JsonUtils.TOKEN_ERROR("提供扫描库位类型不符");
            }

            AtticShelveTaskDetail detail = shelveTaskService.getShelveTaskDetail(taskId,allocLocationId);

            if(detail ==null){
                return JsonUtils.TOKEN_ERROR("系统库位参数错误");
            }
            detail.setQty(realQty);
            detail.setRealLocationId(realLocationId);
            detail.setShelveAt(DateUtils.getCurrentSeconds());
            shelveTaskService.updateDetail(detail);

        }else {
            return JsonUtils.TOKEN_ERROR("提供扫描库位类型不符");
        }

        //移动库存
        StockMove move = new StockMove();
        ObjUtils.bean2bean(quant, move);
        move.setFromLocationId(quant.getLocationId());
        move.setToLocationId(realLocationId);
        move.setQty(realQty.multiply(quant.getPackUnit()));
        move.setFromContainerId(quant.getContainerId());
        stockQuantService.move(move);

        Map result = this.getResultMap(taskId);

        if(result==null) {
            iTaskRpcService.done(taskId);
            return JsonUtils.SUCCESS(new HashMap<String, Boolean>() {
                {
                    put("response", true);
                }
            });
        }else {
            return JsonUtils.SUCCESS(result);
        }
    }


    public Map getResultMap(Long taskId) {

        TaskEntry entry = iTaskRpcService.getTaskEntryById(taskId);
        if (entry == null) {
            return null;
        }
        TaskInfo info = entry.getTaskInfo();
        // 获取quant
        List<StockQuant> quants = stockQuantService.getQuantsByContainerId(info.getContainerId());
        if (quants.size() < 1) {
            throw new BizCheckedException("2030001");
        }
        StockQuant quant = quants.get(0);

        BigDecimal total = stockQuantService.getQuantQtyByLocationIdAndItemId(quant.getLocationId(), quant.getItemId());

        if (quants.size() < 1) {
            throw new BizCheckedException("2030001");
        }

        //对比货架商品和新进商品保质期是否到达阀值
        //TODO

        //判断阁楼捡货位是不是需要补货
        List<BaseinfoItemLocation> locations = itemLocationService.getItemLocationList(quant.getItemId());
        for (BaseinfoItemLocation itemLocation : locations) {
            BaseinfoLocation location = locationService.getLocation(itemLocation.getPickLocationid());
            if(location.getType().compareTo(LocationConstant.LOFT_PICKING_BIN)==0) {
                if (rpcService.needProcurement(itemLocation.getPickLocationid(), itemLocation.getItemId())) {
                    BigDecimal num = total.divide(quant.getPackUnit(), BigDecimal.ROUND_HALF_EVEN);
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("taskId", taskId);
                    map.put("locationId", location.getLocationId());
                    map.put("locationCode", location.getLocationCode());
                    map.put("qty", num.compareTo(new BigDecimal(3)) >= 0 ? 3 : num);
                    map.put("packName", quant.getPackName());
                    return map;
                }
            }
        }

        //当捡货位都不需要补货时，将上架货物存到阁楼存货位上
        //根据库位体积判断需要几个库位存
        BaseinfoItem item = itemService.getItem(quant.getItemId());
        List<AtticShelveTaskDetail> details = new ArrayList<AtticShelveTaskDetail>();
        BigDecimal bulk = BigDecimal.ONE;
        //计算包装单位的体积
        bulk = bulk.multiply(item.getPackLength());
        bulk = bulk.multiply(item.getPackHeight());
        bulk = bulk.multiply(item.getPackWidth());


        while (total.compareTo(BigDecimal.ZERO) > 0) {
            BaseinfoLocation location = locationService.getAvailableLocationByType("loft_store_bin");
            if (!locationService.isLocationInUse(location.getLocationId())) {
                //锁Location
                locationService.lockLocation(location.getLocationId());
                //插detail
                AtticShelveTaskDetail detail = new AtticShelveTaskDetail();
                StockLot lot = lotService.getStockLotByLotId(quant.getLotId());
                ObjUtils.bean2bean(quant, detail);
                detail.setTaskId(taskId);
                detail.setReceiptId(lot.getReceiptId());
                detail.setOrderId(lot.getPoId());
                detail.setAllocLocationId(location.getLocationId());
                detail.setRealLocationId(location.getLocationId());

                BaseinfoLocationBin bin = (BaseinfoLocationBin) locationBinService.getBaseinfoItemLocationModelById(location.getLocationId());
                //体积的80%为有效体积
                BigDecimal valum = bin.getVolume().multiply(new BigDecimal(0.8));
                BigDecimal num = valum.divide(bulk, BigDecimal.ROUND_HALF_EVEN);
                if (total.subtract(num).compareTo(BigDecimal.ZERO) >= 0) {
                    detail.setQty(num);
                } else {
                    detail.setQty(total);
                }
                total = total.subtract(num);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("taskId", taskId);
                map.put("locationId", location.getLocationId());
                map.put("locationCode", location.getLocationCode());
                map.put("qty", detail.getQty().divide(quant.getPackUnit(), BigDecimal.ROUND_HALF_EVEN));
                map.put("packName", quant.getPackName());
                shelveTaskService.create(detail);
                return map;
            }
        }
        return null;
    }
}
