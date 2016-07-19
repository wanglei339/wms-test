package com.lsh.wms.rpc.service.item;

/**
 * Created by zengwenjun on 16/7/8.
 */

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.wms.api.service.csi.ICsiRpcService;
import com.lsh.wms.api.service.item.IItemRpcService;
import com.lsh.wms.core.service.item.ItemLocationService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoItemLocation;
import com.lsh.wms.model.csi.CsiSku;
import com.lsh.wms.rpc.service.csi.CsiRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import java.util.HashMap;
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
    @Autowired
    private ItemLocationService itemLocationService;

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


    public BaseinfoItem insertItem(BaseinfoItem item){
        Map<String,Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("codeType",item.getCodeType());
        mapQuery.put("code",item.getCode());
        List<BaseinfoItem> itemList = this.searchItem(mapQuery);
        if(itemList.size() > 0){
            return null;
        }
        CsiSku sku = this.getSkuByCode(Integer.valueOf(item.getCodeType()), item.getCode());
        if(sku != null){
            item.setSkuId(sku.getSkuId());
            itemService.insertItem(item);

        }else{
            sku = new CsiSku();
            String code = item.getCode();
            sku.setCode(code);
            sku.setCodeType(item.getCodeType().toString());
            sku.setShelfLife(item.getShelfLife());
            sku.setSkuName(item.getSkuName());
            sku.setHeight(item.getHeight());
            sku.setLength(item.getLength());
            sku.setWidth(item.getWidth());
            sku.setWeight(item.getWeight());
            //生成csi_sku表
            CsiSku newSku = remoteCsiRpcService.insertSku(sku);

            long skuId = newSku.getSkuId();
            item.setSkuId(skuId);
            //生成baseinfoItem表
            itemService.insertItem(item);

        }

        return item;

    }

    public BaseinfoItem updateItem(BaseinfoItem item) {
        if(itemService.getItem(item.getOwnerId(),item.getSkuId()) == null){
            return null;
        }
        itemService.updateItem(item);
        return item;
    }

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

    public BaseinfoItemLocation updateItemLocation(BaseinfoItemLocation itemLocation) {
        //查询是否存在该记录
        if(itemLocationService.getItemLocation(itemLocation.getId()) == null){
            return null;
        }
        itemLocationService.updateItemLocation(itemLocation);
        return itemLocation;
    }

    public BaseinfoItem getItem(long itemId) {
        return itemService.getItem(itemId);
    }

}
