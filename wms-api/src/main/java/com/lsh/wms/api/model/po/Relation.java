package com.lsh.wms.api.model.po;

import java.io.Serializable;

/**
 * Created by lixin-mac on 2016/10/18.
 */
public class Relation implements Serializable{
    /** ibd原始订单号 */
    private String ibdOtherId;
    /** ibd细单行项目号 */
    private String ibdDetailId;
    /** obd原始单据号 */
    private String obdOtherId;
    /** obd细单行项目号 */
    private String obdDetailId;

    public Relation(){

    }

    public Relation(String ibdDetailId, String ibdOtherId, String obdDetailId, String obdOtherId) {
        this.ibdDetailId = ibdDetailId;
        this.ibdOtherId = ibdOtherId;
        this.obdDetailId = obdDetailId;
        this.obdOtherId = obdOtherId;
    }

    public String getIbdDetailId() {
        return ibdDetailId;
    }

    public void setIbdDetailId(String ibdDetailId) {
        this.ibdDetailId = ibdDetailId;
    }

    public String getIbdOtherId() {
        return ibdOtherId;
    }

    public void setIbdOtherId(String ibdOtherId) {
        this.ibdOtherId = ibdOtherId;
    }

    public String getObdDetailId() {
        return obdDetailId;
    }

    public void setObdDetailId(String obdDetailId) {
        this.obdDetailId = obdDetailId;
    }

    public String getObdOtherId() {
        return obdOtherId;
    }

    public void setObdOtherId(String obdOtherId) {
        this.obdOtherId = obdOtherId;
    }
}
