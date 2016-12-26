package com.lsh.wms.service.so;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.api.service.so.ISupplierBackRpcService;
import com.lsh.wms.core.constant.ContainerConstant;
import com.lsh.wms.core.service.container.ContainerService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.so.SupplierBackDetailService;
import com.lsh.wms.model.so.ObdDetail;
import com.lsh.wms.model.so.SupplierBackDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
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
    @Autowired
    private SoOrderService soOrderService;


    private static Logger logger = LoggerFactory.getLogger(SupplierBackRpcService.class);

    public List<SupplierBackDetail> getSupplierBackDetailList(Map<String,Object> params)throws BizCheckedException {
        return supplierBackDetailService.getSupplierBackDetailList(params);
    }

    public void batchInsertDetail(List<SupplierBackDetail> requestList)throws BizCheckedException {
        SupplierBackDetail backDetail = requestList.get(0);
        Long orderId = backDetail.getOrderId();
        String detailOtherId = backDetail.getDetailOtherId();
        Long itemId = backDetail.getItemId();
        //获取已有的退货详情
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("orderId",orderId);
        params.put("detailOtherId",detailOtherId);
        params.put("itemId",itemId);
        params.put("isValid",1);
        List<SupplierBackDetail> detailList = supplierBackDetailService.getSupplierBackDetailList(params);
        //key: locationId value: backId
        Map<Long,Long> locationBackIdMap = new HashMap<Long, Long>();
        if(detailList != null && detailList.size() >0){
            for(SupplierBackDetail s : detailList){
                locationBackIdMap.put(s.getLocationId(),s.getBackId());
            }
        }

        //新增列表
        List<SupplierBackDetail> addList = new ArrayList<SupplierBackDetail>();
        //更新列表
        List<SupplierBackDetail> updateList = new ArrayList<SupplierBackDetail>();

        BigDecimal inboundQty = BigDecimal.ZERO;//实际退货数
        for(SupplierBackDetail supplierBackDetail :requestList){
            if(locationBackIdMap.get(supplierBackDetail.getLocationId()) != null){
                supplierBackDetail.setBackId(locationBackIdMap.get(supplierBackDetail.getLocationId()));
                supplierBackDetail.setUpdatedAt(DateUtils.getCurrentSeconds());
                //已有数据更新
                updateList.add(supplierBackDetail);
            }else {
                Long backId = RandomUtils.genId();
                Long containerId = containerService.createContainerByType(ContainerConstant.PALLET).getContainerId();
                supplierBackDetail.setContainerId(containerId);
                supplierBackDetail.setBackId(backId);
                supplierBackDetail.setCreatedAt(DateUtils.getCurrentSeconds());
                supplierBackDetail.setUpdatedAt(0L);
                addList.add(supplierBackDetail);
            }
            inboundQty = inboundQty.add(supplierBackDetail.getReqQty());
        }

        ObdDetail obdDetail = soOrderService.getObdDetailByOrderIdAndDetailOtherId(orderId,detailOtherId);
        obdDetail.setSowQty(inboundQty);
        if(obdDetail.getOrderQty().compareTo(inboundQty) == -1){
            throw new BizCheckedException("");//退货数超过订货数
        }
        supplierBackDetailService.batchInsertOrder(addList,updateList,obdDetail);
    }

    public void updateSupplierBackDetail(SupplierBackDetail requestDetail)throws BizCheckedException{
        Long backId = requestDetail.getBackId();

        Map<String,Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("backId",backId);
        List<SupplierBackDetail> backlist = supplierBackDetailService.getSupplierBackDetailList(paramsMap);
        SupplierBackDetail detail = backlist.get(0);

        Long orderId = detail.getOrderId();
        String detailOtherId = detail.getDetailOtherId();
        ObdDetail obdDetail = soOrderService.getObdDetailByOrderIdAndDetailOtherId(orderId,detailOtherId);

        Map<String,Object> params = new HashMap<String, Object>();
        params.put("orderId",orderId);
        params.put("detailOtherId",detailOtherId);
        List<SupplierBackDetail> list = supplierBackDetailService.getSupplierBackDetailList(params);
        BigDecimal inboundQty = BigDecimal.ZERO;//实际退货数
        //计算实际退货数
        for(SupplierBackDetail s : list){
            if(s.getOrderId().equals(orderId) && s.getDetailOtherId().equals(detailOtherId)){
                if(requestDetail.getIsValid() != null && requestDetail.getIsValid() == 0){
                    //删除记录
                    continue;
                }
                if(requestDetail.getReqQty() != null){
                    //更新
                    inboundQty = inboundQty.add(requestDetail.getReqQty());
                    continue;
                }
            }
            if(s.getIsValid() == 1){
                //有效记录
                inboundQty = inboundQty.add(s.getReqQty());
            }
        }
        if(obdDetail.getOrderQty().compareTo(inboundQty) == -1){
            throw new BizCheckedException("");//退货数超过订货数
        }
        obdDetail.setSowQty(inboundQty);
        supplierBackDetailService.update(requestDetail,obdDetail);
    }


}
