package com.wh.myrpc.server;


import com.wh.myrpc.bean.RpcRequest;
import com.wh.myrpc.bean.RpcResponse;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMember;
import net.sf.cglib.reflect.FastMethod;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @Author:wuhao
 * @Description:
 * @Date:17/7/27
 */
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServerHandler.class);

    private final Map<String, Object> handlerMap;

    public RpcServerHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest rpcRequest) throws Exception {
        //创建并初始化RPC响应对象
        RpcResponse rpcResponse=new RpcResponse();
        rpcResponse.setRequestId(rpcRequest.getRequestId());

        Object result = handle(rpcRequest);
        rpcResponse.setResult(result);

        // 写入 RPC 响应对象并自动关闭连接
        channelHandlerContext.writeAndFlush(rpcResponse).addListener(ChannelFutureListener.CLOSE);

    }

    private Object handle(RpcRequest request) throws Exception{
        // 获取服务对象
        String serviceName = request.getInterfaceName();
        String serviceVersion = request.getServiceVersion();

        if (StringUtils.isNotEmpty(serviceVersion)){
            serviceName+="-"+serviceVersion;
        }

        Object serviceBean = handlerMap.get(serviceName);
        if (serviceBean==null){
            throw new RuntimeException(String.format("can not find service bean by key: %s", serviceName));
        }

        // 获取反射调用所需参数
        Class<?> serviceClass= serviceBean.getClass();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();

        // 执行反射调用
//        Method method = serviceClass.getMethod(methodName, parameterTypes);
//        method.setAccessible(true);
//        return method.invoke(serviceBean, parameters);

        // 使用 CGLib 执行反射调用(避免反射的性能问题)
        FastClass serviceFastClass=FastClass.create(serviceClass);
        FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName,parameterTypes);
        return serviceFastMethod.invoke(serviceBean,parameters);
    }


}
