package com.lsh.wms.model.stock;

import java.io.Serializable;

/**
 * Created by wuhao on 16/7/28.
 */
public class ItemAndSupplierRelation implements Serializable {
    Long itemId;
    Long supplierId;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }
}
