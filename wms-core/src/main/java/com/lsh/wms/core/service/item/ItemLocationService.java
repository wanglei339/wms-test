package com.lsh.wms.core.service.item;

import com.lsh.wms.core.dao.baseinfo.BaseinfoItemLocationDao;
import com.lsh.wms.model.baseinfo.BaseinfoItemLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    private static final ConcurrentMap<Long,List<BaseinfoItemLocation>> m_ItemLocationCache = new ConcurrentHashMap<Long,List<BaseinfoItemLocation>>();
    private static final ConcurrentMap<Long,List<BaseinfoItemLocation>> m_LocationCache = new ConcurrentHashMap<Long,List<BaseinfoItemLocation>>();

    @Autowired
    private BaseinfoItemLocationDao itemLocationDao;

    public List<BaseinfoItemLocation> getItemLocationList(long iSkuId,long iOwnerId){
        Long key = (((long)iOwnerId)<<32) + (iSkuId);
        List<BaseinfoItemLocation> list = m_ItemLocationCache.get(key);
        if(list == null){
            Map<String,Object> mapQuery = new HashMap<String, Object>();
            mapQuery.put("skuId",iSkuId);
            mapQuery.put("ownerId",iOwnerId);
            list = itemLocationDao.getBaseinfoItemLocationList(mapQuery);

            if(list.size()>0){
                m_ItemLocationCache.put(key,list);

            }else{
                return null;
            }
        }

        return list;
    }

    public List<BaseinfoItemLocation> getItemLocationByLocationID(long iLocationId){
        List<BaseinfoItemLocation> list = m_LocationCache.get(iLocationId);
        if(list == null){
            Map<String,Object> mapQuery = new HashMap<String, Object>();
            mapQuery.put("pickLocationid",iLocationId);
            list = itemLocationDao.getBaseinfoItemLocationList(mapQuery);
            if(list.size()>0){
                m_LocationCache.put(iLocationId,list);
            }else{
                return null;
            }

        }
        return list;
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
        itemLocationDao.insert(itemLocation);
        return itemLocation;
    }

    @Transactional(readOnly = false)
    public void updateItemLocation(BaseinfoItemLocation itemLocation){
        itemLocationDao.update(itemLocation);
    }

    public BaseinfoItemLocation getItemLocation(long id){
        return itemLocationDao.getBaseinfoItemLocationById(id);
    }

    public List<BaseinfoItemLocation> getItemLocationList(long itemId){
        Map<String,Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("itemId",itemId);
        List<BaseinfoItemLocation> list =
                itemLocationDao.getBaseinfoItemLocationList(mapQuery);
        return list;
    }


}
