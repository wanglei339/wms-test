package com.lsh.wms.api.service.location;

import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.baseinfo.IBaseinfoLocaltionModel;

import java.util.List;
import java.util.Map;


/**
 * Created by fengkun on 16/7/11.
 */
public interface ILocationRpcService {
    public BaseinfoLocation getLocation(Long locationId);
    public List<BaseinfoLocation> getStoreLocations(Long locationId);
    public List<BaseinfoLocation> getNextLevelLocations(Long locationId);
    public BaseinfoLocation getFatherLocation(Long locationId);
    public BaseinfoLocation getFatherByType(Long locationId, String type);

    public boolean canStore(Long locationId);

    public BaseinfoLocation insertLocation(BaseinfoLocation location);
    public BaseinfoLocation updateLocation(BaseinfoLocation location);
    public BaseinfoLocation assignTemporary();
    public BaseinfoLocation assignFloor();

    //分配退货
    public BaseinfoLocation getBackLocation();
    //分配残次
    public BaseinfoLocation getDefectiveLocation();

    /**
     * 获取全区域,为建货架(阁楼、货架)和货位服务
     * @return
     */
    public List<BaseinfoLocation> getAllRegion();

    /**
     * 获取全区域的所有货架 为建库位服务
     * @return
     */
    public List<BaseinfoLocation> getAllShelfs();
    //获取所有仓库下的所有货位
    public  List<BaseinfoLocation> getAllBin();

    /**
     * 获取所有的拣货位
     * @return
     */
    public List<BaseinfoLocation> getColletionBins();

    /**
     * 上锁
     * @param locationId
     * @return
     */
    public BaseinfoLocation lockLocation(Long locationId);

    /**
     * 解锁
     * @param locationId
     * @return
     */
    public BaseinfoLocation unlockLocation(Long locationId);


}
