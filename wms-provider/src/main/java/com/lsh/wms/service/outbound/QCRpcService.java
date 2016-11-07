package com.lsh.wms.service.outbound;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.api.service.pick.IQCRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.api.service.tu.ITuRpcService;
import com.lsh.wms.core.constant.*;
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
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by zengwenjun on 16/7/30.
 */
@Service(protocol = "dubbo")
public class QCRpcService implements IQCRpcService {
    @Autowired
    private WaveService waveService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private BaseTaskService baseTaskService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private StockQuantService stockQuantService;
    @Autowired
    private TuService tuService;
    @Reference
    private ITuRpcService iTuRpcService;

    public void skipException(long id) throws BizCheckedException {
        WaveDetail detail = waveService.getWaveDetailById(id);
        if (detail == null) {
            throw new BizCheckedException("2070001");
        }
        //必须要进行库存操作
        detail.setQcExceptionDone(PickConstant.QC_EXCEPTION_DONE_SKIP);
        waveService.updateDetail(detail);
    }

    public void repairException(long id) throws BizCheckedException {
        WaveDetail detail = waveService.getWaveDetailById(id);
        if (detail == null) {
            throw new BizCheckedException("2070001");
        }
        detail.setQcExceptionDone(PickConstant.QC_EXCEPTION_DONE_DONE);
        detail.setQcQty(detail.getPickQty());
        detail.setQcExceptionQty(new BigDecimal("0.0000"));
        detail.setQcException(WaveConstant.QC_EXCEPTION_NORMAL);
        waveService.updateDetail(detail);
    }

    public void fallbackException(long id) throws BizCheckedException {
        WaveDetail detail = waveService.getWaveDetailById(id);
        if (detail == null) {
            throw new BizCheckedException("2070001");
        }
        detail.setQcExceptionDone(PickConstant.QC_EXCEPTION_DONE_NORMAL);
        detail.setQcQty(detail.getPickQty());
        detail.setQcException(WaveConstant.QC_EXCEPTION_NORMAL);
        detail.setQcExceptionQty(new BigDecimal("0.0000"));
        waveService.updateDetail(detail);
    }

    /**
     * 门店维度组盘列表
     *
     * @param mapQuery
     * @return
     * @throws BizCheckedException
     */
    public List<Map<String, Object>> getGroupList(Map<String, Object> mapQuery) throws BizCheckedException {
        mapQuery.put("scale", StoreConstant.SCALE_STORE); // 小店不合板
        List<BaseinfoStore> stores = storeService.getOpenedStoreList(mapQuery);
        List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
        for (BaseinfoStore store : stores) {
            BigDecimal totalBoxes = new BigDecimal("0.0000"); // 该门店未装车总箱子
            BigDecimal restBoxes = new BigDecimal("0.0000"); // 该门店未装车余货箱子
            String storeNo = store.getStoreNo();
            List<Long> countedContainerIds = new ArrayList<Long>();
            List<TaskInfo> qcDoneInfos = this.getQcDoneTaskInfoByStoreNo(storeNo);
            if (qcDoneInfos.size() > 0) {
                for (TaskInfo info : qcDoneInfos) {
                    TuDetail tuDetail = iTuRpcService.getDetailByBoardId(info.getContainerId());
                    if (null != tuDetail) {     //查到tudetail就是撞车了
                        continue;
                    }
                    totalBoxes = totalBoxes.add(info.getTaskPackQty()); //总箱数
                    //是否余货
                    if (info.getFinishTime() < DateUtils.getTodayBeginSeconds()) {
                        restBoxes = restBoxes.add(info.getTaskPackQty());
                    }
                }
            }
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("storeNo", store.getStoreNo());
            result.put("storeName", store.getStoreName());
            result.put("address", store.getAddress());
            result.put("totalBoxes", totalBoxes);
            result.put("restBoxes", restBoxes);
            results.add(result);
        }
        return results;
    }

