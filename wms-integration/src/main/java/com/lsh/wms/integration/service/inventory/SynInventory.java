package com.lsh.wms.integration.service.inventory;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.wms.api.service.inventory.ISynInventory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    public void synInventory(Long sku_id) {
        for(int i=0;i<10000;i++){ // TODO: 16/8/20  
            logger.info(sku_id.toString());
            try{
                Thread.currentThread().sleep(1000);
            }catch (Exception ex){
                ex.printStackTrace();
            }

        }

    }
}
