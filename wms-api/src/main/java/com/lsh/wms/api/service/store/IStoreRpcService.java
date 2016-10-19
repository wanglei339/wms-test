package com.lsh.wms.api.service.store;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.baseinfo.BaseinfoStore;

import java.util.List;
import java.util.Map;

/**
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 16/9/30 下午3:48
 */
public interface IStoreRpcService {
    public BaseinfoStore insertStore(BaseinfoStore baseinfoStore) throws BizCheckedException;

    public BaseinfoStore updateStore(BaseinfoStore baseinfoStore) throws BizCheckedException;

    public BaseinfoStore getStoreByStoreNo(String storeNo) throws BizCheckedException;

    public BaseinfoStore removeStore(String storeNo) throws BizCheckedException;

    public List<BaseinfoStore> getStoreList(Map<String,Object> params) throws BizCheckedException;

    public Integer countBaseinfoStore(Map<String, Object> params) throws BizCheckedException;
}
