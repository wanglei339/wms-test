package com.lsh.wms.core.service.location;

import com.lsh.wms.core.dao.baseinfo.BaseinfoLocationShelfDao;
import com.lsh.wms.model.baseinfo.*;
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
 * @Date 16/7/23 下午7:22
 */
@Component
@Transactional(readOnly = true)
public class BaseinfoLocationShelfService implements IStrategy{
    private static final Logger logger = LoggerFactory.getLogger(BaseinfoLocationShelfService.class);
    @Autowired
    private BaseinfoLocationShelfDao baseinfoLocationShelfDao;
    @Autowired
    private LocationService locationService;

    @Transactional(readOnly = false)
    public void insert(IBaseinfoLocaltionModel iBaseinfoLocaltionModel) {
        baseinfoLocationShelfDao.insert((BaseinfoLocationShelf) iBaseinfoLocaltionModel);
    }

    @Transactional(readOnly = false)
    public void update(IBaseinfoLocaltionModel iBaseinfoLocaltionModel) {
        baseinfoLocationShelfDao.insert((BaseinfoLocationShelf) iBaseinfoLocaltionModel);
    }

    public BaseinfoLocation getBaseinfoItemLocationModelById(Long id) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("locationId", id);
        BaseinfoLocation baseinfoLocation = locationService.getLocationListByType(mapQuery);
        BaseinfoLocationShelf baseinfoLocationShelf = null;
        List<BaseinfoLocationShelf> shelfList = new ArrayList<BaseinfoLocationShelf>();
        //
        if (baseinfoLocation != null) {
            shelfList = baseinfoLocationShelfDao.getBaseinfoLocationShelfList(mapQuery);
            baseinfoLocationShelf = shelfList.get(0);
            //将父亲location的属性值拷贝给baseinfoLocationShelf
            //设置子类信息
            baseinfoLocationShelf.setLocationCode(baseinfoLocation.getLocationCode());
            baseinfoLocationShelf.setFatherId(baseinfoLocation.getFatherId());
            baseinfoLocationShelf.setType(baseinfoLocation.getType());
            baseinfoLocationShelf.setTypeName(baseinfoLocation.getTypeName());
            baseinfoLocationShelf.setIsLeaf(baseinfoLocation.getIsLeaf());
            baseinfoLocationShelf.setIsValid(baseinfoLocation.getIsValid());
            baseinfoLocationShelf.setCanStore(baseinfoLocation.getCanStore());
            baseinfoLocationShelf.setContainerVol(baseinfoLocation.getContainerVol());
            baseinfoLocationShelf.setRegionNo(baseinfoLocation.getRegionNo());
            baseinfoLocationShelf.setPassageNo(baseinfoLocation.getPassageNo());
            baseinfoLocationShelf.setShelfLevelNo(baseinfoLocation.getShelfLevelNo());
            baseinfoLocationShelf.setBinPositionNo(baseinfoLocation.getBinPositionNo());
            //设置占用与否
            if (locationService.isLocationInUse(id)) {
                baseinfoLocationShelf.setIsUsed("已占用");
            } else {
                baseinfoLocationShelf.setIsUsed("未占用");
            }
            return baseinfoLocationShelf;
        }
        return null;
    }

    public Integer countBaseinfoLocaltionModel(Map<String, Object> params) {
        return baseinfoLocationShelfDao.countBaseinfoLocationShelf(params);
    }

    public List<BaseinfoLocation> getBaseinfoLocaltionModelList(Map<String, Object> params) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        System.out.println(params.get("type"));
        List<BaseinfoLocation> baseinfoLocationList = locationService.getBaseinfoLocationList(params);
        List<BaseinfoLocationShelf> baseinfoLocationShelfs = new ArrayList<BaseinfoLocationShelf>();
        BaseinfoLocationShelf baseinfoLocationShelf = null;
        List<BaseinfoLocationShelf> shelfList = null;
        //循环父类list逐个拷贝到子类,并添加到子类list中
        if (baseinfoLocationList.size() > 0) {
            for (BaseinfoLocation baseinfoLocation:baseinfoLocationList){
                //根据父类id获取子类bin
                Long locationId = baseinfoLocation.getLocationId();
                //设置id
                params.put("locationId",locationId);
                shelfList = baseinfoLocationShelfDao.getBaseinfoLocationShelfList(params);
                baseinfoLocationShelf = shelfList.get(0);
                //设置子类信息
                baseinfoLocationShelf.setLocationCode(baseinfoLocation.getLocationCode());
                baseinfoLocationShelf.setFatherId(baseinfoLocation.getFatherId());
                baseinfoLocationShelf.setType(baseinfoLocation.getType());
                baseinfoLocationShelf.setTypeName(baseinfoLocation.getTypeName());
                baseinfoLocationShelf.setIsLeaf(baseinfoLocation.getIsLeaf());
                baseinfoLocationShelf.setIsValid(baseinfoLocation.getIsValid());
                baseinfoLocationShelf.setCanStore(baseinfoLocation.getCanStore());
                baseinfoLocationShelf.setContainerVol(baseinfoLocation.getContainerVol());
                baseinfoLocationShelf.setRegionNo(baseinfoLocation.getRegionNo());
                baseinfoLocationShelf.setPassageNo(baseinfoLocation.getPassageNo());
                baseinfoLocationShelf.setShelfLevelNo(baseinfoLocation.getShelfLevelNo());
                baseinfoLocationShelf.setBinPositionNo(baseinfoLocation.getBinPositionNo());
                //设置占用与否
                if (locationService.isLocationInUse(locationId)) {
                    baseinfoLocationShelf.setIsUsed("已占用");
                } else {
                    baseinfoLocationShelf.setIsUsed("未占用");
                }
                baseinfoLocationShelfs.add(baseinfoLocationShelf);
            }
            return (List<BaseinfoLocation>) (List<?>) baseinfoLocationShelfs;
        }
        return null;
    }
}
