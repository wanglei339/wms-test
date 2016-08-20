package com.lsh.wms.rpc.service.pick;

import com.alibaba.dubbo.common.utils.CollectionUtils;
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
    }
}
//        // 拣货位的获取所有alloc_pick_location
//        List<Map<String, Object>> pickList = new ArrayList<Map<String, Object>>();
//        // todo 排序方法的重写
////        Arrays.sort();
//        //用第一个作为全拣货位的第一个,然后比对,调整第一个
//        BaseinfoLocation locationFirst = locationService.getLocation(pickDetails.get(0).getAllocPickLocation());
//        Long minPassageNo = locationFirst.getPassageNo();
//        Long minBinColumn = locationFirst.getBinPositionNo();
//        Map<String, Object> firstLocationMap = new HashMap<String, Object>();
//        firstLocationMap.put("WaveDetail", pickDetails.get(0));
//        firstLocationMap.put("passageNo", minPassageNo);
//        firstLocationMap.put("binColumn", minBinColumn);
//        firstLocationMap.put("location", locationFirst);
//        firstLocationMap.put("order", pickDetails.get(0).getOrderId());
//
//        for (WaveDetail pickDetail : pickDetails) {
//            //map和wave、location、通道号、列、拣货orderId一个捆绑
//            Map<String, Object> pickLocationMap = new HashMap<String, Object>();
//            BaseinfoLocation location = locationService.getLocation(pickDetail.getAllocPickLocation());
//            Long passageNo = location.getPassageNo();   //通道号
//            Long binColumn = location.getBinPositionNo();   //货位的列号
//            pickLocationMap.put("WaveDetail", pickDetail);
//            pickLocationMap.put("passageNo", passageNo);
//            pickLocationMap.put("binColumn", binColumn);
//            pickLocationMap.put("location", location);
//            pickLocationMap.put("order", pickDetail.getOrderId());
//            pickList.add(pickLocationMap);
//            //找最小
//            //需要的找起始点,需要按照通道进行排序(通道号最小(通道是奇数还是偶数)和序列号最小)
//            if (passageNo.compareTo((Long) firstLocationMap.get("passageNo")) < 0 && binColumn.compareTo((Long) firstLocationMap.get("binColumn")) < 0) {
//                firstLocationMap = pickLocationMap;
//            }
//        }
//        //todo 排序算法
//        //第一次排序按照通道的大小排序
//        Collections.sort(pickList, new Comparator<Map<String, Object>>() {
//            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
//                return (Long) o1.get("passageNo") > (Long) o2.get("passageNo") ? 1 : ((Long) o1.get("passageNo") == (Long) o2.get("passageNo") ? 0 : -1);
//            }
//        });
//        //第二次排序按照序列的大小排序(起始通道是递增的,+1后通道按照序列减少)
//        //最大最小通道和最小通道的值,然后分组
//        Integer minIndexPassageNo = Integer.parseInt(((Long) pickList.get(0).get("passageNo")).toString());
//        Integer maxIndexPassageNo = Integer.parseInt(((Long) pickList.get(pickList.size() - 1).get("passageNo")).toString());
//        //最后排序完的list
//        List<Map<String, Object>> finalList = new ArrayList<Map<String, Object>>();
//
//        //按照通道进行分组
//        for (Integer i = minIndexPassageNo; i < maxIndexPassageNo; i++) {
//            //创建几个通道的数组
//            List<Map<String, Object>> temp = new ArrayList<Map<String, Object>>();
//            for (Map<String, Object> one : pickList) {
//                if (((Long) one.get("passageNo")).equals(Long.parseLong(i.toString()))) {
//                    temp.add(one);
//                }
//            }
//            //每个通道的位置排序
//            for (Map<String, Object> one : temp) {
//                //奇数 递增排列
//                if ((Integer.parseInt(((Long) one.get("passageNo")).toString()) % i) == 0) {
//
//                }
//            }
////            finalList.addAll();
//        }
//    }
//    public void calcPickOrder(List<WaveDetail> pickDetails) {
//        //通道排序
//        List<Map<String, Object>> pickList = new ArrayList<Map<String, Object>>();
//        for (WaveDetail pickDetail : pickDetails) {
////            map和wave、location、通道号、列一个捆绑
//            Map<String, Object> pickLocationMap = new HashMap<String, Object>();
//            BaseinfoLocation location = locationService.getLocation(pickDetail.getAllocPickLocation());
//            Long passageNo = location.getPassageNo();   //通道号
//            Long binColumn = location.getBinPositionNo();   //货位的列号
//            pickLocationMap.put("waveDetail", pickDetail);
//            pickLocationMap.put("passageNo", passageNo);
//            pickLocationMap.put("location", location);
//            pickLocationMap.put("locationId", location.getLocationId());
//            pickList.add(pickLocationMap);
//        }
////        第一次排序按照通道的大小排序
//        Collections.sort(pickList, new Comparator<Map<String, Object>>() {
//            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
//                return (Long) o1.get("passageNo") > (Long) o2.get("passageNo") ? 1 : ((Long) o1.get("passageNo") == (Long) o2.get("passageNo") ? 0 : -1);
//            }
//        });
//        //通道分堆
//        List<Map<String, Object>> passageNoList = new ArrayList<Map<String, Object>>(); //map中放同通道的location
//
//        //最大最小通道和最小通道的值,然后分组
//        Integer minIndexPassageNo = Integer.parseInt(((Long) pickList.get(0).get("passageNo")).toString());
//        Integer maxIndexPassageNo = Integer.parseInt(((Long) pickList.get(pickList.size() - 1).get("passageNo")).toString());
//        //将原list数组中的location按照按按同通道塞入到同一个passageNo的map结构中,map中有同通道的locationList
//        for (Integer i = minIndexPassageNo; i <= maxIndexPassageNo; i++) {
//            // todo 此处有塞入空值map的因素存在
//            Map<String, Object> locationInOnePassage = new HashMap<String, Object>();    //passageNo, Location的List
//            locationInOnePassage.put("passageNo", i);
//            List<BaseinfoLocation> locationsInOnePassageList = new ArrayList<BaseinfoLocation>();
//            //同通道分堆
//            for (Map<String, Object> one : pickList) {
//                if (one.get("passageNo").equals(Long.parseLong(i.toString()))) {
//                    locationsInOnePassageList.add((BaseinfoLocation) one.get("location"));
//                }
//            }
//
//            //分完堆就得排序不然没法操作(按通道的奇偶排序)
//            if ((i - minIndexPassageNo) % 2 == 0) { //奇数通道(相对起始通道)
//                //升序
//                Collections.sort(locationsInOnePassageList, new Comparator<BaseinfoLocation>() {
//                    public int compare(BaseinfoLocation o1, BaseinfoLocation o2) {
//                        return (Long) o1.getBinPositionNo() > (Long) o2.getBinPositionNo() ? 1 : ((Long) o1.getBinPositionNo() == (Long) o2.getBinPositionNo() ? 0 : -1);
//                    }
//                });
//            } else {//偶数
//                //降序
//                Collections.sort(locationsInOnePassageList, new Comparator<BaseinfoLocation>() {
//                    public int compare(BaseinfoLocation o1, BaseinfoLocation o2) {
//                        return (Long) o1.getBinPositionNo() < (Long) o2.getBinPositionNo() ? 1 : ((Long) o1.getBinPositionNo() == (Long) o2.getBinPositionNo() ? 0 : -1);
//                    }
//                });
//            }
//            locationInOnePassage.put("locationsInOnePassageList", locationsInOnePassageList);
//            passageNoList.add(locationInOnePassage);
//        }
//        List<BaseinfoLocation> sortList = new ArrayList<BaseinfoLocation>();    //排序好的location
//        //将每个通道排序好的location的list放到一个大的List中,将location的所在index跟新到wavedetail中的orderId中
//        Iterator it = passageNoList.iterator();
//        while (it.hasNext()) { //
//            //将每个通道排序,然后塞入一个集合中
//            Map<String, Object> tempMap = (Map<String, Object>) it.next();
//            //取出list
//            List<BaseinfoLocation> tempList = (List<BaseinfoLocation>) tempMap.get("locationsInOnePassageList");
//            //加入到新的list中
//            sortList.addAll(tempList);
//        }
//        //sortList已经排序好了,更新waveList的pickOrder
//        for (Integer i = 0; i < sortList.size(); i++) {
//            for (Map<String, Object> oneDetail : pickList) {
//                //比对locationId,然后更新map中的waveDetail,Long和Long比对
//                if (sortList.get(i).getLocationId().equals(oneDetail.get("locationId"))){
//                    //更新waveDetail
//                    WaveDetail tempWave = (WaveDetail) oneDetail.get("waveDetail");
//                    tempWave.setPickOrder(Long.parseLong(i.toString()));
//                    waveService.updateDetail(tempWave);
//                }
//            }
//        }
//    }
//}
