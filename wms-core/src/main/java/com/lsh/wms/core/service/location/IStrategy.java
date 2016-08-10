package com.lsh.wms.core.service.location;

import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.baseinfo.BaseinfoLocationBin;
import com.lsh.wms.model.baseinfo.IBaseinfoLocaltionModel;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * 工厂方法的产品类,产品具体的功能规范,不是不同方法的策略(方法相同,只是名字不同)
 * Project Name: lsh-wms
 * Created by fuhao
 * Date: 16/7/23
 * Time: 16/7/23.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.core.service.location.
 * desc:类功能描述
 */
@Component
public interface IStrategy {
    public void insert(IBaseinfoLocaltionModel iBaseinfoLocaltionModel);
    public void update(IBaseinfoLocaltionModel iBaseinfoLocaltionModel);
    BaseinfoLocation getBaseinfoItemLocationModelById(Long id);

    Integer countBaseinfoLocaltionModel(Map<String, Object> params);

    List<BaseinfoLocation> getBaseinfoLocaltionModelList(Map<String, Object> params);
}
