package com.lsh.wms.service.receipt;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
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
import com.lsh.wms.api.service.system.IExceptionCodeRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.*;
import com.lsh.wms.core.dao.redis.RedisStringDao;
import com.lsh.wms.core.service.container.ContainerService;
import com.lsh.wms.core.service.csi.CsiCustomerService;
import com.lsh.wms.core.service.csi.CsiSkuService;
import com.lsh.wms.core.service.csi.CsiSupplierService;
import com.lsh.wms.core.service.item.ItemLocationService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.location.LocationDetailService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.po.PoOrderService;
import com.lsh.wms.core.service.po.PoReceiptService;
import com.lsh.wms.core.service.po.ReceiveService;
import com.lsh.wms.core.service.so.SoDeliveryService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.staff.StaffService;
import com.lsh.wms.core.service.stock.StockLotService;
import com.lsh.wms.core.service.utils.IdGenerator;
import com.lsh.wms.core.service.utils.PackUtil;
import com.lsh.wms.model.baseinfo.*;
import com.lsh.wms.model.csi.CsiCustomer;
import com.lsh.wms.model.csi.CsiSku;
import com.lsh.wms.model.csi.CsiSupplier;
import com.lsh.wms.model.po.*;
import com.lsh.wms.model.so.ObdDetail;
import com.lsh.wms.model.so.ObdHeader;
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
    private IExceptionCodeRpcService iexceptionCodeRpcService;

    @Autowired
    private RedisStringDao redisStringDao;

    @Autowired
    private ReceiveService receiveService;

    @Autowired
    private CsiCustomerService customerService;
    @Autowired
    private CsiSupplierService supplierService;

    @Autowired
    private SoOrderService soOrderService;

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

        //根据request中的orderOtherId查询InbPoHeader
        // 按理说上游因该吧订单转换好,这里就不要用外部id运算了 FIXED: 16/11/20
        IbdHeader ibdHeader = poOrderService.getInbPoHeaderByOrderId(request.getOrderId());
        /*  外层已验证
         if (ibdHeader == null) {
            throw new BizCheckedException("2020001");
        }*/
        //判断PO订单类型  虚拟容器,放入退货区
        Integer orderType = ibdHeader.getOrderType();
        if(PoConstant.ORDER_TYPE_SO_BACK == orderType){
            //新增container
            //TODO 这种虚拟的根据货主的container和locaiton的处理方法很蛋疼,已经让人蒙蔽了。。。。。
            //  16/8/19 退货类型的单据 虚拟容器,放入退货区
            Long containerId = containerService.createContainerByType(ContainerConstant.PALLET).getContainerId();
            inbReceiptHeader.setContainerId(containerId);
            // : 16/8/19 设置退货区
            List<BaseinfoLocation> lists = locationDetailService.getMarketReturnList();
            if(lists != null || lists.size() >0){
                Long location = lists.get(0).getLocationId();
                inbReceiptHeader.setLocation(location);
            }else{
                throw new BizCheckedException("2022223");
            }

            //设置收货类型
            inbReceiptHeader.setReceiptType(ReceiptContant.RECEIPT_TYPE_NORMAL);

        }else{
            BaseinfoLocation baseinfoLocation = locationRpcService.assignTemporary();
            if(baseinfoLocation == null){
                throw new BizCheckedException("2022223");
            }
            inbReceiptHeader.setLocation(baseinfoLocation.getLocationId());//16/7/20  暂存区信息
            if(PoConstant.ORDER_TYPE_CPO == orderType){
                //TODO 这个类型的定义根本看不懂啊
                inbReceiptHeader.setReceiptType(ReceiptContant.RECEIPT_TYPE_ORDER);
            }else{
                inbReceiptHeader.setReceiptType(ReceiptContant.RECEIPT_TYPE_NORMAL);
            }

        }

        // 2016/10/8 查询验收单是否存在,如果不存在,则根据ibd重新生成
        //TODO 这个方法定义的太蛋疼了,谁知道你是拿了个指定状态的玩意?
        ReceiveHeader receiveHeader = receiveService.getReceiveHeader(ibdHeader.getOrderId());
        Long receiveId = 0l;
        if(receiveHeader == null){
            //TODO 这里会有问题,后面失败了,这就sb了
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
        //List<StockLot> stockLotList = new ArrayList<StockLot>();
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
                inbReceiptDetail.setBarCode(baseinfoItem.getCode());


                //根据OrderId及SkuCode获取InbPoDetail
                IbdDetail ibdDetail = poOrderService.getInbPoDetailByOrderIdAndDetailOtherId(receiptItem.getOrderId(),receiptItem.getDetailOtherId());

                //写入InbReceiptDetail中的OrderQty
                inbReceiptDetail.setOrderQty(ibdDetail.getOrderQty());

                IbdDetail updateIbdDetail = new IbdDetail();
                //inboundQty改为 ea的数量
                //BigDecimal inboundUnitQty = inbReceiptDetail.getInboundQty().multiply(inbReceiptDetail.getPackUnit());
                BigDecimal inboundUnitQty = inbReceiptDetail.getInboundQty();
                updateIbdDetail.setInboundQty(inboundUnitQty);
                updateIbdDetail.setOrderId(inbReceiptDetail.getOrderId());
                updateIbdDetail.setDetailOtherId(ibdDetail.getDetailOtherId());
                updateIbdDetailList.add(updateIbdDetail);


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

                ObdHeader obdHeader = soOrderService.getOutbSoHeaderByOrderOtherId(ibdHeader.getOrderOtherRefId());
                //查供应商、生产日期、失效日期
                Long lotId =
                        soDeliveryService.getOutbDeliveryDetail(obdHeader.getOrderId(),baseinfoItem.getItemId()).getLotId();
                StockLot stockLot = stockLotService.getStockLotByLotId(lotId);
                logger.info("~~~~~~~~~~~11111111111 查找批号信息  stocklot : " + JSON.toJSONString(stockLot));
                //stockLot.setIsOld(true);
                logger.info("~~~~~~~~~~~~222222222222 stocklot : " + JSON.toJSONString(stockLot));

                StockLot newStockLot = new StockLot();
                ObjUtils.bean2bean(stockLot,newStockLot);
                Long newLotId = RandomUtils.genId();
                newStockLot.setPoId(ibdHeader.getOrderId());
                newStockLot.setLotId(newLotId);


                //将收货细单中的生产日期改为该lot下的生产日期。
                SimpleDateFormat format =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
                String d = format.format(stockLot.getProductDate());
                Date date = format.parse(d);
                inbReceiptDetail.setProTime(date);
                //将inbReceiptDetail填入inbReceiptDetailList中
                //inbReceiptDetail.setInboundQty(inboundUnitQty);
                inbReceiptDetailList.add(inbReceiptDetail);


                //BigDecimal inboundQty = inbReceiptDetail.getInboundQty();

                //qty转化为ea
                //BigDecimal qty = inboundQty.multiply(inbReceiptDetail.getPackUnit());

                StockMove move = new StockMove();
                move.setToLocationId(inbReceiptHeader.getLocation());
                move.setToContainerId(inbReceiptHeader.getContainerId());
                //move.setQty(inbReceiptDetail.getInboundQty());
                move.setQty(inboundUnitQty);
                move.setItemId(inbReceiptDetail.getItemId());
                move.setOperator(Long.valueOf(inbReceiptHeader.getReceiptUser()));
                move.setTaskId(taskId);

                Map<String, Object> moveInfo = new HashMap<String, Object>();
                moveInfo.put("lot", newStockLot);
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

//                StockTransferPlan plan = new StockTransferPlan();
//                plan.setItemId(baseinfoItem.getItemId());
//                //返仓区Id
//                plan.setFromLocationId(inbReceiptHeader.getLocation());
//                plan.setToLocationId(locationMap.get(baseinfoItem.getItemId()));
//                //// TODO: 16/8/20 数量 转换为包装数量
//                plan.setUomQty(PackUtil.EAQty2UomQty(inboundUnitQty,inbReceiptDetail.getPackUnit()));
//                planList.add(plan);
//                //stockTransferRpcService.addPlan(plan);
            }

        } else{

            for(ReceiptItem receiptItem : request.getItems()){

                if(receiptItem.getInboundQty().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new BizCheckedException("2020007");
                }

                if(!containerService.isContainerCanUse(inbReceiptHeader.getContainerId())){
                    throw new BizCheckedException("2000002");
                }

                //本次收货数量
                BigDecimal receiptEaQty = receiptItem.getInboundQty();


                boolean isCanReceipt = ibdHeader.getOrderStatus() == PoConstant.ORDER_THROW
                        || ibdHeader.getOrderStatus() == PoConstant.ORDER_RECTIPT_PART
                        || ibdHeader.getOrderStatus() == PoConstant.ORDER_RECTIPTING;
                if (!isCanReceipt) {
                    throw new BizCheckedException("2020002");
                }


                //根据InbPoHeader中的OwnerUid及InbReceiptDetail中的SkuId获取Item
                CsiSku csiSku = csiSkuService.getSkuByCode(CsiConstan.CSI_CODE_TYPE_BARCODE, receiptItem.getBarCode());
                if (null == csiSku || csiSku.getSkuId() == null) {
                    throw new BizCheckedException("2020022");
                }

                BaseinfoItem baseinfoItem = itemService.getItem(ibdHeader.getOwnerUid(), csiSku.getSkuId());

                //根据OrderId及SkuId获取InbPoDetail
                List<IbdDetail> ibdDetailList = poOrderService.getInbPoDetailByOrderAndSkuCode(receiptItem.getOrderId(), baseinfoItem.getSkuCode());

                Map<String,Object> ibdDetailInfo = this.mergeIbdDetailList(ibdDetailList,receiptEaQty);
                //箱规
                BigDecimal ibdPackUnit = BigDecimal.valueOf(Double.parseDouble(ibdDetailInfo.get("packUnit").toString()));
                String ibdPackName = ibdDetailInfo.get("packName").toString();
                //订货总数(箱规)
                BigDecimal orderUomQtyTotal = BigDecimal.valueOf(Double.parseDouble(ibdDetailInfo.get("orderUomQtyTotal").toString()));
                //订货总数(ea)
                BigDecimal orderEaQtyTotal = BigDecimal.valueOf(Double.parseDouble(ibdDetailInfo.get("orderEaQtyTotal").toString()));
                //实际收货数(EA)
                BigDecimal inboundEaQtyTotal = BigDecimal.valueOf(Double.parseDouble(ibdDetailInfo.get("inboundEaQtyTotal").toString()));
                //本次收货数量
                BigDecimal inboundUnitQty = receiptItem.getInboundQty();



                // 判断是否超过订单总数
                /*BigDecimal poInboundQty = null != ibdDetail.getInboundQty() ? ibdDetail.getInboundQty() : new BigDecimal(0);

                if (poInboundQty.add(inbReceiptDetail.getInboundQty()).compareTo(ibdDetail.getOrderQty().multiply(ibdDetail.getPackUnit())) > 0) {
                    throw new BizCheckedException("2020005");
                }*/

                if (inboundEaQtyTotal.add(inboundUnitQty).compareTo(orderEaQtyTotal) > 0) {
                    throw new BizCheckedException("2020005");//超过订单数量
                }

                /*IbdDetail updateIbdDetail = new IbdDetail();
                //转成ea
                BigDecimal inboundUnitQty = inbReceiptDetail.getInboundQty();
                updateIbdDetail.setInboundQty(inboundUnitQty);
                updateIbdDetail.setOrderId(inbReceiptDetail.getOrderId());
                //updateIbdDetail.setSkuId(inbReceiptDetail.getSkuId());
                updateIbdDetail.setDetailOtherId(ibdDetail.getDetailOtherId());
                updateIbdDetailList.add(updateIbdDetail);*/
                //更新订单中的收货数量
                updateIbdDetailList = getUpdateibdDetailListData(ibdDetailList,inboundUnitQty);

                //根据receiveId及SkuCode获取receiveDetail
                ReceiveDetail receiveDetail = receiveService.getReceiveDetailByReceiveIdAndSkuCode(receiveId, baseinfoItem.getSkuCode());

                //批量修改receive 实收数量
                ReceiveDetail updateReceiveDetail = new ReceiveDetail();
                updateReceiveDetail.setDetailOtherId(receiveDetail.getDetailOtherId());
                updateReceiveDetail.setReceiveId(receiveDetail.getReceiveId());
                //改为ea的数量
                updateReceiveDetail.setInboundQty(inboundUnitQty);
                updateReceiveDetail.setUpdatedAt(DateUtils.getCurrentSeconds());//更新时间
                updateReceiveDetail.setCode(baseinfoItem.getCode());//更新国条
                updateReceiveDetailList.add(updateReceiveDetail);

                if(ibdHeader.getOrderType() == PoConstant.ORDER_TYPE_CPO){
                    ObdStreamDetail obdStreamDetail = new ObdStreamDetail();
                    obdStreamDetail.setItemId(receiptItem.getItemId());
                    obdStreamDetail.setContainerId(inbReceiptHeader.getContainerId());
                    obdStreamDetail.setOwnerId(ibdHeader.getOwnerUid());
                    //统一放到pickQty中
                    obdStreamDetail.setPickQty(inboundUnitQty);
                    //如果单位不为ea 判断下是否为整件
                    if(inboundUnitQty.divideAndRemainder(ibdPackUnit)[1].compareTo(BigDecimal.ZERO) == 0) {
                        obdStreamDetail.setAllocUnitName(receiptItem.getPackName());
                        obdStreamDetail.setAllocUnitQty(PackUtil.EAQty2UomQty(inboundUnitQty,receiptItem.getPackName()));
                    }else{
                        obdStreamDetail.setAllocUnitName("EA");
                        obdStreamDetail.setAllocUnitQty(inboundUnitQty);
                    }
                    obdStreamDetail.setSkuId(receiptItem.getSkuId());
                    obdStreamDetail.setAllocQty(inboundUnitQty);
                    obdStreamDetailList.add(obdStreamDetail);

                }

                InbReceiptDetail inbReceiptDetail = new InbReceiptDetail();
                ObjUtils.bean2bean(receiptItem, inbReceiptDetail);
                //设置receiptOrderId
                inbReceiptDetail.setReceiptOrderId(inbReceiptHeader.getReceiptOrderId());
                inbReceiptDetail.setOrderOtherId(request.getOrderOtherId());
                //写入InbReceiptDetail中的OrderId
                inbReceiptDetail.setOrderId(ibdHeader.getOrderId());
                inbReceiptDetail.setSkuId(csiSku.getSkuId());
                inbReceiptDetail.setItemId(baseinfoItem.getItemId());
                //写入InbReceiptDetail中的OrderQty
                //inbReceiptDetail.setOrderQty(ibdDetail.getOrderQty());
                inbReceiptDetail.setOrderQty(orderUomQtyTotal);
                inbReceiptDetail.setPackUnit(ibdPackUnit);
                inbReceiptDetail.setPackName(ibdPackName);
                inbReceiptDetail.setInboundQty(inboundUnitQty);
                inbReceiptDetailList.add(inbReceiptDetail);

                CsiSupplier supplier = supplierService.getSupplier(ibdHeader.getSupplierCode(),ibdHeader.getOwnerUid());


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



                StockLot stockLot = new StockLot();
                stockLot.setLotId(lotId);
                if(PoConstant.ORDER_TYPE_CPO == orderType){
                    stockLot.setIsOld(true);
                }else{
                    //修改失效日期
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(inbReceiptDetail.getProTime());
                    calendar.add(calendar.DAY_OF_YEAR,baseinfoItem.getShelfLife().intValue());
                    Long expireDate = calendar.getTime().getTime()/1000;

                    stockLot.setPackUnit(ibdPackUnit);
                    stockLot.setPackName(ibdPackName);
                    stockLot.setSkuId(inbReceiptDetail.getSkuId());
                    stockLot.setSerialNo(inbReceiptDetail.getLotNum());
                    stockLot.setItemId(inbReceiptDetail.getItemId());
                    stockLot.setInDate(receiptTime.getTime() / 1000);
                    stockLot.setProductDate(inbReceiptDetail.getProTime().getTime() / 1000);
                    stockLot.setExpireDate(expireDate);
                    stockLot.setReceiptId(inbReceiptHeader.getReceiptOrderId());
                    stockLot.setPoId(inbReceiptDetail.getOrderId());
                    stockLot.setSupplierId(supplier.getSupplierId());
                    //stockLotList.add(stockLot);
                }


                StockMove move = new StockMove();
                List<BaseinfoLocation> locationList = locationService.getLocationsByType(LocationConstant.SUPPLIER_AREA);
                if(locationList == null || locationList.size() == 0){
                    throw new BizCheckedException("2020107");//没有供货区
                }
                move.setFromLocationId(locationList.get(0).getLocationId());
                move.setToLocationId(inbReceiptHeader.getLocation());
                move.setOperator(Long.valueOf(inbReceiptHeader.getReceiptUser()));
                move.setToContainerId(inbReceiptHeader.getContainerId());
//                //qty转化为ea
//                BigDecimal qty = inbReceiptDetail.getInboundQty().multiply(inbReceiptDetail.getPackUnit());

                move.setQty(inboundUnitQty);
                move.setItemId(inbReceiptDetail.getItemId());
                move.setTaskId(taskId);

                Map<String, Object> moveInfo = new HashMap<String, Object>();
                moveInfo.put("lot", stockLot);
                moveInfo.put("move", move);
                moveList.add(moveInfo);

            }
        }

        //TODO 这后面的东西风险是极高的,一旦出错,便不能修复,不能重入。
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
            //改为uid
            taskInfo.setOperator(Long.valueOf(inbReceiptHeader.getReceiptUser()));
            taskEntry.setTaskInfo(taskInfo);
            taskId = iTaskRpcService.create(TaskConstant.TYPE_PO, taskEntry);
            iTaskRpcService.done(taskId);
        }/*移到了insertOrder事务中
        else if(PoConstant.ORDER_TYPE_SO_BACK == orderType){
            //返仓单生成移库单之后 将状态改为收货完成
            ibdHeader.setOrderStatus(PoConstant.ORDER_RECTIPT_ALL);
            poOrderService.updateInbPoHeader(ibdHeader);

        }*/
        //插入订单
        //poReceiptService.insertOrder(inbReceiptHeader, inbReceiptDetailList, updateInbPoDetailList,stockQuantList,stockLotList);
        poReceiptService.insertOrder(ibdHeader,inbReceiptHeader, inbReceiptDetailList, updateIbdDetailList, moveList,updateReceiveDetailList,obdStreamDetailList,null);




    }
    //验证是否可收货
    /*private boolean canReceipt(BaseinfoItem baseinfoItem,IbdHeader ibdHeader,IbdDetail ibdDetail)throws BizCheckedException{

        //商品信息是否完整
        if(baseinfoItem.getIsInfoIntact() == 0){
            throw new BizCheckedException("2020104");//商品信息不完整,不能收货
        }
        //验证箱规是否一至
        if(baseinfoItem.getPackUnit().compareTo(ibdDetail.getPackUnit()) != 0){
            throw new BizCheckedException("2020105");//箱规不一致,不能收货
        }
        //验证状态是否可收货
        boolean isCanReceipt = ibdHeader.getOrderStatus() == PoConstant.ORDER_THROW || ibdHeader.getOrderStatus() == PoConstant.ORDER_RECTIPT_PART || ibdHeader.getOrderStatus() == PoConstant.ORDER_RECTIPTING;
        if (!isCanReceipt) {
            throw new BizCheckedException("2020002");
        }

        return true;
    }*/

    //验证生产日期
    public boolean checkProTime(BaseinfoItem baseinfoItem,Date proTime,Date dueTime,String exceptionCode) throws BizCheckedException{
        if(StringUtils.isNotEmpty(exceptionCode) ){
            //验证例外代码
            //超过保质期,保质期例外代码验证
            String proTimeexceptionCode = iexceptionCodeRpcService.getExceptionCodeByName("receiveExpired");// 获取保质期的例外代码
            if(exceptionCode.equals(proTimeexceptionCode)){
                return true;
            }else{
                throw new BizCheckedException("2020103"); //例外代码不匹配
            }
        }
        logger.info("#############exceptionCode" + exceptionCode);
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
        if(shelLife.compareTo(BigDecimal.ZERO) <= 0){
            throw new BizCheckedException("2020106");//保质期必须大于0
        }
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

    public List<InbReceiptHeader> getInbReceiptHeaderDetailList(Map<String,Object> param) throws BizCheckedException {
        //封装返回数据
        List<InbReceiptHeader> inbReceiptHeaderList = new ArrayList<InbReceiptHeader>();
        //查询收货详情
        List<InbReceiptDetail> detailList = poReceiptService.getInbReceiptDetailList(param);
        if(detailList == null){
            return new ArrayList<InbReceiptHeader>();
        }
        //记录收货ID,避免重复查询
        Set<Long> receiptOrderIdSet = new HashSet<Long>() ;

        Map<Long,List<InbReceiptDetail>> detailMap = new HashMap<Long, List<InbReceiptDetail>>();
        for(InbReceiptDetail inbReceiptDetail : detailList){
            Long receiptOrderId = inbReceiptDetail.getReceiptOrderId();
            receiptOrderIdSet.add(receiptOrderId);
            List<InbReceiptDetail> inbReceiptDetailList = detailMap.get(receiptOrderId);
            if(inbReceiptDetailList == null){
                inbReceiptDetailList = new ArrayList<InbReceiptDetail>();

            }
            inbReceiptDetailList.add(inbReceiptDetail);
            detailMap.put(receiptOrderId,inbReceiptDetailList);
        }
        for(Long receiptOrderId :receiptOrderIdSet){
            Map<String,Object> newparam = new HashMap<String, Object>();
            newparam.put("receiptOrderId",receiptOrderId);
            InbReceiptHeader inbReceiptHeader = poReceiptService.getInbReceiptHeaderByParams(newparam);
            if(inbReceiptHeader == null){
                continue;
            }
            inbReceiptHeader.setReceiptDetails(detailMap.get(receiptOrderId));
            inbReceiptHeaderList.add(inbReceiptHeader);
        }
        return inbReceiptHeaderList;
    }

    public List<InbReceiptDetail> getInbReceiptDetailList(Map<String,Object> param) throws BizCheckedException{
        return poReceiptService.getInbReceiptDetailList(param);
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

    public Integer countInbPoReceiptDetail(Map<String, Object> params) {
        return poReceiptService.countInbReceiptDetail(params);
    }

    public List<InbReceiptHeader> getInbReceiptHeaderList(Map<String, Object> params) {
        return poReceiptService.getInbReceiptHeaderList(params);
    }

    public void insertReceipt(Long orderId , Long staffId) throws BizCheckedException, ParseException {
        IbdHeader ibdHeader = poOrderService.getInbPoHeaderByOrderId(orderId);
        List<IbdDetail> ibdDetails = poOrderService.getInbPoDetailListByOrderId(orderId);
        ReceiptRequest request = new ReceiptRequest();

        //转换成orderId
        //request.setOrderOtherId(ibdHeader.getOrderOtherId());
        request.setOrderId(orderId);
        Map<String , Object> map = new HashMap<String, Object>();
        map.put("staffId",staffId);
        request.setReceiptUser(staffService.getStaffList(map).get(0).getName());
        request.setWarehouseId(0l);
        request.setStaffId(staffId);
        request.setOrderOtherId(ibdHeader.getOrderOtherId());
        request.setOwnerId(ibdHeader.getOwnerUid());

        //根据返仓单中的orderOtherRefId找到对应的出库单
        ObdHeader obdHeader = soOrderService.getOutbSoHeaderByOrderOtherId(ibdHeader.getOrderOtherRefId());

        List<ReceiptItem> items = new ArrayList<ReceiptItem>();

        for(IbdDetail ibdDetail : ibdDetails){
            ReceiptItem item = new ReceiptItem();

            //这里从obd订单中取skuId
//            List<BaseinfoItem> baseinfoItems = itemService.getItemsBySkuCode(ibdHeader.getOwnerUid(),ibdDetail.getSkuCode());
//            if(baseinfoItems.size() <= 0){
//                throw new BizCheckedException("2900001");
//            }
//            BaseinfoItem baseinfoItem = baseinfoItems.get(baseinfoItems.size()-1);
            Map<String,Object> mapQuery = new HashMap<String, Object>();
            mapQuery.put("orderId", obdHeader.getOrderId());
            mapQuery.put("skuCode",ibdDetail.getSkuCode());
            List<ObdDetail> obdDetails = soOrderService.getOutbSoDetailList(mapQuery);
            if(obdDetails == null || obdDetails.size() <= 0){
                throw new BizCheckedException("2022224");
            }
            ObdDetail obdDetail = obdDetails.get(0);
            item.setArriveNum(ibdDetail.getOrderQty());
            //item.setBarCode(obdDetail);
            item.setInboundQty(ibdDetail.getOrderQty());
            item.setPackName(ibdDetail.getPackName());
            item.setPackUnit(ibdDetail.getPackUnit());
            item.setSkuId(obdDetail.getSkuId());
            item.setSkuName(ibdDetail.getSkuName());
            item.setDetailOtherId(ibdDetail.getDetailOtherId());
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
        Long containerId = request.getContainerId();
        mapQuery.put("containerId",containerId);
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
        CsiCustomer csiCustomer = customerService.getCustomerByCustomerCode(inbReceiptHeader.getStoreCode());

        //获取location的id
        if (null == csiCustomer) {
            throw new BizCheckedException("2180023");
        }
        if (null == csiCustomer.getCollectRoadId()) {
            throw new BizCheckedException("2180024");
        }
        BaseinfoLocation location = locationService.getLocation(csiCustomer.getCollectRoadId());

        if( location != null){
            inbReceiptHeader.setLocation(location.getLocationId());
        }

        Long collectRoadId =csiCustomer.getCollectRoadId();
        if(collectRoadId == null || collectRoadId == 0){
            throw new BizCheckedException("2020108");//店铺没有设置集货道
        }
        inbReceiptHeader.setLocation(collectRoadId);

        /*List<BaseinfoLocation> list = locationRpcService.getCollectionByStoreNo(inbReceiptHeader.getStoreCode());
        if( list != null && list.size() >= 0 ){
            inbReceiptHeader.setLocation(list.get(0).getLocationId());
        }*/
//        BaseinfoLocation baseinfoLocation = locationRpcService.assignTemporary();
//        inbReceiptHeader.setLocation(baseinfoLocation.getLocationId());// TODO: 16/7/20  暂存区信息

        //初始化List<InbReceiptDetail>
        List<InbReceiptDetail> inbReceiptDetailList = new ArrayList<InbReceiptDetail>();
        List<IbdDetail> updateIbdDetailList = new ArrayList<IbdDetail>();
        //List<StockQuant> stockQuantList = new ArrayList<StockQuant>();
        //List<StockLot> stockLotList = new ArrayList<StockLot>();
        List<Map<String, Object>> moveList = new ArrayList<Map<String, Object>>();
        //验收单
        List<ReceiveDetail> updateReceiveDetailList = new ArrayList<ReceiveDetail>();

        //生成出库detail
        List<ObdStreamDetail> obdStreamDetailList = new ArrayList<ObdStreamDetail>();

        Map<Long,Long> locationMap = new HashMap<Long, Long>();
        List<StockTransferPlan> planList = new ArrayList<StockTransferPlan>();

        List<ObdDetail> obdDetails = new ArrayList<ObdDetail>();

        String idKey = "task_" + TaskConstant.TYPE_PO.toString();
        Long taskId = idGenerator.genId(idKey, true, true);
        //Long taskId = RandomUtils.genId();

        for(ReceiptItem receiptItem : request.getItems()){
            if(receiptItem.getInboundQty().compareTo(BigDecimal.ZERO) <= 0) {
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
            List<IbdDetail> ibdDetailList = poOrderService.getInbPoDetailByOrderAndSkuCode(ibdHeader.getOrderId(), baseinfoItem.getSkuCode());

            Map<String,Object>  ibdDetailInfo = this.mergeIbdDetailList(ibdDetailList,receiptItem.getInboundQty());
            //订货总数(箱规)
            BigDecimal orderUomQtyTotal = BigDecimal.valueOf(Double.parseDouble(ibdDetailInfo.get("orderUomQtyTotal").toString()));
            //写入InbReceiptDetail中的OrderQty
            //inbReceiptDetail.setOrderQty(ibdDetail.getOrderQty());
            inbReceiptDetail.setOrderQty(orderUomQtyTotal);

//            //剩余数量存入redis po订单号 托盘码 barcode作为key
//            String qtyKey = StrUtils.formatString(RedisKeyConstant.STORE_QTY,ibdHeader.getOrderId(),containerId,inbReceiptDetail.getBarCode());
//            BigDecimal obdQty = new BigDecimal(redisStringDao.get(qtyKey));
//
//            if(obdQty.compareTo(inbReceiptDetail.getInboundQty()) < 0){
//                throw new BizCheckedException("2022222");
//
//            }

            //获取redis中的orderId
            String key = StrUtils.formatString(RedisKeyConstant.PO_STORE, ibdHeader.getOrderId(), inbReceiptHeader.getStoreCode());

            List<ObdDetail> obdDetailList = new ArrayList<ObdDetail>();
            String values = redisStringDao.get(key);
            String idArray [] = values.split(";");
            for(int i =0;i<idArray.length;i++){
                Long obdOrderId = Long.valueOf(idArray[i].split(",")[0]);
                String detailOtherId = idArray[i].split(",")[1];
                ObdDetail obdDetail = soOrderService.getObdDetailByOrderIdAndDetailOtherId(obdOrderId,detailOtherId);
                obdDetailList.add(obdDetail);
            }
            Map<String,Object> obdDetailInfo = this.mergeObdDetailList(obdDetailList,receiptItem.getInboundQty());
            //收货单位
            BigDecimal obdPackUnit = BigDecimal.valueOf(Double.parseDouble(obdDetailInfo.get("packUnit").toString()));
            String obdPackName = obdDetailInfo.get("packName").toString();
            //门店实际已收货数ea
            BigDecimal obdInboundEaQtyTotal = BigDecimal.valueOf(Double.parseDouble(obdDetailInfo.get("inboundEaQtyTotal").toString()));
            //门店订货总数ea
            BigDecimal obdOrderEaQtyTotal = BigDecimal.valueOf(Double.parseDouble(obdDetailInfo.get("orderEaQtyTotal").toString()));
            //本次收货数量(ea)
            BigDecimal inboundUnitQty = inbReceiptDetail.getInboundQty();

            //ObdHeader obdHeader = soOrderService.getOutbSoHeaderByOrderId(obdOrderId);
            //BigDecimal sowQty = obdDetail.getSowQty();
            //判断是否超过门店收货的数量
            /*if(sowQty.add(inbReceiptDetail.getInboundQty()).compareTo(obdDetail.getOrderQty().multiply(obdDetail.getPackUnit())) > 0){
                throw new BizCheckedException("2022222");
            }*/
            if(obdInboundEaQtyTotal.add(inbReceiptDetail.getInboundQty()).compareTo(obdOrderEaQtyTotal) > 0){
                throw new BizCheckedException("2022222");//本次收货数量大于门店订单剩余数量
            }
            //本次剩余收货数
            BigDecimal receiptEaQty = inboundUnitQty;
            for(ObdDetail obdDetail : obdDetailList){
                //本单剩余待收货数
                BigDecimal remainEaQty = obdDetail.getUnitQty().subtract(obdDetail.getSowQty());
                if(receiptEaQty.compareTo(remainEaQty) >= 0 ){
                    //本次收货数大于该单订货数
                    obdDetail.setSowQty(obdDetail.getSowQty().add(remainEaQty));
                    receiptEaQty = receiptEaQty.subtract(remainEaQty);
                }else{
                    obdDetail.setSowQty(obdDetail.getSowQty().add(receiptEaQty));
                    receiptEaQty = receiptEaQty.subtract(receiptEaQty);
                }
                obdDetails.add(obdDetail);
                if(receiptEaQty.compareTo(BigDecimal.ZERO) <= 0){
                    break;
                }


            }
            //obdDetail.setSowQty(sowQty.add(inbReceiptDetail.getInboundQty()));
            //obdDetails.add(obdDetail);


            // 判断是否超过订单总数
            // 数量改为ea来判断


           /* BigDecimal poInboundQty = null != ibdDetail.getInboundQty() ? ibdDetail.getInboundQty() : new BigDecimal(0);

            if (poInboundQty.add(inbReceiptDetail.getInboundQty())
                    .compareTo(ibdDetail.getOrderQty().multiply(ibdDetail.getPackUnit())) > 0) {
                throw new BizCheckedException("2020005");
            }*/
            //实际收货总数ea
            BigDecimal ibdInboundEaQtyTotal = BigDecimal.valueOf(Double.parseDouble(ibdDetailInfo.get("inboundEaQtyTotal").toString()));
            //订货总数ea
            BigDecimal ibdOrderEaQtyTotal = BigDecimal.valueOf(Double.parseDouble(ibdDetailInfo.get("orderEaQtyTotal").toString()));
            if (ibdInboundEaQtyTotal.add(inbReceiptDetail.getInboundQty()).compareTo(ibdOrderEaQtyTotal) > 0) {
                throw new BizCheckedException("2020005");//超过订单数量
            }
            // 批量修改ibd 实收数量
            /*IbdDetail updateIbdDetail = new IbdDetail();
            //转为ea
            //BigDecimal inboundUnitQty = inbReceiptDetail.getInboundQty();
            updateIbdDetail.setInboundQty(inboundUnitQty);
            updateIbdDetail.setOrderId(inbReceiptDetail.getOrderId());
            updateIbdDetail.setDetailOtherId(ibdDetail.getDetailOtherId());
            updateIbdDetailList.add(updateIbdDetail);*/
            //更新订单中的收货数量
            updateIbdDetailList = getUpdateibdDetailListData(ibdDetailList,inboundUnitQty);
            //根据receiveId及SkuCode获取receiveDetail
            ReceiveDetail receiveDetail = receiveService.getReceiveDetailByReceiveIdAndSkuCode(receiveId, baseinfoItem.getSkuCode());


            //批量修改receive 实收数量
            ReceiveDetail updateReceiveDetail = new ReceiveDetail();
            updateReceiveDetail.setDetailOtherId(receiveDetail.getDetailOtherId());
            updateReceiveDetail.setReceiveId(receiveDetail.getReceiveId());
            //改为ea
            updateReceiveDetail.setInboundQty(inboundUnitQty);
            updateReceiveDetail.setUpdatedAt(DateUtils.getCurrentSeconds());//更新时间
            updateReceiveDetail.setCode(baseinfoItem.getCode());//更新国条
            updateReceiveDetailList.add(updateReceiveDetail);

            //生成出库detail信息

            ObdStreamDetail obdStreamDetail = new ObdStreamDetail();
            obdStreamDetail.setItemId(inbReceiptDetail.getItemId());
            obdStreamDetail.setContainerId(inbReceiptHeader.getContainerId());
            obdStreamDetail.setOwnerId(ibdHeader.getOwnerUid());
            obdStreamDetail.setPickQty(inboundUnitQty);
            /*if(inboundUnitQty.divideAndRemainder(ibdDetail.getPackUnit())[1].compareTo(BigDecimal.ZERO) == 0) {
                obdStreamDetail.setAllocUnitQty(PackUtil.EAQty2UomQty(inboundUnitQty,inbReceiptDetail.getPackName()));
                obdStreamDetail.setAllocUnitName(inbReceiptDetail.getPackName());
            }else{
                obdStreamDetail.setAllocUnitQty(inboundUnitQty);
                obdStreamDetail.setAllocUnitName("EA");
            }*/
            obdStreamDetail.setAllocUnitQty(PackUtil.EAQty2UomQty(inboundUnitQty,obdPackUnit));
            obdStreamDetail.setAllocUnitName(obdPackName);
            obdStreamDetail.setSkuId(inbReceiptDetail.getSkuId());
            //obdStreamDetail.setOrderId(obdOrderId);
            obdStreamDetail.setAllocCollectLocation(collectRoadId);
            obdStreamDetail.setRealCollectLocation(collectRoadId);
            obdStreamDetail.setAllocQty(inboundUnitQty);
            for(ObdDetail obdDetail :obdDetailList){
                obdStreamDetail.setOrderId(obdDetail.getOrderId());
                obdStreamDetailList.add(obdStreamDetail);
            }


            //将数量转为ea
            //inbReceiptDetail.setInboundQty(inboundUnitQty);
            inbReceiptDetailList.add(inbReceiptDetail);


            CsiSupplier supplier = supplierService.getSupplier(ibdHeader.getSupplierCode(),ibdHeader.getOwnerUid());

            if(supplier == null){
                throw new BizCheckedException("2020109");//供应商不存在
            }
            /*直流不需要生成stockLot
            StockLot stockLot = new StockLot();
            stockLot.setIsOld(true);
            //stockLot.setPackUnit(ibdDetail.getPackUnit());
            stockLot.setPackUnit(obdPackUnit);
            stockLot.setSkuId(inbReceiptDetail.getSkuId());
            stockLot.setSerialNo(inbReceiptDetail.getLotNum());
            stockLot.setItemId(inbReceiptDetail.getItemId());
            stockLot.setReceiptId(inbReceiptHeader.getReceiptOrderId());
            stockLot.setPoId(inbReceiptDetail.getOrderId());
            stockLot.setSupplierId(supplier.getSupplierId());*/
            //stockLotList.add(stockLot);

            StockMove move = new StockMove();
            move.setFromLocationId(locationService.getLocationsByType(LocationConstant.SUPPLIER_AREA).get(0).getLocationId());
            move.setToLocationId(inbReceiptHeader.getLocation());
            move.setOperator(Long.valueOf(inbReceiptHeader.getReceiptUser()));
            move.setToContainerId(inbReceiptHeader.getContainerId());
//            //qty转化为ea
//            BigDecimal qty = inbReceiptDetail.getInboundQty().multiply(inbReceiptDetail.getPackUnit());

            move.setQty(inboundUnitQty);
            move.setItemId(inbReceiptDetail.getItemId());
            move.setTaskId(taskId);

            Map<String, Object> moveInfo = new HashMap<String, Object>();
            StockLot stockLot = new StockLot();
            stockLot.setIsOld(true);
            moveInfo.put("lot", stockLot);
            moveInfo.put("move", move);
            moveList.add(moveInfo);

        }


        //插入订单
        //poReceiptService.insertOrder(inbReceiptHeader, inbReceiptDetailList, updateInbPoDetailList,stockQuantList,stockLotList);
        poReceiptService.insertOrder(null,inbReceiptHeader, inbReceiptDetailList, updateIbdDetailList, moveList,updateReceiveDetailList,obdStreamDetailList,obdDetails);




        //如果是大店生成收货任务,使调度起能够生成QC
        if(csiCustomer.getCustomerType().equals(CustomerConstant.SUPER_MARKET)){
            TaskEntry taskEntry = new TaskEntry();
            TaskInfo taskInfo = new TaskInfo();
            taskInfo.setTaskId(taskId);
            taskInfo.setType(TaskConstant.TYPE_PO);
            taskInfo.setSubType(TaskConstant.TASK_STORE_DIRECT);
            taskInfo.setBusinessMode(TaskConstant.MODE_DIRECT);
            taskInfo.setOrderId(inbReceiptHeader.getReceiptOrderId());
            taskInfo.setContainerId(inbReceiptHeader.getContainerId());
            taskInfo.setItemId(inbReceiptDetailList.get(0).getItemId());
            //改为uid
            taskInfo.setOperator(Long.valueOf(inbReceiptHeader.getReceiptUser()));
            taskEntry.setTaskInfo(taskInfo);
            taskId = iTaskRpcService.create(TaskConstant.TYPE_PO, taskEntry);
            iTaskRpcService.done(taskId);
        }
    }

    public Long genReceive(IbdHeader ibdHeader,List<ReceiptItem> receiptItemList){
        // TODO: 16/11/9 保存skucode和barcode的映射关系
        Map<String,String> skuMap = new HashMap<String, String>();
        for(ReceiptItem rt:receiptItemList){
            BaseinfoItem baseinfoItem = itemService.getItem(ibdHeader.getOwnerUid(), rt.getSkuId());
            if(baseinfoItem == null){
                continue;
            }
           skuMap.put(baseinfoItem.getSkuCode(),rt.getBarCode());
        }

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
            receiveDetail.setCode(skuMap.get(ibdDetail.getSkuCode()));// TODO: 16/11/9 增加国条
            receiveDetail.setReceiveId(receiveId);
            receiveDetail.setInboundQty(BigDecimal.ZERO);
            receiveDetail.setCreatedAt(DateUtils.getCurrentSeconds());
            receiveDetails.add(receiveDetail);
        }
        receiveService.insertReceive(receiveHeader,receiveDetails);
        return receiveId;
    }
    /**
     * 合并多个行项目
     * 收货时,一个订单中有多个相同商品(多行项目)
     * @param ibdDetailList
     * @param receiptEaQty 本次收货数量
     * @return
     */
    public Map<String,Object> mergeIbdDetailList(List<IbdDetail> ibdDetailList,BigDecimal receiptEaQty){
        boolean isContainEa = false;//是否包含EA
        String packName = "";
        BigDecimal packUnit = BigDecimal.ONE;
        BigDecimal orderUomQtyTotal = BigDecimal.ZERO;//订货总数(箱规)
        BigDecimal orderEaQtyTotal = BigDecimal.ZERO;//订货总数(EA)
        BigDecimal inboundEaQtyTotal = BigDecimal.ZERO;//实际收货数(EA)
        for(IbdDetail ibdDetail : ibdDetailList ){
            if(!isContainEa && ibdDetail.getPackUnit().compareTo(BigDecimal.ONE) == 0){
                isContainEa = true;//订单包含ea
                packName = ibdDetail.getPackName();
                packUnit = ibdDetail.getPackUnit();
            }
            orderUomQtyTotal = orderUomQtyTotal.add(ibdDetail.getOrderQty());
            orderEaQtyTotal = orderEaQtyTotal.add(ibdDetail.getUnitQty());
            inboundEaQtyTotal = inboundEaQtyTotal.add(ibdDetail.getInboundQty());
        }
        BigDecimal remainEaQtyTotal = orderEaQtyTotal.subtract(inboundEaQtyTotal);//待收货数量
        if(!isContainEa){
            //剩余收货数量是否为整箱
            if(remainEaQtyTotal.divideAndRemainder(ibdDetailList.get(0).getPackUnit())[1].compareTo(BigDecimal.ZERO) == 0) {
                //是整箱
                packName = ibdDetailList.get(0).getPackName();
                packUnit = ibdDetailList.get(0).getPackUnit();
            }else{
                //不是整箱
                packName = "EA";
                packUnit = BigDecimal.ONE;
            }
        }
        //本次收货数量是否是整箱,如果不是按ea算
        if(receiptEaQty.compareTo(BigDecimal.ZERO) > 0){
            if(receiptEaQty.divideAndRemainder(packUnit)[1].compareTo(BigDecimal.ZERO) == 0){
                //是整箱
            }else{
                //不是整箱
                packName = "EA";
                packUnit = BigDecimal.ONE;
            }
        }
        Map<String,Object> obdInfoMap = new HashMap<String, Object>();
        // TODO: 16/12/2 如果订单中除ea外,其他箱规不一致会有问题,但因为生成订单上层,做了箱规一致性校验,所以此处没有处理
        obdInfoMap.put("packName",packName);//本次收货箱规名称
        obdInfoMap.put("packUnit",packUnit);//本次收货箱规
        obdInfoMap.put("orderUomQtyTotal",orderUomQtyTotal);//订货总数(箱规)
        obdInfoMap.put("orderEaQtyTotal",orderEaQtyTotal);//订货总数(EA)
        obdInfoMap.put("inboundEaQtyTotal",inboundEaQtyTotal);//实际收货数(EA)
        obdInfoMap.put("remainEaQtyTotal",remainEaQtyTotal);//待收货数量(EA)
        obdInfoMap.put("remainUomQtyTotal",PackUtil.EAQty2UomQty(remainEaQtyTotal,packUnit));//待收货数量(箱规)
        return obdInfoMap;
    }

    /**
     * 合并多个行项目
     * 收货时,一个订单中有多个相同商品(多行项目)
     * @param obdDetailList
     * @param receiptEaQty 本次收货数量
     * @return
     */
    public Map<String,Object> mergeObdDetailList(List<ObdDetail> obdDetailList,BigDecimal receiptEaQty){
        boolean isContainEa = false;//是否包含EA
        String packName = "";
        BigDecimal packUnit = BigDecimal.ONE;
        BigDecimal orderUomQtyTotal = BigDecimal.ZERO;//订货总数(箱规)
        BigDecimal orderEaQtyTotal = BigDecimal.ZERO;//订货总数(EA)
        BigDecimal inboundEaQtyTotal = BigDecimal.ZERO;//实际收货数(EA)
        for(ObdDetail obdDetail : obdDetailList ){
            if(!isContainEa && obdDetail.getPackUnit().compareTo(BigDecimal.ONE) == 0){
                isContainEa = true;//订单包含ea
                packName = obdDetail.getPackName();
                packUnit = obdDetail.getPackUnit();
            }
            orderUomQtyTotal = orderUomQtyTotal.add(obdDetail.getOrderQty());
            orderEaQtyTotal = orderEaQtyTotal.add(obdDetail.getUnitQty());
            inboundEaQtyTotal = inboundEaQtyTotal.add(obdDetail.getSowQty());
        }
        BigDecimal remainEaQtyTotal = orderEaQtyTotal.subtract(inboundEaQtyTotal);//待收货数量
        if(!isContainEa){
            //剩余收货数量是否为整箱
            if(remainEaQtyTotal.divideAndRemainder(obdDetailList.get(0).getPackUnit())[1].compareTo(BigDecimal.ZERO) == 0) {
                //是整箱
                packName = obdDetailList.get(0).getPackName();
                packUnit = obdDetailList.get(0).getPackUnit();
            }else{
                //不是整箱
                packName = "EA";
                packUnit = BigDecimal.ONE;
            }
        }
        //本次收货数量是否是整箱,如果不是按ea算
        if(receiptEaQty.compareTo(BigDecimal.ZERO) > 0){
            if(receiptEaQty.divideAndRemainder(packUnit)[1].compareTo(BigDecimal.ZERO) == 0){
                //是整箱
            }else{
                //不是整箱
                packName = "EA";
                packUnit = BigDecimal.ONE;
            }
        }
        // TODO: 16/12/2 如果订单中除ea外,其他箱规不一致会有问题,但因为生成订单上层,做了箱规一致性校验,所以此处没有处理
        Map<String,Object> obdInfoMap = new HashMap<String, Object>();
        obdInfoMap.put("packName",packName);
        obdInfoMap.put("packUnit",packUnit);
        obdInfoMap.put("orderUomQtyTotal",orderUomQtyTotal);//订货总数(箱规)
        obdInfoMap.put("orderEaQtyTotal",orderEaQtyTotal);//订货总数(EA)
        obdInfoMap.put("inboundEaQtyTotal",inboundEaQtyTotal);//实际收货数(EA)
        obdInfoMap.put("remainEaQtyTotal",remainEaQtyTotal);//待收货数量(EA)
        obdInfoMap.put("remainUomQtyTotal",PackUtil.EAQty2UomQty(remainEaQtyTotal,packUnit));//待收货数量(箱规)
        return obdInfoMap;
    }
    //更新ibd订单中的收货数量
    public List<IbdDetail> getUpdateibdDetailListData (List<IbdDetail> ibdDetailList,BigDecimal inboundUnitQty){
        List<IbdDetail> updateIbdDetailList = new ArrayList<IbdDetail>();
        //本次剩余收货数量
        BigDecimal ibdReceiptQty = inboundUnitQty;
        for(IbdDetail ibdDetail : ibdDetailList){
            IbdDetail updateIbdDetail = new IbdDetail();
            //本单剩余收货数
            BigDecimal remainQty = ibdDetail.getUnitQty().subtract(ibdDetail.getInboundQty());
            if(remainQty.compareTo(BigDecimal.ZERO) <= 0){
                break;
            }
            if(ibdReceiptQty.compareTo(remainQty) >= 0){
                updateIbdDetail.setInboundQty(remainQty);
                ibdReceiptQty = ibdReceiptQty.subtract(remainQty);
            }else{
                updateIbdDetail.setInboundQty(ibdReceiptQty);
                ibdReceiptQty = ibdReceiptQty.subtract(ibdReceiptQty);
            }
            updateIbdDetail.setOrderId(ibdDetail.getOrderId());
            //updateIbdDetail.setSkuId(ibdDetail.getSkuId());
            updateIbdDetail.setDetailOtherId(ibdDetail.getDetailOtherId());
            updateIbdDetailList.add(updateIbdDetail);
            if(ibdReceiptQty.compareTo(BigDecimal.ZERO) <= 0){
                break;
            }
        }
        return updateIbdDetailList;
    }
}
