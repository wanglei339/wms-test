package com.lsh.wms.api.service.sms;


import com.lsh.base.common.exception.BizCheckedException;

public interface ISmsRestService {

    public String sendMsg(String phone, String msg) throws BizCheckedException;

}
