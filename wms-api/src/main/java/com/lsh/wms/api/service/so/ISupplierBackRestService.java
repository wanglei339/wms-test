package com.lsh.wms.api.service.so;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.so.SupplierBackDetail;

import java.util.List;

/**
 * Created by zhanghongling on 16/12/24.
 */
public interface ISupplierBackRestService {
    String insertDetails(List<SupplierBackDetail> requestList)throws BizCheckedException;
    String getSupplierBackDetailList()throws BizCheckedException;
    String updateSupplierBackDetail(SupplierBackDetail supplierBackDetail)throws BizCheckedException;

}
