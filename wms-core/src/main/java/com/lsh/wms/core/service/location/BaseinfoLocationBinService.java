package com.lsh.wms.core.service.location;

import com.lsh.wms.core.dao.baseinfo.BaseinfoLocationBinDao;
import com.lsh.wms.model.baseinfo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 16/7/23 下午6:50
 */
@Component
@Transactional(readOnly = true)
public class BaseinfoLocationBinService implements IStrategy {
    private static final Logger logger = LoggerFactory.getLogger(LocationService.class);

    /**
     * 获取级别的type,包含货架区20、阁楼区21、地堆区5、残存区6、集货区7、退货区8、残次区9
     */
    private static final long[] REGIONTYPE = {5, 6, 7, 8, 9, 20, 21};


    @Autowired
    private BaseinfoLocationBinDao baseinfoLocationBinDao;
    @Autowired
    private LocationService locationService;
    @Autowired
    private FatherToChildUtil fatherToChildUtil;
//    @Autowired
//    private LocationDetailModelFactory locationDetailModelFactory;
//    @Autowired
//    private LocationDetailServiceFactory locationDetailServiceFactory;
//
//    /**
//     * 将所有的bin的type注册到工厂中
//     */
//    @PostConstruct
//    public void postConstruct(){
//        //添加其他bin的type
//        locationDetailModelFactory.register(LocationConstant.Bin, new BaseinfoLocationBin());
//        locationDetailModelFactory.register(LocationConstant.Pinking,new BaseinfoLocationBin());
//        locationDetailModelFactory.register(LocationConstant.Stock_bin,new BaseinfoLocationBin());
//        locationDetailModelFactory.register(LocationConstant.Floor_bin, new BaseinfoLocationBin());
//        locationDetailModelFactory.register(LocationConstant.Temporary_bin,new BaseinfoLocationBin());
//        locationDetailModelFactory.register(LocationConstant.Collection_bin,new BaseinfoLocationBin());
//        locationDetailModelFactory.register(LocationConstant.Back_bin,new BaseinfoLocationBin());
//        locationDetailModelFactory.register(LocationConstant.Defective_bin,new BaseinfoLocationBin());
//        //添加各种type的service服务
//        locationDetailServiceFactory.register(LocationConstant.Bin,this);
//        locationDetailServiceFactory.register(LocationConstant.Pinking,this);
//        locationDetailServiceFactory.register(LocationConstant.Stock_bin,this);
//        locationDetailServiceFactory.register(LocationConstant.Floor_bin,this);
//        locationDetailServiceFactory.register(LocationConstant.Temporary_bin,this);
//        locationDetailServiceFactory.register(LocationConstant.Collection_bin,this);
//        locationDetailServiceFactory.register(LocationConstant.Back_bin,this);
//        locationDetailServiceFactory.register(LocationConstant.Defective_bin,this);
//    }

    /**
     * 传入BaseinfoLocationBin然后插入到主表中,并且插入到细节表中
     *
     * @param
     */
    @Transactional(readOnly = false)
    public void insert(IBaseinfoLocaltionModel iBaseinfoLocaltionModel) {
        baseinfoLocationBinDao.insert((BaseinfoLocationBin) iBaseinfoLocaltionModel);
    }

    @Transactional(readOnly = false)
    public void update(IBaseinfoLocaltionModel iBaseinfoLocaltionModel) {
        baseinfoLocationBinDao.insert((BaseinfoLocationBin) iBaseinfoLocaltionModel);
    }

    /**
     * 通过locationId查找主表BaseinfoLocaiton,然后利用类反射工具,将父类的值,传给子类
     *
     * @param id
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public BaseinfoLocation getBaseinfoItemLocationModelById(Long id) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("locationId", id);
        BaseinfoLocation baseinfoLocation = locationService.getLocationListByType(mapQuery);
        BaseinfoLocationBin baseinfoLocationBin = null;
        List<BaseinfoLocationBin> binList = null;
        if (baseinfoLocation != null) {
            binList = baseinfoLocationBinDao.getBaseinfoLocationBinList(mapQuery);
            baseinfoLocationBin = binList.get(0);
            //设置子类信息
            baseinfoLocationBin.setLocationCode(baseinfoLocation.getLocationCode());
            baseinfoLocationBin.setFatherId(baseinfoLocation.getFatherId());
            baseinfoLocationBin.setType(baseinfoLocation.getType());
            baseinfoLocationBin.setTypeName(baseinfoLocation.getTypeName());
            baseinfoLocationBin.setIsLeaf(baseinfoLocation.getIsLeaf());
            baseinfoLocationBin.setIsValid(baseinfoLocation.getIsValid());
            baseinfoLocationBin.setCanStore(baseinfoLocation.getCanStore());
            baseinfoLocationBin.setContainerVol(baseinfoLocation.getContainerVol());
            baseinfoLocationBin.setRegionNo(baseinfoLocation.getRegionNo());
            baseinfoLocationBin.setPassageNo(baseinfoLocation.getPassageNo());
            baseinfoLocationBin.setShelfLevelNo(baseinfoLocation.getShelfLevelNo());
            baseinfoLocationBin.setBinPositionNo(baseinfoLocation.getBinPositionNo());
            //设置占用与否
            if (locationService.isLocationInUse(id)) {
                baseinfoLocationBin.setIsUsed("已占用");
            } else {
                baseinfoLocationBin.setIsUsed("未占用");
            }
            String regionName = this.getRegionName(baseinfoLocationBin);
            baseinfoLocationBin.setRegionName(regionName);
            return baseinfoLocationBin;
        }
        return null;
    }

    /**
     * 因为是先查找父类的表,所以只要把父类的数目返回即可
     * 传的参数一定是父类中有的
     *
     * @param params
     * @return
     */
    public Integer countBaseinfoLocaltionModel(Map<String, Object> params) {
        return locationService.countLocation(params);
    }

