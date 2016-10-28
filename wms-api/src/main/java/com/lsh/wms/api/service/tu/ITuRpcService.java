package com.lsh.wms.api.service.tu;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.tu.TuDetail;
import com.lsh.wms.model.tu.TuHead;

import java.util.List;
import java.util.Map;

/**
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 2016/10/20 上午11:03
 */
public interface ITuRpcService {
    public TuHead create(TuHead tuHead) throws BizCheckedException;

    public TuHead update(TuHead tuHead) throws BizCheckedException;

    public TuHead getHeadByTuId(String tuId) throws BizCheckedException;

    public List<TuHead> getTuHeadList(Map<String, Object> mapQuery) throws BizCheckedException;

    public List<TuHead> getTuHeadListOnPc(Map<String, Object> params) throws BizCheckedException;

    public Integer countTuHeadOnPc(Map<String, Object> mapQuery) throws BizCheckedException;

    public Integer countTuHead(Map<String, Object> mapQuery) throws BizCheckedException;

    public TuHead removeTuHead(String tuId) throws BizCheckedException;

    public TuDetail create(TuDetail tuDetail) throws BizCheckedException;

    public TuDetail update(TuDetail tuDetail) throws BizCheckedException;

    /**
     * 通过合板后的板子查tuDetail
     * 表中的字段是mergedContainerId 是唯一key
     *
     * @param boardId
     * @return
     * @throws BizCheckedException
     */
    public TuDetail getDetailByBoardId(Long boardId) throws BizCheckedException;

    public List<TuDetail> getTuDeailListByTuId(String tuId) throws BizCheckedException;

    public TuDetail getDetailById(Long id) throws BizCheckedException;

    public List<TuDetail> getTuDeailList(Map<String, Object> mapQuery) throws BizCheckedException;

    public TuDetail removeTuDetail(Long boardId) throws BizCheckedException;

    public Integer countTuDetail(Map<String, Object> mapQuery) throws BizCheckedException;

    public List<TuDetail> getTuDetailByStoreCode(String tuId, String deliveryCode) throws BizCheckedException;

    public TuHead changeTuHeadStatus(String tuId, Integer status) throws BizCheckedException;

    public TuHead changeTuHeadStatus(TuHead tuHead, Integer status) throws BizCheckedException;

    Boolean postTuDetails(String tuId) throws BizCheckedException;

    TuHead receiveTuHead(Map<String, Object> mapRequest) throws BizCheckedException;

}
