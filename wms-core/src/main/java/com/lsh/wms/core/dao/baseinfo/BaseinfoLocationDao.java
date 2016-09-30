package com.lsh.wms.core.dao.baseinfo;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface BaseinfoLocationDao{

	void insert(BaseinfoLocation baseinfoLocation);
	
	void update(BaseinfoLocation baseinfoLocation);
	
	BaseinfoLocation getBaseinfoLocationById(Long id);

    Integer countBaseinfoLocation(Map<String, Object> params);

    List<BaseinfoLocation> getBaseinfoLocationList(Map<String, Object> params);
	//按照code精确查找location
	List<BaseinfoLocation> getLocationbyCode(Map<String, Object> params);
	//设置dock的服务,处理出入码头的关联查询
	List<BaseinfoLocation> getDockList(Map<String, Object> params);
	// 获取子节点列表
	List<BaseinfoLocation> getChildrenLocationList(Map<String, Object> params);
	//码头dock的计数
	Integer countDockList(Map<String, Object> params);

	List<BaseinfoLocation> lock(Long locationId);
	//按门店号获取升序播种位置
	List<BaseinfoLocation> sortLocationByStoreNoAndType(Map<String, Object> params);
	
}