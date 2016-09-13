package com.lsh.wms.integration.service.ibd;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.api.model.base.BaseResponse;
import com.lsh.wms.api.model.base.ResUtils;
import com.lsh.wms.api.model.base.ResponseConstant;
import com.lsh.wms.api.model.po.IbdDetail;
import com.lsh.wms.api.model.po.IbdRequest;
import com.lsh.wms.api.model.po.PoItem;
import com.lsh.wms.api.model.po.PoRequest;
import com.lsh.wms.api.model.stock.StockItem;
import com.lsh.wms.api.model.stock.StockRequest;
import com.lsh.wms.api.service.po.IIbdService;
import com.lsh.wms.api.service.po.IPoRpcService;
import com.lsh.wms.core.constant.IntegrationConstan;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.po.PoOrderService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.po.InbPoHeader;
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
                ibdDetail.setPackName(baseinfoItem.getPackName());
                ibdDetail.setPackUnit(baseinfoItem.getPackUnit());
                ibdDetail.setBarCode(baseinfoItem.getCode());
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
        StockRequest request = new StockRequest();
        request.setPlant("DC37");
        request.setMoveType("551");
        request.setStorageLocation("0001");
        List<StockItem> items = new ArrayList<StockItem>();
        StockItem item = new StockItem();
        item.setEntryQnt("5");
        item.setMaterialNo("000000000000207274");
        item.setEntryUom("EA");
        items.add(item);
        request.setItems(items);

        return ibdBackService.createOrderByPost(request, IntegrationConstan.URL_STOCKCHANGE);
    }
}
