package com.lsh.wms.integration.service.back;

import com.alibaba.fastjson.JSON;
import com.lsh.base.common.config.PropertyUtils;
import com.lsh.wms.api.model.stock.StockItem;
import com.lsh.wms.api.model.stock.StockRequest;
import com.lsh.wms.core.constant.IntegrationConstan;
import com.lsh.wms.core.constant.SysLogConstant;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.taking.StockTakingService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.stock.OverLossReport;
import com.lsh.wms.model.system.SysLog;
import com.lsh.wms.model.taking.StockTakingDetail;
import com.lsh.wms.model.taking.StockTakingHead;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lixin-mac on 2016/11/17.
 */
@Component
public class InventoryTransporter implements ITransporter{

    @Autowired
    private DataBackService dataBackService;

    @Autowired
    private StockTakingService stockTakingService;
    @Autowired
    private ItemService itemService;



    public void process(SysLog sysLog) {
        OverLossReport overLossReport = stockTakingService.getOverLossReportById(sysLog.getBusinessId());

        StockRequest request = new StockRequest();
        request.setMoveType(overLossReport.getMoveType().toString());
        request.setPlant(PropertyUtils.getString("wumart.werks"));
        request.setMoveReason(overLossReport.getMoveReason());
        StockItem item = new StockItem();
        List<StockItem> list = new ArrayList<StockItem>();
        if(overLossReport.getOwnerId() == 1){
            //item.setEntryUom(overLossReport.getPackName());
            //修改为EA
            item.setEntryUom("EA");
            item.setEntryQnt(overLossReport.getQty().toString());
            item.setMaterialNo(overLossReport.getSkuCode());
            list.add(item);
            request.setMoveType(String.valueOf(IntegrationConstan.WIN));
            request.setItems(list);
            dataBackService.wmDataBackByPost(JSON.toJSONString(request),PropertyUtils.getString("url_stockchange"),SysLogConstant.LOG_TYPE_LOSS_WIN,sysLog);
        }

    }
}
