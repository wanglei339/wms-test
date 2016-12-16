package com.lsh.wms.rpc.service.datareport;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.service.datareport.IDifferenceZoneReportRpcService;
import com.lsh.wms.core.service.datareport.DifferenceZoneReportService;
import com.lsh.wms.model.datareport.DifferenceZoneReport;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by lixin-mac on 2016/12/7.
 */
@Service(protocol = "dubbo")
public class DifferenceZoneReportRpcService implements IDifferenceZoneReportRpcService{
    @Autowired
    private DifferenceZoneReportService reportService;

    public List<DifferenceZoneReport> getDifferenceReportList(Map<String, Object> mapQuery) {
        return reportService.getReportList(mapQuery);
    }

    public Integer countDifferenceReport(Map<String, Object> mapQuery) {
        return reportService.countDifferenceZoneReport(mapQuery);
    }

    public void movingReport(List<Long> reportIds) throws BizCheckedException{
        reportService.movingReport(reportIds);
    }
}
