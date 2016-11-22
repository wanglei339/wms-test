package com.lsh.wms.integration.service.wumartsap;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.api.model.wumart.CreateIbdHeader;
import com.lsh.wms.api.model.wumart.CreateObdHeader;
import com.lsh.wms.api.service.wumart.IWuMart;
import com.lsh.wms.core.constant.SysLogConstant;
import com.lsh.wms.core.service.system.SysLogService;
import com.lsh.wms.model.system.SysLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Created by lixin-mac on 2016/11/4.
 */
@Service(protocol = "dubbo",async=true)
public class WuMart implements IWuMart {
    private static Logger logger = LoggerFactory.getLogger(WuMart.class);

    @Autowired
    private WuMartSap wuMartSap;

    @Autowired
    private SysLogService sysLogService;

    /**
     * 收货创建sap ibd 并过账接口
     * @param createIbdHeader
     * @return
     */
    public String sendIbd(CreateIbdHeader createIbdHeader, SysLog sysLog) {
        CreateIbdHeader backDate = wuMartSap.ibd2Sap(createIbdHeader);

        if(backDate != null) {
            Map<String,Object> map =  wuMartSap.ibd2SapAccount(backDate);
            if("E".equals(map.get("Type"))){
                sysLog.setLogMessage("ibd过账返回结果为空!");
                sysLog.setStatus(SysLogConstant.LOG_STATUS_FAILED);//3表示失败
                sysLog.setLogCode("ibd过账返回结果为空!");
                //0表示初始 1ibd创建成功 2ibd过账成功
                sysLog.setStep(SysLogConstant.LOG_STEP_IBDCREATE);
            }else if("P".equals(map.get("Type")) && map.get("message") == null){
                sysLog.setStatus(SysLogConstant.LOG_STATUS_FINISH);
                sysLog.setStep(SysLogConstant.LOG_STEP_IBDFINISH);
                sysLog.setLogMessage("ibd过账成功");
            }else{
                sysLog.setLogMessage((String) map.get("message"));
                sysLog.setStatus(SysLogConstant.LOG_STATUS_PART_FINISH);//部分过账成功
                sysLog.setStep(SysLogConstant.LOG_STEP_IBDCREATE);
            }
        }else {
            sysLog.setLogMessage("ibd创建失败!");
            sysLog.setStatus(SysLogConstant.LOG_STATUS_FAILED);//3表示失败
            sysLog.setLogCode("ibd创建失败");
        }
        sysLog.setRetryTimes(sysLog.getRetryTimes() + 1);
        //sysLogService.updateSysLog(sysLog);
        return null;
    }

    /**
     * 发货sto 创建sap obd并过账接口
     * @param createObdHeader
     * @return
     */
    public String sendObd(CreateObdHeader createObdHeader, SysLog sysLog) {
        CreateObdHeader backDate = wuMartSap.obd2Sap(createObdHeader);
        if(backDate != null){
            String type = wuMartSap.obd2SapAccount(backDate);
            if ("E".equals(type)){
                sysLog.setLogMessage("obd过账失败!");
                sysLog.setStatus(SysLogConstant.LOG_STATUS_FAILED);//3表示失败
                sysLog.setLogCode("obd过账失败");
                //0表示初始 1ibd创建成功 2ibd过账成功,3obd创建成功 4 obd过账成功
                sysLog.setStep(SysLogConstant.LOG_STEP_OBDCREATE);
            }else{
                sysLog.setStatus(SysLogConstant.LOG_STATUS_FINISH);
                sysLog.setStep(SysLogConstant.LOG_STEP_OBDFINISH);
                sysLog.setLogMessage("obd过账成功");
            }
        }else {
            sysLog.setLogMessage("创建失败!");
            sysLog.setStatus(SysLogConstant.LOG_STATUS_FAILED);//3表示失败
            sysLog.setLogCode("ibd创建失败");
        }
        sysLog.setRetryTimes(sysLog.getRetryTimes() + 1);
        //sysLogService.updateSysLog(sysLog);
        return null;
    }

