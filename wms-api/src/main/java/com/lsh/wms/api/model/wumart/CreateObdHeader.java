package com.lsh.wms.api.model.wumart;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.Serializable;
import java.util.List;

/**
 * Created by lixin-mac on 2016/10/28.
 */
public class CreateObdHeader implements Serializable{

    /**交货时间*/
    private XMLGregorianCalendar dueDate;

    /**商品明细*/
    private List<CreateObdDetail> items;

    public CreateObdHeader(){}

    public CreateObdHeader(XMLGregorianCalendar dueDate, List<CreateObdDetail> items) {
        this.dueDate = dueDate;
        this.items = items;
    }

    public XMLGregorianCalendar getDueDate() {
        return dueDate;
    }

    public void setDueDate(XMLGregorianCalendar dueDate) {
        this.dueDate = dueDate;
    }

    public List<CreateObdDetail> getItems() {
        return items;
    }

    public void setItems(List<CreateObdDetail> items) {
        this.items = items;
    }
}
