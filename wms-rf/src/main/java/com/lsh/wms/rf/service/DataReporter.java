package com.lsh.wms.rf.service;

import com.google.common.collect.Maps;
import com.lsh.base.common.config.PropertyUtils;
import com.lsh.base.common.net.HttpClientUtils;
import com.lsh.base.q.LiveUtils;
import com.lsh.base.q.Utilities.Json.JSONObject;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.core.dao.image.PubImageDao;
import com.lsh.wms.core.service.image.PubImageService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.model.task.TaskInfo;
import org.apache.catalina.util.RequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class DataReporter {

    private static Logger logger = LoggerFactory.getLogger("DataReport");

    @Autowired
    private BaseTaskService baseTaskService;

    public  void REPORT_SUCCESS(Long taskId, Map<String, String> msg) {
        doReport(taskId, msg, 1);
    }

    public void REPORT_FAIL(Long taskId, Map<String, String> msg) {
        doReport(taskId, msg, 0);
    }

    private String getLocalIp() {
        String ip = null;
        return ip;
    }

    private void doReport(Long taskId, Map<String, String> msg, int isSucceed) {
        TaskInfo taskInfo  = baseTaskService.getTaskInfoById(taskId);
        StringBuilder builder = new StringBuilder();
        builder.append("\t"+taskId);
        builder.append("\t"+taskInfo.getType());
        builder.append("\t"+taskInfo.getSubType());
        builder.append("\t"+isSucceed);
        builder.append("\t"+RequestUtils.getHeader("uid"));
        builder.append("\t"+RequestUtils.getHeader("gid"));
        builder.append("\t");
        boolean firstIn = true;
        for (Map.Entry<String, String> entry : msg.entrySet()) {
            if (!firstIn){
                builder.append(";");
            } else {
                firstIn = false;
            }
            builder.append(entry.getKey() + "=" + entry.getValue());
        }

        logger.info(builder.toString());
    }
}
