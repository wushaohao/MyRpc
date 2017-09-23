package com.wh.myrpc;

/**
 * @Author:wuhao
 * @Description:服务发现接口
 * @Date:17/7/29
 */
public interface ServiceDiscovery {

    /**
     * 根据服务名称查找服务地址
     *
     * @param serviceName 服务名称
     * @return 服务地址
     */
    String discover(String serviceName);
}
