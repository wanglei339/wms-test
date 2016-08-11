package com.lsh.wms.service.wave.split;

import com.lsh.wms.model.wave.WaveDetail;
import java.math.BigDecimal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zengwenjun on 16/7/15.
 */
public class SplitModelBigItem extends SplitModelItem{
    public void split(List<SplitNode> stopNodes) {
        this.itemThreshold = model.getFpmrBigItemThreshold();
        super.split(stopNodes);
    }
}
