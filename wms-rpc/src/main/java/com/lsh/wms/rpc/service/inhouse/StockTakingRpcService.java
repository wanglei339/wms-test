package com.lsh.wms.rpc.service.inhouse;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.service.inhouse.IStockTakingRpcService;
import com.lsh.wms.core.service.csi.CsiSkuService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.taking.StockTakingService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.csi.CsiSku;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.taking.StockTakingDetail;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by mali on 16/7/22.
 */
@Service(protocol = "dubbo")
public class StockTakingRpcService implements IStockTakingRpcService {

    @Autowired
    private StockTakingService stockTakingService;
    @Autowired
    private StockQuantService quantService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private LocationService locationService;

    public void fillDetail(StockTakingDetail detail) throws BizCheckedException{
        if(detail!=null){
            List<StockQuant> quants = quantService.getQuantsByLocationId(detail.getLocationId());
            if(quants!=null && quants.size()!=0){
                StockQuant quant = quants.get(0);
                BaseinfoItem item = itemService.getItem(quant.getItemId());
                BaseinfoLocation location = locationService.getLocation(quant.getLocationId());
                detail.setSkuId(quant.getSkuId());
                detail.setContainerId(quant.getContainerId());
                detail.setItemId(quant.getItemId());
                detail.setRealItemId(quant.getItemId());
                detail.setRealSkuId(detail.getSkuId());
                detail.setPackName(quant.getPackName());
                detail.setPackUnit(quant.getPackUnit());
                detail.setOwnerId(quant.getOwnerId());
                detail.setLotId(quant.getLotId());
                detail.setSkuCode(item.getSkuCode());
                detail.setSkuName(item.getSkuName());
                detail.setBarcode(item.getCode());
                detail.setLocationCode(location.getLocationCode());
            }
            stockTakingService.updateDetail(detail);
        }
    }

}
