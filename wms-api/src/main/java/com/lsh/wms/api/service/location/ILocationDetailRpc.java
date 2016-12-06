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
    public IBaseinfoLocaltionModel getLocationDetailById(Long locationId) throws BizCheckedException;

    public List<BaseinfoLocation> getLocationDetailList(Map<String, Object> params) throws BizCheckedException;

    public void insertLocationDetailByType(LocationDetailRequest request) throws BizCheckedException;

    public IBaseinfoLocaltionModel updateLocationDetailByType(IBaseinfoLocaltionModel iBaseinfoLocaltionModel) throws BizCheckedException;

    public Integer countLocationDetailByType(Map<String, Object> mapQuery) throws BizCheckedException;

    public boolean removeLocation(Long locationId) throws BizCheckedException;

    /**
     * 获取需要得到的list方法
     *
     * @param listType
     * @return
     * @throws BizCheckedException
     */
    public List<BaseinfoLocation> getTargetListByListType(Integer listType) throws BizCheckedException;

    /**
     * 获取下一层级的所有节点
     *
     * @param locationId
     * @return
     */
    public List<BaseinfoLocation> getNextLevelLocations(Long locationId) throws BizCheckedException;

    /**
     * 插入一个区域
     * @param request
     * @throws BizCheckedException
     */
    public void insertRegion(LocationDetailRequest request) throws BizCheckedException;



}
