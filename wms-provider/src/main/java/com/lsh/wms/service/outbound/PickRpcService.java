package com.lsh.wms.service.outbound;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.api.service.location.ILocationRpcService;
import com.lsh.wms.api.service.pick.IPCPickRpcService;
import com.lsh.wms.api.service.so.ISoRpcService;
import com.lsh.wms.api.service.stock.IStockQuantRpcService;
import com.lsh.wms.api.service.tu.ITuRpcService;
import com.lsh.wms.core.constant.ItemConstant;
import com.lsh.wms.core.constant.SoConstant;
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
import com.lsh.wms.model.task.TaskInfo;
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
public class PickRpcService implements IPCPickRpcService {

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
    @Autowired
    private BaseTaskService baseTaskService;

    /**
     * 获取托盘上的贵品的详情,用作贵品交付单
     *
     * @param contaienrId
     * @return
     * @throws BizCheckedException
     */
    public Map<String, Object> getContainerExpensiveGoods(Long contaienrId) throws BizCheckedException {
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
                ObdHeader obdHeader = iSoRpcService.getOutbSoHeaderDetailByOrderId(detail.getOrderId());
                if (null == obdHeader) {
                    throw new BizCheckedException("2870006");
                }
                //时间、托盘码
                goodInfo.put("orderTime", obdHeader.getCreatedAt());    //时间戳
                goodInfo.put("containerId", detail.getContainerId());
                //出库的方式
                goodInfo.put("orderTypeName", SoConstant.ORDER_TYPE_NAME_MAP.get(obdHeader.getOrderType()));
                //供货方和收货方的提供 拿托盘,找库存,找供商
                String customerCode = obdHeader.getDeliveryCode();
                Long ownerId = obdHeader.getOwnerUid();
                CsiCustomer csiCustomer = csiCustomerService.getCustomerByCustomerCode(customerCode);
                goodInfo.put("customer", csiCustomer);
                goodList.add(goodInfo);
            }
        }
        //结果map展示
        //head头
        Map<String, Object> headMap = new HashMap<String, Object>();
        //结果集
        Map<String, Object> result = new HashMap<String, Object>();

        if (goodList.isEmpty()) {
            result.put("headInfo", headMap);
            result.put("goodList", goodList);
            return result;
        }

        //时间
        headMap.put("printTime", DateUtils.getCurrentSeconds());
        BaseinfoLocationWarehouse warehouse = (BaseinfoLocationWarehouse) warehouseService.getBaseinfoItemLocationModelById(0L);
        //供货方
        headMap.put("warehouseName", warehouse.getWarehouseName());
        //出库类型
        headMap.put("orderTypeName", goodList.get(0).get("orderTypeName").toString());
        //收货方
        headMap.put("customer", goodList.get(0).get("customer"));

        //结果map
        result.put("headInfo", headMap);
        result.put("goodList", goodList);
        return result;
    }
}
