package com.lsh.wms.core.service.location;

import com.lsh.wms.model.baseinfo.IBaseinfoLocaltionModel;

import java.util.List;
import java.util.Map;

/**
 * Project Name: lsh-wms
 * Created by fuhao
 * Date: 16/7/23
 * Time: 16/7/23.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.core.service.location.
 * desc:类功能描述
 */
public interface IStrategy {
    public void insert(IBaseinfoLocaltionModel  baseinfoLocaltionModel);
    public void update(IBaseinfoLocaltionModel  baseinfoLocaltionModel);
    IBaseinfoLocaltionModel getBaseinfoItemLocationModelById(Long id);

    Integer countBaseinfoLocaltionModel(Map<String, Object> params);

    List<IBaseinfoLocaltionModel> getBaseinfoLocaltionModelList(Map<String, Object> params);
}
