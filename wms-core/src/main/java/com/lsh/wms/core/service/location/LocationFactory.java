package com.lsh.wms.core.service.location;

import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.baseinfo.IBaseinfoLocaltionModel;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * Created by zengwenjun on 16/7/23.
 */
@Component
public class LocationFactory {

    private IStrategy strategy;
    //构造函数，要你使用哪个策略


    public LocationFactory(IStrategy strategy){
        this.strategy = strategy;
    }

    public void insert(BaseinfoLocation baseinfoLocaltionModel){
        this.strategy.insert(baseinfoLocaltionModel);
    }
    public void update(  BaseinfoLocation baseinfoLocaltionModel){
        this.strategy.update(baseinfoLocaltionModel);
    }
    IBaseinfoLocaltionModel getBaseinfoItemLocationModelById(Long id) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return this.strategy.getBaseinfoItemLocationModelById(id);
    }

    Integer countBaseinfoLocaltionModel(Map<String, Object> params){
        return this.strategy.countBaseinfoLocaltionModel(params);
    }

    List<BaseinfoLocation> getBaseinfoLocaltionModelList(Map<String, Object> params) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return this.strategy.getBaseinfoLocaltionModelList(params);
    }

}
