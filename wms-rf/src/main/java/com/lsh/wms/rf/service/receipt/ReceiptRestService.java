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
import com.lsh.wms.api.model.base.BaseResponse;
import com.lsh.wms.api.model.base.ResUtils;
import com.lsh.wms.api.model.base.ResponseConstant;
import com.lsh.wms.api.model.po.ReceiptItem;
import com.lsh.wms.api.model.po.ReceiptRequest;
import com.lsh.wms.api.service.po.IReceiptRfService;
import com.lsh.wms.api.service.po.IReceiptRpcService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.core.constant.CsiConstan;
import com.lsh.wms.core.constant.PoConstant;
import com.lsh.wms.core.service.container.ContainerService;
import com.lsh.wms.core.service.csi.CsiSkuService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.po.PoOrderService;
import com.lsh.wms.core.service.staff.StaffService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.csi.CsiSku;
import com.lsh.wms.model.po.InbPoDetail;
import com.lsh.wms.model.po.InbPoHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @POST
    @Path("add")
    public BaseResponse insertOrder() throws BizCheckedException, ParseException {
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

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("uid",RequestUtils.getHeader("uid"));
        Long staffId = staffService.getStaffList(map).get(0).getStaffId();
        receiptRequest.setStaffId(staffId);



        receiptRequest.setReceiptTime(new Date());

        InbPoHeader inbPoHeader = poOrderService.getInbPoHeaderByOrderOtherId(receiptRequest.getOrderOtherId());
        if(inbPoHeader == null) {
            throw new BizCheckedException("2020001");
        }
        //校验之后修改订单状态为收货中 第一次收货将订单改为收货中
        if(inbPoHeader.getOrderStatus() == PoConstant.ORDER_THROW){
            inbPoHeader.setOrderStatus(PoConstant.ORDER_RECTIPTING);
            poOrderService.updateInbPoHeader(inbPoHeader);
        }

        for(ReceiptItem receiptItem : receiptRequest.getItems()) {
            if(receiptItem.getProTime() == null) {
                throw new BizCheckedException("2020008");
            }

            InbPoDetail inbPoDetail = poOrderService.getInbPoDetailByOrderIdAndBarCode(inbPoHeader.getOrderId(), receiptItem.getBarCode());

            if(inbPoDetail == null){
                throw new BizCheckedException("2020001");
            }

            receiptItem.setSkuName(inbPoDetail.getSkuName());
            receiptItem.setPackUnit(inbPoDetail.getPackUnit());
            receiptItem.setMadein(inbPoDetail.getMadein());
        }

        receiptRequest.setItems(receiptItemList);

        iReceiptRpcService.insertOrder(receiptRequest);
        Map<String,Boolean> body = new HashMap<String, Boolean>();
        body.put("response",true);
        return ResUtils.getResponse(ResponseConstant.RES_CODE_1,ResponseConstant.RES_MSG_OK,body);
    }

    @POST
    @Path("getorderinfo")
    public String getPoDetailByOrderIdAndBarCode(@FormParam("orderOtherId") String orderOtherId,@FormParam("containerId") Long containerId, @FormParam("barCode") String barCode) throws BizCheckedException {
        if(StringUtils.isBlank(orderOtherId) || StringUtils.isBlank(barCode)|| containerId ==null) {
            throw new BizCheckedException("1020001", "参数不能为空");
        }

        InbPoHeader  inbPoHeader  = poOrderService.getInbPoHeaderByOrderOtherId(orderOtherId);

        if (inbPoHeader == null) {
            throw new BizCheckedException("2020001");
        }

        boolean isCanReceipt = inbPoHeader.getOrderStatus() == PoConstant.ORDER_THROW || inbPoHeader.getOrderStatus() == PoConstant.ORDER_RECTIPT_PART || inbPoHeader.getOrderStatus() == PoConstant.ORDER_RECTIPTING;
        if (!isCanReceipt) {
            throw new BizCheckedException("2020002");
        }

        if(!containerService.isContainerCanUse(containerId)){
            throw new BizCheckedException("2000002");
        }

        InbPoDetail inbPoDetail = poOrderService.getInbPoDetailByOrderIdAndBarCode(inbPoHeader.getOrderId(), barCode);

        if (inbPoDetail == null) {
            throw new BizCheckedException("2020022");
        }


        //根据InbPoHeader中的OwnerUid及InbReceiptDetail中的SkuId获取Item
        CsiSku csiSku = csiSkuService.getSkuByCode(CsiConstan.CSI_CODE_TYPE_BARCODE, barCode);
        if (null == csiSku || csiSku.getSkuId() == null) {
            throw new BizCheckedException("2020004");
        }
        BaseinfoItem baseinfoItem = itemService.getItem(inbPoHeader.getOwnerUid(), csiSku.getSkuId());

        Map<String, Object> orderInfoMap = new HashMap<String, Object>();
        orderInfoMap.put("skuName", inbPoDetail.getSkuName());
        orderInfoMap.put("packUnit", inbPoDetail.getPackUnit());
        BigDecimal orderQty = inbPoDetail.getOrderQty().subtract(inbPoDetail.getInboundQty());

        orderInfoMap.put("orderQty", orderQty);// todo 剩余待收货数
        orderInfoMap.put("batchNeeded", baseinfoItem.getBatchNeeded());

        return JsonUtils.SUCCESS(orderInfoMap);
    }
}
