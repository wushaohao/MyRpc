package com.wh.myrpc.client.sample.api;

/**
 * @Author:wuhao
 * @Description:
 * @Date:17/7/30
 */
public interface HelloService {
    String hello(String name);

    String hello(Person person);
}
