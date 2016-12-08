package com.lsh.wms.rpc.service.datareport;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.api.service.datareport.ISkuMapRpcService;
import com.lsh.wms.api.service.wumart.IWuMartSap;
import com.lsh.wms.core.service.datareport.SkuMapService;
import com.lsh.wms.model.datareport.SkuMap;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lixin-mac on 2016/12/8.
 */
@Service(protocol = "dubbo")
public class SkuMapRpcService implements ISkuMapRpcService{

    @Reference
    private IWuMartSap wuMartSap;
    @Autowired
    private SkuMapService skuMapService;

    public void insertSkuMap(List<String> skuCodes) {
        List<SkuMap> addSkuMapList = new ArrayList<SkuMap>();
        List<SkuMap> updateSkuMapList = new ArrayList<SkuMap>();
        for(String skuCode : skuCodes){
            BigDecimal price = wuMartSap.map2Sap(skuCode);
            SkuMap skuMap = skuMapService.getSkuMapBySkuCode(skuCode);
            if(skuMap == null){
                skuMap = new SkuMap();
                skuMap.setSkuCode(skuCode);
                skuMap.setMovingAveragePrice(price);
                skuMap.setUpdatedAt(DateUtils.getCurrentSeconds());
                skuMap.setCreatedAt(DateUtils.getCurrentSeconds());
                addSkuMapList.add(skuMap);
            }else{
                skuMap.setMovingAveragePrice(price);
                skuMap.setUpdatedAt(DateUtils.getCurrentSeconds());
                updateSkuMapList.add(skuMap);
            }
        }

        skuMapService.batchModifySkuMap(addSkuMapList,updateSkuMapList);

    }
}
