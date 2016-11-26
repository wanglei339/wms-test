package com.lsh.wms.service.outbound;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.service.location.ILocationRpcService;
import com.lsh.wms.api.service.pick.IPCPickRpcService;
import com.lsh.wms.api.service.so.ISoRpcService;
import com.lsh.wms.api.service.stock.IStockQuantRpcService;
import com.lsh.wms.api.service.tu.ITuRpcService;
import com.lsh.wms.core.constant.ItemConstant;
import com.lsh.wms.core.service.baseinfo.ItemTypeService;
import com.lsh.wms.core.service.csi.CsiCustomerService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.location.BaseinfoLocationWarehouseService;
import com.lsh.wms.core.service.so.SoDeliveryService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoLocationWarehouse;
import com.lsh.wms.model.csi.CsiCustomer;
import com.lsh.wms.model.so.ObdHeader;
import com.lsh.wms.model.wave.WaveDetail;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zengwenjun on 16/7/15.
 */
@Service(protocol = "dubbo")
public class PickRpcService implements IPCPickRpcService{

    @Autowired
    private WaveService waveService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private CsiCustomerService csiCustomerService;
    @Autowired
    private BaseinfoLocationWarehouseService warehouseService;
    @Reference
    private ISoRpcService iSoRpcService;

    /**
     * 获取托盘上的贵品的详情,用作贵品交付单
     *
     * @param contaienrId
     * @return
     * @throws BizCheckedException
     */
    public List<Map<String, Object>> getContainerExpensiveGoods(Long contaienrId) throws BizCheckedException {
        //合板或者不和板
        List<WaveDetail> waveDetails = waveService.getAliveDetailsByContainerId(contaienrId);
        if (null == waveDetails || waveDetails.size() < 1) {
            waveDetails = waveService.getWaveDetailsByMergedContainerId(contaienrId);
        }
        if (null == waveDetails || waveDetails.size() < 1) {
            throw new BizCheckedException("2870041");
        }

        //封装商品的结果集
        List<Map<String, Object>> goodList = new ArrayList<Map<String, Object>>();
        for (WaveDetail detail : waveDetails) {
            BaseinfoItem item = itemService.getItem(detail.getItemId());
            if (ItemConstant.TYPE_IS_VALUABLE == item.getIsValuable()) {
                Map<String, Object> goodInfo = new HashMap<String, Object>();
                goodInfo.put("item", item);
                goodInfo.put("unitName", detail.getAllocUnitName());
                goodInfo.put("qty", detail.getQcQty());
                goodInfo.put("orderId", detail.getOrderId());

                //供货方和收货方的提供 拿托盘,找库存,找供商
                BaseinfoLocationWarehouse warehouse = (BaseinfoLocationWarehouse) warehouseService.getBaseinfoItemLocationModelById(0L);
                goodInfo.put("warehouseName", warehouse.getWarehouseName());

                Long orderId = detail.getOrderId();
                ObdHeader obdHeader = iSoRpcService.getOutbSoHeaderDetailByOrderId(orderId);
                if (null == obdHeader) {
                    throw new BizCheckedException("2870006");
                }

                String customerCode = obdHeader.getDeliveryCode();
                Long ownerId = obdHeader.getOwnerUid();
                CsiCustomer csiCustomer = csiCustomerService.getCustomerByCustomerCode(customerCode);
                goodInfo.put("customerName", csiCustomer.getCustomerName());
                goodInfo.put("customerAddress", csiCustomer.getAddress());
                goodList.add(goodInfo);
            }
        }
        return goodList;
    }
}
