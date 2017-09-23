package com.wh.myrpc.zookeeper;

import com.wh.myrpc.ServiceRegistry;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author:wuhao
 * @Description:zookeeper服务注册
 * @Date:17/7/29
 */
public class ZookeeperServiceRegistry implements ServiceRegistry{
    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperServiceRegistry.class);


    private final ZkClient zkClient;


    public ZookeeperServiceRegistry(String zkAddress){
        zkClient= new ZkClient(zkAddress,Constant.ZK_SESSION_TIMEOUT,Constant.ZK_CONNECTION_TIMEOUT);
        LOGGER.debug("zk客户度已连接....");
    }

    public void register(String serviceName, String serviceAddress) {
        // 创建注解服务节电信息
        String path=Constant.ZK_REGISTRY_PATH;
        if (!zkClient.exists(path)){
            zkClient.createPersistent(path);
            LOGGER.debug("已注册父节点路径!");
        }

        //创建服务节点信息
        String servicePath=path+"/"+serviceName;

        if (!zkClient.exists(servicePath)){
            zkClient.createPersistent(servicePath);
            LOGGER.debug("注册服务节点信息!");
        }

        //创建address临时节点信息
        String addressPath = servicePath+"/address";
        String addressNode = zkClient.createEphemeralSequential(addressPath,serviceAddress);

        LOGGER.debug("create address node: {}", addressNode);

    }


}
