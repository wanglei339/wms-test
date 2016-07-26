package com.lsh.wms.model;

import com.lsh.wms.model.taking.StockTakingHead;

import java.util.List;
import java.util.Set;

/**
 * Created by wuhao on 16/7/22.
 */
public class StockTakingInfo {
    StockTakingHead head;
    Set operatorSet;

    public Set getOperatorSet() {
        return operatorSet;
    }

    public void setOperatorSet(Set operatorSet) {
        this.operatorSet = operatorSet;
    }

    public StockTakingHead getHead() {
        return head;
    }

    public void setHead(StockTakingHead head) {
        this.head = head;
    }
}
