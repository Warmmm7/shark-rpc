package com.shark.rpc.config;

import com.shark.rpc.registry.RegistryKeys;
import lombok.Data;

/**
 * PRC注册中心配置
 */
@Data
public class RegistryConfig {
    /**
     * 注册中心类别
     */
    private String registry = RegistryKeys.ZOOKEEPER;

    /**
     * 注册中心地址
     */
    private String address = "47.109.184.254:2181";

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 超时时间（单位毫秒）
     */
    private Long timeout = 10000L;
}
