package com.lsh.wms.core.service.redis.image;

import com.lsh.wms.model.image.PubImage;
import com.lsh.wms.core.common.BaseSpringTest;
import com.lsh.wms.core.common.MockitoDependencyInjectionTestExecutionListener;
import com.lsh.wms.core.dao.redis.RedisHashDao;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestExecutionListeners;

/**
 * Created by huangdong on 16/6/23.
 */
@TestExecutionListeners(MockitoDependencyInjectionTestExecutionListener.class)
public class ImageRedisServiceTest extends BaseSpringTest {

    @Mock
    private RedisHashDao redisHashDao;

    @InjectMocks
    @Autowired
    private ImageRedisService service;

    @Test
    public void test() throws Exception {
        Mockito.doNothing().when(redisHashDao).putAll(Mockito.anyString(), Mockito.anyMap());
        PubImage image = new PubImage();
        image.setId(1L);
        service.insertRedis(image);
        System.out.println("success");
    }
}
