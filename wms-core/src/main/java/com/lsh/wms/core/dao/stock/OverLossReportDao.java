package com.lsh.wms.core.dao.stock;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.stock.OverLossReport;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface OverLossReportDao {

	void insert(OverLossReport overLossReport);
	
	void update(OverLossReport overLossReport);
	
	OverLossReport getOverLossReportById(Long id);

    Integer countOverLossReport(Map<String, Object> params);

    List<OverLossReport> getOverLossReportList(Map<String, Object> params);
	
}