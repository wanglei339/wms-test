package com.lsh.wms.web.common;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by huangdong on 16/6/23.
 */
public class MockitoDependencyInjectionTestExecutionListener extends DependencyInjectionTestExecutionListener {

    private static final String ATTRIBUTE_MOCK_FIELDS = "MOCKITO_MOCK_FIELDS";

    private static final String ATTRIBUTE_INJECT_MOCKS_FIELDS = "MOCKITO_INJECT_MOCKS_FIELDS";

    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {
        super.beforeTestClass(testContext);
        Map<String, Field> mocks = new HashMap<String, Field>();
        Set<Field> injects = new HashSet<Field>();
        for (Field field : testContext.getTestClass().getDeclaredFields()) {
            int sign = 0;
            for (Annotation annotation : field.getAnnotations()) {
                if (annotation instanceof Mock) {
                    field.setAccessible(true);
                    mocks.put(field.getName(), field);
                    continue;
                }
                if (annotation instanceof InjectMocks || annotation instanceof Autowired) {
                    if (++sign == 2) {
                        field.setAccessible(true);
                        injects.add(field);
                    }
                }
            }
        }
        testContext.setAttribute(ATTRIBUTE_MOCK_FIELDS, mocks);
        testContext.setAttribute(ATTRIBUTE_INJECT_MOCKS_FIELDS, injects);
    }

    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {
        super.beforeTestMethod(testContext);
        this.injectMockedProxyBean(testContext);
    }

    @SuppressWarnings("unchecked")
    private void injectMockedProxyBean(TestContext testContext) throws Exception {
        Map<String, Field> mockFields = (Map<String, Field>) testContext.getAttribute(ATTRIBUTE_MOCK_FIELDS);
        if (mockFields.isEmpty()) {
            return;
        }
        Object testInstance = testContext.getTestInstance();
        MockitoAnnotations.initMocks(testInstance);
        Set<Field> injectFields = (Set<Field>) testContext.getAttribute(ATTRIBUTE_INJECT_MOCKS_FIELDS);
        for (Field field : injectFields) {
            Object obj = field.get(testInstance);
            if (!(obj instanceof Proxy)) {
                continue;
            }
            Class<?> targetClass = AopUtils.getTargetClass(obj);
            if (targetClass == null) {
                continue;
            }
            if (AopUtils.isJdkDynamicProxy(obj)) {
                obj = ((Advised) obj).getTargetSource().getTarget();
            }
            for (Field targetField : targetClass.getDeclaredFields()) {
                Field mock = mockFields.get(targetField.getName());
                if (mock != null && mock.getType() == targetField.getType()) {
                    targetField.setAccessible(true);
                    targetField.set(obj, mock.get(testInstance));
                }
            }
        }
    }
}