    /**
     * ibd冲销过账接口
     * @param accountId
     * @param accountDetailId
     * @return
     */
    public String ibdAccountBack(String accountId, String accountDetailId) {
        String result = wuMartSap.ibd2SapBack(accountId,accountDetailId);
        return result;
    }
    /**
     * 直流创建 sap ibd创建并过账 以及obd创建并过账
     * @param ibdObdMap
     */
    public void sendSap(Map<String,Object> ibdObdMap, SysLog sysLog){

        // TODO: 2016/11/21 根据step来创建对应的 初始的从创建ibd开始
        if(sysLog.getStep() == SysLogConstant.LOG_STEP_INIT){
            CreateIbdHeader backDate = wuMartSap.ibd2Sap((CreateIbdHeader) ibdObdMap.get("createIbdHeader"));
            if(backDate != null) {
                Map<String,Object> map =  wuMartSap.ibd2SapAccount(backDate);
                if("E".equals(map.get("type"))){
                    sysLog.setLogMessage("直流ibd过账SAP返回值为空!");
                    sysLog.setStatus(SysLogConstant.LOG_STATUS_FAILED);//3表示失败
                    sysLog.setLogCode("直流ibd过账SAP返回值为空!");
                }else if("P".equals(map.get("Type")) && map.get("message") == null){
                    CreateObdHeader obdBackDate = wuMartSap.obd2Sap((CreateObdHeader) ibdObdMap.get("createObdHeader"));
                    if(backDate != null){
                        String type = wuMartSap.obd2SapAccount(obdBackDate);
                        if ("E".equals(type)){
                            sysLog.setLogMessage("obd过账失败!");
                            sysLog.setStatus(SysLogConstant.LOG_STATUS_FAILED);//3表示失败
                            sysLog.setLogCode("obd过账失败");
                            //0表示初始 1ibd创建成功 2ibd过账成功,3obd创建成功 4 obd过账成功
                            sysLog.setStep(SysLogConstant.LOG_STEP_OBDCREATE);
                        }else{
                            sysLog.setStatus(SysLogConstant.LOG_STATUS_FINISH);
                            sysLog.setStep(SysLogConstant.LOG_STEP_OBDFINISH);
                            sysLog.setLogMessage("obd过账成功");
                        }
                    }else {
                        sysLog.setStep(SysLogConstant.LOG_STEP_IBDFINISH);
                        sysLog.setLogMessage("ibd过账成功,obd创建失败!");
                        sysLog.setStatus(SysLogConstant.LOG_STATUS_FAILED);//3表示失败
                        sysLog.setLogCode("ibd过账成功,obd创建失败");
                    }
                }
            }else {
                sysLog.setLogMessage("ibd创建失败!");
                sysLog.setStatus(SysLogConstant.LOG_STATUS_FAILED);//3表示失败
                sysLog.setLogCode("ibd创建失败");
            }
            sysLog.setRetryTimes(sysLog.getRetryTimes() + 1);

        }


    }

    /**
     * 在库so obd创建接口。
     * @param createObdHeader
     * @return
     */
    public String sendSo2Sap(CreateObdHeader createObdHeader , SysLog sysLog) {

        String type = wuMartSap.soObd2Sap(createObdHeader);
        if("E".equals(type)){
            sysLog.setLogMessage("obd创建失败!");
            sysLog.setStatus(SysLogConstant.LOG_STATUS_FAILED);//3表示失败
            sysLog.setLogCode("obd创建失败");
        }else{
            sysLog.setStatus(SysLogConstant.LOG_STATUS_FINISH);
            sysLog.setStep(SysLogConstant.LOG_STEP_OBDFINISH);
            sysLog.setLogMessage("obd过账成功");
        }
        sysLog.setRetryTimes(sysLog.getRetryTimes() + 1);

        return JsonUtils.SUCCESS();
    }


}