    public List<BaseinfoLocation> getBaseinfoLocaltionModelList(Map<String, Object> params) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<BaseinfoLocation> baseinfoLocationList = locationService.getBaseinfoLocationList(params);
        List<BaseinfoLocationBin> baseinfoLocationBins = new ArrayList<BaseinfoLocationBin>();
        BaseinfoLocationBin baseinfoLocationBin = null;
        List<BaseinfoLocationBin> binList = null;
        //循环父类list逐个拷贝到子类,并添加到子类list中
        if (baseinfoLocationList.size() > 0) {
            for (BaseinfoLocation baseinfoLocation : baseinfoLocationList) {
                //根据父类id获取子类bin
                Long locationId = baseinfoLocation.getLocationId();
                params.put("locationId",locationId);
                binList = baseinfoLocationBinDao.getBaseinfoLocationBinList(params);
                baseinfoLocationBin = binList.get(0);
                //设置子类信息
                baseinfoLocationBin.setLocationCode(baseinfoLocation.getLocationCode());
                baseinfoLocationBin.setFatherId(baseinfoLocation.getFatherId());
                baseinfoLocationBin.setType(baseinfoLocation.getType());
                baseinfoLocationBin.setTypeName(baseinfoLocation.getTypeName());
                baseinfoLocationBin.setIsLeaf(baseinfoLocation.getIsLeaf());
                baseinfoLocationBin.setIsValid(baseinfoLocation.getIsValid());
                baseinfoLocationBin.setCanStore(baseinfoLocation.getCanStore());
                baseinfoLocationBin.setContainerVol(baseinfoLocation.getContainerVol());
                baseinfoLocationBin.setRegionNo(baseinfoLocation.getRegionNo());
                baseinfoLocationBin.setPassageNo(baseinfoLocation.getPassageNo());
                baseinfoLocationBin.setShelfLevelNo(baseinfoLocation.getShelfLevelNo());
                baseinfoLocationBin.setBinPositionNo(baseinfoLocation.getBinPositionNo());
                //设置占用与否
                if (locationService.isLocationInUse(locationId)) {
                    baseinfoLocationBin.setIsUsed("已占用");
                } else {
                    baseinfoLocationBin.setIsUsed("未占用");
                }
                String regionName = this.getRegionName(baseinfoLocationBin);
                baseinfoLocationBin.setRegionName(regionName);
                baseinfoLocationBins.add(baseinfoLocationBin);
            }
            return (List<BaseinfoLocation>) (List<?>) baseinfoLocationBins;
        }
        return null;
    }

    /**
     * 根据现有的位置,获取区域的位置,一直找到区的一层
     * @param baseinfoLocation
     * @return
     */
    //获取区域的name,拼接字符串
    public String getRegionName(BaseinfoLocation baseinfoLocation) {
        //先排序
        Arrays.sort(REGIONTYPE);
        String regionName = "";
        //获取父亲对象
        BaseinfoLocation fatherLocation = locationService.getFatherLocation(baseinfoLocation.getLocationId());
        Long fatherLocationType = fatherLocation.getType();
        //向上查找直到type属于货架区、阁楼区、暂存区、地堆区、退货区
        if (Arrays.binarySearch(REGIONTYPE, fatherLocationType) > 0) {
            String regionCode = fatherLocation.getLocationCode();
            String regionTypeName = fatherLocation.getTypeName();
            regionName = regionTypeName + " " + regionTypeName;
            return regionName;
        } else {
            this.getRegionName(fatherLocation);
        }
        return null;
    }
}
