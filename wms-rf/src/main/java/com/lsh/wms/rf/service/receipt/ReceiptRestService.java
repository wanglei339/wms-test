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
import com.lsh.wms.core.constant.CsiConstan;
import com.lsh.wms.core.constant.PoConstant;
import com.lsh.wms.core.constant.RedisKeyConstant;
import com.lsh.wms.core.constant.SoConstant;
import com.lsh.wms.core.dao.redis.RedisStringDao;
import com.lsh.wms.core.service.container.ContainerService;
import com.lsh.wms.core.service.csi.CsiSkuService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.po.PoOrderService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.staff.StaffService;
import com.lsh.wms.core.service.system.SysUserService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoStaffInfo;
import com.lsh.wms.model.csi.CsiSku;
import com.lsh.wms.model.po.IbdDetail;
import com.lsh.wms.model.po.IbdHeader;
import com.lsh.wms.model.po.IbdObdRelation;
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

    @Autowired
    private PoOrderService poOrderService;

    @Autowired
    private CsiSkuService csiSkuService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private ContainerService containerService;

    @Autowired
    private StaffService staffService;
    @Autowired
    private SoOrderService soOrderService;

    @Autowired
    private RedisStringDao redisStringDao;
    @Autowired
    private SysUserService sysUserService;



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

        receiptRequest.setReceiptUser(RequestUtils.getHeader("uid"));

        /*
         *根据用户ID获取员工ID
         */
        //员工ID
        Long staffId = null;

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("uid",RequestUtils.getHeader("uid"));
        List<SysUser> userList =  sysUserService.getSysUserList(map);

        if(userList != null && userList.size() > 0){
            staffId = userList.get(0).getStaffId();
        }
        if(staffId == null){
            //用户不存在
            throw new BizCheckedException("2000003");
        }

        receiptRequest.setStaffId(staffId);



        receiptRequest.setReceiptTime(new Date());

        IbdHeader ibdHeader = poOrderService.getInbPoHeaderByOrderOtherId(receiptRequest.getOrderOtherId());
        if(ibdHeader == null) {
            throw new BizCheckedException("2020001");
        }


        for(ReceiptItem receiptItem : receiptRequest.getItems()) {
            if(receiptItem.getProTime() == null) {
                throw new BizCheckedException("2020008");
            }

            //根据InbPoHeader中的OwnerUid及InbReceiptDetail中的SkuId获取Item
            CsiSku csiSku = csiSkuService.getSkuByCode(CsiConstan.CSI_CODE_TYPE_BARCODE, receiptItem.getBarCode());

            BaseinfoItem baseinfoItem = itemService.getItem(ibdHeader.getOwnerUid(), csiSku.getSkuId());

            IbdDetail ibdDetail = poOrderService.getInbPoDetailByOrderIdAndSkuCode(ibdHeader.getOrderId(), baseinfoItem.getSkuCode());

            if(ibdDetail == null){
                throw new BizCheckedException("2020001");
            }

            receiptItem.setSkuName(ibdDetail.getSkuName());
            receiptItem.setPackUnit(ibdDetail.getPackUnit());
            receiptItem.setPackName(ibdDetail.getPackName());
            receiptItem.setMadein(baseinfoItem.getProducePlace());
        }

        receiptRequest.setItems(receiptItemList);
        Integer orderType = ibdHeader.getOrderType();

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


        //根据InbPoHeader中的OwnerUid及InbReceiptDetail中的SkuId获取Item
        CsiSku csiSku = csiSkuService.getSkuByCode(CsiConstan.CSI_CODE_TYPE_BARCODE, barCode);
        if (null == csiSku || csiSku.getSkuId() == null) {
            throw new BizCheckedException("2020022");
        }

        BaseinfoItem baseinfoItem = itemService.getItem(ibdHeader.getOwnerUid(), csiSku.getSkuId());

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
        //orderInfoMap.put("packName", "H01");
        orderInfoMap.put("packName", ibdDetail.getPackName());
        BigDecimal orderQty = ibdDetail.getOrderQty().subtract(ibdDetail.getInboundQty());
        orderInfoMap.put("orderQty", orderQty);// todo 剩余待收货数
        orderInfoMap.put("batchNeeded", baseinfoItem.getBatchNeeded());

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

        String containerStoreKey=RedisKeyConstant.CONTAINER_STORE.replace("{0}",containerId+"");
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

        //商品是否存在
        CsiSku csiSku=csiSkuService.getSkuByCode(CsiConstan.CSI_CODE_TYPE_BARCODE,barCode);
        if(null==csiSku||csiSku.getSkuId()==null){
            throw new BizCheckedException("2020022");
        }


        //po单是否存在
        IbdHeader ibdHeader=poOrderService.getInbPoHeaderByOrderOtherId(orderOtherId);
        if(ibdHeader==null){
            throw new BizCheckedException("2020001");
        }
        //是否可收货
        boolean isCanReceipt=ibdHeader.getOrderStatus()==PoConstant.ORDER_THROW||ibdHeader.getOrderStatus()==PoConstant.ORDER_RECTIPT_PART||ibdHeader.getOrderStatus()==PoConstant.ORDER_RECTIPTING;
        if(!isCanReceipt){
            throw new BizCheckedException("2020002");
        }

        //获取货主商品信息
        BaseinfoItem baseinfoItem=itemService.getItem(ibdHeader.getOwnerUid(),csiSku.getSkuId());
        if(baseinfoItem==null){
            throw new BizCheckedException("2900001");
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

            ObdHeader obdHeader=soOrderService.getOutbSoHeaderByOrderOtherId(obdOtherId);
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
                    map2.put("location","J"+storeId);
                    map2.put("orderId",ibdHeader.getOrderId());
                    map2.put("barCode",barCode);
                    map2.put("skuName",csiSku.getSkuName());
                    map2.put("orderQty",obdDetail.getOrderQty());
                    map2.put("packName",ibdDetail.getPackName());
                    map2.put("packUnit",ibdDetail.getPackUnit());

                    //将obdorderId存入redis
                    String key=StrUtils.formatString(RedisKeyConstant.PO_STORE,ibdHeader.getOrderId(),storeId);
                    redisStringDao.set(key,obdHeader.getOrderId());
                    break;
                }

            }

        }
        if(!isOrder){
            throw new BizCheckedException("2020101");
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

}
