package com.lsh.wms.rpc.service.datareport;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.api.service.datareport.ISkuMapRpcService;
import com.lsh.wms.api.service.wumart.IWuMartSap;
import com.lsh.wms.core.constant.CsiConstan;
import com.lsh.wms.core.service.datareport.SkuMapService;
import com.lsh.wms.core.service.utils.SkuUtil;
import com.lsh.wms.model.datareport.SkuMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lixin-mac on 2016/12/8.
 */
@Service(protocol = "dubbo")
public class SkuMapRpcService implements ISkuMapRpcService{
    private static Logger logger = LoggerFactory.getLogger(SkuMapRpcService.class);
    @Reference
    private IWuMartSap wuMartSap;
    @Autowired
    private SkuMapService skuMapService;

    public void insertSkuMap(List<String> skuCodes) {

        List<SkuMap> addSkuMapList = new ArrayList<SkuMap>();
        List<SkuMap> updateSkuMapList = new ArrayList<SkuMap>();
        for(String skuCode : skuCodes){
            BigDecimal price = wuMartSap.map2Sap(SkuUtil.getSkuCode(skuCode));
            SkuMap skuMap = skuMapService.getSkuMapBySkuCodeAndOwner(skuCode, CsiConstan.OWNER_WUMART);
            if(skuMap == null){
                skuMap = new SkuMap();
                skuMap.setSkuCode(skuCode);
                skuMap.setOwnerId(CsiConstan.OWNER_WUMART);
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
        logger.info("~~~~~addSkuMapList :" + JSON.toJSONString(addSkuMapList));
        logger.info("~~~~~updateSkuMapList : " + JSON.toJSONString(updateSkuMapList));

        skuMapService.batchModifySkuMap(addSkuMapList,updateSkuMapList);

    }
}
