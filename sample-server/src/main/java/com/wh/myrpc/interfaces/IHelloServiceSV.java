package com.wh.myrpc.interfaces;

import com.wh.myrpc.bean.Person;

/**
 * @Author:wuhao
 * @Description:服务接口类
 * @Date:17/7/27
 */
public interface IHelloServiceSV {

    String hello(String name);

    String hello(Person person);

}
