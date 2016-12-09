package com.lsh.wms.integration.service.back;

import com.lsh.wms.api.model.wumart.CreateMovingDetail;
import com.lsh.wms.api.model.wumart.CreateMovingHeader;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.stock.StockMoveService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.core.service.utils.SkuUtil;
import com.lsh.wms.integration.service.wumartsap.WuMart;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.stock.StockMove;
import com.lsh.wms.model.system.SysLog;
import com.lsh.wms.model.task.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lixin-mac on 2016/12/7.
 */
@Component
public class MovingTransporter implements ITransporter{

    @Autowired
    private StockMoveService stockMoveService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private ItemService itemService;

    @Autowired
    private WuMart wuMart;

    public void process(SysLog sysLog) {
        StockMove move = stockMoveService.getStockMoveByTaskId(sysLog.getBusinessId());
        CreateMovingHeader header = new CreateMovingHeader();
        List<CreateMovingDetail> list = new ArrayList<CreateMovingDetail>();
        CreateMovingDetail detail = new CreateMovingDetail();
        BaseinfoLocation fromLocation = locationService.getLocation(move.getFromLocationId());
        if(LocationConstant.BACK_AREA == fromLocation.getType()){
            detail.setFromLocation("0002");
        }else if(LocationConstant.DEFECTIVE_AREA == fromLocation.getType()){
            detail.setFromLocation("0003");
        }else{
            detail.setFromLocation("0001");
        }

        BaseinfoLocation toLocation = locationService.getLocation(move.getToLocationId());
        if(LocationConstant.BACK_AREA == toLocation.getType()){
            detail.setToLocation("0002");
        }else if(LocationConstant.DEFECTIVE_AREA == toLocation.getType()){
            detail.setToLocation("0003");
        }else{
            detail.setToLocation("0001");
        }
        detail.setPackName("EA");
        //detail.setToLocation("");
        detail.setQty(move.getQty());
        BaseinfoItem item = itemService.getItem(move.getItemId());
        detail.setSkuCode(SkuUtil.getSkuCode(item.getSkuCode()));
        list.add(detail);
        header.setDetails(list);
        wuMart.stockMoving2Sap(header,sysLog);
    }
}
