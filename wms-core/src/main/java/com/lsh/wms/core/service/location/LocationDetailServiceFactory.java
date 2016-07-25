package com.lsh.wms.core.service.location;

import com.lsh.wms.model.baseinfo.IBaseinfoLocaltionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * location的具体service选用的工厂方法,通过传入不同的参数类型,实例化不同的service
 * 生产不同的service
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 16/7/24 上午10:53
 */
@Component
public class LocationDetailServiceFactory {

    private static IStrategy iStrategy;

    //需要注入不同的service
    @Autowired
    private BaseinfoLocationService baseinfoLocationService;
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



    //生产service的方法,根据传入的model的类型不同
    public IStrategy createDetailServiceByModel(IBaseinfoLocaltionModel iBaseinfoLocaltionModel){

        //0基本的location
        if (iBaseinfoLocaltionModel.getClass().getName().equalsIgnoreCase("BaseinfoLocation")){
            iStrategy = baseinfoLocationService;
        }

        //1.货位
        if (iBaseinfoLocaltionModel.getClass().getName().equalsIgnoreCase("BaseinfoLocationBin")) {
            iStrategy = baseinfoLocationBinService;
        }
        //2.码头
        if (iBaseinfoLocaltionModel.getClass().getName().equalsIgnoreCase("BaseinfoLocationDock")){
            iStrategy = baseinfoLocationDockService;
        }
        //3.通道
        if (iBaseinfoLocaltionModel.getClass().getName().equalsIgnoreCase("BaseinfoLocationPassage")){
            iStrategy = baseinfoLocationPassageService;
        }
        //4.区域
        if (iBaseinfoLocaltionModel.getClass().getName().equalsIgnoreCase("BaseinfoLocationRegion")){
            iStrategy = baseinfoLocationRegionService;
        }
        //5.货架,阁楼
        if (iBaseinfoLocaltionModel.getClass().getName().equalsIgnoreCase("BaseinfoLocationShelf")){
            iStrategy = baseinfoLocationShelfService;
        }
        //6.仓库
        if (iBaseinfoLocaltionModel.getClass().getName().equalsIgnoreCase("BaseinfoLocationWarehouse")){
            iStrategy = baseinfoLocationWarehouseService;
        }
        return iStrategy;
    }
    //创建service通过前端传过来的type类型
    public IStrategy createDetailServiceByType(Integer type){
        //1.货位 货位
        if (type==11||type==12||type==13||type==14||type==15||type==16||type==17) {
            iStrategy = baseinfoLocationBinService;
        }
        //2.码头 type=10
        if (10==type){
            iStrategy = baseinfoLocationDockService;
        }
        //3.通道 type=18
        if (18==type){
            iStrategy = baseinfoLocationPassageService;
        }
        //4.区域(地堆5、暂存6、集货7、退货8、残次9)
        if (5==type||6==type||7==type||8==type||9==type){
            iStrategy = baseinfoLocationRegionService;
        }
        //5.货架,阁楼(货区4)
        if (type==4){
            iStrategy = baseinfoLocationShelfService;
        }
        //6.仓库
        if (type==1){
            iStrategy = baseinfoLocationWarehouseService;
        }
        return iStrategy;
    }

}
