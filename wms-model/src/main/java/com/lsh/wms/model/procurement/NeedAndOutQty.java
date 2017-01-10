package com.lsh.wms.model.procurement;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by wuhao on 2017/1/10.
 */
public class NeedAndOutQty implements Serializable {
    /**
     * 是否需要补货
     */
    private boolean needProcurement;
    /**
     * 当前出库数量
     */
    private BigDecimal outQty;

    public boolean isNeedProcurement() {
        return needProcurement;
    }

    public void setNeedProcurement(boolean needProcurement) {
        this.needProcurement = needProcurement;
    }

    public BigDecimal getOutQty() {
        return outQty;
    }

    public void setOutQty(BigDecimal outQty) {
        this.outQty = outQty;
    }

    @Override
    public String toString() {
        return "NeedAndOutQty{" +
                "needProcurement=" + needProcurement +
                ", outQty=" + outQty +
                '}';
    }
}
