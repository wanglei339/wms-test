package com.lsh.wms.rf.service.outbound;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.api.service.container.IContainerRpcService;
import com.lsh.wms.api.service.csi.ICsiRpcService;
import com.lsh.wms.api.service.item.IItemRpcService;
import com.lsh.wms.api.service.location.ILocationRpcService;
import com.lsh.wms.api.service.pick.IQCRpcService;
import com.lsh.wms.api.service.pick.IRFQCRestService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.so.ISoRpcService;
import com.lsh.wms.api.service.stock.IStockQuantRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.CsiConstan;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.constant.WaveConstant;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.core.service.utils.PackUtil;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.baseinfo.BaseinfoContainer;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.csi.CsiSku;
import com.lsh.wms.model.so.ObdHeader;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.wave.WaveDetail;
import com.lsh.wms.model.wave.WaveQcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by zengwenjun on 16/7/30.
 */


@Service(protocol = "rest")
@Path("outbound/qc")
@Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class QCRestService implements IRFQCRestService {
    private static Logger logger = LoggerFactory.getLogger(QCRestService.class);
    @Reference
    private ICsiRpcService csiRpcService;
    @Autowired
    private WaveService waveService;
    @Reference
    private IItemRpcService itemRpcService;
    @Reference
    private ITaskRpcService iTaskRpcService;
    @Autowired
    private BaseTaskService baseTaskService;
    @Reference
    private IContainerRpcService iContainerRpcService;
    @Reference
    private ISoRpcService iSoRpcService;
    @Reference
    private ILocationRpcService iLocationRpcService;
    @Autowired
    private StockQuantService stockQuantService;
    @Reference
    private IQCRpcService iqcRpcService;

    /**
     * 扫码获取qc任务详情
     * 输入捡货签或者托盘嘛,捡货签优先,托盘码其次
     *
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("scan")
    public String scan() throws BizCheckedException {
        Map<String, Object> mapRequest = RequestUtils.getRequest();
        Long pickTaskId = 0L;
        TaskInfo pickTaskInfo = null;
        Long containerId = 0L;
        TaskInfo qcTaskInfo = null;
        boolean isDirect = false;   //直流跳过明细qc的开关,true是跳过
        //判断是拣货签还是托盘码
        //pickTaskId拣货签12开头,18位的长度
        String code = (String) mapRequest.get("code");
        String firstTwoCode = code.substring(0, 2);
        if (code.toString().length() == 18 && firstTwoCode.equals("12")) {
            mapRequest.put("pickTaskId", code);
        } else {
            mapRequest.put("containerId", code);
        }
        //参数获取和初始化
        if (mapRequest.get("pickTaskId") != null && mapRequest.get("pickTaskId").toString().compareTo("") != 0) {
            //根据捡货签做初始化
            pickTaskId = Long.valueOf(mapRequest.get("pickTaskId").toString());
            pickTaskInfo = iTaskRpcService.getTaskInfo(pickTaskId);
            if (pickTaskInfo == null) {
                throw new BizCheckedException("2060003");
            }
            containerId = pickTaskInfo.getContainerId();
        } else {
            //根据托盘码做初始化
            containerId = Long.valueOf(mapRequest.get("containerId").toString());
            Map<String, Object> mapQuery = new HashMap<String, Object>();
            mapQuery.put("containerId", containerId);
        }
        //获取QC任务
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("containerId", containerId);
        mapQuery.put("type", TaskConstant.TYPE_QC);
//        List<TaskEntry> tasks = iTaskRpcService.getTaskHeadList(TaskConstant.TYPE_QC, mapQuery);
        List<TaskInfo> tasks = baseTaskService.getTaskInfoList(mapQuery);
        //商品集合中需要记住有异常的wave_detail的id
        //扫托盘肯定不是拣货了,也有可能
        //根据container和QC的type 找出QC任务,并根据QC任务记录的前一个任务的taskid,找到具体的需要找到数

        if (null == tasks || tasks.size() == 0) {
            throw new BizCheckedException("2120007");
        }
        if (tasks != null && tasks.size() > 1) {
            throw new BizCheckedException("2120006");
        }
        qcTaskInfo = tasks.get(0);

        if (qcTaskInfo.getStatus() == TaskConstant.Draft) {
            iTaskRpcService.assign(qcTaskInfo.getTaskId(), Long.valueOf(RequestUtils.getHeader("uid")));
        }                                                                               // todo 可以解决 加入任务流状态的标示,根据任务流状态和container取 detail中去取
        List<WaveDetail> details = waveService.getDetailsByContainerId(containerId);    //一个托盘上是一个货主的货,需要taskId和container两个去确认了,因为肯定有任务不唯一,加生命周期
        if (details.size() == 0) {
            //空托盘
            throw new BizCheckedException("2120005");
        }
        //merge item_id 2 pick  qty
        Map<Long, BigDecimal> mapItem2PickQty = new HashMap<Long, BigDecimal>();
        Map<Long, WaveDetail> mapItem2WaveDetail = new HashMap<Long, WaveDetail>();
        //计算是拣货量,还是其他货量
        TaskInfo beforeTask = iTaskRpcService.getTaskInfo(qcTaskInfo.getQcPreviousTaskId());    //qc前一个任务量
        boolean isFirstQC = false;  //是否是第一次QC
        //聚类,计算总的QC量    前一个的任务量只在pickTaskQtc
        for (WaveDetail d : details) {
            if (d.getQcTimes() == WaveConstant.QC_TIMES_FIRST) {   //一旦有第一遍没QC的,就不是复Q
                isFirstQC = true;
            }
            if (mapItem2PickQty.get(d.getItemId()) == null) {
                mapItem2PickQty.put(d.getItemId(), new BigDecimal(d.getPickQty().toString()));
            } else {
                mapItem2PickQty.put(d.getItemId(), mapItem2PickQty.get(d.getItemId()).add(d.getPickQty()));
            }
            mapItem2WaveDetail.put(d.getItemId(), d);
            //跳过直流,一旦有组盘没有异常,就直接到组盘
            //一旦是直流大店的门店收货,需要忽略qc,直接进入
//            if (qcTaskInfo.getQcSkip().equals(TaskConstant.QC_SKIP) && !d.getQcException().equals(WaveConstant.QC_EXCEPTION_GROUP)) { //直流组盘不异常,跳过明细qc
//                //直流的大店门店收货跳过组盘的开关
//                isDirect = true;
//            }
        }

//        //如果是直流所有的直接跳转为已经QC了
//        //todo 以后组盘有异常,还需都更新回来,根据qcTaskInfo.getQcSkip(),变更所有的qcDone更改,修改
//        if (isDirect) {
//            for (WaveDetail one : details) {
//                one.setQcExceptionDone(2L);
//                one.setQcQty(one.getPickQty()); //先默认qc数量是正常的
//                waveService.updateDetail(one);
//                isFirstQC = false;
//            }
//        }

        //找出有责任的得detail,现有流程是一个商品只有一个挂有异常,把异常的detail的id、和商品绑定起来,--->那个商品的哪个detail有问题,需要修复
        int boxNum = 0;
        int allBoxNum = 0;
        boolean hasEA = false;
        List<Map<String, Object>> undoDetails = new LinkedList<Map<String, Object>>();
        for (Long itemId : mapItem2PickQty.keySet()) {
            WaveDetail waveDetail = mapItem2WaveDetail.get(itemId);
            Map<String, Object> detail = new HashMap<String, Object>();
            BaseinfoItem item = itemRpcService.getItem(itemId);
            detail.put("skuId", item.getSkuId());
            detail.put("itemId", item.getItemId());
            detail.put("code", item.getCode());
            detail.put("codeType", item.getCodeType());
            BigDecimal uomQty = PackUtil.EAQty2UomQty(mapItem2PickQty.get(itemId), waveDetail.getAllocUnitName());
            if (waveDetail.getAllocUnitName().compareTo("EA") == 0) {
                hasEA = true;
            } else {
                boxNum += (int) (uomQty.floatValue());
            }
            detail.put("uomQty", uomQty);
            detail.put("uom", waveDetail.getAllocUnitName());
            detail.put("isSplit", waveDetail.getAllocUnitName().compareTo("EA") == 0);
            //TODO packName
            detail.put("itemName", item.getSkuName());
            detail.put("isFristTime", waveDetail.getQcTimes() == WaveConstant.QC_TIMES_FIRST);
            //加入qc的状态
            detail.put("qcDone", waveDetail.getQcExceptionDone() != WaveConstant.QC_EXCEPTION_STATUS_UNDO);  //qc任务未处理的的判断  那种商品做,哪种商品没做

            //判断是第几次的QC,只有QC过一遍,再次QC都是复核QC
            detail.put("qcTimes", waveDetail.getQcTimes());
          //todo 如果有异常的话,直流的也要qc
            undoDetails.add(detail);
        }
        allBoxNum = boxNum;
        if (hasEA) {
            allBoxNum++;
        }
        //获取托盘信息
        BaseinfoContainer containerInfo = iContainerRpcService.getContainer(containerId);
        if (containerInfo == null) {
            throw new BizCheckedException("2000002");
        }
        //获取客户信息
        ObdHeader soInfo = iSoRpcService.getOutbSoHeaderDetailByOrderId(details.get(0).getOrderId());   //出库单,orderid
        if (null == soInfo) {
            throw new BizCheckedException("2120016");
        }
        //获取集货道信息去库存表中查位置,   系统设计 一个托盘只能在一个位置上
        List<Long> locationIds = stockQuantService.getLocationIdByContainerId(containerInfo.getContainerId());
        if (null == locationIds || locationIds.size() < 1) {
            throw new BizCheckedException("2180040");
        }
        BaseinfoLocation collectLocaion = iLocationRpcService.getLocation(locationIds.get(0));
        Map<String, Object> rstMap = new HashMap<String, Object>();
        rstMap.put("qcList", undoDetails);
        rstMap.put("isDirect", isDirect);
        rstMap.put("containerType", containerInfo.getType());
        rstMap.put("pickTaskId", qcTaskInfo.getQcPreviousTaskId().toString());
        //送达方的信息
        rstMap.put("customerId", soInfo.getDeliveryCode().toString());
        rstMap.put("customerName", soInfo.getDeliveryName());
        //todo 集货道可以去stockQuent中拿
        rstMap.put("collectionRoadCode", collectLocaion.getLocationCode());
        rstMap.put("itemLineNum", mapItem2PickQty.size());
        //TODO BOX NUM
        rstMap.put("allBoxNum", allBoxNum);
        rstMap.put("itemBoxNum", boxNum);
        //TODO 前端显示有问题,没显示周装箱的总数量
        rstMap.put("turnoverBoxNum", hasEA ? 1 : 0);
        rstMap.put("qcTaskDone", qcTaskInfo.getStatus() == TaskConstant.Done);
        rstMap.put("qcTaskId", qcTaskInfo.getTaskId().toString());
        rstMap.put("isFristQc", isFirstQC);
        return JsonUtils.SUCCESS(rstMap);
    }


    @POST
    @Path("qcOneItem")
    public String qcOneItem() throws BizCheckedException {
        //获取参数
        Map<String, Object> request = RequestUtils.getRequest();
        //在库拣货qc输入ea数量,直流按照EA或者箱子播种和收货
        long qcTaskId = Long.valueOf(request.get("qcTaskId").toString());
        BigDecimal qtyUom = new BigDecimal(request.get("uomQty").toString());   //可以是箱数或EA数量
        BigDecimal defectQty = new BigDecimal(request.get("defectQty").toString()); //可以是箱数或EA数量(两者是箱子的话都是箱子)
        long exceptionType = 0L;
        BigDecimal exceptionQty = new BigDecimal("0.0000");
        if (defectQty.compareTo(BigDecimal.ZERO) > 0) {
            exceptionType = WaveConstant.QC_EXCEPTION_DEFECT;   //残次异常  有残次,不追着
            exceptionQty = defectQty;
        }
        //初始化QC任务
        TaskInfo qcTaskInfo = iTaskRpcService.getTaskInfo(qcTaskId);
        if (qcTaskInfo == null) {
            throw new BizCheckedException("2120007");
        }
        //转换商品条形码为sku码
        String code = (String) request.get("code");
        CsiSku skuInfo = csiRpcService.getSkuByCode(CsiConstan.CSI_CODE_TYPE_BARCODE, code);
        if (skuInfo == null) {
            throw new BizCheckedException("2120001");
        }
        long skuId = skuInfo.getSkuId();
        List<WaveDetail> details = waveService.getDetailsByContainerId(qcTaskInfo.getContainerId());    //qc的数量不在是拣货的数量了,而是集货的数量和收货的数量
        // 标识是拣货生成的QC还是集货生成的QC
        //计算是拣货生成的|集货生成的|收货生成的
        int seekNum = 0;
        List<WaveDetail> matchDetails = new LinkedList<WaveDetail>();
        BigDecimal pickQty = new BigDecimal("0.0000");
        for (WaveDetail d : details) {
            if (d.getSkuId() != skuId) {
                continue;
            }
            seekNum++;
            matchDetails.add(d);
            pickQty = pickQty.add(d.getPickQty());
        }
        if (seekNum == 0) {
            if (true) {
                throw new BizCheckedException("2120002");
            }
            if (exceptionType != WaveConstant.QC_EXCEPTION_NOT_MATCH) {  //一件没找到,还不是错货
                throw new BizCheckedException("2120009");
            }
            WaveQcException qcException = new WaveQcException();
            qcException.setSkuId(skuInfo.getSkuId());
            qcException.setExceptionQty(exceptionQty);
            qcException.setExceptionType(exceptionType);
            qcException.setQcTaskId(qcTaskId);
            qcException.setWaveId(qcTaskInfo.getWaveId());
            waveService.insertQCException(qcException);
        } else {
            BigDecimal qty = PackUtil.UomQty2EAQty(qtyUom, matchDetails.get(0).getAllocUnitName());
            exceptionQty = PackUtil.UomQty2EAQty(exceptionQty, matchDetails.get(0).getAllocUnitName());
            if (exceptionQty.compareTo(qty) > 0) {
                throw new BizCheckedException("2120013");
            }
            int cmpRet = pickQty.compareTo(qty);    //拣货 - qc的数量
            if (cmpRet > 0) exceptionType = WaveConstant.QC_EXCEPTION_LACK; //多货
            if (cmpRet < 0) exceptionType = WaveConstant.QC_EXCEPTION_OVERFLOW; //少货
            BigDecimal curQty = new BigDecimal("0.0000");
            for (int i = 0; i < matchDetails.size(); ++i) {
                WaveDetail detail = matchDetails.get(i);
                BigDecimal lastQty = curQty;
                curQty = curQty.add(detail.getPickQty());
                detail.setQcQty(qty);
                detail.setQcAt(DateUtils.getCurrentSeconds());
                detail.setQcUid(Long.valueOf(RequestUtils.getHeader("uid")));
                detail.setQcException(exceptionType);
                detail.setQcAt(DateUtils.getCurrentSeconds());
                if (exceptionType != 0) {
                    //多货
                    if (i == matchDetails.size() - 1) {     //记录detail的id
                        detail.setQcException(exceptionType);
                        //设置成拣货人的责任
                        if (exceptionType == WaveConstant.QC_EXCEPTION_DEFECT) {
                            detail.setQcExceptionQty(exceptionQty);
                            //残次不追责
//                            detail.setQcFault(WaveConstant.QC_FAULT_NOMAL);
//                            detail.setQcFaultQty(exceptionQty); //残次不记录责任
                        } else {
                            detail.setQcExceptionQty(qty.subtract(curQty));
                            detail.setQcFaultQty(qty.subtract(curQty).abs());   //少货或者多货取绝对值
                            detail.setQcFault(WaveConstant.QC_FAULT_PICK); //永远是拣货人的责任,不修复的话
                        }
                        // 以后做有残次不追责
                        detail.setQcExceptionDone(0L);
                        detail.setQcQty(qty.subtract(lastQty));

                    } else {    //最后一个记录异常,其他的都正常
                        //忽略
                        detail.setQcQty(detail.getPickQty());
                        detail.setQcException(0L);
                        detail.setQcExceptionQty(BigDecimal.ZERO);
                        detail.setQcExceptionDone(1L);
                        detail.setQcQty(detail.getPickQty());
                        detail.setQcFault(WaveConstant.QC_FAULT_NOMAL);//无责任人
                        detail.setQcFaultQty(new BigDecimal("0.0000"));
                    }
                } else {
                    detail.setQcQty(detail.getPickQty());
                    detail.setQcException(0L);
                    detail.setQcExceptionQty(BigDecimal.ZERO);
                    detail.setQcExceptionDone(1L);
                    detail.setQcFault(WaveConstant.QC_FAULT_NOMAL);//无责任人
                    detail.setQcFaultQty(new BigDecimal("0.0000"));
                }
                detail.setQcTimes(WaveConstant.QC_TIMES_MORE);  //qc的次数变更
                waveService.updateDetail(detail);
            }
            qcTaskInfo.setExt2(exceptionType);
            TaskEntry entry = new TaskEntry();
            entry.setTaskInfo(qcTaskInfo);
            iTaskRpcService.update(TaskConstant.TYPE_QC, entry);
        }
        //校验qc任务是否完全完成;
        boolean bSucc = true;
        for (WaveDetail d : details) {
            if (d.getQcExceptionDone() == WaveConstant.QC_EXCEPTION_STATUS_UNDO) {  //未处理异常
                bSucc = false;
                break;
            }
            //计算QC的任务量  qc的数量,一个拣货任务的任务量,怎么算,一次qc的数量吗,复QC的人的任务量怎么算
            //TODO QC TaSK qTY
        }
        //返回结果
        Map<String, Object> rstMap = new HashMap<String, Object>();
        rstMap.put("qcDone", bSucc);
        return JsonUtils.SUCCESS(rstMap);
    }

    /**
     * 箱数boxNum 周转箱数是turnoverBoxNum   总箱数allboxNum= boxNum+turnoverBoxNum
     * 跳过qc明细 skip字段为ture,系统默认拣货数量(待qc数量)和qc相同
     *
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("confirm")
    public String confirm() throws BizCheckedException {
        Map<String, Object> request = RequestUtils.getRequest();
        //前端跳过q明细字段,跳过,系统默认拣货量和qc量的一致
        Boolean skip = Boolean.valueOf(request.get("skip").toString());

        long qcTaskId = Long.valueOf(request.get("qcTaskId").toString());
        long boxNum = Long.valueOf(request.get("boxNum").toString());
        long turnoverBoxNum = Long.valueOf(request.get("turnoverBoxNum").toString());
        //初始化QC任务
        TaskInfo qcTaskInfo = iTaskRpcService.getTaskInfo(qcTaskId);
        if (qcTaskInfo == null) {
            throw new BizCheckedException("2120007");
        }
        List<WaveDetail> details = waveService.getDetailsByContainerId(qcTaskInfo.getContainerId());

        //跳过qc明细,系统默认拣货量和qc量相等
        if (skip) {
            for (WaveDetail detail : details) {
                detail.setQcExceptionDone(1L);  // 正常不需要处理
                detail.setQcQty(detail.getPickQty()); //先默认qc数量是正常的
                waveService.updateDetail(detail);
            }
        }
        //校验qc任务是否完全完成;
        boolean bSucc = true;
        BigDecimal sumEAQty = new BigDecimal("0.0000");
        //直接走组盘,没有必须要进行判断是否QC异常完毕的东西,或者这里的异常是组盘异常,任务才不结束
        //不能组盘的时候,PC忽略异常
        for (WaveDetail d : details) {
            if (d.getQcExceptionDone() == 0) {
                bSucc = false;
                break;
            }
            sumEAQty = sumEAQty.add(d.getPickQty());
            //计算QC的任务量
            //TODO QC TaSK qTY
        }
        if (!bSucc) {
            throw new BizCheckedException("2120004");
        }
        if (bSucc) {
            //成功
            //设置task的信息;
            qcTaskInfo.setTaskEaQty(sumEAQty);
            qcTaskInfo.setTaskPackQty(BigDecimal.valueOf(boxNum + turnoverBoxNum));     //总箱数
            qcTaskInfo.setExt4(boxNum); //箱数
            qcTaskInfo.setExt3(turnoverBoxNum); //总周转箱数
            //设置合板的托盘
            qcTaskInfo.setMergedContainerId(qcTaskInfo.getContainerId());
            TaskEntry entry = new TaskEntry();
            entry.setTaskInfo(qcTaskInfo);
            entry.setTaskDetailList((List<Object>) (List<?>) details);
            iTaskRpcService.update(TaskConstant.TYPE_QC, entry);
            iTaskRpcService.done(qcTaskId, qcTaskInfo.getLocationId());
        }

        return JsonUtils.SUCCESS(new HashMap<String, Boolean>() {
            {
                put("response", true);
            }
        });
    }



    /**
     * 跳过异常
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("skipException")
    public String skipExceptionRf() throws BizCheckedException {
        Map<String, Object> request = RequestUtils.getRequest();
        Long containerId = Long.valueOf(request.get("containerId").toString());
        String code = request.get("code").toString();
        if (null == containerId || null == code) {
            throw new BizCheckedException("2120019");
        }
        boolean result = iqcRpcService.skipExceptionRf(request);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("result", result);
        return JsonUtils.SUCCESS(result);
    }


    /**
     * 跳过异常
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("skipException")
    public String repairExceptionRf() throws BizCheckedException {
        Map<String, Object> request = RequestUtils.getRequest();
        Long containerId = Long.valueOf(request.get("containerId").toString());
        String code = request.get("code").toString();
        if (null == containerId || null == code) {
            throw new BizCheckedException("2120019");
        }
        boolean result = iqcRpcService.repairExceptionRf(request);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("result", result);
        return JsonUtils.SUCCESS(result);
    }


    /**
     * 回滚异常
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("fallbackException")
    public String fallbackExceptionRf() throws BizCheckedException {
        Map<String, Object> request = RequestUtils.getRequest();
        Long containerId = Long.valueOf(request.get("containerId").toString());
        String code = request.get("code").toString();
        if (null == containerId || null == code) {
            throw new BizCheckedException("2120019");
        }
        boolean result = iqcRpcService.fallbackExceptionRf(request);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("result", result);
        return JsonUtils.SUCCESS(result);
    }

    /**
     * 废弃了,呵呵
     *
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("confirmAll")
    public String confirmAll() throws BizCheckedException {
        Map<String, Object> mapRequest = RequestUtils.getRequest();
        HttpSession session = RequestUtils.getSession();
        Long containerId = Long.valueOf(mapRequest.get("containerId").toString());
        //获取当前的有效待QC container 任务列表
        //get task  by containerId
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("containerId", containerId);
        //mapQuery.put("");
        List<TaskEntry> tasks = iTaskRpcService.getTaskHeadList(TaskConstant.TYPE_QC, mapQuery);
        if (tasks.size() == 0) {
            throw new BizCheckedException("2120007");
        } else if (tasks.size() > 1) {
            throw new BizCheckedException("2120006");
        }
        /*
        if(tasks.get(0).getTaskInfo().getStatus()==TaskConstant.Done
                || tasks.get(0).getTaskInfo().getStatus() == TaskConstant.Cancel){
            throw new BizCheckedException("2120011");
        }
        */
        List<Map> qcList = JSON.parseArray(mapRequest.get("qcList").toString(), Map.class);
        List<WaveDetail> details = waveService.getDetailsByContainerId(containerId);
        int addExceptionNum = 0;
        for (Map<String, Object> qcItem : qcList) {
            long exceptionType = 0;
            String code = qcItem.get("code").toString().trim();
            BigDecimal qty = new BigDecimal(qcItem.get("qty").toString());
            CsiSku skuInfo = csiRpcService.getSkuByCode(CsiConstan.CSI_CODE_TYPE_BARCODE, code);
            if (skuInfo == null) {
                throw new BizCheckedException("2120001");
            }
            long skuId = skuInfo.getSkuId();
            int seekNum = 0;
            List<WaveDetail> matchDetails = new LinkedList<WaveDetail>();
            BigDecimal pickQty = new BigDecimal("0.0000");
            for (WaveDetail d : details) {
                if (d.getSkuId() != skuId) {
                    continue;
                }
                seekNum++;
                matchDetails.add(d);
                pickQty = pickQty.add(d.getPickQty());
            }
            if (seekNum == 0) {
                exceptionType = 3;
                long tmpExceptionType = qcItem.get("exceptionType") == null ? 0L : Long.valueOf(qcItem.get("exceptionType").toString());
                if (tmpExceptionType != exceptionType) {
                    throw new BizCheckedException("2120009");
                }
                if (qcItem.get("exceptionQty") == null) {
                    throw new BizCheckedException("2120010");
                }
                WaveQcException qcException = new WaveQcException();
                qcException.setSkuId(skuInfo.getSkuId());
                BigDecimal exctpionQty = new BigDecimal(qcItem.get("exceptionQty").toString());
                qcException.setExceptionQty(exctpionQty);
                qcException.setExceptionType(exceptionType);
                qcException.setQcTaskId(0L);
                qcException.setWaveId(0L);
                waveService.insertQCException(qcException);
                addExceptionNum++;
                continue;
            }
            int cmpRet = pickQty.compareTo(qty);    //拣货数和实际的QC数的差别
            if (cmpRet > 0) exceptionType = 2; //多货
            if (cmpRet < 0) exceptionType = 1; //少货

            BigDecimal curQty = new BigDecimal("0.0000");
            for (int i = 0; i < matchDetails.size(); ++i) {
                WaveDetail detail = matchDetails.get(i);
                BigDecimal lastQty = curQty;
                curQty = curQty.add(detail.getPickQty());
                detail.setQcQty(qty);
                detail.setQcAt(DateUtils.getCurrentSeconds());
                detail.setQcUid(Long.valueOf(RequestUtils.getHeader("uid")));
                detail.setQcException(exceptionType);
                detail.setQcAt(DateUtils.getCurrentSeconds());
                detail.setQcUid(Long.valueOf(RequestUtils.getHeader("uid")));
                if (exceptionType != 0) {
                    //多货
                    if (i == matchDetails.size() - 1) {
                        detail.setQcException(exceptionType);
                        detail.setQcExceptionQty(qty.subtract(curQty));
                        detail.setQcExceptionDone(0L);
                        detail.setQcQty(qty.subtract(lastQty));

                    } else {
                        //忽略
                        detail.setQcQty(detail.getPickQty());
                        detail.setQcException(0L);
                        detail.setQcExceptionQty(BigDecimal.ZERO);
                        detail.setQcExceptionDone(1L);
                        detail.setQcQty(detail.getPickQty());
                    }
                } else {
                    detail.setQcQty(detail.getPickQty());
                    detail.setQcException(0L);
                    detail.setQcExceptionQty(BigDecimal.ZERO);
                    detail.setQcExceptionDone(1L);
                }
                waveService.updateDetail(detail);
            }
        }
        Set<Long> setItem = new HashSet<Long>();
        for (WaveDetail detail : details) {
            setItem.add(detail.getItemId());
        }
        if (qcList.size() - addExceptionNum != setItem.size()) {
            throw new BizCheckedException("2120004");
        }
        // 最后qc提交的状态是有task状态的变更的
        iTaskRpcService.done(tasks.get(0).getTaskInfo().getTaskId());
        return JsonUtils.SUCCESS(new HashMap<String, Boolean>() {
            {
                put("response", true);
            }
        });
    }
}

