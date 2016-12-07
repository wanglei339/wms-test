package com.lsh.wms.core.dao.datareport;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.datareport.DifferenceZoneReport;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface DifferenceZoneReportDao {

	void insert(DifferenceZoneReport differenceZoneReport);
	
	void update(DifferenceZoneReport differenceZoneReport);
	
	DifferenceZoneReport getDifferenceZoneReportById(Long id);

    Integer countDifferenceZoneReport(Map<String, Object> params);

    List<DifferenceZoneReport> getDifferenceZoneReportList(Map<String, Object> params);
	
}