package com.lsh.wms.core.service.store;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.core.constant.StoreConstant;
import com.lsh.wms.core.dao.baseinfo.BaseinfoStoreDao;
import com.lsh.wms.model.baseinfo.BaseinfoStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
        baseinfoStoreDao.insert(baseinfoStore);
    }

    /**
     * 跟新门店
     *
     * @param baseinfoStore 门店
     */
    @Transactional(readOnly = false)
    public void update(BaseinfoStore baseinfoStore) {
        baseinfoStoreDao.update(baseinfoStore);
    }

    /**
     * 通过门店号编号查找门店
     *
     * @param storeNo 门店编号
     * @return
     */
    public BaseinfoStore getStoreByStoreNo(String storeNo) throws BizCheckedException {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("storeNo", storeNo);
        List<BaseinfoStore> baseinfoStores = baseinfoStoreDao.getBaseinfoStoreList(mapQuery);
        if (null == baseinfoStores || baseinfoStores.size() < 1) {
            throw new BizCheckedException("2180013");
        }
            return baseinfoStores.get(0);
    }

    /**
     * 根据门店号,关闭门店,将is_open置为2
     * @param storeNo
     * @return
     */
    @Transactional(readOnly = false)
    public BaseinfoStore removeStore(String storeNo){
        BaseinfoStore store = this.getStoreByStoreNo(storeNo);
        store.setIsOpen(StoreConstant.IS_CLOSED);
        this.update(store);
        return store;
    }


    /**
     * 根据查询条件返回门店list
     * @param params
     * @return
     */
    public List<BaseinfoStore> getBaseinfoStoreList(Map<String,Object> params){
        return baseinfoStoreDao.getBaseinfoStoreList(params);
    }

    /**
     * 计数
     * @param params
     * @return
     */
    public Integer countBaseinfoStore(Map<String, Object> params){
        return baseinfoStoreDao.countBaseinfoStore(params);
    }

    /**
     * 根据storeNo返回门店信息
     */
    public BaseinfoStore getBaseinfoStore(String storeNo){
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("storeNo",storeNo);
        List<BaseinfoStore> baseinfoStoreList = this.getBaseinfoStoreList(map);

        if(baseinfoStoreList.size()<=0){
            return null;
        }
        return baseinfoStoreList.get(0);
    }
}
