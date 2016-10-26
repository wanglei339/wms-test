package com.lsh.wms.api.service.tu;

import com.lsh.base.common.exception.BizCheckedException;

import java.util.Map;

/**
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 2016/10/20 上午11:04
 */
public interface ITuRestService {
    public String getTuheadList() throws BizCheckedException;
    public String countTuHeadOnPc(Map<String, Object> mapQuery) throws BizCheckedException;
    //行程单和发货单(在php层做组装)
    //确认发货
    public String shipTu() throws BizCheckedException;

}
