package com.lsh.wms.core.service.location.targetlist;

import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查找抽象区
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 16/8/10 下午6:28
 */
@Component
@Transactional(readOnly = true)
public class Area implements TargetListHandler {
    @Autowired
    private LocationService locationService;

    /**
     * 查找大区list
     * @return
     */
    public List<BaseinfoLocation> getTargetLocaltionModelList() {

        List<BaseinfoLocation> areaList = locationService.getLocationsByType("area");
        return areaList;
    }
}
