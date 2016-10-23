package com.lsh.wms.rf.service.outbound;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.model.tu.TuHeadResponse;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.store.IStoreRpcService;
import com.lsh.wms.api.service.tu.ILoadRfRestService;
import com.lsh.wms.api.service.tu.ITuRpcService;
import com.lsh.wms.core.constant.TuConstant;
import com.lsh.wms.core.service.store.StoreService;
import com.lsh.wms.model.baseinfo.BaseinfoStore;
import com.lsh.wms.model.tu.TuHead;
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
 * @Date 2016/10/20 下午8:18
 */
@Service(protocol = "rest")
@Path("outbound/load")
@Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class LoadRfRestService implements ILoadRfRestService {
    private static Logger logger = LoggerFactory.getLogger(LoadRfRestService.class);
    @Reference
    private ITuRpcService iTuRpcService;
    @Autowired
    private StoreService storeService;

    /**
     * rf获取所有待装车或者已装车的结果集
     *
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("getTuList")
    public String getTuHeadListByLoadStatus() throws BizCheckedException {
        Map<String, Object> mapRequest = RequestUtils.getRequest();
        Long status = Long.valueOf(mapRequest.get("status").toString());
        //根据传入要的tu单的状态,显示不同list
        if (null == status) {
            throw new BizCheckedException("2990028");
        }
        if (!TuConstant.UNLOAD.equals(status) && !TuConstant.LOAD_OVER.equals(status)) {
            throw new BizCheckedException("2990029");
        }
        List<TuHead> tuHeads = null;
        //结果集封装 序号,运单号,tu,装车数;//
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        //待装车
        if (TuConstant.UNLOAD.equals(status)) {
            Map<String, Object> mapQuery = new HashMap<String, Object>();
            mapQuery.put("status", TuConstant.UNLOAD);
            mapQuery.put("orderBy", "createdAt");    //按照createAt排序
            mapQuery.put("orderType", "asc");    //按照createAt排序
            tuHeads = iTuRpcService.getTuHeadList(mapQuery);   //时间的降序
            //无tu单
            if (null == tuHeads || tuHeads.size() < 1) {
                return "";
            }
            for (int i = 0; i < tuHeads.size(); i++) {
                Map<String, Object> one = new HashMap<String, Object>();
                one.put("number", i + 1);   //序号
                one.put("tu", tuHeads.get(i).getTuId());   //tu号
                one.put("preBoard", tuHeads.get(i).getPreBoard());   //预装板数
                resultList.add(one);
            }
        }
        //已装车代发货
        if (TuConstant.LOAD_OVER.equals(status)) {
            Map<String, Object> mapQuery = new HashMap<String, Object>();
            mapQuery.put("status", TuConstant.LOAD_OVER);
            mapQuery.put("orderBy", "loadedAt");    //按照createAt排序
            mapQuery.put("orderType", "asc");    //按照loadedAt排序
            tuHeads = iTuRpcService.getTuHeadList(mapQuery);
            //无tu单
            if (null == tuHeads || tuHeads.size() < 1) {
                return "";
            }
            for (int i = 0; i < tuHeads.size(); i++) {
                Map<String, Object> one = new HashMap<String, Object>();
                one.put("number", i + 1);   //序号
                one.put("tu", tuHeads.get(i).getTuId());
                one.put("realBoard", tuHeads.get(i).getRealBoard());
                resultList.add(one);
            }
        }
        return JsonUtils.SUCCESS(resultList);
    }

    /**
     * 获取tu单,查找门店信息
     *
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("getTuList")
    public String getTuHead() throws BizCheckedException {
        Map<String, Object> mapRequest = RequestUtils.getRequest();
        String tuId = mapRequest.get("tuId").toString();
        TuHead tuHead = iTuRpcService.getHeadByTuId(tuId);
        if (null == tuHead) {
            throw new BizCheckedException("2990022");
        }
        //门店
        String[] storeIdsStr = tuHead.getStoreIds().split("\\|"); //门店id以|分割
        //List<map<"code":,"name">>
        List<Map<String, Object>> storeList = new ArrayList<Map<String, Object>>();
        for (String storeIdStr : storeIdsStr) {
            Long storeId = Long.valueOf(storeIdStr);
            BaseinfoStore store = storeService.getStoreByStoreId(storeId);
            if (null == store) {
                throw new BizCheckedException("2180018");
            }
            Map<String, Object> storeMap = new HashMap<String, Object>();
            storeMap.put("storeCode", store.getStoreNo());
            storeMap.put("storeName", store.getStoreName());
            //状态修改装车任务已经被领取?

        }
        return null;
    }


    /**
     * 扫托盘码,装板子,插入tu_detail
     * 有板子写板子,没板子写container
     *
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("scan")
    public String loadBoard() throws BizCheckedException {
        //获取参数
        Map<String, Object> request = RequestUtils.getRequest();
        //合板的托盘码
        Long mergedContainerId = Long.valueOf(request.get("containerId").toString());
        //校验是不是改门店的
        //没合板的和合板完成的

        return null;
    }

    /**
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("confirm")
    public String confirmLoad() throws BizCheckedException {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        return JsonUtils.SUCCESS(iTuRpcService.getTuHeadList(mapQuery));
    }


}
