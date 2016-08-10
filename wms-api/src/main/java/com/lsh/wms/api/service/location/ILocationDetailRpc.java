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

    public boolean insertLocationDetailByType(BaseinfoLocation baseinfoLocation) throws BizCheckedException;

    public boolean updateLocationDetailByType(BaseinfoLocation baseinfoLocation) throws BizCheckedException;

    public Integer countLocationDetailByType(Map<String, Object> mapQuery);

    public boolean removeLocation(Long locationId) throws BizCheckedException;

//    /**
//     * 获取全货架
//     * 包含所有阁楼和货架
//     * @return
//     */
//    public List<BaseinfoLocation> getAllShelfs();


    //获取将这些方法写在策略模式中,按照不同的targetType来取不同的策略
//    /**
//     * 获取大区(抽象区)
//     * 仓库中A区B区
//     * @return
//     */
//    public List<BaseinfoLocation> getArea();
//
//    /**
//     * 获取所有的功能区(例如集货区、退货区、残次区、暂存区等)
//     * @return
//     */
//    public List<BaseinfoLocation> getDomains();
//
//    /**
//     * 获取所有的通道
//     * @return
//     */
//    public List<BaseinfoLocation> getAllPassage();
//
//    /**
//     * 获取所有的货架区和阁楼区
//     * @return
//     */
//    public List<BaseinfoLocation> getShelfRegion();

}
