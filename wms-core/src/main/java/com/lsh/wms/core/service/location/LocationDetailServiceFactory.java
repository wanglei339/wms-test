package com.lsh.wms.core.service.location;

import org.springframework.stereotype.Component;

import java.util.*;


/**
 * location的具体service选用的工厂方法,通过传入不同的参数类型,实例化不同的service
 * 生产不同的service
 *
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 16/7/24 上午10:53
 */
@Component
public class LocationDetailServiceFactory {
    private Map<Long,IStrategy> iStrategyMap = new HashMap<Long, IStrategy>();
    public void register(Long locationType,IStrategy iStrategy){
        iStrategyMap.put(locationType,iStrategy);
    }
    public IStrategy getIstrategy(Long locationType){
        return iStrategyMap.get(locationType);
    }

}
