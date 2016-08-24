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
    @Autowired
    private LocationService locationService;

    /**
     * 传入BaseinfoLocationBin然后插入到主表中,并且插入到细节表中
     *
     * @param
     */
    @Transactional(readOnly = false)
    public void insert(IBaseinfoLocaltionModel iBaseinfoLocaltionModel) {
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
     * @return
     */
    public BaseinfoLocation getBaseinfoItemLocationModelById(Long id) {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("locationId", id);
        mapQuery.put("isValid", LocationConstant.IS_VALID);
        List<BaseinfoLocationBin> bins = baseinfoLocationBinDao.getBaseinfoLocationBinList(mapQuery);
//        BaseinfoLocationBin bin =  bins.get(0);
        return bins.size() > 0 ? bins.get(0) : null;
    }

    /**
     * 因为是先查找父类的表,所以只要把父类的数目返回即可
     * 传的参数一定是父类中有的
     *
     * @param params
     * @return
     */
    public Integer countBaseinfoLocaltionModel(Map<String, Object> params) {
        params.put("isValid", LocationConstant.IS_VALID);
        return baseinfoLocationBinDao.countBaseinfoLocationBin(params);
    }

    public List<BaseinfoLocation> getBaseinfoLocaltionModelList(Map<String, Object> params) {
        params.put("isValid", LocationConstant.IS_VALID);
        return (List<BaseinfoLocation>) (List<?>) baseinfoLocationBinDao.getBaseinfoLocationBinList(params);
    }

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
