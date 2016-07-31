package com.lsh.wms.core.service.location;

import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.baseinfo.BaseinfoLocationBin;
import com.lsh.wms.model.baseinfo.IBaseinfoLocaltionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
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
    /**
     * 建立货位的枚举
     */
    //因为货位的类型有多样的,如果需要查询所有的bin,需要分别按现有的bin类型查找,然后将查到的list追加在一起仅用于(list页面)
    public static final List<Integer> BINTYPELIST = Arrays.asList(12, 13, 14, 15, 16, 17, 18);

    /**
     * 货区的编号
     */
    public static final List<Integer> REGIONTYPELIST = Arrays.asList(3, 4, 5, 6, 7, 8, 9);

    /**
     * 所有bin的type=11
     */
    private static final int BINTYPE = 11;

    /**
     * 所有的区域的父类的type=2
     */
    private static final int REGIONTYPE = 2;


    @Autowired
    private LocationDetailServiceFactory locationDetailServiceFactory;
    @Autowired
    private LocationService locationService;

    @Autowired
    private LocationDetailModelFactory locationDetailModelFactory;
    @Autowired
    private BaseinfoLocationBinService baseinfoLocationBinService;
    @Autowired
    private BaseinfoLocationDockService baseinfoLocationDockService;
    @Autowired
    private BaseinfoLocationPassageService baseinfoLocationPassageService;
    @Autowired
    private BaseinfoLocationRegionService baseinfoLocationRegionService;
    @Autowired
    private BaseinfoLocationShelfService baseinfoLocationShelfService;
    @Autowired
    private BaseinfoLocationWarehouseService baseinfoLocationWarehouseService;


    //构造之后实例化之前,完成service注册
    /**
     * 将所有的Service注册到工厂中
     */
    @PostConstruct
    public void postConstruct(){
        //添加各种bin的service服务
        locationDetailServiceFactory.register(LocationConstant.Bin,baseinfoLocationBinService);
        locationDetailServiceFactory.register(LocationConstant.Pinking,baseinfoLocationBinService);
        locationDetailServiceFactory.register(LocationConstant.Stock_bin,baseinfoLocationBinService);
        locationDetailServiceFactory.register(LocationConstant.Floor_bin,baseinfoLocationBinService);
        locationDetailServiceFactory.register(LocationConstant.Temporary_bin,baseinfoLocationBinService);
        locationDetailServiceFactory.register(LocationConstant.Collection_bin,baseinfoLocationBinService);
        locationDetailServiceFactory.register(LocationConstant.Back_bin,baseinfoLocationBinService);
        locationDetailServiceFactory.register(LocationConstant.Defective_bin,baseinfoLocationBinService);
        //注入Dock
        locationDetailServiceFactory.register(LocationConstant.Dock_area,baseinfoLocationDockService);
        //注入passage
        locationDetailServiceFactory.register(LocationConstant.Passage,baseinfoLocationPassageService);
        //注入区域
        locationDetailServiceFactory.register(LocationConstant.Region_area,baseinfoLocationRegionService);
        locationDetailServiceFactory.register(LocationConstant.InventoryLost,baseinfoLocationRegionService);
        locationDetailServiceFactory.register(LocationConstant.Goods_area,baseinfoLocationRegionService);
        locationDetailServiceFactory.register(LocationConstant.Floor,baseinfoLocationRegionService);
        locationDetailServiceFactory.register(LocationConstant.Temporary,baseinfoLocationRegionService);
        locationDetailServiceFactory.register(LocationConstant.Collection_area,baseinfoLocationRegionService);
        locationDetailServiceFactory.register(LocationConstant.Back_area,baseinfoLocationRegionService);
        locationDetailServiceFactory.register(LocationConstant.Defective_area,baseinfoLocationRegionService);
        //注入货架和阁楼
        locationDetailServiceFactory.register(LocationConstant.Shelf,baseinfoLocationShelfService);
        locationDetailServiceFactory.register(LocationConstant.Loft,baseinfoLocationShelfService);
        //注入仓库
        locationDetailServiceFactory.register(LocationConstant.Warehouse,baseinfoLocationWarehouseService);
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
        //根据model选择service
        IStrategy iStrategy = locationDetailServiceFactory.getIstrategy(iBaseinfoLocaltionModel.getType());
        iStrategy.insert(iBaseinfoLocaltionModel);
        //主表插入
        locationService.insertLocation(iBaseinfoLocaltionModel);
    }

    /**
     * location的主表和细节表一起更新
     *
     * @param iBaseinfoLocaltionModel location对象
     */
    @Transactional(readOnly = false)
    public void update(IBaseinfoLocaltionModel iBaseinfoLocaltionModel) {
        //校验
        if (null == iBaseinfoLocaltionModel) {
            throw new RuntimeException("更新的Location对象为空");
        }
        IStrategy iStrategy = locationDetailServiceFactory.getIstrategy(iBaseinfoLocaltionModel.getType());
        iStrategy.update(iBaseinfoLocaltionModel);
        //主表更新
        locationService.updateLocation(iBaseinfoLocaltionModel);
    }

    /**
     * location的detail的查询
     *
     * @param locationId 位置的
     * @param type       位置类型Integer
     * @return
     */
    public BaseinfoLocation getIBaseinfoLocaltionModelByIdAndType(Long locationId, Integer type) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        IStrategy iStrategy = locationDetailServiceFactory.getIstrategy(Long.valueOf(type.toString()));
        BaseinfoLocation baseinfoLocation = iStrategy.getBaseinfoItemLocationModelById(locationId);
        return baseinfoLocation;
    }

    /**
     * 根据前端map返回Location的detail信息
     *
     * @param params 传入的是getList的map参数集合
     * @return locationDetail的model
     * @throws NoSuchMethodException     找不到方法,源自本包内的FatherToChildUtil类
     * @throws IllegalAccessException    源自本包内的FatherToChildUtil类
     * @throws InvocationTargetException 源自本包内的FatherToChildUtil类
     */
    public List<BaseinfoLocation> getIBaseinfoLocaltionModelListByType(Map<String, Object> params) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Integer typeImp = (Integer) params.get("type");
        Long type = Long.valueOf(typeImp.toString());

        IStrategy strategy = locationDetailServiceFactory.getIstrategy(type);
        List<BaseinfoLocation> locationList = new ArrayList<BaseinfoLocation>();
        //如果是传过来的type是11显示所有的货位,则需要将按type的结果集list不断追加所有按各货位的type查出来的list
        if (BINTYPE == type) {
            for (Integer binType : BINTYPELIST) {
                params.put("type", binType);
                List<BaseinfoLocation> tempList = strategy.getBaseinfoLocaltionModelList(params);
                locationList.addAll(tempList);
            }
            return locationList;
        } else if (REGIONTYPE == type) {
            for (Integer regionType : REGIONTYPELIST) {
                params.put("type", regionType);
                List<BaseinfoLocation> tempList = strategy.getBaseinfoLocaltionModelList(params);
                locationList.addAll(tempList);
            }
            return locationList;
        } else {
            params.put("type", type);
            return strategy.getBaseinfoLocaltionModelList(params);
        }
    }

    /**
     * locationDetail的计数
     *
     * @param params
     * @return
     */
    public Integer countLocationDetail(Map<String, Object> params) {
        Integer typeImp = (Integer) params.get("type");
        Long type = Long.valueOf(typeImp.toString());
        IStrategy iStrategy = locationDetailServiceFactory.getIstrategy(type);

        return iStrategy.countBaseinfoLocaltionModel(params);
    }


}
