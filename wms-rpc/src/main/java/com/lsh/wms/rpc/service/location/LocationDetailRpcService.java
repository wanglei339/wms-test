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
     * @param binCodes 待合并库位
     * @return 返回一个map 分别 "msg":xx , "arr":问题locationId
     * @throws BizCheckedException
     */
    public Map<String, Object> mergeBinsByLocationIds(List<String> binCodes) throws BizCheckedException {
        //结果集
        Map<String, Object> result = new HashMap<String, Object>();
        //有库存的
        List<String> quantBinCodes = new ArrayList<String>();
        //合过的
        List<String> mergedBinCodes = new ArrayList<String>();
        //被锁定的
        List<String> lockBinCodes = new ArrayList<String>();
        if (null == binCodes || binCodes.size() < 2) {
            throw new BizCheckedException("2180036");
        }
        List<Long> binIds = new ArrayList<Long>();
        //code=>locationId
        Map<String, Long> code2Ids = new HashMap<String, Long>();
        //判断货架的Id
        long shelfId = 0L;
        //判断货架的层数
        long shelfLevelNo = 0L;
        for (String locationCode : binCodes) {
            Long locationId = locationRpcService.getLocationIdByCode(locationCode);
            BaseinfoLocation location = locationService.getLocation(locationId);
            if (null == location) {
                throw new BizCheckedException("2180001");
            }
            if (null == code2Ids.get(locationCode)) {
                code2Ids.put(locationCode, location.getLocationId());
            } else {
                throw new BizCheckedException("2180041");
            }
            if (!LocationConstant.BIN.equals(location.getType())) {
                throw new BizCheckedException("2180034");
            }
            //锁定不能合并
            if (LocationConstant.IS_LOCKED.equals(location.getIsLocked())) {
                lockBinCodes.add(locationCode);
            }
            //无库存
            if (0L != location.getCurContainerVol()) {
                quantBinCodes.add(locationCode);
            }
            //同一层判断
            if (0L == shelfLevelNo) {
                shelfLevelNo = location.getShelfLevelNo();
            } else if (shelfLevelNo != location.getShelfLevelNo()) {
                throw new BizCheckedException("2180042");
            }

            //同一货架判断
            BaseinfoLocation oneShelf = locationService.getShelfByLocationId(location.getLocationId());
            if (0L == shelfId) {
                shelfId = oneShelf.getLocationId();
            } else if (shelfId != oneShelf.getLocationId()) {
                throw new BizCheckedException("2180043");
            }
            binIds.add(locationId);
        }
        //转成bin
        List<BaseinfoLocationBin> bins = new ArrayList<BaseinfoLocationBin>();
        BaseinfoLocationBin toBin = null;
        if (binIds.size() > 1) {
            //目标库位
            toBin = (BaseinfoLocationBin) locationDetailService.getIBaseinfoLocaltionModelById(binIds.get(0));
            if (0L != toBin.getRelLocationId()) {
                mergedBinCodes.add(toBin.getLocationCode());
            }
            //合并库位
            for (int i = 1; i < binIds.size(); i++) {
                BaseinfoLocationBin bin = (BaseinfoLocationBin) locationDetailService.getIBaseinfoLocaltionModelById(binIds.get(i));
                if (0L != bin.getRelLocationId()) {
                    mergedBinCodes.add(bin.getLocationCode());
                }
                bins.add(bin);
            }
        }
        //有库存的
        if (quantBinCodes.size() > 0) {
            result.put("msg", "位置有库存,不能合并");
            result.put("arr", quantBinCodes);
            return result;
        }
        //合并过的
        if (mergedBinCodes.size() > 0) {
            result.put("msg", "库位已合并过,不能再次合并");
            result.put("arr", mergedBinCodes);
            return result;
        }
        //锁定的
        if (lockBinCodes.size() > 0) {
            result.put("msg", "库位已被锁定,不能合并");
            result.put("arr", lockBinCodes);
            return result;
        }
        locationDetailService.mergeBins(bins, toBin);
        return result;
    }

    /**
     * @param locationCode 库位码
     * @throws BizCheckedException
     */
    public void splitBins(String locationCode) throws BizCheckedException {
        Long oneLocationId = locationRpcService.getLocationIdByCode(locationCode);
        BaseinfoLocation location = locationService.getLocation(oneLocationId);
        //库存,锁
        if (null == location) {
            throw new BizCheckedException("2180001");
        }
        if (!LocationConstant.BIN.equals(location.getType())) {
            throw new BizCheckedException("2180037");
        }
        BaseinfoLocationBin oneBin = (BaseinfoLocationBin) locationDetailService.getIBaseinfoLocaltionModelById(location.getLocationId());
        Long targetLocationId = oneBin.getRelLocationId();
        if (0L == targetLocationId) {
            throw new BizCheckedException("2180038");
        }
        //关联的在使用的bin,区别其他被锁定的bin
        BaseinfoLocationBin targetBin = (BaseinfoLocationBin) locationDetailService.getIBaseinfoLocaltionModelById(targetLocationId);
        if (LocationConstant.IS_LOCKED.equals(targetBin.getIsLocked())) {
            throw new BizCheckedException("2180031");
        }
        //库存和这个息息相关,有库存就一定有这个数
        if (0L != targetBin.getCurContainerVol()) {
            throw new BizCheckedException("2180032");
        }
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("relLocationId", targetBin.getRelLocationId());
        List<BaseinfoLocationBin> allRelBins = binService.getBins(queryMap);
        List<BaseinfoLocationBin> toSplitbins = new ArrayList<BaseinfoLocationBin>();
        //解锁,拆分关联关系,包含需要拆分库位的本身
        if (null != allRelBins && allRelBins.size() > 1) {
            for (BaseinfoLocationBin tempBin : allRelBins) {
                if (targetBin.getRelLocationId().equals(tempBin.getLocationId())) {
                    continue;
                }
                toSplitbins.add(tempBin);
            }
            locationDetailService.splitBins(toSplitbins, targetBin);
        }
    }

    /**
     * 查询库位的关联的库位编码集合
     *
     * @param locationCode
     * @return 返回map  "canSplit":true|false  "binCodes":list
     * @throws BizCheckedException
     */
    public Map<String, Object> checkBin(String locationCode) throws BizCheckedException {
        boolean canSplit = true;
        List<String> binCodes = new ArrayList<String>();
        Map<String, Object> result = new HashMap<String, Object>();
        Long oneLocationId = locationRpcService.getLocationIdByCode(locationCode);
        BaseinfoLocation location = locationService.getLocation(oneLocationId);
        //库存,锁
        if (null == location) {
            throw new BizCheckedException("2180001");
        }
        if (!LocationConstant.BIN.equals(location.getType())) {
            throw new BizCheckedException("2180037");
        }
        BaseinfoLocationBin oneBin = (BaseinfoLocationBin) locationDetailService.getIBaseinfoLocaltionModelById(location.getLocationId());
        Long targetLocationId = oneBin.getRelLocationId();
        //若不是关联的库位,不需要拆分
        if (0L == targetLocationId) {
            canSplit = false;
            result.put("canSplit", canSplit);
            result.put("arr", binCodes);
            result.put("msg", "该位置不是合并库位,无需拆分");
            return result;
        }
        //关联的在使用的bin,区别其他被锁定的bin
        BaseinfoLocationBin targetBin = (BaseinfoLocationBin) locationDetailService.getIBaseinfoLocaltionModelById(targetLocationId);
        if (LocationConstant.IS_LOCKED.equals(targetBin.getIsLocked())) {
            canSplit = false;
            result.put("canSplit", canSplit);
            result.put("arr", binCodes);
            result.put("msg", "该位置已被锁定,不可拆分");
            return result;
        }
        //库存和这个息息相关,有库存就一定有这个数
        if (0L != targetBin.getCurContainerVol()) {
            canSplit = false;
            result.put("canSplit", canSplit);
            result.put("arr", binCodes);
            result.put("msg", "该位置已被占用,不可拆分");
            return result;
        }
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("relLocationId", targetBin.getRelLocationId());
        List<BaseinfoLocationBin> allRelBins = binService.getBins(queryMap);
        if (null == allRelBins || allRelBins.size() < 2) {
            canSplit = false;
            result.put("canSplit", canSplit);
            result.put("arr", binCodes);
            result.put("msg", "该库位无关联,无需拆分");
            return result;
        }
        for (BaseinfoLocationBin bin : allRelBins) {
            BaseinfoLocation temp = locationService.getLocation(bin.getLocationId());
            if (null != temp) {
                binCodes.add(temp.getLocationCode());
            }
        }
        result.put("canSplit",canSplit);
        result.put("arr", binCodes);
        return result;
    }


}
