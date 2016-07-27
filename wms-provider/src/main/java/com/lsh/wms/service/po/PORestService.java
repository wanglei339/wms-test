package com.lsh.wms.service.po;

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
import com.lsh.wms.api.model.po.PoItem;
import com.lsh.wms.api.model.po.PoRequest;
import com.lsh.wms.api.service.po.IPoRestService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.BusiConstant;
import com.lsh.wms.core.constant.PoConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.po.PoOrderService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.po.InbPoDetail;
import com.lsh.wms.model.po.InbPoHeader;
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
 * Date: 16/7/8
 * Time: 16/7/8.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.service.po.
 * desc:类功能描述
 */
@Service(protocol = "rest")
@Path("order/po")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class PORestService implements IPoRestService {

    @Autowired
    private PoOrderService poOrderService;

    @Autowired
    private ItemService itemService;

    @Reference
    private ITaskRpcService iTaskRpcService;

    @POST
    @Path("init")
    public String init(String poOrderInfo) { // test
        InbPoHeader inbPoHeader = JSON.parseObject(poOrderInfo,InbPoHeader.class);
        List<InbPoDetail> inbPoDetailList = JSON.parseArray((String)inbPoHeader.getOrderDetails(),InbPoDetail.class);
        poOrderService.insertOrder(inbPoHeader,inbPoDetailList);
        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("insert")
    public BaseResponse insertOrder(PoRequest request) throws BizCheckedException{
        //初始化InbPoHeader
        InbPoHeader inbPoHeader = new InbPoHeader();
        ObjUtils.bean2bean(request, inbPoHeader);

        inbPoHeader.setOrderStatus(PoConstant.ORDER_YES);
        inbPoHeader.setInserttime(new Date());

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



        //打包返回数据

        return ResUtils.getResponse(ResponseConstant.RES_CODE_0,ResponseConstant.RES_MSG_OK,null);
    }

    public void editOrder(InbPoHeader inbPoHeader){

    }

    public InbPoHeader getInbPoHeaderById(Integer id){
        return null;
    }

    public Integer countInbPoHeader(Map<String, Object> params){
        return null;
    }

    public List<InbPoHeader> getInbPoHeaderList(Map<String, Object> params){
        return null;
    }
}
