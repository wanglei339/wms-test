package com.lsh.wms.core.service.location;

import com.lsh.wms.core.dao.baseinfo.BaseinfoLocationDockDao;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.baseinfo.BaseinfoLocationDock;
import com.lsh.wms.model.baseinfo.IBaseinfoLocaltionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 16/7/23 下午6:58
 */
@Component
@Transactional(readOnly = true)
public class BaseinfoLocationDockService implements IStrategy {
    private static final Logger logger = LoggerFactory.getLogger(LocationService.class);

    @Autowired
    private BaseinfoLocationDockDao baseinfoLocationDockDao;
    @Autowired
    private LocationService locationService;
//    @Autowired
//    private FatherToChildUtil fatherToChildUtil;
//    @Autowired
//    private LocationDetailModelFactory locationDetailModelFactory;
//    @Autowired
//    private LocationDetailServiceFactory locationDetailServiceFactory;
//
//    @PostConstruct
//    public void postConstruct(){
//        locationDetailModelFactory.register(LocationConstant.Dock_area,new BaseinfoLocationDock());
//        locationDetailServiceFactory.register(LocationConstant.Dock_area,this);
//    }

    /**
     * 只完成插入服务
     *
     * @param
     */
    @Transactional(readOnly = false)
    public void insert(IBaseinfoLocaltionModel iBaseinfoLocaltionModel) {
        baseinfoLocationDockDao.insert((BaseinfoLocationDock) iBaseinfoLocaltionModel);

    }

    @Transactional(readOnly = false)
    public void update(IBaseinfoLocaltionModel iBaseinfoLocaltionModel) {
        baseinfoLocationDockDao.insert((BaseinfoLocationDock) iBaseinfoLocaltionModel);

    }

    public BaseinfoLocation getBaseinfoItemLocationModelById(Long id) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("locationId", id);
        BaseinfoLocation baseinfoLocation = locationService.getLocationListByType(mapQuery);
        BaseinfoLocationDock baseinfoLocationDock = null;
        List<BaseinfoLocationDock> dockList = null;
        //
        if (baseinfoLocation != null) {
            dockList = baseinfoLocationDockDao.getBaseinfoLocationDockList(mapQuery);
            baseinfoLocationDock = dockList.get(0);
            //设置子类信息
            baseinfoLocationDock.setLocationCode(baseinfoLocation.getLocationCode());
            baseinfoLocationDock.setFatherId(baseinfoLocation.getFatherId());
            baseinfoLocationDock.setType(baseinfoLocation.getType());
            baseinfoLocationDock.setTypeName(baseinfoLocation.getTypeName());
            baseinfoLocationDock.setIsLeaf(baseinfoLocation.getIsLeaf());
            baseinfoLocationDock.setIsValid(baseinfoLocation.getIsValid());
            baseinfoLocationDock.setCanStore(baseinfoLocation.getCanStore());
            baseinfoLocationDock.setContainerVol(baseinfoLocation.getContainerVol());
            baseinfoLocationDock.setRegionNo(baseinfoLocation.getRegionNo());
            baseinfoLocationDock.setPassageNo(baseinfoLocation.getPassageNo());
            baseinfoLocationDock.setShelfLevelNo(baseinfoLocation.getShelfLevelNo());
            baseinfoLocationDock.setBinPositionNo(baseinfoLocation.getBinPositionNo());
            //设置占用与否
            if (locationService.isLocationInUse(id)) {
                baseinfoLocationDock.setIsUsed("已占用");
            } else {
                baseinfoLocationDock.setIsUsed("未占用");
            }
            return baseinfoLocationDock;
        }
        return null;
    }

    /**
     * Location的主表没有码头的出和入的性质
     * 不包含码头的dock_type数目就是主表的数
     * 含有出入码头,就计数为dock表的条目数
     *
     * @param params
     * @return
     */
    public Integer countBaseinfoLocaltionModel(Map<String, Object> params) {
        if ((params.get("dock_type") == null)) {
            return locationService.countLocation(params);
        }
        return baseinfoLocationDockDao.countBaseinfoLocationDock(params);
    }

    /**
     * 返回BaseinfoLocationDock的getList
     *
     * @param params
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public List<BaseinfoLocation> getBaseinfoLocaltionModelList(Map<String, Object> params) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<BaseinfoLocation> baseinfoLocationList = locationService.getBaseinfoLocationList(params);
        List<BaseinfoLocationDock> baseinfoLocationDocks = new ArrayList<BaseinfoLocationDock>();
        BaseinfoLocationDock baseinfoLocationDock = null;
        List<BaseinfoLocationDock> dockList = null;
        //循环父类list逐个拷贝到子类,并添加到子类list中
        if (baseinfoLocationList.size() > 0) {
            for (BaseinfoLocation baseinfoLocation : baseinfoLocationList) {
                //根据父类id获取子类bin
                Long locationId = baseinfoLocation.getLocationId();
                params.put("locationId",locationId);
                dockList = baseinfoLocationDockDao.getBaseinfoLocationDockList(params);
                baseinfoLocationDock = dockList.get(0);
                //设置子类信息
                baseinfoLocationDock.setLocationCode(baseinfoLocation.getLocationCode());
                baseinfoLocationDock.setFatherId(baseinfoLocation.getFatherId());
                baseinfoLocationDock.setType(baseinfoLocation.getType());
                baseinfoLocationDock.setTypeName(baseinfoLocation.getTypeName());
                baseinfoLocationDock.setIsLeaf(baseinfoLocation.getIsLeaf());
                baseinfoLocationDock.setIsValid(baseinfoLocation.getIsValid());
                baseinfoLocationDock.setCanStore(baseinfoLocation.getCanStore());
                baseinfoLocationDock.setContainerVol(baseinfoLocation.getContainerVol());
                baseinfoLocationDock.setRegionNo(baseinfoLocation.getRegionNo());
                baseinfoLocationDock.setPassageNo(baseinfoLocation.getPassageNo());
                baseinfoLocationDock.setShelfLevelNo(baseinfoLocation.getShelfLevelNo());
                baseinfoLocationDock.setBinPositionNo(baseinfoLocation.getBinPositionNo());

                //设置占用与否
                if (locationService.isLocationInUse(locationId)) {
                    baseinfoLocationDock.setIsUsed("已占用");
                } else {
                    baseinfoLocationDock.setIsUsed("未占用");
                }
                baseinfoLocationDocks.add(baseinfoLocationDock);
            }
            System.out.println(baseinfoLocationDocks.size());
            return (List<BaseinfoLocation>) (List<?>) baseinfoLocationDocks;
        }
        return null;
    }

}


