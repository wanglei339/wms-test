package com.lsh.wms.core.service.location;

import com.lsh.wms.model.baseinfo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 注册机加工厂方法
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 16/7/29 下午2:28
 */
@Component
public class LocationDetailModelFactory {
    private Map<Long,IBaseinfoLocaltionModel> locationMap = new HashMap<Long, IBaseinfoLocaltionModel>();
    public void register(Long locationType,IBaseinfoLocaltionModel localtionModel){
        locationMap.put(locationType,localtionModel);
    }
    public IBaseinfoLocaltionModel getLocationModel(Long locationType){
        return locationMap.get(locationType);
    }

}
