package com.lsh.wms.rf.service.outbound;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.q.Module.Base;
import com.lsh.wms.api.service.location.ILocationRpcService;
import com.lsh.wms.api.service.pick.IShipRestService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.stock.IStockQuantRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.api.service.tmstu.ITmsTuRpcService;
import com.lsh.wms.api.service.tu.ITuRpcService;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.constant.TuConstant;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.tu.TuService;
import com.lsh.wms.core.service.utils.IdGenerator;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.so.ObdHeader;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.stock.StockQuantCondition;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.tu.TuDetail;
import com.lsh.wms.model.tu.TuEntry;
import com.lsh.wms.model.tu.TuHead;
import com.lsh.wms.model.wave.WaveDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.parsing.Location;

import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by zengwenjun on 16/7/30.
 */
@Service(protocol = "rest")
@Path("outbound/ship")
@Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class ShipRestService implements IShipRestService {
    private static Logger logger = LoggerFactory.getLogger(ShipRestService.class);

    @Reference
    ITaskRpcService iTaskRpcService;
    @Autowired
    WaveService waveService;
    @Reference
    ILocationRpcService iLocationRpcService;
    @Autowired
    LocationService locationService;
    @Reference
    IStockQuantRpcService stockQuantRpcService;
    @Autowired
    private StockQuantService stockQuantService;
    @Reference
    private ITuRpcService iTuRpcService;
    @Autowired
    protected IdGenerator idGenerator;
    @Autowired
    private TuService tuService;
    @Autowired
    private SoOrderService soOrderService;
    @Reference
    private ITmsTuRpcService iTmsTuRpcService;

    @Path("releaseCollectionRoad")
    @POST
    public String releaseCollectionRoad() throws BizCheckedException {
        //看下位置上是否有货
        Map<String, Object> mapRequest = RequestUtils.getRequest();
        Long locationId = iLocationRpcService.getLocationIdByCode(mapRequest.get("locationCode").toString());
        BaseinfoLocation location = iLocationRpcService.getLocation(locationId);
        if (location.getType() != LocationConstant.COLLECTION_ROAD) {
            throw new BizCheckedException("2130011");
        }
        if (location.getIsLocked() == 0) {
            throw new BizCheckedException("2130012");
        }
        StockQuantCondition condition = new StockQuantCondition();
        condition.setLocationId(locationId);
        java.math.BigDecimal qty = stockQuantRpcService.getQty(condition);
        if (qty.compareTo(BigDecimal.ZERO) != 0) {
            throw new BizCheckedException("2130010");
        } else {
            //释放集货导
            BaseinfoLocation curLocation = new BaseinfoLocation();
            curLocation.setLocationId(locationId);
            locationService.unlockLocationAndSetCanUse(curLocation);
        }
        return JsonUtils.SUCCESS(new HashMap<String, Boolean>() {
            {
                put("response", true);
            }
        });
    }

    /**
     * todo 修改一键装车,创建tu,装车
     *
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("quickLoad")
    public String quickLoad() throws BizCheckedException {
        Map<String, Object> mapRequest = RequestUtils.getRequest();
        String locationCode = mapRequest.get("locationCode").toString();
        Long loadUid = Long.valueOf(RequestUtils.getHeader("uid").toString());
        if (null == locationCode || locationCode.equals("")) {
            throw new BizCheckedException("2180008");
        }
        BaseinfoLocation collection = iLocationRpcService.getLocationByCode(locationCode);
        if (collection == null || collection.getType() != LocationConstant.COLLECTION_ROAD) {
            throw new BizCheckedException("2180026");
        }
        //封装所有订单
        Set<Long> orderIds = new HashSet<Long>();
        Long collectionId = collection.getLocationId();
        //获取库存
        List<StockQuant> stockQuants = stockQuantService.getQuantsByLocationId(collectionId);
        if (null == stockQuants || stockQuants.size() < 1) {
            throw new BizCheckedException("2130013");
        }
        //找到所有的托盘
        Set<Long> containterIds = new HashSet<Long>();
        for (StockQuant quant : stockQuants) {
            containterIds.add(quant.getContainerId());
        }

        List<TaskInfo> qcInfos = new ArrayList<TaskInfo>();
        Set<Long> qcTaskIdDup = new HashSet<Long>();
        //通过合盘的托盘码聚合
        Map<Long, List<TaskInfo>> mergedQcListMap = new HashMap<Long, List<TaskInfo>>();
        //封装托盘码和
        //同一个mergeContainerId的qc的list
        List<TaskInfo> qcInfoList = null;
        for (Long containerId : containterIds) {
            //判断是否组盘完成,先去listdetail总找组盘
            List<WaveDetail> waveDetails = waveService.getAliveDetailsByContainerId(containerId);
            if (null == waveDetails || waveDetails.size() < 1) {
                throw new BizCheckedException("2880012");
            }
            //以qc维度聚类
            for (WaveDetail detail : waveDetails) {
                if (detail.getQcTaskId() == 0) {
                    throw new BizCheckedException("2870034");
                }
                if (qcTaskIdDup.contains(detail.getQcTaskId())) {
                    continue;
                } else {
                    TaskInfo qcInfo = iTaskRpcService.getTaskInfo(detail.getQcTaskId());
                    //没qc完成
                    if (null == qcInfo || !TaskConstant.Done.equals(qcInfo.getStatus())) {
                        throw new BizCheckedException("2870034");
                    }
                    //判断合盘的是否被运单运走了
                    List<TuDetail> tuDetails = tuService.getTuDeailListByMergedContainerId(qcInfo.getMergedContainerId());
                    if (null != tuDetails && tuDetails.size() > 0) {
                        for (TuDetail tuDetail : tuDetails) {
                            TuHead tuHead = tuService.getHeadByTuId(tuDetail.getTuId());
                            if (!TuConstant.SHIP_OVER.equals(tuHead.getStatus())) {
                                throw new BizCheckedException("2130014");
                            }
                        }
                    }

                    qcInfos.add(qcInfo);
                    qcTaskIdDup.add(detail.getQcTaskId());

                    //聚类qc
                    if (!mergedQcListMap.containsKey(qcInfo.getMergedContainerId())) {
                        qcInfoList = new ArrayList<TaskInfo>();
                        mergedQcListMap.put(qcInfo.getMergedContainerId(), qcInfoList);
                    }
                    qcInfoList = mergedQcListMap.get(qcInfo.getMergedContainerId());
                    qcInfoList.add(qcInfo);
                    mergedQcListMap.put(qcInfo.getMergedContainerId(), qcInfoList);
                }
                orderIds.add(detail.getOrderId());
            }
        }
        //根据订单获取线路号
        if (null == orderIds || orderIds.isEmpty()) {
            throw new BizCheckedException("2990053");
        }
        //取第一线路号
        ObdHeader obdHeader = soOrderService.getOutbSoHeaderByOrderId(orderIds.iterator().next());
        //线路号
        String transPlan = obdHeader.getTransPlan();
        //请求tms获取数据
        TuHead driverInfo = iTmsTuRpcService.requestTMSGetDriverInfo(transPlan);

        //创建TuEntry并且回写tuDetailId到waveDetail中
        tuService.createTuEntryByQcListAndDriverInfo(loadUid, locationCode, mergedQcListMap, driverInfo);

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("response", true);
        return JsonUtils.SUCCESS(result);
    }


    /**
     * todo 修改获取集货道的所有东西
     *
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("showCollectionInfo")
    public String showCollectionInfo() throws BizCheckedException {
        Map<String, Object> mapRequest = RequestUtils.getRequest();
        String locationCode = mapRequest.get("locationCode").toString();
        Long loadUid = Long.valueOf(RequestUtils.getHeader("uid").toString());
        //结果集分装
        Map<String, Object> result = new HashMap<String, Object>();
        if (null == locationCode || locationCode.equals("")) {
            throw new BizCheckedException("2180008");
        }
        BaseinfoLocation collection = iLocationRpcService.getLocationByCode(locationCode);
        if (collection == null || collection.getType() != LocationConstant.COLLECTION_ROAD) {
            throw new BizCheckedException("2180026");
        }
        Long collectionId = collection.getLocationId();
        //获取库存
        List<StockQuant> stockQuants = stockQuantService.getQuantsByLocationId(collectionId);
        if (null == stockQuants || stockQuants.size() < 1) {
            throw new BizCheckedException("2130013");
        }
        //找到所有的托盘
        Set<Long> containterIds = new HashSet<Long>();
        for (StockQuant quant : stockQuants) {
            containterIds.add(quant.getContainerId());
        }
        //封装所有的qc任务做箱数加和
        List<TaskInfo> qcInfos = new ArrayList<TaskInfo>();
        Set<Long> qcTaskIdDup = new HashSet<Long>();
        //封装containId|mergedContainerId 的detail
        Map<Long, List<WaveDetail>> containerId2WaveDetails = new HashMap<Long, List<WaveDetail>>();
        //qc 维度去找客户
        //一个托盘一个客户? 如果多客户一托盘,qc也不知道每个具体的箱数,用detail里的orderId是合理的
        Set<Long> orderIds = new HashSet<Long>();
        for (Long containerId : containterIds) {

            //判断是否组盘完成,先去listdetail总找组盘
            List<WaveDetail> waveDetails = waveService.getAliveDetailsByContainerId(containerId);
            if (null == waveDetails || waveDetails.size() < 1) {
                throw new BizCheckedException("2880012");
            }
            for (WaveDetail detail : waveDetails) {
                if (detail.getQcTaskId() == 0) {
                    throw new BizCheckedException("2870034");
                }
                orderIds.add(detail.getOrderId());
                if (qcTaskIdDup.contains(detail.getQcTaskId())) {
                    continue;
                } else {
                    TaskInfo qcInfo = iTaskRpcService.getTaskInfo(detail.getQcTaskId());
                    //没qc完成
                    if (null == qcInfo || !TaskConstant.Done.equals(qcInfo.getStatus())) {
                        throw new BizCheckedException("2870034");
                    }
                    //判断合盘的是否被运单运走了
                    List<TuDetail> tuDetails = tuService.getTuDeailListByMergedContainerId(qcInfo.getMergedContainerId());
                    if (null != tuDetails && tuDetails.size() > 0) {
                        for (TuDetail tuDetail : tuDetails) {
                            TuHead tuHead = tuService.getHeadByTuId(tuDetail.getTuId());
                            if (!TuConstant.SHIP_OVER.equals(tuHead.getStatus())) {
                                throw new BizCheckedException("2130014");
                            }
                        }
                    }

                    qcInfos.add(qcInfo);
                    qcTaskIdDup.add(detail.getQcTaskId());
                }
            }
        }

        //客户id
        Set<String> customerIds = new HashSet<String>();
        List<Map<String, Object>> customerInfos = new ArrayList<Map<String, Object>>();
        Set<String> transPlanSet = new HashSet<String>();
        for (Long orderId : orderIds) {
            ObdHeader obdHeader = soOrderService.getOutbSoHeaderByOrderId(orderId);
            if (null != obdHeader) {
                if (!customerIds.contains(obdHeader.getDeliveryCode())) {
                    customerIds.add(obdHeader.getDeliveryCode());
                    Map<String, Object> customerInfo = new HashMap<String, Object>();
                    customerInfo.put("customerName", obdHeader.getDeliveryName());
                    customerInfo.put("customerCode", obdHeader.getDeliveryCode());
                    customerInfo.put("customerAddress", obdHeader.getDeliveryAddrs());
                    customerInfos.add(customerInfo);
                }
                transPlanSet.add(obdHeader.getTransPlan());
            }
        }
        if (transPlanSet.isEmpty()) {
            throw new BizCheckedException("2990052");
        }
        //获取第一个线路号
        String transCode = transPlanSet.iterator().next();
        TuHead tuHead = iTmsTuRpcService.requestTMSGetDriverInfo(transCode);
        //显示


//        //拼接线路编号线路号只能有一个,因为线路号只能是一车
////        String transPlan = "";
//        StringBuilder transPlan = new StringBuilder();
//        int count = 0;
//        for (String one : transPlanSet) {
//            if (count == transPlanSet.size() - 1) {
//                transPlan.append(one);
//            } else {
//                transPlan.append(one);
//                transPlan.append(",");
//            }
//            count++;
//        }
        //关于司机信息

        result.put("transPlan", transCode);
        result.put("driverName", "");
        result.put("carNumber", "");
        result.put("cellphone", "");
        if (null != tuHead.getCarNumber()
                || null != tuHead.getTransUid()
                || null != tuHead.getCellphone()
                || null != tuHead.getName()
                ) {
            result.put("driverName", tuHead.getName());
            result.put("carNumber", tuHead.getCarNumber());
            result.put("cellphone", tuHead.getCellphone());
        }

        //箱数和周转箱的统计
        Long packCount = 0L;
        Long turnoverBoxNum = 0L;
        for (TaskInfo info : qcInfos) {
            packCount += info.getExt4(); //箱数
            turnoverBoxNum += info.getExt3(); //周转箱数
        }
        //客户数
        int customerCount = customerInfos.size();
        result.put("packCount", packCount);
        result.put("turnoverBoxNum", turnoverBoxNum);
        result.put("customerCount", customerCount);
        result.put("customerList", customerInfos);
        return JsonUtils.SUCCESS(result);
    }

    @Path("scan")
    @POST
    public String scan() throws BizCheckedException {
        Map<String, Object> mapRequest = RequestUtils.getRequest();
        Long locationId = iLocationRpcService.getLocationIdByCode(mapRequest.get("locationCode").toString());
        HttpSession session = RequestUtils.getSession();
        //get task  by containerId
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("locationId", locationId);
        //mapQuery.put("");
        List<TaskEntry> tasks = iTaskRpcService.getTaskHeadList(TaskConstant.TYPE_SHIP, mapQuery);
        if (tasks.size() != 1) {
            throw new BizCheckedException("2130008");
        }
        TaskInfo info = tasks.get(0).getTaskInfo();
        //if(info.)
        iTaskRpcService.assign(info.getTaskId(), 0L);//Long.valueOf((String) session.getAttribute("uid")));
        return JsonUtils.SUCCESS();
    }

    @Path("scanContainer")
    @POST
    public String scanContainer() throws BizCheckedException {
        Map<String, Object> mapRequest = RequestUtils.getRequest();
        Long containerId = Long.valueOf(mapRequest.get("containerId").toString());
        HttpSession session = RequestUtils.getSession();
        Long uid = 0L;//Long.valueOf((String) session.getAttribute("uid"));
        List<WaveDetail> details = waveService.getDetailsByContainerId(containerId);
        for (WaveDetail d : details) {
            d.setShipAt(DateUtils.getCurrentSeconds());
            d.setShipUid(uid);
        }
        if (details.size() == 0) {
            throw new BizCheckedException("2130001");
        }
        waveService.updateDetails(details);
        //get task  by containerId
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("containerId", containerId);
        //mapQuery.put("");
        List<TaskEntry> tasks = iTaskRpcService.getTaskHeadList(TaskConstant.TYPE_SHIP, mapQuery);
        if (tasks.size() > 1) {
            throw new BizCheckedException("2130008");
        }
        if (tasks.size() == 0) {
            throw new BizCheckedException("2130009");
        }
        TaskInfo info = tasks.get(0).getTaskInfo();
        iTaskRpcService.assign(info.getTaskId(), 123123L);
        /*
        获取发货码头
         */
        Map<String, Object> rstMap = new HashMap<String, Object>();
        rstMap.put("dockName", "TESTDOCK");
        return JsonUtils.SUCCESS(rstMap);
    }

    @Path("confirm")
    @POST
    public String confirm() throws BizCheckedException {
        Map<String, Object> mapRequest = RequestUtils.getRequest();
        Long locationId = Long.valueOf(mapRequest.get("locationId").toString());
        List<WaveDetail> details = waveService.getDetailsByLocationId(locationId);
        for (WaveDetail d : details) {
            if (d.getShipAt() == 0) {
                throw new BizCheckedException("2130002");
            }
        }
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("locationId", locationId);
        List<TaskEntry> tasks = iTaskRpcService.getTaskHeadList(TaskConstant.TYPE_SHIP, mapQuery);
        if (tasks.size() != 1) {
            throw new BizCheckedException("2130008");
        }
        iTaskRpcService.done(tasks.get(0).getTaskInfo().getTaskId());
        return JsonUtils.SUCCESS();
    }

    @Path("createTask")
    @POST
    public String createTask() throws BizCheckedException {
        Map<String, Object> mapRequest = RequestUtils.getRequest();
        Long containerId = Long.valueOf(mapRequest.get("containerId").toString());
        List<WaveDetail> details = waveService.getDetailsByContainerId(containerId);
        for (WaveDetail detail : details) {
            if (detail.getQcExceptionDone() == 0) {
                throw new BizCheckedException("2130005");
            }
        }
        if (details.size() == 0) {
            throw new BizCheckedException("2130007");
        }
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("containerId", containerId);
        final List<TaskEntry> taskList = iTaskRpcService.getTaskHeadList(TaskConstant.TYPE_SHIP, mapQuery);
        if (taskList.size() > 0) {
            throw new BizCheckedException("2130003");
        }
        TaskEntry entry = new TaskEntry();
        TaskInfo info = new TaskInfo();
        info.setType(TaskConstant.TYPE_SHIP);
        info.setContainerId(containerId);
        info.setLocationId(details.get(0).getRealCollectLocation());
        entry.setTaskDetailList((List<Object>) (List<?>) details);
        entry.setTaskInfo(info);
        Long taskId = iTaskRpcService.create(TaskConstant.TYPE_SHIP, entry);
        return JsonUtils.SUCCESS(new HashMap<String, Boolean>() {
            {
                put("response", true);
            }
        });
    }

    /*
    @Path("createTask")
    @POST
    public String createTask() throws BizCheckedException {
        Map<String, Object> mapRequest = RequestUtils.getRequest();
        Long locationId = Long.valueOf(mapRequest.get("locationId").toString());
        if(iLocationRpcService.getLocation(locationId)==null){
            throw new BizCheckedException("2130006");
        }
        List<BaseinfoLocation> locations = iLocationRpcService.getNextLevelLocations(locationId);
        List<WaveDetail> details = waveService.getDetailsByLocationId(locationId);
        if(locations != null) {
            for (BaseinfoLocation location : locations) {
                List<WaveDetail> subDetails = waveService.getDetailsByLocationId(location.getLocationId());
                details.addAll(subDetails);
            }
        }
        for(WaveDetail detail : details){
            if(detail.getQcExceptionDone()==0){
                throw new BizCheckedException("2130005");
            }
        }
        if(details.size()==0){
            throw new BizCheckedException("2130007");
        }
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("locationId", locationId);
        final List<TaskEntry> taskList = iTaskRpcService.getTaskHeadList(TaskConstant.TYPE_SHIP, mapQuery);
        if(taskList.size()>0){
            throw new BizCheckedException("2130003");
        }
        TaskEntry entry = new TaskEntry();
        TaskInfo info = new TaskInfo();
        info.setType(TaskConstant.TYPE_SHIP);
        info.setLocationId(locationId);
        entry.setTaskDetailList((List<Object>)(List<?>)details);
        entry.setTaskInfo(info);
        Long taskId = iTaskRpcService.create(TaskConstant.TYPE_SHIP, entry);
        return JsonUtils.SUCCESS();
    }
    */
}
