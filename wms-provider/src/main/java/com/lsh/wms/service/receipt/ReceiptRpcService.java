package com.lsh.wms.service.receipt;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.config.PropertyUtils;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.api.model.po.ReceiptItem;
import com.lsh.wms.api.model.po.ReceiptRequest;
import com.lsh.wms.api.service.location.ILocationRpcService;
import com.lsh.wms.api.service.po.IReceiptRpcService;
import com.lsh.wms.api.service.stock.IStockLotRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.*;
import com.lsh.wms.core.service.container.ContainerService;
import com.lsh.wms.core.service.csi.CsiSkuService;
import com.lsh.wms.core.service.item.ItemLocationService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.location.LocationDetailService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.po.PoOrderService;
import com.lsh.wms.core.service.po.PoReceiptService;
import com.lsh.wms.core.service.so.SoDeliveryService;
import com.lsh.wms.core.service.staff.StaffService;
import com.lsh.wms.core.service.stock.StockLotService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.utils.IdGenerator;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoItemLocation;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.baseinfo.BaseinfoLocationRegion;
import com.lsh.wms.model.csi.CsiSku;
import com.lsh.wms.model.po.InbPoDetail;
import com.lsh.wms.model.po.InbPoHeader;
import com.lsh.wms.model.po.InbReceiptDetail;
import com.lsh.wms.model.po.InbReceiptHeader;
import com.lsh.wms.model.stock.StockLot;
import com.lsh.wms.model.stock.StockMove;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.transfer.StockTransferPlan;
import com.lsh.wms.service.inhouse.StockTransferRpcService;
import com.sun.xml.bind.v2.TODO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.math.BigDecimal.ROUND_HALF_EVEN;

/**
 * Project Name: lsh-wms
 * Created by fuhao
 * Date: 16/7/29
 * Time: 16/7/29.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.service.receipt.
 * desc:类功能描述
 */
@Service(protocol = "dubbo")
public class ReceiptRpcService implements IReceiptRpcService {

    private static Logger logger = LoggerFactory.getLogger(ReceiptRpcService.class);

    @Autowired
    private PoReceiptService poReceiptService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private PoOrderService poOrderService;

    @Reference
    private ILocationRpcService locationRpcService;


    @Autowired
    private CsiSkuService csiSkuService;

    @Reference
    private ITaskRpcService iTaskRpcService;

    @Autowired
    private ContainerService containerService;

    @Autowired
    private SoDeliveryService soDeliveryService;

    @Autowired
    private StockLotService stockLotService;

    @Autowired
    private ItemLocationService itemLocationService;

    @Autowired
    private StockTransferRpcService stockTransferRpcService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private LocationDetailService locationDetailService;

    @Autowired
    private StaffService staffService;

    @Autowired
    private IdGenerator idGenerator;

    public Boolean throwOrder(String orderOtherId) throws BizCheckedException {
        InbPoHeader inbPoHeader = new InbPoHeader();
        inbPoHeader.setOrderOtherId(orderOtherId);
        inbPoHeader.setOrderStatus(PoConstant.ORDER_THROW);
        poOrderService.updateInbPoHeaderByOrderOtherIdOrOrderId(inbPoHeader);
        return true;
    }

