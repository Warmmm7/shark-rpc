package com.shark.rpc.server;

/**
 * 基于netty是实现的web
 */
public interface HttpServer {

    /**
     * 启动web服务
     * @param port
     */
    void run (int port);
}
