package com.lsh.wms.service.tu;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.api.service.location.ILocationRpcService;
import com.lsh.wms.api.service.tu.ITuOrdersRpcService;
import com.lsh.wms.api.service.tu.ITuRpcService;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.so.SoDeliveryService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.store.StoreService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.baseinfo.BaseinfoStore;
import com.lsh.wms.model.so.ObdHeader;
import com.lsh.wms.model.so.OutbDeliveryDetail;
import com.lsh.wms.model.so.OutbDeliveryHeader;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.tu.TuDetail;
import com.lsh.wms.model.tu.TuHead;
import com.lsh.wms.model.wave.WaveDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.Task;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by zhanghongling on 16/11/4.
 */
@Service(protocol = "dubbo")
public class TuOrdersRpcService implements ITuOrdersRpcService {
    @Reference
    private ITuRpcService iTuRpcService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private SoDeliveryService soDeliveryService;
    @Autowired
    private SoOrderService soOrderService;
    @Autowired
    private WaveService waveService;
    @Autowired
    private BaseTaskService baseTaskService;
    @Reference
    private ILocationRpcService iLocationRpcService;

    public Map<String, Object> getTuOrdersList(String tuId) throws BizCheckedException {
        //根据运单号获取发货信息
        TuHead tuHead = iTuRpcService.getHeadByTuId(tuId);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("transPlan", tuId);

        //根据运单号,获取发货单列表
        List<OutbDeliveryHeader> outbDeliveryHeaderList = soDeliveryService.getOutbDeliveryHeaderList(params);
        if (outbDeliveryHeaderList == null || outbDeliveryHeaderList.size() == 0) {
            throw new BizCheckedException("2990022");
        }
        Set<Long> deliveryIdSet = new HashSet<Long>();
        for (OutbDeliveryHeader oudh : outbDeliveryHeaderList) {
            deliveryIdSet.add(oudh.getDeliveryId());
        }

        //根据发货单id,获取订单id
        List<Long> deliveryIdList = new ArrayList<Long>();
        deliveryIdList.addAll(deliveryIdSet);
        List<OutbDeliveryDetail> outbDeliveryDetailList = soDeliveryService.getOutbDeliveryDetailList(deliveryIdList);
        //店铺no集合
        Set<String> storeNoSet = new HashSet<String>();
        Map<String, Set<Long>> storeNoToDeliveryId = new HashMap<String, Set<Long>>();
        for (OutbDeliveryDetail oudd : outbDeliveryDetailList) {


            //根据订单ID获取店铺ID
            ObdHeader obdHeader = soOrderService.getOutbSoHeaderByOrderId(oudd.getOrderId());
            if (obdHeader == null) {
                continue;
            }
            String storeNo = obdHeader.getDeliveryCode();

            //店铺集合
            storeNoSet.add(storeNo);

            //封装店铺和发货单映射关系
            if (storeNoToDeliveryId.get(storeNo) == null) {
                storeNoToDeliveryId.put(storeNo, new HashSet<Long>());
            }
            storeNoToDeliveryId.get(storeNo).add(oudd.getDeliveryId());

        }

        //获取并封装店铺信息
        Map<String, Map<String, Object>> storeInfoMap = new HashMap<String, Map<String, Object>>();
        for (String storeNo : storeNoSet) {
            List<BaseinfoStore> baseinfoStore = storeService.getStoreIdByCode(storeNo);
            Map<String, Object> storeMap = new HashMap<String, Object>();
            storeMap.put("storeNo", storeNo);
            String storeId = "";
            String storeName = "";
            if (baseinfoStore != null || baseinfoStore.size() > 0) {
                storeId = String.valueOf(baseinfoStore.get(0).getStoreId());
                storeName = baseinfoStore.get(0).getStoreName();
            }


            storeMap.put("storeId", storeId);
            storeMap.put("storeName", storeName);
            storeInfoMap.put(storeId, storeMap);
        }

        List<TuDetail> tuDetailList = iTuRpcService.getTuDeailListByTuId(tuId);
        //统计店铺其他数据
        Map<String, Map<String, BigDecimal>> storeInfoCountMap = new HashMap<String, Map<String, BigDecimal>>();
        for (TuDetail td : tuDetailList) {
            String storeId = String.valueOf(td.getStoreId());
            if (storeInfoCountMap.get(storeId) == null) {
                Map<String, BigDecimal> countInitMap = new HashMap<String, BigDecimal>();
                countInitMap.put("boxNum", BigDecimal.ZERO);//箱数
                countInitMap.put("turnBoxNum", BigDecimal.ZERO);//周转箱数
                countInitMap.put("containerNum", BigDecimal.ZERO);//板数
                storeInfoCountMap.put(storeId, countInitMap);
            }
            Map<String, BigDecimal> countMap = storeInfoCountMap.get(storeId);
            countMap.put("boxNum", countMap.get("boxNum").add(td.getBoxNum()));
            countMap.put("turnBoxNum", countMap.get("turnBoxNum").add(BigDecimal.valueOf(td.getTurnoverBoxNum())));
            countMap.put("containerNum", countMap.get("containerNum").add(BigDecimal.ONE));//有一条记录,板数加1
        }
        //合并统计数据,重新封装店铺信息
        BigDecimal boxNumTotal = BigDecimal.ZERO;
        BigDecimal turnBoxNumTotal = BigDecimal.ZERO;
        BigDecimal containerNumTotal = BigDecimal.ZERO;
        for (String storeId : storeInfoMap.keySet()) {
            BigDecimal boxNum = BigDecimal.ZERO;
            BigDecimal turnBoxNum = BigDecimal.ZERO;
            BigDecimal containerNum = BigDecimal.ZERO;
            if (storeInfoCountMap.get(storeId) != null) {
                boxNum = storeInfoCountMap.get(storeId).get("boxNum");
                turnBoxNum = storeInfoCountMap.get(storeId).get("turnBoxNum");
                containerNum = storeInfoCountMap.get(storeId).get("containerNum");

                boxNumTotal = boxNumTotal.add(boxNum);
                turnBoxNumTotal = turnBoxNumTotal.add(turnBoxNum);
                containerNumTotal = containerNumTotal.add(containerNum);
            }
            storeInfoMap.get(storeId).put("boxNum", boxNum);//箱数
            storeInfoMap.get(storeId).put("turnBoxNum", turnBoxNum);//周转箱数
            storeInfoMap.get(storeId).put("containerNum", containerNum);//板数

        }
        Map<String, Object> totalMap = new HashMap<String, Object>();
        totalMap.put("boxNum", boxNumTotal);//总箱数
        totalMap.put("turnBoxNum", turnBoxNumTotal);//总周转箱数
        totalMap.put("containerNum", containerNumTotal);//总板数
        storeInfoMap.put("total", totalMap);

        //封装返回数据
        Map<String, Object> returnData = new HashMap<String, Object>();
        returnData.put("tuId", tuId);//运单号
        returnData.put("printDate", DateUtils.FORMAT_DATE_WITH_BAR.format(new Date()));//打印日期
        returnData.put("warehouseId", tuHead.getWarehouseId());//供货仓库
        returnData.put("companyName", tuHead.getCompanyName());//承运商
        returnData.put("name", tuHead.getName());//司机姓名
        returnData.put("cellphone", tuHead.getCellphone());//司机电话
        returnData.put("storeCountInfo", storeInfoMap);//门店统计信息
        returnData.put("storeDeliveryList", storeNoToDeliveryId);//门店发货单号信息
        return returnData;
    }