    public void insertOrder(ReceiptRequest request) throws BizCheckedException, ParseException {
        //初始化InbReceiptHeader
        InbReceiptHeader inbReceiptHeader = new InbReceiptHeader();
        ObjUtils.bean2bean(request, inbReceiptHeader);

        //设置receiptOrderId
        inbReceiptHeader.setReceiptOrderId(RandomUtils.genId());

        //设置托盘码,暂存区,分配库位;实际库位由他人写入
        // TODO: 16/8/19 退货类型的单据 虚拟容器,放入退货区
        //根据request中的orderOtherId查询InbPoHeader
        InbPoHeader inbPoHeader = poOrderService.getInbPoHeaderByOrderOtherId(request.getOrderOtherId());
        if (inbPoHeader == null) {
            throw new BizCheckedException("2020001");
        }
        //判断PO订单类型  虚拟容器,放入退货区
        Integer orderType = inbPoHeader.getOrderType();
        if(PoConstant.ORDER_TYPE_SO_BACK == orderType){
            //新增container
            Long containerId = containerService.createContainerByType(ContainerConstant.PALLET).getContainerId();
            inbReceiptHeader.setContainerId(containerId);
            // TODO: 16/8/19 设置退货区

            List<BaseinfoLocationRegion> lists = locationDetailService.getMarketReturnList(inbPoHeader.getOwnerUid());
            Long location = lists.get(0).getLocationId();
            inbReceiptHeader.setLocation(location);

        }else{
            BaseinfoLocation baseinfoLocation = locationRpcService.assignTemporary();
            inbReceiptHeader.setLocation(baseinfoLocation.getLocationId());// TODO: 16/7/20  暂存区信息

        }
        //设置InbReceiptHeader状态
        inbReceiptHeader.setReceiptStatus(BusiConstant.EFFECTIVE_YES);

        //设置InbReceiptHeader插入时间
        inbReceiptHeader.setInserttime(new Date());

        //初始化List<InbReceiptDetail>
        List<InbReceiptDetail> inbReceiptDetailList = new ArrayList<InbReceiptDetail>();
        List<InbPoDetail> updateInbPoDetailList = new ArrayList<InbPoDetail>();
        List<StockQuant> stockQuantList = new ArrayList<StockQuant>();
        List<StockLot> stockLotList = new ArrayList<StockLot>();
        List<Map<String, Object>> moveList = new ArrayList<Map<String, Object>>();

        Map<Long,Long> locationMap = new HashMap<Long, Long>();
        List<StockTransferPlan> planList = new ArrayList<StockTransferPlan>();

        String idKey = "task_" + TaskConstant.TYPE_PO.toString();
        Long taskId = idGenerator.genId(idKey, true, true);
        //Long taskId = RandomUtils.genId();

        if(PoConstant.ORDER_TYPE_SO_BACK == orderType){
            for(ReceiptItem receiptItem : request.getItems()){
                InbReceiptDetail inbReceiptDetail = new InbReceiptDetail();
                ObjUtils.bean2bean(receiptItem, inbReceiptDetail);

                //设置receiptOrderId
                inbReceiptDetail.setReceiptOrderId(inbReceiptHeader.getReceiptOrderId());
                inbReceiptDetail.setOrderOtherId(request.getOrderOtherId());
                //写入InbReceiptDetail中的OrderId
                inbReceiptDetail.setOrderId(inbPoHeader.getOrderId());

                //根据InbPoHeader中的OwnerUid及InbReceiptDetail中的SkuId获取Item
                BaseinfoItem baseinfoItem = itemService.getItem(inbPoHeader.getOwnerUid(), inbReceiptDetail.getSkuId());
                inbReceiptDetail.setItemId(baseinfoItem.getItemId());


                //根据OrderId及SkuId获取InbPoDetail
                InbPoDetail inbPoDetail = poOrderService.getInbPoDetailByOrderIdAndSkuId(inbReceiptDetail.getOrderId(), inbReceiptDetail.getSkuId());

                //写入InbReceiptDetail中的OrderQty
                inbReceiptDetail.setOrderQty(inbPoDetail.getOrderQty());

                InbPoDetail updateInbPoDetail = new InbPoDetail();
                updateInbPoDetail.setInboundQty(inbReceiptDetail.getInboundQty());
                updateInbPoDetail.setOrderId(inbReceiptDetail.getOrderId());
                updateInbPoDetail.setSkuId(inbReceiptDetail.getSkuId());
                updateInbPoDetailList.add(updateInbPoDetail);
                //inbReceiptDetailList.add(inbReceiptDetail);


                // TODO: 16/8/19 找原so单对应货品的批号,从出库单找
                /***
                 * skuId 商品码
                 * locationId 存储位id
                 * containerId 容器设备id
                 * qty 商品数量
                 * supplierId 货物供应商id
                 * ownerId 货物所属公司id
                 * inDate 入库时间
                 * expireDate 保质期失效时间
                 * itemId
                 *
                 */
                //查供应商、生产日期、失效日期
                Long lotId =
                        soDeliveryService.getOutbDeliveryDetail(Long.parseLong(inbPoHeader.getOrderOtherId()),baseinfoItem.getItemId()).getLotId();
                StockLot stockLot = stockLotService.getStockLotByLotId(lotId);
                stockLot.setIsOld(true);

                //将收货细单中的生产日期改为该lot下的生产日期。
                SimpleDateFormat format =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
                String d = format.format(stockLot.getProductDate());
                Date date = format.parse(d);
                inbReceiptDetail.setProTime(date);
                //将inbReceiptDetail填入inbReceiptDetailList中
                inbReceiptDetailList.add(inbReceiptDetail);


//                StockQuant quant = new StockQuant();
//                quant.setLotId(lotId);
//                quant.setPackUnit(inbPoDetail.getPackUnit());
//                quant.setSkuId(inbReceiptDetail.getSkuId());
//                quant.setItemId(inbReceiptDetail.getItemId());
//                quant.setLocationId(inbReceiptHeader.getLocation());
//                quant.setContainerId(inbReceiptHeader.getContainerId());
//                quant.setSupplierId(stockLot.getSupplierId());
//                quant.setOwnerId(inbPoHeader.getOwnerUid());
//                Date receiptTime = inbReceiptHeader.getReceiptTime();
//                quant.setInDate(receiptTime.getTime() / 1000);
//
//                quant.setExpireDate(stockLot.getExpireDate());
//                quant.setCost(inbPoDetail.getPrice());
//                BigDecimal inboundQty = inbReceiptDetail.getInboundQty();
//                // TODO: 16/8/22  qty只能传转换成基本单位的数量
//                BigDecimal qty = inboundQty.multiply(inbReceiptDetail.getPackUnit());
//                quant.setQty(qty);
//                //quant.setQty(inboundQty);
//                BigDecimal value = inbPoDetail.getPrice().multiply(inboundQty);
//                quant.setValue(value);
//                stockQuantList.add(quant);

                BigDecimal inboundQty = inbReceiptDetail.getInboundQty();

                //qty转化为ea
                BigDecimal qty = inboundQty.multiply(inbReceiptDetail.getPackUnit());

                StockMove move = new StockMove();
                move.setToLocationId(inbReceiptHeader.getLocation());
                move.setToContainerId(inbReceiptHeader.getContainerId());
                //move.setQty(inbReceiptDetail.getInboundQty());
                move.setQty(qty);
                move.setItemId(inbReceiptDetail.getItemId());
                move.setOperator(inbReceiptHeader.getStaffId());
                move.setTaskId(taskId);

                Map<String, Object> moveInfo = new HashMap<String, Object>();
                moveInfo.put("lot", stockLot);
                moveInfo.put("move", move);
                moveList.add(moveInfo);

                // TODO: 16/8/19 找货品对应的拣货位
                List<BaseinfoItemLocation> itemLocations = itemLocationService.getItemLocationList(baseinfoItem.getItemId());
                for(BaseinfoItemLocation itemLocation : itemLocations){
                    // TODO: 16/8/19  判断拣货位是否可用
                    BaseinfoLocation location = locationService.getLocation(itemLocation.getPickLocationid());

                    if((location.getCanUse().equals(1)) && location.getIsLocked().equals(0)){
                        locationMap.put(baseinfoItem.getItemId(),itemLocation.getPickLocationid());
                        break;
                    }
                }

                StockTransferPlan plan = new StockTransferPlan();
                plan.setItemId(baseinfoItem.getItemId());
                //返仓区Id
                plan.setFromLocationId(inbReceiptHeader.getLocation());
                plan.setToLocationId(locationMap.get(baseinfoItem.getItemId()));
                //// TODO: 16/8/20 数量
                plan.setUomQty(inboundQty);


                planList.add(plan);
                //stockTransferRpcService.addPlan(plan);

            }

        } else{
            for(ReceiptItem receiptItem : request.getItems()){

                if(System.currentTimeMillis() - receiptItem.getProTime().getTime() <= 0) {
                    throw new BizCheckedException("2020009");
                }

                if(receiptItem.getInboundQty().compareTo(new BigDecimal(0)) <= 0) {
                    throw new BizCheckedException("2020007");
                }

                if(!containerService.isContainerCanUse(inbReceiptHeader.getContainerId())){
                    throw new BizCheckedException("2000002");
                }

                InbReceiptDetail inbReceiptDetail = new InbReceiptDetail();

                ObjUtils.bean2bean(receiptItem, inbReceiptDetail);

                //设置receiptOrderId
                inbReceiptDetail.setReceiptOrderId(inbReceiptHeader.getReceiptOrderId());
                inbReceiptDetail.setOrderOtherId(request.getOrderOtherId());
                boolean isCanReceipt = inbPoHeader.getOrderStatus() == PoConstant.ORDER_THROW || inbPoHeader.getOrderStatus() == PoConstant.ORDER_RECTIPT_PART || inbPoHeader.getOrderStatus() == PoConstant.ORDER_RECTIPTING;
                if (!isCanReceipt) {
                    throw new BizCheckedException("2020002");
                }

                //写入InbReceiptDetail中的OrderId
                inbReceiptDetail.setOrderId(inbPoHeader.getOrderId());


                //根据InbPoHeader中的OwnerUid及InbReceiptDetail中的SkuId获取Item
                CsiSku csiSku = csiSkuService.getSkuByCode(CsiConstan.CSI_CODE_TYPE_BARCODE, inbReceiptDetail.getBarCode());
                if (null == csiSku || csiSku.getSkuId() == null) {
                    throw new BizCheckedException("2020004");
                }
                inbReceiptDetail.setSkuId(csiSku.getSkuId());
                BaseinfoItem baseinfoItem = itemService.getItem(inbPoHeader.getOwnerUid(), csiSku.getSkuId());
                inbReceiptDetail.setItemId(baseinfoItem.getItemId());

                //根据OrderId及SkuId获取InbPoDetail
                InbPoDetail inbPoDetail = poOrderService.getInbPoDetailByOrderIdAndSkuId(inbReceiptDetail.getOrderId(), inbReceiptDetail.getSkuId());

                //写入InbReceiptDetail中的OrderQty
                inbReceiptDetail.setOrderQty(inbPoDetail.getOrderQty());

                // 判断是否超过订单总数
                BigDecimal poInboundQty = null != inbPoDetail.getInboundQty() ? inbPoDetail.getInboundQty() : new BigDecimal(0);

                if (poInboundQty.add(inbReceiptDetail.getInboundQty()).compareTo(inbPoDetail.getOrderQty()) > 0) {
                    throw new BizCheckedException("2020005");
                }

                //取出是否检验保质期字段 exceptionReceipt = 0 校验 = 1不校验
                Integer exceptionReceipt = inbPoDetail.getExceptionReceipt();
                //调拨类型的单据不校验保质期
                if(exceptionReceipt != 1 && (PoConstant.ORDER_TYPE_TRANSFERS == orderType)){
                    // TODO: 16/7/20   商品信息是否完善,怎么排查.2,保质期例外怎么验证?
                    //保质期判断,如果失败抛出异常
                    BigDecimal shelLife = baseinfoItem.getShelfLife();
                    String producePlace = baseinfoItem.getProducePlace();
                    Double shelLife_CN = Double.parseDouble(PropertyUtils.getString("shelLife_CN"));
                    Double shelLife_Not_CN = Double.parseDouble(PropertyUtils.getString("shelLife_Not_CN"));
                    String produceChina = PropertyUtils.getString("produceChina");
                    BigDecimal left_day = new BigDecimal(DateUtils.daysBetween(inbReceiptDetail.getProTime(), new Date()));
                    if (producePlace.contains(produceChina)) { // TODO: 16/7/20  产地是否存的是CN
                        if (left_day.divide(shelLife, 2, ROUND_HALF_EVEN).doubleValue() >= shelLife_CN) {
                            throw new BizCheckedException("2020003");
                        }
                    } else {
                        if (left_day.divide(shelLife, 2, ROUND_HALF_EVEN).doubleValue() > shelLife_Not_CN) {
                            throw new BizCheckedException("2020003");
                        }
                    }
                }
                InbPoDetail updateInbPoDetail = new InbPoDetail();
                updateInbPoDetail.setInboundQty(inbReceiptDetail.getInboundQty());
                updateInbPoDetail.setOrderId(inbReceiptDetail.getOrderId());
                updateInbPoDetail.setSkuId(inbReceiptDetail.getSkuId());
                updateInbPoDetailList.add(updateInbPoDetail);
                inbReceiptDetailList.add(inbReceiptDetail);


                /***
                 * skuId 商品码
                 * locationId 存储位id
                 * containerId 容器设备id
                 * qty 商品数量
                 * supplierId 货物供应商id
                 * ownerId 货物所属公司id
                 * inDate 入库时间
                 * expireDate 保质期失效时间
                 * itemId
                 *
                 */
                // TODO: 16/7/21  如何形成上架任务
//                Long lotId = RandomUtils.genId();
//
//                StockQuant quant = new StockQuant();
//                quant.setLotId(lotId);
//                quant.setPackUnit(inbPoDetail.getPackUnit());
//                quant.setSkuId(inbReceiptDetail.getSkuId());
//                quant.setItemId(inbReceiptDetail.getItemId());
//                quant.setLocationId(inbReceiptHeader.getLocation());
//                quant.setContainerId(inbReceiptHeader.getContainerId());
//                quant.setSupplierId(inbPoHeader.getSupplierCode());
//                quant.setOwnerId(inbPoHeader.getOwnerUid());
//                Date receiptTime = inbReceiptHeader.getReceiptTime();
//                quant.setInDate(receiptTime.getTime() / 1000);
//                Long expireDate = inbReceiptDetail.getProTime().getTime() + baseinfoItem.getShelfLife().longValue(); // 生产日期+保质期=保质期失效时间
//                quant.setExpireDate(expireDate / 1000);
//                quant.setCost(inbPoDetail.getPrice());
//                BigDecimal inboundQty = inbReceiptDetail.getInboundQty();
//
//                // TODO: 16/8/22  qty只能传转换成基本单位的数量
//                BigDecimal qty = inboundQty.multiply(inbReceiptDetail.getPackUnit());
//                quant.setQty(qty);
//                //quant.setQty(inboundQty);
//                BigDecimal value = inbPoDetail.getPrice().multiply(inboundQty);
//                quant.setValue(value);
//                stockQuantList.add(quant);
//                // stockQuantService.create(quant);


                /***
                 * skuId         商品id
                 * serialNo      生产批次号
                 * inDate        入库时间
                 * productDate   生产时间
                 * expireDate    保质期失效时间
                 * itemId
                 * poId          采购订单
                 * receiptId     收货单
                 */
                Long lotId = RandomUtils.genId();
                Date receiptTime = inbReceiptHeader.getReceiptTime();

                //修改失效日期
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(inbReceiptDetail.getProTime());
                calendar.add(calendar.DAY_OF_YEAR,baseinfoItem.getShelfLife().intValue());
                Long expireDate = calendar.getTime().getTime()/1000;

                //Long expireDate = inbReceiptDetail.getProTime().getTime() + baseinfoItem.getShelfLife().longValue(); // 生产日期+保质期=保质期失效时间

                StockLot stockLot = new StockLot();
                stockLot.setLotId(lotId);
                stockLot.setPackUnit(inbPoDetail.getPackUnit());
                stockLot.setSkuId(inbReceiptDetail.getSkuId());
                stockLot.setSerialNo(inbReceiptDetail.getLotNum());
                stockLot.setItemId(inbReceiptDetail.getItemId());
                stockLot.setInDate(receiptTime.getTime() / 1000);
                stockLot.setProductDate(inbReceiptDetail.getProTime().getTime() / 1000);
                stockLot.setExpireDate(expireDate / 1000);
                stockLot.setReceiptId(inbReceiptHeader.getReceiptOrderId());
                stockLot.setPoId(inbReceiptDetail.getOrderId());
                stockLot.setSupplierId(inbPoHeader.getSupplierCode());
                stockLotList.add(stockLot);

                StockMove move = new StockMove();
                move.setFromLocationId(locationService.getLocationsByType(LocationConstant.SUPPLIER_AREA).get(0).getLocationId());
                move.setToLocationId(inbReceiptHeader.getLocation());
                move.setOperator(inbReceiptHeader.getStaffId());
                move.setToContainerId(inbReceiptHeader.getContainerId());
                //qty转化为ea
                BigDecimal qty = inbReceiptDetail.getInboundQty().multiply(inbReceiptDetail.getPackUnit());

                move.setQty(qty);
                move.setItemId(inbReceiptDetail.getItemId());
                move.setTaskId(taskId);

                Map<String, Object> moveInfo = new HashMap<String, Object>();
                moveInfo.put("lot", stockLot);
                moveInfo.put("move", move);
                moveList.add(moveInfo);

            }
        }

        //插入订单
        //poReceiptService.insertOrder(inbReceiptHeader, inbReceiptDetailList, updateInbPoDetailList,stockQuantList,stockLotList);
        poReceiptService.insertOrder(inbReceiptHeader, inbReceiptDetailList, updateInbPoDetailList, moveList);

        if(PoConstant.ORDER_TYPE_PO == orderType){
            TaskEntry taskEntry = new TaskEntry();
            TaskInfo taskInfo = new TaskInfo();
            taskInfo.setTaskId(taskId);
            taskInfo.setType(TaskConstant.TYPE_PO);
            taskInfo.setOrderId(inbReceiptHeader.getReceiptOrderId());
            taskInfo.setContainerId(inbReceiptHeader.getContainerId());
            taskInfo.setItemId(inbReceiptDetailList.get(0).getItemId());
            taskInfo.setOperator(inbReceiptHeader.getStaffId());
            taskEntry.setTaskInfo(taskInfo);
            taskId = iTaskRpcService.create(TaskConstant.TYPE_PO, taskEntry);
            iTaskRpcService.done(taskId);
        }else if(PoConstant.ORDER_TYPE_SO_BACK == orderType){
            for(StockTransferPlan plan : planList){
                BaseinfoItem item  =  itemService.getItem(plan.getItemId());
                Long skuId = item.getSkuId();
                InbPoDetail inbPoDetail = poOrderService.getInbPoDetailByOrderIdAndSkuId(inbPoHeader.getOrderId(),skuId);
                taskId = stockTransferRpcService.addPlan(plan);
                inbPoDetail.setTaskId(taskId);

                poOrderService.updateInbPoDetail(inbPoDetail);
            }
            //返仓单生成移库单之后 将状态改为收货完成
            inbPoHeader.setOrderStatus(PoConstant.ORDER_RECTIPT_ALL);
            poOrderService.updateInbPoHeader(inbPoHeader);

        }


       /* TaskEntry taskEntry = new TaskEntry();
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setType(TaskConstant.TYPE_SHELVE);
        taskInfo.setOrderId(inbReceiptHeader.getReceiptOrderId());
        taskEntry.setTaskInfo(taskInfo);
        iTaskRpcService.create(TaskConstant.TYPE_SHELVE, taskEntry);*/

    }


