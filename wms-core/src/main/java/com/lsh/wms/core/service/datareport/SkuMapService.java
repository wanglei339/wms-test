package com.lsh.wms.core.service.datareport;

import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.core.dao.datareport.SkuMapDao;
import com.lsh.wms.model.datareport.SkuMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lixin-mac on 2016/12/8.
 */
@Component
@Transactional(readOnly = true)
public class SkuMapService {
    private static final Logger logger = LoggerFactory.getLogger(SkuMapService.class);

    @Autowired
    private SkuMapDao skuMapDao;

    @Transactional(readOnly = false)
    public void insertSkuCode(SkuMap skuMap){
        skuMap.setCreatedAt(DateUtils.getCurrentSeconds());
        skuMap.setUpdatedAt(DateUtils.getCurrentSeconds());
        skuMapDao.insert(skuMap);
    }

    @Transactional(readOnly = false)
    public void updateSkuCode(SkuMap skuMap){
        skuMap.setUpdatedAt(DateUtils.getCurrentSeconds());
        skuMapDao.update(skuMap);
    }

    @Transactional(readOnly = false)
    public void batchUpdate(List<SkuMap> skuMaps){
        skuMapDao.batchUpdate(skuMaps);
    }
    @Transactional(readOnly = false)
    public void batchAdd(List<SkuMap> skuMaps){
        skuMapDao.batchInsert(skuMaps);
    }


    public List<SkuMap> getSkuMapList(Map<String,Object> map){
        return skuMapDao.getSkuMapList(map);
    }

    public SkuMap getSkuMapBySkuCodeAndOwner(String skuCode,Long ownerId){
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("skuCode",skuCode);
        map.put("ownerId",ownerId);
        List<SkuMap> skuMaps = skuMapDao.getSkuMapList(map);
        if(skuMaps == null || skuMaps.size() == 0){
            return null;
        }
        return skuMaps.get(0);
    }

    @Transactional(readOnly = false)
    public void batchModifySkuMap(List<SkuMap> addSkuMap,List<SkuMap> updateSkuMap){
        if(addSkuMap != null && addSkuMap.size() > 0){
            this.batchAdd(addSkuMap);
        }
        if(updateSkuMap != null && updateSkuMap.size() > 0){
            this.batchUpdate(updateSkuMap);
        }
    }

}
