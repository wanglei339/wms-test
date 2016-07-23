package com.lsh.wms.core.dao.baseinfo;

import com.lsh.wms.core.dao.MyBatisRepository;
import com.lsh.wms.model.baseinfo.BaseinfoLocationDock;
import com.lsh.wms.model.baseinfo.IBaseinfoLocaltionModel;

import java.util.List;
import java.util.Map;

/**
 * Created by zengwenjun on 16/7/23.
 */

@MyBatisRepository
public interface IBaseinfoLocationDao {
    void insert(IBaseinfoLocaltionModel loction);

    void update(IBaseinfoLocaltionModel loction);

    IBaseinfoLocaltionModel get(Long id);

}
