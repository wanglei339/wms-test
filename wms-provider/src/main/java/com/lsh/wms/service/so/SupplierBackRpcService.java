package com.lsh.wms.service.so;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.api.service.so.ISupplierBackRpcService;
import com.lsh.wms.core.constant.ContainerConstant;
import com.lsh.wms.core.service.container.ContainerService;
import com.lsh.wms.core.service.so.SupplierBackDetailService;
import com.lsh.wms.model.so.SupplierBackDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    @Autowired
    private ContainerService containerService;


    private static Logger logger = LoggerFactory.getLogger(SupplierBackRpcService.class);

    public List<SupplierBackDetail> getSupplierBackDetailList(Map<String,Object> params)throws BizCheckedException {
        return supplierBackDetailService.getSupplierBackDetailList(params);
    }

    public void batchInsertDetail(List<SupplierBackDetail> requestList)throws BizCheckedException {
        List<SupplierBackDetail> list = new ArrayList<SupplierBackDetail>();
        Long containerId = containerService.createContainerByType(ContainerConstant.PALLET).getContainerId();
        for(SupplierBackDetail supplierBackDetail :requestList){
            Long backId = RandomUtils.genId();
            supplierBackDetail.setContainerId(containerId);
            supplierBackDetail.setBackId(backId);
            supplierBackDetail.setCreatedAt(DateUtils.getCurrentSeconds());
            supplierBackDetail.setUpdatedAt(0L);
            list.add(supplierBackDetail);
        }
        supplierBackDetailService.batchInsertOrder(list);
    }

    public void updateSupplierBackDetail(SupplierBackDetail supplierBackDetail)throws BizCheckedException{
        supplierBackDetailService.update(supplierBackDetail);
    }


}
