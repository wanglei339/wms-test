package com.lsh.wms.rpc.service;

/**
 * Created by zengwenjun on 16/7/8.
 */

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
 * Created by mali on 16/6/29.
 */

@Service(protocol = "dubbo")
public class ItemService implements IItemService {
    private static Logger logger = LoggerFactory.getLogger(ItemService.class);

    @Autowired
    private com.lsh.wms.core.service.item.ItemService itemService;

    public BaseinfoItem getItem(long iOwnerId, long iSkuId) {
        return itemService.getItem(iOwnerId, iSkuId);
    }

    public CsiSku getBaseInfo(long iSkuId) {
        return itemService.getSkuBaseInfo(iSkuId);
    }

    public CsiSku getSkuByCode(int iCodeType, String sCode) {
        return itemService.getSkuByCode(iCodeType, sCode);
    }

    public List<BaseinfoItem> getItemsBySkuCode(long iOwnerId, String sSkuCode) {
        return itemService.getItemsBySkuCode(iOwnerId, sSkuCode);
    }

    public List<BaseinfoItem> searchItem(Map<String, Object> mapQuery) {
        return itemService.searchItem(mapQuery);
    }
}
