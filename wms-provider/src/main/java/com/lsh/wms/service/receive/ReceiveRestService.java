package com.lsh.wms.service.receive;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.model.po.Header;
import com.lsh.wms.api.model.po.IbdBackRequest;
import com.lsh.wms.api.model.po.IbdItem;
import com.lsh.wms.api.model.wumart.CreateIbdDetail;
import com.lsh.wms.api.model.wumart.CreateIbdHeader;
import com.lsh.wms.api.service.back.IDataBackService;
import com.lsh.wms.api.service.po.IReceiveRestService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.wumart.IWuMart;
import com.lsh.wms.api.service.wumart.IWuMartSap;
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
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.util.*;

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

//    @Reference
//    private IWuMartSap wuMartSap;
    @Reference
    private IWuMart wuMart;

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
            List<ReceiveDetail> receiveDetails = receiveService.getReceiveDetailListByReceiveId((Long) map.get("receiveId"));
            //ReceiveHeader ibdHeader = poOrderService.getInbPoHeaderById((Long) map.get("orderId"));
            ReceiveHeader receiveHeader = receiveService.getReceiveHeaderByReceiveId((Long) map.get("receiveId"));
//            //回传类型
//            String docType = receiveHeader.getOrderType().equals(3) ? "08" : "02";
//            String docStyle = receiveHeader.getOrderType().equals(3) ? "02" : "00";
//            header.setDocStyle(docStyle);
//            header.setDocType(docType);
//            header.setPoNumber(receiveHeader.getOrderOtherId());
//            header.setDelivNumber(receiveHeader.getOrderOtherRefId());
            // TODO: 2016/11/3 回传WMSAP 组装信息
            CreateIbdHeader createIbdHeader = new CreateIbdHeader();
            List<CreateIbdDetail> details = new ArrayList<CreateIbdDetail>();
            for(ReceiveDetail receiveDetail : receiveDetails){
                CreateIbdDetail detail = new CreateIbdDetail();
                detail.setPoNumber(receiveHeader.getOrderOtherId());
                detail.setPoItme(receiveDetail.getDetailOtherId());
                BigDecimal inboudQty =  receiveDetail.getInboundQty();

                BigDecimal orderQty = receiveDetail.getOrderQty();
                BigDecimal deliveQty = receiveHeader.getOrderType().equals(3) ? orderQty : inboudQty;
                if(deliveQty.compareTo(BigDecimal.ZERO) <= 0){
                    continue;
                }
                detail.setDeliveQty(deliveQty.setScale(2,BigDecimal.ROUND_HALF_UP));
                detail.setUnit(receiveDetail.getUnitName());
                detail.setMaterial(receiveDetail.getSkuCode());
                detail.setOrderType(receiveHeader.getOrderType());
                detail.setVendMat(receiveHeader.getReceiveId().toString());

                details.add(detail);
            }
            createIbdHeader.setItems(details);

            if(receiveHeader.getOwnerUid() == 1){
                wuMart.sendIbd(createIbdHeader);

            }else{
                //dataBackService.erpDataBack(createIbdHeader);
            }


            // TODO: 2016/11/3  回传WMSAP 组装信息

//
//
//            List<IbdItem>  items = new ArrayList<IbdItem>();
//            for(ReceiveDetail receiveDetail : receiveDetails){
//                IbdItem ibdItem = new IbdItem();
//                //转成ea
//                BigDecimal inboudQty =  receiveDetail.getInboundQty().multiply(receiveDetail.getPackUnit()).setScale(3);
//                if(inboudQty.compareTo(BigDecimal.ZERO) <= 0){
//                    continue;
//                }
//                BigDecimal orderQty = receiveDetail.getOrderQty().multiply(receiveDetail.getPackUnit()).setScale(3);
//                BigDecimal entryQnt = receiveHeader.getOrderType().equals(3) ? orderQty : inboudQty;
//                ibdItem.setEntryQnt(entryQnt);
//                ibdItem.setMaterialNo(receiveDetail.getSkuCode());
//                ibdItem.setDelivItem(receiveDetail.getDetailOtherId());
//                ibdItem.setPoItem(receiveDetail.getDetailOtherId());
//                //回传baseinfo_item中的unitName
//                //ibdItem.setPackName(inbPoDetail.getPackName());
//                //String unitName = itemService.getItem(ibdHeader.getOwnerUid(),inbPoDetail.getSkuId()).getUnitName().toUpperCase();
//                ibdItem.setPackName(receiveDetail.getUnitName());
//                items.add(ibdItem);
//            }
//            ibdBackRequest.setItems(items);
//            ibdBackRequest.setHeader(header);
//            if(receiveHeader.getOwnerUid() == 1){
//                dataBackService.wmDataBackByPost(ibdBackRequest, IntegrationConstan.URL_IBD, SysLogConstant.LOG_TYPE_WUMART_IBD);
//            }else{
//                dataBackService.erpDataBack(ibdBackRequest);
//            }

        }
        return JsonUtils.SUCCESS();
    }

    @GET
    @Path("accountBack")
    public String accountBack(@QueryParam("receiveId") Long receiveId,@QueryParam("detailOtherId") String detailOtherId) {
        ReceiveDetail detail = receiveService.getReceiveDetailByReceiveIdAnddetailOtherId(receiveId,detailOtherId);
        return wuMart.ibdAccountBack(detail.getAccountId(),detail.getAccountDetailId());
    }
}
