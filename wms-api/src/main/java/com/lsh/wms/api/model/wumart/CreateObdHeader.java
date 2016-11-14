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

    /**商品明细*/
    private List<CreateObdDetail> items;

    public CreateObdHeader(){}

    public CreateObdHeader(List<CreateObdDetail> items, String orderOtherId) {
        this.items = items;
        this.orderOtherId = orderOtherId;
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
}
