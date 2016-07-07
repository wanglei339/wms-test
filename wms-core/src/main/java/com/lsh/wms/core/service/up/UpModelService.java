package com.lsh.wms.core.service.up;

import com.google.common.collect.Maps;
import com.lsh.wms.core.model.up.UpModel;
import com.lsh.wms.core.constant.BusiConstant;
import com.lsh.wms.core.dao.up.UpModelDao;
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
 * 型号
 */
@Component
@Transactional(readOnly = true)
public class UpModelService {

    private static Logger logger = LoggerFactory.getLogger(UpModelService.class);

    @Autowired
    private UpModelDao modelDao;

    public Integer countModel(String keyword) {
        Map<String, Object> params = Maps.newHashMap();
        if (StringUtils.isNotBlank(keyword)) {
            params.put("keyword", keyword);
        }
        return modelDao.countModel(params);
    }

    public List<UpModel> getModelList(String keyword, Integer start, Integer limit) {
        Map<String, Object> params = Maps.newHashMap();
        if (StringUtils.isNotBlank(keyword)) {
            params.put("keyword", keyword);
        }
        params.put("orderBy", "order by ID desc");
        params.put("start", start);
        params.put("limit", limit);
        return modelDao.getModelList(params);
    }

    public List<UpModel> getAllValidModelList() {
        Map<String, Object> params = Maps.newHashMap();
        params.put("status", BusiConstant.EFFECTIVE_YES);
        params.put("orderBy", "order by ID");
        return modelDao.getModelList(params);
    }

    public UpModel getModelById(Integer id) {
        if (id == null) {
            return null;
        }
        return modelDao.getModelById(id);
    }

    public boolean existModel(String code, Integer id) {
        if (StringUtils.isEmpty(code)) {
            return true;
        }
        Map<String, Object> params = Maps.newHashMap();
        params.put("modCode", code.trim());
        List<UpModel> list = modelDao.getModelList(params);
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

    @Transactional(readOnly = false)
    public void insert(UpModel model) {
        if (model == null) {
            return;
        }
        model.setCreatedTime(new Date());
        modelDao.insert(model);
    }

    @Transactional(readOnly = false)
    public void update(UpModel model) {
        if (model == null) {
            return;
        }
        model.setUpdatedTime(new Date());
        modelDao.update(model);
    }

}
