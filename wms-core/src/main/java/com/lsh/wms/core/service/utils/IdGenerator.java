package com.lsh.wms.core.service.utils;

import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.core.constant.IdGeneratorContant;
import com.lsh.wms.core.dao.utils.IdCounterDao;
import com.lsh.wms.model.utils.IdCounter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by fengkun on 16/8/26.
 */
@Component
@Transactional(readOnly = true)
public class IdGenerator {
    @Autowired
    private IdCounterDao idCounterDao;

    /**
     * id生成方法
     * 格式: 992016082600000088 (99-可选自定义前缀;20160826-可选当前日期;00000088计数器)
     * @param prefix key前缀
     * @param useDateFormat 是否使用日期格式
     * @param addPrefixNum 是否加标识前缀
     * @return
     */
    @Transactional(readOnly = false)
    public Long genId(String prefix, Boolean useDateFormat, Boolean addPrefixNum) {
        Long counter = 1L; // 计数器
        Long value = 0L; // 返回值
        String idKey = prefix; // 计数器的key
        String dateValue = ""; // 日期格式

        // 使用日期格式时设置key的格式: prefix+yyMMdd
        if (useDateFormat) {
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
            dateValue = dateFormat.format(date);
            idKey += dateValue;
        }

        IdCounter idCounter = idCounterDao.getIdCounterByIdKey(idKey);

        // 自增计数器
        if (idCounter == null) {
            idCounter = new IdCounter();
            idCounter.setIdKey(idKey);
            idCounter.setCounter(counter);
            idCounter.setCreatedAt(DateUtils.getCurrentSeconds());
            idCounter.setUpdatedAt(DateUtils.getCurrentSeconds());
            idCounterDao.insert(idCounter);
        } else {
            counter = idCounter.getCounter();
            counter++;
            idCounter.setCounter(counter);
            idCounter.setUpdatedAt(DateUtils.getCurrentSeconds());
            idCounterDao.update(idCounter);
        }

        // 设置返回值前缀
        if (addPrefixNum) {
            Integer prefixNum = IdGeneratorContant.PREFIX_CONFIG.get(prefix);
            if (prefixNum != null) {
                value += Long.valueOf(prefixNum.toString() + String.format("%014d", 0));
            }
        }

        // 拼接返回值
        if (useDateFormat) {
            String strValue = String.format("%08d", counter);
            value += Long.valueOf(dateValue + strValue);
        } else {
            value += counter;
        }

        return value;
    }


}
