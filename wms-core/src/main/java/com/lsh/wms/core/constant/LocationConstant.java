package com.lsh.wms.core.constant;

import java.math.BigDecimal;
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
    public static final Long FLOOR = 7L;
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

    //货架层和阁楼货架层
    public static final Long SHELF_LEVELS = 27L;
    public static final Long LOFT_LEVELS = 28L;
    //返仓区
    public static final Long MARKET_RETURN_AREA = 29L;
    //集货道组和集货道
    public static final Long COLLECTION_ROAD_GROUP = 30L;

    public static final Long COLLECTION_ROAD = 31L;
    //拆零区
    public static final Long SPLIT_AREA = 32L;
    //拆零货架
    public static final Long SPLIT_SHELF = 33L;
    //拆零货架的层
    public static final Long SPLIT_SHELF_LEVEL = 34L;
    //拆零存储一体货位
    public static final Long SPLIT_SHELF_BIN = 35L;

    //货架层的拣货块
    public static final Long SHELF_PICK_BLOCK = 36L; // TODO 在生成层的过程,生成拣货块,不会进行联动
    //货架层的存货块
    public static final Long SHELF_STORE_BLOCK = 37L;
    //阁楼货架层的拣货块
    public static final Long LOFT_PICK_BLOCK = 38L;
    //阁楼货架层的拣货块
    public static final Long LOFT_STORE_BLOCK = 39L;

    //贵品区
    public static final Long VALUABLES_AREA = 40L;      //贵品区
    public static final Long VALUABLES_SHELF = 41L;     //贵品货架个体
    public static final Long VALUABLES_SHELF_LEVEL = 42L;   //贵品货架的货架层
    public static final Long VALUABLES_SHELF_BIN = 43L; //贵品存拣一体位

    //播种区
    public static final Long SOW_AREA = 44L;  //播种区
    public static final Long SOW_BIN = 45L;  //播种货位

    //供商退货区
    public static final Long SUPPLIER_RETURN_AREA = 46L;    //供商退货区
    public static final Long SUPPLIER_RETURN_SHELF = 47L;   //供商退货货架
    public static final Long SUPPLIER_RETURN_LEVEL = 48L;    //供商退货货架层
    public static final Long SUPPLIER_RETURN_IN_BIN = 49L;  //供商退货入库位置(货架一层,方便取货)
    public static final Long SUPPLIER_RETURN_STORE_BIN = 50L;   //供商退货存储位置(一层往上)


    //是否能存储
    public static final Integer CAN_STORE = 1;
    public static final Integer CANNOT_STORE = 0;
    //是否继续使用
    public static final Integer CAN_USE = 1;
    public static final Integer CANNOT_USE = 2;


    //区域(主要是返仓)的货主配置
    public static final Long OWER_WUMARKET = 1L;  //物美
    public static final Long OWER_LSH = 2L;  //链商

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


    //设置库位和库区的区别项classification

    public static final Integer REGION_TYPE = 1; //库区
    public static final Integer BIN_TYPE = 2;    //库位
    public static final Integer OTHER_TYPE = 3; //其他项
    public static final Integer LOFT_SHELF = 4;    //货架和阁楼的架子个体

    //位置是否删除
    public static final Integer IS_VALID = 1;
    public static final Integer NOT_VALID = 0;
    //是否上锁
    public static final Integer IS_LOCKED = 1;
    public static final Integer UNLOCK = 0;
    //关于classification,同属性分类
    public static final Integer CLASSIFICATION_AREAS = 1;
    public static final Integer CLASSIFICATION_BINS = 2;
    public static final Integer CLASSIFICATION_OTHERS = 3;
    public static final Integer CLASSIFICATION_SHELFS = 4;

    //设置location的门店号为删除,即置为0
    public static final String REMOVE_STORE_NO = "0";


    // location_id区域策略
    public static final Integer LOCATION_CAN_ADD = 1; //能追加存入货位

    //与获取的targetList的方法相关
    public static final Integer LIST_TYPE_AREA = 1; //获取大区的list方法
    public static final Integer LIST_TYPE_DOMAIN = 2;   //获取功能list方法
    public static final Integer LIST_TYPE_PASSAGE = 3;   //获取通道list方法
    public static final Integer LIST_TYPE_SHELFREGION = 4;   //获取货架区和阁楼区的list方法
    public static final Integer LIST_TYPE_SHELF = 5; //获取货架和阁楼的list方法

    // location_id划分相关
    public static final Integer CHILDREN_RANGE = 128; //每个节点的子节点数
    public static final Integer LOCATION_LEVEL = 8; // 整棵树的最大层数



    public static final Map<Long, String> LOCATION_TYPE_NAME = new HashMap<Long, String>() {
        {
            put(27L, "货架层");
            put(28L, "阁楼层");
            put(34L, "拆零货架层");
            put(42L, "贵品货架层");
            put(48L, "退货货架层");
            put(36L, "货架层拣货块");
            put(37L, "货架层存货块");
            put(38L, "阁楼货架层拣货块");
            put(39L, "阁楼货架层存货块");

        }
    };



    /**
     * todo 库区库位的配置 初始化使用
     */
    public static final Map<Long, Map<String, Object>> LOCATION_CONFIGS = new HashMap<Long, Map<String, Object>>() {
        {
            //虚拟区
            put(REGION_AREA, new HashMap<String, Object>() {
                {
                    put("length", new BigDecimal("300.00"));
                    put("width", new BigDecimal("300.00"));
                    put("type", REGION_AREA);
                    put("isValid", 1);
                    put("typeName", "虚拟区");
                    //ownerid自己设置
                }
            });
            //通道
            put(PASSAGE, new HashMap<String, Object>() {
                {
                    put("type", PASSAGE);
                    put("length", new BigDecimal("50.00"));
                    put("width", new BigDecimal("2.00"));
                    put("direction", PASSAGE_EAST_WEST);
                    put("typeName", "通道");
                    put("isValid", 1);
                }
            });

            //获取货架
            put(SHELF, new HashMap<String, Object>() {
                {
                    put("depth", 1L);//一层深度
                    put("type", SHELF);
                    put("typeName", "货架区货架");
                    put("isValid", 1);
                }
            });
            //阁楼货架
            put(LOFT, new HashMap<String, Object>() {
                {
                    put("depth", 1l);   //一层深度
                    put("type", LOFT);
                    put("typeName", "阁楼区货架");
                    put("isValid", 1);
                }
            });

            //拆零货架
            put(SPLIT_SHELF, new HashMap<String, Object>() {
                {
                    put("depth", 1l);   //一层深度
                    put("type", SPLIT_SHELF);
                    put("type", SPLIT_AREA);
                    put("typeName", "拆零区货架");
                    put("isValid", 1);
                }
            });


            //贵品区货架
            put(VALUABLES_SHELF, new HashMap<String, Object>() {
                {
                    put("depth", 1l);   //一层深度
                    put("type", VALUABLES_SHELF);
                    put("regionNo", VALUABLES_AREA);
                    put("typeName", "贵品区货架");
                    put("isValid", 1);
                }
            });
            //供商直流退货区货架
            put(SUPPLIER_RETURN_SHELF, new HashMap<String, Object>() {
                {
                    put("depth", 1l);   //一层深度
                    put("type", SUPPLIER_RETURN_SHELF);
                    put("regionNo", SHELFS);
                    put("typeName", "供商直流退货货架");
                    put("isValid", 1);
                }
            });
            //货架层拣货块
            put(SHELF_PICK_BLOCK, new HashMap<String, Object>() {
                {
                    put("type", SHELF_PICK_BLOCK);
                    put("typeName", "货架层拣货块");
                    put("regionNo", SHELFS);
                    put("isValid", 1);
                }
            });
            //货架拣货位
            put(SHELF_PICKING_BIN, new HashMap<String, Object>() {
                {
                    put("type", SHELF_PICKING_BIN);
                    put("typeName", "货架拣货位");
                    put("fatherType", SHELF_PICK_BLOCK);
                    put("regionNo", SHELFS);
                    put("regionCode","A");
                    put("isValid", 1);
                }
            });
            //货架层存货块
            put(SHELF_STORE_BLOCK, new HashMap<String, Object>() {
                {
                    put("type", SHELF_STORE_BLOCK);
                    put("typeName", "货架层存货块");
                    put("regionCode","A");
                    put("regionNo", SHELFS);
                    put("isValid", 1);
                }
            });
            //货架存货位
            put(SHELF_STORE_BIN, new HashMap<String, Object>() {
                {
                    put("type", SHELF_STORE_BIN);
                    put("typeName", "货架存货位");
                    put("fatherType", SHELF_STORE_BLOCK);
                    put("regionCode","A");
                    put("regionNo", SHELFS);
                    put("isValid", 1);
                }
            });
            //阁楼货架层的拣货块
            put(LOFT_PICK_BLOCK, new HashMap<String, Object>() {
                {
                    put("type", LOFT_PICK_BLOCK);
                    put("typeName", "阁楼货架层的拣货块");
                    put("regionCode","B");
                    put("regionNo", LOFTS);
                    put("isValid", 1);
                }
            });
            //阁楼的拣货位
            put(LOFT_PICKING_BIN, new HashMap<String, Object>() {
                {
                    put("type", LOFT_PICKING_BIN);
                    put("typeName", "阁楼的拣货位");
                    put("fatherType", LOFT_PICK_BLOCK);
                    put("regionCode","B");
                    put("regionNo", LOFTS);
                    put("isValid", 1);
                }
            });
            //阁楼货架层的存货块
            put(LOFT_STORE_BLOCK, new HashMap<String, Object>() {
                {
                    put("type", LOFT_STORE_BLOCK);
                    put("typeName", "阁楼货架层的存货块");
                    put("regionCode","B");
                    put("regionNo", LOFTS);
                    put("isValid", 1);
                }
            });
            //阁楼的拣货位
            put(LOFT_STORE_BIN, new HashMap<String, Object>() {
                {
                    put("type", LOFT_STORE_BIN);
                    put("typeName", "阁楼的存货位");
                    put("fatherType", LOFT_STORE_BLOCK);
                    put("regionNo", LOFTS);
                    put("regionCode","B");
                    put("isValid", 1);
                }
            });




        }
    };


}
