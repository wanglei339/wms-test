package com.lsh.wms.service.inhouse;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.inhouse.IProcurementProveiderRpcService;
import com.lsh.wms.api.service.inhouse.IProcurementProviderRestService;
import com.lsh.wms.model.transfer.StockTransferPlan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by mali on 16/8/2.
 */
@Service(protocol = "rest")
@Path("inhouse/procurement")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class ProcurementProviderRestService implements IProcurementProviderRestService {
    private static Logger logger = LoggerFactory.getLogger(ProcurementProviderRestService.class);

    @Autowired
    private ProcurementProviderRpcService rpcService;

    @POST
    @Path("add")
    public String addProcurementPlan(StockTransferPlan plan)  throws BizCheckedException {
        try{
            rpcService.addProcurementPlan(plan);
        } catch (BizCheckedException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getCause().getMessage());
            return JsonUtils.EXCEPTION_ERROR(e.getCause().getMessage());
        }
        return JsonUtils.SUCCESS();
    }
    @POST
    @Path("update")
    public String updateProcurementPlan(StockTransferPlan plan)  throws BizCheckedException {
        try{
            rpcService.addProcurementPlan(plan);
        } catch (BizCheckedException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getCause().getMessage());
            return JsonUtils.EXCEPTION_ERROR(e.getCause().getMessage());
        }
        return JsonUtils.SUCCESS();
    }

    @GET
    @Path("autoCreate")
    public String createProcurement()  throws BizCheckedException {
        try{
            rpcService.createProcurement();
        } catch (BizCheckedException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getCause().getMessage());
            return JsonUtils.EXCEPTION_ERROR(e.getCause().getMessage());
        }
        return JsonUtils.SUCCESS();
    }
}
