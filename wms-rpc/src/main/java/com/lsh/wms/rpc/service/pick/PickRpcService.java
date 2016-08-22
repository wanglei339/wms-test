package com.lsh.wms.rpc.service.pick;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
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
     * 拣货顺序排序z型排序
     * @param pickDetails
     */
    public void calcPickOrder(List<WaveDetail> pickDetails) throws BizCheckedException{
        if (!(pickDetails.size() > 0)) {
            throw new BizCheckedException("2040010");
        }
        List<Map<String, Object>> pickList = new ArrayList<Map<String, Object>>();
        for (WaveDetail pickDetail : pickDetails) { //查找拣货位location
//            map和wave、location、通道号捆绑
            Map<String, Object> pickLocationMap = new HashMap<String, Object>();
            BaseinfoLocation location = locationService.getLocation(pickDetail.getAllocPickLocation());
            Long passageNo = location.getPassageNo();   //通道号
            Long binColumn = location.getBinPositionNo();   //货位的列号
            pickLocationMap.put("waveDetail", pickDetail);
            pickLocationMap.put("passageNo", passageNo);
            pickLocationMap.put("location", location);
            pickList.add(pickLocationMap);
        }
        //货位按通道排序
        Collections.sort(pickList, new Comparator<Map<String, Object>>() {
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                return (Long) o1.get("passageNo") > (Long) o2.get("passageNo") ? 1 : ((Long) o1.get("passageNo") == (Long) o2.get("passageNo") ? 0 : -1);
            }
        });
        //货位按通道分组(原则,有几个通道建几个通道)
        Map<Long, List<BaseinfoLocation>> passageNoMap = new LinkedHashMap<Long, List<BaseinfoLocation>>(); //map中放同通道的locationList,使用LinkedHashMap,为了记住map的存放顺序
        List<BaseinfoLocation> list = new ArrayList<BaseinfoLocation>();
        for (int i = 0; i < pickList.size(); i++) { //此处不用迭代器是为了按照顺序取list(已按照passage拍过一次)
            //根据通道号不同,新建通道的map
            Map<String, Object> temp = pickList.get(i);
            BaseinfoLocation location = (BaseinfoLocation) temp.get("location");
            Long passageNo = (Long) temp.get("passageNo");
            if (passageNoMap.get(passageNo) == null) { //如果没有创建,有的话拿出来存进去
                List<BaseinfoLocation> locations = new ArrayList<BaseinfoLocation>();
                locations.add(location);
                passageNoMap.put(passageNo, locations);
            } else {
                List<BaseinfoLocation> locationList = passageNoMap.get(passageNo);
                locationList.add(location);
                passageNoMap.put(passageNo, locationList);
            }
        }
        //同通道货位排序
        List<BaseinfoLocation> sortList = new ArrayList<BaseinfoLocation>();
        int count = 0;
        //遍历不同通道 位置数组的map,为了不同通道货位的正序和逆序的蛇形排列
        for (Long key : passageNoMap.keySet()) {
            List<BaseinfoLocation> tempList = passageNoMap.get(key);
            //奇数升序
            if (count % 2 == 0) {
                Collections.sort(tempList, new Comparator<BaseinfoLocation>() {
                    public int compare(BaseinfoLocation o1, BaseinfoLocation o2) {
                        return o1.getBinPositionNo() > o2.getBinPositionNo() ? 1 : ((o1.getBinPositionNo() == o2.getBinPositionNo()) ? 0 : -1);
                    }
                });
            } else {// 降序
                Collections.sort(tempList, new Comparator<BaseinfoLocation>() {
                    public int compare(BaseinfoLocation o1, BaseinfoLocation o2) {
                        return o1.getBinPositionNo() < o2.getBinPositionNo() ? 1 : ((o1.getBinPositionNo() == o2.getBinPositionNo()) ? 0 : -1);
                    }
                });
            }
            sortList.addAll(tempList);
            count++;
        }
        //更新wave
        for (int index = 0; index < sortList.size(); index++) {
            for (WaveDetail tempWave : pickDetails) {
                if (sortList.get(index).getLocationId().equals(tempWave.getAllocPickLocation())) {
                    tempWave.setPickOrder(Long.parseLong(Integer.toString(index + 1)));
                    try {
                        waveService.updateDetail(tempWave);
                    }catch (Exception e){
                        logger.error(e.getCause().getMessage());
                        throw new BizCheckedException("2040011");
                    }
                }
            }
        }
    }
}
