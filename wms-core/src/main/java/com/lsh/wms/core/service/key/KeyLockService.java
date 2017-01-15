package com.lsh.wms.core.service.key;

import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.core.dao.key.KeyLockDao;
import com.lsh.wms.model.key.KeyLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 当主键锁使用
 *
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 2017/1/12 下午7:42
 */
@Component
@Transactional(readOnly = true)
public class KeyLockService {
    private static final Logger logger = LoggerFactory.getLogger(KeyLockService.class);

    @Autowired
    private KeyLockDao keyLockDao;

    @Transactional(readOnly = false)
    public void lockIdByInsert(KeyLock keyLock) {
        long time = DateUtils.getCurrentSeconds();
        keyLock.setCreatedAt(time);
        keyLock.setUpdatedAt(time);
        keyLockDao.insert(keyLock);
    }

    public KeyLock getKeyLockByKeyAndType(String key, Long type) {
        Map<String, Object> query = new HashMap<String, Object>();
        query.put("key",key);
        query.put("type",type);
        List<KeyLock> keyLocks = keyLockDao.getKeyLockList(query);
        if (null == keyLocks || keyLocks.isEmpty()) {
            return null;
        } else {
            return keyLocks.get(0);
        }
    }

    @Transactional(readOnly = false)
    public void deleteKeyLock(KeyLock keyLock) {
        keyLockDao.deleteKeyLock(keyLock);
    }

}
