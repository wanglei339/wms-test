package com.lsh.wms.service.merge;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.api.service.merge.IMergeRpcService;
import com.lsh.wms.core.constant.StoreConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.constant.TuConstant;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.store.StoreService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.core.service.tu.TuService;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.baseinfo.BaseinfoStore;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.tu.TuDetail;
import com.lsh.wms.model.tu.TuHead;
import com.lsh.wms.model.wave.WaveDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fengkun on 2016/10/20.
 */
@Service(protocol = "dubbo")
public class MergeRpcService implements IMergeRpcService {
    private static Logger logger = LoggerFactory.getLogger(MergeRpcService.class);

    @Autowired
    private StoreService storeService;
    @Autowired
    private BaseTaskService baseTaskService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private StockQuantService stockQuantService;
    @Autowired
    private WaveService waveService;
    @Autowired
    private TuService tuService;

    /**
     * 门店维度的未装车板数列表
     * @param mapQuery
     * @return
     * @throws BizCheckedException
     */
    public List<Map<String, Object>> getMergeList(Map<String, Object> mapQuery) throws BizCheckedException {
        mapQuery.put("scale", StoreConstant.SCALE_HYPERMARKET); // 大店
        List<BaseinfoStore> stores = storeService.getOpenedStoreList(mapQuery);
        List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
        for (BaseinfoStore store: stores) {
            Integer totalMergedContainers = 0; // 未装车总板数
            Integer restMergedContainers = 0; // 未装车余货总板数
            String storeNo = store.getStoreNo();
            List<Long> countedContainerIds = new ArrayList<Long>();
            List<WaveDetail> waveDetails = this.getWaveDetailByStoreNo(storeNo);
            if (waveDetails.size() > 0) {
                for (WaveDetail waveDetail : waveDetails) {
                    Long mergedContainerId = 0L;
                    if (waveDetail.getMergedContainerId().equals(0L)) {
                        mergedContainerId = waveDetail.getMergedContainerId();
                    } else {
                        mergedContainerId = waveDetail.getContainerId();
                    }
                    if (!countedContainerIds.contains(mergedContainerId)) {
                        countedContainerIds.add(mergedContainerId);
                        List<TuDetail> tuDetails = tuService.getTuDeailListByMergedContainerId(mergedContainerId);
                        if (tuDetails.size() == 0) {
                            totalMergedContainers++;
                            // 是否是余货
                            if (waveDetail.getQcAt() < DateUtils.getTodayBeginSeconds()) {
                                restMergedContainers++;
                            }
                        } else {
                            Boolean needCount = true;
                            for (TuDetail tuDetail : tuDetails) {
                                String tuId = tuDetail.getTuId();
                                TuHead tuHead = tuService.getHeadByTuId(tuId);
                                if (!tuHead.getStatus().equals(TuConstant.SHIP_OVER)) {
                                    needCount = false;
                                    break;
                                }
                            }
                            if (needCount) {
                                totalMergedContainers++;
                                if (waveDetail.getQcAt() < DateUtils.getTodayBeginSeconds()) {
                                    restMergedContainers++;
                                }
                            }
                        }
                    }
                }
            }
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("storeNo", store.getStoreNo());
            result.put("storeName", store.getStoreName());
            result.put("address", store.getAddress());
            result.put("totalMergedContainers", totalMergedContainers);
            result.put("restMergedContainers", restMergedContainers);
            results.add(result);
        }
        return results;
    }

    /**
     * 列表total
     * @param mapQuery
     * @return
     * @throws BizCheckedException
     */
    public Integer countMergeList(Map<String, Object> mapQuery) throws BizCheckedException {
        mapQuery.put("scale", StoreConstant.SCALE_HYPERMARKET); // 大店
        mapQuery.put("isOpen", 1);
        Integer total = storeService.countBaseinfoStore(mapQuery);
        return total;
    }

    /**
     * 通过osd获取组盘完成记录的统计数
     * @param waveDetail
     * @return
     * @throws BizCheckedException
     */
    public Map<String, BigDecimal> getQcCountsByWaveDetail(WaveDetail waveDetail) throws BizCheckedException {
        Long qcTaskId = waveDetail.getQcTaskId();
        Map<String, BigDecimal> result = new HashMap<String, BigDecimal>();
        TaskInfo qcTaskInfo = baseTaskService.getTaskInfoById(qcTaskId);
        if (qcTaskInfo == null || !qcTaskInfo.getStatus().equals(TaskConstant.Done)) {
            throw new BizCheckedException("2870003");
        }
        result.put("packCount", new BigDecimal(qcTaskInfo.getExt4())); // 总箱数
        result.put("turnoverBoxCount", new BigDecimal(qcTaskInfo.getExt3())); // 总周转箱数
        return result;
    }

