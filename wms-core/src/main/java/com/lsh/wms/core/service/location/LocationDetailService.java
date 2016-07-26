package com.lsh.wms.core.service.location;

import com.lsh.wms.model.baseinfo.IBaseinfoLocaltionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 封装所有的底层增删改查的底层service服务
 * 然后封装在locationservice中,只操作locationservice,detail就发生变化
 *
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 16/7/24 下午12:28
 */
@Service
@Transactional(readOnly = true)
public class LocationDetailService {
    private static final Logger logger = LoggerFactory.getLogger(LocationService.class);
    @Autowired
    private LocationDetailServiceFactory locationDetailServiceFactory;
    @Autowired
    private LocationService locationService;

    public void insert(IBaseinfoLocaltionModel baseinfoLocaltionModel) {
        //校验
        if (null == baseinfoLocaltionModel) {
            //TODO 抛异常
            return;
        }
        //根据model选择service
        IStrategy iStrategy = locationDetailServiceFactory.createDetailServiceByModel(baseinfoLocaltionModel);
        iStrategy.insert(baseinfoLocaltionModel);
    }

    public void update(IBaseinfoLocaltionModel baseinfoLocaltionModel) {
        //校验
        if (null == baseinfoLocaltionModel) {
            //TODO 抛异常
            return;
        }
        IStrategy iStrategy = locationDetailServiceFactory.createDetailServiceByModel(baseinfoLocaltionModel);
        iStrategy.update(baseinfoLocaltionModel);
    }

    //前端id怎么去哪个detail表查,前端的id带来type类型`
    public IBaseinfoLocaltionModel getIBaseinfoLocaltionModelByIdAndType(Long id, Integer type) {
        IStrategy iStrategy = locationDetailServiceFactory.createDetailServiceByType(type);
        IBaseinfoLocaltionModel iBaseinfoLocaltionModel = iStrategy.getBaseinfoItemLocationModelById(id);
        return iBaseinfoLocaltionModel;
    }

    public List<IBaseinfoLocaltionModel> getIBaseinfoLocaltionModelListByType(Map<String, Object> params) {
        String typeStr = (String) params.get("type");
        Integer type = Integer.parseInt(typeStr);
        IStrategy strategy = locationDetailServiceFactory.createDetailServiceByType(type);
        params.put("type",type);
        //TODO 如果是region的话,需要具体设置相应的type,无所谓,params中设置type的类型就行
        return strategy.getBaseinfoLocaltionModelList(params);

    }

    //计数
    public Integer countLocationDetail(Map<String,Object> params){
        Integer type = (Integer) params.get("type");
        System.out.println("type~~~~~~~~~~~~~~~~~~"+type);
        IStrategy iStrategy = locationDetailServiceFactory.createDetailServiceByType(type);

        return iStrategy.countBaseinfoLocaltionModel(params);
    }




}
