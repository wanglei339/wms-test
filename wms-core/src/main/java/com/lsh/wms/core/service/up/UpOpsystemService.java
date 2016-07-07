package com.lsh.wms.core.service.up;

import com.google.common.collect.Maps;
import com.lsh.wms.core.model.up.UpOpsystem;
import com.lsh.wms.core.constant.BusiConstant;
import com.lsh.wms.core.dao.up.UpOpsystemDao;
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
 * 系统
 */
@Component
@Transactional(readOnly = true)
public class UpOpsystemService {

    private static Logger logger = LoggerFactory.getLogger(UpOpsystemService.class);

    @Autowired
    private UpOpsystemDao opsystemDao;

    public Integer countOpsystem(String keyword) {
        Map<String, Object> params = Maps.newHashMap();
        if (StringUtils.isNotBlank(keyword)) {
            params.put("keyword", keyword);
        }
        return opsystemDao.countOpsystem(params);
    }

    public List<UpOpsystem> getOpsystemList(String keyword, Integer start, Integer limit) {
        Map<String, Object> params = Maps.newHashMap();
        if (StringUtils.isNotBlank(keyword)) {
            params.put("keyword", keyword);
        }
        params.put("orderBy", "order by ID desc");
        params.put("start", start);
        params.put("limit", limit);
        return opsystemDao.getOpsystemList(params);
    }

    public List<UpOpsystem> getAllValidOpsystemlList() {
        Map<String, Object> params = Maps.newHashMap();
        params.put("status", BusiConstant.EFFECTIVE_YES);
        params.put("orderBy", "order by ID");
        return opsystemDao.getOpsystemList(params);
    }

    public UpOpsystem getOpsystemById(Integer id) {
        if (id == null) {
            return null;
        }
        return opsystemDao.getOpsystemById(id);
    }

    public boolean existOpsystem(String code, Integer id) {
        if (StringUtils.isEmpty(code)) {
            return true;
        }
        Map<String, Object> params = Maps.newHashMap();
        params.put("osCode", code.trim());
        List<UpOpsystem> list = opsystemDao.getOpsystemList(params);
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
    public void insert(UpOpsystem opsystem) {
        if (opsystem == null) {
            return;
        }
        opsystem.setCreatedTime(new Date());
        opsystemDao.insert(opsystem);
    }

    @Transactional(readOnly = false)
    public void update(UpOpsystem opsystem) {
        if (opsystem == null) {
            return;
        }
        opsystem.setUpdatedTime(new Date());
        opsystemDao.update(opsystem);
    }

}
