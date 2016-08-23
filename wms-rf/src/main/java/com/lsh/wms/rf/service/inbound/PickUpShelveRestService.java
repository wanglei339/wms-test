package com.lsh.wms.rf.service.inbound;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.api.service.inhouse.IProcurementRpcService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.shelve.IAtticShelveRfRestService;
import com.lsh.wms.api.service.shelve.IPickUpShelveRfRestService;
import com.lsh.wms.api.service.shelve.IShelveRpcService;
import com.lsh.wms.api.service.system.ISysUserRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.container.ContainerService;
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
 * Created by wuhao on 16/8/16.
 */
@Service(protocol = "rest")
@Path("inbound/pick_up_shelve")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class PickUpShelveRestService implements IPickUpShelveRfRestService {
    private static Logger logger = LoggerFactory.getLogger(PickUpShelveRestService.class);
    @Reference
    private ITaskRpcService iTaskRpcService;
    @Reference
    private IShelveRpcService shelveRpcService;
    @Reference
    private IProcurementRpcService rpcService;
    @Autowired
    private BaseinfoLocationBinService locationBinService;
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
    private StockLotService lotService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private ContainerService containerService;
    @Reference
    private ISysUserRpcService iSysUserRpcService;

    private Long taskType = TaskConstant.TYPE_PICK_UP_SHELVE;

    /**
     * 创建上架任务
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("createTask")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
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
        taskInfo.setSubType(1L);
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
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String scanContainer() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        Long uId=0L;
        Long containerId = 0L;
        Long taskId = 0L;
        try {
            uId = Long.valueOf(mapQuery.get("uId").toString());
            containerId = Long.valueOf(mapQuery.get("containerId").toString());
            taskId = baseTaskService.getDraftTaskIdByContainerId(containerId);
        }catch (Exception e) {
            logger.error(e.getMessage());
            return JsonUtils.TOKEN_ERROR("参数传递格式有误");
        }

        SysUser user =  iSysUserRpcService.getSysUserById(uId);
        if(user==null){
            return JsonUtils.TOKEN_ERROR("用户不存在");
        }
        // 检查是否有已分配的任务
        if (taskId == null) {
            //查看是否有已经执行的任务
            taskId = baseTaskService.getAssignTaskIdByContainerId(containerId);
            if(taskId==null) {
                throw new BizCheckedException("2030008");
            }
            TaskEntry entry = iTaskRpcService.getTaskEntryById(taskId);
            TaskInfo info = entry.getTaskInfo();
            if (info.getOperator().compareTo(user.getStaffId()) != 0 && baseTaskService.checkTaskByContainerId(containerId)) {
                return JsonUtils.TOKEN_ERROR("该上架任务已被人领取");
            }
            AtticShelveTaskDetail detail = shelveTaskService.getDetailByTaskIdAndStatus(taskId, 1L);
            if(detail==null){
                return JsonUtils.TOKEN_ERROR("任务详情异常");
            }
            BaseinfoLocation location = locationService.getLocation(detail.getAllocLocationId());
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("taskId", taskId);
            map.put("locationId", location.getLocationId());
            map.put("locationCode", location.getLocationCode());
            map.put("qty", detail.getQty().divide(info.getPackUnit(), BigDecimal.ROUND_HALF_EVEN));
            map.put("packName", info.getPackName());
            return JsonUtils.SUCCESS(map);
        }else {

            Map result = this.getResultMap(taskId);
            if (result == null) {
                return JsonUtils.TOKEN_ERROR("阁楼上架库存异常 ");
            } else {
                iTaskRpcService.assign(taskId, user.getStaffId());
                return JsonUtils.SUCCESS(result);
            }
        }
    }

    /**
     * 扫描上架目标location_id
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("scanTargetLocation")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
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
        if(location==null || realLocation ==null){
            return JsonUtils.TOKEN_ERROR("库位不存在");
        }

        AtticShelveTaskDetail detail = shelveTaskService.getShelveTaskDetail(taskId,allocLocationId);
        //TODO 判断扫描库位是不是存储合一库位
        if(realLocation.getType().compareTo(LocationConstant.LOFT_STORE_BIN)==0 ){
            if(locationService.checkLocationUseStatus(realLocationId) && realLocationId.compareTo(allocLocationId)!=0 ){
                return JsonUtils.TOKEN_ERROR("扫描库位已被占用");
            }
            if(location.getType().compareTo((LocationConstant.LOFT_STORE_BIN))!=0){
                return JsonUtils.TOKEN_ERROR("提供扫描库位类型不符");
            }


            if(detail ==null){
                return JsonUtils.TOKEN_ERROR("系统库位参数错误");
            }
            detail.setRealQty(realQty.multiply(quant.getPackUnit()));
            detail.setRealLocationId(realLocationId);
            detail.setShelveAt(DateUtils.getCurrentSeconds());
            detail.setOperator(info.getOperator());
            detail.setStatus(2L);
        }else {
            return JsonUtils.TOKEN_ERROR("提供扫描库位类型不符");
        }

        //移动库存
        List<StockQuant> pickQuant = stockQuantService.getQuantsByLocationId(realLocationId);
        Long containerId = 0L;
        if(pickQuant ==null ||pickQuant.size() ==0){
            containerId = containerService.createContainerByType(1L).getContainerId();
        }else {
            containerId = pickQuant.get(0).getContainerId();
        }
        StockMove move = new StockMove();
        ObjUtils.bean2bean(quant, move);
        move.setFromLocationId(quant.getLocationId());
        move.setToLocationId(realLocationId);
        move.setQty(realQty.multiply(quant.getPackUnit()));
        move.setFromContainerId(quant.getContainerId());
        move.setToContainerId(containerId);
        stockQuantService.move(move);
        locationService.unlockLocation(move.getToLocationId());
        shelveTaskService.updateDetail(detail);


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


    private Map getResultMap(Long taskId) {
        TaskEntry entry = iTaskRpcService.getTaskEntryById(taskId);
        if (entry == null) {
            return null;
        }
        TaskInfo info = entry.getTaskInfo();
        // 获取quant
        List<StockQuant> quants = stockQuantService.getQuantsByContainerId(info.getContainerId());
        if (quants.size() < 1) {
            return null;
        }
        StockQuant quant = quants.get(0);

        BigDecimal total = stockQuantService.getQuantQtyByLocationIdAndItemId(quant.getLocationId(), quant.getItemId());

        //将上架货物存到阁楼存货位上
        BaseinfoItem item = itemService.getItem(quant.getItemId());
        List<AtticShelveTaskDetail> details = new ArrayList<AtticShelveTaskDetail>();
        BigDecimal bulk = BigDecimal.ONE;
        //计算包装单位的体积
        bulk = bulk.multiply(item.getPackLength());
        bulk = bulk.multiply(item.getPackHeight());
        bulk = bulk.multiply(item.getPackWidth());



        while (total.compareTo(BigDecimal.ZERO) > 0) {
            //TODO 获取存储合一货位
            BaseinfoLocation location = locationService.getlocationIsEmptyAndUnlockByType(LocationConstant.LOFT_STORE_BIN);
            if(location==null) {
                throw new BizCheckedException("2030015");
            }

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
        return null;
    }
}
