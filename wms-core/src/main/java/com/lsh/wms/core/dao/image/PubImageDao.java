package com.lsh.wms.core.dao.image;



import com.lsh.wms.model.image.PubImage;
import com.lsh.wms.core.dao.MyBatisRepository;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface PubImageDao {

	void insert(PubImage PubImage);
	
	void update(PubImage PubImage);
	
	PubImage getPubImageById(Integer id);

    Integer countPubImage(Map<String, Object> params);

    List<PubImage> getPubImageList(Map<String, Object> params);
	
}