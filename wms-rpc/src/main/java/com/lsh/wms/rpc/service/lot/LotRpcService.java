package com.lsh.wms.rpc.service.lot;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.wms.api.service.csi.ICsiRpcService;
import com.lsh.wms.api.service.lot.ILotRpcService;
import com.lsh.wms.core.service.lot.LotService;
import com.lsh.wms.model.baseinfo.BaseinfoLot;
import com.lsh.wms.model.csi.CsiSku;
import com.lsh.wms.rpc.service.csi.CsiRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import java.util.List;
import java.util.Map;

/**
 * Created by Ming on 7/11/16.
 */

@Service(protocol = "dubbo")
public class LotRpcService implements ILotRpcService{
    public static Logger logger = LoggerFactory.getLogger(LotRpcService.class);

    @Autowired
    private LotService lotService;
    @Reference
    private ICsiRpcService remoteCsiRpcServcie;

    public BaseinfoLot getLotByLotId(long iLotId) {
        return lotService.getLotByLotId(iLotId);
    }

    public BaseinfoLot insertLot(BaseinfoLot lot) {
        lotService.insertLot(lot);
        return lot;
    }

    public int updateLot(BaseinfoLot lot) {
        return lotService.updateLot(lot);
    }

    public List<BaseinfoLot> searchLot(Map<String, Object> mapQuery) {
        return lotService.searchLot(mapQuery);
    }

}
