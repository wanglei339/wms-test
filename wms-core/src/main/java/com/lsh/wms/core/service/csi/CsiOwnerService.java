package com.lsh.wms.core.service.csi;

import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.core.dao.csi.CsiOwnerDao;
import com.lsh.wms.core.service.utils.IdGenerator;
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
    @Autowired
    private IdGenerator idGenerator;
    @Autowired
    private CsiOwnerDao ownerDao;
    public CsiOwner getOwner(long iOwnerId) {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("ownerId", iOwnerId);
        List<CsiOwner> items = ownerDao.getCsiOwnerList(mapQuery);
        if (items.size() == 1) {
            return items.get(0);
        } else {
            return null;
        }
    }

    @Transactional(readOnly = false)
    public void insertOwner(CsiOwner owner){
        long iOwnerid = idGenerator.genId("csi_owner", false, false);
        owner.setOwnerId(iOwnerid);
        //增加新增时间
        owner.setCreatedAt(DateUtils.getCurrentSeconds());
        ownerDao.insert(owner);
    }

    @Transactional(readOnly = false)
    public void updateOwner(CsiOwner owner){
        //增加更新时间
        owner.setUpdatedAt(DateUtils.getCurrentSeconds());
        ownerDao.update(owner);
    }

    public List<CsiOwner> getOwnerList(Map<String,Object> mapQuery){
        return ownerDao.getCsiOwnerList(mapQuery);

    }
    public int getOwnerCount(Map<String,Object> mapQuery){
        return ownerDao.countCsiOwner(mapQuery);

    }
}
