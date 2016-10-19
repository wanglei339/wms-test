package com.lsh.wms.service.merge;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.service.merge.IMergeRpcService;
import com.lsh.wms.core.service.store.StoreService;
import com.lsh.wms.model.baseinfo.BaseinfoStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by fengkun on 2016/10/20.
 */
@Service(protocol = "dubbo")
public class MergeRpcService implements IMergeRpcService {
    private static Logger logger = LoggerFactory.getLogger(MergeRpcService.class);

    @Autowired
    private StoreService storeService;

    public String getMergeList(Map<String, Object> mapQuery) throws BizCheckedException {
        List<BaseinfoStore> stores = storeService.getBaseinfoStoreList(mapQuery);
        for (BaseinfoStore store: stores) {
            String store_no = store.getStoreNo();
        }
        return "";
    };

    public String getMergeCount(Map<String, Object> mapQuery) throws BizCheckedException {
        return "";
    };
}
