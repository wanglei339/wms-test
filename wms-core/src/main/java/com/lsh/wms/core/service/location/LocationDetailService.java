package com.lsh.wms.core.service.location;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.model.baseinfo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

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
    @Autowired
    private BaseinfoLocationBinService baseinfoLocationBinService;
    @Autowired
    private BaseinfoLocationDockService baseinfoLocationDockService;
    @Autowired
    private BaseinfoLocationPassageService baseinfoLocationPassageService;
    @Autowired
    private BaseinfoLocationRegionService baseinfoLocationRegionService;
    @Autowired
    private BaseinfoLocationWarehouseService baseinfoLocationWarehouseService;
    @Autowired
    private BaseinfoLocationShelfService baseinfoLocationShelfService;
    @Autowired
    private LocationDetailModelFactory locationDetailModelFactory;


    /**
     * 将所有的Service注册到工厂中
     */
    @PostConstruct
    public void postConstruct() {
        //注入仓库
        locationDetailServiceFactory.register(LocationConstant.Warehouse, baseinfoLocationWarehouseService);
        //注入区域
        locationDetailServiceFactory.register(LocationConstant.Region_area, baseinfoLocationRegionService);
        //注入passage
        locationDetailServiceFactory.register(LocationConstant.Passage, baseinfoLocationPassageService);
        //注入货架区和阁楼区
        locationDetailServiceFactory.register(LocationConstant.Shelfs, baseinfoLocationRegionService);
        locationDetailServiceFactory.register(LocationConstant.Lofts, baseinfoLocationRegionService);
        //注入区域
        locationDetailServiceFactory.register(LocationConstant.InventoryLost, baseinfoLocationRegionService);
        locationDetailServiceFactory.register(LocationConstant.Floor, baseinfoLocationRegionService);
        locationDetailServiceFactory.register(LocationConstant.Temporary, baseinfoLocationRegionService);
        locationDetailServiceFactory.register(LocationConstant.Collection_area, baseinfoLocationRegionService);
        locationDetailServiceFactory.register(LocationConstant.Back_area, baseinfoLocationRegionService);
        locationDetailServiceFactory.register(LocationConstant.Defective_area, baseinfoLocationRegionService);
        //货架和阁楼
        locationDetailServiceFactory.register(LocationConstant.Shelf, baseinfoLocationShelfService);
        locationDetailServiceFactory.register(LocationConstant.Loft, baseinfoLocationShelfService);
        //注入码头
        locationDetailServiceFactory.register(LocationConstant.Dock_area, baseinfoLocationDockService);
        //货位
        locationDetailServiceFactory.register(LocationConstant.Bin, baseinfoLocationBinService);
        //货架和阁楼的货位
        locationDetailServiceFactory.register(LocationConstant.Shelf_collection_bin, baseinfoLocationBinService);
        locationDetailServiceFactory.register(LocationConstant.Shelf_store_bin, baseinfoLocationBinService);
        locationDetailServiceFactory.register(LocationConstant.Loft_collection_bin, baseinfoLocationBinService);
        locationDetailServiceFactory.register(LocationConstant.Loft_store_bin, baseinfoLocationBinService);


        //添加各种功能bin的service服务
        locationDetailServiceFactory.register(LocationConstant.Floor_bin, baseinfoLocationBinService);
        locationDetailServiceFactory.register(LocationConstant.Temporary_bin, baseinfoLocationBinService);
        locationDetailServiceFactory.register(LocationConstant.Collection_bin, baseinfoLocationBinService);
        locationDetailServiceFactory.register(LocationConstant.Back_bin, baseinfoLocationBinService);
        locationDetailServiceFactory.register(LocationConstant.Defective_bin, baseinfoLocationBinService);
    }

    /**
     * 将所有的bin的type注册到工厂中
     */
    @PostConstruct
    public void postBinConstruct() {
        //仓库
        locationDetailModelFactory.register(LocationConstant.Warehouse, new BaseinfoLocationWarehouse());
        //区域
        locationDetailModelFactory.register(LocationConstant.Region_area, new BaseinfoLocationRegion());
        //注入过道
        locationDetailModelFactory.register(LocationConstant.Passage, new BaseinfoLocationPassage());

        //注入阁楼区和货架区
        locationDetailModelFactory.register(LocationConstant.Shelfs, new BaseinfoLocationRegion());
        locationDetailModelFactory.register(LocationConstant.Lofts, new BaseinfoLocationRegion());
        //注入区域
        locationDetailModelFactory.register(LocationConstant.InventoryLost, new BaseinfoLocationRegion());
        locationDetailModelFactory.register(LocationConstant.Floor, new BaseinfoLocationRegion());
        locationDetailModelFactory.register(LocationConstant.Temporary, new BaseinfoLocationRegion());
        locationDetailModelFactory.register(LocationConstant.Collection_area, new BaseinfoLocationRegion());
        locationDetailModelFactory.register(LocationConstant.Back_area, new BaseinfoLocationRegion());
        locationDetailModelFactory.register(LocationConstant.Defective_area, new BaseinfoLocationRegion());
        //货架和阁楼
        locationDetailModelFactory.register(LocationConstant.Shelf, new BaseinfoLocationShelf());
        locationDetailModelFactory.register(LocationConstant.Loft, new BaseinfoLocationShelf());
        //注入码头
        locationDetailModelFactory.register(LocationConstant.Dock_area, new BaseinfoLocationDock());
        //货位
        locationDetailModelFactory.register(LocationConstant.Bin, new BaseinfoLocationBin());
        //货架和阁楼的货位
        locationDetailModelFactory.register(LocationConstant.Shelf_collection_bin, new BaseinfoLocationBin());
        locationDetailModelFactory.register(LocationConstant.Shelf_store_bin, new BaseinfoLocationBin());
        locationDetailModelFactory.register(LocationConstant.Loft_collection_bin, new BaseinfoLocationBin());
        locationDetailModelFactory.register(LocationConstant.Loft_store_bin, new BaseinfoLocationBin());
        //功能bin
        locationDetailModelFactory.register(LocationConstant.Floor_bin, new BaseinfoLocationBin());
        locationDetailModelFactory.register(LocationConstant.Temporary_bin, new BaseinfoLocationBin());
        locationDetailModelFactory.register(LocationConstant.Collection_bin, new BaseinfoLocationBin());
        locationDetailModelFactory.register(LocationConstant.Back_bin, new BaseinfoLocationBin());
        locationDetailModelFactory.register(LocationConstant.Defective_bin, new BaseinfoLocationBin());
    }


    /**
     * Location的细节表插入
     * location的主表也插入
     *
     * @param iBaseinfoLocaltionModel location的父类对象
     */
    @Transactional(readOnly = false)
    public void insert(IBaseinfoLocaltionModel iBaseinfoLocaltionModel) {
        //校验
        if (null == iBaseinfoLocaltionModel) {
            throw new RuntimeException("插入和Location对象为空");
        }
        //转化成父类,插入
        BaseinfoLocation location = new BaseinfoLocation();
        ObjUtils.bean2bean(iBaseinfoLocaltionModel, location);

        //先插入父亲
        locationService.insertLocation(location);
        //根据model选择service
        IStrategy iStrategy = locationDetailServiceFactory.getIstrategy(iBaseinfoLocaltionModel.getType());
        iStrategy.insert(iBaseinfoLocaltionModel);
    }

    /**
     * location的主表和细节表一起更新
     *
     * @param iBaseinfoLocaltionModel location对象
     */
    @Transactional(readOnly = false)
    public void update(IBaseinfoLocaltionModel iBaseinfoLocaltionModel) {
        //主表更新
        BaseinfoLocation location = (BaseinfoLocation) iBaseinfoLocaltionModel;
        locationService.updateLocation(location);
        //子表更新
        IStrategy iStrategy = locationDetailServiceFactory.getIstrategy(iBaseinfoLocaltionModel.getType());
        iStrategy.update(iBaseinfoLocaltionModel);
    }

    /**
     * location的detail的查询
     * 先查父亲,再查子类
     *
     * @param locationId 位置的
     * @return
     */
    public IBaseinfoLocaltionModel getIBaseinfoLocaltionModelById(Long locationId) throws BizCheckedException {

        BaseinfoLocation baseinfoLocation = locationService.getLocation(locationId);
        if (baseinfoLocation == null) {
            throw new BizCheckedException("2180001");
        }
        IStrategy iStrategy = locationDetailServiceFactory.getIstrategy(Long.valueOf(baseinfoLocation.getType()));
        IBaseinfoLocaltionModel iBaseinfoLocaltionModel = iStrategy.getBaseinfoItemLocationModelById(locationId);
        ObjUtils.bean2bean(baseinfoLocation, iBaseinfoLocaltionModel);
        return iBaseinfoLocaltionModel;
    }

    /**
     * 根据前端map返回Location的detail信息
     *
     * @param params 传入的是getList的map参数集合
     * @return locationDetail的model
     */
    public List<BaseinfoLocation> getIBaseinfoLocaltionModelListByType(Map<String, Object> params) {
        ///////////////////////////////////////////
        //如果传入的参数只有locationId,那么,先查主表,再查子表,此处先查主表
        //1.先查主表
        List<BaseinfoLocation> baseinfoLocationList = locationService.getBaseinfoLocationList(params);
        if (baseinfoLocationList.size() > 0) {
            List<BaseinfoLocation> subList = new ArrayList<BaseinfoLocation>();
            //从结果集中去子类的表中去查,并处理结果集
            for (BaseinfoLocation location : baseinfoLocationList) {
                IStrategy istrategy = locationDetailServiceFactory.getIstrategy(location.getType());
                //就是子
                BaseinfoLocation son = istrategy.getBaseinfoItemLocationModelById(location.getLocationId());
                //拷贝主表的信息
                ObjUtils.bean2bean(location, son);
                subList.add(son);
            }
            return subList;
        }
        return null;
    }

    /**
     * locationDetail的计数
     *
     * @param params
     * @return
     */
    public Integer countLocationDetail(Map<String, Object> params) {
        return locationService.countLocation(params);
    }


    /**
     * 获取码头的指定条件的location集合
     *
     * @param params
     * @return
     */
    public List<BaseinfoLocation> getDockListByType(Map<String, Object> params) {
        ///////////////////////////////////////////
        //如果传入的参数只有locationId,那么,先查主表,再查子表,此处先查主表
        //1.先查主表
        List<BaseinfoLocation> locationList = locationService.getDockList(params);
        if (locationList.size() > 0) {
            List<BaseinfoLocation> subList = new ArrayList<BaseinfoLocation>();
            //从结果集中去子类的表中去查,并处理结果集
            for (BaseinfoLocation location : locationList) {
                IStrategy istrategy = locationDetailServiceFactory.getIstrategy(location.getType());
                //就是子
                BaseinfoLocation son = istrategy.getBaseinfoItemLocationModelById(location.getLocationId());
                //拷贝主表的信息
                ObjUtils.bean2bean(location, son);
                subList.add(son);
            }
            return subList;
        }
        return null;
    }

}
