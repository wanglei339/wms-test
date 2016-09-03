package com.lsh.wms.core.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fengkun on 16/8/26.
 */
public class IdGeneratorContant {
    public static final Map<String, Integer> PREFIX_CONFIG = new HashMap<String, Integer>(){
        {
            put("task", 1);
            put("wave", 2);
        }
    };

}
