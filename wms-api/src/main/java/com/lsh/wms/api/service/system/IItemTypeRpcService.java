package com.lsh.wms.api.service.system;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.baseinfo.BaseinfoItemType;
import com.lsh.wms.model.baseinfo.BaseinfoItemTypeRelation;

import java.util.List;
import java.util.Map;

/**
 * Created by zhanghongling on 16/11/10.
 */
public interface IItemTypeRpcService {
    //  void insertItemType(BaseinfoItemType baseinfoItemType);

    //  void updateItemType(BaseinfoItemType baseinfoItemType);

    BaseinfoItemType getBaseinfoItemTypeById(Integer id);

    List<BaseinfoItemType> getBaseinfoItemTypeList(Map<String, Object> params);

    List<Object> getItemTypeList(Map<String, Object> params);

    Integer countItemTypeList(Map<String, Object> params);

    void insertItemTypeRelation(BaseinfoItemTypeRelation baseinfoItemTypeRelation)throws BizCheckedException;

    public void updateItemTypeRelation(BaseinfoItemTypeRelation baseinfoItemTypeRelation);

    public BaseinfoItemTypeRelation getBaseinfoItemTypeRelationById(Long id);

    public List<BaseinfoItemTypeRelation> getBaseinfoItemTypeRelationList(Map<String, Object> params);
}
