package com.lsh.wms.rf.service.receipt;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.config.PropertyUtils;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.BeanMapTransUtils;
import com.lsh.base.common.utils.StrUtils;
import com.lsh.wms.api.model.po.ReceiptItem;
import com.lsh.wms.api.model.po.ReceiptRequest;
import com.lsh.wms.api.service.po.IReceiptRfService;
import com.lsh.wms.api.service.po.IReceiptRpcService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.system.IItemTypeRpcService;
import com.lsh.wms.core.constant.CsiConstan;
import com.lsh.wms.core.constant.PoConstant;
import com.lsh.wms.core.constant.RedisKeyConstant;
import com.lsh.wms.core.constant.SoConstant;
import com.lsh.wms.core.dao.redis.RedisStringDao;
import com.lsh.wms.core.service.container.ContainerService;
import com.lsh.wms.core.service.csi.CsiSkuService;
import com.lsh.wms.core.service.csi.CsiSupplierService;
import com.lsh.wms.core.service.item.ItemLocationService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.po.PoOrderService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.system.SysUserService;
import com.lsh.wms.core.service.utils.PackUtil;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoItemLocation;
import com.lsh.wms.model.baseinfo.BaseinfoItemType;
import com.lsh.wms.model.csi.CsiSku;
import com.lsh.wms.model.csi.CsiSupplier;
import com.lsh.wms.model.po.*;
import com.lsh.wms.model.so.ObdDetail;
import com.lsh.wms.model.so.ObdHeader;
import com.lsh.wms.model.system.SysUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Project Name: lsh-wms
 * Created by fuhao
 * Date: 16/7/29
 * Time: 16/7/29.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.rf.service.receipt.
 * desc:类功能描述
 */
