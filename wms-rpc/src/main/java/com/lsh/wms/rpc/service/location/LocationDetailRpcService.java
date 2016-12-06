package com.lsh.wms.rpc.service.location;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.api.model.location.LocationDetailRequest;
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
import java.util.ArrayList;
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
     * PC端查找location明细专用
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
                return new ArrayList<BaseinfoLocation>();
            }
            return baseinfoLocationList;
        }
        List<BaseinfoLocation> baseinfoLocationList = locationDetailService.getIBaseinfoLocaltionModelListByType(params);
        //抛异常
        if (null == baseinfoLocationList || baseinfoLocationList.size() < 1) {
            return new ArrayList<BaseinfoLocation>();
        }
        return baseinfoLocationList;
    }

    //    public BaseinfoLocation insertLocationDetailByType(BaseinfoLocation baseinfoLocation) throws BizCheckedException {
//            //一个通道只能插入两个货架子,加入校验判断
//            if (baseinfoLocation.getClassification().equals(LocationConstant.LOFT_SHELF)) {
//                Map<String, Object> mapQuery = new HashMap<String, Object>();
//                mapQuery.put("fatherId", baseinfoLocation.getFatherId());
//                int size = locationService.getBaseinfoLocationList(mapQuery).size();
//                if (size >= 2) {
//                    //一个通道放两个以上的货架是不可以的
//                    throw new BizCheckedException("2180006");
//                }
//            }
//            locationDetailService.insert(baseinfoLocation);
//            return baseinfoLocation;
//    }
    public void insertLocationDetailByType(LocationDetailRequest request) throws BizCheckedException {
        //一个通道只能插入两个货架子,加入校验判断
        if (request.getClassification().equals(LocationConstant.LOFT_SHELF)) {
            Map<String, Object> mapQuery = new HashMap<String, Object>();
            mapQuery.put("fatherId", request.getFatherId());
            int size = locationService.getBaseinfoLocationList(mapQuery).size();
            if (size >= 2) {
                //一个通道放两个以上的货架是不可以的
                throw new BizCheckedException("2180006");
            }
        }
        try {
            locationDetailService.insert(request);
        } catch (Exception e) {
            logger.error("insertLocationError" + e.getMessage());
            throw new BizCheckedException("2180020");
        }
    }

    public IBaseinfoLocaltionModel updateLocationDetailByType(IBaseinfoLocaltionModel iBaseinfoLocaltionModel) throws BizCheckedException {
//        if (locationDetailService.getIBaseinfoLocaltionModelById(iBaseinfoLocaltionModel.getLocationId()) == null) {
//            throw new BizCheckedException("2180001");
//        }
        try {
            locationDetailService.update(iBaseinfoLocaltionModel);
        } catch (Exception e) {
            logger.error("updateLocation  ERROR " + e.getMessage());
            throw new BizCheckedException("2180022");
        }
        return iBaseinfoLocaltionModel;
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

    /**
     * 插入一个区保证区的坐标完整
     * 1.获取父节点
     * 2.type
     * 3.classfication
     * 4.获取当前最大的regionNo,横向生长
     * 5.locationCode(前端给)
     *
     * @param request
     * @throws BizCheckedException
     */
    public void insertRegion(LocationDetailRequest request) throws BizCheckedException {
        Long fatherId = request.getFatherId();
        BaseinfoLocation fatherLocation = locationService.getLocation(fatherId);
        if (null == fatherLocation) {
            throw new BizCheckedException("2180033");
        }
        //获取大区下的区坐标最大的一个
        Map<String, Object> sortQuery = new HashMap<String, Object>();
        sortQuery.put("leftRange", fatherLocation.getLeftRange());
        sortQuery.put("rightRange", fatherLocation.getRightRange());
        sortQuery.put("classification", LocationConstant.CLASSIFICATION_AREAS);
        sortQuery.put("isValid", LocationConstant.IS_VALID);
        sortQuery.put("regionNoDESC", "");
        List<BaseinfoLocation> locations = locationService.getSortLocations(sortQuery);
        //坐标初始化
        if (null == locations || locations.isEmpty()) {
            request.setRegionNo(0L);
        } else {
            request.setRegionNo(locations.get(0).getRegionNo() + 1L);
        }
        request.setPassageNo(0L);
        request.setShelfLevelNo(0L);
        request.setBinPositionNo(0L);
        request.setTypeName(LocationConstant.LOCATION_TYPE_NAME.get(request.getType()));
//        locationDetailService.insert(request);
    }

    /**
     * 插入一个通道保证通道的坐标正确性
     * 1.获取父节点
     * 2.type
     * 3.classfication
     * 4.继承父亲regionNo,passage获取最大的自增
     * 5.locationCode(前端给)
     *
     * @param request
     * @throws BizCheckedException
     */
    public void insertPassage(LocationDetailRequest request) throws BizCheckedException {
        Long fatherId = request.getFatherId();
        BaseinfoLocation fatherLocation = locationService.getLocation(fatherId);
        if (null == fatherLocation) {
            throw new BizCheckedException("2180033");
        }
        //获取大区下的区坐标最大的一个
        Map<String, Object> sortQuery = new HashMap<String, Object>();
        sortQuery.put("leftRange", fatherLocation.getLeftRange());
        sortQuery.put("rightRange", fatherLocation.getRightRange());
        sortQuery.put("type", LocationConstant.PASSAGE);
        sortQuery.put("isValid", LocationConstant.IS_VALID);
        sortQuery.put("passageNoDESC", "");
        List<BaseinfoLocation> locations = locationService.getSortLocations(sortQuery);
        //坐标初始化
        if (null == locations || locations.isEmpty()) {
            request.setPassageNo(1L);
        } else {
            request.setPassageNo(locations.get(0).getPassageNo() + 1L);
        }
        request.setShelfLevelNo(0L);
        request.setBinPositionNo(0L);
        request.setTypeName(LocationConstant.LOCATION_TYPE_NAME.get(request.getType()));
//        locationDetailService.insert(request);
    }


}
