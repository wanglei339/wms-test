package com.lsh.wms.rpc.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.wms.api.service.item.IItemService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.csi.CsiSku;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

/**
 * Created by zengwenjun on 16/7/8.
 */

@Service(protocol = "rest")
@Path("item")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class ItemRestService implements IItemService{
    private static Logger logger = LoggerFactory.getLogger(ItemRestService.class);

    @Autowired
    private ItemService service;

    public BaseinfoItem getItem(long iOwnerId, long iSkuId) {
        return service.getItem(iOwnerId, iSkuId);
    }

    public CsiSku getBaseInfo(long iSkuId) {
        return service.getBaseInfo(iSkuId);
    }

    public CsiSku getSkuByCode(int iCodeType, String sCode) {
        return service.getSkuByCode(iCodeType, sCode);
    }

    public List<BaseinfoItem> getItemsBySkuCode(long iOwnerId, String sSkuCode) {
        return service.getItemsBySkuCode(iOwnerId, sSkuCode);
    }

    public List<BaseinfoItem> searchItem(Map<String, Object> mapQuery) {
        return service.searchItem(mapQuery);
    }
}
