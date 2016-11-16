package com.lsh.wms.core.service.stock;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.wms.api.service.inventory.ISynInventory;
import com.lsh.wms.api.service.inventory.ISynStockInventory;
import com.lsh.wms.core.service.item.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Project Name: lsh-wms
 * Created by fuhao
 * Date: 16/9/7
 * Time: 16/9/7.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.core.service.stock.
 * desc:类功能描述
 */
@Component
@Service(protocol = "dubbo")
public class SynStockService implements ISynStockInventory{

    private static Logger logger = LoggerFactory.getLogger(SynStockService.class);
    @Autowired
    private ItemService itemService;

    @Reference
    private ISynInventory iSynInventory;

    public void synStock(Long item_id, Double qty) { // TODO: 16/9/8
//        iSynInventory.synInventory(item_id,qty);
//        InventorySyncLshRequest request = new InventorySyncLshRequest();
//        request.setZoneCode(PropertyUtils.getString("zone_code"));
//        request.setSystem(PropertyUtils.getString("system"));
//        BaseinfoItem baseinfoItem   = itemService.getItem(item_id);
//        Long ownerId = baseinfoItem.getOwnerId();
//        request.setDcCode(PropertyUtils.getString("owner_"+ownerId));   // TODO: 16/9/7
//        List<SkuVo> skuList = new ArrayList<SkuVo>();
//        SkuVo itemDc = new SkuVo();
//        itemDc.setItemId(item_id);
//        itemDc.setQty(new BigDecimal(qty)); // TODO: 16/9/7
//        skuList.add(itemDc);
//        request.setSkuList(skuList);
//        String requestBody = JsonUtils.obj2Json(request);
//        String atp_inventory_url = PropertyUtils.getString("atp_inventory_url");
//        int atp_inventory_timeout = PropertyUtils.getInt("atp_inventory_timeout");
//        String atp_inventory_charset = PropertyUtils.getString("atp_inventory_charset");
//        Map<String, String> headMap = new HashMap<String, String>();
//        headMap.put("Content-type", "application/json; charset=utf-8");
//        headMap.put("Accept", "application/json");
//        headMap.put("api-version", "1.1");
//        headMap.put("random", RandomUtils.randomStr2(32));
//        headMap.put("platform", "1");
//        try{
//            String res  = HttpClientUtils.postBody(atp_inventory_url,  requestBody,atp_inventory_timeout , atp_inventory_charset, headMap);
//            logger.info("库存同步返回结果是: "+res);
//        }catch (Exception ex ){
//            logger.error("库存同步异常"); // TODO: 16/9/10 库存同步
//            logger.error(ex.getMessage());
//        }




    }
}
