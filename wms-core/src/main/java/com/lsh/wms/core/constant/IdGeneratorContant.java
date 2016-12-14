package com.lsh.wms.core.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fengkun on 16/8/26.
 */
public class IdGeneratorContant {
    public static final Map<String, Integer> PREFIX_CONFIG = new HashMap<String, Integer>(){
        {
            put("task_100", 10); // TYPE_STOCK_TAKING
            put("task_101", 11); // TYPE_PO
            put("task_102", 12); // TYPE_PICK
            put("task_103", 13); // TYPE_SHELVE
            put("task_104", 14); // TYPE_PROCUREMENT
            put("task_105", 15); // TYPE_STOCK_TRANSFER
            put("task_106", 16); // TYPE_ATTIC_SHELVE
            put("task_107", 17); // TYPE_PICK_UP_SHELVE
            put("task_108", 18);
            put("task_109", 19);
            put("task_110", 20); // TYPE_QC
            put("task_111", 21); // TYPE_SHIP
            put("task_112", 22);
            put("task_113", 23);
            put("task_114", 24);
            put("task_115", 25);
            put("task_116", 26);    //TYPE_DIRECT_SHIP

            //uid
            put("uid",99);// uid

            //tuId运单号
            put("tuId", 30);

            //托盘码
            put("containerCode",1);//前缀1

        }
    };

}
