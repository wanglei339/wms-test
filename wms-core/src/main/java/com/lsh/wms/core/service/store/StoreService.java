package com.lsh.wms.core.service.store;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.core.constant.StoreConstant;
import com.lsh.wms.core.dao.baseinfo.BaseinfoStoreDao;
import com.lsh.wms.model.baseinfo.BaseinfoStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 16/9/30 下午3:19
 */
@Component
@Transactional(readOnly = true)
public class StoreService {
    @Autowired
    private BaseinfoStoreDao baseinfoStoreDao;

    /**
     * 插入门店
     *
     * @param baseinfoStore 门店
     */
    @Transactional(readOnly = false)
    public void insertStore(BaseinfoStore baseinfoStore) {
        baseinfoStore.setStoreId(RandomUtils.genId());
        baseinfoStore.setCreateAt(DateUtils.getCurrentSeconds());
        baseinfoStore.setUpdateAt(DateUtils.getCurrentSeconds());
        baseinfoStoreDao.insert(baseinfoStore);
    }

    /**
     * 跟新门店
     *
     * @param baseinfoStore 门店
     */
    @Transactional(readOnly = false)
    public void update(BaseinfoStore baseinfoStore) {
        baseinfoStore.setUpdateAt(DateUtils.getCurrentSeconds());
        baseinfoStoreDao.update(baseinfoStore);
    }

    /**
     * 通过门店号编号查找门店
     *
     * @param storeId
     * @return
     * @throws BizCheckedException
     */
    public BaseinfoStore getStoreByStoreId(Long storeId) throws BizCheckedException {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("storeId", storeId);
        mapQuery.put("isValid", 1); //1有效
        List<BaseinfoStore> baseinfoStores = baseinfoStoreDao.getBaseinfoStoreList(mapQuery);
        if (null == baseinfoStores || baseinfoStores.size() < 1) {
            throw new BizCheckedException("2180013");
        }
        return baseinfoStores.get(0);
    }

    /**
     * 根据门店号,关闭门店,将is_open置为2
     *
     * @param storeId 门店id
     * @return
     */
    @Transactional(readOnly = false)
    public BaseinfoStore closeStore(Long storeId) {
        BaseinfoStore store = this.getStoreByStoreId(storeId);
        store.setIsOpen(StoreConstant.IS_CLOSED);
        this.update(store);
        return store;
    }

    /**
     * 删除门店,将isValid置为0
     *
     * @param storeId
     * @return
     */
    @Transactional(readOnly = false)
    public BaseinfoStore removeStore(Long storeId) {
        BaseinfoStore store = this.getStoreByStoreId(storeId);
        store.setIsValid(0);
        this.update(store);
        return store;
    }


    /**
     * 根据查询条件返回门店list
     *
     * @param params
     * @return
     */
    public List<BaseinfoStore> getBaseinfoStoreList(Map<String, Object> params) {
        params.put("isValid", 1);    //有效的
        return baseinfoStoreDao.getBaseinfoStoreList(params);
    }

    /**
     * 获取开店的门店列表
     *
     * @param params
     * @return
     */
    public List<BaseinfoStore> getOpenedStoreList(Map<String, Object> params) {
        params.put("isValid", 1);    //有效的
        params.put("isOpen", 1);
        return baseinfoStoreDao.getBaseinfoStoreList(params);
    }

    /**
     * 计数
     *
     * @param params
     * @return
     */
    public Integer countBaseinfoStore(Map<String, Object> params) {
        params.put("isValid", 1);    //有效的
        return baseinfoStoreDao.countBaseinfoStore(params);
    }

    /**
     * 根据门店id返回门店信息
     */
    public BaseinfoStore getBaseinfoStore(Long storeId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("storeId", storeId);
        map.put("isValid", 1);   //有效的
        List<BaseinfoStore> baseinfoStoreList = this.getBaseinfoStoreList(map);

        if (baseinfoStoreList.size() <= 0) {
            return null;
        }
        return baseinfoStoreList.get(0);
    }

    /**
     * 门店的编码storeNo,转为门店的id
     *
     * @param storeNo
     * @return
     */
    public List<BaseinfoStore> getStoreIdByCode(String storeNo) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("storeNo", storeNo);
        map.put("isValid", 1);   //有效的
        List<BaseinfoStore> baseinfoStoreList = this.getBaseinfoStoreList(map);
        return baseinfoStoreList;
    }

    /**
     * 将storeIds字符串解析成id,并查处
     * id1 | id2 |id3
     * @param storeIds  id集合
     * @return  结果集合
     */
    public List<Map<String, Object>> analyStoresIds2Stores(String storeIds) throws BizCheckedException{
        String[] storeIdsStr = storeIds.split("\\|");
        List<Map<String, Object>> storeList = new ArrayList<Map<String, Object>>();
        for (String storeIdStr : storeIdsStr) {
            Long storeId = Long.valueOf(storeIdStr);
            BaseinfoStore store = this.getStoreByStoreId(storeId);
            if (null == store) {
                throw new BizCheckedException("2180018");
            }
            Map<String, Object> storeMap = new HashMap<String, Object>();
            storeMap.put("storeNo", store.getStoreNo());
            storeMap.put("storeName", store.getStoreName());
            storeMap.put("storeId", store.getStoreId());
            storeMap.put("scale",store.getScale());
            storeList.add(storeMap);
        }
        return storeList;
    }

}
