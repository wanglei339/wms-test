package com.lsh.wms.integration.service.back;

import com.alibaba.fastjson.JSON;
import com.lsh.base.common.config.PropertyUtils;
import com.lsh.wms.api.model.stock.StockItem;
import com.lsh.wms.api.model.stock.StockRequest;
import com.lsh.wms.core.constant.IntegrationConstan;
import com.lsh.wms.core.constant.SysLogConstant;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.taking.StockTakingService;
import com.lsh.wms.core.service.utils.SkuUtil;
import com.lsh.wms.model.stock.OverLossReport;
import com.lsh.wms.model.system.SysLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lixin-mac on 2016/11/17.
 */
@Component
public class ErpInventoryTransporter implements ITransporter{

    @Autowired
    private DataBackService dataBackService;

    @Autowired
    private StockTakingService stockTakingService;



    public void process(SysLog sysLog) {
        OverLossReport overLossReport = stockTakingService.getOverLossReportById(sysLog.getBusinessId());
        if(overLossReport.getOwnerId().compareTo(2L)==0){
            dataBackService.erpOvLosserBack(overLossReport,sysLog);
        }

    }
}
