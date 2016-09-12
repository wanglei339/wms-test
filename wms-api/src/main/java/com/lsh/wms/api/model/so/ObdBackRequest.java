package com.lsh.wms.api.model.so;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lixin-mac on 16/9/8.
 */
public class ObdBackRequest implements Serializable {
    /**业务方订单 ID,原始订单 ID orderOtherId*/
    private String businessId;
    /**OFC 订单 ID orderOtherRefId*/
    private String ofcId;
    /**工厂(DC37)*/
    private String plant;
    /**客户Id*/
    private String agPartnNumber;

    private List<ObdItem> items;

    public ObdBackRequest(){}

    public ObdBackRequest(String agPartnNumber, String businessId, List<ObdItem> items, String ofcId, String plant) {
        this.agPartnNumber = agPartnNumber;
        this.businessId = businessId;
        this.items = items;
        this.ofcId = ofcId;
        this.plant = plant;
    }

    public String getAgPartnNumber() {
        return agPartnNumber;
    }

    public void setAgPartnNumber(String agPartnNumber) {
        this.agPartnNumber = agPartnNumber;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getOfcId() {
        return ofcId;
    }

    public void setOfcId(String ofcId) {
        this.ofcId = ofcId;
    }

    public String getPlant() {
        return plant;
    }

    public void setPlant(String plant) {
        this.plant = plant;
    }

    public List<ObdItem> getItems() {
        return items;
    }

    public void setItems(List<ObdItem> items) {
        this.items = items;
    }
}
