package com.lsh.wms.api.service.system;

import java.util.Map;

/**
 * Created by zhanghongling on 16/11/10.
 */
public interface IItemTypeRestService {
    String getItemTypeList(Map<String, Object> mapQuery);
    String  getItemTypeListCount(Map<String, Object> mapQuery);
}
