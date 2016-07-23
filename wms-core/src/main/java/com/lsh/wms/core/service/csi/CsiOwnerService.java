package com.lsh.wms.core.service.csi;

import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.core.dao.csi.CsiOwnerDao;
import com.lsh.wms.model.csi.CsiCategory;
import com.lsh.wms.model.csi.CsiOwner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by zengwenjun on 16/7/8.
 */
@Component
@Transactional(readOnly = true)
public class CsiOwnerService {
    private static final Logger logger = LoggerFactory.getLogger(CsiOwnerService.class);
    //将Integer改为long modify by lixin
    private static final ConcurrentMap<Long, CsiOwner> m_OwnerCache = new ConcurrentHashMap<Long, CsiOwner>();

    @Autowired
    private CsiOwnerDao ownerDao;
    //将int改为long modify by lixin
    public CsiOwner getOwner(long iOwnerId){
        CsiOwner cat = m_OwnerCache.get(iOwnerId);
        if(cat == null){
            Map<String, Object> mapQuery = new HashMap<String, Object>();
            mapQuery.put("ownerId", iOwnerId);
            List<CsiOwner> items = ownerDao.getCsiOwnerList(mapQuery);
            if(items.size() == 1){
                cat = items.get(0);
                m_OwnerCache.put(iOwnerId, cat);
            } else {
                return null;
            }
        }
        return cat;
    }

    @Transactional(readOnly = false)
    public void insertOwner(CsiOwner owner){
        long iOwnerid = RandomUtils.genId();
        owner.setOwnerId(iOwnerid);
        //增加新增时间
        owner.setCreatedAt(DateUtils.getCurrentSeconds());
        ownerDao.insert(owner);
        //更新缓存
        m_OwnerCache.put(owner.getOwnerId(),owner);
    }

    @Transactional(readOnly = false)
    public void updateOwner(CsiOwner owner){
        //增加更新时间
        owner.setUpdatedAt(DateUtils.getCurrentSeconds());
        ownerDao.update(owner);

        //更新缓存中的数据
        Map<String, Object> mapQuery = new HashMap<String, Object>();

        mapQuery.put("ownerId", owner.getOwnerId());
        CsiOwner newOwner = ownerDao.getCsiOwnerList(mapQuery).get(0);
        m_OwnerCache.put(owner.getOwnerId(),newOwner);
    }

    public List<CsiOwner> getOwnerList(Map<String,Object> mapQuery){
        return ownerDao.getCsiOwnerList(mapQuery);

    }
    public int getOwnerCount(Map<String,Object> mapQuery){
        return ownerDao.countCsiOwner(mapQuery);

    }
}