    public Boolean updateReceiptStatus(Map<String, Object> map) throws BizCheckedException {
        if(map.get("receiptId") == null || map.get("receiptStatus") == null) {
            throw new BizCheckedException("1020001", "参数不能为空");
        }

        if(!StringUtils.isInteger(String.valueOf(map.get("receiptId")))
                || !StringUtils.isInteger(String.valueOf(map.get("receiptStatus")))) {
            throw new BizCheckedException("1020002", "参数类型不正确");
        }

        InbReceiptHeader inbReceiptHeader = new InbReceiptHeader();
        inbReceiptHeader.setReceiptOrderId(Long.valueOf(String.valueOf(map.get("receiptId"))));
        inbReceiptHeader.setReceiptStatus(Integer.valueOf(String.valueOf(map.get("receiptStatus"))));

        poReceiptService.updateInbReceiptHeaderByReceiptId(inbReceiptHeader);

        return true;
    }


    public InbReceiptHeader getPoReceiptDetailByReceiptId(Long receiptId) throws BizCheckedException {
        if (receiptId == null) {
            throw new BizCheckedException("1020001", "参数不能为空");
        }

        InbReceiptHeader inbReceiptHeader = poReceiptService.getInbReceiptHeaderByReceiptId(receiptId);

        poReceiptService.fillDetailToHeader(inbReceiptHeader);

        return inbReceiptHeader;
    }

