package com.lsh.wms.service.inhouse;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.inhouse.IStockTransferProviderRestService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.utils.PackUtil;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.transfer.StockTransferPlan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;

/**
 * Created by mali on 16/8/1.
 */
@Service(protocol = "rest")
@Path("inhouse/stock_transfer")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class StockTransferProviderRestService implements IStockTransferProviderRestService {
    private static Logger logger = LoggerFactory.getLogger(StockTransferProviderRestService.class);

    @Autowired
    private StockTransferRpcService rpcService;
    @Autowired
    private ItemService itemService;

    @POST
    @Path("add")
    public String addPlan(StockTransferPlan plan) throws BizCheckedException {
        try {
            if(plan.getSubType() == 3){
                plan.setPackName("EA");
                plan.setPackUnit(BigDecimal.valueOf(1L));
            }else if (plan.getSubType() == 2){
                plan.setPackUnit(itemService.getItem(plan.getItemId()).getPackUnit());
                plan.setPackName(PackUtil.PackUnit2Uom(plan.getPackUnit(), "EA"));
            }else{
                return JsonUtils.TOKEN_ERROR("不合法的单位类型");
            }
            rpcService.addPlan(plan);
        } catch (BizCheckedException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JsonUtils.TOKEN_ERROR(e.getMessage());
        }
        return JsonUtils.SUCCESS();
    }

    @GET
    @Path("cancel")
    public String cancelPlan(@QueryParam("taskId") Long taskId) throws BizCheckedException {
        try {
            rpcService.cancelPlan(taskId);
        } catch (BizCheckedException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JsonUtils.TOKEN_ERROR(e.getMessage());
        }
        return JsonUtils.SUCCESS();
    }

}
