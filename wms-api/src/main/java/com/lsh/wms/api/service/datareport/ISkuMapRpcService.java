package com.lsh.wms.api.service.datareport;

import java.util.List;

/**
 * Created by lixin-mac on 2016/12/8.
 */
public interface ISkuMapRpcService {
    void insertSkuMap(List<String> skuCode);

    void insertSkuMapFromErp(List<String> skuCodes);
}
