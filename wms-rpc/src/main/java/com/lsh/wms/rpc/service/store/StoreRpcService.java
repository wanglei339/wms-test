package com.lsh.wms.rpc.service.store;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.service.store.IStoreRpcService;
import com.lsh.wms.core.service.store.StoreService;
import com.lsh.wms.model.baseinfo.BaseinfoStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 16/9/30 下午3:53
 */
@Service(protocol = "dubbo")
public class StoreRpcService implements IStoreRpcService {
    private static Logger logger = LoggerFactory.getLogger(StoreRpcService.class);
    @Autowired
    private StoreService storeService;

    public BaseinfoStore insertStore(BaseinfoStore baseinfoStore) throws BizCheckedException {
        storeService.insertStore(baseinfoStore);
        return baseinfoStore;
    }

    public BaseinfoStore updateStore(BaseinfoStore baseinfoStore) throws BizCheckedException {
        storeService.update(baseinfoStore);
        return baseinfoStore;
    }

    public BaseinfoStore getStoreByStoreNo(String storeNo) throws BizCheckedException {
        if (null == storeNo) {
            throw new BizCheckedException("2180014");
        }
        Long storeId = this.getStoreIdByCode(storeNo);
        BaseinfoStore store = storeService.getStoreByStoreId(storeId);
        return store;
    }

    public BaseinfoStore getStoreByStoreId(Long storeId) throws BizCheckedException {
        if (null == storeId) {
            throw new BizCheckedException("2180018");
        }
        return storeService.getStoreByStoreId(storeId);
    }

    public BaseinfoStore closeStore(String storeNo) throws BizCheckedException {
        if (null == storeNo) {
            throw new BizCheckedException("2180014");
        }
        Long storeId = this.getStoreIdByCode(storeNo);
        return storeService.closeStore(storeId);
    }

    public BaseinfoStore removeStore(String storeNo) throws BizCheckedException {
        if (null == storeNo) {
            throw new BizCheckedException("2180014");
        }
        Long storeId = this.getStoreIdByCode(storeNo);
        return storeService.removeStore(storeId);
    }

    public List<BaseinfoStore> getStoreList(Map<String, Object> params) throws BizCheckedException {
        return storeService.getBaseinfoStoreList(params);
    }

    public Integer countBaseinfoStore(Map<String, Object> params) throws BizCheckedException {
        return storeService.countBaseinfoStore(params);
    }

    public Long getStoreIdByCode(String storeNo) throws BizCheckedException {
        if (null == storeNo) {
            throw new BizCheckedException("2180014");
        }
        List<BaseinfoStore> baseinfoStores = storeService.getStoreIdByCode(storeNo);
        if (null == baseinfoStores || baseinfoStores.size() < 1) {
            throw new BizCheckedException("2180013");
        }
        if (baseinfoStores.size() > 1) {
            throw new BizCheckedException("2180017");
        }
        return baseinfoStores.get(0).getStoreId();
    }
}
