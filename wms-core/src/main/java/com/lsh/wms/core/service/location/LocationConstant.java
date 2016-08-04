package com.lsh.wms.core.service.location;

/**
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 16/7/29 下午2:55
 */
public class LocationConstant {
    public static final Long Warehouse = 1L;
    public static final Long Region_area = 2L;
    public static final Long Passage = 3L;
    //各区域
    public static final Long InventoryLost = 4L;    //盘亏盘盈区
    public static final Long Shelfs = 5L;     //货架区
    public static final Long Lofts = 6L;       //阁楼区
    //功能区
    public static final Long Floor = 7l;
    public static final Long Temporary = 8L;
    public static final Long Collection_area = 9L;
    public static final Long Back_area = 10L;
    public static final Long Defective_area = 11L;
    public static final Long Dock_area = 12L;
    //货架和阁楼隶属货架区,阁楼区
    public static final Long Shelf = 13L;
    public static final Long Loft = 14L;
    //所有的货位
    public static final Long Bin = 15L;
    //所有的货位
    //货架拣货位和存货位
    public static final Long Shelf_collection_bin = 16L;
    public static final Long Shelf_store_bin = 17L;
    //阁楼拣货位和存货位
    public static final Long Loft_collection_bin = 18L;
    public static final Long Loft_store_bin = 19L;
    //功能区的货位
    public static final Long Floor_bin = 20L;
    public static final Long Temporary_bin = 21L;
    public static final Long Collection_bin = 22L;
    public static final Long Back_bin = 23L;
    public static final Long Defective_bin = 24L;
    //虚拟货区
    public static final Long Consume_area = 25L;
    public static final Long Supplier_area = 25L;


    //温区设置
    public static final Integer RoomTemperature = 1;
    public static final Integer LowTemperature = 2;

    //通道的位置设置
    public static final Integer PassageEastWest = 1;//东西走向
    public static final Integer PassageNorthSouth = 2;//南北走向

    //码头的位置设置
    public static final Integer DockEast = 1;//东
    public static final Integer DockSouth = 2;//南
    public static final Integer DockWest = 3;//西
    public static final Integer DockNorth = 4;//北
    //码头出库入库设置
    public static final Integer DockIn = 1; //入库码头
    public static final Integer DockOut = 2; //出库码头


}
