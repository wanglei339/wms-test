package com.lsh.wms.core.dao.stock;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.stock.StockQuantMoveRel;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface StockQuantMoveRelDao {

	void insert(StockQuantMoveRel stockQuantMoveRel);
	
	void update(StockQuantMoveRel stockQuantMoveRel);
	
	StockQuantMoveRel getStockQuantMoveRelById(Integer id);

    Integer countStockQuantMoveRel(Map<String, Object> params);

    List<StockQuantMoveRel> getStockQuantMoveRelList(Map<String, Object> params);
	
}