package com.lsh.wms.worker;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by huangdong on 16/6/23.
 */
public class Bootstrap {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-worker.xml");
        context.start();
    }
}
