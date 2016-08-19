package com.lsh.wms.rpc.service.pick;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.wms.api.service.pick.IPickRpcService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.wave.WaveDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Created by fengkun on 16/8/5.
 */

@Service(protocol = "dubbo")
public class PickRpcService implements IPickRpcService {
    private static Logger logger = LoggerFactory.getLogger(PickRpcService.class);

    @Autowired
    private WaveService waveService;
    @Autowired
    private LocationService locationService;

    /**
     * 拣货顺序排序s
     * TODO 拣货顺序Z字形拣货 拣货开始的点,分布在不同的货架
     *
     * @param pickDetails
     */
    public void calcPickOrder(List<WaveDetail> pickDetails) {
        Long order = 1L;
        for (WaveDetail pickDetail : pickDetails) {
            pickDetail.setPickOrder(order);
            waveService.updateDetail(pickDetail);
            order++;
        }
        // 拣货位的获取所有alloc_pick_location
        List<Map<String, Object>> pickList = new ArrayList<Map<String, Object>>();
        // todo 排序方法的重写
//        Arrays.sort();
        //用第一个作为全拣货位的第一个,然后比对,调整第一个
        BaseinfoLocation locationFirst = locationService.getLocation(pickDetails.get(0).getAllocPickLocation());
        Long minPassageNo = locationFirst.getPassageNo();
        Long minBinColumn = locationFirst.getBinPositionNo();
        Map<String, Object> firstLocationMap = new HashMap<String, Object>();
        firstLocationMap.put("WaveDetail", pickDetails.get(0));
        firstLocationMap.put("passageNo", minPassageNo);
        firstLocationMap.put("binColumn", minBinColumn);
        firstLocationMap.put("location", locationFirst);
        firstLocationMap.put("order", pickDetails.get(0).getOrderId());

        for (WaveDetail pickDetail : pickDetails) {
            //map和wave、location、通道号、列、拣货orderId一个捆绑
            Map<String, Object> pickLocationMap = new HashMap<String, Object>();
            BaseinfoLocation location = locationService.getLocation(pickDetail.getAllocPickLocation());
            Long passageNo = location.getPassageNo();   //通道号
            Long binColumn = location.getBinPositionNo();   //货位的列号
            pickLocationMap.put("WaveDetail", pickDetail);
            pickLocationMap.put("passageNo", passageNo);
            pickLocationMap.put("binColumn", binColumn);
            pickLocationMap.put("location", location);
            pickLocationMap.put("order", pickDetail.getOrderId());
            pickList.add(pickLocationMap);
            //找最小
            //需要的找起始点,需要按照通道进行排序(通道号最小(通道是奇数还是偶数)和序列号最小)
            if (passageNo.compareTo((Long) firstLocationMap.get("passageNo")) < 0 && binColumn.compareTo((Long) firstLocationMap.get("binColumn")) < 0) {
                firstLocationMap = pickLocationMap;
            }
        }

        //第一次排序按照通道的大小排序
        //第二次排序按照序列的大小排序(起始通道是递增的,+1后通道按照序列减少)
    }
}
