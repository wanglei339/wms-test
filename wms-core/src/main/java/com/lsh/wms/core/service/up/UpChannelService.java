package com.lsh.wms.core.service.up;

import com.google.common.collect.Maps;
import com.lsh.wms.api.model.up.UpChannel;
import com.lsh.wms.core.constant.BusiConstant;
import com.lsh.wms.core.dao.up.UpChannelDao;
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
 * 渠道
 */
@Component
@Transactional(readOnly = true)
public class UpChannelService {

    private static Logger logger = LoggerFactory.getLogger(UpChannelService.class);

    @Autowired
    private UpChannelDao channelDao;

    public Integer countChannel(String keyword) {
        Map<String, Object> params = Maps.newHashMap();
        if (StringUtils.isNotBlank(keyword)) {
            params.put("keyword", keyword);
        }
        return channelDao.countChannel(params);
    }

    public List<UpChannel> getChannelList(String keyword, Integer start, Integer limit) {
        Map<String, Object> params = Maps.newHashMap();
        if (StringUtils.isNotBlank(keyword)) {
            params.put("keyword", keyword);
        }
        params.put("orderBy", "order by ID desc");
        params.put("start", start);
        params.put("limit", limit);
        return channelDao.getChannelList(params);
    }

    public List<UpChannel> getAllValidChannelList() {
        Map<String, Object> params = Maps.newHashMap();
        params.put("status", BusiConstant.EFFECTIVE_YES);
        params.put("orderBy", "order by ID");
        return channelDao.getChannelList(params);
    }

    public UpChannel getChannelById(Integer id) {
        if (id == null) {
            return null;
        }
        return channelDao.getChannelById(id);
    }

    public boolean existChannel(String code, Integer id) {
        if (StringUtils.isEmpty(code)) {
            return true;
        }
        Map<String, Object> params = Maps.newHashMap();
        params.put("chnCode", code.trim());
        List<UpChannel> list = channelDao.getChannelList(params);
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
    public void insert(UpChannel channel) {
        if (channel == null) {
            return;
        }
        channel.setCreatedTime(new Date());
        channelDao.insert(channel);
    }

    @Transactional(readOnly = false)
    public void update(UpChannel channel) {
        if (channel == null) {
            return;
        }
        channel.setUpdatedTime(new Date());
        channelDao.update(channel);
    }

}
