package com.lsh.wms.api.model.stock;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lixin-mac on 16/9/7.
 */
public class StockRequest implements Serializable{
    /**仓库*/
    private String plant;
    /**库存地 0001*/
    private String storageLocation = "0001";
    /**类型551报损552报溢*/
    private String moveType;
    /**移动原因 moveReason 写死0001*/
    private String moveReason = "0001";

    private List<StockItem> items;

    public StockRequest(){}

    public StockRequest(List<StockItem> items, String moveReason, String moveType, String plant, String storageLocation) {
        this.items = items;
        this.moveReason = moveReason;
        this.moveType = moveType;
        this.plant = plant;
        this.storageLocation = storageLocation;
    }

    public List<StockItem> getItems() {
        return items;
    }

    public void setItems(List<StockItem> items) {
        this.items = items;
    }

    public String getMoveReason() {
        return moveReason;
    }

    public void setMoveReason(String moveReason) {
        this.moveReason = moveReason;
    }

    public String getMoveType() {
        return moveType;
    }

    public void setMoveType(String moveType) {
        this.moveType = moveType;
    }

    public String getPlant() {
        return plant;
    }

    public void setPlant(String plant) {
        this.plant = plant;
    }

    public String getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }
}
