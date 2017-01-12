package com.lsh.wms.service.sms;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.alibaba.fastjson.JSON;
import com.baidubce.util.DateUtils;
import com.fasterxml.jackson.databind.deser.Deserializers;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.BeanMapTransUtils;
import com.lsh.wms.api.model.so.ObdDetail;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.sms.ISmsRestService;
import com.lsh.wms.api.service.stock.IStockQuantRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.ContainerConstant;
import com.lsh.wms.core.constant.StockConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.dao.redis.RedisSortedSetDao;
import com.lsh.wms.core.dao.stock.StockSummaryDao;
import com.lsh.wms.core.dao.task.TaskInfoDao;
import com.lsh.wms.core.service.container.ContainerService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.stock.*;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.core.service.task.MessageService;
import com.lsh.wms.model.baseinfo.BaseinfoContainer;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.so.ObdHeader;
import com.lsh.wms.model.stock.StockLot;
import com.lsh.wms.model.stock.StockMove;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.stock.StockSummary;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.service.so.SoRpcService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

@Service(protocol = "rest")
@Path("sms")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class SmsRestService implements ISmsRestService {

    @Autowired
    private TaskInfoDao taskInfoDao;

    @Autowired
    private StockQuantService stockQuantService;

    @Autowired
    private ContainerService containerService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private SmsService smsService;

    @Autowired
    private StockMoveService moveService;

    @Autowired
    private StockQuantService quantService;

    @Autowired
    private StockSummaryDao stockSummaryDao;

    @Autowired
    private StockSummaryService stockSummaryService;

    @Autowired
    private SoOrderService soOrderService;

    @Autowired
    private SoRpcService soRpcService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private StockMoveService stockMoveService;

    @Autowired
    private BaseTaskService baseTaskService;

    @Reference
    private ITaskRpcService iTaskRpcService;

    public void setSmsService(SmsService smsService) {
        this.smsService = smsService;
    }


    @GET
    @Path("sendMsg")
    public String sendMsg (@QueryParam("item_id") String itemId,
                          @QueryParam("location_code") String locationCode) throws BizCheckedException  {
        soRpcService.eliminateDiff(Long.valueOf(itemId));
        return JsonUtils.SUCCESS();
    }

    @GET
    @Path("inventory")
    public String inventory(@QueryParam("item_id") Long itemId,
                            @QueryParam("from_location_id") Long fromLocationId,
                            @QueryParam("to_location_id") Long toLocationId,
                            @QueryParam("qty")BigDecimal qty) throws BizCheckedException {
        StockMove move = new StockMove();
        move.setItemId(itemId);
        move.setFromLocationId(fromLocationId);
        move.setToLocationId(toLocationId);
        move.setQty(qty);
        moveService.move(move);
        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("initTask")
    public String initTask() throws BizCheckedException {
        Map<String, Object> map = RequestUtils.getRequest();
        Long uid = Long.valueOf(map.get("uid").toString());
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setOperator(uid);
        taskInfo.setTaskId(0L);
        taskInfo.setType(99L);
        taskInfo.setCreatedAt(com.lsh.base.common.utils.DateUtils.getCurrentSeconds());
        taskInfo.setUpdatedAt(com.lsh.base.common.utils.DateUtils.getCurrentSeconds());
        taskInfoDao.insert(taskInfo);
        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("initStock")
    public String initStock() throws BizCheckedException {
        Map<String, Object> map = RequestUtils.getRequest();
        // 获取商品信息
        String skuCode = map.get("skuCode").toString();
        Long owenrId = Long.valueOf(map.get("ownerId").toString());
        BaseinfoItem item = itemService.getItemsBySkuCode(owenrId, skuCode);
        if (item == null) {
            throw new BizCheckedException("2900001");
        }

        // 初始化lot
        StockLot lot = new StockLot();
        lot.setItemId(item.getItemId());
        lot.setSkuId(item.getSkuId());
        lot.setPackName(item.getPackName());
        lot.setPackUnit(item.getPackUnit());
        lot.setCode(item.getCode());
        lot.setInDate(com.lsh.base.common.utils.DateUtils.getCurrentSeconds());
        lot.setExpireDate(Long.valueOf(map.get("expireDate").toString()));
        // 计算失效日期
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(lot.getExpireDate() * 1000));
        calendar.roll(calendar.DAY_OF_YEAR, 0 - item.getShelfLife().intValue());
        Long productDate = calendar.getTime().getTime() / 1000;
        lot.setProductDate(productDate);

        // 初始化move
        StockMove move = new StockMove();
        move.setItemId(item.getItemId());
        move.setFromLocationId(locationService.getNullArea().getLocationId());
        BaseinfoLocation locatioin = locationService.getLocationByCode(map.get("locationCode").toString());
        if (locatioin == null) {
            throw new BizCheckedException("2030013");
        }
        move.setToLocationId(locatioin.getLocationId());
        List<Long> containerList = stockQuantService.getContainerIdByLocationId(move.getToLocationId());
        if (CollectionUtils.isEmpty(containerList)) {
            BaseinfoContainer container = containerService.createContainerByType(ContainerConstant.PALLET);
            move.setToContainerId(container.getContainerId());
        } else if (containerList.size() == 1) {
            move.setToContainerId(containerList.get(0));
        } else {
            throw new BizCheckedException("3550002");
        }
        move.setQty(new BigDecimal(map.get("qty").toString()));
        move.setOwnerId(item.getOwnerId());
        move.setSkuId(item.getSkuId());
        move.setTaskId(0L);
        move.setOperator(Long.valueOf(map.get("uid").toString()));

        move.setLot(lot);
        List<StockMove> moveList = Arrays.asList(move);
        stockMoveService.move(moveList);
        return JsonUtils.SUCCESS();
    }




    @GET
    @Path("correctAvailQty")
    public String correctAvailQty() throws BizCheckedException {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        List<BaseinfoItem> itemList = itemService.searchItem(mapQuery);
        for (BaseinfoItem item : itemList) {
            Map<String, Object> quantMapQuery = new HashMap<String, Object>();
            quantMapQuery.put("itemId", item.getItemId());
            List<StockQuant> stockQuants = quantService.getQuants(quantMapQuery);
            for (StockQuant quant : stockQuants) {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("itemId", item.getItemId());
                params.put("skuCode", item.getSkuCode());
                params.put("ownerId", item.getOwnerId());
                params.put(StockConstant.REGION_TO_FIELDS.get(locationService.getLocation(quant.getLocationId()).getRegionType()), quant.getQty());
                StockSummary summary = BeanMapTransUtils.map2Bean(params, StockSummary.class);
                summary.setCreatedAt(com.lsh.base.common.utils.DateUtils.getCurrentSeconds());
                summary.setUpdatedAt(com.lsh.base.common.utils.DateUtils.getCurrentSeconds());
                stockSummaryDao.changeStock(summary);
            }
        }
        return JsonUtils.SUCCESS();
    }

    @GET
    @Path("so")
    public String alloc(@QueryParam("order_id") String orderId) throws BizCheckedException {
        List<StockMove> moveList = new ArrayList<StockMove>();
        StockMove move1 = new StockMove();
        move1.setItemId(127869766337950L);
        move1.setFromLocationId(locationService.getSupplyArea().getLocationId());
        move1.setToLocationId(locationService.getConsumerArea().getLocationId());
        move1.setToContainerId(0L);
        move1.setQty(BigDecimal.TEN);
        moveList.add(move1);

        StockMove move2 = new StockMove();
        move2.setItemId(1194000925153L);
        move2.setFromLocationId(locationService.getSupplyArea().getLocationId());
        move2.setToLocationId(locationService.getConsumerArea().getLocationId());
        move2.setToContainerId(0L);
        move2.setQty(BigDecimal.TEN);
        moveList.add(move2);

        stockMoveService.move(moveList);

        return JsonUtils.SUCCESS();
    }

    @GET
    @Path("diff")
    public String diff(@QueryParam("order_id") String orderId) throws BizCheckedException {
        return JsonUtils.SUCCESS();
    }

    @GET
    @Path("mergeLocationQuant")
    public String mergeLocationQuant(@QueryParam("locationId") String locationId) throws BizCheckedException {
        Long lid = Long.valueOf(locationId);
        List<StockQuant> quants = stockQuantService.getQuantsByLocationId(lid);
        Long fromContainerId = 0L;
        Long toContainerId = 0L;
        if (quants.size() > 1) {
            for (StockQuant quant: quants) {
                if (fromContainerId.equals(0L) && toContainerId.equals(0L)) {
                    toContainerId = quant.getContainerId();
                    continue;
                }
                fromContainerId = quant.getContainerId();
                if (!fromContainerId.equals(toContainerId)) {
                    stockMoveService.moveWholeContainer(fromContainerId, toContainerId, 0L, 1L, lid, lid);
                }
            }
        }
        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("taskDone")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String taskDone() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        List<Long> taskIds = JSON.parseArray(mapQuery.get("taskIds").toString(), Long.class);
        for (Long taskId: taskIds) {
            TaskInfo taskInfo = baseTaskService.getTaskInfoById(taskId);
            if (null == taskInfo) {
                throw new BizCheckedException("999", taskId.toString() + "不存在");
            }
            if (taskInfo.getStatus().equals(TaskConstant.Done)) {
                throw new BizCheckedException("999", taskId.toString() + "已完成");
            }
            iTaskRpcService.done(taskId);
        }
        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("moveByItemId")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String moveByItemId() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        List<Map> moveList = JSON.parseArray(mapQuery.get("moveList").toString(), Map.class);
        Long fromLocationId = Long.valueOf(mapQuery.get("fromLocationId").toString());
        Long toLocationId = Long.valueOf(mapQuery.get("toLocationId").toString());
        for (Map move: moveList) {
            Long itemId = Long.valueOf(move.get("itemId").toString());
            BaseinfoItem item = itemService.getItem(itemId);
            if (null == item) {
                throw new BizCheckedException("999", itemId.toString() + "商品不存在");
            }
            BigDecimal qty = new BigDecimal(move.get("qty").toString());
            StockMove stockMove = new StockMove();
            stockMove.setItemId(itemId);
            stockMove.setFromLocationId(fromLocationId);
            stockMove.setToLocationId(toLocationId);
            stockMove.setQty(qty);
            stockMoveService.move(stockMove);
        }
        return JsonUtils.SUCCESS();
    }
}
