package com.lsh.wms.api.service.location;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.baseinfo.IBaseinfoLocaltionModel;
import com.lsh.wms.model.stock.StockQuant;

import java.util.List;
import java.util.Map;


/**
 * Created by fengkun on 16/7/11.
 */
public interface ILocationRpcService {
    public BaseinfoLocation getLocation(Long locationId) throws BizCheckedException;

    public List<BaseinfoLocation> getStoreLocations(Long locationId) throws BizCheckedException;

    public List<BaseinfoLocation> getNextLevelLocations(Long locationId) throws BizCheckedException;

    public BaseinfoLocation getFatherLocation(Long locationId) throws BizCheckedException;

    public BaseinfoLocation getFatherByType(Long locationId, Long type) throws BizCheckedException;

    public boolean canStore(Long locationId) throws BizCheckedException;

    public BaseinfoLocation insertLocation(BaseinfoLocation location) throws BizCheckedException;

    public BaseinfoLocation updateLocation(BaseinfoLocation location) throws BizCheckedException;

    public BaseinfoLocation assignTemporary() throws BizCheckedException;

    public BaseinfoLocation assignFloor(StockQuant quant) throws BizCheckedException;

    public Long getLocationIdByCode(String code) throws BizCheckedException;

    //分配退货
    public BaseinfoLocation getBackLocation();

    //分配残次
    public BaseinfoLocation getDefectiveLocation();

    /**
     * 获取全区域,为建货架(阁楼、货架)和货位服务
     *
     * @return
     */
    public List<BaseinfoLocation> getAllRegion();

    /**
     * 获取全区域的所有货架 为建库位服务
     *
     * @return
     */
    public List<BaseinfoLocation> getAllShelfs();

    //获取所有仓库下的所有货位
    public List<BaseinfoLocation> getAllBin();

    /**
     * 获取所有的拣货位
     *
     * @return
     */
    public List<BaseinfoLocation> getColletionBins();

    /**
     * 上锁
     *
     * @param locationId
     * @return
     */
    public BaseinfoLocation lockLocation(Long locationId);

    /**
     * 解锁
     *
     * @param locationId
     * @return
     */
    public BaseinfoLocation unlockLocation(Long locationId) throws BizCheckedException;

    /**
     * 同通道的位置排序
     */
    public List<BaseinfoLocation> sortLocationInOnePassage(List<BaseinfoLocation> locations) throws BizCheckedException;

    /**
     * 查找最近的通道
     * @param location
     * @return
     */
    public List<BaseinfoLocation> getNearestPassage(BaseinfoLocation location);

    /**
     * 将mysql数据一次性导入redis
     */
    public void syncRedisAll();

    /**
     * 设置集货道或集货位的位置id
     * @param locationId  位置
     * @param storeNo     门店编号
     * @return
     */
    public BaseinfoLocation setStoreNoOnRoad(Long locationId,Long storeNo)throws BizCheckedException;

    /**
     * 将门店的所有集货位置列出
     * @param storeNo
     * @return
     * @throws BizCheckedException
     */
    public List<BaseinfoLocation> getCollectionByStoreNo(Long storeNo)throws BizCheckedException;

    /**
     * 移除集货道的门店号,将其置为0
     * @param locationId
     * @return
     * @throws BizCheckedException
     */
    public BaseinfoLocation removeStoreNoOnRoad(Long locationId) throws  BizCheckedException;

    /**
     * 获取门店号升序的播种位置
     * @return
     * @throws BizCheckedException
     */
    public List<BaseinfoLocation> sortSowLocationByStoreNo() throws BizCheckedException;

    List<BaseinfoLocation> getSowByStoreNo(Long storeNo) throws BizCheckedException;

}
