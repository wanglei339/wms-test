package com.lsh.wms.core.service.location;

import com.lsh.wms.core.dao.baseinfo.BaseinfoLocationRegionDao;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.baseinfo.BaseinfoLocationRegion;
import com.lsh.wms.model.baseinfo.BaseinfoLocationRegion;
import com.lsh.wms.model.baseinfo.IBaseinfoLocaltionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 16/7/23 下午5:10
 */
@Component
@Transactional(readOnly = true)
public class BaseinfoLocationRegionService implements IStrategy {
    private static final Logger logger = LoggerFactory.getLogger(LocationService.class);
    //增删改查
    @Autowired
    private BaseinfoLocationRegionDao baseinfoLocationRegionDao;
    @Autowired
    private LocationService locationService;

    @Transactional(readOnly = false)
    public void insert(IBaseinfoLocaltionModel iBaseinfoLocaltionModel) {
         baseinfoLocationRegionDao.insert((BaseinfoLocationRegion) iBaseinfoLocaltionModel);
    }

    @Transactional(readOnly = false)
    public void update(IBaseinfoLocaltionModel iBaseinfoLocaltionModel) {
        baseinfoLocationRegionDao.update((BaseinfoLocationRegion) iBaseinfoLocaltionModel);
    }

    
    public BaseinfoLocation getBaseinfoItemLocationModelById(Long id) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("locationId", id);
        BaseinfoLocation baseinfoLocation = locationService.getLocationListByType(mapQuery);
        BaseinfoLocationRegion baseinfoLocationRegion = null;
        List<BaseinfoLocationRegion> regionList = new ArrayList<BaseinfoLocationRegion>();
        //
        if (baseinfoLocation != null) {
            regionList = baseinfoLocationRegionDao.getBaseinfoLocationRegionList(mapQuery);
            baseinfoLocationRegion = regionList.get(0);
            //将父亲location的属性值拷贝给BaseinfoLocationRegion
            //设置子类信息
            baseinfoLocationRegion.setLocationCode(baseinfoLocation.getLocationCode());
            baseinfoLocationRegion.setFatherId(baseinfoLocation.getFatherId());
            baseinfoLocationRegion.setType(baseinfoLocation.getType());
            baseinfoLocationRegion.setTypeName(baseinfoLocation.getTypeName());
            baseinfoLocationRegion.setIsLeaf(baseinfoLocation.getIsLeaf());
            baseinfoLocationRegion.setIsValid(baseinfoLocation.getIsValid());
            baseinfoLocationRegion.setCanStore(baseinfoLocation.getCanStore());
            baseinfoLocationRegion.setContainerVol(baseinfoLocation.getContainerVol());
            baseinfoLocationRegion.setRegionNo(baseinfoLocation.getRegionNo());
            baseinfoLocationRegion.setPassageNo(baseinfoLocation.getPassageNo());
            baseinfoLocationRegion.setShelfLevelNo(baseinfoLocation.getShelfLevelNo());
            baseinfoLocationRegion.setBinPositionNo(baseinfoLocation.getBinPositionNo());
            //设置占用与否
            if (locationService.isLocationInUse(id)) {
                baseinfoLocationRegion.setIsUsed("已占用");
            } else {
                baseinfoLocationRegion.setIsUsed("未占用");
            }
            return baseinfoLocationRegion;
        }
        return null;
    }

    public Integer countBaseinfoLocaltionModel(Map<String, Object> params) {
        return locationService.countLocation(params);
    }

    public List<BaseinfoLocation> getBaseinfoLocaltionModelList(Map<String, Object> params) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<BaseinfoLocation> baseinfoLocationList = locationService.getBaseinfoLocationList(params);
        List<BaseinfoLocationRegion> baseinfoLocationRegions = new ArrayList<BaseinfoLocationRegion>();
        BaseinfoLocationRegion baseinfoLocationRegion = null;
        List<BaseinfoLocationRegion> regionList = null;
        //循环父类list逐个拷贝到子类,并添加到子类list中
        if (baseinfoLocationList.size() > 0) {
            for (BaseinfoLocation baseinfoLocation:baseinfoLocationList){
                //根据父类id获取子类bin
                Long locationId = baseinfoLocation.getLocationId();
                //设置id
                params.put("locationId",locationId);
                regionList = baseinfoLocationRegionDao.getBaseinfoLocationRegionList(params);
                baseinfoLocationRegion = regionList.get(0);
                //设置子类信息
                baseinfoLocationRegion.setLocationCode(baseinfoLocation.getLocationCode());
                baseinfoLocationRegion.setFatherId(baseinfoLocation.getFatherId());
                baseinfoLocationRegion.setType(baseinfoLocation.getType());
                baseinfoLocationRegion.setTypeName(baseinfoLocation.getTypeName());
                baseinfoLocationRegion.setIsLeaf(baseinfoLocation.getIsLeaf());
                baseinfoLocationRegion.setIsValid(baseinfoLocation.getIsValid());
                baseinfoLocationRegion.setCanStore(baseinfoLocation.getCanStore());
                baseinfoLocationRegion.setContainerVol(baseinfoLocation.getContainerVol());
                baseinfoLocationRegion.setRegionNo(baseinfoLocation.getRegionNo());
                baseinfoLocationRegion.setPassageNo(baseinfoLocation.getPassageNo());
                baseinfoLocationRegion.setShelfLevelNo(baseinfoLocation.getShelfLevelNo());
                baseinfoLocationRegion.setBinPositionNo(baseinfoLocation.getBinPositionNo());

                //设置占用与否
                if (locationService.isLocationInUse(locationId)) {
                    baseinfoLocationRegion.setIsUsed("已占用");
                } else {
                    baseinfoLocationRegion.setIsUsed("未占用");
                }
                baseinfoLocationRegions.add(baseinfoLocationRegion);
            }
            return (List<BaseinfoLocation>) (List<?>) baseinfoLocationRegions;
        }
        return null;
    }
}
