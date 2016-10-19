package com.lsh.wms.rpc.service.store;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.store.IStoreRestService;
import com.lsh.wms.model.baseinfo.BaseinfoStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;


/**
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 16/9/30 下午3:52
 */
@Service(protocol = "rest")
@Path("store")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class StoreRestService implements IStoreRestService {
    private static Logger logger = LoggerFactory.getLogger(StoreRestService.class);
    @Autowired
    private StoreRpcService storeRpcService;

    @POST
    @Path("addStore")
    public String insertStore(BaseinfoStore baseinfoStore) throws BizCheckedException {
        if (storeRpcService.getStoreByStoreNo(baseinfoStore.getStoreNo()) != null) {
            throw new BizCheckedException("2180015");
        }
        return JsonUtils.SUCCESS(storeRpcService.insertStore(baseinfoStore));
    }

    @POST
    @Path("updateStore")
    public String updateStore(BaseinfoStore baseinfoStore) throws BizCheckedException {
        return JsonUtils.SUCCESS(storeRpcService.updateStore(baseinfoStore));
    }

    @GET
    @Path("getStore")
    public String getStoreByStoreNo(@QueryParam("storeNo") String storeNo) throws BizCheckedException {
        if (null == storeNo) {
            throw new BizCheckedException("2180014");
        }

        return JsonUtils.SUCCESS(storeRpcService.getStoreByStoreNo(storeNo));
    }

    @GET
    @Path("removeStore")
    public String removeStore(@QueryParam("storeNo") String storeNo) throws BizCheckedException {
        if (null == storeNo) {
            throw new BizCheckedException("2180014");
        }
        return JsonUtils.SUCCESS(storeRpcService.removeStore(storeNo));
    }

    @POST
    @Path("getStoreList")
    public String getStoreList() throws BizCheckedException {
        Map<String, Object> params = RequestUtils.getRequest();
        List<BaseinfoStore> baseinfoStores = storeRpcService.getStoreList(params);
        if (baseinfoStores == null || baseinfoStores.size() < 1) {
            throw new BizCheckedException("2180016");
        }
        return JsonUtils.SUCCESS(baseinfoStores);
    }

    @POST
    @Path("countStores")
    public String countBaseinfoStore() throws BizCheckedException {
        Map<String, Object> params = RequestUtils.getRequest();
        return JsonUtils.SUCCESS(storeRpcService.countBaseinfoStore(params));
    }
}
