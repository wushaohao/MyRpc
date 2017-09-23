package com.wh.myrpc;


import com.wh.myrpc.ServiceDiscovery;
import com.wh.myrpc.bean.RpcRequest;
import com.wh.myrpc.bean.RpcResponse;
import com.wh.myrpc.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * @Author:wuhao
 * @Description:客户端代理类
 * @Date:17/7/29
 */
public class RpcProxy {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcProxy.class);

    private String serviceAddress;

    private ServiceDiscovery serviceDiscovery;

    public RpcProxy(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public RpcProxy(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }


    public <T> T create(final Class<?> interfaceClass){
        return create(interfaceClass,"");
    }

    public <T> T create(final Class<?> interfaceClass,final String serviceVersion){
        // 创建动态代理对象
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass}, new InvocationHandler() {
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        // 创建PRC请求对象并设置属性
                        RpcRequest request = new RpcRequest();
                        request.setRequestId(UUID.randomUUID().toString());
                        request.setInterfaceName(method.getDeclaringClass().getName());
                        request.setServiceVersion(serviceVersion);
                        request.setMethodName(method.getName());
                        request.setParameterTypes(method.getParameterTypes());
                        request.setParameters(args);

                        // 获取Rpc服务地址
                        if (serviceDiscovery != null) {
                            String serviceName = interfaceClass.getName();
                            if (StringUtil.isNotEmpty(serviceVersion)) {
                                serviceName += "-" + serviceVersion;
                            }
                            serviceAddress = serviceDiscovery.discover(serviceName);
                            LOGGER.debug("discover service: {} => {}", serviceName, serviceAddress);
                        }
                        if (StringUtil.isEmpty(serviceAddress)) {
                            throw new RuntimeException("server address is empty");
                        }

                        // 从 RPC 服务地址中解析主机名与端口号
                        String[] array = StringUtil.split(serviceAddress, ":");
                        String host = array[0];
                        int port = Integer.parseInt(array[1]);
                        // 创建RPC客户端并发送RPC请求
                        RpcClient rpcClient =new RpcClient(host,port);
                        long time=System.currentTimeMillis();
                        RpcResponse rpcResponse=rpcClient.send(request);
                        LOGGER.debug("time: {}ms", System.currentTimeMillis() - time);
                        if (rpcResponse == null){
                            throw new RuntimeException("response is null!");
                        }

                        //
                        if (rpcResponse.hasException()){
                            throw rpcResponse.getException();
                        }else{
                            return rpcResponse.getResult();
                        }
                    }
                });

    }
}
