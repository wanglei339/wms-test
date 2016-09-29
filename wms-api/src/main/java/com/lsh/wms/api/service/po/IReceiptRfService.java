package com.lsh.wms.api.service.po;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.model.base.BaseResponse;
import com.lsh.wms.api.model.po.ReceiptRequest;

import javax.ws.rs.QueryParam;
import java.text.ParseException;

/**
 * Project Name: lsh-wms
 * Created by fuhao
 * Date: 16/7/12
 * Time: 16/7/12.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.api.service.po.
 * desc:类功能描述
 */
public interface IReceiptRfService {

    public String insertOrder() throws BizCheckedException, ParseException;

    public String getPoDetailByOrderIdAndBarCode(String orderOtherId,Long containerId, String barCode) throws BizCheckedException;


    String getStoreInfo(String storeId,Long containerId, String barCode) throws BizCheckedException;
}
