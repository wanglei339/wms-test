package com.lsh.wms.service.so;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.api.model.base.BaseResponse;
import com.lsh.wms.api.model.base.ResUtils;
import com.lsh.wms.api.model.base.ResponseConstant;
import com.lsh.wms.api.model.so.SoItem;
import com.lsh.wms.api.model.so.SoRequest;
import com.lsh.wms.api.service.so.ISoRestService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.BusiConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.po.InbPoHeader;
import com.lsh.wms.model.so.OutbSoDetail;
import com.lsh.wms.model.so.OutbSoHeader;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
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
@Service(protocol = "rest")
@Path("order/so")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class SORestService  implements ISoRestService {

    @Autowired
    private SoOrderService soOrderService;

    @Autowired
    private ItemService itemService;

    @Reference
    private ITaskRpcService iTaskRpcService;

    @POST
    @Path("init")
    public String init(String soOrderInfo) {
        OutbSoHeader outbSoHeader = JSON.parseObject(soOrderInfo,OutbSoHeader.class);
        List<OutbSoDetail> outbSoDetailList = JSON.parseArray((String)outbSoHeader.getOrderDetails(),OutbSoDetail.class);
        soOrderService.insert(outbSoHeader,outbSoDetailList);
        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("insert")
    public BaseResponse insertOrder(SoRequest request) throws BizCheckedException {
        BaseResponse response = new BaseResponse();

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
                    String.valueOf(outbSoDetail.getItemId()));

            if(baseinfoItemList.size() <=0) {
                throw new BizCheckedException("2900001");
            }

            //设置skuId
            outbSoDetail.setSkuId(baseinfoItemList.get(0).getSkuId());

            outbSoDetailList.add(outbSoDetail);
        }

        //插入订单
        soOrderService.insertOrder(outbSoHeader, outbSoDetailList);

        TaskEntry taskEntry = new TaskEntry();
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setType(TaskConstant.TYPE_PICK);
        taskInfo.setOrderId(outbSoHeader.getOrderId());
        taskEntry.setTaskInfo(taskInfo);
        iTaskRpcService.create(TaskConstant.TYPE_PICK,taskEntry);

        return ResUtils.getResponse(ResponseConstant.RES_CODE_0,ResponseConstant.RES_MSG_OK,null);
    }

}
