package com.shark.example.provider;

import com.shark.example.common.service.UserService;
import com.shark.rpc.registry.LocalRegistry;
import com.shark.rpc.server.NettyHttpServer;

/**
 * 简易版启动
 */
public class SimpleProviderExample {

    public static void main(String[] args) {
        //简易注册服务
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        NettyHttpServer httpServer = new NettyHttpServer();
        httpServer.run(8080);
    }
}
