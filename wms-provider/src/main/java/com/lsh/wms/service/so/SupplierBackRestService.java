package com.lsh.wms.service.so;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.so.ISupplierBackRestService;
import com.lsh.wms.model.so.SupplierBackDetail;
import com.lsh.wms.model.so.SupplierBackDetailRequest;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 供应商退货
 * Created by zhanghongling on 16/12/23.
 */
@Service(protocol = "rest")
@Path("order/so/supplierback")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class SupplierBackRestService implements ISupplierBackRestService{
    @Autowired
    private SupplierBackRpcService supplierBackRpcService;

    @POST
    @Path("insert")
    public String insertDetails(List<SupplierBackDetailRequest> requestList)throws BizCheckedException {
        supplierBackRpcService.batchInsertDetail(requestList);
        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("getSupplierBackDetailList")
    public String getSupplierBackDetailList()throws BizCheckedException{
        Map<String, Object> params = RequestUtils.getRequest();
        return JsonUtils.SUCCESS(supplierBackRpcService.getSupplierBackDetailList(params));
    }

    @GET
    @Path("updateSupplierBackDetail")
    public String updateSupplierBackDetail(@QueryParam("backId") Long backId,@QueryParam("reqQty") Long reqQty)throws BizCheckedException{
        if(backId == null || reqQty == null){
            throw new BizCheckedException("1010001", "参数不能为空");
        }
        SupplierBackDetail supplierBackDetail = new SupplierBackDetail();
        supplierBackDetail.setBackId(backId);
        supplierBackDetail.setReqQty(BigDecimal.valueOf(reqQty));
        supplierBackRpcService.updateSupplierBackDetail(supplierBackDetail);
        return JsonUtils.SUCCESS();
    }

}
