package com.shark.rpc.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 服务注册信息 快捷启动
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceRegistryInfo<T> {

    /**
     * 服务名
     */
    private String serviceName;

    /**
     * 实现类
     */
    private Class<? extends T> implClass;
}
