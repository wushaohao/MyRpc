package com.wh.myrpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Author:wuhao
 * @Description:服务端
 * @Date:17/7/27
 */
public class RpcServerStart {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServerStart.class);

    public static void main(String[] args) {

        LOGGER.debug("Server is start...");

        new ClassPathXmlApplicationContext("spring.xml");
    }
}
