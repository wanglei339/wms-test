package com.lsh.wms.rpc.service.location;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.service.location.ILocationDetailRpc;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.service.location.LocationDetailService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.baseinfo.IBaseinfoLocaltionModel;
import com.lsh.wms.rpc.service.stock.StockLotRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 16/8/6 下午4:08
 */
@Service(protocol = "dubbo")
public class LocationDetailRpcService implements ILocationDetailRpc {
    private static Logger logger = LoggerFactory.getLogger(LocationDetailRpcService.class);
    @Autowired
    private LocationDetailService locationDetailService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private LocationRpcService locationRpcService;

    public IBaseinfoLocaltionModel getLocationDetailById(Long locationId) throws BizCheckedException {
        BaseinfoLocation subLocation = (BaseinfoLocation) locationDetailService.getIBaseinfoLocaltionModelById(locationId);
        if (null == subLocation) {
            throw new BizCheckedException("2180001");
        }
        return subLocation;
    }

    /**
     * 查找location明细
     *
     * @param params
     * @return
     */
    public List<BaseinfoLocation> getLocationDetailList(Map<String, Object> params) throws BizCheckedException {
        //如果存在码头出入的参数dockApplication,选用对付码头的方法
        if (params.get("dockApplication") != null) {
            List<BaseinfoLocation> baseinfoLocationList = locationDetailService.getDockListByType(params);
            //抛异常
            if (null == baseinfoLocationList) {
                throw new BizCheckedException("2180002");
            }
            return baseinfoLocationList;
        }
        List<BaseinfoLocation> baseinfoLocationList = locationDetailService.getIBaseinfoLocaltionModelListByType(params);
        //抛异常
        if (null == baseinfoLocationList) {
            throw new BizCheckedException("2180002");
        }
        return baseinfoLocationList;
    }

    public boolean insertLocationDetailByType(BaseinfoLocation baseinfoLocation) throws BizCheckedException {
        try {
            //一个通道只能插入两个货架子,加入校验判断
            if (baseinfoLocation.getClassification().equals(LocationConstant.LOFT_SHELF)) {
                Map<String, Object> mapQuery = new HashMap<String, Object>();
                mapQuery.put("fatherId", baseinfoLocation.getFatherId());
                if (locationService.getBaseinfoLocationList(mapQuery).size() >= 2) {
                    //一个通道放两个以上的货架是不可以的
                    throw new BizCheckedException("2180006");
                }
            }
            locationDetailService.insert(baseinfoLocation);
        } catch (BizCheckedException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean updateLocationDetailByType(BaseinfoLocation baseinfoLocation) throws BizCheckedException {
        if (locationDetailService.getIBaseinfoLocaltionModelById(baseinfoLocation.getLocationId()) == null) {
            throw new BizCheckedException("2180001");
        }
        try {
            locationDetailService.update(baseinfoLocation);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }

    public Integer countLocationDetailByType(Map<String, Object> mapQuery) throws BizCheckedException {
        if (mapQuery.get("dockApplication") != null) {
            return locationDetailService.countDockList(mapQuery);
        }
        return locationDetailService.countLocationDetail(mapQuery);
    }

    public boolean removeLocation(Long locationId) throws BizCheckedException {
        if (locationId != null) {
            try {
                locationService.removeLocationAndChildren(locationId);
                return true;
            } catch (Exception e) {
                logger.error(e.getMessage());
                return false;
            }
        } else {
            throw new BizCheckedException("2180003");
        }
    }

    /**
     * 获取固定的位置list的方法,通过传入获取的list方法不同,返回不同的location集合,如功能区、全货架、全大区、全货架区、全通道
     *
     * @param listType
     * @return
     * @throws BizCheckedException
     */
    public List<BaseinfoLocation> getTargetListByListType(Integer listType) throws BizCheckedException {
        if (null == listType) {
            throw new BizCheckedException("2180004");
        }
        return locationDetailService.getTargetListByListType(listType);
    }

    /**
     * 获取下一层级的所有节点
     *
     * @param locationId
     * @return
     */
    public List<BaseinfoLocation> getNextLevelLocations(Long locationId) throws BizCheckedException {
        return locationRpcService.getNextLevelLocations(locationId);
    }
}
