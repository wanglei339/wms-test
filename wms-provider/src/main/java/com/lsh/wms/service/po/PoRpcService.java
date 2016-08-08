package com.lsh.wms.service.po;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.api.model.po.PoItem;
import com.lsh.wms.api.model.po.PoRequest;
import com.lsh.wms.api.service.po.IPoRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.PoConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.po.PoOrderService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.po.InbPoDetail;
import com.lsh.wms.model.po.InbPoHeader;
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
 * Date: 16/7/8
 * Time: 16/7/8.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.service.po.
 * desc:类功能描述
 */
@Service(protocol = "dubbo")
public class PoRpcService implements IPoRpcService {

    private static Logger logger = LoggerFactory.getLogger(PoRpcService.class);

    @Autowired
    private PoOrderService poOrderService;

    @Autowired
    private ItemService itemService;

    @Reference
    private ITaskRpcService iTaskRpcService;

    public void insertOrder(PoRequest request) throws BizCheckedException{
        //初始化InbPoHeader
        InbPoHeader inbPoHeader = new InbPoHeader();
        ObjUtils.bean2bean(request, inbPoHeader);

        inbPoHeader.setOrderStatus(PoConstant.ORDER_YES);
        inbPoHeader.setInserttime(new Date());
        // TODO: 16/8/8 根据原SO订单号查询SO单是否存在
        Integer orderType = inbPoHeader.getOrderType();
        if(PoConstant.ORDER_TYPE_SO_BACK == orderType){
            
        }

        //设置orderId
        inbPoHeader.setOrderId(RandomUtils.genId());

        //初始化List<InbPoDetail>
        List<InbPoDetail> inbPoDetailList = new ArrayList<InbPoDetail>();

        for(PoItem poItem : request.getItems()) {
            InbPoDetail inbPoDetail = new InbPoDetail();

            ObjUtils.bean2bean(poItem, inbPoDetail);

            //设置orderId
            inbPoDetail.setOrderId(inbPoHeader.getOrderId());
            // 获取skuId
            // TODO: 16/8/8 反仓单可以根据原so单确定itemId
            List<BaseinfoItem>  baseinfoItemList= itemService.getItemsBySkuCode(inbPoHeader.getOwnerUid(),inbPoDetail.getSkuCode());
            if(null != baseinfoItemList && baseinfoItemList.size()>=1){
                BaseinfoItem baseinfoItem = baseinfoItemList.get(baseinfoItemList.size()-1);
                inbPoDetail.setSkuId(baseinfoItem.getSkuId());
            }

            inbPoDetailList.add(inbPoDetail);
        }

        //插入订单
        poOrderService.insertOrder(inbPoHeader, inbPoDetailList);

        TaskEntry taskEntry = new TaskEntry();
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setType(TaskConstant.TYPE_PO);
        taskInfo.setOrderId(inbPoHeader.getOrderId());
        taskEntry.setTaskInfo(taskInfo);
        iTaskRpcService.create(TaskConstant.TYPE_PO,taskEntry);

    }

    public Boolean updateOrderStatus(Map<String, Object> map) throws BizCheckedException {
        //OrderOtherId与OrderId都为NULL 或者 OrderStatus为NULL
        if((map.get("orderOtherId") == null && map.get("orderId") == null)
                || map.get("orderStatus") == null) {
            throw new BizCheckedException("1010001", "参数不能为空");
        }

        if(map.get("orderOtherId") == null && map.get("orderId") != null) {
            if(!StringUtils.isInteger(String.valueOf(map.get("orderId")))) {
                throw new BizCheckedException("1010002", "参数类型不正确");
            }
        }

        if(map.get("orderOtherId") != null && map.get("orderId") == null) {
            if(StringUtils.isBlank(String.valueOf(map.get("orderOtherId")))) {
                throw new BizCheckedException("1010002", "参数类型不正确");
            }
        }

        if(map.get("orderOtherId") != null && map.get("orderId") != null) {
            if(StringUtils.isBlank(String.valueOf(map.get("orderOtherId")))
                    && !StringUtils.isInteger(String.valueOf(map.get("orderId")))) {
                throw new BizCheckedException("1010002", "参数类型不正确");
            }
        }

        if(!StringUtils.isInteger(String.valueOf(map.get("orderStatus")))) {
            throw new BizCheckedException("1010002", "参数类型不正确");
        }

        InbPoHeader inbPoHeader = new InbPoHeader();
        if(map.get("orderOtherId") != null && !StringUtils.isBlank(String.valueOf(map.get("orderOtherId")))) {
            inbPoHeader.setOrderOtherId(String.valueOf(map.get("orderOtherId")));
        }
        if(map.get("orderId") != null && StringUtils.isInteger(String.valueOf(map.get("orderId")))) {
            inbPoHeader.setOrderId(Long.valueOf(String.valueOf(map.get("orderId"))));
        }
        inbPoHeader.setOrderStatus(Integer.valueOf(String.valueOf(map.get("orderStatus"))));

        poOrderService.updateInbPoHeaderByOrderOtherIdOrOrderId(inbPoHeader);

        return true;
    }

    public List<InbPoHeader> getPoHeaderList(Map<String, Object> params) {
        return poOrderService.getInbPoHeaderList(params);
    }

    public InbPoHeader getPoDetailByOrderId(Long orderId) throws BizCheckedException {
        if(orderId == null) {
            throw new BizCheckedException("1010001", "参数不能为空");
        }

        InbPoHeader inbPoHeader = poOrderService.getInbPoHeaderByOrderId(orderId);

        poOrderService.fillDetailToHeader(inbPoHeader);

        return inbPoHeader;
    }

    public Integer countInbPoHeader(Map<String, Object> params) {
        return poOrderService.countInbPoHeader(params);
    }

    public List<InbPoHeader> getPoDetailList(Map<String, Object> params) {
        List<InbPoHeader> inbPoHeaderList = poOrderService.getInbPoHeaderList(params);

        poOrderService.fillDetailToHeaderList(inbPoHeaderList);

        return inbPoHeaderList;
    }
}
