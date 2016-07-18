package com.lsh.wms.service.po;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.api.model.base.BaseResponse;
import com.lsh.wms.api.model.po.ReceiptItem;
import com.lsh.wms.api.model.po.ReceiptRequest;
import com.lsh.wms.api.service.po.IReceiptRestService;
import com.lsh.wms.core.service.po.PoReceiptService;
import com.lsh.wms.model.po.InbPoDetail;
import com.lsh.wms.model.po.InbReceiptDetail;
import com.lsh.wms.model.po.InbReceiptHeader;
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
    public BaseResponse insertOrder(ReceiptRequest request) {
        BaseResponse response = new BaseResponse();

        InbReceiptHeader inbReceiptHeader = new InbReceiptHeader();
        ObjUtils.bean2bean(request, inbReceiptHeader);

        List<InbReceiptDetail> inbReceiptDetailList = new ArrayList<InbReceiptDetail>();
        InbReceiptDetail inbReceiptDetail = null;

        for(ReceiptItem receiptItem : request.getItems()) {
            inbReceiptDetail = new InbReceiptDetail();

            ObjUtils.bean2bean(receiptItem, inbReceiptDetail);

            inbReceiptDetailList.add(inbReceiptDetail);
        }

        try {
            poReceiptService.insertOrder(inbReceiptHeader, inbReceiptDetailList);

            response.setStatus(0);
            response.setMeg("ok");
            response.setDataKey(new Date());
        } catch (Exception ex) {
            response.setStatus(1);
            response.setMeg(ex.getMessage());
            response.setDataKey(new Date());
        }

        return response;
    }
}
