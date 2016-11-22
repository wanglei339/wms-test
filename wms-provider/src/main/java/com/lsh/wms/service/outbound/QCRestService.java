package com.lsh.wms.service.outbound;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.api.model.tu.GroupRestResponse;
import com.lsh.wms.api.service.pick.IQCRestService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.wave.WaveDetail;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zengwenjun on 16/8/20.
 */
@Service(protocol = "rest")
@Path("outbound/qc")
public class QCRestService implements IQCRestService {
    @Autowired
    QCRpcService qcRpcService;

    @POST
    @Path("skipException")
    public String skipException() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        long id = mapQuery.get("id") == null ? 0 : Long.valueOf(mapQuery.get("id").toString());
        qcRpcService.skipException(id);
        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("repairException")
    public String repairException() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        long id = mapQuery.get("id") == null ? 0 : Long.valueOf(mapQuery.get("id").toString());
        qcRpcService.repairException(id);
        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("fallbackException")
    public String fallbackException() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        long id = mapQuery.get("id") == null ? 0 : Long.valueOf(mapQuery.get("id").toString());
        qcRpcService.fallbackException(id);
        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("getGroupList")
    public String getGroupList() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        return JsonUtils.SUCCESS(qcRpcService.getGroupList(mapQuery));
    }

    @POST
    @Path("countGroupList")
    public String countGroupList() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        return JsonUtils.SUCCESS(qcRpcService.countGroupList(mapQuery));
    }

    @POST
    @Path("getGroupDetailByStoreNo")
    public String getGroupDetailByStoreNo() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        String customerCode = mapQuery.get("customerCode").toString();
        List<WaveDetail> waveDetails = qcRpcService.getQcWaveDetailsByStoreNo(customerCode);
        List<TaskInfo> qcDoneTaskInfos = qcRpcService.getQcDoneTaskInfoByWaveDetails(waveDetails);
        List<GroupRestResponse> groupRestResponses = new ArrayList<GroupRestResponse>();
        if (qcDoneTaskInfos.size() > 0) {
            for (TaskInfo info : qcDoneTaskInfos) {
                GroupRestResponse response = new GroupRestResponse();
                ObjUtils.bean2bean(info, response);
                //余货
                if (info.getFinishTime() < DateUtils.getTodayBeginSeconds()) {
                    response.setIsRest(true);
                }else {
                    response.setIsRest(false);
                }
                //todo 贵品
                response.setIsExpensive(false);
                groupRestResponses.add(response);
            }
        }
        return JsonUtils.SUCCESS(groupRestResponses);
    }


}
