package com.lsh.wms.service.pick.wave.split;

import com.lsh.wms.model.pick.PickModel;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by zengwenjun on 16/7/15.
 */


public abstract class SplitModel {
    PickModel model;
    List<SplitNode> oriNodes;
    List<SplitNode> dstNodes;

    public void init(PickModel model, List<SplitNode> nodes){
        oriNodes = nodes;
        dstNodes = new LinkedList<SplitNode>();
        this.model = model;
    }

    public abstract void split(List<SplitNode> stopNodes);

    public List<SplitNode> getSplitedNodes(){
        return this.dstNodes;
    }

    protected void skipSplit(){
        this.dstNodes = this.oriNodes;
    }
}
