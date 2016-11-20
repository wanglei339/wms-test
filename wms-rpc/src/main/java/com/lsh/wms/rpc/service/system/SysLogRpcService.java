package com.lsh.wms.rpc.service.system;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.StrUtils;
import com.lsh.wms.api.model.po.IbdBackRequest;
import com.lsh.wms.api.model.so.ObdBackRequest;
import com.lsh.wms.api.model.so.ObdDetail;
import com.lsh.wms.api.service.back.IDataBackService;
import com.lsh.wms.api.service.back.ITransportService;
import com.lsh.wms.api.service.system.ISysLogRpcService;
import com.lsh.wms.core.constant.IntegrationConstan;
import com.lsh.wms.core.constant.RedisKeyConstant;
import com.lsh.wms.core.constant.SysLogConstant;
import com.lsh.wms.core.dao.redis.RedisListDao;
import com.lsh.wms.core.service.system.SysLogService;
import com.lsh.wms.core.service.system.SysMsgService;
import com.lsh.wms.model.system.SysLog;
import com.lsh.wms.model.system.SysMsg;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lixin-mac on 2016/10/24.
 */
@Service(protocol = "dubbo")
public class SysLogRpcService implements ISysLogRpcService{

    public static final Logger logger = LoggerFactory.getLogger(SysLogRpcService.class);

    @Autowired
    private SysLogService sysLogService;

    @Reference
    private ITransportService iTransportService;


    public List<SysLog> getSysLogList(Map<String, Object> params) {
        return sysLogService.getSysLogList(params);
    }

    public Integer countSysLog(Map<String, Object> params) {
        return sysLogService.countSysLog(params);
    }

    /**
     *回传失败的订单重新回传
     */
    public void retransmission(Long logId) throws BizCheckedException{
        logger.info(StrUtils.formatString("retransmit SysLog : {0}", logId));
        iTransportService.dealOne(logId);
    }
}
