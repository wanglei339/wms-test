package com.lsh.wms.api.service.datareport;

import java.util.List;
import java.util.Map;

/**
 * Created by lixin-mac on 2016/12/7.
 */
public interface IDifferenceZoneReportRestService {
    String getDifferenceReportList(Map<String, Object> mapQuery);
    String countDifferenceReport(Map<String, Object> mapQuery);

    String movingReport();

}
