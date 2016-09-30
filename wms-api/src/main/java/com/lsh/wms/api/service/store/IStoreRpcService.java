package com.lsh.wms.api.service.store;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.baseinfo.BaseinfoStore;

/**
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 16/9/30 下午3:48
 */
public interface IStoreRpcService {
    public BaseinfoStore insertStore(BaseinfoStore baseinfoStore) throws BizCheckedException;

    public BaseinfoStore updateStore(BaseinfoStore baseinfoStore) throws BizCheckedException;

    public BaseinfoStore getStoreByStoreNo(Long storeNo) throws BizCheckedException;

    public BaseinfoStore removeStore(Long storeNo) throws BizCheckedException;
}
