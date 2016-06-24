package com.lsh.wms.core.common;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by huangdong on 16/6/23.
 */
public abstract class BaseTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }
}