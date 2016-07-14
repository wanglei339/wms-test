package com.lsh.wms.api.service.lot;

import com.lsh.wms.model.baseinfo.BaseinfoLot;

import java.util.Map;

/**
 * Created by Ming on 7/11/16.
 */
public interface ILotRestService {
    public String getLotByLotId(long iLotId);
    public String insertLot(BaseinfoLot lot);
    public String updateLot(BaseinfoLot lot);
    public String searchLot(Map<String, Object> mapQuery);

}
