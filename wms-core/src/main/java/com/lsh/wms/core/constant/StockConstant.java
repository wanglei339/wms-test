package com.lsh.wms.core.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mali on 16/12/6.
 */

public class StockConstant {
    public final static Map<Long, String> REGION_TO_FIELDS = new HashMap() {
        {
            put(LocationConstant.SO_INBOUND_AREA, "allocQty");
            put(LocationConstant.SO_DIRECT_AREA, "presaleQty");
            put(LocationConstant.INVENTORYLOST, "inventoryLossQty");
            put(LocationConstant.SHELFS, "shelfQty");
            put(LocationConstant.SPLIT_AREA, "splitQty");
            put(LocationConstant.LOFTS, "atticQty");
            put(LocationConstant.FLOOR, "floorQty");
            put(LocationConstant.TEMPORARY, "temporaryQty");
            put(LocationConstant.COLLECTION_AREA, "collectionQty");
            put(LocationConstant.BACK_AREA, "backQty");
            put(LocationConstant.DEFECTIVE_AREA, "defectQty");
            put(LocationConstant.DOCK_AREA, "dockQty");
            put(LocationConstant.MARKET_RETURN_AREA, "marketReturnQty");
            put(LocationConstant.SOW_AREA, "sowQty");
            put(LocationConstant.SUPPLIER_RETURN_AREA, "supplierReturnQty");
            put(LocationConstant.DIFF_AREA, "diffQty");
            put(LocationConstant.CONSUME_AREA, "consumeQty");
            put(LocationConstant.SUPPLIER_AREA, "supplierQty");
        }
    };
}
