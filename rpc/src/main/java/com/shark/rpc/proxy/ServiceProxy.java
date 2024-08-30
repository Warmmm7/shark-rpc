package com.shark.rpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.shark.rpc.RpcApp;
import com.shark.rpc.config.RpcConfig;
import com.shark.rpc.constant.RpcConstant;
import com.shark.rpc.fault.retry.RetryStrategy;
import com.shark.rpc.fault.retry.RetryStrategyFactory;
import com.shark.rpc.loadbalancer.LoadBalancer;
import com.shark.rpc.loadbalancer.LoadBalancerFactory;
import com.shark.rpc.model.RpcRequest;
import com.shark.rpc.model.RpcResponse;
import com.shark.rpc.model.ServiceMetaInfo;
import com.shark.rpc.protocol.common.ProtocolConstant;
import com.shark.rpc.protocol.common.ProtocolMessage;
import com.shark.rpc.protocol.messageEnum.ProtocolMessageSerializerEnum;
import com.shark.rpc.protocol.messageEnum.ProtocolMessageTypeEnum;
import com.shark.rpc.registry.Registry;
import com.shark.rpc.registry.RegistryFactory;
import com.shark.rpc.serializer.JdkSerializer;
import com.shark.rpc.serializer.Serializer;
import com.shark.rpc.serializer.SerializerFactory;
import com.shark.rpc.server.tcpServer.NettyTcpClient;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * jdk动态代理  消费者获取service实现类
 */
public class ServiceProxy implements InvocationHandler {
    /**
     * 调用代理
     *
     * @param proxy  the proxy instance that the method was invoked on
     * @param method the {@code Method} instance corresponding to
     *               the interface method invoked on the proxy instance.  The declaring
     *               class of the {@code Method} object will be the interface that
     *               the method was declared in, which may be a superinterface of the
     *               proxy interface that the proxy class inherits the method through.
     * @param args   an array of objects containing the values of the
     *               arguments passed in the method invocation on the proxy instance,
     *               or {@code null} if interface method takes no arguments.
     *               Arguments of primitive types are wrapped in instances of the
     *               appropriate primitive wrapper class, such as
     *               {@code java.lang.Integer} or {@code java.lang.Boolean}.
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //序列化器
        Serializer serializer = SerializerFactory.getInstance(RpcApp.getRpcConfig().getSerializer());
        //构造请求
        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();

        try {
            // 序列化
            byte[] bodyBytes = serializer.serialize(rpcRequest);
            // 发送请求
            //从注册中心获取服务提供地址
            RpcConfig rpcConfig = RpcApp.getRpcConfig();
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if (CollUtil.isEmpty(serviceMetaInfoList)) {
                throw new RuntimeException("暂无服务地址");
            }
            //负载均衡
            LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(rpcConfig.getLoadBalancer());
            //调用方法名为 负载均衡参数
            HashMap<String, Object> requestParams = new HashMap<>();
            requestParams.put("methodName", rpcRequest.getMethodName());
            ServiceMetaInfo selected = loadBalancer.select(requestParams, serviceMetaInfoList);

            //发送 使用重试机制
            RetryStrategy retryStrategy = RetryStrategyFactory.getInstance(RpcApp.getRpcConfig().getRetryStrategy());
            RpcResponse rpcResponse = retryStrategy.doRetry(() -> {
                //发送http请求并得到响应结果
                try (HttpResponse httpResponse = HttpRequest.post(selected.getServiceAddress())
                        .body(bodyBytes)
                        .execute()) {
                    byte[] result = httpResponse.bodyBytes();
                    // 反序列化
                    RpcResponse response = serializer.deserialize(result, RpcResponse.class);
                    return response;
                }catch (Exception e){
                    throw new Exception("调用失败...");
                }
            });
            return rpcResponse.getData();

//            //发送请求 自定义格式 tcp传输
//            // 构造消息
//            ProtocolMessage<RpcRequest> protocolMessage = new ProtocolMessage<>();
//            ProtocolMessage.Header header = new ProtocolMessage.Header();
//            header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
//            header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
//            header.setSerializer((byte) ProtocolMessageSerializerEnum.getEnumByValue(RpcApp.getRpcConfig().getSerializer()).getKey());
//            header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
//            // 生成全局请求 ID
//            header.setRequestId(IdUtil.getSnowflakeNextId());
//            protocolMessage.setHeader(header);
//            protocolMessage.setBody(rpcRequest);
//
//            // 创建客户端并发送请求
//            NettyTcpClient tcpClient = new NettyTcpClient(serviceMetaInfo1.getServiceHost(), serviceMetaInfo1.getServicePort());
//            tcpClient.start();
//            RpcResponse rpcResponse = (RpcResponse)tcpClient.sendRequest(protocolMessage);
//            return rpcResponse.getData();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
