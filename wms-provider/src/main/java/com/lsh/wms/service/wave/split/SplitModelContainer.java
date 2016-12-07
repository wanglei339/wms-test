package com.lsh.wms.service.wave.split;

import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.core.constant.PickConstant;
import com.lsh.wms.core.service.utils.PackUtil;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.wave.WaveDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.math.BigDecimal;

/**
 * Created by zengwenjun on 16/7/15.
 */
public class SplitModelContainer extends SplitModel{
    private static Logger logger = LoggerFactory.getLogger(SplitModelContainer.class);

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
            BigDecimal sumCapacity = new BigDecimal("0");
            for(WaveDetail detail : node.details){
                BaseinfoItem item = mapItems.get(detail.getItemId());
                BigDecimal eaCapacity = item.getWidth().multiply(item.getLength()).multiply(item.getHeight());
                BigDecimal packCapacity = item.getPackWidth().multiply(item.getPackLength()).multiply(item.getPackHeight());
                BigDecimal eachCapacity = detail.getAllocUnitName().equals("EA") ? eaCapacity : packCapacity;
                BigDecimal capacity = eachCapacity.multiply(detail.getAllocUnitQty());
                //先走单品体积逻辑
                if(capacity.compareTo(this.model.getContainerCapacityCm3())>=0
                    || detail.getAllocUnitQty().compareTo(BigDecimal.valueOf(this.model.getContainerUnitCapacity()))>=0){
                    //单商品就装不下了,我日哦
                    BigDecimal containerQty = null;
                    if(capacity.compareTo(this.model.getContainerCapacityCm3())>=0) {
                        containerQty = this.model.getContainerCapacityCm3()
                                .divide(eachCapacity, 0, BigDecimal.ROUND_FLOOR)
                                .multiply(PackUtil.Uom2PackUnit(detail.getAllocUnitName()));
                        logger.info(String.format("item %d[%s] cq %s cm3 %s each %s",
                                item.getItemId(),
                                item.getSkuName(),
                                containerQty.toString(),
                                this.model.getContainerCapacityCm3().toString(),
                                eaCapacity.toString()));
                    }else{
                        containerQty = PackUtil.Uom2PackUnit(detail.getAllocUnitName())
                                .multiply(BigDecimal.valueOf(this.model.getContainerUnitCapacity()));
                    }
                    if(containerQty.compareTo(BigDecimal.ZERO)<=0){
                        containerQty = BigDecimal.valueOf(1L).multiply(PackUtil.Uom2PackUnit(detail.getAllocUnitName()));
                    }
                    int num = detail.getAllocQty().divide(containerQty, 0, BigDecimal.ROUND_FLOOR).intValue();
                    logger.info("num cao "+num+" "+containerQty+" "+detail.getAllocQty());
                    for(int i = 0;i < num;++i){
                        SplitNode newNode = new SplitNode();
                        WaveDetail newDetail = new WaveDetail();
                        ObjUtils.bean2bean(detail, newDetail);
                        newDetail.setAllocQty(containerQty);
                        newDetail.setAllocUnitQty(PackUtil.EAQty2UomQty(containerQty, detail.getAllocUnitName()));
                        newNode.details.add(newDetail);
                        newNode.iPickType = node.iPickType;
                        stopNodes.add(newNode);
                    }
                    if(detail.getAllocQty().compareTo(containerQty.multiply(BigDecimal.valueOf((long)num)))>0){
                        BigDecimal leftQty = detail.getAllocQty().subtract(containerQty.multiply(BigDecimal.valueOf((long)num)));
                        detail.setAllocQty(leftQty);
                        detail.setAllocUnitQty(PackUtil.EAQty2UomQty(leftQty, detail.getAllocUnitName()));
                    }else{
                        continue;
                    }
                }
                capacity = eachCapacity.multiply(detail.getAllocUnitQty());
                if(sumCapacity.add(capacity).compareTo(this.model.getContainerCapacityCm3())>0
                    || sumQty.add(detail.getAllocUnitQty()).compareTo(BigDecimal.valueOf(this.model.getContainerUnitCapacity()))>0){
                    //卧槽,多了,要分开
                    if(newDetails.size()>0) {
                        SplitNode newNode = new SplitNode();
                        newNode.details = newDetails;
                        newNode.iPickType = node.iPickType;
                        stopNodes.add(newNode);
                        newDetails = new LinkedList<WaveDetail>();
                        sumQty = new BigDecimal("0");
                    }
                }
                newDetails.add(detail);
                sumQty = sumQty.add(detail.getAllocUnitQty());
                sumCapacity = sumCapacity.add(capacity);
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
