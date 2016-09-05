package com.lsh.wms.core.dao.baseinfo;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.baseinfo.BaseinfoItemQuantRange;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface BaseinfoItemQuantRangeDao {

	void insert(BaseinfoItemQuantRange baseinfoItemQuantRange);
	
	void update(BaseinfoItemQuantRange baseinfoItemQuantRange);
	
	BaseinfoItemQuantRange getBaseinfoItemQuantRangeById(Long id);

    Integer countBaseinfoItemQuantRange(Map<String, Object> params);

    List<BaseinfoItemQuantRange> getBaseinfoItemQuantRangeList(Map<String, Object> params);
	
}