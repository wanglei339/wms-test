package com.lsh.wms.core.dao.csi;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.csi.CsiSku;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface CsiSkuDao {

	void insert(CsiSku csiSku);
	
	void update(CsiSku csiSku);
	
	CsiSku getCsiSkuById(Integer id);

    Integer countCsiSku(Map<String, Object> params);

    List<CsiSku> getCsiSkuList(Map<String, Object> params);
	
}