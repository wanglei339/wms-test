package com.lsh.wms.core.service.image;

import com.lsh.wms.model.image.PubImage;
import com.lsh.wms.core.dao.image.PubImageDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class PubImageService {

    private static Logger logger = LoggerFactory.getLogger(PubImageService.class);

    @Autowired
    private PubImageDao pubImageDao;


    /**
     * 插入图片
     * @param pubImage
     * @return
     */
    @Transactional(readOnly = false)
    public void insert(PubImage pubImage) {
        pubImageDao.insert(pubImage);
    }

}
