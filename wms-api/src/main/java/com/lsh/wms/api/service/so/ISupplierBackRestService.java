package com.lsh.wms.api.service.so;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.so.SupplierBackDetailRequest;

import javax.ws.rs.QueryParam;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by zhanghongling on 16/12/24.
 */
public interface ISupplierBackRestService {
    String insertDetails(List<SupplierBackDetailRequest> requestList)throws BizCheckedException;
    String getSupplierBackDetailList()throws BizCheckedException;
    String updateSupplierBackDetail(Long backId,Long reqQty)throws BizCheckedException;

}
