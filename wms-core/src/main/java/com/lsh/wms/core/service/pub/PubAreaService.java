package com.lsh.wms.core.service.pub;

import com.lsh.wms.model.pub.PubArea;
import com.lsh.wms.core.dao.pub.PubAreaDao;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Transactional(readOnly = true)
public class PubAreaService {

    private static Logger logger = LoggerFactory.getLogger(PubAreaService.class);

    @Autowired
    private PubAreaDao pubAreaDao;

    /**
     * 获取国家列表
     *
     * @return
     */
    public List<PubArea> getCountryList() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("areaLevel", 1);
        params.put("status", 1);
        return pubAreaDao.getPubAreaList(params);
    }

    /**
     * 获取子地区列表
     *
     * @param parentAreaCode
     * @return
     */
    public List<PubArea> getSubAreaList(String parentAreaCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtils.isBlank(parentAreaCode)) {
            params.put("areaLevel", 1);
        } else {
            params.put("parentAreaCode", parentAreaCode);
        }
        params.put("status", 1);
        return pubAreaDao.getPubAreaList(params);
    }

}
