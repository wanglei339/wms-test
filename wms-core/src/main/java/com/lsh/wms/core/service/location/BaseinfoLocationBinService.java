package com.lsh.wms.core.service.location;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.dao.baseinfo.BaseinfoLocationBinDao;
import com.lsh.wms.model.baseinfo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 16/7/23 下午6:50
 */
@Component
@Transactional(readOnly = true)
public class BaseinfoLocationBinService implements IStrategy {
    private static final Logger logger = LoggerFactory.getLogger(LocationService.class);


    @Autowired
    private BaseinfoLocationBinDao baseinfoLocationBinDao;
    /**
     * 插入货位表baseinfo_location_bin
     *
     * @param
     */
    @Transactional(readOnly = false)
    public void insert(IBaseinfoLocaltionModel iBaseinfoLocaltionModel) {
        iBaseinfoLocaltionModel.setClassification(LocationConstant.CLASSIFICATION_BINS);
        baseinfoLocationBinDao.insert((BaseinfoLocationBin) iBaseinfoLocaltionModel);
    }

    @Transactional(readOnly = false)
    public void update(IBaseinfoLocaltionModel iBaseinfoLocaltionModel) {
        baseinfoLocationBinDao.update((BaseinfoLocationBin) iBaseinfoLocaltionModel);
    }

    /**
     * 通过locationId查找主表BaseinfoLocaiton
     *
     * @param id
     * @return BaseinfoLocation
     */
    public BaseinfoLocation getBaseinfoItemLocationModelById(Long id) {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("locationId", id);
        mapQuery.put("isValid", LocationConstant.IS_VALID);
        List<BaseinfoLocationBin> bins = baseinfoLocationBinDao.getBaseinfoLocationBinList(mapQuery);
        return bins.size() > 0 ? bins.get(0) : null;
    }

    /**
     * 货位计数
     * @param params
     * @return 货位个数
     */
    public Integer countBaseinfoLocaltionModel(Map<String, Object> params) {
        params.put("isValid", LocationConstant.IS_VALID);
        return baseinfoLocationBinDao.countBaseinfoLocationBin(params);
    }

    /**
     * 获取货位的list
     * @param params
     * @return  货位的list
     */
    public List<BaseinfoLocation> getBaseinfoLocaltionModelList(Map<String, Object> params) {
        params.put("isValid", LocationConstant.IS_VALID);
        return (List<BaseinfoLocation>) (List<?>) baseinfoLocationBinDao.getBaseinfoLocationBinList(params);
    }

    /**
     * 删除货位
     * @param locationId
     * @return 货位bin
     */
    @Transactional(readOnly = false)
    public IBaseinfoLocaltionModel removeLocation(Long locationId) {
        //先查,然后删除
        BaseinfoLocationBin temp = (BaseinfoLocationBin) this.getBaseinfoItemLocationModelById(locationId);
        if (temp == null) {
            throw new BizCheckedException("2180003");
        }
        temp.setIsValid(LocationConstant.NOT_VALID);
        this.update(temp);
        return temp;
    }


}