    public Map<String, Object> getDeliveryOrdersList(String tuId) throws BizCheckedException {

        return null;
    }

    /**
     * 小店的派车单
     *
     * @param tuId
     * @return
     * @throws BizCheckedException
     */
    public Map<String, Object> getSendCarOrdersList(String tuId) throws BizCheckedException {
        //根据运单号获取发货信息
        TuHead tuHead = iTuRpcService.getHeadByTuId(tuId);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", tuHead.getName());
        params.put("carNumber", tuHead.getCarNumber());
        params.put("cellPhone", tuHead.getCellphone());
        params.put("printTime", DateUtils.FORMAT_DATE_WITH_BAR.format(new Date()));  //打印时间
        //总箱数
        BigDecimal totalPackCount = new BigDecimal("0.0000");
        Long totalTurnoverBoxCount = 0L;
        //周转箱数
        List<TuDetail> tuDetails = iTuRpcService.getTuDeailListByTuId(tuId);
        //门店信息集合
        Map<Long, Map<String, Object>> stores = new HashMap<Long, Map<String, Object>>();
        for (TuDetail tuDetail : tuDetails) {
            Long storeId = tuDetail.getStoreId();
            //包含
            if (stores.containsKey(storeId)) {
                Map<String, Object> storeMap = stores.get(storeId);
                List<Map<String, Object>> containerList = (List<Map<String, Object>>) storeMap.get("containerList");
                //查找托盘信息
                List<WaveDetail> waveDetails = waveService.getAliveDetailsByContainerId(tuDetail.getMergedContainerId());
                if (null == waveDetails || waveDetails.size() < 1) {
                    throw new BizCheckedException("2990041");
                }
                WaveDetail detail = waveDetails.get(0);
                TaskInfo qcInfo = this.getTaskInfoByWaveDetail(detail);
                if (null == qcInfo) {
                    continue;   //不存在组盘未完成(不可能)
                }
                Map<String, Object> container = new HashMap<String, Object>();
                container.put("containerId", detail.getContainerId());
                container.put("packCount", qcInfo.getTaskPackQty());
                container.put("turnoverBoxCount", qcInfo.getExt3());
                container.put("isRest", qcInfo.getFinishTime() < DateUtils.getTodayBeginSeconds()); //余货
                containerList.add(container);
                //单门店总箱数
                BigDecimal storeTotalPackCount = (BigDecimal) storeMap.get("storeTotalPackCount");
                storeMap.put("storeTotalPackCount", storeTotalPackCount.add(qcInfo.getTaskPackQty()));
                //单门店总周转箱
                Long storeTotalTurnoverBoxCount = (Long) storeMap.get("storeTotalTurnoverBoxCount");
                storeMap.put("storeTotalTurnoverBoxCount", storeTotalTurnoverBoxCount + qcInfo.getExt3());
            }
            //门店名,集货道list,门店id
            BaseinfoStore store = storeService.getStoreByStoreId(storeId);
            Map<String, Object> storeMap = new HashMap<String, Object>();
            storeMap.put("storeId", storeId);
            storeMap.put("storeName", store.getStoreName());
            storeMap.put("collectionBins", iLocationRpcService.getCollectionByStoreNo(store.getStoreNo()));

            List<Map<String, Object>> containerList = new LinkedList<Map<String, Object>>();
            //查找托盘信息
            List<WaveDetail> waveDetails = waveService.getAliveDetailsByContainerId(tuDetail.getMergedContainerId());
            if (null == waveDetails || waveDetails.size() < 1) {
                throw new BizCheckedException("2990041");
            }
            WaveDetail detail = waveDetails.get(0);
            TaskInfo qcInfo = this.getTaskInfoByWaveDetail(detail);
            if (null == qcInfo) {
                continue;   //不存在组盘未完成(不可能)
            }
            Map<String, Object> container = new HashMap<String, Object>();
            container.put("containerId", detail.getContainerId());
            container.put("packCount", qcInfo.getTaskPackQty());
            container.put("turnoverBoxCount", qcInfo.getExt3());
            container.put("isRest", qcInfo.getFinishTime() < DateUtils.getTodayBeginSeconds()); //余货
            containerList.add(container);
            //门店总箱数,周转箱数
            storeMap.put("storeTotalPackCount", qcInfo.getTaskPackQty());
            storeMap.put("storeTotalTurnoverBoxCount", qcInfo.getExt3());
            stores.put(storeId, storeMap);
            //全运单总箱数,总周转箱数
            totalPackCount = totalPackCount.add(qcInfo.getTaskPackQty());
            totalTurnoverBoxCount = totalTurnoverBoxCount + qcInfo.getExt3();
        }
        params.put("totalPackCount", totalPackCount);
        params.put("totalTurnoverBoxCount", totalTurnoverBoxCount);
        return params;
    }

    /**
     * 通过osd获取qctaskInfo的方法,task_info中的托盘有复用,用task_id查找
     *
     * @param waveDetail
     * @return
     * @throws BizCheckedException
     */
    public TaskInfo getTaskInfoByWaveDetail(WaveDetail waveDetail) throws BizCheckedException {
        Long taskId = waveDetail.getQcTaskId();
        TaskInfo qcInfo = baseTaskService.getTaskInfoById(taskId);
        if (TaskConstant.Done.equals(qcInfo.getStatus())) {
            return qcInfo;
        }
        return null;
    }
}
