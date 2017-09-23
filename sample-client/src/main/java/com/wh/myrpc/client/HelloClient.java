package com.wh.myrpc.client;

import com.wh.myrpc.RpcProxy;
import com.wh.myrpc.client.sample.api.HelloService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Author:wuhao
 * @Description:客户端test类
 * @Date:17/7/30
 */
public class HelloClient {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        RpcProxy proxy = applicationContext.getBean(RpcProxy.class);

        HelloService helloService = proxy.create(HelloService.class);
        String results = helloService.hello("SpringBoot_RPC");
        System.out.println(results);

        HelloService helloService1 = proxy.create(HelloService.class,"version");
        String results1 = helloService1.hello("远程调用框架Demo");
        System.out.println(results);

        System.exit(0);
    }
}
