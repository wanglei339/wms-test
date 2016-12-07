package com.lsh.wms.core.service.datareport;

import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.core.dao.datareport.DifferenceZoneReportDao;
import com.lsh.wms.model.datareport.DifferenceZoneReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

}
