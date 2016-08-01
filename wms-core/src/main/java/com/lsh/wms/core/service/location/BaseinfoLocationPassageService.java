package com.lsh.wms.core.service.location;

import com.lsh.wms.core.dao.baseinfo.BaseinfoLocationPassageDao;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.baseinfo.BaseinfoLocationPassage;
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
 * @Date 16/7/23 下午7:17
 */
@Component
@Transactional(readOnly = true)
public class BaseinfoLocationPassageService implements IStrategy {
    private static final Logger logger = LoggerFactory.getLogger(LocationService.class);

    @Autowired
    private BaseinfoLocationPassageDao baseinfoLocationPassageDao;
    @Autowired
    private LocationService locationService;


    @Transactional(readOnly = false)
    public void insert(IBaseinfoLocaltionModel iBaseinfoLocaltionModel) {
        baseinfoLocationPassageDao.insert((BaseinfoLocationPassage) iBaseinfoLocaltionModel);
    }

    @Transactional(readOnly = false)
    public void update(IBaseinfoLocaltionModel iBaseinfoLocaltionModel) {
        baseinfoLocationPassageDao.update((BaseinfoLocationPassage) iBaseinfoLocaltionModel);
    }

    public BaseinfoLocation getBaseinfoItemLocationModelById(Long id) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("locationId", id);
        BaseinfoLocation baseinfoLocation = locationService.getLocationListByType(mapQuery);
        BaseinfoLocationPassage baseinfoLocationPassage = null;
        List<BaseinfoLocationPassage> passageList = new ArrayList<BaseinfoLocationPassage>();
        //
        if (baseinfoLocation != null) {
            passageList = baseinfoLocationPassageDao.getBaseinfoLocationPassageList(mapQuery);
            baseinfoLocationPassage =  passageList.get(0);
            //将父亲location的属性值拷贝给baseinfoLocationPassage
            //设置子类信息
            baseinfoLocationPassage.setLocationCode(baseinfoLocation.getLocationCode());
            baseinfoLocationPassage.setFatherId(baseinfoLocation.getFatherId());
            baseinfoLocationPassage.setType(baseinfoLocation.getType());
            baseinfoLocationPassage.setTypeName(baseinfoLocation.getTypeName());
            baseinfoLocationPassage.setIsLeaf(baseinfoLocation.getIsLeaf());
            baseinfoLocationPassage.setIsValid(baseinfoLocation.getIsValid());
            baseinfoLocationPassage.setCanStore(baseinfoLocation.getCanStore());
            baseinfoLocationPassage.setContainerVol(baseinfoLocation.getContainerVol());
            baseinfoLocationPassage.setRegionNo(baseinfoLocation.getRegionNo());
            baseinfoLocationPassage.setPassageNo(baseinfoLocation.getPassageNo());
            baseinfoLocationPassage.setShelfLevelNo(baseinfoLocation.getShelfLevelNo());
            baseinfoLocationPassage.setBinPositionNo(baseinfoLocation.getBinPositionNo());
            //设置占用与否
            if (locationService.isLocationInUse(id)) {
                baseinfoLocationPassage.setIsUsed("已占用");
            } else {
                baseinfoLocationPassage.setIsUsed("未占用");
            }
            return baseinfoLocationPassage;
        }
        return null;
    }


    /**
     * 因为是先查找父类的表,所以只要把父类的数目返回即可
     * 传的参数一定是父类中有的
     * @param params
     * @return
     */
    public Integer countBaseinfoLocaltionModel(Map<String, Object> params) {
        return locationService.countLocation(params);
    }

    public List<BaseinfoLocation> getBaseinfoLocaltionModelList(Map<String, Object> params) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<BaseinfoLocation> baseinfoLocationList = locationService.getBaseinfoLocationList(params);
        List<BaseinfoLocationPassage> baseinfoLocationPassages = new ArrayList<BaseinfoLocationPassage>();
        BaseinfoLocationPassage baseinfoLocationPassage = null;
        List<BaseinfoLocationPassage> passageList = null;
        //循环父类list逐个拷贝到子类,并添加到子类list中
        if (baseinfoLocationList.size() > 0) {
            for (BaseinfoLocation baseinfoLocation:baseinfoLocationList){
                //根据父类id获取子类bin
                Long locationId = baseinfoLocation.getLocationId();
                //设置locationId
                params.put("locationId",locationId);
                passageList = baseinfoLocationPassageDao.getBaseinfoLocationPassageList(params);
                baseinfoLocationPassage = passageList.get(0);
                //设置子类信息
                baseinfoLocationPassage.setLocationCode(baseinfoLocation.getLocationCode());
                baseinfoLocationPassage.setFatherId(baseinfoLocation.getFatherId());
                baseinfoLocationPassage.setType(baseinfoLocation.getType());
                baseinfoLocationPassage.setTypeName(baseinfoLocation.getTypeName());
                baseinfoLocationPassage.setIsLeaf(baseinfoLocation.getIsLeaf());
                baseinfoLocationPassage.setIsValid(baseinfoLocation.getIsValid());
                baseinfoLocationPassage.setCanStore(baseinfoLocation.getCanStore());
                baseinfoLocationPassage.setContainerVol(baseinfoLocation.getContainerVol());
                baseinfoLocationPassage.setRegionNo(baseinfoLocation.getRegionNo());
                baseinfoLocationPassage.setPassageNo(baseinfoLocation.getPassageNo());
                baseinfoLocationPassage.setShelfLevelNo(baseinfoLocation.getShelfLevelNo());
                baseinfoLocationPassage.setBinPositionNo(baseinfoLocation.getBinPositionNo());

                //设置占用与否
                if (locationService.isLocationInUse(locationId)) {
                    baseinfoLocationPassage.setIsUsed("已占用");
                } else {
                    baseinfoLocationPassage.setIsUsed("未占用");
                }
                baseinfoLocationPassages.add(baseinfoLocationPassage);
            }
            return (List<BaseinfoLocation>) (List<?>) baseinfoLocationPassages;
        }
        return null;
    }
}
