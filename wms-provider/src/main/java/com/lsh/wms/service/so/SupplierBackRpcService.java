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
        Map<Long,BigDecimal> locationReqqtyMap = new HashMap<Long, BigDecimal>();

        if(detailList != null && detailList.size() >0){
            for(SupplierBackDetail s : detailList){
                locationBackIdMap.put(s.getLocationId(),s.getBackId());
                locationReqqtyMap.put(s.getLocationId(),s.getReqQty());
            }
        }

        //新增列表
        List<SupplierBackDetail> addList = new ArrayList<SupplierBackDetail>();
        //更新列表
        List<SupplierBackDetail> updateList = new ArrayList<SupplierBackDetail>();
        Long containerId = containerService.createContainerByType(ContainerConstant.PALLET).getContainerId();

        ObdDetail obdDetail = soOrderService.getObdDetailByOrderIdAndDetailOtherId(orderId,detailOtherId);
        BigDecimal inboundQty = obdDetail.getSowQty();//实际退货数

        for(SupplierBackDetail supplierBackDetail :requestList){
            if(locationBackIdMap.get(supplierBackDetail.getLocationId()) != null){
                supplierBackDetail.setBackId(locationBackIdMap.get(supplierBackDetail.getLocationId()));
                supplierBackDetail.setUpdatedAt(DateUtils.getCurrentSeconds());
                //已有数据更新
                updateList.add(supplierBackDetail);
                BigDecimal oldReqQty = locationReqqtyMap.get(supplierBackDetail.getLocationId());
                //实际退货数加上本次更新的退货数
                inboundQty = inboundQty.add(supplierBackDetail.getReqQty()).subtract(oldReqQty);
            }else {
                Long backId = RandomUtils.genId();
                supplierBackDetail.setContainerId(containerId);
                supplierBackDetail.setBackId(backId);
                supplierBackDetail.setCreatedAt(DateUtils.getCurrentSeconds());
                supplierBackDetail.setUpdatedAt(0L);
                addList.add(supplierBackDetail);
                //实际退货数加上本次的退货数
                inboundQty = inboundQty.add(supplierBackDetail.getReqQty());
            }
        }

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
        params.put("isValid",1);
        List<SupplierBackDetail> list = supplierBackDetailService.getSupplierBackDetailList(params);
        BigDecimal inboundQty = obdDetail.getSowQty();//实际退货数
        //计算实际退货数
        for(SupplierBackDetail s : list){
            if(s.getBackId().equals(backId)){
                if(requestDetail.getIsValid() != null && requestDetail.getIsValid() == 0){
                    //删除记录
                    inboundQty = inboundQty.subtract(s.getReqQty());
                    continue;
                }
                if(requestDetail.getReqQty() != null){
                    //更新  总数 = 实收总数 + 更新量即(该记录本次退货数 - 该记录之前退货数)
                    inboundQty = inboundQty.add(requestDetail.getReqQty()).subtract(s.getReqQty());
                    continue;
                }
            }
        }
        if(obdDetail.getOrderQty().compareTo(inboundQty) == -1){
            throw new BizCheckedException("");//退货数超过订货数
        }
        obdDetail.setSowQty(inboundQty);
        supplierBackDetailService.update(requestDetail,obdDetail);
    }


}
