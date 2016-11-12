package com.lsh.wms.rpc.service.system;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.service.system.IItemTypeRpcService;
import com.lsh.wms.core.service.baseinfo.ItemTypeService;
import com.lsh.wms.model.baseinfo.BaseinfoItemType;
import com.lsh.wms.model.baseinfo.BaseinfoItemTypeRelation;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhanghongling on 16/11/10.
 */
@Service(protocol = "dubbo")
public class ItemTypeRpcService implements IItemTypeRpcService {
    @Autowired
    private ItemTypeService itemTypeService;

    public  void insertItemType(BaseinfoItemType baseinfoItemType){
        itemTypeService.insertItemType(baseinfoItemType);
    }

    public  void updateItemType(BaseinfoItemType baseinfoItemType){
        itemTypeService.updateItemType(baseinfoItemType);
    }

    public BaseinfoItemType getBaseinfoItemTypeById(Integer id){
        return itemTypeService.getBaseinfoItemTypeById(id);
    }
    public List<BaseinfoItemType> getBaseinfoItemTypeList(Map<String, Object> params){
        return itemTypeService.getBaseinfoItemTypeList(params);
    }

    //类型列表
    public List<Object> getItemTypeList(Map<String, Object> params) {
        List<Map<String,Object>> relationList = itemTypeService.getBaseinfoItemTypeAllRelationList(params);
        List<BaseinfoItemType> itemTypeList = itemTypeService.getBaseinfoItemTypeList(params);
        Map<String,String> itemNameMap = new HashMap<String, String>();
        Map<String,String> itemStatusMap = new HashMap<String, String>();

        for(BaseinfoItemType b :itemTypeList){
            itemNameMap.put(b.getId()+"",b.getItemName());
            itemStatusMap.put(b.getId()+"",b.getIsNeedProtime()+"");
        }
        List<Object> returnList = new ArrayList<Object>();
        for(Map relationMap :relationList){
            Map<String,Object> itemMap = new HashMap<String, Object>();
            //类型ID
            String itemTypeId = String.valueOf(relationMap.get("itemTypeId"));
            //互斥类型列表
            String mutexType = String.valueOf(relationMap.get("mutexIdStr"));

            itemMap.put("itemTypeId",itemTypeId);
            itemMap.put("itemTypeName",itemNameMap.get(itemTypeId));
            itemMap.put("isNeedProtime",itemStatusMap.get(itemTypeId));
            String []mutexArray = mutexType.split(",");
            Map<String,Object> itemMutexMap = new HashMap<String, Object>();

            for(String i :mutexArray){
                itemMutexMap.put(i,itemNameMap.get(i));
            }
            itemMap.put("itemMutexType",itemMutexMap);
            returnList.add(itemMap);
        }
        return returnList;
    }

    public Integer countItemTypeList(Map<String, Object> params) {
        return itemTypeService.countBaseinfoItemTypeAllRelationList(params);
    }


    public void insertItemTypeRelation(BaseinfoItemTypeRelation baseinfoItemTypeRelation)throws BizCheckedException {
        Long itemTypeId = baseinfoItemTypeRelation.getItemTypeId();
        Long itemMutexTypeId = baseinfoItemTypeRelation.getItemMutexId();
        Long temp = null;
        //小的ID作为类型,大的作为互斥类型
        if(itemTypeId.compareTo(itemMutexTypeId) == 1){
            temp = itemTypeId;
            itemTypeId = itemMutexTypeId;
            itemMutexTypeId = temp;
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("itemTypeId",itemTypeId);
        params.put("itemMutexId",itemMutexTypeId);
        List<BaseinfoItemTypeRelation> list = itemTypeService.getBaseinfoItemTypeRelationList(params);
        if(list != null && list.size() > 0){
            throw new BizCheckedException("1990001");//类型互斥已存在
        }else {
            itemTypeService.insertItemTypeRelation(baseinfoItemTypeRelation);
        }
    }

    public void updateItemTypeRelation(BaseinfoItemTypeRelation baseinfoItemTypeRelation){
        itemTypeService.insertItemTypeRelation(baseinfoItemTypeRelation);
    }

    public BaseinfoItemTypeRelation getBaseinfoItemTypeRelationById(Long id){
        return itemTypeService.getBaseinfoItemTypeRelationById(id);
    }

    public List<BaseinfoItemTypeRelation> getBaseinfoItemTypeRelationList(Map<String, Object> params){
        return itemTypeService.getBaseinfoItemTypeRelationList(params);
    }
}
