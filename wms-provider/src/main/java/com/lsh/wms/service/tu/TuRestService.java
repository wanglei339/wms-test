package com.lsh.wms.service.tu;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.tu.ITuRestService;
import com.lsh.wms.api.service.tu.ITuRpcService;
import com.lsh.wms.core.constant.TuConstant;
import com.lsh.wms.model.tu.TuHead;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 2016/10/25 下午9:25
 */
@Service(protocol = "rest")
@Path("outbound/tu")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class TuRestService implements ITuRestService {

    private static Logger logger = LoggerFactory.getLogger(TuRestService.class);
    @Reference
    private ITuRpcService iTuRpcService;

    @POST
    @Path("getTuheadList")
    public String getTuheadList() throws BizCheckedException {
        Map<String, Object> mapRequest = RequestUtils.getRequest();
        //可传入起始时间和结束时间
        return JsonUtils.SUCCESS(iTuRpcService.getTuHeadListOnPc(mapRequest));
    }

    @GET
    @Path("getTuDeailListByTuId")
    public String getTuDeailListByTuId(@QueryParam("tuId") String tuId) throws BizCheckedException {
        return JsonUtils.SUCCESS(iTuRpcService.getTuDeailListByTuId(tuId));
    }

    @GET
    @Path("getDetailById")
    public String getDetailById(@QueryParam("id") Long id) throws BizCheckedException {
        return JsonUtils.SUCCESS(iTuRpcService.getDetailById(id));
    }

    @POST
    @Path("getTuDetailList")
    public String getTuDetailList(Map<String, Object> mapQuery) throws BizCheckedException {
        return JsonUtils.SUCCESS(iTuRpcService.getTuHeadList(mapQuery));
    }

    @GET
    @Path("getTuheadByTuId")
    public String getTuheadByTuId(@QueryParam("tuId") String tuId) throws BizCheckedException {
        return JsonUtils.SUCCESS(iTuRpcService.getHeadByTuId(tuId));
    }

    @POST
    @Path("countTuheadList")
    public String countTuHeadOnPc(Map<String, Object> mapQuery) throws BizCheckedException {
        return JsonUtils.SUCCESS(iTuRpcService.countTuHeadOnPc(mapQuery));
    }


    /**
     * todo 发货
     * 减库存|写入task的绩效
     *
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("shipTu")
    public String shipTu() throws BizCheckedException {
        //进来先校验是否发车,已发车,不需要继续做java层事务,直接跳过传数据,  未发车全部进行
        Map<String, Object> mapRequest = RequestUtils.getRequest();
        String tuId = mapRequest.get("tuId").toString();
        TuHead tuHead = iTuRpcService.getHeadByTuId(tuId);
        if (null == tuHead) {
            throw new BizCheckedException("2990022");
        }
        //事务成功 tms失败
        if (TuConstant.SHIP_OVER.equals(tuHead.getStatus())) {
            Boolean postResult = iTuRpcService.postTuDetails(tuId);
            Map<String, Boolean> resultMap = new HashMap<String, Boolean>();
            resultMap.put("response", postResult);
            return JsonUtils.SUCCESS(resultMap);
        }
        //事务操作,创建任务,发车状态改变



        // 传给TMS运单发车信息,此过程可以重复调用
        Boolean postResult = iTuRpcService.postTuDetails(tuId);

        return JsonUtils.SUCCESS("需要写发货的逻辑");
    }

    /**
     * 接收TU头信息
     *
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("receiveTuHead")
    public String receiveTuHead() throws BizCheckedException {
        Map<String, Object> mapRequest = RequestUtils.getRequest();
        TuHead tuHead = iTuRpcService.receiveTuHead(mapRequest);
        return JsonUtils.SUCCESS("");
    }

    /**
     * 关闭尾货开关
     *
     * @param tuId
     * @return
     * @throws BizCheckedException
     */
    @GET
    @Path("closeRfRestSwitch")
    public String closeRfRestSwitch(@QueryParam("tuId") String tuId) throws BizCheckedException {
        iTuRpcService.closeRfRestSwitch(tuId);
        return JsonUtils.SUCCESS("success");
    }

    /**
     * 开启rf尾货开关
     *
     * @param tuId
     * @return
     * @throws BizCheckedException
     */
    @GET
    @Path("openRfRestSwitch")
    public String openRfRestSwitch(@QueryParam("tuId") String tuId) throws BizCheckedException {
        iTuRpcService.openRfRestSwitch(tuId);
        return JsonUtils.SUCCESS("success");
    }

    /**
     * 移除板子
     *
     * @return
     * @throws BizCheckedException
     */
    @GET
    @Path("removeTuDetail")
    public String removeTuDetail(@QueryParam("mergedContainerId") Long mergedContainerId) throws BizCheckedException {
        return JsonUtils.SUCCESS(iTuRpcService.removeTuDetail(mergedContainerId));
    }

}
