package com.lsh.wms.rpc.service.stock;

import com.alibaba.dubbo.common.json.JSON;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.task.ITestService;
import com.lsh.wms.core.service.task.TestService;
import com.lsh.wms.model.task.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.Method;

/**
 * Created by mali on 16/8/10.
 */
@Service(protocol = "rest")
@Path("test")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class TestRestService implements ITestService {
    @Autowired
    private TestService testService;

    @GET
    @Path("add")
    public String add(@QueryParam("val") Long val) {
        Test test = new Test();
        test.setVal(val);
        testService.create(test);
        return JsonUtils.SUCCESS();
    }

    @GET
    @Path("get")
    public String get(@QueryParam("id") Long id) {
        return JsonUtils.SUCCESS(testService.get(id));
    }

    @GET
    @Path("update")
    public String update(@QueryParam("id") Long id, @QueryParam("val") Long val) {
        Test test = testService.get(id);
        test.setVal(val);
        testService.update(test);
        return JsonUtils.SUCCESS();
    }

    @GET
    @Path("test")
    public String test(@QueryParam("id") Long id, @QueryParam("val") Long val) {
        try {
            Method method = testService.getClass().getDeclaredMethod("get", Long.class);
            Test test = (Test) method.invoke(testService, id);
            test.setVal(val);
            method = testService.getClass().getDeclaredMethod("update", Test.class);
            method.invoke(testService, test);
        } catch (Exception e) {
            System.out.println("fail");
            return JsonUtils.EXCEPTION_ERROR(e.getMessage());
        }
        return JsonUtils.SUCCESS();
    }
}