    public List<InbReceiptHeader> getPoReceiptDetailByOrderId(Long orderId) throws BizCheckedException {
        if (orderId == null) {
            throw new BizCheckedException("1020001", "参数不能为空");
        }
        List<InbReceiptDetail> inbReceiptDetailList = poReceiptService.getInbReceiptDetailListByOrderId(orderId);

        List<InbReceiptHeader> inbReceiptHeaderList = new ArrayList<InbReceiptHeader>();

        for (InbReceiptDetail inbReceiptDetail : inbReceiptDetailList) {
            InbReceiptHeader inbReceiptHeader = poReceiptService.getInbReceiptHeaderByReceiptId(inbReceiptDetail.getReceiptOrderId());

            // TODO:InbReceiptHeader与当前时间比较

            poReceiptService.fillDetailToHeader(inbReceiptHeader);

            inbReceiptHeaderList.add(inbReceiptHeader);
        }

        return inbReceiptHeaderList;
    }


    public Integer countInbPoReceiptHeader(Map<String, Object> params) {
        return poReceiptService.countInbReceiptHeader(params);
    }

    public List<InbReceiptHeader> getPoReceiptDetailList(Map<String, Object> params) {
        return poReceiptService.getInbReceiptHeaderList(params);
    }

    public void insertReceipt(Long orderId , Long staffId) throws BizCheckedException, ParseException {
        InbPoHeader inbPoHeader = poOrderService.getInbPoHeaderByOrderId(orderId);
        List<InbPoDetail> inbPoDetails = poOrderService.getInbPoDetailListByOrderId(orderId);
        ReceiptRequest request = new ReceiptRequest();

        request.setOrderOtherId(inbPoHeader.getOrderOtherId());
        Map<String , Object> map = new HashMap<String, Object>();
        map.put("staffId",staffId);
        request.setReceiptUser(staffService.getStaffList(map).get(0).getName());
        request.setWarehouseId(inbPoHeader.getWarehouseId());
        request.setStaffId(staffId);

        List<ReceiptItem> items = new ArrayList<ReceiptItem>();

        for(InbPoDetail inbPoDetail : inbPoDetails){
            ReceiptItem item = new ReceiptItem();
            item.setArriveNum(inbPoDetail.getOrderQty());
            item.setBarCode(inbPoDetail.getBarCode());
            item.setInboundQty(inbPoDetail.getOrderQty());
            item.setPackName(inbPoDetail.getPackName());
            item.setPackUnit(inbPoDetail.getPackUnit());
            item.setSkuId(inbPoDetail.getSkuId());
            item.setSkuName(inbPoDetail.getSkuName());
            items.add(item);
        }
        request.setItems(items);
        this.insertOrder(request);
    }
}
