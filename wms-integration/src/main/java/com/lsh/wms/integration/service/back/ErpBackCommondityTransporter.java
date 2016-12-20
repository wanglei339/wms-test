package com.lsh.wms.integration.service.back;

import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.taking.StockTakingService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.stock.OverLossReport;
import com.lsh.wms.model.system.SysLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by lixin-mac on 2016/11/17.
 */
@Component
public class ErpBackCommondityTransporter implements ITransporter{

    @Autowired
    private DataBackService dataBackService;

    @Autowired
    private ItemService itemService;



    public void process(SysLog sysLog) {
        BaseinfoItem baseinfoItem = itemService.getItem(sysLog.getBusinessId());
        if(baseinfoItem.getOwnerId().compareTo(2L)==0){
            dataBackService.erpBackCommodityData(baseinfoItem, sysLog);
        }

    }
}
