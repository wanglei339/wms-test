package com.lsh.wms.api.service.baseinfo;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.wms.model.baseinfo.BaseinfoItemLocation;

import java.util.List;

/**
 * Created by lixin-mac on 16/7/14.
 */

@Service(protocol = "dubbo")
public interface IItemLocationRpcService {
    List<BaseinfoItemLocation> getItemLocationList(long iSkuId,long iOwnerId);
    List<BaseinfoItemLocation> getItemLocationByLocationID(long iLocationId);

    BaseinfoItemLocation insertItemLocation(BaseinfoItemLocation itemLocation);
    int updateItemLocation(BaseinfoItemLocation itemLocation);

}
