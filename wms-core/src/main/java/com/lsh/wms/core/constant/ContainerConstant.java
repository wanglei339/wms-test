package com.lsh.wms.core.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 16/8/8 下午4:07
 */
public class ContainerConstant {
    // container类型定义
    // TODO 以后放托盘的各项配置
    public static final Map<Long, Map<String, Object>> containerConfigs = new HashMap<Long, Map<String, Object>>() {
        {
            put(1L, new HashMap<String, Object>() { // 托盘
                {
                    put("typeName", "托盘");
                }
            });
            put(2L,new HashMap<String, Object>(){   //笼车
                {
                    put("typeName","笼车");
                }
            });
            put(3L,new HashMap<String, Object>(){   //周转箱
                {
                    put("typeName","周转箱");
                }
            });
        }
    };
}
