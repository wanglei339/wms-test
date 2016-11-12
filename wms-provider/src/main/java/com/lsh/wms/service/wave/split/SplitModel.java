package com.lsh.wms.service.wave.split;

import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.pick.PickModel;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by zengwenjun on 16/7/15.
 */


public abstract class SplitModel {
    PickModel model;
    List<SplitNode> oriNodes;
    List<SplitNode> dstNodes;
    Map<Long, BaseinfoItem> mapItems;

    public void init(PickModel model, List<SplitNode> nodes, Map<Long, BaseinfoItem> mapItems){
        oriNodes = nodes;
        dstNodes = new LinkedList<SplitNode>();
        this.model = model;
        this.mapItems = mapItems;
    }

    public abstract void split(List<SplitNode> stopNodes);

    public List<SplitNode> getSplitedNodes(){
        return this.dstNodes;
    }

    protected void skipSplit(){
        this.dstNodes = this.oriNodes;
    }
}
