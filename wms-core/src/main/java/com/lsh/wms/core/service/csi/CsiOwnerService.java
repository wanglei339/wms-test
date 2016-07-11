package com.lsh.wms.core.service.csi;

import com.lsh.wms.core.dao.csi.CsiOwnerDao;
import com.lsh.wms.model.csi.CsiCategory;
import com.lsh.wms.model.csi.CsiOwner;
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
    public CsiOwner insertOwner(CsiOwner owner){
        if(owner.getOwnerId() == 0){
            int iOwnerid = 0;
            int count = ownerDao.countCsiOwner(null);
            if(count == 0){
                iOwnerid = 1;
            }else{
                iOwnerid = count+1;
            }
            owner.setOwnerId((long)iOwnerid);
        }
        ownerDao.insert(owner);

        return owner;
    }

    @Transactional(readOnly = false)
    public int updateOwner(CsiOwner owner){
        if(this.getOwner(owner.getOwnerId()) == null){
            return -1;
        }
        ownerDao.update(owner);

        //更新缓存中的数据
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("ownerId", owner.getOwnerId());
        CsiOwner newOwner = ownerDao.getCsiOwnerList(mapQuery).get(0);
        m_OwnerCache.put(owner.getOwnerId(),newOwner);
        return 0;
    }
}
