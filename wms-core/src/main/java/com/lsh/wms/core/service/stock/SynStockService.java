package com.lsh.wms.core.service.stock;

import com.lsh.atp.api.model.baseVo.SkuVo;
import com.lsh.atp.api.model.inventory.InventorySyncLshRequest;
import com.lsh.base.common.config.PropertyUtils;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.net.HttpClientUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class SynStockService{

    private static Logger logger = LoggerFactory.getLogger(SynStockService.class);
    @Autowired
    private ItemService itemService;

    public void synStock(Long item_id, Double qty) { // TODO: 16/9/8
        InventorySyncLshRequest request = new InventorySyncLshRequest();
        request.setZoneCode(PropertyUtils.getString("zone_code"));
        request.setSystem(PropertyUtils.getString("system"));
        BaseinfoItem baseinfoItem   = itemService.getItem(item_id);
        Long ownerId = baseinfoItem.getOwnerId();
        request.setDcCode(PropertyUtils.getString("owner_"+ownerId));   // TODO: 16/9/7
        List<SkuVo> skuList = new ArrayList<SkuVo>();
        SkuVo itemDc = new SkuVo();
        itemDc.setItemId(item_id);
        itemDc.setQty(new BigDecimal(qty)); // TODO: 16/9/7
        skuList.add(itemDc);
        request.setSkuList(skuList);
        String requestBody = JsonUtils.obj2Json(request);
        String atp_inventory_url = PropertyUtils.getString("atp_inventory_url");
        int atp_inventory_timeout = PropertyUtils.getInt("atp_inventory_timeout");
        String atp_inventory_charset = PropertyUtils.getString("atp_inventory_charset");
        Map<String, String> headMap = new HashMap<String, String>();
        headMap.put("Content-type", "application/json; charset=utf-8");
        headMap.put("Accept", "application/json");
        headMap.put("api-version", "1.1");
        headMap.put("random", RandomUtils.randomStr2(32));
        headMap.put("platform", "1");
        try{
            String res  = HttpClientUtils.postBody(atp_inventory_url,  requestBody,atp_inventory_timeout , atp_inventory_charset, headMap);
            logger.info("库存同步返回结果是: "+res);
        }catch (Exception ex ){
            logger.error("库存同步异常"); // TODO: 16/9/10 库存同步 
            logger.error(ex.getMessage());
        }
       

    }
}
