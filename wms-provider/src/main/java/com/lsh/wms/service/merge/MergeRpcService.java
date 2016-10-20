package com.lsh.wms.service.merge;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.service.merge.IMergeRpcService;
import com.lsh.wms.core.constant.StoreConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.store.StoreService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.baseinfo.BaseinfoStore;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.task.TaskInfo;
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
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("deliveryCode", store.getStoreNo());
            result.put("deliveryName", store.getStoreName());
            result.put("address", store.getAddress());
            result.put("totalMergedContainers", 0); // 未装车总板数
            result.put("restMergedContainers", 0); // 未装车余货总板数
            String storeNo = store.getStoreNo();
            List<BaseinfoLocation> locations = locationService.getCollectionByStoreNo(storeNo); // 门店对应的集货道
            // List<StockQuant> stockQuants = new ArrayList<StockQuant>();
            List<String> countedContainerIds = new ArrayList<String>();
            for (BaseinfoLocation location: locations) {
                List<StockQuant> quants = stockQuantService.getQuantsByLocationId(location.getLocationId());
                for (StockQuant quant: quants) {
                    Long containerId = quant.getContainerId();
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("containerId", containerId);
                    params.put("type", TaskConstant.TYPE_QC);
                    params.put("status", TaskConstant.Done);
                    List<TaskInfo> taskInfos = baseTaskService.getTaskInfoList(params);
                    if (taskInfos.size() > 0) {
                        TaskInfo taskInfo = taskInfos.get(0);
                        Long qcTaskId = taskInfo.getTaskId();
                        List<WaveDetail> waveDetails = waveService.getDetailsByQCTaskId(qcTaskId);
                        if (waveDetails.size() > 0) {
                            for (WaveDetail waveDetail: waveDetails) {
                                
                            }
                        }
                    }
                }
            }
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
}
