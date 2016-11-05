package com.lsh.wms.rpc.service.system;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.StrUtils;
import com.lsh.wms.api.model.po.IbdBackRequest;
import com.lsh.wms.api.model.so.ObdBackRequest;
import com.lsh.wms.api.model.so.ObdDetail;
import com.lsh.wms.api.service.back.IDataBackService;
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

    @Autowired
    private SysLogService sysLogService;

    @Reference
    private IDataBackService dataBackService;

    @Autowired
    private SysMsgService sysMsgService;


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
        String key = StrUtils.formatString(RedisKeyConstant.SYS_MSG,logId);
        SysMsg sysMsg = sysMsgService.getMessage(key);
        // TODO: 2016/10/24 日志表中还需要记录当时存在问题单据的ID 防止没有生成redis记录造成无法再次回传 
        if (sysMsg == null) {
            throw new BizCheckedException("2771000");
        }
        String json = sysMsg.getMsgBody();


        //Object request = BeanMapTransUtils.map2Bean(map, Object.class);

        SysLog syslog = sysLogService.getSysLogById(logId);
        if(syslog.getTargetSystem() == SysLogConstant.LOG_TARGET_WUMART){
            // TODO: 2016/10/24 根据type来确定回传的url 先加了ibd obd 还需要报损 报溢
            String url;
            if(syslog.getLogType() == SysLogConstant.LOG_TYPE_WUMART_IBD){
                url = IntegrationConstan.URL_IBD;
            }else{
                url = IntegrationConstan.URL_OBD;
            }
            dataBackService.wmDataBackByPost(json,url,syslog.getLogType());
        }

    }
}
