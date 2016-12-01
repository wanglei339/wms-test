package com.lsh.wms.rpc.service.system;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.service.system.IItemTypeRpcService;
import com.lsh.wms.core.service.baseinfo.ItemTypeService;
import com.lsh.wms.core.service.utils.IdGenerator;
import com.lsh.wms.model.baseinfo.BaseinfoItemType;
import com.lsh.wms.model.baseinfo.BaseinfoItemTypeRelation;
import org.apache.commons.lang3.StringUtils;
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
    @Autowired
    private IdGenerator idGenerator;

    public  void insertItemType(BaseinfoItemType baseinfoItemType){
        baseinfoItemType.setItemTypeId(idGenerator.genId("itemTypeId", false, false));
        itemTypeService.insertItemType(baseinfoItemType);
    }

    public  void updateItemType(BaseinfoItemType baseinfoItemType){
        itemTypeService.updateItemType(baseinfoItemType);
    }

    public BaseinfoItemType getBaseinfoItemTypeByItemId(Integer itemTypeId){
        return itemTypeService.getBaseinfoItemTypeByItemId(itemTypeId);
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
        Map<String,String> itemRelationMap = new HashMap<String, String>();

        for(BaseinfoItemType b :itemTypeList){
            itemNameMap.put(b.getItemTypeId()+"",b.getItemName());
            itemStatusMap.put(b.getItemTypeId()+"",b.getIsNeedProtime()+"");
        }
        for(Map relationMap :relationList){
            //类型ID
            String itemTypeId = String.valueOf(relationMap.get("itemTypeId"));
            //互斥类型列表
            String mutexType = String.valueOf(relationMap.get("mutexIdStr"));

            itemRelationMap.put(itemTypeId,mutexType);
        }
        List<Object> returnList = new ArrayList<Object>();
        for(BaseinfoItemType b :itemTypeList){
            Map<String,Object> itemMap = new HashMap<String, Object>();
           String itemTypeId =  b.getItemTypeId()+"";
            itemMap.put("itemTypeId",itemTypeId);
            itemMap.put("itemTypeName",itemNameMap.get(itemTypeId));
            itemMap.put("isNeedProtime",itemStatusMap.get(itemTypeId));
            String mutexTypeStr = itemRelationMap.get(itemTypeId);
            if(StringUtils.isBlank(mutexTypeStr)){
                //没有互斥类型
                itemMap.put("itemMutexType","");
            }else{
                String []mutexArray = mutexTypeStr.split(",");
                String [] itemMutexArr = new String[mutexArray.length];

                for(int i = 0;i<mutexArray.length;i++){
                    itemMutexArr[i] = itemNameMap.get(mutexArray[i]);
                }
                itemMap.put("itemMutexType",itemMutexArr);
            }

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
        }else if(itemTypeId.compareTo(itemMutexTypeId) == 0){
            throw new BizCheckedException("1990002");
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

    public  void deleteItemTypeRelation(Long id){
        itemTypeService.deleteItemTypeRelation(id);
    }


    public BaseinfoItemTypeRelation getBaseinfoItemTypeRelationById(Long id){
        return itemTypeService.getBaseinfoItemTypeRelationById(id);
    }

    public List<Map<String,Object>> getItemTypeRelationListByItemTypeId(String itemTypeId){
        List<Map<String,Object>> returnList = new ArrayList<Map<String, Object>>();

        List<BaseinfoItemTypeRelation> relationList = itemTypeService.getItemTypeRelationListByItemTypeId(itemTypeId);
        if(relationList == null || relationList.size() == 0){
            return returnList;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        List<BaseinfoItemType> itemTypeList = itemTypeService.getBaseinfoItemTypeList(params);
        Map<Long,String> itemNameMap = new HashMap<Long, String>();

        for(BaseinfoItemType b :itemTypeList){
            itemNameMap.put(b.getItemTypeId(),b.getItemName());
        }
         for(BaseinfoItemTypeRelation it : relationList){
             Map<String,Object> itemTypeRelationMap = new HashMap<String, Object>();
             itemTypeRelationMap.put("id",it.getId());
             itemTypeRelationMap.put("itemTypeId",it.getItemTypeId());
             itemTypeRelationMap.put("itemTypeName",itemNameMap.get(it.getItemTypeId()));
             itemTypeRelationMap.put("itemMutexId",it.getItemMutexId());
             itemTypeRelationMap.put("itemMutexName",itemNameMap.get(it.getItemMutexId()));
             returnList.add(itemTypeRelationMap);
         }
        return returnList;
    }
}
