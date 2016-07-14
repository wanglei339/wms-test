package com.lsh.wms.api.service.lot;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.wms.model.baseinfo.BaseinfoLot;
import com.lsh.wms.model.csi.CsiSku;

import java.util.List;
import java.util.Map;

/**
 * Created by Ming on 7/11/16.
 */

@Service(protocol = "dubbo")
public interface ILotRpcService {
    public BaseinfoLot getLotByLotId(long iLotId);
    public BaseinfoLot insertLot(BaseinfoLot lot);
    public int updateLot(BaseinfoLot lot);
    public List<BaseinfoLot> searchLot(Map<String, Object> mapQuery);
}
