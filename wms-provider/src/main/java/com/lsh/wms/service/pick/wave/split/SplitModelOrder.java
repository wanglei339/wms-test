package com.lsh.wms.service.pick.wave.split;

import com.lsh.wms.model.pick.PickTaskDetail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zengwenjun on 16/7/15.
 */
public class SplitModelOrder extends SplitModel{
    @Override
    public void split(List<SplitNode> stopNodes) {
        //这个简单,直接按照是否是一个相同的订单进行合并就行了
        for(SplitNode node : this.oriNodes){
            Map<Long, SplitNode> mapOrderNodes = new HashMap<Long, SplitNode>();
            for(PickTaskDetail detail : node.details){
                if(mapOrderNodes.get(detail.getOrderId())==null){
                    mapOrderNodes.put(detail.getOrderId(), new SplitNode());
                }
                mapOrderNodes.get(detail.getOrderId()).details.add(detail);
            }
            for(Long key : mapOrderNodes.keySet()){
                this.dstNodes.add(mapOrderNodes.get(key));
            }
        }
    }

}
