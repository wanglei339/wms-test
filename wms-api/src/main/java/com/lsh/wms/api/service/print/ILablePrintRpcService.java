package com.lsh.wms.api.service.print;

import com.lsh.base.common.exception.BizCheckedException;

import java.util.List;

/**
 * Created by zhanghongling on 16/11/9.
 */
public interface ILablePrintRpcService {
    List<String> getContainerCode(Integer number);
    boolean checkContainerCode(String containerCode) throws BizCheckedException;
}
