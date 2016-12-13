package com.lsh.wms.rpc.service.datareport;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.datareport.ISkuMapRestService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by lixin-mac on 2016/12/10.
 */
@Service(protocol = "rest")
@Path("skumap")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class SkuMapRestService implements ISkuMapRestService{

    private static Logger logger = LoggerFactory.getLogger(SkuMapRestService.class);

    @Autowired
    private SkuMapRpcService skuMapRpcService;

    @Autowired
    private ItemService itemService;

    @Path("addmap")
    @GET
    public String addMap() {
        List<String> skuCodes = itemService.getSkuCodeList();

        skuMapRpcService.insertSkuMap(skuCodes);
        return JsonUtils.SUCCESS();
    }
}
