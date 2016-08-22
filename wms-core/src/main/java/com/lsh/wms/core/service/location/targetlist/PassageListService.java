package com.lsh.wms.core.service.location.targetlist;

import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 查找通道
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 16/8/10 下午6:31
 */
@Component
@Transactional(readOnly = true)
public class PassageListService implements TargetListHandler {
    @Autowired
    private LocationService locationService;
    public List<BaseinfoLocation> getTargetLocaltionModelList() {

        List<BaseinfoLocation> passageList = locationService.getLocationsByType(LocationConstant.PASSAGE);
        return passageList;
    }
}
