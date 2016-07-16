package com.lsh.wms.core.dao.stock;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.stock.StockMove;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface StockMoveDao {

		void insert(StockMove stockMove);

		void update(StockMove stockMove);

		StockMove getStockMoveById(Long id);

		Integer countStockMove(Map<String, Object> params);

		List<StockMove> getStockMoveList(Map<String, Object> params);

	}