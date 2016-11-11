package com.lsh.wms.rpc.service.system;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.wms.api.service.system.IItemTypeRpcService;
import com.lsh.wms.core.service.baseinfo.ItemTypeService;
import com.lsh.wms.model.baseinfo.BassinfoItemType;
import com.lsh.wms.model.baseinfo.BassinfoItemTypeRelation;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by zhanghongling on 16/11/10.
 */
@Service(protocol = "dubbo")
public class ItemTypeRpcService implements IItemTypeRpcService {
    @Autowired
    private ItemTypeService itemTypeService;

    public  void insertItemType(BassinfoItemType bassinfoItemType){
        itemTypeService.insertItemType(bassinfoItemType);
    }

    public  void updateItemType(BassinfoItemType bassinfoItemType){
        itemTypeService.updateItemType(bassinfoItemType);
    }

    public BassinfoItemType getBassinfoItemTypeById(Integer id){
        return itemTypeService.getBassinfoItemTypeById(id);
    }

    public void insertItemTypeRelation(BassinfoItemTypeRelation bassinfoItemTypeRelation){
        itemTypeService.insertItemTypeRelation(bassinfoItemTypeRelation);
    }

    public void updateItemTypeRelation(BassinfoItemTypeRelation bassinfoItemTypeRelation){
        itemTypeService.insertItemTypeRelation(bassinfoItemTypeRelation);
    }

    public BassinfoItemTypeRelation getBassinfoItemTypeRelationById(Long id){
        return itemTypeService.getBassinfoItemTypeRelationById(id);
    }

    public List<BassinfoItemTypeRelation> getBassinfoItemTypeRelationList(Map<String, Object> params){
        return itemTypeService.getBassinfoItemTypeRelationList(params);
    }
}
