package com.lsh.wms.integration.service.obd;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lsh.base.common.config.PropertyUtils;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.net.HttpClientUtils;
import com.lsh.base.common.utils.BeanMapTransUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.api.model.base.BaseResponse;
import com.lsh.wms.api.model.base.ResUtils;
import com.lsh.wms.api.model.base.ResponseConstant;
import com.lsh.wms.api.model.so.ObdRequest;
import com.lsh.wms.api.model.so.SoItem;
import com.lsh.wms.api.model.so.SoRequest;
import com.lsh.wms.api.model.wumart.CreateObdHeader;
import com.lsh.wms.api.service.so.IObdService;
import com.lsh.wms.api.service.so.ISoRpcService;
import com.lsh.wms.core.constant.CsiConstan;
import com.lsh.wms.core.constant.IntegrationConstan;
import com.lsh.wms.core.constant.PoConstant;
import com.lsh.wms.core.constant.SoConstant;
import com.lsh.wms.core.service.csi.CsiCustomerService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.po.PoOrderService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.utils.HttpUtils;
import com.lsh.wms.integration.service.wumartsap.WuMartSap;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.csi.CsiCustomer;
import com.lsh.wms.model.po.IbdHeader;
import com.lsh.wms.model.so.ObdDetail;
import com.lsh.wms.model.so.ObdHeader;
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
@Path("obd")
@Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class ObdService implements IObdService{

    private static Logger logger = LoggerFactory.getLogger(ObdService.class);
    @Reference
    private ISoRpcService soRpcService;

    @Autowired
    private SoOrderService soOrderService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private CsiCustomerService customerService;
    @Autowired
    private WuMartSap wuMartSap;

    @Autowired
    private PoOrderService poOrderService;

    @POST
    @Path("add")
    public BaseResponse add(ObdRequest request) throws BizCheckedException{

        //数量做转换 ea转化为外包装箱数
        List<com.lsh.wms.api.model.so.ObdDetail> details = request.getDetailList();

        List<com.lsh.wms.api.model.so.ObdDetail> newDetails = new ArrayList<com.lsh.wms.api.model.so.ObdDetail>();
        List<SoItem> items = new ArrayList<SoItem>();


        if(request.getWarehouseCode().equals("DC41")){
            String requestBody = JsonUtils.obj2Json(request);
            int dc41_timeout = PropertyUtils.getInt("dc41_timeout");
            String dc41_charset = PropertyUtils.getString("dc41_charset");
            Map<String, String> headerMap = new HashMap<String, String>();
            headerMap.put("Content-type", "application/json; charset=utf-8");
            headerMap.put("Accept", "application/json");
            headerMap.put("api-version", "1.1");
            headerMap.put("random", RandomUtils.randomStr2(32));
            headerMap.put("platform", "1");
            String res  = HttpClientUtils.postBody(PropertyUtils.getString("url_so"),  requestBody,dc41_timeout , dc41_charset, headerMap);
            logger.info("~~~~~~~~~~下发黑狗数据 request : " + JSON.toJSONString(request) + "~~~~~~~~~");
            JSONObject jsonObject = JSON.parseObject(res);

            return ResUtils.getResponse(ResponseConstant.RES_CODE_1, ResponseConstant.RES_MSG_OK, jsonObject.get("body"));


        }

        for(com.lsh.wms.api.model.so.ObdDetail obdDetail : details){
            String skuCode = obdDetail.getSkuCode();
//            if(CsiConstan.OWNER_WUMART == request.getOwnerUid()){
//                //如果货主是物美 将skucode前面的0去掉
//                skuCode = obdDetail.getSkuCode().replaceAll("^(0+)", "");
//            }
            BaseinfoItem  baseinfoItem= itemService.getItemsBySkuCode(request.getOwnerUid(),skuCode);
            if(baseinfoItem == null) {
                throw new BizCheckedException("2770001");
            }
//            if(null != baseinfoItemList && baseinfoItemList.size()>=1){
//                BaseinfoItem baseinfoItem = baseinfoItemList.get(baseinfoItemList.size()-1);
//                obdDetail.setPackName(baseinfoItem.getPackName());
//                obdDetail.setPackUnit(baseinfoItem.getPackUnit());
//                obdDetail.setBarCode(baseinfoItem.getCode());
//            }

//            BigDecimal qty = obdDetail.getOrderQty().divide(obdDetail.getPackUnit(),2);
//            obdDetail.setOrderQty(qty);
//            newDetails.add(obdDetail);
            SoItem soItem = new SoItem();
            ObjUtils.bean2bean(obdDetail,soItem);
            items.add(soItem);
        }
        request.setDetailList(newDetails);


        //初始化SoRequest
        SoRequest soRequest = new SoRequest();
        ObjUtils.bean2bean(request,soRequest);

        soRequest.setItems(items);
        //默认下单用户
        //soRequest.setOrderUser("超市");

        String orderOtherId = request.getOrderOtherId();
        Integer orderType = request.getOrderType();

        //供商退货单检查是否原单是否存在
        if(SoConstant.ORDER_TYPE_PO_BACK == orderType){
            Map<String ,Object> map = new HashMap<String, Object>();
            map.put("orderOtherId",request.getOrderOtherRefId());
            map.put("orderType", PoConstant.ORDER_TYPE_PO);
            List<IbdHeader> ibdHeaderList = poOrderService.getInbPoHeaderList(map);
            if(ibdHeaderList == null || ibdHeaderList.size() <= 0 ){
                throw new BizCheckedException("2021112",orderOtherId,request.getOrderOtherRefId());
            }
        }



        Map<String,Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("orderOtherId" , orderOtherId);
        mapQuery.put("orderType",orderType);
        List<ObdHeader> lists = soOrderService.getOutbSoHeaderList(mapQuery);
        if(lists.size() > 0){
            //throw new BizCheckedException("2020099",orderOtherId,"");
            List<ObdDetail> soDetails = soOrderService.getOutbSoDetailListByOrderId(lists.get(0).getOrderId());
            lists.get(0).setOrderDetails(soDetails);
            return ResUtils.getResponse(ResponseConstant.RES_CODE_1, ResponseConstant.RES_MSG_OK, lists.get(0));
        }
        //添加waveOrderType
        String waveOrderType = "";
        if(orderType == SoConstant.ORDER_TYPE_SO){
            waveOrderType = "YouGong store";
        }else if(orderType == SoConstant.ORDER_TYPE_STO){
            CsiCustomer customer = customerService.getCustomerByCustomerCode(soRequest.getDeliveryCode());
            if(customer != null){
                waveOrderType = customer.getCustomerType();
            }
        }
        soRequest.setWaveOrderType(waveOrderType);

        //soRequest.setWarehouseId(1l);
        Long orderId = soRpcService.insertOrder(soRequest);
        ObdHeader obdHeader = soOrderService.getOutbSoHeaderByOrderId(orderId);
        List<ObdDetail> soDetails = soOrderService.getOutbSoDetailListByOrderId(orderId);
        obdHeader.setOrderDetails(soDetails);

//        Map<String,Object> map = new HashMap<String, Object>();
//        map.put("orderId",orderId);
//        map.put("orderOtherId",request.getOrderOtherId());
//        map.put("orderOtherRefId",request.getOrderOtherRefId());

        return ResUtils.getResponse(ResponseConstant.RES_CODE_1, ResponseConstant.RES_MSG_OK, obdHeader);
    }

    @POST
    @Path("bdSendObd2Sap")
    public String bdSendObd2Sap(CreateObdHeader createObdHeader) {

        logger.info(" 黑狗创建obd 入口参数 : createObdHeader : " + JSON.toJSONString(createObdHeader));
        String type = wuMartSap.soObd2Sap(createObdHeader);
        if("E".equals(type)){
            return JsonUtils.TOKEN_ERROR("创建obd失败");
        }
        return JsonUtils.SUCCESS("创建obd并过账成功");
    }
}
