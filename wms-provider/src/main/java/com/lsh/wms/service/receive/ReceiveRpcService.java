package com.lsh.wms.service.receive;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.service.po.IReceiptRpcService;
import com.lsh.wms.api.service.po.IReceiveRpcService;
import com.lsh.wms.core.service.po.ReceiveService;
import com.lsh.wms.model.po.ReceiveHeader;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by lixin-mac on 2016/10/21.
 */
@Service(protocol = "dubbo")
public class ReceiveRpcService implements IReceiveRpcService{

    @Autowired
    private ReceiveService receiveService;

    public List<ReceiveHeader> getReceiveHeaderList(Map<String, Object> params) {
        return receiveService.getReceiveHeaderList(params);
    }

    public Integer countReceiveHeader(Map<String, Object> params) {
        return receiveService.countReceiveHeader(params);
    }

    public ReceiveHeader getReceiveDetailList(Long receiveId) {


        if (receiveId == null) {
            throw new BizCheckedException("1020001", "参数不能为空");
        }

        ReceiveHeader receiveHeader = receiveService.getReceiveHeaderByReceiveId(receiveId);

        receiveService.fillDetailToHeader(receiveHeader);

        return receiveHeader;
    }

    public Boolean updateOrderStatus(Map<String, Object> map) throws BizCheckedException {
        ReceiveHeader receiveHeader = new ReceiveHeader();
        receiveHeader.setOrderStatus(Integer.valueOf(String.valueOf(map.get("orderStatus"))));
        receiveHeader.setReceiveId(Long.valueOf(String.valueOf(map.get("receiveId"))));
        receiveService.updateStatus(receiveHeader);
        return true;
    }
}
