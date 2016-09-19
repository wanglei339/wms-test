package com.lsh.wms.api.model.po;

import java.io.Serializable;

/**
 * Created by lixin-mac on 16/9/6.
 */
public class Header implements Serializable {
    //仓库
    private String plant;
    //采购凭证号 orderOtherId
    private String poNumber;

    //delivNumber  orderOtherRefId
    private String delivNumber;

    //R2POS的单据类型
    private String docType;
    //R2POS的单据类型,即供应商订单、门店订单、DC订单
    private String docStyle;

    public Header(){}

    public Header(String docStyle, String docType, String plant, String poNumber,String delivNumber) {
        this.docStyle = docStyle;
        this.docType = docType;
        this.plant = plant;
        this.poNumber = poNumber;
        this.delivNumber = delivNumber;
    }

    public String getDocStyle() {
        return docStyle;
    }

    public void setDocStyle(String docStyle) {
        this.docStyle = docStyle;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getPlant() {
        return plant;
    }

    public void setPlant(String plant) {
        this.plant = plant;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public String getDelivNumber() {
        return delivNumber;
    }

    public void setDelivNumber(String delivNumber) {
        this.delivNumber = delivNumber;
    }
}
