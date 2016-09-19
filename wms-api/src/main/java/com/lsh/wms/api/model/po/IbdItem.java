package com.lsh.wms.api.model.po;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by lixin-mac on 16/9/6.
 */
public class IbdItem implements Serializable {
    //上游细单号
    private String poItem;
    //收货行项目号detailOtherId
    private String delivItem;

    //上游商品编码
    private String materialNo;
    //收货数量
    private BigDecimal entryQnt;
    // 包装单位
    private String packName;

    public IbdItem(){}

    public IbdItem(String poItem, String materialNo, BigDecimal entryQnt,String packName,String delivItem) {
        this.poItem = poItem;
        this.materialNo = materialNo;
        this.entryQnt = entryQnt;
        this.packName = packName;
        this.delivItem = delivItem;
    }

    public BigDecimal getEntryQnt() {
        return entryQnt;
    }

    public void setEntryQnt(BigDecimal entryQnt) {
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

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public String getDelivItem() {
        return delivItem;
    }

    public void setDelivItem(String delivItem) {
        this.delivItem = delivItem;
    }
}
