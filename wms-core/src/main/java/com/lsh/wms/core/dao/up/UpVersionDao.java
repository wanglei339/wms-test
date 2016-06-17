package com.lsh.wms.core.dao.up;


import com.lsh.wms.api.model.up.UpVersion;
import com.lsh.wms.core.dao.MyBatisRepository;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface UpVersionDao {

    void insert(UpVersion version);

    void update(UpVersion version);

    UpVersion getVersionById(Long id);

    Integer countVersion(Map<String, Object> params);

    List<UpVersion> getVersionList(Map<String, Object> params);

    Integer countVersionNo(Map<String, Object> params);

    List<UpVersion> getVersionNoList(Map<String, Object> params);

}