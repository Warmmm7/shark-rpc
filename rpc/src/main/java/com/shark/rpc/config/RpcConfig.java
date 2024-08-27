package com.shark.rpc.config;

import com.shark.rpc.serializer.SerializerKeys;
import lombok.Data;

/**
 * rpc默认配置框架
 */
@Data
public class RpcConfig {
    /**
     * 名称
     */
    private String name = "shark-rpc";

    /**
     * 版本
     */
    private String version = "1.0";


    /**
     * 服务器主机
     */
    private String serverHost = "localhost";

    /**
     * 服务端口号
     */
    private Integer serverPort = 8080;

    /**
     * 序列化器
     */
    private String serializer = SerializerKeys.JDK;

    /**
     * 注册中心配置
     */
    //private RegistryConfig registryConfig = new RegistryConfig();


}
