package com.lsh.wms.core.service.pick;

import com.lsh.wms.core.dao.pick.PickModelDao;
import com.lsh.wms.core.dao.pick.PickModelTemplateDao;
import com.lsh.wms.model.pick.PickModel;
import com.lsh.wms.model.pick.PickModelTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zengwenjun on 16/7/15.
 */
@Component
@Transactional(readOnly = true)
public class PickModelService {
    private static final Logger logger = LoggerFactory.getLogger(PickZoneService.class);

    @Autowired
    PickModelDao modelDao;
    @Autowired
    PickModelTemplateDao modelTemplateDao;

    public void createPickModelTemplate(PickModelTemplate tpl){
        modelTemplateDao.insert(tpl);
    }

    public void createPickModel(PickModel model){
        modelDao.insert(model);
    }

    public void removePickModelById(long id){
        //
    }

    public PickModelTemplate getPickModelTemplate(long iPickTemplateId){
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("pickModelTemplateId", iPickTemplateId);
        List<PickModelTemplate> pickModelTemplateList = modelTemplateDao.getPickModelTemplateList(mapQuery);
        return pickModelTemplateList.size() == 0 ? null : pickModelTemplateList.get(0);
    }

    public List<PickModel> getPickModelsByTplId(long iPickTemplateId){
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("pickModelTemplateId", iPickTemplateId);
        return modelDao.getPickModelList(mapQuery);
    }

    public PickModel getPickModel(long iPickModelId){
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("pickModelId", iPickModelId);
        List<PickModel> pickModels = modelDao.getPickModelList(mapQuery);
        return pickModels.size() == 0 ? null : pickModels.get(0);
    }

}
