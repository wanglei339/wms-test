package com.lsh.wms.api.service.store;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.baseinfo.BaseinfoStore;

import java.util.List;
import java.util.Map;

/**
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 16/9/30 下午3:50
 */
public interface IStoreRestService {
    public String insertStore(BaseinfoStore baseinfoStore) throws BizCheckedException;

    public String updateStore(BaseinfoStore baseinfoStore) throws BizCheckedException;

    public String getStoreByStoreNo(String storeNo) throws BizCheckedException;

    public String closeStore(String storeNo) throws BizCheckedException;

    public String removeStore(String storeNo) throws BizCheckedException;

    public String getStoreList() throws BizCheckedException;

    public String countBaseinfoStore() throws BizCheckedException;
}
