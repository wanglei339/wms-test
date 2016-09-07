package com.lsh.wms.api.model.po;

import java.io.Serializable;

/**
 * Created by lixin-mac on 16/9/6.
 */
public class Header implements Serializable {
    private String plant;
    private String poNumber;

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
}
