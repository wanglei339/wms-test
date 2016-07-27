package com.lsh.wms.api.service.po;


import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.model.base.BaseResponse;
import com.lsh.wms.api.model.po.PoRequest;
import com.lsh.wms.model.po.InbPoHeader;

import java.util.List;
import java.util.Map;

/**
 * Project Name: lsh-wms
 * Created by fuhao
 * Date: 16/7/8
 * Time: 16/7/8.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.api.service.po.
 * desc:类功能描述
 */
public interface IPoRestService {
    public String init(String poOrderInfo);

    public BaseResponse insertOrder(PoRequest request) throws BizCheckedException;

    public void editOrder(InbPoHeader inbPoHeader);

    public InbPoHeader getInbPoHeaderById(Integer id);

    public Integer countInbPoHeader(Map<String, Object> params);

    public List<InbPoHeader> getInbPoHeaderList(Map<String, Object> params);
}