    /**
     * 获取门店的合板详情
     * @param storeNo
     * @return
     * @throws BizCheckedException
     */
    public Map<Long, Map<String, Object>> getMergeDetailByStoreNo(String storeNo) throws BizCheckedException {
        Map<Long, Map<String, Object>> results = new HashMap<Long, Map<String, Object>>();
        List<Long> countedContainerIds = new ArrayList<Long>();
        List<WaveDetail> waveDetails = this.getWaveDetailByStoreNo(storeNo);
        for (WaveDetail waveDetail: waveDetails) {
            if (!countedContainerIds.contains(waveDetail.getContainerId())) {
                Long containerId = 0L;
                if (waveDetail.getMergedContainerId().equals(0L)) {
                    containerId = waveDetail.getContainerId();
                } else {
                    containerId = waveDetail.getMergedContainerId();
                }
                // 未装车的
                List<TuDetail> tuDetails = tuService.getTuDeailListByMergedContainerId(containerId);
                Boolean needCount = true;
                if (tuDetails.size() > 0 ){
                    for (TuDetail tuDetail : tuDetails) {
                        String tuId = tuDetail.getTuId();
                        TuHead tuHead = tuService.getHeadByTuId(tuId);
                        if (!tuHead.getStatus().equals(TuConstant.SHIP_OVER)) { //未装车
                            needCount = false;
                            break;
                        }
                    }
                }
                if (!needCount) {
                    continue;
                }
                Map<String, BigDecimal> qcCounts = this.getQcCountsByWaveDetail(waveDetail);
                Map<String, Object> result = new HashMap<String, Object>();
                if (results.containsKey(containerId)) {
                    result = results.get(containerId);
                    result.put("packCount", new BigDecimal(Double.valueOf(result.get("packCount").toString())).add(qcCounts.get("packCount")));
                    result.put("turnoverBoxCount", new BigDecimal(Double.valueOf(result.get("turnoverBoxCount").toString())).add(qcCounts.get("turnoverBoxCount")));
                    result.put("containerCount", Integer.valueOf(result.get("containerCount").toString()) + 1);
                    // 是否是余货
                    if (waveDetail.getQcAt() < DateUtils.getTodayBeginSeconds()) {
                        result.put("isRest", true);
                    }
                } else {
                    result.put("containerId", containerId);
                    result.put("containerCount", 1);
                    result.put("packCount", qcCounts.get("packCount"));
                    result.put("turnoverBoxCount", qcCounts.get("turnoverBoxCount"));
                    result.put("isExpensive", false);
                    if (waveDetail.getQcAt() < DateUtils.getTodayBeginSeconds()) {
                        result.put("isRest", true);
                    } else {
                        result.put("isRest", false);
                    }
                    result.put("mergedTime", waveDetail.getMergeAt());
                }
                results.put(containerId, result);
                countedContainerIds.add(waveDetail.getContainerId());
            }
        }
        return results;
    }

    /**
     * 通过门店号获取osd
     * @param storeNo
     * @return
     */
    public List<WaveDetail> getWaveDetailByStoreNo(String storeNo) {
        List<BaseinfoLocation> locations = locationService.getCollectionByStoreNo(storeNo); // 门店对应的集货道
        List<WaveDetail> waveDetails = new ArrayList<WaveDetail>();
        for (BaseinfoLocation location: locations) {
            List<StockQuant> quants = stockQuantService.getQuantsByLocationId(location.getLocationId());
            for (StockQuant quant: quants) {
                Long containerId = quant.getContainerId();
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("containerId", containerId);
                params.put("type", TaskConstant.TYPE_QC);
                params.put("status", TaskConstant.Done);
                params.put("businessMode", TaskConstant.MODE_DIRECT);
                List<TaskInfo> taskInfos = baseTaskService.getTaskInfoList(params);
                if (taskInfos.size() > 0) {
                    TaskInfo taskInfo = taskInfos.get(0);
                    Long qcTaskId = taskInfo.getTaskId();
                    waveDetails.addAll(waveService.getDetailsByQCTaskId(qcTaskId));
                }
            }
        }
        return waveDetails;
    }
}
