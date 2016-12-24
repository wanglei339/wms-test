package com.lsh.wms.api.service.so;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.so.SupplierBackDetail;
import com.lsh.wms.model.so.SupplierBackDetailRequest;

import java.util.List;
import java.util.Map;

/**
 * Created by zhanghongling on 16/12/23.
 */
public interface ISupplierBackRpcService {
    List<SupplierBackDetail> getSupplierBackDetailList(Map<String,Object> params)throws BizCheckedException;
    void batchInsertDetail(List<SupplierBackDetailRequest> requestList)throws BizCheckedException;
    void updateSupplierBackDetail(SupplierBackDetail supplierBackDetail)throws BizCheckedException;
}
