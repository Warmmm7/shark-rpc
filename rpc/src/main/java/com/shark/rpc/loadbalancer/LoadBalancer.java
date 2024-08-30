package com.shark.rpc.loadbalancer;


import com.shark.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;

/**
 * 简单的负载均衡实现
 */
public interface LoadBalancer {

    /**
     * 选择服务调用
     * @param requestParams
     * @param serviceMetaInfoList
     * @return
     */
    ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList);
}
