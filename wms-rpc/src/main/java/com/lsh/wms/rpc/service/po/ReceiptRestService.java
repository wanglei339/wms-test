package com.lsh.wms.rpc.service.po;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.api.model.base.BaseResponse;
import com.lsh.wms.api.model.base.ResUtils;
import com.lsh.wms.api.model.base.ResponseConstant;
import com.lsh.wms.api.model.po.ReceiptItem;
import com.lsh.wms.api.model.po.ReceiptRequest;
import com.lsh.wms.api.service.po.IReceiptRestService;
import com.lsh.wms.core.constant.BusiConstant;
import com.lsh.wms.core.service.po.PoOrderService;
import com.lsh.wms.core.service.po.PoReceiptService;
import com.lsh.wms.model.po.InbPoHeader;
import com.lsh.wms.model.po.InbReceiptDetail;
import com.lsh.wms.model.po.InbReceiptHeader;
import com.lsh.wms.rpc.service.item.ItemRestService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Project Name: lsh-wms
 * Created by fuhao
 * Date: 16/7/12
 * Time: 16/7/12.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.service.po.
 * desc:类功能描述
 */
@Service(protocol = "rest")
@Path("order/po/receipt")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class ReceiptRestService implements IReceiptRestService {

    @Autowired
    private PoReceiptService poReceiptService;

    @Autowired
    private ItemRestService itemRestService;

    @Autowired
    private PoOrderService poOrderService;

    @POST
    @Path("init")
    public String init(String poReceiptInfo) {
        InbReceiptHeader inbReceiptHeader = JSON.parseObject(poReceiptInfo,InbReceiptHeader.class);
        List<InbReceiptDetail> inbReceiptDetailList = JSON.parseArray(inbReceiptHeader.getReceiptDetails(),InbReceiptDetail.class);
        poReceiptService.orderInit(inbReceiptHeader,inbReceiptDetailList);
        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("insert")
    public BaseResponse insertOrder(ReceiptRequest request) throws BizCheckedException{
        //初始化InbReceiptHeader
        InbReceiptHeader inbReceiptHeader = new InbReceiptHeader();
        ObjUtils.bean2bean(request, inbReceiptHeader);

        //设置receiptOrderId
        inbReceiptHeader.setReceiptOrderId(RandomUtils.genId());

        //设置托盘码,暂存区,分配库位;实际库位由他人写入

        //设置InbReceiptHeader状态
        inbReceiptHeader.setReceiptStatus(BusiConstant.EFFECTIVE_YES);

        //设置InbReceiptHeader插入时间
        inbReceiptHeader.setInserttime(new Date());

        //初始化List<InbReceiptDetail>
        List<InbReceiptDetail> inbReceiptDetailList = new ArrayList<InbReceiptDetail>();

        for(ReceiptItem receiptItem : request.getItems()) {
            InbReceiptDetail inbReceiptDetail = new InbReceiptDetail();

            ObjUtils.bean2bean(receiptItem, inbReceiptDetail);

            //设置receiptOrderId
            inbReceiptDetail.setReceiptOrderId(inbReceiptHeader.getReceiptOrderId());

            //根据request中的orderOtherId查询InbPoHeader
            InbPoHeader inbPoHeader = poOrderService.getInbPoHeaderByOrderOtherId(request.getOrderOtherId());
            if(inbPoHeader == null) {
                throw  new BizCheckedException("3020001");
                // throw  new BizCheckedException("3020001","订单不存在");
            }

            //写入InbReceiptDetail中的OrderId
            inbReceiptDetail.setOrderId(inbPoHeader.getOrderId());

            //根据InbPoHeader中的OwnerUid及InbReceiptDetail中的SkuId获取Item

            //保质期判断,如果失败抛出异常

            //根据OrderId及SkuId获取InbPoDetail

            //写入InbReceiptDetail中的OrderQty

            //写入InbPoDetail中的inboundQty

            inbReceiptDetailList.add(inbReceiptDetail);
        }

        //插入订单
        poReceiptService.insertOrder(inbReceiptHeader, inbReceiptDetailList);

        return ResUtils.getResponse(ResponseConstant.RES_CODE_0,ResponseConstant.RES_MSG_OK,null);
    }
}
