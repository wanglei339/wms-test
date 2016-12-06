package com.lsh.wms.api.service.po;


import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.model.po.PoRequest;
import com.lsh.wms.model.po.IbdHeader;
import com.lsh.wms.model.so.ObdHeader;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Project Name: lsh-wms
 * Created by fuhao
 * Date: 16/7/8
 * Time: 16/7/8.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.api.service.po.
 * desc:类功能描述
 */
public interface IPoRpcService {

    public Long insertOrder(PoRequest request) throws BizCheckedException;

    public Boolean updateOrderStatus(Map<String, Object> map) throws BizCheckedException;

    public List<IbdHeader> getPoHeaderList(Map<String, Object> params);

    public IbdHeader getPoDetailByOrderId(Long orderId) throws BizCheckedException;

    public Integer countInbPoHeader(Map<String, Object> params);

    public List<IbdHeader> getPoDetailList(Map<String, Object> params);
    void canReceipt(Map<String, Object> map);

    List<IbdHeader> getIbdHeader(String storeCode) throws BizCheckedException;

    Set<ObdHeader> getObdHeader(String ibdOtherId);

    List<Map<String,Object>> getStoreInfo(Long orderId,String detailOtherId);

    void updateStatusTOthrow(Long intervalTime);

}
