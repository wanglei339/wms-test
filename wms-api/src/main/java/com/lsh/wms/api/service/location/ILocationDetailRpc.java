package com.lsh.wms.api.service.location;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.model.location.LocationDetailRequest;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.baseinfo.IBaseinfoLocaltionModel;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 16/8/6 下午3:14
 */
public interface ILocationDetailRpc {
    public IBaseinfoLocaltionModel getLocationDetailById(Long locationId) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;
    public List<BaseinfoLocation> getLocationDetailList(Map<String, Object> params) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;
    public IBaseinfoLocaltionModel insertLocationDetailByType(IBaseinfoLocaltionModel localtionModel) throws BizCheckedException;
    public IBaseinfoLocaltionModel updateLocationDetailByType(IBaseinfoLocaltionModel localtionModel) throws BizCheckedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException;
    public Integer countLocationDetailByType(Map<String, Object> mapQuery);
    public boolean removeLocation(Long locationId) throws BizCheckedException;
}
