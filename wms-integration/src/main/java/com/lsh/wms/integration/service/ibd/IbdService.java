package com.lsh.wms.integration.service.ibd;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.base.common.utils.StrUtils;
import com.lsh.wms.api.model.base.BaseResponse;
import com.lsh.wms.api.model.base.ResUtils;
import com.lsh.wms.api.model.base.ResponseConstant;
import com.lsh.wms.api.model.po.IbdDetail;
import com.lsh.wms.api.model.po.IbdRequest;
import com.lsh.wms.api.model.po.PoItem;
import com.lsh.wms.api.model.po.PoRequest;
import com.lsh.wms.api.model.so.ObdBackRequest;
import com.lsh.wms.api.model.so.ObdItem;
import com.lsh.wms.api.model.so.ObdOfcBackRequest;
import com.lsh.wms.api.model.so.ObdOfcItem;
import com.lsh.wms.api.model.stock.StockItem;
import com.lsh.wms.api.model.stock.StockRequest;
import com.lsh.wms.api.service.po.IIbdService;
import com.lsh.wms.api.service.po.IPoRpcService;
import com.lsh.wms.core.constant.IntegrationConstan;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.po.PoOrderService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoLocationWarehouse;
import com.lsh.wms.model.po.InbPoHeader;
import com.lsh.wms.model.so.OutbDeliveryDetail;
import com.lsh.wms.model.so.OutbSoDetail;
import com.lsh.wms.model.so.OutbSoHeader;
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
 * Created by mali on 16/9/2.
 */
