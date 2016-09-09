package com.lsh.wms.api.model.po;

import java.io.Serializable;

/**
 * Created by lixin-mac on 16/9/6.
 */
public class IbdItem implements Serializable {
    //上游细单号
    private String poItem;
    //上游商品编码
    private String materialNo;
    //收货数量
    private String entryQnt;

    public IbdItem(){}

    public IbdItem(String poItem, String materialNo, String entryQnt) {
        this.poItem = poItem;
        this.materialNo = materialNo;
        this.entryQnt = entryQnt;
    }

    public String getEntryQnt() {
        return entryQnt;
    }

    public void setEntryQnt(String entryQnt) {
        this.entryQnt = entryQnt;
    }

    public String getMaterialNo() {
        return materialNo;
    }

    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo;
    }

    public String getPoItem() {
        return poItem;
    }

    public void setPoItem(String poItem) {
        this.poItem = poItem;
    }
}
