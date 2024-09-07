package com.shark.example.provider;

import com.shark.example.common.service.UserService;
import com.shark.rpc.RpcApp;
import com.shark.rpc.bootstrap.ProviderBootstrap;
import com.shark.rpc.config.RegistryConfig;
import com.shark.rpc.config.RpcConfig;
import com.shark.rpc.model.ServiceMetaInfo;
import com.shark.rpc.model.ServiceRegistryInfo;
import com.shark.rpc.registry.LocalRegistry;
import com.shark.rpc.registry.Registry;
import com.shark.rpc.registry.RegistryFactory;
import com.shark.rpc.server.httpServer.NettyHttpServer;
import com.shark.rpc.server.tcpServer.NettyTcpServer;

import java.util.ArrayList;
import java.util.List;

/**
 * 简易版启动 提供者
 */
public class ProviderExample {

    public static void main(String[] args) {
        //加入需要的服务
        List<ServiceRegistryInfo> serviceRegistryInfoList = new ArrayList<>();
        ServiceRegistryInfo serviceRegistryInfo = new ServiceRegistryInfo(UserService.class.getName(), UserServiceImpl.class);
        serviceRegistryInfoList.add(serviceRegistryInfo);

        //服务提供者初始化
        ProviderBootstrap.init(serviceRegistryInfoList);

    }
}
