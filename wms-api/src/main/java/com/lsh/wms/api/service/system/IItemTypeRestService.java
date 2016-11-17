package com.lsh.wms.api.service.system;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.baseinfo.BaseinfoItemType;
import com.lsh.wms.model.baseinfo.BaseinfoItemTypeRelation;

import javax.ws.rs.QueryParam;
import java.util.Map;

/**
 * Created by zhanghongling on 16/11/10.
 */
public interface IItemTypeRestService {
    String getBaseinfoItemTypeList();
    String getBaseinfoItemTypeByItemId(String itemTypeId);
    String insertItemType(BaseinfoItemType baseinfoItemType);
    String getItemTypeList(Map<String, Object> mapQuery);
    String  getItemTypeListCount(Map<String, Object> mapQuery);
    String getItemTypeRelationList(String itemTypeId);
    String deleteItemTypeRelation(Long id);
    String insertItemTypeRelation(BaseinfoItemTypeRelation baseinfoItemTypeRelation)throws BizCheckedException;
    String update(BaseinfoItemType baseinfoItemType);
}