    /**
     * 组盘列表的total
     *
     * @param mapQuery
     * @return
     * @throws BizCheckedException
     */
    public Integer countGroupList(Map<String, Object> mapQuery) throws BizCheckedException {
        mapQuery.put("scale", StoreConstant.SCALE_STORE); // 小店
        mapQuery.put("isOpen", 1);
        Integer total = storeService.countBaseinfoStore(mapQuery);
        return total;
    }

    /**
     * 获取门店的组盘详情
     *
     * @param storeNo
     * @return 《containerId,map《string,object》》
     * @throws BizCheckedException
     */
    public Map<Long, Map<String, Object>> getGroupDetailByStoreNo(String storeNo) throws BizCheckedException {
        Map<Long, Map<String, Object>> results = new HashMap<Long, Map<String, Object>>();
        List<TaskInfo> qcDoneTaskinfos = this.getQcDoneTaskInfoByStoreNo(storeNo);
        if (null == qcDoneTaskinfos || qcDoneTaskinfos.size() < 1) {
            return results;
        }
        //taskinfo里面的qc托盘码唯一
        for (TaskInfo info : qcDoneTaskinfos) {
            Long containerId = info.getContainerId(); //获取托盘
            // 未装车的
            TuDetail tuDetail = tuService.getDetailByBoardId(containerId);
            Boolean needCount = true;
            if (null != tuDetail) {    //一旦能查到就是装车了
                needCount = false;
            }
            if (!needCount) {
                continue;
            }
            // todo
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("containerId", containerId);
            result.put("markContainerId", info.getContainerId());  //当前作为查找板子码标识的物理托盘码,随机选的
            result.put("containerCount", 1);
            result.put("packCount", info.getTaskPackQty()); //总箱数
            result.put("turnoverBoxCount", info.getExt3()); //周转箱
            result.put("storeNo", storeNo);
            result.put("isExpensive", false);   //贵品默认是部署的
            if (info.getFinishTime() < DateUtils.getTodayBeginSeconds()) {
                result.put("isRest", true);
            } else {
                result.put("isRest", false);
            }
            results.put(containerId, result);
        }
        return results;
    }

    /**
     * 通过门店号获取Taskinfo聚类qc完的托盘
     * 考虑到以后taskinfo的托盘可能复用,一个托盘码可能有多个完成的qc任务
     *
     * @param storeNo
     * @return
     */
    public List<TaskInfo> getQcDoneTaskInfoByStoreNo(String storeNo) {
        List<BaseinfoLocation> locations = locationService.getCollectionByStoreNo(storeNo); // 门店对应的集货道
        List<TaskInfo> qcDoneInfos = new ArrayList<TaskInfo>();
        //先去集货位拿到所有的托盘的wave_detailList
        List<WaveDetail> waveDetailList = new ArrayList<WaveDetail>();
        for (BaseinfoLocation location : locations) {
            List<StockQuant> quants = stockQuantService.getQuantsByLocationId(location.getLocationId());
            for (StockQuant quant : quants) {
                Long containerId = quant.getContainerId();
                List<WaveDetail> waveDetails = waveService.getAliveDetailsByContainerId(containerId);
                if (null == waveDetails || waveDetails.size() < 1) {
                    continue;
                }
                waveDetailList.addAll(waveDetails);
            }
        }
        //拿qctaskId 没有qc不会写入wave
        HashSet<Long> qcTaskIds = new HashSet<Long>();
        if (waveDetailList.size() > 0) {
            for (WaveDetail detail : waveDetailList) {
                if (!detail.getQcTaskId().equals(0L)) {  //有qc任务
                    qcTaskIds.add(detail.getQcTaskId());
                }
            }
        }
        //过滤完成的qc任务
        if (qcTaskIds.size() > 0) {
            for (Long qcTaskId : qcTaskIds) {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("taskId", qcTaskId);
                params.put("type", TaskConstant.TYPE_QC);
                params.put("status", TaskConstant.Done);
                List<TaskInfo> taskInfos = baseTaskService.getTaskInfoList(params);
                if (null != taskInfos && taskInfos.size() > 0) {
                    qcDoneInfos.add(taskInfos.get(0));
                }
            }
        }
        return qcDoneInfos;
    }
}
