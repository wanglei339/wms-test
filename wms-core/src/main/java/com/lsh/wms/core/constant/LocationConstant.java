package com.lsh.wms.core.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 16/7/29 下午2:55
 */
public class LocationConstant {
    public static final Long WAREHOUSE = 1L;
    public static final Long REGION_AREA = 2L;
    public static final Long PASSAGE = 3L;
    //各区域
    public static final Long INVENTORYLOST = 4L;    //盘亏盘盈区
    public static final Long SHELFS = 5L;     //货架区
    public static final Long LOFTS = 6L;       //阁楼区(指的是阁楼的一层为一个阁楼区,有四层就是四个阁楼区)
    //功能区
    public static final Long FLOOR = 7l;
    public static final Long TEMPORARY = 8L;
    public static final Long COLLECTION_AREA = 9L; //集货区
    public static final Long BACK_AREA = 10L;
    public static final Long DEFECTIVE_AREA = 11L;
    public static final Long DOCK_AREA = 12L;
    //货架和阁楼隶属货架区,阁楼区
    public static final Long SHELF = 13L;
    public static final Long LOFT = 14L;
    //所有的货位
    public static final Long BIN = 15L;
    //所有的货位
    //货架拣货位和存货位
    public static final Long SHELF_PICKING_BIN = 16L;
    public static final Long SHELF_STORE_BIN = 17L;
    //阁楼拣货位和存货位
    public static final Long LOFT_PICKING_BIN = 18L;
    public static final Long LOFT_STORE_BIN = 19L;
    //功能区的货位
    public static final Long FLOOR_BIN = 20L;
    public static final Long TEMPORARY_BIN = 21L;
    public static final Long COLLECTION_BIN = 22L;  //集货货位
    public static final Long BACK_BIN = 23L;
    public static final Long DEFECTIVE_BIN = 24L;
    //虚拟货区
    public static final Long CONSUME_AREA = 25L;
    public static final Long SUPPLIER_AREA = 26L;

    //货架层和阁楼层
    public static final Long SHELF_LEVELS = 27L;
    public static final Long LOFT_LEVELS = 28L;
    //返仓区
    public static final Long MARKET_RETURN_AREA = 29L;
    //区域(主要是返仓)的货主配置
    public static final Long OWER_WUMARKET=1L;  //物美
    public static final Long OWER_LSH=2L;  //链商

    //温区设置
    public static final Integer ROOM_TEMPERATURE = 1;
    public static final Integer LOW_TEMPERATURE = 2;

    //通道的位置设置
    public static final Integer PASSAGE_EAST_WEST = 1;//东西走向
    public static final Integer PASSAGE_NORTH_SOUTH = 2;//南北走向

    //码头的位置设置
    public static final Integer DOCK_EAST = 1;//东
    public static final Integer DOCK_SOUTH = 2;//南
    public static final Integer DOCK_WEST = 3;//西
    public static final Integer DOCK_NORTH = 4;//北
    //码头出库入库设置
    public static final Integer DOCK_IN = 1; //入库码头
    public static final Integer DOCK_OUT = 2; //出库码头

    //设置库位和库区的区别项

    public static final Integer REGION_TYPE = 1; //库区
    public static final Integer BIN_TYPE = 2;    //库位
    public static final Integer OTHER_TYPE = 3; //其他项
    public static final Integer LOFT_SHELF= 4;    //货架和阁楼的架子个体

    //位置是否删除
    public static final Integer IS_VALID = 1;
    public static final Integer NOT_VALID = 0;

    public static final Map<String, Long> LOCATION_TYPE = new HashMap<String, Long>() {
        {
            put("warehouse", new Long(1)); // 1仓库
            put("area", new Long(2)); // 2区域
            put("passage", new Long(3));   //3通道
            //各区域
            put("inventoryLost", new Long(4));    //4盘盈盘亏
            put("shelf_area", new Long(5)); //5货架区
            put("loft_area", new Long(6)); //6阁楼区
            put("floor", new Long(7)); // 7 地堆区
            put("temporary", new Long(8)); // 8 暂存区
            put("collection_area", new Long(9)); // 9 集货区
            put("back_area", new Long(10)); // 10 退货区
            put("defective_area", new Long(11)); // 11残次区
            put("dock_area", new Long(12)); // 12 码头区

            //货架和阁楼隶属货架区,阁楼区
            put("shelf", new Long(13));  //13货架(个体)
            put("loft", new Long(14));   //14阁楼(个体)

            //所有的货位
            put("bin", new Long(15)); // 15 所有的货位(存有货架|阁楼|区域的id)
            //所有的货位
            put("shelf_collection_bin", new Long(16)); //16货架拣货位
            put("shelf_store_bin", new Long(17));   //17 货架存货位货位
            put("loft_collection_bin", new Long(18)); //18阁楼拣货位
            put("loft_store_bin", new Long(19));   //19阁楼存货位

    
            /*其他功能区的货位*/
            put("floor_bin", new Long(20)); // 20 地堆货位
            put("temporary_bin", new Long(21)); // 21 暂存货位
            put("collection_bin", new Long(22)); // 22 集货货位
            put("back_bin", new Long(23)); // 23 退货货位
            put("defective_bin", new Long(24));// 24 残次货位
            put("consume_area", new Long(25));// 25 消费区
            put("supplier_area", new Long(26));// 26 供应区
            put("shelf_levels", new Long(27));// 27 货架层
            put("loft_levels", new Long(28));// 28 阁楼层
        }
    };

    // location_id划分相关
    public static final Integer CHILDREN_RANGE = 128; //每个节点的子节点数
    public static final Integer LOCATION_LEVEL = 8; // 整棵树的最大层数

    //与获取的targetList的方法相关
    public static final Integer LIST_TYPE_AREA = 1; //获取大区的list方法
    public static final Integer LIST_TYPE_DOMAIN = 2;   //获取功能list方法
    public static final Integer LIST_TYPE_PASSAGE = 3;   //获取通道list方法
    public static final Integer LIST_TYPE_SHELFREGION = 4;   //获取货架区和阁楼区的list方法
    public static final Integer LIST_TYPE_SHELF = 5; //获取货架和阁楼的list方法

}
