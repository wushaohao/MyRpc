package com.wh.myrpc.impl;

import com.wh.myrpc.client.sample.api.HelloService;
import com.wh.myrpc.client.sample.api.Person;

/**
 * @Author:wuhao
 * @Description:实现类
 * @Date:17/7/30
 */
public class HelloServiceImpl implements HelloService {
    public String hello(String name) {
        return "Hello" + name;
    }

    public String hello(Person person) {
        return "Hello" + person.getFirstName();
    }
}
