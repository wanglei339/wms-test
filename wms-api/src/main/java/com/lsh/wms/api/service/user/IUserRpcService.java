package com.lsh.wms.api.service.user;

import com.lsh.base.common.exception.BizCheckedException;

import java.util.Map;

/**
 * Created by lixin-mac on 16/8/3.
 */
public interface IUserRpcService {
    Map<String,Object> login(String userName,String passwd) throws BizCheckedException;
}
