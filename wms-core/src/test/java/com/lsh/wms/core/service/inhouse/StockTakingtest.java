package com.lsh.wms.core.service.inhouse;

import com.alibaba.fastjson.JSON;
import com.lsh.wms.model.taking.StockTakingHead;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuhao on 16/7/22.
 */
public class StockTakingtest {
    public static void main(String args []) {
        List<StockTakingHead> list=new ArrayList<StockTakingHead>();
        StockTakingHead head =null;
        for(int i=0;i<3;i++) {
            head=new StockTakingHead();
            list.add(head);
        }
        System.out.println(JSON.toJSON(list).toString());
    }
}
