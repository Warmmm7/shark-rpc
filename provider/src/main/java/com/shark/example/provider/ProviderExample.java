package com.shark.example.provider;

import com.shark.example.common.service.UserService;
import com.shark.rpc.RpcApp;
import com.shark.rpc.config.RegistryConfig;
import com.shark.rpc.config.RpcConfig;
import com.shark.rpc.model.ServiceMetaInfo;
import com.shark.rpc.registry.LocalRegistry;
import com.shark.rpc.registry.Registry;
import com.shark.rpc.registry.RegistryFactory;
import com.shark.rpc.server.httpServer.NettyHttpServer;
import com.shark.rpc.server.tcpServer.NettyTcpServer;

/**
 * 简易版启动 提供者
 */
public class ProviderExample {

    public static void main(String[] args) {
        //初始化框架
        RpcApp.init();

        //注册服务
        String serverName = UserService.class.getName();
        LocalRegistry.register(serverName, UserServiceImpl.class);

        //注册到服务中心
        RpcConfig rpcConfig = RpcApp.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serverName);
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());

        try {
            registry.register(serviceMetaInfo);
        }catch (Exception e){
            throw new RuntimeException(e);
        }

        //启动web
        NettyHttpServer httpServer = new NettyHttpServer();
        httpServer.run(RpcApp.getRpcConfig().getServerPort());

        //启动自定义的tcp
//        NettyTcpServer tcpServer = new NettyTcpServer();
//        tcpServer.doStart(8080);
    }
}
