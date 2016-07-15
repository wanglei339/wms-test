package com.lsh.wms.rpc.service.item;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.wms.api.service.item.IItemLocationRpcService;
import com.lsh.wms.core.service.item.ItemLocationService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoItemLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by lixin-mac on 16/7/14.
 */

@Service(protocol = "dubbo")
public class ItemLocationRpcService implements IItemLocationRpcService{


    private static Logger logger = LoggerFactory.getLogger(ItemLocationRpcService.class);
    @Autowired
    private ItemLocationService itemLocationService;

    @Autowired
    private ItemService itemService;

    public List<BaseinfoItemLocation> getItemLocationList(long iSkuId, long iOwnerId) {
        return itemLocationService.getItemLocationList(iSkuId,iOwnerId);
    }

    public List<BaseinfoItemLocation> getItemLocationByLocationID(long iLocationId) {
        return itemLocationService.getItemLocationByLocationID(iLocationId);
    }

    public BaseinfoItemLocation insertItemLocation(BaseinfoItemLocation itemLocation) {
        //查询是否存在该Item
        long skuId = itemLocation.getSkuId();
        long ownerId = itemLocation.getOwnerId();
        BaseinfoItem item = itemService.getItem(ownerId,skuId);
        if(item == null){
            return null;
        }
        return itemLocationService.insertItemLocation(itemLocation);
    }

    public int updateItemLocation(BaseinfoItemLocation itemLocation) {

        return itemLocationService.updateItemLocation(itemLocation);
    }
}
