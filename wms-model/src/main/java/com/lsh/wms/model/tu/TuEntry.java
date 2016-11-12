package com.lsh.wms.model.tu;

import java.util.List;

/**
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 2016/11/12 下午6:06
 */
public class TuEntry {
    /**
     * tu的head头
     */
    private TuHead tuHead;
    /**
     * tuDetail的list
     */
    private List<TuDetail> tuDetails;

    public TuHead getTuHead() {
        return tuHead;
    }

    public List<TuDetail> getTuDetails() {
        return tuDetails;
    }

    public void setTuHead(TuHead tuHead) {
        this.tuHead = tuHead;
    }

    public void setTuDetails(List<TuDetail> tuDetails) {
        this.tuDetails = tuDetails;
    }
}
