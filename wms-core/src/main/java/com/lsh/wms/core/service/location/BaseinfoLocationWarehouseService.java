package com.lsh.wms.core.service.location;

import com.lsh.wms.core.dao.baseinfo.BaseinfoLocationWarehouseDao;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.baseinfo.BaseinfoLocationBin;
import com.lsh.wms.model.baseinfo.BaseinfoLocationWarehouse;
import com.lsh.wms.model.baseinfo.IBaseinfoLocaltionModel;
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
 * @Date 16/7/23 下午8:29
 */
@Component
@Transactional(readOnly = true)
public class BaseinfoLocationWarehouseService implements IStrategy {

    @Autowired
    private BaseinfoLocationWarehouseDao baseinfoLocationWarehouseDao;
    @Autowired
    private  LocationService locationService;
    @Autowired
    private  FatherToChildUtil fatherToChildUtil;
//    @Autowired
//    private LocationDetailModelFactory locationDetailModelFactory;
//    @Autowired
//    private LocationDetailServiceFactory locationDetailServiceFactory;
//
//    @PostConstruct
//    public void postConstruct(){
//        locationDetailModelFactory.register(LocationConstant.Warehouse,new BaseinfoLocationWarehouse());
//        //注册service
//        locationDetailServiceFactory.register(LocationConstant.Warehouse,this);
//    }


    @Transactional(readOnly = false)
    public void insert(IBaseinfoLocaltionModel iBaseinfoLocaltionModel) {
        baseinfoLocationWarehouseDao.insert((BaseinfoLocationWarehouse) iBaseinfoLocaltionModel);
    }

    @Transactional(readOnly = false)
    public void update(IBaseinfoLocaltionModel iBaseinfoLocaltionModel) {
        baseinfoLocationWarehouseDao.update((BaseinfoLocationWarehouse) iBaseinfoLocaltionModel);
    }

    public BaseinfoLocation getBaseinfoItemLocationModelById(Long id) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("locationId", id);
        BaseinfoLocation baseinfoLocation = locationService.getLocationListByType(mapQuery);
        BaseinfoLocationWarehouse baseinfoLocationWarehouse = null;
        List<BaseinfoLocationWarehouse> warehouseList = new ArrayList<BaseinfoLocationWarehouse>();
        //
        if (baseinfoLocation != null) {
            warehouseList = baseinfoLocationWarehouseDao.getBaseinfoLocationWarehouseList(mapQuery);
            baseinfoLocationWarehouse = warehouseList.get(0);
            //将父亲location的属性值拷贝给baseinfoLocationWarehouse
            //设置子类信息
            baseinfoLocationWarehouse.setLocationCode(baseinfoLocation.getLocationCode());
            baseinfoLocationWarehouse.setFatherId(baseinfoLocation.getFatherId());
            baseinfoLocationWarehouse.setType(baseinfoLocation.getType());
            baseinfoLocationWarehouse.setTypeName(baseinfoLocation.getTypeName());
            baseinfoLocationWarehouse.setIsLeaf(baseinfoLocation.getIsLeaf());
            baseinfoLocationWarehouse.setIsValid(baseinfoLocation.getIsValid());
            baseinfoLocationWarehouse.setCanStore(baseinfoLocation.getCanStore());
            baseinfoLocationWarehouse.setContainerVol(baseinfoLocation.getContainerVol());
            baseinfoLocationWarehouse.setRegionNo(baseinfoLocation.getRegionNo());
            baseinfoLocationWarehouse.setPassageNo(baseinfoLocation.getPassageNo());
            baseinfoLocationWarehouse.setShelfLevelNo(baseinfoLocation.getShelfLevelNo());
            baseinfoLocationWarehouse.setBinPositionNo(baseinfoLocation.getBinPositionNo());

            //设置占用与否
            if (locationService.isLocationInUse(id)) {
                baseinfoLocationWarehouse.setIsUsed("已占用");
            } else {
                baseinfoLocationWarehouse.setIsUsed("未占用");
            }
            return baseinfoLocationWarehouse;
        }
        return null;
    }

    public Integer countBaseinfoLocaltionModel(Map<String, Object> params) {
        return locationService.countLocation(params);
    }

    public List<BaseinfoLocation> getBaseinfoLocaltionModelList(Map<String, Object> params) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<BaseinfoLocation> baseinfoLocationList = locationService.getBaseinfoLocationList(params);
        List<BaseinfoLocationWarehouse> baseinfoLocationWarehouses = new ArrayList<BaseinfoLocationWarehouse>();
        BaseinfoLocationWarehouse baseinfoLocationWarehouse = null;
        List<BaseinfoLocationWarehouse> warehouseList = null;
        //循环父类list逐个拷贝到子类,并添加到子类list中
        if (baseinfoLocationList.size() > 0) {
            for (BaseinfoLocation baseinfoLocation:baseinfoLocationList){
                //根据父类id获取子类bin
                Long locationId = baseinfoLocation.getLocationId();
                //设置位置id
                params.put("locationId",locationId);
                warehouseList = baseinfoLocationWarehouseDao.getBaseinfoLocationWarehouseList(params);
                baseinfoLocationWarehouse = warehouseList.get(0);
                //设置子类信息
                baseinfoLocationWarehouse.setLocationCode(baseinfoLocation.getLocationCode());
                baseinfoLocationWarehouse.setFatherId(baseinfoLocation.getFatherId());
                baseinfoLocationWarehouse.setType(baseinfoLocation.getType());
                baseinfoLocationWarehouse.setTypeName(baseinfoLocation.getTypeName());
                baseinfoLocationWarehouse.setIsLeaf(baseinfoLocation.getIsLeaf());
                baseinfoLocationWarehouse.setIsValid(baseinfoLocation.getIsValid());
                baseinfoLocationWarehouse.setCanStore(baseinfoLocation.getCanStore());
                baseinfoLocationWarehouse.setContainerVol(baseinfoLocation.getContainerVol());
                baseinfoLocationWarehouse.setRegionNo(baseinfoLocation.getRegionNo());
                baseinfoLocationWarehouse.setPassageNo(baseinfoLocation.getPassageNo());
                baseinfoLocationWarehouse.setShelfLevelNo(baseinfoLocation.getShelfLevelNo());
                baseinfoLocationWarehouse.setBinPositionNo(baseinfoLocation.getBinPositionNo());
                //设置占用与否
                if (locationService.isLocationInUse(locationId)) {
                    baseinfoLocationWarehouse.setIsUsed("已占用");
                } else {
                    baseinfoLocationWarehouse.setIsUsed("未占用");
                }
                baseinfoLocationWarehouses.add(baseinfoLocationWarehouse);
            }
            return (List<BaseinfoLocation>) (List<?>) baseinfoLocationWarehouses;
        }
        return null;
    }
}
