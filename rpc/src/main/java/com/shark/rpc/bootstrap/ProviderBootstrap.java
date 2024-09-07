package com.shark.rpc.bootstrap;

import com.shark.rpc.RpcApp;
import com.shark.rpc.config.RegistryConfig;
import com.shark.rpc.config.RpcConfig;
import com.shark.rpc.model.ServiceMetaInfo;
import com.shark.rpc.model.ServiceRegistryInfo;
import com.shark.rpc.registry.LocalRegistry;
import com.shark.rpc.registry.Registry;
import com.shark.rpc.registry.RegistryFactory;
import com.shark.rpc.server.httpServer.NettyHttpServer;

import java.util.List;

/**
 * 服务提供者 快捷
 */
public class ProviderBootstrap {

    /**
     * 启动初始化
     * @param serviceRegistryInfoList 服务注册信息集合
     */
    public static void init(List<ServiceRegistryInfo> serviceRegistryInfoList){
        RpcApp.init();//初始化配置

        final RpcConfig rpcConfig = RpcApp.getRpcConfig();

        //注册服务
        for (ServiceRegistryInfo<?> serviceRegistryInfo :serviceRegistryInfoList){
            String serviceName = serviceRegistryInfo.getServiceName();

            LocalRegistry.register(serviceName, serviceRegistryInfo.getImplClass());//本地注册

            //注册到服务中心
            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
            serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
            try {
                registry.register(serviceMetaInfo);
            }catch (Exception e){
                throw new RuntimeException(serviceName + "----此服务注册失败",e);
            }
        }

        //启动服务
        NettyHttpServer httpServer = new NettyHttpServer();
        httpServer.run(RpcApp.getRpcConfig().getServerPort());

    }

}
