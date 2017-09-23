package com.wh.myrpc.impl;

import com.wh.myrpc.bean.Person;
import com.wh.myrpc.interfaces.IHelloServiceSV;

/**
 * @Author:wuhao
 * @Description
 * @Date:17/7/27
 */
public class ServiceScanSVImpl2 implements IHelloServiceSV{
    public String hello(String name) {
        return "你好"+name;
    }

    public String hello(Person person) {
        return "你好"+person.getFirstName();
    }
}
