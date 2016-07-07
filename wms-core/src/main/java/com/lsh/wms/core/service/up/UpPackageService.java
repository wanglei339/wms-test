package com.lsh.wms.core.service.up;

import com.google.common.collect.Maps;
import com.lsh.base.common.utils.StrUtils;
import com.lsh.wms.core.model.up.UpPackage;
import com.lsh.wms.core.constant.BusiConstant;
import com.lsh.wms.core.constant.RedisKeyConstant;
import com.lsh.wms.core.dao.redis.RedisStringDao;
import com.lsh.wms.core.dao.up.UpPackageDao;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 产品包
 */
@Component
@Transactional(readOnly = true)
public class UpPackageService {

    private static Logger logger = LoggerFactory.getLogger(UpPackageService.class);

    @Autowired
    private UpPackageDao packageDao;

    @Autowired
    private RedisStringDao stringDao;

    public Integer countUpPackage(String keyword, Integer appId, Integer osId, Integer chnId, Integer modId) {
        Map<String, Object> params = Maps.newHashMap();
        if (StringUtils.isNotBlank(keyword)) {
            params.put("keyword", keyword);
        }
        params.put("appId", appId);
        params.put("osId", osId);
        params.put("chnId", chnId);
        params.put("modId", modId);
        return packageDao.countPackage(params);
    }

    public List<UpPackage> getUpPackageList(String keyword, Integer appId, Integer osId, Integer chnId, Integer modId, Integer start, Integer limit) {
        Map<String, Object> params = Maps.newHashMap();
        if (StringUtils.isNotBlank(keyword)) {
            params.put("keyword", keyword);
        }
        params.put("appId", appId);
        params.put("osId", osId);
        params.put("chnId", chnId);
        params.put("modId", modId);
        params.put("start", start);
        params.put("limit", limit);
        return packageDao.getPackageList(params);
    }

    public List<UpPackage> getValidPackageList(String keyword, Integer appId, Integer osId) {
        Map<String, Object> params = Maps.newHashMap();
        if (StringUtils.isNotBlank(keyword)) {
            params.put("keyword", keyword);
        }
        params.put("appId", appId);
        params.put("osId", osId);
        params.put("status", BusiConstant.EFFECTIVE_YES);
        return packageDao.getPackageList(params);
    }

    public UpPackage getPackageById(Long id) {
        if (id == null) {
            return null;
        }
        return packageDao.getPackageById(id);
    }

    @Transactional(readOnly = false)
    public void insert(UpPackage upPackage) {
        if (upPackage == null) {
            return;
        }
        upPackage.setCreatedTime(new Date());
        packageDao.insert(upPackage);
        cachePkg(upPackage.getId());
    }

    @Transactional(readOnly = false)
    public void update(UpPackage upPackage) {
        if (upPackage == null) {
            return;
        }
        upPackage.setUpdatedTime(new Date());
        packageDao.update(upPackage);
    }

    public boolean existPackage(Integer appId, Integer osId, Integer chnId, Integer modId, Integer pkgType, Long id) {
        if (appId == null || osId == null || chnId == null || modId == null) {
            return true;
        }
        Map<String, Object> params = Maps.newHashMap();
        params.put("appId", appId);
        params.put("osId", osId);
        params.put("chnId", chnId);
        params.put("modId", modId);
        params.put("pkgType", pkgType);
        List<UpPackage> list = packageDao.getPackageList(params);
        if (list == null || list.isEmpty()) {
            return false;
        }
        // 如果新增，有记录则存在
        if (id == null) {
            return true;
        }
        // 如果是修改，则多于一条记录则存在
        if (list.size() > 1) {
            return true;
        }
        // 如果是修改，只有一条记录且ID与自己不同，则存在
        if (!list.get(0).getId().equals(id)) {
            return true;
        }
        return false;
    }

    public boolean existAppkey(String appKey, Long id) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("appKey", appKey.trim());
        List<UpPackage> list = packageDao.getPackageList(params);
        if (list == null || list.isEmpty()) {
            return false;
        }
        // 如果新增，有记录则存在
        if (id == null) {
            return true;
        }
        // 如果是修改，则多于一条记录则存在
        if (list.size() > 1) {
            return true;
        }
        // 如果是修改，只有一条记录且ID与自己不同，则存在
        if (!list.get(0).getId().equals(id)) {
            return true;
        }
        return false;
    }

    public void cachePkg(Long id) {
        UpPackage pkg = getPackageById(id);
        if (pkg != null) {
            String key = StrUtils.formatString(RedisKeyConstant.UP_PKG_KEY, pkg.getAppKey());
            stringDao.set(key, id);
        }
    }

}
