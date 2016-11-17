package com.lsh.wms.api.service.po;

import com.alibaba.dubbo.common.json.ParseException;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.model.base.BaseResponse;
import com.lsh.wms.api.model.po.IbdRequest;
import com.lsh.wms.api.model.wumart.CreateIbdHeader;

import java.util.Map;

/**
 * Created by mali on 16/9/2.
 */
public interface IIbdService {

    BaseResponse add(IbdRequest request)throws BizCheckedException;

    BaseResponse addRelation() throws BizCheckedException, ParseException;

    String bdSendIbd2Sap(CreateIbdHeader createIbdHeader);

    String Test();

    String sendSap();

    String sendSapObd();


}
