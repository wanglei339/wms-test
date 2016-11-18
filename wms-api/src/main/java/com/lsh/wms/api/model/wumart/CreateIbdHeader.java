package com.lsh.wms.api.model.wumart;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.Serializable;
import java.util.List;

/**
 * Created by lixin-mac on 2016/10/28.
 */
public class CreateIbdHeader implements Serializable{
//
//    /**交货时间*/
//    private XMLGregorianCalendar deliveDate;
    /**仓库编码*/
    private String warehouseCode = "";

    /**商品明细*/
    private List<CreateIbdDetail> items;

    public CreateIbdHeader(){}

    public CreateIbdHeader(List<CreateIbdDetail> items, String warehouseCode) {
        this.items = items;
        this.warehouseCode = warehouseCode;
    }

    public List<CreateIbdDetail> getItems() {
        return items;
    }

    public void setItems(List<CreateIbdDetail> items) {
        this.items = items;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }
}
