package com.lsh.wms.core.service.location.targetlist;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * getList的工厂方法,通过不同条件,实现不同targetList方法输出
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 16/8/11 上午11:01
 */
@Component
public class TargetListFactory {
    private Map<Integer,TargetListHandler> targetListHandlerMap = new HashMap<Integer, TargetListHandler>();
    public void register(Integer listType, TargetListHandler targetListHandler){
        targetListHandlerMap.put(listType,targetListHandler);
    }
    public TargetListHandler getTargetListHandler(Integer listType){
        return targetListHandlerMap.get(listType);
    }
}
