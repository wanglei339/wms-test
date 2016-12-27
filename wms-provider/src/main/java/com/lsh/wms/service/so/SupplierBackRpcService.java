package com.lsh.wms.service.so;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.api.service.so.ISupplierBackRpcService;
import com.lsh.wms.api.service.stock.IStockQuantRpcService;
import com.lsh.wms.core.constant.ContainerConstant;
import com.lsh.wms.core.service.container.ContainerService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.so.SupplierBackDetailService;
import com.lsh.wms.core.service.utils.PackUtil;
import com.lsh.wms.model.so.ObdDetail;
import com.lsh.wms.model.so.SupplierBackDetail;
import com.lsh.wms.model.stock.StockQuant;
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
    @Reference
    private IStockQuantRpcService iStockQuantRpcService;


    private static Logger logger = LoggerFactory.getLogger(SupplierBackRpcService.class);

    public List<SupplierBackDetail> getSupplierBackDetailList(Map<String,Object> params)throws BizCheckedException {
        List<SupplierBackDetail> detailList = supplierBackDetailService.getSupplierBackDetailList(params);
        if(detailList == null || detailList.size() <= 0){
            return null;
        }
        for(SupplierBackDetail s : detailList){
            //获取该商品的可退货库存
            StockQuant stockQuant = getStockQuantByItemId(s.getItemId(),s.getLocationId());
            BigDecimal qty = BigDecimal.ZERO;
            if(stockQuant != null){
                qty = stockQuant.getQty();
            }
            s.setAllocQty(qty);
        }
        return detailList;
    }

    public void batchInsertDetail(List<SupplierBackDetail> requestList)throws BizCheckedException {
        SupplierBackDetail backDetail = requestList.get(0);
        Long orderId = backDetail.getOrderId();
        String detailOtherId = backDetail.getDetailOtherId();
        Long itemId = backDetail.getItemId();

        //获取该订单已有的退货详情
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("orderId",orderId);
        params.put("isValid",1);
        List<SupplierBackDetail> detailList = supplierBackDetailService.getSupplierBackDetailList(params);
        //key: locationId
        Map<Long,SupplierBackDetail> locationBackMap = new HashMap<Long, SupplierBackDetail>();
        //key: locationId value: reqqty
        //同一个订单退货使用相同的托盘码
        Long containerId = null;
        if(detailList != null && detailList.size() >0){
            for(SupplierBackDetail s : detailList){
                if(s.getItemId().equals(itemId) && s.getDetailOtherId().equals(detailOtherId)) {
                    locationBackMap.put(s.getLocationId(), s);
                }
            }
            //获取该订单对应的虚拟托盘ID
            containerId = detailList.get(0).getContainerId();
        }

        if(containerId == null){
            //生成新的托盘码
            containerId = containerService.createContainerByType(ContainerConstant.PALLET).getContainerId();
        }

        //新增列表
        List<SupplierBackDetail> addList = new ArrayList<SupplierBackDetail>();
        //更新列表
        List<SupplierBackDetail> updateList = new ArrayList<SupplierBackDetail>();

        ObdDetail obdDetail = soOrderService.getObdDetailByOrderIdAndDetailOtherId(orderId,detailOtherId);
        BigDecimal inboundQty = obdDetail.getSowQty();//实际退货数

        for(SupplierBackDetail supplierBackDetail :requestList){
            if(supplierBackDetail.getAllocQty().compareTo(supplierBackDetail.getReqQty()) == -1){
                throw new BizCheckedException("2901001");//退货数超过库存数
            }
            if(locationBackMap.get(supplierBackDetail.getLocationId()) != null){
                //已有数据更新
                SupplierBackDetail oldDetail = locationBackMap.get(supplierBackDetail.getLocationId());
                supplierBackDetail.setBackId(oldDetail.getBackId());
                supplierBackDetail.setUpdatedAt(DateUtils.getCurrentSeconds());

                updateList.add(supplierBackDetail);
                BigDecimal oldReqQty = oldDetail.getReqQty();
                //实际退货数加上本次更新的退货数
                inboundQty = inboundQty.add(supplierBackDetail.getReqQty()).subtract(oldReqQty);
            }else {
                //新增数据
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
        //订货ea数
        BigDecimal orderUnitQty = PackUtil.UomQty2EAQty(obdDetail.getOrderQty(),obdDetail.getPackUnit());
        if(orderUnitQty.compareTo(inboundQty) == -1){
            throw new BizCheckedException("2901000");//退货数超过订货数
        }
        supplierBackDetailService.batchInsertOrder(addList,updateList,obdDetail);
    }

    public void updateSupplierBackDetail(SupplierBackDetail requestDetail)throws BizCheckedException{
        Long backId = requestDetail.getBackId();
        //获取要更新的记录信息
        Map<String,Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("backId",backId);
        List<SupplierBackDetail> backlist = supplierBackDetailService.getSupplierBackDetailList(paramsMap);
        SupplierBackDetail backdetail = backlist.get(0);

        //获取退货单信息
        Long orderId = backdetail.getOrderId();
        String detailOtherId = backdetail.getDetailOtherId();
        Long itemId = backdetail.getItemId();
        Long locationId = backdetail.getLocationId();
        ObdDetail obdDetail = soOrderService.getObdDetailByOrderIdAndDetailOtherId(orderId,detailOtherId);

        //获取该商品的可退货库存
        StockQuant stockQuant = getStockQuantByItemId(itemId,locationId);
        BigDecimal qty = BigDecimal.ZERO;//库存可退货数
        BigDecimal inboundQty = obdDetail.getSowQty();//实际退货数
        if(stockQuant != null){
            qty = stockQuant.getQty();
        }
        //计算实际退货数
        if(requestDetail.getIsValid() != null && requestDetail.getIsValid() == 0){
            //删除记录
            inboundQty = inboundQty.subtract(backdetail.getReqQty());
        }else if(requestDetail.getReqQty() != null){
            //更新记录
            if(qty.compareTo(requestDetail.getReqQty()) == -1){
                throw new BizCheckedException("2901001");//退货数超过当前库位库存
            }
            // 总数 = 实收总数 + 更新量即(该记录本次退货数 - 该记录之前退货数)
            inboundQty = inboundQty.add(requestDetail.getReqQty()).subtract(backdetail.getReqQty());
        }

        requestDetail.setUpdatedAt(DateUtils.getCurrentSeconds());
        //订货ea数
        BigDecimal orderUnitQty = PackUtil.UomQty2EAQty(obdDetail.getOrderQty(),obdDetail.getPackUnit());
        if(orderUnitQty.compareTo(inboundQty) == -1){
            throw new BizCheckedException("2901000");//退货总数已超出
        }
        obdDetail.setSowQty(inboundQty);
        supplierBackDetailService.update(requestDetail,obdDetail);
    }
    //获取商品库位库存
    private StockQuant getStockQuantByItemId(Long itemId,Long locationId){
        Map<String,Object> locationMap = new HashMap<String, Object>();
        locationMap.put("itemId",itemId);
        locationMap.put("locationId",locationId);
        //获取商品该库位的库存
        List<StockQuant> quantList = iStockQuantRpcService.getItemLocationList(locationMap);
        if(quantList == null || quantList.size() <= 0){
            return null;
        }
        return quantList.get(0);
    }


}
