package com.lsh.wms.rpc.service.location;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.base.q.Module.Base;
import com.lsh.wms.api.model.location.LocationDetailRequest;
import com.lsh.wms.api.service.location.ILocationDetailRpc;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.service.location.BaseinfoLocationBinService;
import com.lsh.wms.core.service.location.LocationDetailService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.baseinfo.BaseinfoLocationBin;
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
    @Autowired
    private BaseinfoLocationBinService binService;

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
     * 合并库位
     *
     * @param fromLocationCodes     等待合并的库位码
     * @param targetLocationCode     目标库位码
     * @throws BizCheckedException
     */
    public void mergeBinsByLocationIds(List<String> fromLocationCodes, String targetLocationCode) throws BizCheckedException {
        //校验是否是货位,是否位于同一货架,层坐标相同
        boolean sameShelf = true;
        boolean sameLevel = true;
        Long targetLocationId = locationRpcService.getLocationIdByCode(targetLocationCode);
        BaseinfoLocation targetBin = locationService.getLocation(targetLocationId);
        if (null == targetBin) {
            throw new BizCheckedException("2180001");
        }
        if (null == fromLocationCodes || fromLocationCodes.isEmpty()) {
            throw new BizCheckedException("2180036");
        }
        if (!LocationConstant.BIN.equals(targetBin.getType())) {
            throw new BizCheckedException("2180034");
        }
        //无库存||因为有移库必然有托盘数量的变更
        if (0L != targetBin.getCurContainerVol()) {
            throw new BizCheckedException("2180035");
        }
        //锁定不能合并
        if (LocationConstant.IS_LOCKED.equals(targetBin.getIsLocked())){
            throw new BizCheckedException("2180045");
        }

        BaseinfoLocation targetShelf = locationService.getShelfByLocationId(targetBin.getLocationId());
        List<Long> binIds = new ArrayList<Long>();
        for (String locationCode : fromLocationCodes) {
            Long locationId = locationRpcService.getLocationIdByCode(locationCode);
            BaseinfoLocation location = locationService.getLocation(locationId);
            if (targetLocationId.equals(locationId)) {
                throw new BizCheckedException("2180041");
            }
            if (null == location) {
                throw new BizCheckedException("2180001");
            }
            if (!LocationConstant.BIN.equals(location.getType())) {
                throw new BizCheckedException("2180034");
            }
            //锁定不能合并
            if (LocationConstant.IS_LOCKED.equals(location.getIsLocked())){
                throw new BizCheckedException("2180045");
            }
            //无库存
            if (0L != location.getCurContainerVol()) {
                throw new BizCheckedException("2180035");
            }
            if (!location.getShelfLevelNo().equals(targetBin.getShelfLevelNo())) {
                sameLevel = false;
                break;
            }
            BaseinfoLocation oneShelf = locationService.getShelfByLocationId(targetBin.getLocationId());
            if (!targetShelf.getLocationId().equals(oneShelf.getLocationId())) {
                sameShelf = false;
                break;
            }
            binIds.add(locationId);
        }
        if (!sameLevel) {
            throw new BizCheckedException("2180042");
        }
        if (!sameShelf) {
            throw new BizCheckedException("2180043");
        }
        //转成bin
        BaseinfoLocationBin toBin = (BaseinfoLocationBin) locationDetailService.getIBaseinfoLocaltionModelById(targetBin.getLocationId());
        if (0L!=toBin.getRelLocationId()){
            throw new BizCheckedException("2180044");
        }
        List<BaseinfoLocationBin> bins = new ArrayList<BaseinfoLocationBin>();
        for (Long locationId : binIds) {
            BaseinfoLocationBin bin = (BaseinfoLocationBin) locationDetailService.getIBaseinfoLocaltionModelById(locationId);
            if (0L!=toBin.getRelLocationId()){
                throw new BizCheckedException("2180044");
            }
            bins.add(bin);
        }
        locationDetailService.mergeBins(bins, toBin);
    }


    /**
     * 需要拆分库位
     * @param targetLocationCode 需要拆分的库位码
     * @throws BizCheckedException
     */
    public void splitBins(String targetLocationCode) throws BizCheckedException {
        Long targetLocationId = locationRpcService.getLocationIdByCode(targetLocationCode);
        BaseinfoLocation location = locationService.getLocation(targetLocationId);
        //库存,锁
        if (null == location) {
            throw new BizCheckedException("2180001");
        }
        if (!LocationConstant.BIN.equals(location.getType())) {
            throw new BizCheckedException("2180037");
        }
        if (LocationConstant.IS_LOCKED.equals(location.getIsLocked())) {
            throw new BizCheckedException("2180031");
        }
        //库存和这个息息相关,有库存就一定有这个数
        if (0L != location.getCurContainerVol()) {
            throw new BizCheckedException("2180032");
        }
        BaseinfoLocationBin targetBin = (BaseinfoLocationBin) locationDetailService.getIBaseinfoLocaltionModelById(location.getLocationId());
        if (0L == targetBin.getRelLocationId()) {
            throw new BizCheckedException("2180038");
        }
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("relLocationId", targetBin.getRelLocationId());
        List<BaseinfoLocationBin> allRelBins = binService.getBins(queryMap);
        List<BaseinfoLocationBin> toSplitbins = new ArrayList<BaseinfoLocationBin>();
        //解锁,拆分关联关系,包含需要拆分库位的本身
        if (null != allRelBins && allRelBins.size() > 1) {
            for (BaseinfoLocationBin oneBin : allRelBins){
                if (targetBin.getRelLocationId().equals(oneBin.getLocationId())){
                    continue;
                }
                toSplitbins.add(oneBin);
            }
            locationDetailService.splitBins(toSplitbins, targetBin);
        }
    }


}
