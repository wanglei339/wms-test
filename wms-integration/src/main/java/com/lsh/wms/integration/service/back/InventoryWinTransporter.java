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
public class InventoryWinTransporter implements ITransporter {
    @Autowired
    private DataBackService dataBackService;

    @Autowired
    private StockTakingService stockTakingService;
    @Autowired
    private ItemService itemService;


    public void process(SysLog sysLog) {

        StockTakingHead stockTakingHead = stockTakingService.getHeadById(sysLog.getBusinessId());
        List<StockTakingDetail> stockTakingDetails = stockTakingService.getDetailByTakingId(sysLog.getBusinessId());
        //盘亏 盘盈的分成两个list itemsLoss为盘亏 itemsWin盘盈
        StockRequest request = new StockRequest();
        List<StockItem> itemsWin = new ArrayList<StockItem>();


        for(StockTakingDetail stockTakingDetail : stockTakingDetails){
            StockItem item = new StockItem();
            BaseinfoItem baseinfoItem = itemService.getItem(stockTakingDetail.getItemId());
            if(baseinfoItem.getOwnerId() == 1){

                item.setEntryUom("EA");
                item.setMaterialNo(baseinfoItem.getSkuCode());
                //实际值大于理论值 报溢
                if(stockTakingDetail.getRealQty().compareTo(stockTakingDetail.getTheoreticalQty()) > 0){
                    item.setEntryQnt(stockTakingDetail.getTheoreticalQty().subtract(stockTakingDetail.getRealQty()).toString());
                    itemsWin.add(item);
                }
            }
        }
        request.setPlant(PropertyUtils.getString("wumart.werks"));
        if(itemsWin != null || itemsWin.size() > 0 ){
            request.setMoveType(String.valueOf(IntegrationConstan.WIN));
            request.setItems(itemsWin);
            dataBackService.wmDataBackByPost(JSON.toJSONString(request),IntegrationConstan.URL_STOCKCHANGE,SysLogConstant.LOG_TYPE_LOSS_WIN,sysLog);
        }

    }
}
