package com.lsh.wms.service.outbound;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.api.service.csi.ICsiRpcService;
import com.lsh.wms.api.service.pick.IQCRpcService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.api.service.tu.ITuRpcService;
import com.lsh.wms.core.constant.*;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.store.StoreService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.core.service.tu.TuService;
import com.lsh.wms.core.service.utils.PackUtil;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.baseinfo.BaseinfoStore;
import com.lsh.wms.model.csi.CsiSku;
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
    @Reference
    private ICsiRpcService csiRpcService;

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
            List<WaveDetail> waveDetails = this.getQcWaveDetailsByStoreNo(storeNo);
            List<TaskInfo> qcDoneInfos = this.getQcDoneTaskInfoByWaveDetails(waveDetails);
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
        List<WaveDetail> waveDetails = this.getQcWaveDetailsByStoreNo(storeNo);
         List<TaskInfo> qcDoneTaskinfos = this.getQcDoneTaskInfoByWaveDetails(waveDetails);
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
            // todo贵品
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
     * @return
     */
    public List<TaskInfo> getQcDoneTaskInfoByWaveDetails(List<WaveDetail> waveDetails) {
        List<TaskInfo> qcDoneInfos = new ArrayList<TaskInfo>();
        //拿qctaskId 没有qc不会写入wave
        HashSet<Long> qcTaskIds = new HashSet<Long>();
        if (waveDetails.size() > 0) {
            for (WaveDetail detail : waveDetails) {
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

    public List<WaveDetail> getQcWaveDetailsByStoreNo(String storeNo) {
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
        return waveDetailList;
    }

    public boolean repairExceptionRf(Map<String, Object> request) throws BizCheckedException {
        Long containerId = Long.valueOf(request.get("containerId").toString());
        String code = request.get("code").toString();
        BigDecimal qtyUom = new BigDecimal(request.get("uomQty").toString());   //可以是箱数或EA数量
        BigDecimal pickQty = new BigDecimal("0.0000");  //拣货数量
        if (null == containerId || null == code) {
            throw new BizCheckedException("2120019");
        }
        CsiSku skuInfo = csiRpcService.getSkuByCode(CsiConstan.CSI_CODE_TYPE_BARCODE, code);
        if (skuInfo == null) {
            throw new BizCheckedException("2120001");
        }
        long skuId = skuInfo.getSkuId();
        //以商品为维度,根据skuId和containerId找wave_detail的
        List<WaveDetail> waveDetails = waveService.getDetailByContainerIdAndSkuId(containerId, skuId);
        if (null == waveDetails || waveDetails.size() < 1) {
            throw new BizCheckedException("2120018");
        }
        BigDecimal normalQty = new BigDecimal("0.0000");    //除了最后那条detail的正常的数量
        //缺交判断
        for (WaveDetail d : waveDetails) {
            pickQty = pickQty.add(d.getPickQty());
            if (d.getQcException() == WaveConstant.QC_EXCEPTION_NORMAL) {    //正常的
                normalQty = normalQty.add(d.getQcQty());
            }
        }

        BigDecimal qty = PackUtil.UomQty2EAQty(qtyUom, waveDetails.get(0).getAllocUnitName());
        if (pickQty.compareTo(qty) <= 0) {   //多货或者数量相同
            throw new BizCheckedException("2120021");
        }

        /**
         * 忽略异常
         * 数量写在有异常的那条
         * 设置库存的qc的数量
         * 残次不追责
         */
        for (WaveDetail detail : waveDetails) {
            if (detail.getQcException() != WaveConstant.QC_EXCEPTION_NORMAL) {
                //残次不追责
                if (WaveConstant.QC_EXCEPTION_DEFECT == detail.getQcException()) {
                    //设置qc数量
                    detail.setQcFault(WaveConstant.QC_FAULT_NOMAL);
                    detail.setQcFaultQty(new BigDecimal("0.0000"));
                } else { //非残次拣货人的责任
                    detail.setQcFault(WaveConstant.QC_FAULT_PICK);
                    detail.setQcFaultQty(detail.getQcExceptionQty().abs());
                }
                detail.setQcQty(qty.subtract(normalQty));   //前面的都是正常的,有异常那条记录异常的数量
                detail.setQcExceptionDone(PickConstant.QC_EXCEPTION_DONE_SKIP);
                waveService.updateDetail(detail);
            }
        }

        //检验修复完毕
        boolean result = true;
        for (WaveDetail d : waveDetails) {
            if (d.getQcExceptionDone() == WaveConstant.QC_EXCEPTION_STATUS_UNDO) {
                result = false;
            }
        }
        return result;
    }

    public boolean fallbackExceptionRf(Map<String, Object> request) throws BizCheckedException {
        Long containerId = Long.valueOf(request.get("containerId").toString());
        BigDecimal qtyUom = new BigDecimal(request.get("uomQty").toString());   //复QC的数量
        String code = request.get("code").toString();
        BigDecimal pickQty = new BigDecimal("0.0000");  //拣货数量

        if (null == containerId || null == code) {
            throw new BizCheckedException("2120019");
        }
        CsiSku skuInfo = csiRpcService.getSkuByCode(CsiConstan.CSI_CODE_TYPE_BARCODE, code);
        if (skuInfo == null) {
            throw new BizCheckedException("2120001");
        }
        long skuId = skuInfo.getSkuId();
        //以商品为维度,根据skuId和containerId找wave_detail的
        List<WaveDetail> waveDetails = waveService.getDetailByContainerIdAndSkuId(containerId, skuId);
        if (null == waveDetails || waveDetails.size() < 1) {
            throw new BizCheckedException("2120018");
        }

        //数量判断
        for (WaveDetail d : waveDetails) {
            pickQty = pickQty.add(d.getPickQty());
        }
        BigDecimal qty = PackUtil.UomQty2EAQty(qtyUom, waveDetails.get(0).getAllocUnitName());
        if (pickQty.compareTo(qty) != 0) {   //多货或者数量相同
            throw new BizCheckedException("2120023");
        }
        //责任变更,记录数量
        for (WaveDetail d : waveDetails) {
            if (d.getQcException() != WaveConstant.QC_EXCEPTION_NORMAL){
                //残次不追责
                if (WaveConstant.QC_EXCEPTION_DEFECT == d.getQcException()){
                    //设置qc数量
                    d.setQcFault(WaveConstant.QC_FAULT_NOMAL);
                    d.setQcFaultQty(new BigDecimal("0.0000"));
                }else {
                    d.setQcFault(WaveConstant.QC_FAULT_QC);
                    d.setQcFaultQty(d.getQcExceptionQty().abs());
                }
                d.setQcExceptionDone(PickConstant.QC_EXCEPTION_DONE_DONE);
                d.setQcQty(d.getPickQty());
                d.setQcExceptionQty(new BigDecimal("0.0000"));
                d.setQcException(WaveConstant.QC_EXCEPTION_NORMAL);
                waveService.updateDetail(d);
            }
        }
        //检验修复完毕
        boolean result = true;
        for (WaveDetail d : waveDetails) {
            if (d.getQcExceptionDone() == WaveConstant.QC_EXCEPTION_STATUS_UNDO) {
                result = false;
            }
        }
        return result;
    }

    public boolean skipExceptionRf(Map<String, Object> request) throws BizCheckedException {
        Long containerId = Long.valueOf(request.get("containerId").toString());
        String code = request.get("code").toString();
        BigDecimal qtyUom = new BigDecimal(request.get("uomQty").toString());   //可以是箱数或EA数量
        BigDecimal pickQty = new BigDecimal("0.0000");  //拣货数量
        if (null == containerId || null == code) {
            throw new BizCheckedException("2120019");
        }
        CsiSku skuInfo = csiRpcService.getSkuByCode(CsiConstan.CSI_CODE_TYPE_BARCODE, code);
        if (skuInfo == null) {
            throw new BizCheckedException("2120001");
        }
        long skuId = skuInfo.getSkuId();
        //以商品为维度,根据skuId和containerId找wave_detail的
        List<WaveDetail> waveDetails = waveService.getDetailByContainerIdAndSkuId(containerId, skuId);
        if (null == waveDetails || waveDetails.size() < 1) {
            throw new BizCheckedException("2120018");
        }
        BigDecimal normalQty = new BigDecimal("0.0000");    //除了最后那条detail的正常的数量
        //缺交判断
        for (WaveDetail d : waveDetails) {
            pickQty = pickQty.add(d.getPickQty());
            if (d.getQcException() == WaveConstant.QC_EXCEPTION_NORMAL) {    //正常的
                normalQty = normalQty.add(d.getQcQty());
            }
        }

        BigDecimal qty = PackUtil.UomQty2EAQty(qtyUom, waveDetails.get(0).getAllocUnitName());
        if (pickQty.compareTo(qty) <= 0) {   //多货或者数量相同
            throw new BizCheckedException("2120021");
        }

        /**
         * 忽略异常
         * 数量写在有异常的那条
         * 设置库存的qc的数量
         * 残次不追责
         */
        for (WaveDetail detail : waveDetails) {
            if (detail.getQcException() != WaveConstant.QC_EXCEPTION_NORMAL) {
                //残次不追责
                if (WaveConstant.QC_EXCEPTION_DEFECT == detail.getQcException()) {
                    //设置qc数量
                    detail.setQcFault(WaveConstant.QC_FAULT_NOMAL);
                    detail.setQcFaultQty(new BigDecimal("0.0000"));
                } else { //非残次拣货人的责任
                    detail.setQcFault(WaveConstant.QC_FAULT_PICK);
                    detail.setQcFaultQty(detail.getQcExceptionQty().abs());
                }
                detail.setQcQty(qty.subtract(normalQty));   //前面的都是正常的,有异常那条记录异常的数量
                detail.setQcExceptionDone(PickConstant.QC_EXCEPTION_DONE_SKIP);
                waveService.updateDetail(detail);
            }
        }

        //检验修复完毕
        boolean result = true;
        for (WaveDetail d : waveDetails) {
            if (d.getQcExceptionDone() == WaveConstant.QC_EXCEPTION_STATUS_UNDO) {
                result = false;
            }
        }
        return result;
    }
}
