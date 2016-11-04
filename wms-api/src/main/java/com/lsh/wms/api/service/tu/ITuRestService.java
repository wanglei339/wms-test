package com.lsh.wms.api.service.tu;

import com.lsh.base.common.exception.BizCheckedException;

import java.util.Map;

/**
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 2016/10/20 上午11:04
 */
public interface ITuRestService {
    public String getTuheadList() throws BizCheckedException;

    public String getTuDeailListByTuId(String tuId) throws BizCheckedException;

    public String getDetailById(Long id) throws BizCheckedException;

    public String getTuDetailList(Map<String, Object> mapQuery) throws BizCheckedException;

    public String getTuheadByTuId(String tuId) throws BizCheckedException;

    public String countTuHeadOnPc(Map<String, Object> mapQuery) throws BizCheckedException;

    //行程单和发货单(在php层做组装)
    //确认发货
    public String shipTu() throws BizCheckedException;

    String receiveTuHead() throws BizCheckedException;

    public String changeRfRestSwitch() throws BizCheckedException;
    /**
     * 移除板子
     * @param mergedContainerId
     * @return
     * @throws BizCheckedException
     */
    public String removeTuDetail(Long mergedContainerId) throws BizCheckedException;
}
