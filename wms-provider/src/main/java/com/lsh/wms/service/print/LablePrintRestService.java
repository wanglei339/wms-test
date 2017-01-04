package com.lsh.wms.service.print;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.api.service.print.ILablePrintRestService;
import com.lsh.wms.core.dao.redis.RedisStringDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhanghongling on 16/11/8.
 * 标签打印
 */
@Service(protocol = "rest")
@Path("print/lable")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class LablePrintRestService implements ILablePrintRestService {
    private static Logger logger = LoggerFactory.getLogger(LablePrintRestService.class);

    @Autowired
    private LablePrintRpcService lablePrintRpcService;
    @Autowired
    private RedisStringDao redisStringDao;

    //生成托盘码
    @GET
    @Path("getContainerCode")
    public String getContainerCode(@QueryParam("number") Integer number,
                                   @QueryParam("containerCode") String containerCode) throws BizCheckedException{

            if (number != null && number > 0) {
                //批量生成
                return JsonUtils.SUCCESS(lablePrintRpcService.getContainerCode(number));
            } else if (StringUtils.isNotEmpty(containerCode)) {
                if(lablePrintRpcService.checkContainerCode(containerCode)) {
                    //补残验证
                    List<String> list = new ArrayList<String>();
                    list.add(containerCode);
                    return JsonUtils.SUCCESS(list);
                }

            }
                return JsonUtils.TOKEN_ERROR("参数错误");


    }

    //批量生成合板标识码
    @GET
    @Path("getMergeLable")
    public String getMergeLable(@QueryParam("number") Integer number){
        if(number == null ){
            return JsonUtils.TOKEN_ERROR("参数错误");
        }
        Set<String> lableSet = new HashSet<String>();

        while(lableSet.size() < number){
            int r = RandomUtils.randomInt(4) ;
            String lable = "H-"+DateUtils.FORMAT_MILLISECOND_NO_BAR.format(new Date()) + r;
            lableSet.add(lable);
        }

        return JsonUtils.SUCCESS(lableSet);
    }
}
