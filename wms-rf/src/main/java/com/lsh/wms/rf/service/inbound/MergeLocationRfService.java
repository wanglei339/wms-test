package com.lsh.wms.rf.service.inbound;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.location.ILocationDetailRpc;
import com.lsh.wms.api.service.location.IMergeLocationRfService;
import com.lsh.wms.api.service.request.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 2017/2/16 下午7:36
 */
@Service(protocol = "rest")
@Path("inbound/location")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class MergeLocationRfService implements IMergeLocationRfService {
    private static Logger logger = LoggerFactory.getLogger(MergeLocationRfService.class);
    @Reference
    private ILocationDetailRpc iLocationDetailRpc;

    /**
     * 合并货位
     *
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("mergeBins")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String mergeBins() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        List<String> binCodes = null;
        try {
            binCodes = JSON.parseArray(mapQuery.get("binCodes").toString(), String.class);
            logger.info(binCodes.toString());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JsonUtils.TOKEN_ERROR("传递格式参数有误");
        }
        //做target的校验
        Map<String, Object> msg2ArrMap = iLocationDetailRpc.mergeBinsByLocationIds(binCodes);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (msg2ArrMap.isEmpty()) {
            resultMap.put("response", true);
        } else {
            resultMap.put("response", false);
            resultMap.put("msg",msg2ArrMap.get("msg"));
            resultMap.put("arr",msg2ArrMap.get("arr"));
        }
        return JsonUtils.SUCCESS(resultMap);
    }

    /**
     * 拆分库位
     *
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("splitBins")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String splitBins() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        String locationCode = null;
        try {
            locationCode = mapQuery.get("locationCode").toString();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JsonUtils.TOKEN_ERROR("传递格式参数有误");
        }
        iLocationDetailRpc.splitBins(locationCode);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("response", true);
        return JsonUtils.SUCCESS(resultMap);
    }
    /**
     * 查询库位的合并状态
     *
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("checkBin")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String checkBin() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        String locationCode = null;
        try {
            locationCode = mapQuery.get("locationCode").toString();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JsonUtils.TOKEN_ERROR("传递格式参数有误");
        }
        Map<String,Object> result = iLocationDetailRpc.checkBin(locationCode);
        return JsonUtils.SUCCESS(result);
    }
}
