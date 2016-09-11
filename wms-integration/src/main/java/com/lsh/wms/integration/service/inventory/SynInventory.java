package com.lsh.wms.integration.service.inventory;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.atp.api.model.baseVo.ItemDc;
import com.lsh.atp.api.model.baseVo.SkuVo;
import com.lsh.atp.api.model.inventory.InventorySyncLshRequest;
import com.lsh.atp.api.service.inventory.IInventorySyncLshRpcService;
import com.lsh.base.common.config.PropertyUtils;
import com.lsh.wms.api.service.inventory.ISynInventory;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * Project Name: lsh-wms
 * Created by fuhao
 * Date: 16/8/20
 * Time: 16/8/20.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.integration.service.inventory.
 * desc:类功能描述
 */

@Service(protocol = "dubbo",async=true)
public class SynInventory implements ISynInventory {
    private static Logger logger = LoggerFactory.getLogger(SynInventory.class);

    @Reference(async = true)
    private IInventorySyncLshRpcService iInventorySyncLshRpcService;


    @Autowired
    private ItemService itemService;


    public void synInventory(Long item_id,Double qty) {
        InventorySyncLshRequest request = new InventorySyncLshRequest();
        request.setZoneCode(PropertyUtils.getString("zone_code"));
        request.setSystem(PropertyUtils.getString("system"));
        BaseinfoItem  baseinfoItem   = itemService.getItem(item_id);
        Long ownerId = baseinfoItem.getOwnerId();
        request.setDcCode(PropertyUtils.getString("owner_"+ownerId));   // TODO: 16/9/7
        List<SkuVo> skuList = new ArrayList<SkuVo>();
        SkuVo itemDc = new SkuVo();
        itemDc.setItemId(item_id);
        itemDc.setQty(new BigDecimal(qty)); // TODO: 16/9/7
        request.setSkuList(skuList);
        iInventorySyncLshRpcService.inventorySyncLsh(request);
    }
}
