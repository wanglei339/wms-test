package com.lsh.wms.api.service.location;

import com.lsh.wms.model.baseinfo.IBaseinfoLocaltionModel;

/**
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 16/7/24 下午4:04
 */
public interface ILocationDetailRestService {
    //前端页面带着固定的type进来,进行相应的额查找,增加,更新,获取list
    public String getLocationDetailByIdAndType(Long locationId,Integer type);
    public String getLocationDetailListByType(Integer type);
    public String insertLocationDetailByType(IBaseinfoLocaltionModel baseinfoLocaltionModel, Integer type) throws ClassNotFoundException;
    public String updateLocationDetailByType(IBaseinfoLocaltionModel baseinfoLocaltionModel, Integer type);
}
