package com.lsh.wms.core.service.item;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.dao.baseinfo.BaseinfoItemLocationDao;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoItemLocation;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.stock.StockQuant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by lixin-mac on 16/7/13.
 */
@Component
@Transactional(readOnly = true)
public class ItemLocationService {
    private static final Logger logger = LoggerFactory.getLogger(ItemLocationService.class);
//    private static final ConcurrentMap<Long,List<BaseinfoItemLocation>> m_ItemLocationCache = new ConcurrentHashMap<Long,List<BaseinfoItemLocation>>();
//    private static final ConcurrentMap<Long,List<BaseinfoItemLocation>> m_LocationCache = new ConcurrentHashMap<Long,List<BaseinfoItemLocation>>();

    @Autowired
    private BaseinfoItemLocationDao itemLocationDao;
    @Autowired
    private LocationService locationService;
    @Autowired
    private StockQuantService stockQuantService;

//    public List<BaseinfoItemLocation> getItemLocationList(long iSkuId,long iOwnerId){
//        Long key = (((long)iOwnerId)<<32) + (iSkuId);
//        List<BaseinfoItemLocation> list = m_ItemLocationCache.get(key);
//        if(list == null){
//            Map<String,Object> mapQuery = new HashMap<String, Object>();
//            mapQuery.put("skuId",iSkuId);
//            mapQuery.put("ownerId",iOwnerId);
//            list = itemLocationDao.getBaseinfoItemLocationList(mapQuery);
//
//            if(list.size()>0){
//                m_ItemLocationCache.put(key,list);
//
//            }else{
//                return null;
//            }
//        }
//
//        return list;
//    }

    public List<BaseinfoItemLocation> getItemLocationByLocationID(long iLocationId){
        Map<String,Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("pickLocationid",iLocationId);
        List<BaseinfoItemLocation> list  = itemLocationDao.getBaseinfoItemLocationList(mapQuery);
        if (list == null) {
            return new ArrayList<BaseinfoItemLocation>();
        } else {
            return list;
        }
    }
    public List<BaseinfoItemLocation> getItemLocation(Map<String,Object> mapQuery){
        List<BaseinfoItemLocation> list  = itemLocationDao.getBaseinfoItemLocationList(mapQuery);
        if (list == null) {
            return new ArrayList<BaseinfoItemLocation>();
        } else {
            return list;
        }
    }



    @Transactional(readOnly = false)
    public BaseinfoItemLocation insertItemLocation(BaseinfoItemLocation itemLocation){
//        //检查是否有重复记录
//        long skuID = itemLocation.getSkuId();
//        long ownerId = itemLocation.getOwnerId();
//        long locationId = itemLocation.getPickLocationid();
//        Map<String,Object> mapQuery = new HashMap<String, Object>();
//        mapQuery.put("skuId",skuID);
//        mapQuery.put("ownerId",ownerId);
//        mapQuery.put("pickLocationid",locationId);
//        List<BaseinfoItemLocation> list = itemLocationDao.getBaseinfoItemLocationList(mapQuery);
//        if(list.size()>0){
//            return null;
//        }
        long itemId = itemLocation.getItemId();
        long locationId = itemLocation.getPickLocationid();
        BaseinfoLocation newLocation = locationService.getLocation(locationId);
        if(LocationConstant.SPLIT_AREA.compareTo(newLocation.getRegionType()) != 0){
            //非拆零区,验证拣货位是否被占用
            List<BaseinfoItemLocation> newList = this.getItemLocationByLocationID(locationId);
            if(newList.size()>0){
                throw new BizCheckedException("2990001");//该拣货位已被占用
            }
        }

        List<BaseinfoItemLocation> oldList = this.getItemLocationList(itemId);
        if(oldList.size()>0){
            BaseinfoItemLocation oldItemList = oldList.get(0);
            BaseinfoLocation oldLocation = locationService.getLocation(oldItemList.getPickLocationid());
            if(!oldLocation.getRegionType().equals(newLocation.getRegionType())){
                throw new BizCheckedException("2990002");
            }
        }



        itemLocationDao.insert(itemLocation);
        return itemLocation;
    }

