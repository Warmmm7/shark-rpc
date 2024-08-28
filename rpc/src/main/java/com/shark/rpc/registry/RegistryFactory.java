package com.shark.rpc.registry;

import com.shark.rpc.spi.SpiLoader;

/**
 * 注册中心工厂 获取注册中心对象
 */
public class RegistryFactory {
    static {
        SpiLoader.load(Registry.class);
    }

    /**
     * 默认注册中心
     */
    private static final Registry DEFAULT_REGISTRY = new ZooKeeperRegistry();

    /**
     * 获取注册中心实例
     * @param key
     * @return
     */
    public static Registry getInstance(String key){
        return SpiLoader.getInstance(Registry.class,key);
    }
}
