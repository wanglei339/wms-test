package com.lsh.wms.api.service.system;

import com.lsh.wms.model.baseinfo.BassinfoItemType;
import com.lsh.wms.model.baseinfo.BassinfoItemTypeRelation;

import java.util.List;
import java.util.Map;

/**
 * Created by zhanghongling on 16/11/10.
 */
public interface IItemTypeRpcService {
    public  void insertItemType(BassinfoItemType bassinfoItemType);

    public  void updateItemType(BassinfoItemType bassinfoItemType);

    public BassinfoItemType getBassinfoItemTypeById(Integer id);

    public void insertItemTypeRelation(BassinfoItemTypeRelation bassinfoItemTypeRelation);

    public void updateItemTypeRelation(BassinfoItemTypeRelation bassinfoItemTypeRelation);

    public BassinfoItemTypeRelation getBassinfoItemTypeRelationById(Long id);

    public List<BassinfoItemTypeRelation> getBassinfoItemTypeRelationList(Map<String, Object> params);
}
