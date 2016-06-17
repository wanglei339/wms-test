package com.lsh.wms.core.service.up;

import com.google.common.collect.Maps;
import com.lsh.wms.api.model.up.UpApp;
import com.lsh.wms.core.constant.BusiConstant;
import com.lsh.wms.core.dao.up.UpAppDao;
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
 * 应用
 */
@Component
@Transactional(readOnly = true)
public class UpAppService {

    private static Logger logger = LoggerFactory.getLogger(UpAppService.class);

    @Autowired
    private UpAppDao appDao;

    public Integer countApp(String keyword) {
        Map<String, Object> params = Maps.newHashMap();
        if (StringUtils.isNotBlank(keyword)) {
            params.put("keyword", keyword);
        }
        return appDao.countApp(params);
    }

    public List<UpApp> getAppList(String keyword, Integer start, Integer limit) {
        Map<String, Object> params = Maps.newHashMap();
        if (StringUtils.isNotBlank(keyword)) {
            params.put("keyword", keyword);
        }
        params.put("orderBy", "order by ID desc");
        params.put("start", start);
        params.put("limit", limit);
        return appDao.getAppList(params);
    }

    public List<UpApp> getAllValidAppList() {
        Map<String, Object> params = Maps.newHashMap();
        params.put("status", BusiConstant.EFFECTIVE_YES);
        params.put("orderBy", "order by ID");
        return appDao.getAppList(params);
    }

    public UpApp getAppById(Integer id) {
        if (id == null) {
            return null;
        }
        return appDao.getAppById(id);
    }

    public boolean existApp(String appCode, Integer appId) {
        if (StringUtils.isEmpty(appCode)) {
            return true;
        }
        Map<String, Object> params = Maps.newHashMap();
        params.put("appCode", appCode.trim());
        List<UpApp> appList = appDao.getAppList(params);
        if (appList == null || appList.isEmpty()) {
            return false;
        }
        // 如果新增，有记录则存在
        if (appId == null) {
            return true;
        }
        // 如果是修改，则多于一条记录则存在
        if (appList.size() > 1) {
            return true;
        }
        // 如果是修改，只有一条记录且ID与自己不同，则存在
        if (!appList.get(0).getId().equals(appId)) {
            return true;
        }
        return false;
    }

    @Transactional(readOnly = false)
    public void insert(UpApp app) {
        if (app == null) {
            return;
        }
        app.setCreatedTime(new Date());
        appDao.insert(app);
    }

    @Transactional(readOnly = false)
    public void update(UpApp app) {
        if (app == null) {
            return;
        }
        app.setUpdatedTime(new Date());
        appDao.update(app);
    }

}
