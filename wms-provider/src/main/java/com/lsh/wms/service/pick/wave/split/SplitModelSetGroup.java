package com.lsh.wms.service.pick.wave.split;

import java.util.List;

/**
 * Created by zengwenjun on 16/7/15.
 */
public class SplitModelSetGroup extends SplitModel{
    @Override
    public void split(List<SplitNode> stopNodes) {
        //判断单个container能容纳的商品数量,这个商品数量和基本单位怎么转换呢?你妈了个大爷的
        //按照如下规则分列
        // 1. 符合容纳限制的最小份数
        // 2. 相同商品尽量放在同一个里面
        // 3. 尽量均匀的分布
        // 4. 和最后的合并算法一起考虑,对路径优化有一定的提升
        for (SplitNode node : this.oriNodes) {
            stopNodes.add(node);
        }
    }
}
