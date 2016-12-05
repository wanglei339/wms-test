package com.lsh.wms.service.wave.split;

import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.core.constant.PickConstant;
import com.lsh.wms.core.service.utils.PackUtil;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.wave.WaveDetail;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zengwenjun on 16/11/11.
 */
public class SplitModelWholePackage extends SplitModel{
    @Override
    public void split(List<SplitNode> stopNodes) {
        if(model.getStrategyWholePackage() != 1){
            this.skipSplit();
            return;
        }
        for(SplitNode node : this.oriNodes) {
            List<WaveDetail> details = new ArrayList<WaveDetail>();
            for (WaveDetail detail : node.details) {
                //如果单品的数量大于整托的数量,则直接拆出来搞起
                BaseinfoItem item = mapItems.get(detail.getItemId());
                final BigDecimal packageQty = item.getPackUnit();

                if (packageQty.compareTo(BigDecimal.ZERO)>0
                        && detail.getAllocUnitName() == "EA"
                        && detail.getAllocQty().compareTo(packageQty) >= 0) {
                    int num = detail.getAllocQty().divide(packageQty, 0, BigDecimal.ROUND_FLOOR).intValue();
                    BigDecimal newQty = packageQty.multiply(BigDecimal.valueOf(num));
                    if(num > 0){
                        SplitNode newNode = new SplitNode();
                        WaveDetail newDetail = new WaveDetail();
                        ObjUtils.bean2bean(detail, newDetail);
                        newDetail.setAllocQty(newQty);
                        newDetail.setAllocUnitName(PackUtil.PackUnit2Uom(packageQty, "EA"));
                        newDetail.setAllocUnitQty(BigDecimal.valueOf(num));
                        newNode.details.add(newDetail);
                        //newNode.iPickType = PickConstant.SPLIT_LOFT_PACKAGE_TASK_TYPE;//呵呵,找日的代码.
                        stopNodes.add(newNode);
                    }
                    if(detail.getAllocQty().compareTo(newQty)>0){
                        BigDecimal leftQty = detail.getAllocQty().subtract(newQty);
                        detail.setAllocQty(leftQty);
                        detail.setAllocUnitQty(PackUtil.EAQty2UomQty(leftQty, detail.getAllocUnitName()));
                        details.add(detail);
                    }
                }else{
                    details.add(detail);
                }
            }
            if(details.size()>0){
                node.details = details;
                this.dstNodes.add(node);
            }
        }
    }
}

