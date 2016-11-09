package com.lsh.wms.core.service.wave;

import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.core.dao.wave.WaveTemplateDao;
import com.lsh.wms.model.wave.WaveHead;
import com.lsh.wms.model.wave.WaveTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zengwenjun on 16/8/19.
 */
@Component
@Transactional(readOnly = true)
public class WaveTemplateService {
    private static final Logger logger = LoggerFactory.getLogger(WaveTemplateService.class);

    @Autowired
    private WaveTemplateDao waveTemplateDao;

    @Transactional(readOnly = false)
    public void createWaveTemplate(WaveTemplate tpl)
    {
        tpl.setCreatedAt(DateUtils.getCurrentSeconds());
        tpl.setUpdatedAt(DateUtils.getCurrentSeconds());
        tpl.setWaveTemplateId(RandomUtils.genId());
        waveTemplateDao.insert(tpl);
    }

    @Transactional(readOnly = false)
    public void updateWaveTemplate(WaveTemplate tpl)
    {
        tpl.setUpdatedAt(DateUtils.getCurrentSeconds());
        waveTemplateDao.update(tpl);
    }

    @Transactional(readOnly = true)
    public WaveTemplate getWaveTemplate(long tplId){
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("waveTemplateId", tplId);
        List<WaveTemplate> WaveTplList = waveTemplateDao.getWaveTemplateList(mapQuery);
        return WaveTplList.size() == 0 ? null : WaveTplList.get(0);
    }

    @Transactional(readOnly = false)
    public List<WaveTemplate> getWaveTemplateList(Map<String, Object> mapQuery){
        return waveTemplateDao.getWaveTemplateList(mapQuery);
    }

    @Transactional(readOnly = false)
    public int getWaveTemplateCount(Map<String, Object> mapQuery){
        return waveTemplateDao.countWaveTemplate(mapQuery);
    }

}
