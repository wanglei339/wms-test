package com.lsh.wms.rpc.service.item;

/**
 * Created by zengwenjun on 16/7/8.
 */

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.wms.api.service.csi.ICsiRpcService;
import com.lsh.wms.api.service.item.IItemRpcService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.csi.CsiSku;
import com.lsh.wms.rpc.service.csi.CsiRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import java.util.List;
import java.util.Map;

/**
 * Created by mali on 16/6/29.
 */

@Service(protocol = "dubbo")
public class ItemRpcService implements IItemRpcService {
    private static Logger logger = LoggerFactory.getLogger(ItemRpcService.class);

    @Autowired
    private ItemService itemService;
    @Reference
    private ICsiRpcService remoteCsiRpcService;

    public BaseinfoItem getItem(long iOwnerId, long iSkuId) {
        return itemService.getItem(iOwnerId, iSkuId);
    }

    public CsiSku getSku(long iSkuId) {
        return remoteCsiRpcService.getSku(iSkuId);
    }

    public CsiSku getSkuByCode(int iCodeType, String sCode) {
        return remoteCsiRpcService.getSkuByCode(iCodeType, sCode);
    }

    public List<BaseinfoItem> getItemsBySkuCode(long iOwnerId, String sSkuCode) {
        return itemService.getItemsBySkuCode(iOwnerId, sSkuCode);
    }

    public List<BaseinfoItem> searchItem(Map<String, Object> mapQuery) {
        return itemService.searchItem(mapQuery);
    }


    public BaseinfoItem insertItem(BaseinfoItem item) {
        CsiSku sku = this.getSkuByCode(Integer.valueOf(item.getCodeType()), item.getCode());
        if(sku == null){
            item.setSkuId(sku.getSkuId());
            CsiSku new_sku = new CsiSku();

            sku = remoteCsiRpcService.insertSku(new_sku);
            if(sku == null){
                return null;
            }
        }
        item.setSkuId(sku.getSkuId());
        itemService.insertItem(item);
        return item;
    }

}
