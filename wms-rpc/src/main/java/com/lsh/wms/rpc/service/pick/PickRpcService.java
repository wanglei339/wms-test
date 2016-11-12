package com.lsh.wms.rpc.service.pick;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.service.pick.IPickRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.container.ContainerService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.pick.PickTaskService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.baseinfo.BaseinfoContainer;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.pick.PickTaskHead;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.wave.WaveDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.ast.Literal;

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
    @Autowired
    private BaseTaskService baseTaskService;
    @Autowired
    private PickTaskService pickTaskService;
    @Autowired
    private ContainerService containerService;

    /**
     * 拣货顺序排序z型排序
     *
     * @param pickDetails
     */
    public List<WaveDetail> calcPickOrder(List<WaveDetail> pickDetails) throws BizCheckedException {
        if (null == pickDetails && pickDetails.size() <= 0) {
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
        Map<Long, List<Map<String, Object>>> passageNoMap = new LinkedHashMap<Long, List<Map<String, Object>>>(); //map中放同通道的locationList,使用LinkedHashMap,为了记住map的存放顺序
        for (int i = 0; i < pickList.size(); i++) { //此处不用迭代器是为了按照顺序取list(已按照passage拍过一次)
            //根据通道号不同,新建通道的map
            Map<String, Object> temp = pickList.get(i);
            BaseinfoLocation location = (BaseinfoLocation) temp.get("location");
            WaveDetail waveDetail = (WaveDetail) temp.get("waveDetail");
            Long passageNo = (Long) temp.get("passageNo");
            if (passageNoMap.get(passageNo) == null) { //如果没有创建,有的话拿出来存进去
                List<Map<String, Object>> locations = new ArrayList<Map<String, Object>>();
                Map<String, Object> locationWaveMap = new HashMap<String, Object>();
                locationWaveMap.put("location", location);
                locationWaveMap.put("waveDetail", waveDetail);
                locationWaveMap.put("passageNo", passageNo);
                locations.add(locationWaveMap);
                passageNoMap.put(passageNo, locations);
            } else {
                List<Map<String, Object>> locationList = passageNoMap.get(passageNo);
                locationList.add(temp);
                passageNoMap.put(passageNo, locationList);
            }
        }
        //同通道货位排序
        List<Map<String, Object>> sortList = new ArrayList<Map<String, Object>>();
        int count = 0;
        //遍历不同通道 位置数组的map,为了不同通道货位的正序和逆序的蛇形排列
        for (Long key : passageNoMap.keySet()) {
            List<Map<String, Object>> tempList = passageNoMap.get(key);
            //奇数升序
            if (count % 2 == 0) {
                Collections.sort(tempList, new Comparator<Map<String, Object>>() {

                    public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                        return ((BaseinfoLocation) o1.get("location")).getBinPositionNo() > ((BaseinfoLocation) o2.get("location")).getBinPositionNo() ? 1 : (((BaseinfoLocation) o1.get("location")).getBinPositionNo() == ((BaseinfoLocation) o2.get("location")).getBinPositionNo() ? 0 : -1);
                    }
                });
            } else {// 降序
                Collections.sort(tempList, new Comparator<Map<String, Object>>() {
                    public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                        return ((BaseinfoLocation) o1.get("location")).getBinPositionNo() < ((BaseinfoLocation) o2.get("location")).getBinPositionNo() ? 1 : (((BaseinfoLocation) o1.get("location")).getBinPositionNo() == ((BaseinfoLocation) o2.get("location")).getBinPositionNo() ? 0 : -1);
                    }
                });
            }
            sortList.addAll(tempList);
            count++;
        }
        List<WaveDetail> waveDetailList = new ArrayList<WaveDetail>();
        for (int index = 0; index < sortList.size(); index++) {
            Map<String, Object> tempWaveAndLocationMap = sortList.get(index);
            WaveDetail waveDetail = (WaveDetail) tempWaveAndLocationMap.get("waveDetail");
            waveDetail.setPickOrder(Long.parseLong(Integer.toString(index + 1)));
            try {
                waveService.updateDetail(waveDetail);
                waveDetailList.add(waveDetail);
            } catch (Exception e) {
                logger.error(e.getCause().getMessage());
                throw new BizCheckedException("2040011");
            }
        }
        return waveDetailList;
    }
}
