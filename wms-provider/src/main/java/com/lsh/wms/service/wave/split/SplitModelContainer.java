package com.lsh.wms.service.wave.split;

import com.lsh.wms.model.wave.WaveDetail;

import java.util.LinkedList;
import java.util.List;
import java.math.BigDecimal;

/**
 * Created by zengwenjun on 16/7/15.
 */
public class SplitModelContainer extends SplitModel{
    @Override
    public void split(List<SplitNode> stopNodes) {
        //判断单个container能容纳的商品数量,这个商品数量和基本单位怎么转换呢?你妈了个大爷的
        //按照如下规则分列
        // 1. 符合容纳限制的最小份数, 没做
        // 2. 相同商品尽量放在同一个里面, 没做
        // 3. 尽量均匀的分布, 没做
        // 4. 和最后的合并算法一起考虑,对路径优化有一定的提升, 没做
        for(SplitNode node : this.oriNodes){
            List<WaveDetail> newDetails = new LinkedList<WaveDetail>();
            BigDecimal sumQty = new BigDecimal("0");
            for(WaveDetail detail : node.details){
                newDetails.add(detail);
                sumQty = sumQty.add(detail.getAllocUnitQty()); // 这TM的是ea的,草勒,看来还得存个捡货单元单位量
                if(sumQty.compareTo(BigDecimal.valueOf(this.model.getContainerUnitCapacity()))>0){
                    //卧槽,多了,要分开
                    SplitNode newNode = new SplitNode();
                    newNode.details = newDetails;
                    newNode.iPickType = node.iPickType;
                    stopNodes.add(newNode);
                    newDetails = new LinkedList<WaveDetail>();
                    sumQty = new BigDecimal("0");
                }
            }
            if(newDetails.size()>0){
                SplitNode newNode = new SplitNode();
                newNode.details = newDetails;
                newNode.iPickType = node.iPickType;
                stopNodes.add(newNode);
            }
        }
    }
}
