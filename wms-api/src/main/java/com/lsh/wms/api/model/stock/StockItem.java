package com.lsh.wms.api.model.stock;

import java.io.Serializable;

/**
 * Created by lixin-mac on 16/9/7.
 */
public class StockItem implements Serializable{

    /**上游商品编码*/
    private String materialNo;
    /**数量*/
    private String entryQnt;
    /**单位*/
    private String entryUom;

    public String getEntryQnt() {
        return entryQnt;
    }

    public void setEntryQnt(String entryQnt) {
        this.entryQnt = entryQnt;
    }

    public String getEntryUom() {
        return entryUom;
    }

    public void setEntryUom(String entryUom) {
        this.entryUom = entryUom;
    }

    public String getMaterialNo() {
        return materialNo;
    }

    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo;
    }
}
