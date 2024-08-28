package com.shark.rpc.registry;

import com.shark.rpc.model.ServiceMetaInfo;

import java.util.List;

/**
 * 注册中心服务本地缓存
 */
public class RegistryServiceCache {
    /**
     * 服务缓存
     */
    List<ServiceMetaInfo> serviceCache;

    /**
     * 写缓存
     * @param newServiceCache
     */
    void writeCache(List<ServiceMetaInfo> newServiceCache){
        this.serviceCache = newServiceCache;
    }

    /**
     * 读取缓存
     * @return
     */
    List<ServiceMetaInfo> readCache(){
        return this.serviceCache;
    }

    void clearCache(){
        this.serviceCache = null;
    }


}
