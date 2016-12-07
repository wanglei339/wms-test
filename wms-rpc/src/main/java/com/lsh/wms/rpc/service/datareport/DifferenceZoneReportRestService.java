package com.lsh.wms.rpc.service.datareport;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.datareport.IDifferenceZoneReportRestService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Map;

/**
 * Created by lixin-mac on 2016/12/7.
 */
@Service(protocol = "rest")
@Path("differenceReport")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class DifferenceZoneReportRestService implements IDifferenceZoneReportRestService{

    @Autowired
    private DifferenceZoneReportRpcService differenceZoneReportRpcService;


    @Path("getDifferenceReportList")
    @POST
    public String getDifferenceReportList(Map<String, Object> mapQuery) {
        return JsonUtils.SUCCESS(differenceZoneReportRpcService.getDifferenceReportList(mapQuery));
    }

    @Path("countDifferenceReport")
    @POST
    public String countDifferenceReport(Map<String, Object> mapQuery) {
        return JsonUtils.SUCCESS(differenceZoneReportRpcService.countDifferenceReport(mapQuery));
    }
}
