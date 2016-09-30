package com.lsh.wms.api.service.shelve;

import com.lsh.base.common.exception.BizCheckedException;

/**
 * Created by wuhao on 16/8/16.
 */
public interface IPickUpShelveRestService {
    String createTask() throws BizCheckedException;
    String createDetail() throws BizCheckedException;
    String conFirmDetail() throws BizCheckedException;
    String getTaskList() throws BizCheckedException;
    String getDetail() throws BizCheckedException;
    String updateOperator() throws BizCheckedException;
    String doDetail() throws BizCheckedException;
    String cancelDetail() throws BizCheckedException;
}
