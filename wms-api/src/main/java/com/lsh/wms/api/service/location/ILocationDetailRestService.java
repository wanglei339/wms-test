package com.lsh.wms.api.service.location;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.model.location.LocationDetailRequest;
import com.lsh.wms.api.model.location.LocationDetailUpdateRequest;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 16/7/24 下午4:04
 */
public interface ILocationDetailRestService {
    //前端页面带着固定的type进来,进行相应的额查找,增加,更新,获取list
    public java.lang.String getLocationDetailById(Long locationId) throws BizCheckedException;

    public java.lang.String insertLocationDetailByType(LocationDetailRequest request) throws BizCheckedException;

    public java.lang.String updateLocationDetailByType(LocationDetailUpdateRequest locationDetailUpdateRequest) throws BizCheckedException;

    public String countLocationDetailByType() throws BizCheckedException;

    public String searchList() throws BizCheckedException;

    public String removeLocation() throws BizCheckedException;

    public String getTargetListByListType(Integer listType) throws BizCheckedException;

    public String getNextLevelLocations(Long locationId) throws BizCheckedException;
}
