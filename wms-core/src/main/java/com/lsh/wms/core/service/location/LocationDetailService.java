package com.lsh.wms.core.service.location;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.service.location.targetlist.*;
import com.lsh.wms.model.baseinfo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
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
    //获取list方法的注入
    @Autowired
    private TargetListFactory targetListFactory;
    @Autowired
    private AreaListService areaListService;
    @Autowired
    private DomainListService domainListService;
    @Autowired
    private PassageListService passageListService;
    @Autowired
    private ShelfListService shelfListService;
    @Autowired
    private ShelfRegionListService shelfRegionListService;
    @Autowired
    private BaseinfoLocationLevelService baseinfoLocationLevelService;

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
        //注入location的层级的服务
        locationDetailServiceFactory.register(LocationConstant.SHELF_LEVELS, baseinfoLocationLevelService);
        locationDetailServiceFactory.register(LocationConstant.LOFT_LEVELS, baseinfoLocationLevelService);
        //注入超市返仓区服务
        locationDetailServiceFactory.register(LocationConstant.MARKET_RETURN_AREA, baseinfoLocationRegionService);
        //注入CONSUME_AREA和SUPPLIER_AREA
        locationDetailServiceFactory.register(LocationConstant.CONSUME_AREA, baseinfoLocationRegionService);
        locationDetailServiceFactory.register(LocationConstant.SUPPLIER_AREA, baseinfoLocationRegionService);
        //注入拆零区、拆零货架、拆零层级、拆零存储一体货位
        locationDetailServiceFactory.register(LocationConstant.SPLIT_AREA, baseinfoLocationRegionService);
        locationDetailServiceFactory.register(LocationConstant.SPLIT_SHELF, baseinfoLocationShelfService);
        locationDetailServiceFactory.register(LocationConstant.SPLIT_SHELF_LEVEL, baseinfoLocationLevelService);
        locationDetailServiceFactory.register(LocationConstant.SPLIT_SHELF_BIN, baseinfoLocationBinService);

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
        //货架和阁楼层
        locationDetailModelFactory.register(LocationConstant.SHELF_LEVELS, new BaseinfoLocation());
        locationDetailModelFactory.register(LocationConstant.LOFT_LEVELS, new BaseinfoLocation());
        //注入超市返仓区
        locationDetailModelFactory.register(LocationConstant.MARKET_RETURN_AREA, new BaseinfoLocationRegion());
        //注入CONSUME_AREA和SUPPLIER_AREA
        locationDetailModelFactory.register(LocationConstant.CONSUME_AREA, new BaseinfoLocationRegion());
        locationDetailModelFactory.register(LocationConstant.SUPPLIER_AREA, new BaseinfoLocationRegion());
        //注入拆零区、拆零货架、拆零层级、拆零存储一体货位
        locationDetailModelFactory.register(LocationConstant.SPLIT_AREA, new BaseinfoLocationRegion());
        locationDetailModelFactory.register(LocationConstant.SPLIT_SHELF, new BaseinfoLocationShelf());
        locationDetailModelFactory.register(LocationConstant.SPLIT_SHELF_LEVEL, new BaseinfoLocation());
        locationDetailModelFactory.register(LocationConstant.SPLIT_SHELF_BIN, new BaseinfoLocationBin());
    }

    /**
     * 将所有的getList方法注入到工厂中
     */
    @PostConstruct
    public void postTargetListConstruct() {
        targetListFactory.register(LocationConstant.LIST_TYPE_AREA, areaListService);
        targetListFactory.register(LocationConstant.LIST_TYPE_DOMAIN, domainListService);
        targetListFactory.register(LocationConstant.LIST_TYPE_PASSAGE, passageListService);
        targetListFactory.register(LocationConstant.LIST_TYPE_SHELFREGION, shelfRegionListService);
        targetListFactory.register(LocationConstant.LIST_TYPE_SHELF, shelfListService);
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
        //如果插入的是仓库
        if (location.getFatherId() == -1L) {
            return;
        }
        //如果是货架个体,插入指定层的货架层
        // todo 如果单插入阁楼层的话,会出现插入两次主表的问题(货架和层一起插就不会),因为阁楼|货架层service注入了原来的locationService
        if (LocationConstant.SHELF.equals(iBaseinfoLocaltionModel.getType())) {
            this.insertShelflevelsByShelf(baseinfoLocation,iBaseinfoLocaltionModel,LocationConstant.SHELF_LEVELS);
            //将货架的叶子节点设置为0
            location.setIsLeaf(0);
            locationService.updateLocation(location);
        }
        //  如果是阁楼个体,插入指定层的阁楼层
        if (LocationConstant.LOFT.equals(iBaseinfoLocaltionModel.getType())) {
          this.insertShelflevelsByShelf(baseinfoLocation,iBaseinfoLocaltionModel,LocationConstant.SHELF_LEVELS);
            //将货架的叶子节点设置为0
            location.setIsLeaf(0);
            locationService.updateLocation(location);
        }
        if (LocationConstant.SPLIT_SHELF.equals(iBaseinfoLocaltionModel.getType())){
            this.insertShelflevelsByShelf(baseinfoLocation,iBaseinfoLocaltionModel,LocationConstant.SPLIT_SHELF_LEVEL);
            //将货架的叶子节点设置为0
            location.setIsLeaf(0);
            locationService.updateLocation(location);
        }
        //其他type处理方式
        BaseinfoLocation fatherLocation = locationService.getFatherLocation(location.getLocationId());
        fatherLocation.setIsLeaf(0);
        locationService.updateLocation(fatherLocation);
    }

    /**
     * 根据货架个体(阁楼货架、货架个体、拆零货架)联动插入货架层
     * @param baseinfoLocation
     * @param iBaseinfoLocaltionModel
     * @param type
     */
    public void insertShelflevelsByShelf(BaseinfoLocation baseinfoLocation, IBaseinfoLocaltionModel iBaseinfoLocaltionModel, Long type) {
        // 拿到指定的code,加入-i
        BaseinfoLocationShelf loft = new BaseinfoLocationShelf();
        ObjUtils.bean2bean(iBaseinfoLocaltionModel, loft);  //拷贝性质
        Long levels = loft.getLevel();
        int level = Integer.parseInt(levels.toString());
        String fatherCode = loft.getLocationCode();
        for (int i = 1; i <= level; i++) {
            String newCode = fatherCode + "-" + i;
            BaseinfoLocation levelLocation = new BaseinfoLocation();
            ObjUtils.bean2bean(baseinfoLocation, levelLocation);
            levelLocation.setFatherId(baseinfoLocation.getLocationId());  //  设置父亲的id
            levelLocation.setLocationCode(newCode); //code刷新,range不用管
            levelLocation.setType(type); //设置类型
            levelLocation.setShelfLevelNo(Long.parseLong(Integer.toString(i)));
            levelLocation.setTypeName(LocationConstant.LOCATION_TYPE_NAME.get(type));
            levelLocation.setClassification(3);
            locationService.insertLocation(levelLocation);
        }
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

    /**
     * 获取指定的全功能区、全货架、全货架区、全大区、全通道, 使用的话,请使用locationDeatilRPCService服务
     *
     * @param listType
     * @return
     * @throws BizCheckedException
     */
    public List<BaseinfoLocation> getTargetListByListType(Integer listType) throws BizCheckedException {
//        TargetListHandler listHandler = targetListFactory.getTargetListHandler(listType);
//        List<BaseinfoLocation> targetList = listHandler.getTargetLocaltionModelList();
        //根据type选择对应的type的service
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("type", listType);
        params.put("isvalid", 1);
        return locationService.getBaseinfoLocationList(params);
    }

    /**
     * 获取超市返仓区,里面的货主区分,通过BaseinfoLocationRegion类中的。通过货主owerid查返仓位置
     * Todo 前端的页面没有货主的概念
     *
     * @return
     */
    public List<BaseinfoLocationRegion> getMarketReturnList(Long ownerid) throws BizCheckedException {
        Map<String, Object> params = new HashMap<String, Object>();
        List<BaseinfoLocation> locationList = locationService.getTargetLocationListByType(LocationConstant.MARKET_RETURN_AREA);
        List<IBaseinfoLocaltionModel> iBaseinfoLocaltionModels = new ArrayList<IBaseinfoLocaltionModel>();
        IBaseinfoLocaltionModel marketLocation = null;
        for (BaseinfoLocation location : locationList) {
            marketLocation = this.getIBaseinfoLocaltionModelById(location.getLocationId());
            iBaseinfoLocaltionModels.add(marketLocation);
        }
        //过滤货主
        List<BaseinfoLocationRegion> regions = (List<BaseinfoLocationRegion>) (List<?>) iBaseinfoLocaltionModels;
        List<BaseinfoLocationRegion> targetReturn = new ArrayList<BaseinfoLocationRegion>();
        for (BaseinfoLocationRegion one : regions) {
            if (one.getOwnerid().equals(ownerid)) {
                targetReturn.add(one);
            }
        }
        if (targetReturn.size() > 0) {
            return targetReturn;
        } else {
            throw new BizCheckedException("2180005");
        }
    }

    /**
     * 删除细节表
     * 该方法只删除细节表
     * @param iBaseinfoLocaltionModel
     * @return
     */
    @Transactional(readOnly = false)
    public IBaseinfoLocaltionModel removeLocationDetail(IBaseinfoLocaltionModel iBaseinfoLocaltionModel){
        IStrategy istrategy = locationDetailServiceFactory.getIstrategy(iBaseinfoLocaltionModel.getType());
        istrategy.removeLocation(iBaseinfoLocaltionModel.getLocationId());
        return iBaseinfoLocaltionModel;
    }
}
