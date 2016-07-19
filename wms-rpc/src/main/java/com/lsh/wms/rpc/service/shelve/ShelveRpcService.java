package com.lsh.wms.rpc.service.shelve;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.wms.api.service.shelve.IShelveRpcService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.parsing.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by fengkun on 16/7/15.
 */
public class ShelveRpcService implements IShelveRpcService {
    private static Logger logger = LoggerFactory.getLogger(ShelveRpcService.class);

    @Autowired
    private LocationService locationService;

    public BaseinfoLocation assginShelveLocation() {
        BaseinfoLocation targetLocation = new BaseinfoLocation();
        return targetLocation;
    }
}
