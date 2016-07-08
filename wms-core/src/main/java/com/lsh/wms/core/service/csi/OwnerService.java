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
public class OwnerService {
    private static final Logger logger = LoggerFactory.getLogger(OwnerService.class);
    private static final ConcurrentMap<Integer, CsiOwner> m_OwnerCache = new ConcurrentHashMap<Integer, CsiOwner>();

    @Autowired
    private CsiOwnerDao ownerDao;

    public CsiOwner getOwner(int iOwnerId){
        CsiOwner cat = m_OwnerCache.get(iOwnerId);
        if(cat == null){
            Map<String, Object> mapQuery = new HashMap<String, Object>();
            mapQuery.put("owner_id", iOwnerId);
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
}
