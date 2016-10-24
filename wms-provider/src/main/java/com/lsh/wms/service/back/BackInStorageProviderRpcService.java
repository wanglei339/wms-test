package com.lsh.wms.service.back;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.service.back.IBackInStorageProviderRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.csi.CsiSkuService;
import com.lsh.wms.core.service.po.PoOrderService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.model.back.BackTaskDetail;
import com.lsh.wms.model.po.IbdObdRelation;
import com.lsh.wms.model.so.ObdDetail;
import com.lsh.wms.model.so.ObdHeader;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project Name: lsh-wms
 * Created by wuhao
 * Date: 16/10/22
 * Time: 16/10/22.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.service.back
 * desc:类功能描述
 */
@Service(protocol = "dubbo")
public class BackInStorageProviderRpcService implements IBackInStorageProviderRpcService {

    private static Logger logger = LoggerFactory.getLogger(BackInStorageProviderRpcService.class);
    @Autowired
    PoOrderService poOrderService;
    @Autowired
    SoOrderService soOrderService;
    @Autowired
    private CsiSkuService skuService;
    @Reference
    ITaskRpcService taskRpcService;


    public void createTask(Long poId) throws BizCheckedException {
        Map<String,Object> queryMap = new HashMap<String, Object>();
        List<TaskEntry> entries = new ArrayList<TaskEntry>();
        //清除已经生成任务的so
        Map<Long,Integer> chargeMap =  new HashMap<Long, Integer>();

        queryMap.put("ibdOtherId",poId);
        List<IbdObdRelation> ibdObdRelations = poOrderService.getIbdObdRelationList(queryMap);
        for(IbdObdRelation ibdObdRelation:ibdObdRelations){

            if(chargeMap.get(ibdObdRelation.getObdOtherId())==null){
                TaskEntry entry = new TaskEntry();
                TaskInfo info = new TaskInfo();
                ObdHeader header = soOrderService.getOutbSoHeaderByOrderOtherId(ibdObdRelation.getObdOtherId());
                List<ObdDetail> details = soOrderService.getOutbSoDetailListByOrderId(header.getOrderId());
                info.setOrderId(header.getOrderId());
                info.setStatus(TaskConstant.Draft);
                info.setTaskName("退货入库任务[ " + header.getOrderId() + "]");
                info.setType(TaskConstant.TYPE_BACK_IN_STORAGE);
                List<BackTaskDetail> backTaskDetails = new ArrayList<BackTaskDetail>();
                for(ObdDetail obdDetail:details){
                    BackTaskDetail detail = new BackTaskDetail();
                    detail.setSkuName(obdDetail.getSkuName());
                    detail.setPackUnit(obdDetail.getPackUnit());
                    detail.setBarcode(skuService.getSku(obdDetail.getSkuId()).getCode());
                    detail.setSkuId(obdDetail.getSkuId());
                    backTaskDetails.add(detail);
                }
                entry.setTaskInfo(info);
                entry.setTaskDetailList((List<Object>) (List<?>) backTaskDetails);
                entries.add(entry);
                chargeMap.put(header.getOrderId(),1);
            }
        }

        taskRpcService.batchCreate(TaskConstant.TYPE_BACK_IN_STORAGE,entries);

    }

}
