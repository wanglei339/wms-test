package com.lsh.wms.rpc.service.item;

/**
 * Created by zengwenjun on 16/7/8.
 */

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
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

import java.math.BigDecimal;
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

    @Autowired
    private CsiRpcService remoteCsiRpcService;



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
        //生成baseinfoItem表
       return itemService.insertItem(item);
    }

    public BaseinfoItem updateItem(BaseinfoItem item) {
        itemService.updateItem(item);
        return item;
    }

    public List<BaseinfoItemLocation> getItemLocationList(long iItemId) {
        return itemLocationService.getItemLocationList(iItemId);
    }

    public List<BaseinfoItemLocation> getItemLocationByLocationID(long iLocationId) {
        return itemLocationService.getItemLocationByLocationID(iLocationId);
    }

    public BaseinfoItemLocation insertItemLocation(BaseinfoItemLocation itemLocation) {
        return itemLocationService.insertItemLocation(itemLocation);
    }

    public void updateItemLocation(BaseinfoItemLocation itemLocation) {
        itemLocationService.updateItemLocation(itemLocation);
    }
    public void updateByItemIdAndPicId(BaseinfoItemLocation baseinfoItemLocation){
        itemLocationService.updateByItemIdAndPicId(baseinfoItemLocation);
    }

    public BaseinfoItem getItem(long itemId) {
        return itemService.getItem(itemId);
    }

    /**
     * 转换包装
     * h60-->60
     */
    public  BigDecimal getPackUnit(String str) throws BizCheckedException {
        String newStr = str.replace(" ", "");
        if("ea".equals(newStr.toLowerCase())){
            return new BigDecimal(1);
        }
        BigDecimal packUnit = BigDecimal.ZERO;
        boolean result=newStr.substring(1).matches("[0-9]+");
        if(result){
            packUnit = new BigDecimal(newStr.substring(1));
        }else{
            throw new BizCheckedException("2050005");
        }
        return packUnit;
    }
    public List<BaseinfoItemLocation> getItemLocation(Map<String,Object> queryMap) {
        return  itemLocationService.getItemLocation(queryMap);
    }
}
