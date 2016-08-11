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
        locationDetailServiceFactory.register(LocationConstant.WAREHOUSE, baseinfoLocationWarehouseService);
        //注入区域
        locationDetailServiceFactory.register(LocationConstant.REGION_AREA, baseinfoLocationRegionService);
        //注入passage
        locationDetailServiceFactory.register(LocationConstant.PASSAGE, baseinfoLocationPassageService);
        //注入货架区和阁楼区
        locationDetailServiceFactory.register(LocationConstant.SHELFS, baseinfoLocationRegionService);
        locationDetailServiceFactory.register(LocationConstant.LOFTS, baseinfoLocationRegionService);
        //注入区域
        locationDetailServiceFactory.register(LocationConstant.INVENTORYLOST, baseinfoLocationRegionService);
        locationDetailServiceFactory.register(LocationConstant.FLOOR, baseinfoLocationRegionService);
        locationDetailServiceFactory.register(LocationConstant.TEMPORARY, baseinfoLocationRegionService);
        locationDetailServiceFactory.register(LocationConstant.COLLECTION_AREA, baseinfoLocationRegionService);
        locationDetailServiceFactory.register(LocationConstant.BACK_AREA, baseinfoLocationRegionService);
        locationDetailServiceFactory.register(LocationConstant.DEFECTIVE_AREA, baseinfoLocationRegionService);
        //货架和阁楼
        locationDetailServiceFactory.register(LocationConstant.SHELF, baseinfoLocationShelfService);
        locationDetailServiceFactory.register(LocationConstant.LOFT, baseinfoLocationShelfService);
        //注入码头
        locationDetailServiceFactory.register(LocationConstant.DOCK_AREA, baseinfoLocationDockService);
        //货位
        locationDetailServiceFactory.register(LocationConstant.BIN, baseinfoLocationBinService);
        //货架和阁楼的货位
        locationDetailServiceFactory.register(LocationConstant.SHELF_PICKING_BIN, baseinfoLocationBinService);
        locationDetailServiceFactory.register(LocationConstant.SHELF_STORE_BIN, baseinfoLocationBinService);
        locationDetailServiceFactory.register(LocationConstant.LOFT_PICKING_BIN, baseinfoLocationBinService);
        locationDetailServiceFactory.register(LocationConstant.LOFT_STORE_BIN, baseinfoLocationBinService);


        //添加各种功能bin的service服务
        locationDetailServiceFactory.register(LocationConstant.FLOOR_BIN, baseinfoLocationBinService);
        locationDetailServiceFactory.register(LocationConstant.TEMPORARY_BIN, baseinfoLocationBinService);
        locationDetailServiceFactory.register(LocationConstant.COLLECTION_BIN, baseinfoLocationBinService);
        locationDetailServiceFactory.register(LocationConstant.BACK_BIN, baseinfoLocationBinService);
        locationDetailServiceFactory.register(LocationConstant.DEFECTIVE_BIN, baseinfoLocationBinService);
    }

    /**
     * 将所有的bin的type注册到工厂中
     */
    @PostConstruct
    public void postBinConstruct() {
        //仓库
        locationDetailModelFactory.register(LocationConstant.WAREHOUSE, new BaseinfoLocationWarehouse());
        //区域
        locationDetailModelFactory.register(LocationConstant.REGION_AREA, new BaseinfoLocationRegion());
        //注入过道
        locationDetailModelFactory.register(LocationConstant.PASSAGE, new BaseinfoLocationPassage());

        //注入阁楼区和货架区
        locationDetailModelFactory.register(LocationConstant.SHELFS, new BaseinfoLocationRegion());
        locationDetailModelFactory.register(LocationConstant.LOFTS, new BaseinfoLocationRegion());
        //注入区域
        locationDetailModelFactory.register(LocationConstant.INVENTORYLOST, new BaseinfoLocationRegion());
        locationDetailModelFactory.register(LocationConstant.FLOOR, new BaseinfoLocationRegion());
        locationDetailModelFactory.register(LocationConstant.TEMPORARY, new BaseinfoLocationRegion());
        locationDetailModelFactory.register(LocationConstant.COLLECTION_AREA, new BaseinfoLocationRegion());
        locationDetailModelFactory.register(LocationConstant.BACK_AREA, new BaseinfoLocationRegion());
        locationDetailModelFactory.register(LocationConstant.DEFECTIVE_AREA, new BaseinfoLocationRegion());
        //货架和阁楼
        locationDetailModelFactory.register(LocationConstant.SHELF, new BaseinfoLocationShelf());
        locationDetailModelFactory.register(LocationConstant.LOFT, new BaseinfoLocationShelf());
        //注入码头
        locationDetailModelFactory.register(LocationConstant.DOCK_AREA, new BaseinfoLocationDock());
        //货位
        locationDetailModelFactory.register(LocationConstant.BIN, new BaseinfoLocationBin());
        //货架和阁楼的货位
        locationDetailModelFactory.register(LocationConstant.SHELF_PICKING_BIN, new BaseinfoLocationBin());
        locationDetailModelFactory.register(LocationConstant.SHELF_STORE_BIN, new BaseinfoLocationBin());
        locationDetailModelFactory.register(LocationConstant.LOFT_PICKING_BIN, new BaseinfoLocationBin());
        locationDetailModelFactory.register(LocationConstant.LOFT_STORE_BIN, new BaseinfoLocationBin());
        //功能bin
        locationDetailModelFactory.register(LocationConstant.FLOOR_BIN, new BaseinfoLocationBin());
        locationDetailModelFactory.register(LocationConstant.TEMPORARY_BIN, new BaseinfoLocationBin());
        locationDetailModelFactory.register(LocationConstant.COLLECTION_BIN, new BaseinfoLocationBin());
        locationDetailModelFactory.register(LocationConstant.BACK_BIN, new BaseinfoLocationBin());
        locationDetailModelFactory.register(LocationConstant.DEFECTIVE_BIN, new BaseinfoLocationBin());
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

        //先插入主表(并获得主表的location)
        BaseinfoLocation baseinfoLocation = locationService.insertLocation(location);

        //拷贝插入过主表后的location数据(时间和id)
        iBaseinfoLocaltionModel.setLocationId(baseinfoLocation.getLocationId());
        iBaseinfoLocaltionModel.setCreatedAt(baseinfoLocation.getCreatedAt());
        iBaseinfoLocaltionModel.setUpdatedAt(baseinfoLocation.getUpdatedAt());
        //根据model选择service
        IStrategy iStrategy = locationDetailServiceFactory.getIstrategy(iBaseinfoLocaltionModel.getType());
        iStrategy.insert(iBaseinfoLocaltionModel);
        //将father的叶子节点变为0
        BaseinfoLocation fatherLocation = locationService.getFatherLocation(location.getLocationId());
        fatherLocation.setIsLeaf(0);
        locationService.updateLocation(fatherLocation);
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
     * location中dock的筛选条件计数
     *
     * @param params
     * @return
     */
    public Integer countDockList(Map<String, Object> params) {
        return locationService.countDockList(params);
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
