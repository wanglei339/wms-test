package com.lsh.wms.api.service.print;

import com.lsh.base.common.exception.BizCheckedException;

import javax.ws.rs.QueryParam;

/**
 * Created by zhanghongling on 16/11/8.
 */
public interface ILablePrintRestService {
     String getContainerCode(Integer number,String containerCode) throws BizCheckedException;
     String getMergeLable(@QueryParam("number") Integer number);


    }
