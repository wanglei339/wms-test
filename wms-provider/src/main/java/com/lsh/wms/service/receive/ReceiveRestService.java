package com.lsh.wms.service.receive;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.model.po.Header;
import com.lsh.wms.api.model.po.IbdBackRequest;
import com.lsh.wms.api.model.po.IbdItem;
import com.lsh.wms.api.service.back.IDataBackService;
import com.lsh.wms.api.service.po.IReceiveRestService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.core.constant.IntegrationConstan;
import com.lsh.wms.core.constant.SysLogConstant;
import com.lsh.wms.core.service.location.BaseinfoLocationWarehouseService;
import com.lsh.wms.core.service.po.ReceiveService;
import com.lsh.wms.model.baseinfo.BaseinfoLocationWarehouse;
import com.lsh.wms.model.po.ReceiveDetail;
import com.lsh.wms.model.po.ReceiveHeader;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lixin-mac on 2016/10/21.
 */

@Service(protocol = "rest")
@Path("receive")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class ReceiveRestService implements IReceiveRestService{

    @Autowired
    private ReceiveRpcService receiveRpcService;

    @Reference
    private IDataBackService dataBackService;

    @Autowired
    private BaseinfoLocationWarehouseService baseinfoLocationWarehouseService;

    @Autowired
    private ReceiveService receiveService;

    @POST
    @Path("getReceiveHeaderList")
    public String getReceiveHeaderList(Map<String, Object> params) {

        return JsonUtils.SUCCESS(receiveRpcService.getReceiveHeaderList(params));
    }

    @POST
    @Path("countReceiveHeader")
    public String countReceiveHeader(Map<String, Object> params) {
        return JsonUtils.SUCCESS(receiveRpcService.countReceiveHeader(params));
    }

    @GET
    @Path("getReceiveDetailList")
    public String getReceiveDetailList(@QueryParam("receiveId") Long receiveId) {
        return JsonUtils.SUCCESS(receiveRpcService.getReceiveDetailList(receiveId));
    }


    @POST
    @Path("updateOrderStatus")
    public String updateOrderStatus() throws BizCheckedException {
        Map<String, Object> map = RequestUtils.getRequest();

        receiveRpcService.updateOrderStatus(map);

        //确认收货之后将验收单回传到上游系统
        if("5".equals(map.get("orderStatus").toString())){
            IbdBackRequest ibdBackRequest = new IbdBackRequest();
            Header header = new Header();
            BaseinfoLocationWarehouse warehouse = (BaseinfoLocationWarehouse) baseinfoLocationWarehouseService.getBaseinfoItemLocationModelById(0L);
            String warehouseName = warehouse.getWarehouseName();
            header.setPlant(warehouseName);
//            String poNumber =map.get("orderOtherId").toString();
//            header.setPoNumber(poNumber);
            List<ReceiveDetail> receiveDetails = receiveService.getReceiveDetailListByReceiveId((Long) map.get("receiveId"));
            //ReceiveHeader ibdHeader = poOrderService.getInbPoHeaderById((Long) map.get("orderId"));
            ReceiveHeader receiveHeader = receiveService.getReceiveHeaderByReceiveId((Long) map.get("receiveId"));
            //回传类型
            String docType = receiveHeader.getOrderType().equals(3) ? "08" : "02";
            String docStyle = receiveHeader.getOrderType().equals(3) ? "02" : "00";
            header.setDocStyle(docStyle);
            header.setDocType(docType);
            header.setPoNumber(receiveHeader.getOrderOtherId());
            header.setDelivNumber(receiveHeader.getOrderOtherRefId());


            List<IbdItem>  items = new ArrayList<IbdItem>();
            for(ReceiveDetail receiveDetail : receiveDetails){
                IbdItem ibdItem = new IbdItem();
                //转成ea
                BigDecimal inboudQty =  receiveDetail.getInboundQty().multiply(receiveDetail.getPackUnit()).setScale(3);
                BigDecimal orderQty = receiveDetail.getOrderQty().multiply(receiveDetail.getPackUnit()).setScale(3);
                BigDecimal entryQnt = receiveHeader.getOrderType().equals(3) ? orderQty : inboudQty;
                ibdItem.setEntryQnt(entryQnt);
                ibdItem.setMaterialNo(receiveDetail.getSkuCode());
                ibdItem.setDelivItem(receiveDetail.getDetailOtherId());
                ibdItem.setPoItem(receiveDetail.getDetailOtherId());
                //回传baseinfo_item中的unitName
                //ibdItem.setPackName(inbPoDetail.getPackName());
                //String unitName = itemService.getItem(ibdHeader.getOwnerUid(),inbPoDetail.getSkuId()).getUnitName().toUpperCase();
                ibdItem.setPackName(receiveDetail.getUnitName());
                items.add(ibdItem);
            }
            ibdBackRequest.setItems(items);
            ibdBackRequest.setHeader(header);
            if(receiveHeader.getOwnerUid() == 1){
                dataBackService.wmDataBackByPost(ibdBackRequest, IntegrationConstan.URL_IBD, SysLogConstant.LOG_TYPE_WUMART_IBD);
            }else{
                dataBackService.erpDataBack(ibdBackRequest);
            }

        }
        return JsonUtils.SUCCESS();
    }
}
