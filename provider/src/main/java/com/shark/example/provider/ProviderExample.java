package com.shark.example.provider;

import com.shark.example.common.service.UserService;
import com.shark.rpc.RpcApp;
import com.shark.rpc.registry.LocalRegistry;
import com.shark.rpc.server.NettyHttpServer;

/**
 * 简易版启动 提供者
 */
public class ProviderExample {

    public static void main(String[] args) {
        //初始化框架
        RpcApp.init();

        //简易注册服务
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        NettyHttpServer httpServer = new NettyHttpServer();
        httpServer.run(RpcApp.getRpcConfig().getServerPort());
    }
}