@Service(protocol = "rest")
@Path("order/po/receipt")
@Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class ReceiptRestService implements IReceiptRfService {

    private static Logger logger = LoggerFactory.getLogger(ReceiptRestService.class);

    @Reference
    private IReceiptRpcService iReceiptRpcService;

    @Reference
    private IItemTypeRpcService iItemTypeRpcService;

    @Autowired
    private PoOrderService poOrderService;

    @Autowired
    private CsiSkuService csiSkuService;
    @Autowired
    private CsiSupplierService csiSupplierService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemLocationService itemLocationService;
    @Autowired
    private ContainerService containerService;

    @Autowired
    private SoOrderService soOrderService;

    @Autowired
    private RedisStringDao redisStringDao;
    @Autowired
    private SysUserService sysUserService;

   // @Autowired
   // private PoReceiptService poReceiptService;



    @POST
    @Path("add")
    public String insertOrder() throws BizCheckedException, ParseException {
        Map<String, Object> request = RequestUtils.getRequest();

        List<ReceiptItem> receiptItemList = JSON.parseArray((String)request.get("items"), ReceiptItem.class);
        request.put("items", receiptItemList);
        ReceiptRequest receiptRequest = BeanMapTransUtils.map2Bean(request, ReceiptRequest.class);

        HttpSession session = RequestUtils.getSession();
        if(session.getAttribute("wareHouseId") == null) {
            receiptRequest.setWarehouseId(PropertyUtils.getLong("wareHouseId", 1L));
        } else {
            receiptRequest.setWarehouseId((Long) session.getAttribute("wareHouseId"));
        }


        /*
         *根据用户ID获取员工ID
         */


        /*if(RequestUtils.getHeader("uid") == null){
            throw new BizCheckedException("1020001", "参数不能为空");
        }*/

        String uid = RequestUtils.getHeader("uid");
        SysUser sysUser =  sysUserService.getSysUserByUid(uid);
        //员工ID
        Long staffId = null;
        if(sysUser != null){
            staffId = sysUser.getStaffId();
        }else{
            //用户不存在
            throw new BizCheckedException("2000003");
        }

        receiptRequest.setStaffId(staffId);
        receiptRequest.setReceiptUser(uid);
        receiptRequest.setReceiptTime(new Date());

        //TODO 这里根据other order id去查理论上可能会有冲突,是唯一键吗?
        IbdHeader ibdHeader = poOrderService.getInbPoHeaderByOrderOtherId(receiptRequest.getOrderOtherId());
        if(ibdHeader == null) {
            throw new BizCheckedException("2020001");
        }
        receiptRequest.setOrderId(ibdHeader.getOrderId());
        Integer orderType = ibdHeader.getOrderType();

        for(ReceiptItem receiptItem : receiptRequest.getItems()) {

            //将数量转换成EA
            String packName = receiptItem.getPackName();
            BigDecimal inboundQty = receiptItem.getInboundQty();
            BigDecimal scatterQty = receiptItem.getScatterQty();

            if("EA".equals(packName) && inboundQty.compareTo(BigDecimal.ZERO) > 0){
                throw new BizCheckedException("2021111");
            }
            BigDecimal inboundUnitQty = PackUtil.UomQty2EAQty(inboundQty,packName).add(scatterQty);
            receiptItem.setInboundQty(inboundUnitQty);
            if(inboundUnitQty.compareTo(BigDecimal.ZERO) <= 0){
                throw new BizCheckedException("2020007");
            }
            /*if(receiptItem.getProTime() == null) {
                throw new BizCheckedException("2020008");
            }*/
//            if(receiptItem.getInboundQty().compareTo(BigDecimal.ZERO) <= 0 && receiptItem.getScatterQty().compareTo(BigDecimal.ZERO) <= 0){
//                throw new BizCheckedException("2020007");//收货数量必须大于0
//            }
            //根据InbPoHeader中的OwnerUid及InbReceiptDetail中的SkuId获取Item
            //CsiSku csiSku = csiSkuService.getSkuByCode(CsiConstan.CSI_CODE_TYPE_BARCODE, receiptItem.getBarCode());

            //BaseinfoItem baseinfoItem = itemService.getItem(ibdHeader.getOwnerUid(), csiSku.getSkuId());
            BaseinfoItem baseinfoItem = this.getItem(receiptItem.getBarCode(),ibdHeader.getOwnerUid());
            //  16/11/17  因用户输入的可能是物美码,此处为国条重新赋值
            receiptItem.setBarCode(baseinfoItem.getCode());
            /*
              验证商品是否配置了拣货位
             */
            List<BaseinfoItemLocation> itemLocationList = itemLocationService.getItemLocationList(baseinfoItem.getItemId());
            if(itemLocationList == null || itemLocationList.size() == 0){
                throw new BizCheckedException("2030010");//该商品没有拣货位
            }
            /*
            按配置验证生产日期/到期日是否输入
             */
            if(PoConstant.ORDER_TYPE_CPO == orderType){
                //直流,需根据配置验证生产日期/到期日是否输入
                //  16/11/9 根据商品类型获取生产日期开关配置
                BaseinfoItemType baseinfoItemType = iItemTypeRpcService.getBaseinfoItemTypeByItemId(baseinfoItem.getItemType());
                //商品所属类型需要输入生产日期
                if(baseinfoItemType != null && 1== baseinfoItemType.getIsNeedProtime()){
                    //  16/11/9 根据配置验证生产日期是否输入
                    if(receiptItem.getProTime() == null && receiptItem.getDueTime() == null){
                        throw new BizCheckedException("2020008");//生产日期不能为空
                    }
                }
            }else{
                if (PoConstant.ORDER_TYPE_TRANSFERS != orderType){
                    //在库,且不是调拨,根据商品主数据,有保质期时,生产日期/到期日,必须输入一个
                    if(baseinfoItem.getIsShelfLifeValid() == 1 && receiptItem.getProTime() == null && receiptItem.getDueTime() == null){
                        throw new BizCheckedException("2020008");//生产日期不能为空
                    }
                }

            }


            //TODO 这里也是有风险的,没有地方限制了订单里一个商品只能有一行,尤其是直流里面一个CPO对应一个门店2个STO就会出错。
            IbdDetail ibdDetail = poOrderService.getInbPoDetailByOrderIdAndSkuCode(ibdHeader.getOrderId(), baseinfoItem.getSkuCode());
            if(ibdDetail == null){
                throw new BizCheckedException("2020001");
            }


            //这里有问题没调通,然后其实真实逻辑因该是非EA的情况下做这个限制,是可以EA收货的
            //验证箱规是否一致
            if(ibdDetail.getPackUnit().compareTo(BigDecimal.ONE) != 0 &&
                    baseinfoItem.getPackUnit().compareTo(ibdDetail.getPackUnit()) != 0){
                throw new BizCheckedException("2020105");//箱规不一致,不能收货
            }

            /*
            验证保质期是否有效
             */
            //取出是否检验保质期字段 exceptionReceipt = 0 校验 = 1不校验
            Integer exceptionReceipt = ibdDetail.getExceptionReceipt();
            String exceptionCode = receiptItem.getExceptionCode() == null ? "" :receiptItem.getExceptionCode();// 16/11/10 从请求参数中获取例外代码
            if(StringUtils.isNotEmpty(exceptionCode)){
                receiptItem.setIsException(1);//1表示例外收货
            }
            if(receiptItem.getProTime() != null || receiptItem.getDueTime() != null) {
                //调拨类型的单据不校验保质期
                if (PoConstant.ORDER_TYPE_TRANSFERS != ibdHeader.getOrderType()) {
                    if (exceptionReceipt != 1) {
                        // 16/7/20   商品信息是否完善,怎么排查.2,保质期例外怎么验证?
                        //保质期判断,如果失败抛出异常
                        iReceiptRpcService.checkProTime(baseinfoItem, receiptItem.getProTime(),receiptItem.getDueTime(), exceptionCode);
                    }
                }
            }
            //如果没有输入生产日期和到期日
            //TODO 没有输入生产日期的不能填现在的日期,逻辑不合理,而且容易对后面的先进先出逻辑造成困扰
            if(receiptItem.getProTime() == null && receiptItem.getDueTime() == null) {
                receiptItem.setProTime(new Date());
            }else if(receiptItem.getProTime() == null && receiptItem.getDueTime() != null){
                //根据到期日计算生产日期  //  16/11/12  根据生产日期-保质期计算
                BigDecimal shelfLife = baseinfoItem.getShelfLife();//保质期天数
                if(shelfLife == null || shelfLife.compareTo(BigDecimal.ZERO)<=0){
                    throw new BizCheckedException("2020106");
                }else{
                    int onedayMs = 24 * 60 * 60 * 1000;
                    BigDecimal shelfLifeMs = shelfLife.multiply(BigDecimal.valueOf(onedayMs));
                    long betweenTime = receiptItem.getDueTime().getTime() - shelfLifeMs.longValue();
                    receiptItem.setProTime(new Date(betweenTime));
                }
            }

            receiptItem.setSkuCode(baseinfoItem.getSkuCode());
            receiptItem.setItemId(baseinfoItem.getItemId());
            receiptItem.setSkuId(baseinfoItem.getSkuId());
            receiptItem.setSkuName(ibdDetail.getSkuName());
            receiptItem.setPackUnit(ibdDetail.getPackUnit());
            //receiptItem.setPackName(ibdDetail.getPackName());
            receiptItem.setMadein(baseinfoItem.getProducePlace());
        }

        //TODO check list
        /*
            只是判断了商品级别的
            没有判断订单级别的一些数据
            比如:
                供商是否正确维护 OK
                直流的门店信息是否维护
                直流的集货道没有维护会有什么问题?
             。。。。。。
         */
        String supplierCode = ibdHeader.getSupplierCode();//供商编码
        CsiSupplier csiSupplier = csiSupplierService.getSupplier(supplierCode,ibdHeader.getOwnerUid());
        if(csiSupplier == null){
            throw new BizCheckedException("2020109");//供商信息不存在
        }
        receiptRequest.setSupplierId(csiSupplier.getSupplierId());
        receiptRequest.setItems(receiptItemList);
        receiptRequest.setOwnerId(ibdHeader.getOwnerUid());


        if(PoConstant.ORDER_TYPE_CPO == orderType && receiptRequest.getStoreId() != null){
            iReceiptRpcService.addStoreReceipt(receiptRequest);
        }else{
            iReceiptRpcService.insertOrder(receiptRequest);
        }
        return JsonUtils.SUCCESS(new HashMap<String, Object>() {
            {
                put("response", true);
            }
        });
    }

//    @POST
//    @Path("addStoreReceipt")
//    public String addStoreReceipt() throws BizCheckedException, ParseException {
//        Map<String, Object> request = RequestUtils.getRequest();
//
//        List<ReceiptItem> receiptItemList = JSON.parseArray((String)request.get("items"), ReceiptItem.class);
//        //request.put("items", receiptItemList);
//
//        ReceiptRequest receiptRequest = BeanMapTransUtils.map2Bean(request, ReceiptRequest.class);
//
//        HttpSession session = RequestUtils.getSession();
//
//        if(session.getAttribute("wareHouseId") == null) {
//            receiptRequest.setWarehouseId(PropertyUtils.getLong("wareHouseId", 1L));
//        } else {
//            receiptRequest.setWarehouseId((Long) session.getAttribute("wareHouseId"));
//        }
//        receiptRequest.setReceiptUser(RequestUtils.getHeader("uid"));
//        Map<String,Object> map = new HashMap<String, Object>();
//        map.put("uid",RequestUtils.getHeader("uid"));
//        Long staffId = staffService.getStaffList(map).get(0).getStaffId();
//        receiptRequest.setStaffId(staffId);
//
//        //取出订单ID
//        String orderIds = (String)request.get("orderIds");
//
//        List<IbdHeader> ibdHeaderList = poOrderService.getIbdListOrderByDate(orderIds);
//        List<ReceiptItem> items = new ArrayList<ReceiptItem>();
//
//
//        ReceiptItem receiptItem = receiptItemList.get(0);
//
//        //取出数量
//        BigDecimal packQty = receiptItem.getInboundQty();
//        BigDecimal unitQty = receiptItem.getUnitQty();
//
//        BigDecimal packUnit = PackUtil.Uom2PackUnit(receiptItem.getPackName());
//
//        BigDecimal sumQty = packQty.multiply(packUnit).add(unitQty) ;
//
//        //根据InbPoHeader中的OwnerUid及InbReceiptDetail中的SkuId获取Item
//        CsiSku csiSku = csiSkuService.getSkuByCode(CsiConstan.CSI_CODE_TYPE_BARCODE, receiptItem.getBarCode());
//
//
//        for(IbdHeader ibdHeader : ibdHeaderList){
//            Long orderId = ibdHeader.getOrderId();
//            BaseinfoItem baseinfoItem = itemService.getItem(ibdHeader.getOwnerUid(), csiSku.getSkuId());
//
//            IbdDetail ibdDetail = poOrderService.getInbPoDetailByOrderIdAndSkuCode(ibdHeader.getOrderId(), baseinfoItem.getSkuCode());
//
//            if(ibdDetail == null){
//                throw new BizCheckedException("2020001");
//            }
//
//            receiptItem.setSkuName(ibdDetail.getSkuName());
//            receiptItem.setPackUnit(ibdDetail.getPackUnit());
//            //receiptItem.setPackName(ibdDetail.getPackName());
//            receiptItem.setMadein(baseinfoItem.getProducePlace());
//            receiptItem.setOrderId(orderId);
//            // TODO: 2016/10/8 转化为ea进行比较
//            if(sumQty.compareTo(ibdDetail.getUnitQty()) <= 0){
//                receiptItem.setInboundQty(sumQty);
//                break;
//            }else{
//                receiptItem.setInboundQty(ibdDetail.getUnitQty());
//                sumQty = sumQty.subtract(ibdDetail.getUnitQty());
//            }
//            items.add(receiptItem);
//        }
//
//
//        receiptRequest.setReceiptTime(new Date());
//        receiptRequest.setItems(items);
//
//        iReceiptRpcService.addStoreReceipt(receiptRequest);
//
//        return JsonUtils.SUCCESS(new HashMap<String, Object>() {
//            {
//                put("response", true);
//            }
//        });
//    }

    @POST
    @Path("getorderinfo")
    public String getPoDetailByOrderIdAndBarCode(@FormParam("orderOtherId") String orderOtherId,@FormParam("containerId") Long containerId, @FormParam("barCode") String barCode) throws BizCheckedException {
        if(StringUtils.isBlank(orderOtherId) || StringUtils.isBlank(barCode)|| containerId ==null) {
            throw new BizCheckedException("1020001", "参数不能为空");
        }

        IbdHeader ibdHeader = poOrderService.getInbPoHeaderByOrderOtherId(orderOtherId);

        if (ibdHeader == null) {
            throw new BizCheckedException("2020001");
        }

        boolean isCanReceipt = ibdHeader.getOrderStatus() == PoConstant.ORDER_THROW || ibdHeader.getOrderStatus() == PoConstant.ORDER_RECTIPT_PART || ibdHeader.getOrderStatus() == PoConstant.ORDER_RECTIPTING;
        if (!isCanReceipt) {
            throw new BizCheckedException("2020002");
        }

        if(!containerService.isContainerCanUse(containerId)){
            throw new BizCheckedException("2000002");
        }

        BaseinfoItem baseinfoItem = this.getItem(barCode,ibdHeader.getOwnerUid());

        if(baseinfoItem.getIsInfoIntact() == 0){
            throw new BizCheckedException("2020104");//商品信息不完整,不能收货
        }

        //根据InbPoHeader中的OwnerUid及InbReceiptDetail中的SkuId获取Item
        CsiSku csiSku = csiSkuService.getSkuByCode(CsiConstan.CSI_CODE_TYPE_BARCODE, baseinfoItem.getCode());
        if (null == csiSku || csiSku.getSkuId() == null) {
            throw new BizCheckedException("2020022");
        }

        //BaseinfoItem baseinfoItem = itemService.getItem(ibdHeader.getOwnerUid(), csiSku.getSkuId());

        IbdDetail ibdDetail = poOrderService.getInbPoDetailByOrderIdAndSkuCode(ibdHeader.getOrderId(),baseinfoItem.getSkuCode());

        if (ibdDetail == null) {
            throw new BizCheckedException("2020004");
        }


        //校验之后修改订单状态为收货中 第一次收货将订单改为收货中
        if(ibdHeader.getOrderStatus() == PoConstant.ORDER_THROW){
            ibdHeader.setOrderStatus(PoConstant.ORDER_RECTIPTING);
            poOrderService.updateInbPoHeader(ibdHeader);
        }


        Map<String, Object> orderInfoMap = new HashMap<String, Object>();
        orderInfoMap.put("skuName", ibdDetail.getSkuName());
        orderInfoMap.put("sukCode",Long.parseLong(baseinfoItem.getSkuCode()));
        orderInfoMap.put("orderId",ibdHeader.getOrderId());
        orderInfoMap.put("barCode",baseinfoItem.getCode());
        //orderInfoMap.put("packName", "H01");
        //orderInfoMap.put("packName", ibdDetail.getPackName());

        //剩余数量。
        //判断是否为整箱
        BigDecimal inboundQty = ibdDetail.getInboundQty();
        if(inboundQty.divideAndRemainder(ibdDetail.getPackUnit())[1].compareTo(BigDecimal.ZERO) == 0) {
            orderInfoMap.put("orderQty",ibdDetail.getOrderQty().subtract(ibdDetail.getInboundQty().divide(ibdDetail.getPackUnit())));
            orderInfoMap.put("packName",ibdDetail.getPackName());
        }else{
            orderInfoMap.put("orderQty",PackUtil.UomQty2EAQty(ibdDetail.getOrderQty(),ibdDetail.getPackUnit()).subtract(inboundQty));
            orderInfoMap.put("packName","EA");
        }

//        BigDecimal orderQty = ibdDetail.getOrderQty().subtract(ibdDetail.getInboundQty().divide(ibdDetail.getPackUnit()));
//        orderInfoMap.put("orderQty", orderQty);
        orderInfoMap.put("batchNeeded", baseinfoItem.getBatchNeeded());
        //码盘规则
        orderInfoMap.put("pile",baseinfoItem.getPileX()+ "*" + baseinfoItem.getPileY() + "*" + baseinfoItem.getPileZ());
        Integer orderType = ibdHeader.getOrderStatus();
        if(orderType == PoConstant.ORDER_TYPE_CPO){
            //直流,根据商品类型判断是否需要输入
            BaseinfoItemType baseinfoItemType = iItemTypeRpcService.getBaseinfoItemTypeByItemId(baseinfoItem.getItemType());
            if(baseinfoItemType != null && baseinfoItemType.getIsNeedProtime() == 1){
                orderInfoMap.put("isNeedProTime",1);//16/12/6  需要输入生产日期
            }else{
                orderInfoMap.put("isNeedProTime",0);//否
            }
        }else if (PoConstant.ORDER_TYPE_TRANSFERS != orderType){
            //非直流,非调拨单 根据商品是否有保质期判断是否需要输入
            if(baseinfoItem.getIsShelfLifeValid() == 1){
                orderInfoMap.put("isNeedProTime",1);//16/12/6  需要输入生产日期
            }else{
                orderInfoMap.put("isNeedProTime",0);//否
            }
        }

        return JsonUtils.SUCCESS(orderInfoMap);
    }


    /*@POST
    @Path("getStoreInfo")
    public String getStoreInfo(@FormParam("storeId") String storeId,@FormParam("containerId") Long containerId, @FormParam("barCode") String barCode,@FormParam("orderOtherId") String orderOtherId) throws BizCheckedException {
        //参数有效性验证
        if(StringUtils.isBlank(storeId) || StringUtils.isBlank(barCode) || StringUtils.isBlank(orderOtherId)|| containerId ==null) {
            throw new BizCheckedException("1020001", "参数不能为空");
        }
        //判断门店是否有订货
        Map<String,Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("deliveryCode",storeId);
        mapQuery.put("orderType", SoConstant.ORDER_TYPE_DIRECT);
        mapQuery.put("orderStatus",1);
        List<ObdHeader> obdHeaderList = soOrderService.getOutbSoHeaderList(mapQuery);
        if(obdHeaderList.size() <= 0){
            throw new BizCheckedException("2020100");
        }

        *//*
         *判断托盘是否可用
         *//*

        String containerStoreKey = StrUtils.formatString(RedisKeyConstant.CONTAINER_STORE,containerId);
        //从缓存中获取该托盘对应的店铺信息
        String oldStoreId = redisStringDao.get(containerStoreKey);
        if(!storeId.equals(oldStoreId)){
            //验证托盘是否可用
            if(!containerService.isContainerCanUse(containerId)){
                throw new BizCheckedException("2000002");
            }else{
                //可用,存入缓存
                redisStringDao.set(containerStoreKey,storeId,2, TimeUnit.DAYS);
            }
        }

        CsiSku csiSku = csiSkuService.getSkuByCode(CsiConstan.CSI_CODE_TYPE_BARCODE, barCode);
        if (null == csiSku || csiSku.getSkuId() == null) {
            throw new BizCheckedException("2020022");
        }

        //该标志用来判断detail中是否存在该商品
        Boolean flag = false;
        //用来存查询细单
        Map<String,ObdDetail> detailMap = new HashMap<String, ObdDetail>();
        //查找保质期天数
        BigDecimal shelfLife = BigDecimal.ZERO;
        //skucode
        String skuCode = "";
        for(ObdHeader obdHeader : obdHeaderList){
            Long orderId = obdHeader.getOrderId();

            BaseinfoItem baseinfoItem = itemService.getItem(obdHeader.getOwnerUid(), csiSku.getSkuId());
            shelfLife = baseinfoItem.getShelfLife();
            skuCode = baseinfoItem.getSkuCode();

            Map<String,Object> params = new HashMap<String, Object>();
            params.put("orderId",orderId);
            params.put("itemId",baseinfoItem.getItemId());
            List<ObdDetail> obdDetailList = soOrderService.getOutbSoDetailList(mapQuery);
            if(obdDetailList.size() > 0 ){
                flag = true;
            }
            detailMap.put(obdHeader.getOrderOtherId(),obdDetailList.get(0));


        }
        if(flag == false){
            throw new BizCheckedException("2020101");
        }

        //map1存放订单信息
        final List<Map<String,Object>> list1 = new ArrayList<Map<String, Object>>();
        final Map<String,Object> map2 = new HashMap<String, Object>();

//        StringBuilder sb = new StringBuilder();
//        BigDecimal sumPackQty = BigDecimal.ZERO;//剩余应收数量 包装维度
//        BigDecimal sumUnitQty = BigDecimal.ZERO;//基本单位维度
//        String packName = "";
//        BigDecimal packUnit = BigDecimal.ZERO;
        // TODO: 2016/10/9 先查询出ibdHeader ibdDetail
        IbdHeader ibdHeader = poOrderService.getInbPoHeaderByOrderOtherId(orderOtherId);
        if (ibdHeader == null) {
            throw new BizCheckedException("2020001");
        }
        //是否可收货 add by zhl
        boolean isCanReceipt = ibdHeader.getOrderStatus() == PoConstant.ORDER_THROW || ibdHeader.getOrderStatus() == PoConstant.ORDER_RECTIPT_PART || ibdHeader.getOrderStatus() == PoConstant.ORDER_RECTIPTING;
        if (!isCanReceipt) {
            throw new BizCheckedException("2020002");
        }
        IbdDetail ibdDetail = poOrderService.getInbPoDetailByOrderIdAndSkuCode(ibdHeader.getOrderId(),skuCode);
        if (ibdDetail == null) {
            throw new BizCheckedException("2020004");
        }
        //查询对应的obd
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("ibdOtherId",orderOtherId);
        params.put("ibdDetailId",ibdDetail.getDetailOtherId());

        List<IbdObdRelation> ibdObdRelations = poOrderService.getIbdObdRelationList(params);
        if(ibdObdRelations.size() <= 0){
            throw new BizCheckedException("2021000");
        }

        // TODO: 2016/10/9 根据ibd来找对应的obd
        for(IbdObdRelation ibdObdRelation : ibdObdRelations){
            String obdOtherId = ibdObdRelation.getObdOtherId();
            String obdDetailId = ibdObdRelation.getObdDetailId();

            ObdHeader obdHeader = soOrderService.getOutbSoHeaderByOrderOtherId(obdOtherId);

            if(storeId.equals(obdHeader.getDeliveryCode())){
                ObdDetail obdDetail = soOrderService.getObdDetailByOrderIdAndDetailOtherId(obdHeader.getOrderId(),obdDetailId);

                map2.put("location","J"+storeId);
                map2.put("orderId",ibdHeader.getOrderId());
                map2.put("barCode",barCode);
                map2.put("skuName",csiSku.getSkuName());
                map2.put("orderQty",obdDetail.getOrderQty());
                map2.put("packName",ibdDetail.getPackName());
                map2.put("packUnit",ibdDetail.getPackUnit());

                //将obd orderId存入redis
                String key = StrUtils.formatString(RedisKeyConstant.PO_STORE, ibdHeader.getOrderId(), storeId);
                redisStringDao.set(key,obdHeader.getOrderId());

                break;
            }


        }

//        //根据obdOtherId 找对应的ibd
//        for(Map.Entry<String, ObdDetail> entry : detailMap.entrySet()){
//            Map<String,Object> map1 = new HashMap<String, Object>();
//            String obdOtherId = entry.getKey();
//            ObdDetail obdDetail = entry.getValue();
//            String obdDetailId = obdDetail.getDetailOtherId();
//            // TODO: 2016/9/29 一条obd明细应该来自一条ibd明细。
//            IbdObdRelation ibdObdRef =  poOrderService.getIbdObdRelationListByObd(obdOtherId,obdDetailId).get(0);
//            String ibdOtherId = ibdObdRef.getIbdOtherId();
//            String ibdDetailId = ibdObdRef.getIbdDetailId();
//            //找ibd订单
//            IbdHeader ibdHeader = poOrderService.getInbPoHeaderByOrderOtherId(ibdOtherId);
//            Long orderId = ibdHeader.getOrderId();
//            //得到ibd_detail
//            IbdDetail ibdDetail = poOrderService.getInbPoDetailByOrderIdAndDetailOtherId(orderId,ibdDetailId);
//
//
//            map1.put("orderId",orderId);
//            map1.put("packName",ibdDetail.getPackName());
//            map1.put("obdQty",obdDetail.getUnitQty());
//            map1.put("createTime",new Date());
//            list1.add(map1);
//
//            sb.append(orderId+",");
//
////
////            //数量
////            BigDecimal orderQty = ibdDetail.getOrderQty().subtract(ibdDetail.getInboundQty());
////            sumPackQty = sumPackQty.add(orderQty);
//            packName = ibdDetail.getPackName();
//            packUnit = ibdDetail.getPackUnit();
//            //订单的数量
//            sumUnitQty = sumUnitQty.add(obdDetail.getUnitQty());
//
//
//        }
        // TODO: 2016/10/8  sumPackQty表示含有的整件个数,sumUnitQty表示余数散件的个数


//        map2.put("orderIds",sb.substring(0,sb.length()-1));
//        map2.put("barCode",barCode);
//        map2.put("skuName",csiSku.getSkuName());
//        map2.put("sumPackQty",sumUnitQty.divide(packUnit,0,BigDecimal.ROUND_HALF_EVEN));
//        map2.put("sumUnitQty",sumUnitQty.remainder(packUnit));
//        map2.put("packName",packName);
        //推算最晚生产日期
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(calendar.DAY_OF_YEAR,-shelfLife.intValue());
        Date pro = calendar.getTime();
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        map2.put("proTime",sd.format(pro));

        //校验之后修改订单状态为收货中 第一次收货将订单改为收货中
        if(ibdHeader.getOrderStatus() == PoConstant.ORDER_THROW){
            ibdHeader.setOrderStatus(PoConstant.ORDER_RECTIPTING);
            poOrderService.updateInbPoHeader(ibdHeader);
        }


        return JsonUtils.SUCCESS(new HashMap<String,Object>(){
            {
                put("receiptInfo",map2);
            }

        });
    }*/
    //获取门店收获信息
    @POST
    @Path("getStoreInfo")
    public String getStoreInfo(@FormParam("storeId")String storeId,@FormParam("containerId")Long containerId,@FormParam("barCode")String barCode,@FormParam("orderOtherId")String orderOtherId)throws BizCheckedException{

        //参数有效性验证
        if(StringUtils.isBlank(storeId)||StringUtils.isBlank(barCode)||StringUtils.isBlank(orderOtherId)||containerId==null){
            throw new BizCheckedException("1020001","参数不能为空");
        }

        /*
        *判断托盘是否可用
        */

        String containerStoreKey=StrUtils.formatString(RedisKeyConstant.CONTAINER_STORE,containerId);
        //从缓存中获取该托盘对应的店铺信息
        String oldStoreId=redisStringDao.get(containerStoreKey);
        if(!storeId.equals(oldStoreId)){
            //验证托盘是否可用
            if(!containerService.isContainerCanUse(containerId)){
                throw new BizCheckedException("2000002");
            }else{
            //可用,存入缓存
                redisStringDao.set(containerStoreKey,storeId,2,TimeUnit.DAYS);
            }
        }

        //po单是否存在
        IbdHeader ibdHeader=poOrderService.getInbPoHeaderByOrderOtherId(orderOtherId);
        if(ibdHeader==null){
            throw new BizCheckedException("2020001");
        }


       BaseinfoItem baseinfoItem = this.getItem(barCode,ibdHeader.getOwnerUid());

        logger.info("1111111111~~~~~~~~~~~~~~~~~~~~~ baeseinfo_item  : " + JSON.toJSONString(baseinfoItem) +" ~~~~~~~~~~~~~~~~");
       if(baseinfoItem.getIsInfoIntact() == 0){
           logger.info("22222222222~~~~~~~~~~~~~~~~ IsInfoIntact : " + baseinfoItem.getIsInfoIntact());
           throw new BizCheckedException("2020104");//商品信息不完整,不能收货
       }
        logger.info("33333333333333333");

        //是否可收货
        boolean isCanReceipt=ibdHeader.getOrderStatus()==PoConstant.ORDER_THROW||ibdHeader.getOrderStatus()==PoConstant.ORDER_RECTIPT_PART||ibdHeader.getOrderStatus()==PoConstant.ORDER_RECTIPTING;
        if(!isCanReceipt){
            throw new BizCheckedException("2020002");
        }


        //查找保质期天数
        BigDecimal shelfLife=baseinfoItem.getShelfLife();
        //skucode
        String skuCode=baseinfoItem.getSkuCode();

        IbdDetail ibdDetail=poOrderService.getInbPoDetailByOrderIdAndSkuCode(ibdHeader.getOrderId(),skuCode);
        if(ibdDetail==null){
            throw new BizCheckedException("2020004");
        }
        //查询对应的ibdobdrelation
        Map<String,Object>params=new HashMap<String,Object>();
        params.put("ibdOtherId",orderOtherId);
        params.put("ibdDetailId",ibdDetail.getDetailOtherId());

        List<IbdObdRelation> ibdObdRelations = poOrderService.getIbdObdRelationList(params);
        if(ibdObdRelations!=null&&ibdObdRelations.size()<=0){
            throw new BizCheckedException("2021000");
        }

        final Map<String,Object>map2=new HashMap<String,Object>();
        //用来标记该门店是否有订货
        boolean isOrder=false;
        //用来标记商品是否在订货范围内
        boolean isGoods=false;

        //根据ibdobdrelation来找对应的obd
        for(IbdObdRelation ibdObdRelation:ibdObdRelations){
            String obdOtherId=ibdObdRelation.getObdOtherId();
            String obdDetailId=ibdObdRelation.getObdDetailId();

            ObdHeader obdHeader=soOrderService.getOutbSoHeaderByOrderOtherIdAndType(obdOtherId,SoConstant.ORDER_TYPE_DIRECT);
            //该门店有新建的直流出库订单
            isOrder=obdHeader!=null&&storeId.equals(obdHeader.getDeliveryCode())
                    &&SoConstant.ORDER_TYPE_DIRECT==obdHeader.getOrderType()
                    &&1==obdHeader.getOrderStatus();

            if(isOrder){
                ObdDetail obdDetail=soOrderService.getObdDetailByOrderIdAndDetailOtherId(obdHeader.getOrderId(),obdDetailId);
                if(obdDetail==null){
                    throw new BizCheckedException("2020010");
                }
                //该商品是否在门店订货范围内
                isGoods=baseinfoItem.getItemId().equals(obdDetail.getItemId());
                if(isGoods){
//                    //查询inbReceiptHeader是否存在 根据托盘查询
//                    Map<String,Object> mapQuery = new HashMap<String, Object>();
//                    mapQuery.put("containerId",containerId);
//                    InbReceiptHeader inbReceiptHeader = poReceiptService.getInbReceiptHeaderByParams(mapQuery);
//
//                    if(inbReceiptHeader != null ){
//                        Long receiptId = inbReceiptHeader.getReceiptOrderId();
//                        InbReceiptDetail inbReceiptDetail = poReceiptService.getInbReceiptDetailListByReceiptIdAndCode(receiptId,barCode);
//                        if(inbReceiptDetail != null){
//                            BigDecimal inboundQty = inbReceiptDetail.getInboundQty();
//                            orderQty = orderQty.subtract(inboundQty);
//                        }
//                    }
                    //实际播种的数量。
                    BigDecimal sowQty = obdDetail.getSowQty();
                    BigDecimal orderQty = obdDetail.getOrderQty();
                    map2.put("location","J"+storeId);
                    map2.put("orderId",ibdHeader.getOrderId());
                    map2.put("barCode",baseinfoItem.getCode());
                    map2.put("skuCode",Long.parseLong(baseinfoItem.getSkuCode()));
                    map2.put("skuName",baseinfoItem.getSkuName());
                    //剩余数量。
                    //判断是否为整箱
                    BigDecimal subQty = orderQty.multiply(obdDetail.getPackUnit()).subtract(sowQty);
                    if(subQty.divideAndRemainder(obdDetail.getPackUnit())[1].compareTo(BigDecimal.ZERO) == 0) {
                        map2.put("orderQty",orderQty.subtract(PackUtil.EAQty2UomQty(sowQty,obdDetail.getPackUnit())));
                        map2.put("packName",obdDetail.getPackName());
                        map2.put("packUnit",obdDetail.getPackUnit());
                    }else{
                        map2.put("orderQty",PackUtil.UomQty2EAQty(orderQty,obdDetail.getPackUnit()).subtract(sowQty));
                        map2.put("packName","EA");
                        map2.put("packUnit",1);
                    }


                    map2.put("pile",baseinfoItem.getPileX()+ "*" + baseinfoItem.getPileY() + "*" + baseinfoItem.getPileZ());
                    BaseinfoItemType baseinfoItemType = iItemTypeRpcService.getBaseinfoItemTypeByItemId(baseinfoItem.getItemType());
                    if(baseinfoItemType != null && baseinfoItemType.getIsNeedProtime() == 1){
                        map2.put("isNeedProTime",1);// TODO: 16/11/15 是否需要输入生产日期
                    }else{
                        map2.put("isNeedProTime",0);
                    }
//                    //剩余数量存入redis po订单号 托盘码 barcode作为key
//                    String qtyKey = StrUtils.formatString(RedisKeyConstant.STORE_QTY,ibdHeader.getOrderId(),containerId,barCode);
//                    redisStringDao.set(qtyKey,orderQty);

                    //将obdorderId存入redis
                    String key=StrUtils.formatString(RedisKeyConstant.PO_STORE,ibdHeader.getOrderId(),storeId);
                    redisStringDao.set(key,obdHeader.getOrderId()+","+obdDetail.getDetailOtherId());
                    break;
                }

            }

        }
        if(!isOrder){
            throw new BizCheckedException("2020100");
        }
        if(!isGoods){
            throw new BizCheckedException("2020004");
        }
        //推算最晚生产日期
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(calendar.DAY_OF_YEAR,-shelfLife.intValue());
        Date pro=calendar.getTime();
        SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd");
        map2.put("proTime",sd.format(pro));

        //校验之后修改订单状态为收货中第一次收货将订单改为收货中
        if(ibdHeader.getOrderStatus()==PoConstant.ORDER_THROW){
            ibdHeader.setOrderStatus(PoConstant.ORDER_RECTIPTING);
            poOrderService.updateInbPoHeader(ibdHeader);
        }

        return JsonUtils.SUCCESS(new HashMap<String,Object>(){
            {
                put("receiptInfo",map2);
            }

        });
    }

    /**
     * 判断物美码还是国条,来获取对应商品信息
     * @param barCode 值为物美码或国条
     * @param ownerId
     * @return
     */
    private BaseinfoItem getItem(String barCode , Long ownerId){
        BaseinfoItem baseinfoItem = new BaseinfoItem();
        if (barCode.length() == 6 || barCode.length() == 9){
            //barCode的值为物美码
            while (barCode.length() < 18){
                StringBuffer sb = new StringBuffer();
                sb.append("0").append(barCode);
                barCode = sb.toString();
            }
            List<BaseinfoItem> items = itemService.getItemsBySkuCode(ownerId,barCode);
            if(items.size() <= 0){
                throw new BizCheckedException("2900001");
            }
            baseinfoItem = items.get(items.size()-1);

        }else{
            //barCode值为国条
            //商品是否存在
            CsiSku csiSku=csiSkuService.getSkuByCode(CsiConstan.CSI_CODE_TYPE_BARCODE,barCode);
            if(null==csiSku||csiSku.getSkuId()==null){
                throw new BizCheckedException("2020022");
            }
            //获取货主商品信息
            baseinfoItem=itemService.getItem(ownerId,csiSku.getSkuId());
            if(baseinfoItem==null){
                throw new BizCheckedException("2900001");
            }

        }
        return baseinfoItem;

    }
    //获取门店收获orderOtherId
    @POST
    @Path("getOrderOtherIdList")
    public String getOrderOtherIdList(@FormParam("storeId")String storeId,@FormParam("barCode")String barCode)throws BizCheckedException{
        //参数有效性验证
        if(StringUtils.isBlank(storeId)||StringUtils.isBlank(barCode)){
            throw new BizCheckedException("1020001","参数不能为空");
        }
        //获取门店的obdHeaderList
        Map<String, Object> query = new HashMap<String, Object>();
        query.put("deliveryCode",storeId);
        query.put("orderType",SoConstant.ORDER_TYPE_DIRECT);
        query.put("orderStatus",1);
        List<ObdHeader>   headerList = soOrderService.getOutbSoHeaderList(query);

        Set<String> ibdOtherIdList = new HashSet<String>();
        //获取ibdObdRelation
        for(ObdHeader obdHeader : headerList){
            BaseinfoItem baseinfoItem = this.getItem(barCode,obdHeader.getOwnerUid());
            List<ObdDetail> obdDetailList = soOrderService.getObdDetailListByOrderIdAndItemId(obdHeader.getOrderId(),baseinfoItem.getItemId());
            if(obdDetailList == null || obdDetailList.size() <= 0){
                continue;
            }
            //查询对应的ibdobdrelation
            Map<String,Object>params=new HashMap<String,Object>();
            params.put("obdOtherId",obdHeader.getOrderOtherId());
            for(ObdDetail obdDetail : obdDetailList){
                params.put("obdDetailId",obdDetail.getDetailOtherId());
                List<IbdObdRelation> ibdObdRelations = poOrderService.getIbdObdRelationList(params);
                if(ibdObdRelations == null || ibdObdRelations.size()<=0){
                    continue;
                }
                for(IbdObdRelation ibdObdRelation : ibdObdRelations){
                    String ibdOtherId=ibdObdRelation.getIbdOtherId();
                    String ibdDetailId=ibdObdRelation.getIbdDetailId();
                    IbdHeader ibdHeader = poOrderService.getInbPoHeaderByOrderOtherId(ibdOtherId);
                    if(ibdHeader == null){
                        continue;
                    }
                    //是否可收货
                    boolean isCanReceipt=ibdHeader.getOrderStatus()==PoConstant.ORDER_THROW||ibdHeader.getOrderStatus()==PoConstant.ORDER_RECTIPT_PART||ibdHeader.getOrderStatus()==PoConstant.ORDER_RECTIPTING;
                    if(!isCanReceipt){
                        continue;
                    }
                    IbdDetail ibdDetail = poOrderService.getInbPoDetailByOrderIdAndDetailOtherId(ibdHeader.getOrderId(),ibdDetailId);
                    if(ibdDetail == null){
                        continue;
                    }
                    if(!baseinfoItem.getItemId().equals(obdDetail.getItemId())){
                        //该商品不是门店订货商品
                        continue;
                    }
                    ibdOtherIdList.add(ibdHeader.getOrderOtherId());
                }
            }

        }
        Map<String,Object> returnData = new HashMap<String, Object>();
        returnData.put("orderOtherIdList",ibdOtherIdList);
        return JsonUtils.SUCCESS(returnData);

    }


}
