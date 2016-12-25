package com.lsh.wms.service.so;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.BeanMapTransUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.api.model.po.ReceiptRequest;
import com.lsh.wms.api.service.so.ISupplierBackRpcService;
import com.lsh.wms.core.service.so.SupplierBackDetailService;
import com.lsh.wms.model.so.SupplierBackDetail;
import com.lsh.wms.model.so.SupplierBackDetailRequest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhanghongling on 16/12/23.
 */
@Service(protocol = "dubbo")
public class SupplierBackRpcService implements ISupplierBackRpcService{
    @Autowired
    private SupplierBackDetailService supplierBackDetailService;

    public List<SupplierBackDetail> getSupplierBackDetailList(Map<String,Object> params)throws BizCheckedException {
        return supplierBackDetailService.getSupplierBackDetailList(params);
    }

    public void batchInsertDetail(List<SupplierBackDetailRequest> requestList)throws BizCheckedException {
        List<SupplierBackDetail> list = new ArrayList<SupplierBackDetail>();
        for(SupplierBackDetailRequest request :requestList){
            SupplierBackDetail supplierBackDetail = new SupplierBackDetail();
            ObjUtils.bean2bean(request, supplierBackDetail);
            supplierBackDetail.setCreatedAt(DateUtils.getCurrentSeconds());

        }
        supplierBackDetailService.batchInsertOrder(list);
    }

    public void updateSupplierBackDetail(SupplierBackDetail supplierBackDetail)throws BizCheckedException{
        supplierBackDetailService.update(supplierBackDetail);
    }


}
