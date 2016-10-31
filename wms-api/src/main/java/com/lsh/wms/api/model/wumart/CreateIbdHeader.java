package com.lsh.wms.api.model.wumart;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.Serializable;
import java.util.List;

/**
 * Created by lixin-mac on 2016/10/28.
 */
public class CreateIbdHeader implements Serializable{

    /**交货时间*/
    private XMLGregorianCalendar deliveDate;

    /**商品明细*/
    private List<CreateIbdDetail> items;

    public CreateIbdHeader(){}

    public CreateIbdHeader(XMLGregorianCalendar deliveDate, List<CreateIbdDetail> items) {
        this.deliveDate = deliveDate;
        this.items = items;
    }

    public List<CreateIbdDetail> getItems() {
        return items;
    }

    public void setItems(List<CreateIbdDetail> items) {
        this.items = items;
    }

    public XMLGregorianCalendar getDeliveDate() {
        return deliveDate;
    }

    public void setDeliveDate(XMLGregorianCalendar deliveDate) {
        this.deliveDate = deliveDate;
    }
}