@Service(protocol = "rest", validation = "true")
@Path("ibd")
@Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class IbdService implements IIbdService {
    private static Logger logger = LoggerFactory.getLogger(IbdService.class);

    @Reference
    private IPoRpcService poRpcService;

    @Autowired
    private PoOrderService poOrderService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private IbdBackService ibdBackService;
    @Autowired
    private SoOrderService soOrderService;

    @POST
    @Path("add")
    public BaseResponse add(IbdRequest request) throws BizCheckedException{
        //数量做转换 ea转化为外包装箱数
        List<IbdDetail> details = request.getDetailList();

        List<IbdDetail> newDetails = new ArrayList<IbdDetail>();
        List<PoItem> items = new ArrayList<PoItem>();

        for(IbdDetail ibdDetail : details){
            List<BaseinfoItem>  baseinfoItemList= itemService.getItemsBySkuCode(request.getOwnerUid(),ibdDetail.getSkuCode());
            if(null != baseinfoItemList && baseinfoItemList.size()>=1){
                BaseinfoItem baseinfoItem = baseinfoItemList.get(baseinfoItemList.size()-1);
                String unitName = baseinfoItem.getUnitName().toUpperCase();
                //基础数据中维护基本单位名称,物美下传的packName为基本单位名称, 如果两边不相等,抛异常
                if(!unitName.equals(ibdDetail.getPackName().toUpperCase())){
                    throw new BizCheckedException("2770002");
                }
                //转换为系统内部的PackName和packUnit
                ibdDetail.setPackName(baseinfoItem.getPackName());
                ibdDetail.setPackUnit(baseinfoItem.getPackUnit());
                ibdDetail.setBarCode(baseinfoItem.getCode());
            }else{
                throw new BizCheckedException("2770001");
            }

            BigDecimal qty = ibdDetail.getOrderQty().divide(ibdDetail.getPackUnit(),2);
            ibdDetail.setOrderQty(qty);
            PoItem poItem = new PoItem();
            ObjUtils.bean2bean(ibdDetail,poItem);
            newDetails.add(ibdDetail);
            items.add(poItem);
        }
        //request.setDetailList(newDetails);

        //初始化PoRequest
        if (StringUtils.isContains(request.getSupplierCode(), "DC")) {
            request.setSupplierCode(request.getSupplierCode().substring(2));
        }
        PoRequest poRequest = new PoRequest();
        ObjUtils.bean2bean(request,poRequest);
        //将IbdDetail转化为poItem
        // TODO: 16/9/5  warehouseCode 转换为warehouseId 如何转化 重复的order_other_id 校验
        String orderOtherId = request.getOrderOtherId();
        Integer orderType = request.getOrderType();
        Map<String,Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("orderOtherId" , orderOtherId);
        mapQuery.put("orderType",orderType);
        List<InbPoHeader> lists = poOrderService.getInbPoHeaderList(mapQuery);
        if(lists.size() > 0){
            throw new BizCheckedException("2020088");
        }

        poRequest.setItems(items);
        poRequest.setWarehouseId(1l);

        poRpcService.insertOrder(poRequest);
        return ResUtils.getResponse(ResponseConstant.RES_CODE_1, ResponseConstant.RES_MSG_OK, null);
    }

    @POST
    @Path("test")
    public String Test() {
//        StockRequest request = new StockRequest();
//        request.setPlant("DC37");
//        request.setMoveType("551");
//        request.setStorageLocation("0001");
//        List<StockItem> items = new ArrayList<StockItem>();
//        StockItem item = new StockItem();
//        item.setEntryQnt("5");
//        item.setMaterialNo("000000000000207274");
//        item.setEntryUom("EA");
//        items.add(item);
//        request.setItems(items);

        OutbSoHeader soHeader = soOrderService.getOutbSoHeaderByOrderId(214580861081622l);
        //组装OBD反馈信息
        ObdBackRequest request = new ObdBackRequest();
        request.setPlant("DC37");//仓库
        request.setBusinessId(soHeader.getOrderOtherId());
        request.setOfcId(soHeader.getOrderOtherRefId());//参考单号
        request.setAgPartnNumber(soHeader.getOrderUserCode());//用户

        //查询明细。
        List<OutbSoDetail> soDetails = soOrderService.getOutbSoDetailListByOrderId(214580861081622l);
        List<ObdItem> items = new ArrayList<ObdItem>();


        for (OutbSoDetail soDetail : soDetails){
            ObdItem soItem = new ObdItem();
            soItem.setMaterialNo(soDetail.getSkuCode());//skuCode
            soItem.setMeasuringUnit("EA");
            //soItem.setPrice(soDetail.getPrice());
            //转化成ea
            soItem.setQuantity(soDetail.getOrderQty().multiply(soDetail.getPackUnit()).setScale(3));
            soItem.setSendQuantity(soDetail.getOrderQty());
            //查询waveDetail找出实际出库的数量
            items.add(soItem);
        }
        //查询waveDetail找出实际出库的数量
        request.setItems(items);

        return ibdBackService.createOrderByPost(request, IntegrationConstan.URL_OBD);


//        OutbSoHeader soHeader = soOrderService.getOutbSoHeaderByOrderId(76978698850361L);
//        //组装OBD反馈信息
//        ObdOfcBackRequest request = new ObdOfcBackRequest();
//        request.setDeliveryTime("2016-09-20");
//        request.setObdCode(soHeader.getOrderId().toString());
//        request.setSoCode(soHeader.getOrderOtherId());
//        //查询明细。
//        List<OutbSoDetail> soDetails = soOrderService.getOutbSoDetailListByOrderId(76978698850361L);
//        List<ObdOfcItem> items = new ArrayList<ObdOfcItem>();
//
//        for(OutbSoDetail detail : soDetails){
//            ObdOfcItem item = new ObdOfcItem();
//            item.setPackNum(detail.getPackUnit());
//            item.setSkuQty(detail.getOrderQty());
//            item.setSupplySkuCode(detail.getSkuCode());
//            items.add(item);
//
//        }
//        request.setDetails(items);
//        String url = "http://api.ofc.lsh123.com/ofc/api/order/obd/push";
//        return ibdBackService.createOfcOrderByPost(request,url);


    }



}
