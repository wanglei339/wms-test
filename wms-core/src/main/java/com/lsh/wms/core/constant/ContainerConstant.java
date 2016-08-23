package com.lsh.wms.core.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 16/8/8 下午4:07
 */
public class ContainerConstant {
    // container类型定义
    public static final Long PALLET = 1L;
    public static final Long CAGE = 2L;
    public static final Long TURNOVER_BOX = 3L;
    // TODO 以后放托盘的各项配置
    public static final Map<Long, Map<String, Object>> containerConfigs = new HashMap<Long, Map<String, Object>>() {
        {
            put(1L, new HashMap<String, Object>() { // 托盘
                {
                    put("typeName", "托盘");
                    put("type",1L);
                    put("model","1.2*1.1*0.1m");
                    put("capacity",1000L);
                    put("loadCapacity",2000L);
                    put("description","木托盘1000L容积,称重2t");
                    put("status","1");
                }
            });
            put(2L,new HashMap<String, Object>(){   //笼车
                {
                    put("typeName","笼车");
                    put("type",2L);
                    put("model","1100*800*1700cm");
                    put("capacity",1000L);
                    put("loadCapacity",500L);
                    put("description","笼车1000L容积,称重500KG");
                    put("status","1");
                }
            });
            put(3L,new HashMap<String, Object>(){   //周转箱
                {
                    put("typeName","周转箱");
                    put("type",2L);
                    put("model","555*365*295mm");
                    put("capacity",60L);
                    put("loadCapacity",30L);
                    put("description","周转箱60L容积,称重30KG");
                    put("status","1");
                }
            });
        }
    };
}
