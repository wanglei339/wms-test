package com.lsh.wms.api.model.wumart;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.Serializable;
import java.util.List;

/**
 * Created by lixin-mac on 2016/10/28.
 */
public class CreateObdHeader implements Serializable{

//    /**交货时间*/
//    private XMLGregorianCalendar dueDate;
    /**
     * 订单号
     */
    private String orderOtherId;
    /**仓库编码*/
    private String warehouseCode = "";

    /**运单的tuId
     */
    private String tuId = "";

    /**商品明细*/
    private List<CreateObdDetail> items;

    public CreateObdHeader(){}

    public CreateObdHeader(List<CreateObdDetail> items, String orderOtherId, String tuId, String warehouseCode) {
        this.items = items;
        this.orderOtherId = orderOtherId;
        this.tuId = tuId;
        this.warehouseCode = warehouseCode;
    }

    public List<CreateObdDetail> getItems() {
        return items;
    }

    public void setItems(List<CreateObdDetail> items) {
        this.items = items;
    }

    public String getOrderOtherId() {
        return orderOtherId;
    }

    public void setOrderOtherId(String orderOtherId) {
        this.orderOtherId = orderOtherId;
    }

    public String getTuId() {
        return tuId;
    }

    public void setTuId(String tuId) {
        this.tuId = tuId;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }
}
