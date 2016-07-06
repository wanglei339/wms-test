package com.lsh.wms.web.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Created by huangdong on 16/6/23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:spring/applicationContext-web.xml", "classpath:spring/spring-mvc-test.xml"})
public class BaseMvcTest {

    @Autowired
    private WebApplicationContext wac;

    protected MockMvc mvc;

    private ObjectMapper mapper;

    @Rule
    public final ExpectedException exception = ExpectedException.none();


    @Before
    public void before() {
        this.mvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @After
    public void after() throws Exception {
    }

    private ObjectMapper getMapper() {
        if (this.mapper == null) {
            this.mapper = new ObjectMapper();
            this.mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        }
        return this.mapper;
    }

    protected String toJson(Object obj) throws Exception {
        return this.getMapper().writeValueAsString(obj);
    }
}
