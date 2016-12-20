package com.lsh.wms.core.service.datareport;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.dao.datareport.DifferenceZoneReportDao;
import com.lsh.wms.core.service.item.ItemLocationService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.stock.StockMoveService;
import com.lsh.wms.model.baseinfo.BaseinfoItemLocation;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.datareport.DifferenceZoneReport;
import com.lsh.wms.model.stock.StockMove;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lixin-mac on 2016/12/7.
 */
@Component
@Transactional(readOnly = true)
public class DifferenceZoneReportService {
    private static final Logger logger = LoggerFactory.getLogger(DifferenceZoneReportService.class);

    @Autowired
    private DifferenceZoneReportDao reportDao;

    @Autowired
    private StockMoveService moveService;

    @Autowired
    private ItemLocationService itemLocationService;

    @Autowired
    private LocationService locationService;

    @Transactional(readOnly = false)
    public void insertReport(DifferenceZoneReport report){
        report.setDifferenceId(RandomUtils.genId());
        report.setCreatedAt(DateUtils.getCurrentSeconds());
        report.setUpdatedAt(DateUtils.getCurrentSeconds());
        reportDao.insert(report);
    }

    public List<DifferenceZoneReport> getReportList(Map<String, Object> mapQuery){
        return reportDao.getDifferenceZoneReportList(mapQuery);
    }

    public Integer countDifferenceZoneReport(Map<String, Object> mapQuery){
        return reportDao.countDifferenceZoneReport(mapQuery);
    }

    public DifferenceZoneReport getReportByReportId(Long reportId){
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("differenceId",reportId);
        List<DifferenceZoneReport> list = this.getReportList(map);
        if(list == null || list.size() <= 0){
            return  null;
        }
        return list.get(0);
    }

    @Transactional(readOnly = false)
    public void batchUpdate(List<DifferenceZoneReport> updateReport){
        reportDao.batchUpdate(updateReport);
    }

    @Transactional(readOnly = false)
    public void movingReport(List<Long> reportIds) throws BizCheckedException{

        // 在库SO预占库存。
        List<StockMove> moveList = new ArrayList<StockMove>();
        List<DifferenceZoneReport> updateReport = new ArrayList<DifferenceZoneReport>();
        // 根据排序后的detailIst进行库存占用
        for (Long reportId : reportIds) {
            DifferenceZoneReport report = this.getReportByReportId(reportId);
            StockMove move = new StockMove();
            move.setItemId(report.getItemId());
            move.setQty(report.getQty());
            // TODO: 16/8/19 找货品对应的拣货位
            List<BaseinfoItemLocation> itemLocations = itemLocationService.getItemLocationList(report.getItemId());
            for(BaseinfoItemLocation itemLocation : itemLocations){
                // TODO: 16/8/19  判断拣货位是否可用
                BaseinfoLocation location = locationService.getLocation(itemLocation.getPickLocationid());
                if((location.getCanUse().equals(LocationConstant.CAN_USE)) && location.getIsLocked().equals(LocationConstant.UNLOCK)){
                    move.setToLocationId(location.getLocationId());
                    break;
                }
            }
            if(move.getToLocationId().equals(0L)){
                throw new BizCheckedException("2910001");
            }
            move.setFromLocationId(locationService.getDiffAreaLocation().getLocationId());
            move.setTaskId(reportId);
            moveList.add(move);

            report.setUpdatedAt(DateUtils.getCurrentSeconds());
            report.setStatus(2);
            updateReport.add(report);
            logger.info("~~~~~1111stockMove : " + move);
        }

        moveService.move(moveList);
        this.batchUpdate(updateReport);

    }

}
