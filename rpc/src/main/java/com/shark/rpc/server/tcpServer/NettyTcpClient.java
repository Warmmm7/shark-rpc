package com.shark.rpc.server.tcpServer;

import com.shark.rpc.model.RpcRequest;
import com.shark.rpc.model.RpcResponse;
import com.shark.rpc.protocol.codec.NettyMessageDecoder;
import com.shark.rpc.protocol.codec.NettyMessageEncoder;
import com.shark.rpc.protocol.common.ProtocolMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.CompletableFuture;

public class NettyTcpClient {
    private String host;
    private int port;
    private Channel channel;
    public NettyTcpClient(String host,int port){
        this.host = host;
        this.port = port;
    }

    public void start(){
        NioEventLoopGroup group = new NioEventLoopGroup(1);
        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 添加用于解决粘包和拆包问题的处理器
                            //pipeline.addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4));
                            //pipeline.addLast(new LengthFieldPrepender(4));
                            // 编码解码
                            pipeline.addLast(new NettyMessageEncoder());
                            pipeline.addLast(new NettyMessageDecoder());
                        }
                    });
            ChannelFuture future = bootstrap.connect(host, port).sync();
            channel = future.channel();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            //group.shutdownGracefully();
        }
    }

    public Object sendRequest(ProtocolMessage request) throws Exception {
        if (channel != null && channel.isActive()) {
            CompletableFuture responseFuture = new CompletableFuture<>();
            channel.pipeline().addLast(new NettyTcpClientHandler(responseFuture));

            // 发送请求
            channel.writeAndFlush(request);

            // 等待并获取响应结果
            return responseFuture.get();
        } else {
            throw new IllegalStateException("Channel is not active. Cannot send message.");
        }
    }

    public void shutdown() {
        if (channel != null) {
            channel.close();
        }
    }


    public static void main(String[] args) {
        new NettyTcpClient("127.0.0.1", 8888).start();
    }
}
