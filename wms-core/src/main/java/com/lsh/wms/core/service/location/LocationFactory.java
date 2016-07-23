package com.lsh.wms.core.service.location;

import com.lsh.wms.model.baseinfo.IBaseinfoLocaltionModel;
import java.util.List;
import java.util.Map;

/**
 * Created by zengwenjun on 16/7/23.
 */
public class LocationFactory {

    private IStrategy strategy;
    //构造函数，要你使用哪个策略


    public LocationFactory(IStrategy strategy){
        this.strategy = strategy;
    }

    public void insert(IBaseinfoLocaltionModel baseinfoLocaltionModel){
        this.strategy.insert(baseinfoLocaltionModel);
    }
    public void update(IBaseinfoLocaltionModel  baseinfoLocaltionModel){
        this.strategy.update(baseinfoLocaltionModel);
    }
    IBaseinfoLocaltionModel getBaseinfoItemLocationModelById(Long id){
        return this.strategy.getBaseinfoItemLocationModelById(id);
    }

    Integer countBaseinfoLocaltionModel(Map<String, Object> params){
        return this.strategy.countBaseinfoLocaltionModel(params);
    }

    List<IBaseinfoLocaltionModel> getBaseinfoLocaltionModelList(Map<String, Object> params){
        return this.strategy.getBaseinfoLocaltionModelList(params);
    }

}
