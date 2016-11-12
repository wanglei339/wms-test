package com.lsh.wms.service.receipt;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.config.PropertyUtils;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.base.common.utils.StrUtils;
import com.lsh.wms.api.model.po.ReceiptItem;
import com.lsh.wms.api.model.po.ReceiptRequest;
import com.lsh.wms.api.model.so.ObdStreamDetail;
import com.lsh.wms.api.service.location.ILocationRpcService;
import com.lsh.wms.api.service.po.IReceiptRpcService;
import com.lsh.wms.api.service.store.IStoreRpcService;
import com.lsh.wms.api.service.system.IExceptionCodeRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.*;
import com.lsh.wms.core.dao.redis.RedisStringDao;
import com.lsh.wms.core.service.container.ContainerService;
import com.lsh.wms.core.service.csi.CsiCustomerService;
import com.lsh.wms.core.service.csi.CsiSkuService;
import com.lsh.wms.core.service.item.ItemLocationService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.location.LocationDetailService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.po.PoOrderService;
import com.lsh.wms.core.service.po.PoReceiptService;
import com.lsh.wms.core.service.po.ReceiveService;
import com.lsh.wms.core.service.so.SoDeliveryService;
import com.lsh.wms.core.service.staff.StaffService;
import com.lsh.wms.core.service.stock.StockLotService;
import com.lsh.wms.core.service.utils.IdGenerator;
import com.lsh.wms.model.baseinfo.*;
import com.lsh.wms.model.csi.CsiCustomer;
import com.lsh.wms.model.csi.CsiSku;
import com.lsh.wms.model.po.*;
import com.lsh.wms.model.stock.StockLot;
import com.lsh.wms.model.stock.StockMove;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.transfer.StockTransferPlan;
import com.lsh.wms.service.inhouse.StockTransferRpcService;
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

    @Reference
    private IStoreRpcService iStoreRpcService;

    @Reference
    private IExceptionCodeRpcService iexceptionCodeRpcService;

    @Autowired
    private RedisStringDao redisStringDao;

    @Autowired
    private ReceiveService receiveService;

    @Autowired
    private CsiCustomerService customerService;

    public Boolean throwOrder(String orderOtherId) throws BizCheckedException {
        IbdHeader ibdHeader = new IbdHeader();
        ibdHeader.setOrderOtherId(orderOtherId);
        ibdHeader.setOrderStatus(PoConstant.ORDER_THROW);
        poOrderService.updateInbPoHeaderByOrderOtherIdOrOrderId(ibdHeader);
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
        IbdHeader ibdHeader = poOrderService.getInbPoHeaderByOrderOtherId(request.getOrderOtherId());
        if (ibdHeader == null) {
            throw new BizCheckedException("2020001");
        }
        //判断PO订单类型  虚拟容器,放入退货区
        Integer orderType = ibdHeader.getOrderType();
        if(PoConstant.ORDER_TYPE_SO_BACK == orderType){
            //新增container
            Long containerId = containerService.createContainerByType(ContainerConstant.PALLET).getContainerId();
            inbReceiptHeader.setContainerId(containerId);
            // TODO: 16/8/19 设置退货区

            List<BaseinfoLocationRegion> lists = locationDetailService.getMarketReturnList(ibdHeader.getOwnerUid());
            Long location = lists.get(0).getLocationId();
            inbReceiptHeader.setLocation(location);
            inbReceiptHeader.setReceiptType(1);

            //设置收货类型
            inbReceiptHeader.setReceiptType(ReceiptContant.RECEIPT_TYPE_NORMAL);

        }else{
            BaseinfoLocation baseinfoLocation = locationRpcService.assignTemporary();
            inbReceiptHeader.setLocation(baseinfoLocation.getLocationId());// TODO: 16/7/20  暂存区信息
            if(PoConstant.ORDER_TYPE_CPO == orderType){
                inbReceiptHeader.setReceiptType(ReceiptContant.RECEIPT_TYPE_ORDER);
            }else{
                inbReceiptHeader.setReceiptType(ReceiptContant.RECEIPT_TYPE_NORMAL);
            }

        }

        // TODO: 2016/10/8 查询验收单是否存在,如果不存在,则根据ibd重新生成
        ReceiveHeader receiveHeader = receiveService.getReceiveHeader(ibdHeader.getOrderId());
        Long receiveId = 0l;
        if(receiveHeader == null){
            receiveId = this.genReceive(ibdHeader,request.getItems());
        }else{
            receiveId = receiveHeader.getReceiveId();
        }


        //设置InbReceiptHeader状态
        inbReceiptHeader.setReceiptStatus(BusiConstant.EFFECTIVE_YES);

        //设置InbReceiptHeader插入时间
        inbReceiptHeader.setInserttime(new Date());

        //初始化List<InbReceiptDetail>
        List<InbReceiptDetail> inbReceiptDetailList = new ArrayList<InbReceiptDetail>();
        List<IbdDetail> updateIbdDetailList = new ArrayList<IbdDetail>();
        List<StockLot> stockLotList = new ArrayList<StockLot>();
        List<Map<String, Object>> moveList = new ArrayList<Map<String, Object>>();
        //初始化验收单
        List<ReceiveDetail> updateReceiveDetailList = new ArrayList<ReceiveDetail>();

        //生成出库detail
        List<ObdStreamDetail> obdStreamDetailList = new ArrayList<ObdStreamDetail>();


        Map<Long,Long> locationMap = new HashMap<Long, Long>();
        List<StockTransferPlan> planList = new ArrayList<StockTransferPlan>();

        String idKey = "task_" + TaskConstant.TYPE_PO.toString();
        Long taskId = idGenerator.genId(idKey, true, true);

        if(PoConstant.ORDER_TYPE_SO_BACK == orderType){
            for(ReceiptItem receiptItem : request.getItems()){
                InbReceiptDetail inbReceiptDetail = new InbReceiptDetail();
                ObjUtils.bean2bean(receiptItem, inbReceiptDetail);

                //设置receiptOrderId
                inbReceiptDetail.setReceiptOrderId(inbReceiptHeader.getReceiptOrderId());
                inbReceiptDetail.setOrderOtherId(request.getOrderOtherId());
                //写入InbReceiptDetail中的OrderId
                inbReceiptDetail.setOrderId(ibdHeader.getOrderId());

                //根据InbPoHeader中的OwnerUid及InbReceiptDetail中的SkuId获取Item
                BaseinfoItem baseinfoItem = itemService.getItem(ibdHeader.getOwnerUid(), inbReceiptDetail.getSkuId());
                inbReceiptDetail.setItemId(baseinfoItem.getItemId());


                //根据OrderId及SkuCode获取InbPoDetail
                IbdDetail ibdDetail = poOrderService.getInbPoDetailByOrderIdAndSkuCode(inbReceiptDetail.getOrderId(), baseinfoItem.getSkuCode());

                //写入InbReceiptDetail中的OrderQty
                inbReceiptDetail.setOrderQty(ibdDetail.getOrderQty());

                IbdDetail updateIbdDetail = new IbdDetail();
                updateIbdDetail.setInboundQty(inbReceiptDetail.getInboundQty());
                updateIbdDetail.setOrderId(inbReceiptDetail.getOrderId());
                //updateIbdDetail.setSkuId(inbReceiptDetail.getSkuId());
                updateIbdDetail.setDetailOtherId(ibdDetail.getDetailOtherId());
                updateIbdDetailList.add(updateIbdDetail);
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
                        soDeliveryService.getOutbDeliveryDetail(Long.parseLong(ibdHeader.getOrderOtherId()),baseinfoItem.getItemId()).getLotId();
                StockLot stockLot = stockLotService.getStockLotByLotId(lotId);
                stockLot.setIsOld(true);

                //将收货细单中的生产日期改为该lot下的生产日期。
                SimpleDateFormat format =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
                String d = format.format(stockLot.getProductDate());
                Date date = format.parse(d);
                inbReceiptDetail.setProTime(date);
                //将inbReceiptDetail填入inbReceiptDetailList中
                inbReceiptDetailList.add(inbReceiptDetail);


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

                if(receiptItem.getInboundQty().compareTo(BigDecimal.ZERO) < 0) {
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
                boolean isCanReceipt = ibdHeader.getOrderStatus() == PoConstant.ORDER_THROW || ibdHeader.getOrderStatus() == PoConstant.ORDER_RECTIPT_PART || ibdHeader.getOrderStatus() == PoConstant.ORDER_RECTIPTING;
                if (!isCanReceipt) {
                    throw new BizCheckedException("2020002");
                }

                //写入InbReceiptDetail中的OrderId
                inbReceiptDetail.setOrderId(ibdHeader.getOrderId());


                //根据InbPoHeader中的OwnerUid及InbReceiptDetail中的SkuId获取Item
                CsiSku csiSku = csiSkuService.getSkuByCode(CsiConstan.CSI_CODE_TYPE_BARCODE, inbReceiptDetail.getBarCode());
                if (null == csiSku || csiSku.getSkuId() == null) {
                    throw new BizCheckedException("2020022");
                }
                inbReceiptDetail.setSkuId(csiSku.getSkuId());
                BaseinfoItem baseinfoItem = itemService.getItem(ibdHeader.getOwnerUid(), csiSku.getSkuId());
                inbReceiptDetail.setItemId(baseinfoItem.getItemId());

                //根据OrderId及SkuId获取InbPoDetail
                IbdDetail ibdDetail = poOrderService.getInbPoDetailByOrderIdAndSkuCode(inbReceiptDetail.getOrderId(), baseinfoItem.getSkuCode());

                //写入InbReceiptDetail中的OrderQty
                inbReceiptDetail.setOrderQty(ibdDetail.getOrderQty());

                // 判断是否超过订单总数
                BigDecimal poInboundQty = null != ibdDetail.getInboundQty() ? ibdDetail.getInboundQty() : new BigDecimal(0);

                if (poInboundQty.add(inbReceiptDetail.getInboundQty()).compareTo(ibdDetail.getOrderQty()) > 0) {
                    throw new BizCheckedException("2020005");
                }

                //取出是否检验保质期字段 exceptionReceipt = 0 校验 = 1不校验
                /*Integer exceptionReceipt = ibdDetail.getExceptionReceipt();
                //调拨类型的单据不校验保质期
                if(PoConstant.ORDER_TYPE_TRANSFERS != orderType){
                    if(exceptionReceipt != 1){
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
                }*/
                IbdDetail updateIbdDetail = new IbdDetail();
                updateIbdDetail.setInboundQty(inbReceiptDetail.getInboundQty());
                updateIbdDetail.setOrderId(inbReceiptDetail.getOrderId());
                //updateIbdDetail.setSkuId(inbReceiptDetail.getSkuId());
                updateIbdDetail.setDetailOtherId(ibdDetail.getDetailOtherId());
                updateIbdDetailList.add(updateIbdDetail);

                //根据receiveId及SkuCode获取receiveDetail
                ReceiveDetail receiveDetail = receiveService.getReceiveDetailByReceiveIdAndSkuCode(receiveId, baseinfoItem.getSkuCode());

                //批量修改receive 实收数量
                ReceiveDetail updateReceiveDetail = new ReceiveDetail();
                updateReceiveDetail.setDetailOtherId(receiveDetail.getDetailOtherId());
                updateReceiveDetail.setReceiveId(receiveDetail.getReceiveId());
                updateReceiveDetail.setInboundQty(inbReceiptDetail.getInboundQty());
                updateReceiveDetailList.add(updateReceiveDetail);

                if(ibdHeader.getOrderType() == PoConstant.ORDER_TYPE_CPO){
                    ObdStreamDetail obdStreamDetail = new ObdStreamDetail();
                    obdStreamDetail.setItemId(inbReceiptDetail.getItemId());
                    obdStreamDetail.setContainerId(inbReceiptHeader.getContainerId());
                    obdStreamDetail.setOwnerId(ibdHeader.getOwnerUid());
                    //统一放到pickQty中
                    obdStreamDetail.setPickQty(inbReceiptDetail.getInboundQty().multiply(inbReceiptDetail.getPackUnit()));
                    obdStreamDetail.setAllocUnitName(inbReceiptDetail.getPackName());
                    obdStreamDetail.setAllocUnitQty(inbReceiptDetail.getInboundQty());
                    obdStreamDetail.setSkuId(inbReceiptDetail.getSkuId());
                    obdStreamDetailList.add(obdStreamDetail);

                }
                inbReceiptDetailList.add(inbReceiptDetail);


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
                stockLot.setPackUnit(ibdDetail.getPackUnit());
                stockLot.setSkuId(inbReceiptDetail.getSkuId());
                stockLot.setSerialNo(inbReceiptDetail.getLotNum());
                stockLot.setItemId(inbReceiptDetail.getItemId());
                stockLot.setInDate(receiptTime.getTime() / 1000);
                stockLot.setProductDate(inbReceiptDetail.getProTime().getTime() / 1000);
                stockLot.setExpireDate(expireDate);
                stockLot.setReceiptId(inbReceiptHeader.getReceiptOrderId());
                stockLot.setPoId(inbReceiptDetail.getOrderId());
                stockLot.setSupplierId(ibdHeader.getSupplierCode());
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
        poReceiptService.insertOrder(inbReceiptHeader, inbReceiptDetailList, updateIbdDetailList, moveList,updateReceiveDetailList,obdStreamDetailList,request.getIsCreateTask());

        if(PoConstant.ORDER_TYPE_PO == orderType || PoConstant.ORDER_TYPE_TRANSFERS == orderType || PoConstant.ORDER_TYPE_CPO == orderType){
            TaskEntry taskEntry = new TaskEntry();
            TaskInfo taskInfo = new TaskInfo();
            taskInfo.setTaskId(taskId);
            taskInfo.setType(TaskConstant.TYPE_PO);
            //根据类型来决定任务的流向
            if(PoConstant.ORDER_TYPE_CPO == orderType){
                taskInfo.setSubType(TaskConstant.TASK_DIRECT);
                taskInfo.setBusinessMode(TaskConstant.MODE_DIRECT);
            }else{
                taskInfo.setBusinessMode(TaskConstant.MODE_INBOUND);
                taskInfo.setSubType(TaskConstant.TASK_INBOUND);
            }
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
                String skuCode = item.getSkuCode();
                IbdDetail ibdDetail = poOrderService.getInbPoDetailByOrderIdAndSkuCode(ibdHeader.getOrderId(),skuCode);
                taskId = stockTransferRpcService.addPlan(plan);
                ibdDetail.setTaskId(taskId);

                poOrderService.updateInbPoDetail(ibdDetail);
            }
            //返仓单生成移库单之后 将状态改为收货完成
            ibdHeader.setOrderStatus(PoConstant.ORDER_RECTIPT_ALL);
            poOrderService.updateInbPoHeader(ibdHeader);

        }


    }
    //验证生产日期
    public boolean checkProTime(BaseinfoItem baseinfoItem,Date proTime,Date dueTime,String exceptionCode) throws BizCheckedException{

        //超过保质期,保质期例外代码验证
        String proTimeexceptionCode = iexceptionCodeRpcService.getExceptionCodeByName("receiveExpired");// FIXME: 16/11/9 获取保质期的例外代码
        logger.info("#############proTimeexceptionCode:"+proTimeexceptionCode);
        logger.info("#############exceptionCode:"+exceptionCode);
        if(StringUtils.isNotEmpty(exceptionCode) && exceptionCode.equals(proTimeexceptionCode)){
            //例外代码匹配
            return true;
        }
        logger.info("#############");
        if(proTime == null && dueTime == null){
            throw new BizCheckedException("2020008");//生产日期不能为空
        }
        if(proTime != null && System.currentTimeMillis() - proTime.getTime() <= 0) {
            throw new BizCheckedException("2020009");
        }
        if(dueTime != null && System.currentTimeMillis() - dueTime.getTime() >= 0) {
            throw new BizCheckedException("2020102");//到期日期不能小于当前日期
        }
        BigDecimal shelLife = baseinfoItem.getShelfLife();
        String producePlace = baseinfoItem.getProducePlace();
        Double shelLife_CN = Double.parseDouble(PropertyUtils.getString("shelLife_CN"));
        Double shelLife_Not_CN = Double.parseDouble(PropertyUtils.getString("shelLife_Not_CN"));
        String produceChina = PropertyUtils.getString("produceChina");
        if(proTime != null){
            //根据生产日期判断
            BigDecimal left_day = new BigDecimal(DateUtils.daysBetween(proTime, new Date()));
            if (producePlace.contains(produceChina)){
                // TODO: 16/7/20  产地是否存的是CN
                if (left_day.divide(shelLife, 2, ROUND_HALF_EVEN).doubleValue() >= shelLife_CN) {
                    throw new BizCheckedException("2020003");
                }
            } else {
                if (left_day.divide(shelLife, 2, ROUND_HALF_EVEN).doubleValue() > shelLife_Not_CN) {
                    throw new BizCheckedException("2020003");
                }
            }
        }else if(dueTime != null){
            //根据到期日判断
            BigDecimal right_day = new BigDecimal(DateUtils.daysBetween(new Date(),dueTime));
            if (producePlace.contains(produceChina)){
                // TODO: 16/7/20  产地是否存的是CN
                if (right_day.divide(shelLife, 2, ROUND_HALF_EVEN).doubleValue() <= shelLife_CN) {
                    throw new BizCheckedException("2020003");
                }
            } else {
                if (right_day.divide(shelLife, 2, ROUND_HALF_EVEN).doubleValue() < shelLife_Not_CN) {
                    throw new BizCheckedException("2020003");
                }
            }
        }

        return true;
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


    public List<InbReceiptDetail> getInbReceiptDetailListByOrderId(Long orderId){
        if (orderId == null) {
            throw new BizCheckedException("1020001", "参数不能为空");
        }
        return poReceiptService.getInbReceiptDetailListByOrderId(orderId);
    }


    public Integer countInbPoReceiptHeader(Map<String, Object> params) {
        return poReceiptService.countInbReceiptHeader(params);
    }

    public List<InbReceiptHeader> getPoReceiptDetailList(Map<String, Object> params) {
        return poReceiptService.getInbReceiptHeaderList(params);
    }

    public void insertReceipt(Long orderId , Long staffId) throws BizCheckedException, ParseException {
        IbdHeader ibdHeader = poOrderService.getInbPoHeaderByOrderId(orderId);
        List<IbdDetail> ibdDetails = poOrderService.getInbPoDetailListByOrderId(orderId);
        ReceiptRequest request = new ReceiptRequest();

        request.setOrderOtherId(ibdHeader.getOrderOtherId());
        Map<String , Object> map = new HashMap<String, Object>();
        map.put("staffId",staffId);
        request.setReceiptUser(staffService.getStaffList(map).get(0).getName());
        request.setWarehouseId(0l);
        request.setStaffId(staffId);

        List<ReceiptItem> items = new ArrayList<ReceiptItem>();

        for(IbdDetail ibdDetail : ibdDetails){
            ReceiptItem item = new ReceiptItem();
            item.setArriveNum(ibdDetail.getOrderQty());
            //item.setBarCode(ibdDetail.getBarCode());
            item.setInboundQty(ibdDetail.getOrderQty());
            item.setPackName(ibdDetail.getPackName());
            item.setPackUnit(ibdDetail.getPackUnit());
            //item.setSkuId(ibdDetail.getSkuId());
            item.setSkuName(ibdDetail.getSkuName());
            items.add(item);
        }
        request.setItems(items);
        this.insertOrder(request);
    }

    /**
     * 门店收货
     */
    public void addStoreReceipt(ReceiptRequest request) throws BizCheckedException, ParseException {


        //查询inbReceiptHeader是否存在 根据托盘查询
        Map<String,Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("containerId",request.getContainerId());
        InbReceiptHeader inbReceiptHeader = poReceiptService.getInbReceiptHeaderByParams(mapQuery);
        if(inbReceiptHeader == null){
            //初始化InbReceiptHeader
            inbReceiptHeader = new InbReceiptHeader();
            ObjUtils.bean2bean(request, inbReceiptHeader);
            //设置receiptOrderId
            inbReceiptHeader.setReceiptOrderId(RandomUtils.genId());
            //设置门店以及收货类型
            inbReceiptHeader.setReceiptType(ReceiptContant.RECEIPT_TYPE_STORE);
            inbReceiptHeader.setStoreCode(request.getStoreId());
            //设置InbReceiptHeader状态
            inbReceiptHeader.setReceiptStatus(BusiConstant.EFFECTIVE_YES);
            //设置InbReceiptHeader插入时间
            inbReceiptHeader.setInserttime(new Date());
        }

        //大店放在集货道 小店放到集货位
        //BaseinfoStore baseinfoStore = iStoreRpcService.getStoreByStoreNo(inbReceiptHeader.getStoreCode());
        CsiCustomer csiCustomer = customerService.getCustomerByCustomerCode(request.getOwnerId(),inbReceiptHeader.getStoreCode());

        List<BaseinfoLocation> list = locationRpcService.getCollectionByStoreNo(inbReceiptHeader.getStoreCode());
        if( list != null && list.size() >= 0 ){
            inbReceiptHeader.setLocation(list.get(0).getLocationId());
        }
//        BaseinfoLocation baseinfoLocation = locationRpcService.assignTemporary();
//        inbReceiptHeader.setLocation(baseinfoLocation.getLocationId());// TODO: 16/7/20  暂存区信息

        //初始化List<InbReceiptDetail>
        List<InbReceiptDetail> inbReceiptDetailList = new ArrayList<InbReceiptDetail>();
        List<IbdDetail> updateIbdDetailList = new ArrayList<IbdDetail>();
        List<StockQuant> stockQuantList = new ArrayList<StockQuant>();
        List<StockLot> stockLotList = new ArrayList<StockLot>();
        List<Map<String, Object>> moveList = new ArrayList<Map<String, Object>>();
        //验收单
        List<ReceiveDetail> updateReceiveDetailList = new ArrayList<ReceiveDetail>();

        //生成出库detail
        List<ObdStreamDetail> obdStreamDetailList = new ArrayList<ObdStreamDetail>();

        Map<Long,Long> locationMap = new HashMap<Long, Long>();
        List<StockTransferPlan> planList = new ArrayList<StockTransferPlan>();

        String idKey = "task_" + TaskConstant.TYPE_PO.toString();
        Long taskId = idGenerator.genId(idKey, true, true);
        //Long taskId = RandomUtils.genId();

        for(ReceiptItem receiptItem : request.getItems()){
            if(receiptItem.getInboundQty().compareTo(BigDecimal.ZERO) < 0) {
                throw new BizCheckedException("2020007");
            }

            InbReceiptDetail inbReceiptDetail = new InbReceiptDetail();

            ObjUtils.bean2bean(receiptItem, inbReceiptDetail);

            //根据request中的orderOtherId查询InbPoHeader
            IbdHeader ibdHeader = poOrderService.getInbPoHeaderByOrderOtherId(request.getOrderOtherId());
            if (ibdHeader == null) {
                throw new BizCheckedException("2020001");
            }
            // TODO: 2016/10/8 查询验收单是否存在,如果不存在,则根据ibd重新生成
            ReceiveHeader receiveHeader = receiveService.getReceiveHeader(ibdHeader.getOrderId());
            Long receiveId = 0l;
            if(receiveHeader == null){
                receiveId = this.genReceive(ibdHeader,request.getItems());

            }else{
                receiveId = receiveHeader.getReceiveId();
            }

            //设置receiptOrderId
            inbReceiptDetail.setReceiptOrderId(inbReceiptHeader.getReceiptOrderId());
            inbReceiptDetail.setOrderOtherId(ibdHeader.getOrderOtherId());
            inbReceiptDetail.setOrderId(ibdHeader.getOrderId());

            boolean isCanReceipt = ibdHeader.getOrderStatus() == PoConstant.ORDER_THROW || ibdHeader.getOrderStatus() == PoConstant.ORDER_RECTIPT_PART || ibdHeader.getOrderStatus() == PoConstant.ORDER_RECTIPTING;
            if (!isCanReceipt) {
                throw new BizCheckedException("2020002");
            }

            //根据InbPoHeader中的OwnerUid及InbReceiptDetail中的SkuId获取Item
            CsiSku csiSku = csiSkuService.getSkuByCode(CsiConstan.CSI_CODE_TYPE_BARCODE, inbReceiptDetail.getBarCode());
            if (null == csiSku || csiSku.getSkuId() == null) {
                throw new BizCheckedException("2020022");
            }
            inbReceiptDetail.setSkuId(csiSku.getSkuId());
            BaseinfoItem baseinfoItem = itemService.getItem(ibdHeader.getOwnerUid(), csiSku.getSkuId());
            inbReceiptDetail.setItemId(baseinfoItem.getItemId());

            //根据OrderId及SkuCode获取InbPoDetail
            IbdDetail ibdDetail = poOrderService.getInbPoDetailByOrderIdAndSkuCode(ibdHeader.getOrderId(), baseinfoItem.getSkuCode());

            //写入InbReceiptDetail中的OrderQty
            inbReceiptDetail.setOrderQty(ibdDetail.getOrderQty());

            // 判断是否超过订单总数
            BigDecimal poInboundQty = null != ibdDetail.getInboundQty() ? ibdDetail.getInboundQty() : new BigDecimal(0);

            if (poInboundQty.add(inbReceiptDetail.getInboundQty()).compareTo(ibdDetail.getOrderQty()) > 0) {
                throw new BizCheckedException("2020005");
            }

            // 批量修改ibd 实收数量
            IbdDetail updateIbdDetail = new IbdDetail();
            updateIbdDetail.setInboundQty(inbReceiptDetail.getInboundQty());
            updateIbdDetail.setOrderId(inbReceiptDetail.getOrderId());
            //updateIbdDetail.setSkuId(inbReceiptDetail.getSkuId());
            updateIbdDetail.setDetailOtherId(ibdDetail.getDetailOtherId());
            updateIbdDetailList.add(updateIbdDetail);

            //根据receiveId及SkuCode获取receiveDetail
            ReceiveDetail receiveDetail = receiveService.getReceiveDetailByReceiveIdAndSkuCode(receiveId, baseinfoItem.getSkuCode());


            //批量修改receive 实收数量
            ReceiveDetail updateReceiveDetail = new ReceiveDetail();
            updateReceiveDetail.setDetailOtherId(receiveDetail.getDetailOtherId());
            updateReceiveDetail.setReceiveId(receiveDetail.getReceiveId());
            updateReceiveDetail.setInboundQty(inbReceiptDetail.getInboundQty());
            updateReceiveDetailList.add(updateReceiveDetail);

            //生成出库detail信息
            //获取redis中的orderId
            String key = StrUtils.formatString(RedisKeyConstant.PO_STORE, ibdHeader.getOrderId(), inbReceiptHeader.getStoreCode());

            Long obdOrderId = Long.valueOf(redisStringDao.get(key));


            ObdStreamDetail obdStreamDetail = new ObdStreamDetail();
            obdStreamDetail.setItemId(inbReceiptDetail.getItemId());
            obdStreamDetail.setContainerId(inbReceiptHeader.getContainerId());
            obdStreamDetail.setOwnerId(ibdHeader.getOwnerUid());
            obdStreamDetail.setPickQty(inbReceiptDetail.getInboundQty().multiply(inbReceiptDetail.getPackUnit()));
            obdStreamDetail.setAllocUnitQty(inbReceiptDetail.getInboundQty());
            obdStreamDetail.setAllocUnitName(inbReceiptDetail.getPackName());
            obdStreamDetail.setSkuId(inbReceiptDetail.getSkuId());
            obdStreamDetailList.add(obdStreamDetail);
            obdStreamDetail.setOrderId(obdOrderId);

            inbReceiptDetailList.add(inbReceiptDetail);

            StockLot stockLot = new StockLot();
            stockLot.setIsOld(true);

            stockLot.setPackUnit(ibdDetail.getPackUnit());
            stockLot.setSkuId(inbReceiptDetail.getSkuId());
            stockLot.setSerialNo(inbReceiptDetail.getLotNum());
            stockLot.setItemId(inbReceiptDetail.getItemId());
            stockLot.setReceiptId(inbReceiptHeader.getReceiptOrderId());
            stockLot.setPoId(inbReceiptDetail.getOrderId());
            stockLot.setSupplierId(ibdHeader.getSupplierCode());
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


        //插入订单
        //poReceiptService.insertOrder(inbReceiptHeader, inbReceiptDetailList, updateInbPoDetailList,stockQuantList,stockLotList);
        poReceiptService.insertOrder(inbReceiptHeader, inbReceiptDetailList, updateIbdDetailList, moveList,updateReceiveDetailList,obdStreamDetailList,request.getIsCreateTask());




        //如果是大店 生成QC
        if(request.getIsCreateTask()==1) {
            if(csiCustomer.getCustomerType().equals(CustomerConstant.BiG_STORE)){
                TaskEntry taskEntry = new TaskEntry();
                TaskInfo taskInfo = new TaskInfo();
                taskInfo.setTaskId(taskId);
                taskInfo.setType(TaskConstant.TYPE_PO);
                taskInfo.setSubType(TaskConstant.TASK_STORE_DIRECT);
                taskInfo.setBusinessMode(TaskConstant.MODE_DIRECT);
                taskInfo.setOrderId(inbReceiptHeader.getReceiptOrderId());
                taskInfo.setContainerId(inbReceiptHeader.getContainerId());
                taskInfo.setItemId(inbReceiptDetailList.get(0).getItemId());
                taskInfo.setOperator(inbReceiptHeader.getStaffId());
                taskEntry.setTaskInfo(taskInfo);
                taskId = iTaskRpcService.create(TaskConstant.TYPE_PO, taskEntry);
                iTaskRpcService.done(taskId);
            }
        }
    }

    public Long genReceive(IbdHeader ibdHeader,List<ReceiptItem> receiptItemList){
//        // TODO: 16/11/9 保存skucode和barcode的映射关系
//        Map<String,String> skuMap = new HashMap<String, String>();
//        for(ReceiptItem rt:receiptItemList){
//            BaseinfoItem baseinfoItem = itemService.getItem(ibdHeader.getOwnerUid(), rt.getSkuId());
//            if(baseinfoItem == null){
//                continue;
//            }
//            skuMap.put(baseinfoItem.getSkuCode(),rt.getBarCode());
//        }

        //增加receiveHeader总单
        Long receiveId = RandomUtils.genId();
        ReceiveHeader receiveHeader = new ReceiveHeader();
        ObjUtils.bean2bean(ibdHeader,receiveHeader);
        receiveHeader.setReceiveId(receiveId);
        receiveHeader.setOrderStatus(1);
        receiveHeader.setCreatedAt(DateUtils.getCurrentSeconds());
        List<IbdDetail> ibdList = poOrderService.getInbPoDetailListByOrderId(ibdHeader.getOrderId());
        List<ReceiveDetail> receiveDetails = new ArrayList<ReceiveDetail>();
        for (IbdDetail ibdDetail : ibdList){
            ReceiveDetail receiveDetail = new ReceiveDetail();
            ObjUtils.bean2bean(ibdDetail,receiveDetail);
            //receiveDetail.setCode(skuMap.get(ibdDetail.getSkuCode()));// TODO: 16/11/9 增加国条
            receiveDetail.setReceiveId(receiveId);
            receiveDetail.setCreatedAt(DateUtils.getCurrentSeconds());
            receiveDetails.add(receiveDetail);
        }
        receiveService.insertReceive(receiveHeader,receiveDetails);
        return receiveId;
    }
}
