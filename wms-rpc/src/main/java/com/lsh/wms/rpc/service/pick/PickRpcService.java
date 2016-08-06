package com.lsh.wms.rpc.service.pick;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.wms.api.service.pick.IPickRpcService;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.wave.WaveDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by fengkun on 16/8/5.
 */

@Service(protocol = "dubbo")
public class PickRpcService implements IPickRpcService {
    private static Logger logger = LoggerFactory.getLogger(PickRpcService.class);

    @Autowired
    private WaveService waveService;

    /**
     * 拣货顺序排序
     * @param pickDetails
     */
    public void calcPickOrder(List<WaveDetail> pickDetails) {
        Long order = 1L;
        for (WaveDetail pickDetail : pickDetails) {
            pickDetail.setPickOrder(order);
            waveService.updateDetail(pickDetail);
            order++;
        }
    }
}
