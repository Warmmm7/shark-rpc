package com.shark.rpc.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 自定义本地注册中心
 * 服务提供启动时，需要将注册服务到注册器内
 */
public class LocalRegistry {
    /**
     * 注册信息存储
     */
    private static final Map<String,Class<?>> map = new ConcurrentHashMap<>();

    /**
     * 注册服务
     * @param serviceName 服务类名
     * @param implClass 实现类 class对象
     */
    public static void register(String serviceName,Class<?> implClass){
        map.put(serviceName, implClass);
    }

    /**
     * 获取服务
     * @param serviceName
     * @return
     */
    public static Class<?> get(String serviceName){
        return map.get(serviceName);
    }

    /**
     * 删除服务
     * @param serviceName
     */
    public static void remove(String serviceName){
        map.remove(serviceName);
    }


}
