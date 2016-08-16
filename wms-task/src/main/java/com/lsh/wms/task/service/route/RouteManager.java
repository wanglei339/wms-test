package com.lsh.wms.task.service.route;

import com.lsh.wms.model.task.TaskTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by mali on 16/8/16.
 */
@Component
public class RouteManager {

    public List<TaskTrigger> getRoute(String key) {
        return RouteCache.getInstance().getRoute(key);
    }
}
