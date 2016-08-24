package com.lsh.wms.api.service.user;


import com.lsh.base.common.exception.BizCheckedException;

/**
 * Created by lixin-mac on 16/7/28.
 */
public interface IUserRestService {

    String userLogin() throws BizCheckedException;
    String getMenuList() throws BizCheckedException;
}
