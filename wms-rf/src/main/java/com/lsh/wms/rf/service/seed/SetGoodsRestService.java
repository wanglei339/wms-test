package com.lsh.wms.rf.service.seed;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.location.ILocationRpcService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.seed.ISetGoodsRestService;
import com.lsh.wms.api.service.store.IStoreRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.so.ObdHeader;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.wave.WaveDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wuhao on 16/10/8.
 */

@Service(protocol = "rest")
@Path("setGoods")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class SetGoodsRestService implements ISetGoodsRestService {

    private static Logger logger = LoggerFactory.getLogger(SetGoodsRestService.class);

    @Autowired
    BaseTaskService baseTaskService;
    @Reference
    ITaskRpcService taskRpcService;
    @Autowired
    SoOrderService soOrderService;
    @Autowired
    WaveService waveService;
    @Reference
    private IStoreRpcService storeRpcService;
    @Reference
    private ILocationRpcService locationRpcService;
    /**
     * 集货
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("doSet")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String doSet() throws BizCheckedException {
        Long containerId = 0L;
        Long uId = 0L;
        Map<String, Object> request = RequestUtils.getRequest();
        try {
            uId =  Long.valueOf(RequestUtils.getHeader("uid"));
            containerId = Long.valueOf(request.get("containerId").toString());
        }catch (Exception e) {
            logger.error(e.getMessage());
            return JsonUtils.TOKEN_ERROR("参数传递格式有误");
        }
        Map<String,Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("containerId",containerId);
        mapQuery.put("type",TaskConstant.TYPE_SET_GOODS);
        List<TaskInfo> infos = baseTaskService.getTaskInfoList(mapQuery);
        if(infos==null || infos.size()==0){
            throw new BizCheckedException("2880008");
        }
        TaskInfo info = infos.get(0);
        if(info.getStep()==2){
            throw new BizCheckedException("2880009");
        }
        info.setStep(2);
        info.setOperator(uId);
        TaskEntry entry = new TaskEntry();
        entry.setTaskInfo(info);

        taskRpcService.done(entry);

        return JsonUtils.SUCCESS(new HashMap<String, Boolean>() {
            {
                put("response", true);
            }
        });

    }
    /**
     * 集货详情
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("view")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String view() throws BizCheckedException {
        Long uId=0L;
        Long containerId = 0L;
        Map<String, Object> request = RequestUtils.getRequest();
        try {
            uId =  Long.valueOf(RequestUtils.getHeader("uid"));
            containerId = Long.valueOf(request.get("containerId").toString());
        }catch (Exception e) {
            logger.error(e.getMessage());
            return JsonUtils.TOKEN_ERROR("参数传递格式有误");
        }
        Map<String,Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("containerId",containerId);
        mapQuery.put("type",TaskConstant.TYPE_SET_GOODS);
        //TODO 怎么判断该托盘是在可集货区域
        List<TaskInfo> infos = baseTaskService.getTaskInfoList(mapQuery);
        TaskInfo info =null;
        if(infos==null || infos.size()==0){
            TaskEntry entry = new TaskEntry();
            info = new TaskInfo();
            info.setType(TaskConstant.TYPE_SET_GOODS);
            info.setContainerId(containerId);
            info.setStep(1);
            info.setTaskName("集货任务[ " + containerId + "]");
            info.setStatus(TaskConstant.Draft);
            info.setPlanner(uId);
            entry.setTaskInfo(info);
            taskRpcService.create(TaskConstant.TYPE_SET_GOODS,entry);
        }else {
            info = infos.get(0);
        }
        List<WaveDetail> details = waveService.getAliveDetailsByContainerId(containerId);
        if(details==null || details.size()==0){
            throw new BizCheckedException("2880003");
        }
        ObdHeader header = soOrderService.getOutbSoHeaderByOrderId(details.get(0).getOrderId());
        if(header ==null){
            return JsonUtils.TOKEN_ERROR("so订单不存在");
        }
        Long storeNo = Long.valueOf(header.getDeliveryCode());

        //获得集货区信息
        List<BaseinfoLocation> locations = locationRpcService.getCollectionByStoreNo(storeNo);
        if(locations == null || locations.size()==0 ){
            return JsonUtils.TOKEN_ERROR("该托盘对应门店无集货信息");
        }

        Map<String,Object> result = new HashMap<String, Object>();
        result.put("storeName",storeRpcService.getStoreByStoreNo(storeNo).getStoreName());
        result.put("locationCode",locations.get(0).getLocationCode());
        result.put("containerId",containerId);
        result.put("status",info.getStep());
        return JsonUtils.SUCCESS(result);
    }
}
