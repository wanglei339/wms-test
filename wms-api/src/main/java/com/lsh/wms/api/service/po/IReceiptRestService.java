package com.lsh.wms.api.service.po;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.model.base.BaseResponse;
import com.lsh.wms.api.model.po.ReceiptRequest;

/**
 * Project Name: lsh-wms
 * Created by fuhao
 * Date: 16/7/12
 * Time: 16/7/12.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.api.service.po.
 * desc:类功能描述
 */
public interface IReceiptRestService {
    public String init(String poReceiptInfo);

    public BaseResponse insertOrder(ReceiptRequest request) throws BizCheckedException;

    /* 投单接口 */
    public String throwOrder(String orderOtherId) throws BizCheckedException;

}
