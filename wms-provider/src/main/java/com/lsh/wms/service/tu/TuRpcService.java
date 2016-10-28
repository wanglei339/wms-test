package com.lsh.wms.service.tu;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.config.PropertyUtils;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.net.HttpClientUtils;
import com.lsh.base.common.utils.BeanMapTransUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.api.service.tu.ITuRpcService;
import com.lsh.wms.core.constant.TuConstant;
import com.lsh.wms.core.service.store.StoreService;
import com.lsh.wms.core.service.tu.TuService;
import com.lsh.wms.model.baseinfo.BaseinfoStore;
import com.lsh.wms.model.tu.TuDetail;
import com.lsh.wms.model.tu.TuHead;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 2016/10/20 下午2:12
 */
@Service(protocol = "dubbo")
public class TuRpcService implements ITuRpcService {

    private static Logger logger = LoggerFactory.getLogger(TuRpcService.class);

    @Autowired
    private TuService tuService;
    @Autowired
    private StoreService storeService;

    public TuHead create(TuHead tuHead) throws BizCheckedException {
        //先查有无,有的话,不能创建
        TuHead preHead = this.getHeadByTuId(tuHead.getTuId());
        if (preHead != null) {
            throw new BizCheckedException("2990020");
        }
        tuService.create(tuHead);
        return tuHead;
    }

    public TuHead update(TuHead tuHead) throws BizCheckedException {
        tuService.update(tuHead);
        return tuHead;
    }

    public TuHead getHeadByTuId(String tuId) throws BizCheckedException {
        if (null == tuId || tuId.equals("")) {
            throw new BizCheckedException("2990021");
        }
        TuHead tuHead = tuService.getHeadByTuId(tuId);
        return tuHead;
    }

    public List<TuHead> getTuHeadList(Map<String, Object> mapQuery) throws BizCheckedException {
        return tuService.getTuHeadList(mapQuery);
    }

    /**
     * PC上筛选tuList的方法,涉及时间区间的传入
     *
     * @param params
     * @return
     * @throws BizCheckedException
     */
    public List<TuHead> getTuHeadListOnPc(Map<String, Object> params) throws BizCheckedException {
        return tuService.getTuHeadListOnPc(params);
    }

    /**
     * PC上筛选tuList的方法,涉及时间区间的传入
     *
     * @param mapQuery
     * @return
     * @throws BizCheckedException
     */
    public Integer countTuHeadOnPc(Map<String, Object> mapQuery) throws BizCheckedException {
        return tuService.countTuHeadOnPc(mapQuery);
    }

    public Integer countTuHead(Map<String, Object> mapQuery) throws BizCheckedException {
        return tuService.countTuHead(mapQuery);
    }

    public TuHead removeTuHead(String tuId) throws BizCheckedException {
        if (null == tuId || tuId.equals("")) {
            throw new BizCheckedException("2990021");
        }
        TuHead tuHead = tuService.getHeadByTuId(tuId);
        if (null == tuHead) {
            throw new BizCheckedException("2990022");
        }
        return tuService.removeTuHead(tuHead);
    }

    public TuDetail create(TuDetail tuDetail) throws BizCheckedException {
        //先查有无,boardId是唯一的key
        TuDetail preDetail = this.getDetailByBoardId(tuDetail.getMergedContainerId());
        if (preDetail != null) {
            throw new BizCheckedException("2990023");
        }
        tuService.create(tuDetail);
        return tuDetail;
    }

    public TuDetail update(TuDetail tuDetail) throws BizCheckedException {
        tuService.update(tuDetail);
        return tuDetail;
    }

    public TuDetail getDetailByBoardId(Long boardId) throws BizCheckedException {
        if (null == boardId) {
            throw new BizCheckedException("2990024");
        }
        TuDetail tuDetail = tuService.getDetailByBoardId(boardId);
        return tuDetail;
    }

    public List<TuDetail> getTuDeailListByTuId(String tuId) throws BizCheckedException {
        if (null == tuId || tuId.equals("")) {
            throw new BizCheckedException("2990021");
        }
        List<TuDetail> tuDetails = tuService.getTuDeailListByTuId(tuId);
        return tuDetails;
    }

    public TuDetail getDetailById(Long id) throws BizCheckedException {
        if (null == id) {
            throw new BizCheckedException("2990025");
        }
        return tuService.getDetailById(id);
    }

    public List<TuDetail> getTuDeailList(Map<String, Object> mapQuery) throws BizCheckedException {
        return tuService.getTuDeailList(mapQuery);
    }

    public TuDetail removeTuDetail(Long boardId) throws BizCheckedException {
        if (null == boardId) {
            throw new BizCheckedException("2990024");
        }
        TuDetail tuDetail = tuService.getDetailByBoardId(boardId);
        if (null == tuDetail) {
            throw new BizCheckedException("2990026");
        }
        return tuService.removeTuDetail(tuDetail);
    }