    @Transactional(readOnly = false)
    public void updateItemLocation(BaseinfoItemLocation itemLocation){
        BaseinfoItemLocation oldItemLocation = itemLocationDao.getBaseinfoItemLocationById(itemLocation.getId());

        Long locationId = itemLocation.getPickLocationid();
        long itemId = itemLocation.getItemId();
        if(itemLocation.getPickLocationid() == null || oldItemLocation.getPickLocationid().equals(itemLocation.getPickLocationid())){
            //不更新拣货位ID
            //验证该拣货位是否有库存
            List<StockQuant> quantList = stockQuantService.getQuantsByLocationId(locationId);
            if(quantList != null && quantList.size() > 0){
                throw new BizCheckedException("");//该拣货位有库存不能更新
            }
        }else{
            //更新拣货位id
            //新拣货位ID验证
            BaseinfoLocation newLocation = locationService.getLocation(locationId);
            if(LocationConstant.SPLIT_AREA.compareTo(newLocation.getRegionType()) != 0){
                //非拆零区,验证拣货位是否被占用
                List<BaseinfoItemLocation> newList = this.getItemLocationByLocationID(locationId);
                if(newList.size()>0){
                    throw new BizCheckedException("2990001");//该拣货位已被占用
                }
            }
            List<BaseinfoItemLocation> oldList = this.getItemLocationList(itemId);
            if(oldList.size()>0){
                BaseinfoItemLocation oldItemList = oldList.get(0);
                BaseinfoLocation oldLocation = locationService.getLocation(oldItemList.getPickLocationid());
                if(!oldLocation.getRegionType().equals(newLocation.getRegionType())){
                    throw new BizCheckedException("2990002");
                }
            }
        }
        itemLocationDao.update(itemLocation);
    }

    public BaseinfoItemLocation getItemLocation(long id){
        return itemLocationDao.getBaseinfoItemLocationById(id);
    }

    public List<BaseinfoItemLocation> getItemLocationList(long itemId){
        Map<String,Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("itemId",itemId);
        List<BaseinfoItemLocation> list = itemLocationDao.getBaseinfoItemLocationList(mapQuery);
        return list;
    }

    @Transactional(readOnly = false)
    public void deleteItemLocation(BaseinfoItemLocation itemLocation){
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("id",itemLocation.getItemId());
        map.put("pickLocationid",itemLocation.getPickLocationid());
        List<BaseinfoItemLocation> baseinfoItemLocations = itemLocationDao.getBaseinfoItemLocationList(map);
        if(baseinfoItemLocations != null && baseinfoItemLocations.size() > 0){
            BaseinfoItemLocation baseinfoItemLocation = baseinfoItemLocations.get(0);
            //验证该拣货位是否有库存
            List<StockQuant> quantList = stockQuantService.getQuantsByLocationId(baseinfoItemLocation.getPickLocationid());
            if(quantList != null && quantList.size() > 0){
                throw new BizCheckedException("");//该拣货位有库存不能删除
            }
        }
        itemLocationDao.deleteItemLocation(itemLocation);
    }

    @Transactional(readOnly = false)
    public void updateByItemIdAndPicId(BaseinfoItemLocation baseinfoItemLocation){
        itemLocationDao.updateByItemIdAndPicId(baseinfoItemLocation);
    }

    public List<Map<String, String>> getPickLocationsByItemId (Long itemId) {
        List<BaseinfoItemLocation> itemLocations = this.getItemLocationList(itemId);
        List<Map<String, String>> pickLocations = new ArrayList<Map<String, String>>();
        for (BaseinfoItemLocation itemLocation: itemLocations) {
            Long pickLocationId = itemLocation.getPickLocationid();
            BaseinfoLocation pickLocation = locationService.getLocation(pickLocationId);
            if (pickLocation != null) {
                Map<String, String> pickLocationMap = new HashMap<String, String>();
                pickLocationMap.put("locationId", pickLocationId.toString());
                pickLocationMap.put("locationCode", pickLocation.getLocationCode());
                pickLocations.add(pickLocationMap);
            }
        }
        return pickLocations;
    }
}
