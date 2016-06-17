package com.lsh.wms.core.task;

import com.google.common.collect.Maps;
import com.lsh.base.common.config.PropertyUtils;
import com.lsh.base.common.net.HttpClientUtils;
import com.lsh.base.q.LiveUtils;
import com.lsh.base.q.Utilities.Json.JSONObject;
import com.lsh.wms.core.dao.image.PubImageDao;
import com.lsh.wms.core.service.image.PubImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("liveChnTaskService")
@Component
@Transactional
public class TestTaskService {

    private static Logger logger = LoggerFactory.getLogger(TestTaskService.class);
    public static String LIVE_REST_HOST = PropertyUtils.getString("live.rest.host");

    @Autowired
    private PubImageDao pubImageDao;

    @Autowired
    private PubImageService pubImageService;



    @Transactional(readOnly = false)
    public void chnStatusCheck(){
        logger.info("chnStatusCheck task start!");
    }


}
