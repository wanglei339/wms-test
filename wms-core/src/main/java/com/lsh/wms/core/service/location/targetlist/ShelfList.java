package com.lsh.wms.core.service.location.targetlist;

import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 查找所有的阁楼和货架
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 16/8/10 下午6:28
 */
@Component
@Transactional(readOnly = true)
public class ShelfList implements TargetListHandler{
    @Autowired
    private LocationService locationService;
    public List<BaseinfoLocation> getTargetLocaltionModelList() {
        List<BaseinfoLocation> targetList = new ArrayList<BaseinfoLocation>();
        //查找所有货架和阁楼
        List<Long> DOMAIN_TYPE = Arrays.asList(LocationConstant.SHELF, LocationConstant.LOFT);
        //查出来拼在一起
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("isValid", 1);
        for (Long oneType : DOMAIN_TYPE) {
            mapQuery.put("type", oneType);
            List<BaseinfoLocation> locationList = locationService.getBaseinfoLocationList(mapQuery);
            if (locationList.size() > 0) {
                targetList.addAll(locationList);
            }
        }
        return targetList;
    }
}
