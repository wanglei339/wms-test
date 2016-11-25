package com.lsh.wms.api.service.back;


import com.lsh.wms.api.model.po.IbdBackRequest;
import com.lsh.wms.api.model.wumart.CreateIbdHeader;
import com.lsh.wms.model.system.SysLog;

/**
 * Created by lixin-mac on 16/9/6.
 */

public interface IDataBackService {
    String wmDataBackByPost(String request, String url , Integer type,SysLog sysLog);
    String ofcDataBackByPost(String request, String url,SysLog sysLog);
    Boolean erpDataBack(CreateIbdHeader createIbdHeader,SysLog sysLog);
}