    public Integer countTuDetail(Map<String, Object> mapQuery) throws BizCheckedException {
        return tuService.countTuDetail(mapQuery);
    }

    public List<TuDetail> getTuDetailByStoreCode(String tuId, String deliveryCode) throws BizCheckedException {
        if (null == tuId || null == deliveryCode) {
            throw new BizCheckedException("2990027");
        }
        return tuService.getTuDetailByStoreCode(tuId, deliveryCode);
    }

    public TuHead changeTuHeadStatus(String tuId, Integer status) throws BizCheckedException {
        if (null == tuId || tuId.equals("")) {
            throw new BizCheckedException("2990021");
        }
        TuHead tuHead = this.getHeadByTuId(tuId);
        if (null == tuHead) {
            throw new BizCheckedException("2990022");
        }
        tuHead.setStatus(status);
        this.update(tuHead);
        return tuHead;
    }

    public TuHead changeTuHeadStatus(TuHead tuHead, Integer status) throws BizCheckedException {
        tuHead.setStatus(status);
        this.update(tuHead);
        return tuHead;
    }

    /**
     * 使用POST方式将TU发车
     * @param tuId
     * @throws BizCheckedException
     */
    public Boolean postTuDetails(String tuId) throws BizCheckedException {
        TuHead tuHead = tuService.getHeadByTuId(tuId);
        Map<String, String> result = new HashMap<String, String>();
        String responseBody = "";
        if (tuHead == null) {
            throw new BizCheckedException("2990022");
        }
        if (!tuHead.getStatus().equals(TuConstant.SHIP_OVER)) {
            throw new BizCheckedException("2990037");
        }
        List<TuDetail> tuDetails = tuService.getTuDeailListByTuId(tuId);
        List<Map<String, Object>> details = new ArrayList<Map<String, Object>>();
        for (TuDetail tuDetail: tuDetails) {
            Map<String, Object> detail = BeanMapTransUtils.Bean2map(tuDetail);
            BaseinfoStore store = storeService.getBaseinfoStore(tuDetail.getStoreId());
            detail.put("storeNo", store.getStoreNo());
            detail.put("storeName", store.getStoreName());
            details.add(detail);
        }
        result.put("tuId", tuId);
        result.put("scale", tuHead.getScale().toString());
        result.put("tuDetails", JSON.toJSONString(details));
        String url = PropertyUtils.getString("tms_ship_over_url");
        int timeout = PropertyUtils.getInt("tms_timeout");
        String charset = PropertyUtils.getString("tms_charset");
        Map<String, String> headMap = new HashMap<String, String>();
        headMap.put("Content-type", "application/x-www-form-urlencoded; charset=utf-8");
        headMap.put("Accept", "*/*");
        logger.info("[SHIP OVER]Begin to transfer to TMS, " + "URL: " + url + ", Request body: " + JSON.toJSONString(result));
        try {
            responseBody = HttpClientUtils.post(url, result, timeout, charset, headMap);
        } catch (Exception e) {
            logger.info("[SHIP OVER]Transfer to TMS failed: " + responseBody);
            return false;
        }
        logger.info("[SHIP OVER]Transfer to TMS success: " + responseBody);
        return true;
    }

    /**
     * 接收TU头信息
     * @param mapRequest
     * @return
     * @throws BizCheckedException
     */
    public TuHead receiveTuHead(Map<String, Object> mapRequest) throws BizCheckedException {
        logger.info("[RECEIVE TU]Receive TU: " + JSON.toJSONString(mapRequest));
        String tuId = mapRequest.get("tu_id").toString();
        TuHead tuHead = tuService.getHeadByTuId(tuId);
        Boolean newTu = true;
        if (tuHead != null) {
            logger.info("[RECEIVE TU]Receive TU: " + tuId + " is duplicated");
            newTu = false;
        } else {
            tuHead = new TuHead();
        }
        tuHead.setTuId(tuId);
        tuHead.setTransUid(Long.valueOf(mapRequest.get("trans_uid").toString()));
        tuHead.setCellphone(mapRequest.get("cellphone").toString());
        tuHead.setName(mapRequest.get("name").toString());
        tuHead.setCarNumber(mapRequest.get("car_number").toString());
        tuHead.setStoreIds(mapRequest.get("store_ids").toString());
        tuHead.setPreBoard(Long.valueOf(mapRequest.get("pre_board").toString()));
        tuHead.setCommitedAt(Long.valueOf(mapRequest.get("commited_at").toString()));
        tuHead.setScale(Integer.valueOf(mapRequest.get("scale").toString()));
        tuHead.setStatus(TuConstant.UNLOAD);
        if (newTu) {
            tuService.create(tuHead);
        } else {
            tuService.update(tuHead);
        }
        logger.info("[RECEIVE TU]Receive TU success: " + JSON.toJSONString(tuHead));
        return tuHead;
    }
}
