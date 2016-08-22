package com.lsh.wms.api.service.location;

import com.lsh.wms.model.baseinfo.BaseinfoLocation;

import java.util.Map;

/**
 * Created by fengkun on 16/7/11.
 */
public interface ILocationRestService {
    public String getLocation(Long locationId);
    public String getStoreLocationIds(Long locationId);
    public String getFatherByType(Long locationId, Long type);
    public String getFatherArea(Long locationId);
    public String getWarehouseLocationId();
    public String getInventoryLostLocationId();
    public String insertLocation();
    public String updateLocation();
    public String countBaseinfoLocation(Map<String, Object> params);
    public String searchList(Map<String, Object> params);
    public String getTemp(Long type);
    //仓库找货区
    public String getRegionByWareHouseId();
    //货区找货架
    public String getShelfByRegionId(Long locationId);
    //货架找货位
    public String getBinByShelf(Long locationId);
    //仓库找货位
    public String getBinByWarehouseId();

    /**
     * 货物所有的拣货位
     * @return
     */
    public String getAllColletionBins();

    /**
     * 获取全货架
     * @return
     */
    public String getAllShelfs();
}
