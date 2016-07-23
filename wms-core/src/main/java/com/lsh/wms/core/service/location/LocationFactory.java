package com.lsh.wms.core.service.location;

import com.lsh.wms.core.dao.baseinfo.BaseinfoLocationDao;
import com.lsh.wms.core.dao.baseinfo.BaseinfoLocationDockDao;
import com.lsh.wms.core.dao.baseinfo.IBaseinfoLocationDao;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zengwenjun on 16/7/23.
 */
@Component
public class LocationFactory {
    @Autowired
    private BaseinfoLocationDockDao baseinfoLocationDockDao;

    private Map<Long, IBaseinfoLocationDao> handlerMap = new HashMap<Long, IBaseinfoLocationDao>();

    @PostConstruct
    public void init(){
        register(1L,baseinfoLocationDockDao);
    }


    public void register(Long taskType, IBaseinfoLocationDao handler) {
        handlerMap.put(taskType, handler);
    }

    public IBaseinfoLocationDao getTaskHandler(Long taskType) {
        return (IBaseinfoLocationDao)  handlerMap.get(taskType);
    }


}
