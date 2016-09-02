package com.lsh.wms.service.so;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.api.model.so.SoItem;
import com.lsh.wms.api.model.so.SoRequest;
import com.lsh.wms.api.service.so.ISoRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.BusiConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.so.OutbSoDetail;
import com.lsh.wms.model.so.OutbSoHeader;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Project Name: lsh-wms
 * Created by fuhao
 * Date: 16/7/11
 * Time: 16/7/11.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.service.so.
 * desc:类功能描述
 */
@Service(protocol = "dubbo")
public class SoRpcService implements ISoRpcService {

    private static Logger logger = LoggerFactory.getLogger(SoRpcService.class);

    @Autowired
    private SoOrderService soOrderService;

    @Autowired
    private ItemService itemService;

    @Reference
    private ITaskRpcService iTaskRpcService;

    public void insertOrder(SoRequest request) throws BizCheckedException {
        //OutbSoHeader
        OutbSoHeader outbSoHeader = new OutbSoHeader();
        ObjUtils.bean2bean(request, outbSoHeader);

        //设置订单状态
        outbSoHeader.setOrderStatus(BusiConstant.EFFECTIVE_YES);

        //设置订单插入时间
        outbSoHeader.setInserttime(new Date());

        //设置orderId
        outbSoHeader.setOrderId(RandomUtils.genId());

        //初始化List<OutbSoDetail>
        List<OutbSoDetail> outbSoDetailList = new ArrayList<OutbSoDetail>();

        for(SoItem soItem : request.getItems()) {
            OutbSoDetail outbSoDetail = new OutbSoDetail();

            ObjUtils.bean2bean(soItem, outbSoDetail);

            //设置orderId
            outbSoDetail.setOrderId(outbSoHeader.getOrderId());

            //根据ItemId及OwnerUid获取List<BaseinfoItem>
            // TODO: 根据ItemId,OwnerUid获取BaseinfoItem,现在是取List第一个元素,待改进
            List<BaseinfoItem> baseinfoItemList = itemService.getItemsBySkuCode(outbSoHeader.getOwnerUid(),
                    outbSoDetail.getSkuCode());

            if(baseinfoItemList.size() <=0) {
                throw new BizCheckedException("2900001");
            }

            //设置skuId
            outbSoDetail.setSkuId(baseinfoItemList.get(0).getSkuId());
            //设置itemId
            outbSoDetail.setItemId(baseinfoItemList.get(0).getItemId());

            outbSoDetailList.add(outbSoDetail);
        }

        //插入订单
        soOrderService.insertOrder(outbSoHeader, outbSoDetailList);

//        TaskEntry taskEntry = new TaskEntry();
//        TaskInfo taskInfo = new TaskInfo();
//        taskInfo.setType(TaskConstant.TYPE_PICK);
//        taskInfo.setOrderId(outbSoHeader.getOrderId());
//        taskEntry.setTaskInfo(taskInfo);
//        iTaskRpcService.create(TaskConstant.TYPE_PICK,taskEntry);

    }

    public Boolean updateOrderStatus(Map<String, Object> map) throws BizCheckedException {
        if((map.get("orderOtherId") == null && map.get("orderId") == null)
                || map.get("orderStatus") == null) {
            throw new BizCheckedException("1030001", "参数不能为空");
        }

        if(map.get("orderOtherId") == null && map.get("orderId") != null) {
            if(!StringUtils.isInteger(String.valueOf(map.get("orderId")))) {
                throw new BizCheckedException("1030002", "参数类型不正确");
            }
        }

        if(map.get("orderOtherId") != null && map.get("orderId") == null) {
            if(StringUtils.isBlank(String.valueOf(map.get("orderOtherId")))) {
                throw new BizCheckedException("1030002", "参数类型不正确");
            }
        }

        if(map.get("orderOtherId") != null && map.get("orderId") != null) {
            if(StringUtils.isBlank(String.valueOf(map.get("orderOtherId")))
                    && !StringUtils.isInteger(String.valueOf(map.get("orderId")))) {
                throw new BizCheckedException("1030002", "参数类型不正确");
            }
        }

        if(!StringUtils.isInteger(String.valueOf(map.get("orderStatus")))) {
            throw new BizCheckedException("1030002", "参数类型不正确");
        }

        OutbSoHeader outbSoHeader = new OutbSoHeader();
        if(map.get("orderOtherId") != null && !StringUtils.isBlank(String.valueOf(map.get("orderOtherId")))) {
            outbSoHeader.setOrderOtherId(String.valueOf(map.get("orderOtherId")));
        }
        if(map.get("orderId") != null && StringUtils.isInteger(String.valueOf(map.get("orderId")))) {
            outbSoHeader.setOrderId(Long.valueOf(String.valueOf(map.get("orderId"))));
        }
        outbSoHeader.setOrderStatus(Integer.valueOf(String.valueOf(map.get("orderStatus"))));

        soOrderService.updateOutbSoHeaderByOrderOtherIdOrOrderId(outbSoHeader);

        return true;
    }

    public OutbSoHeader getOutbSoHeaderDetailByOrderId(Long orderId) throws BizCheckedException {
        if(orderId == null) {
            throw new BizCheckedException("1030001", "参数不能为空");
        }

        return soOrderService.getOutbSoHeaderByOrderId(orderId);
    }

    public Integer countOutbSoHeader(Map<String, Object> params) {
        return soOrderService.countOutbSoHeader(params);
    }

    public List<OutbSoHeader> getOutbSoHeaderList(Map<String, Object> params) {
        return soOrderService.getOutbSoHeaderList(params);
    }
}
