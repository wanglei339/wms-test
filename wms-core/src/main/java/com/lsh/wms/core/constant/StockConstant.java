package com.lsh.wms.core.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mali on 16/12/6.
 */

public class StockConstant {
    public final static Map<Long, String> REGION_TO_FIELDS = new HashMap() {
        {
            put(LocationConstant.SO_INBOUND_AREA, "alloc_qty");
            put(LocationConstant.SO_DIRECT_AREA, "presale_qty");
            put(LocationConstant.INVENTORYLOST, "inventory_loss_qty");
            put(LocationConstant.SHELFS, "shelf_qty");
            put(LocationConstant.SPLIT_AREA, "split_qty");
            put(LocationConstant.LOFTS, "attic_qty");
            put(LocationConstant.FLOOR, "floor_qty");
            put(LocationConstant.TEMPORARY, "temporary_qty");
            put(LocationConstant.COLLECTION_AREA, "collection_qty");
            put(LocationConstant.BACK_AREA, "back_qty");
            put(LocationConstant.DEFECTIVE_AREA, "defect_qty");
            put(LocationConstant.DOCK_AREA, "dock_qty");
            put(LocationConstant.MARKET_RETURN_AREA, "market_return_qty");
            put(LocationConstant.SOW_AREA, "sow_qty");
            put(LocationConstant.SUPPLIER_RETURN_AREA, "supplier_return_qty");
            put(LocationConstant.DIFF_AREA, "diff_qty");
            put(LocationConstant.CONSUME_AREA, "consume_qty");
            put(LocationConstant.SUPPLIER_AREA, "supplier_qty");
        }
    };
}
