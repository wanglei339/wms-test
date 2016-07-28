package com.lsh.wms.core.service.location;

import com.lsh.wms.model.baseinfo.IBaseinfoLocaltionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
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
    //建立货位的枚举,因为货位的类型有多样的,如果需要查询所有的bin,需要分别按现有的bin类型查找,然后将查到的list追加在一起仅用于(list页面)
    public static final List<Integer> BINTYPELIST = Arrays.asList(12, 13, 14, 15, 16, 17, 18);
    //货区
    public static final List<Integer> REGIONTYPELIST = Arrays.asList(3, 4, 5, 6, 7, 8, 9);


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
        Integer type = (Integer) params.get("type");
        IStrategy strategy = locationDetailServiceFactory.createDetailServiceByType(type);
        List<IBaseinfoLocaltionModel> locationList = new ArrayList<IBaseinfoLocaltionModel>();
        //如果是传过来的type是11显示所有的货位,则需要将按type的结果集list不断追加所有按各货位的type查出来的list
        if (11 == type) {
            for (Integer binType : BINTYPELIST) {
                params.put("type", binType);
                List<IBaseinfoLocaltionModel> tempList = strategy.getBaseinfoLocaltionModelList(params);
                locationList.addAll(tempList);
            }
            return locationList;
        }else if (2 == type){
            for (Integer regionType : REGIONTYPELIST) {
                params.put("type", regionType);
                List<IBaseinfoLocaltionModel> tempList = strategy.getBaseinfoLocaltionModelList(params);
                locationList.addAll(tempList);
            }
            return locationList;
        } else {
            params.put("type", type);
            return strategy.getBaseinfoLocaltionModelList(params);
        }
    }

    //计数
    public Integer countLocationDetail(Map<String, Object> params) {
        Integer type = (Integer) params.get("type");
//        System.out.println("type~~~~~~~~~~~~~~~~~~"+type);
        IStrategy iStrategy = locationDetailServiceFactory.createDetailServiceByType(type);

        return iStrategy.countBaseinfoLocaltionModel(params);
    }


}
