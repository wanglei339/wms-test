package com.lsh.wms.api.service.datareport;

import com.lsh.wms.model.datareport.DifferenceZoneReport;

import java.util.List;
import java.util.Map;

/**
 * Created by lixin-mac on 2016/12/7.
 */
public interface IDifferenceZoneReportRpcService {
    List<DifferenceZoneReport> getDifferenceReportList(Map<String, Object> mapQuery);
    Integer countDifferenceReport(Map<String, Object> mapQuery);

    String movingReport(List<Long> reportIds);
}
