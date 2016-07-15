package com.lsh.wms.api.service.item;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.wms.model.baseinfo.BaseinfoItemLocation;

import java.util.List;

/**
 * Created by lixin-mac on 16/7/14.
 */
@Service(protocol = "rest")
public interface IItemLocationRestService {

    String getItemLocation(long iSkuId,long iOwnerId);
    String getItemLocationByLocationID(long iLocationId);
    String insertItemLocation(BaseinfoItemLocation itemLocation);
    String updateItemLocation(BaseinfoItemLocation itemLocation);
}
