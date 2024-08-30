package com.shark.rpc.server.httpServer;

/**
 * 基于netty是实现的web
 */
public interface HttpServer {

    /**
     * 启动webServer服务
     * @param port
     */
    void run (int port);
}
