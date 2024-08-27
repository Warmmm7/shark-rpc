package com.shark.rpc;


import com.shark.rpc.config.RpcConfig;
import com.shark.rpc.constant.RpcConstant;
import com.shark.rpc.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * rpc框架
 */
@Slf4j
public class RpcApp {
    //双重锁单例加载
    private static volatile RpcConfig rpcConfig;

    /**
     * 初始化框架
     * @param newRpcConfig
     */
    public  static void  init(RpcConfig newRpcConfig){
        rpcConfig = newRpcConfig;
        log.info("rpc init,config = {}",newRpcConfig.toString());
    }

    /**
     * 初始化配置
     */
    public static void init(){
        RpcConfig newRpcConfig;
        try {
            newRpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        } catch (Exception e) {
            e.printStackTrace();
            //配置加载失败 使用默认配置
            newRpcConfig = new RpcConfig();
        }
        init(newRpcConfig);
    }

    public static RpcConfig getRpcConfig(){
        if (rpcConfig == null){
            synchronized (RpcApp.class){
                if (rpcConfig == null){
                    init();
                }
            }
        }
        return rpcConfig;
    }
}
