package com.lsh.wms.rpc.service.lot;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.lot.ILotRestService;
import com.lsh.wms.model.baseinfo.BaseinfoLot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;
/**
 * Created by Ming on 7/11/16.
 */

@Service(protocol = "rest")
@Path("lot")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})

public class LotRestService implements ILotRestService{
    private static Logger logger = LoggerFactory.getLogger(LotRestService.class);

    @Autowired
    private  LotRpcService lotRpcService;

    @GET
    @Path("getLotByLotId")
    public String getLotByLotId(@QueryParam("lotId") long iLotId) {
        logger.info("lotId = " + iLotId);
        BaseinfoLot baseinfoLot = lotRpcService.getLotByLotId(iLotId);
        return JsonUtils.SUCCESS(baseinfoLot);
    }

    @POST
    @Path("insertLot")
    public String insertLot(BaseinfoLot lot) {
        BaseinfoLot lot_new = lotRpcService.insertLot(lot);
        return JsonUtils.SUCCESS(lot_new);
    }

    @POST
    @Path("updateLot")
    public String updateLot(BaseinfoLot lot) {
        int result = lotRpcService.updateLot(lot);
        if(result == 0) return "Success!";
        return "Failure!";
    }

    @POST
    @Path("searchLot")
    public String searchLot(Map<String, Object> mapQuery) {
        List<BaseinfoLot> baseinfoLotlist = lotRpcService.searchLot(mapQuery);
        return JsonUtils.SUCCESS(baseinfoLotlist);
    }

}
