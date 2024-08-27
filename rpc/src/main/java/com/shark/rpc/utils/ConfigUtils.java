package com.shark.rpc.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;

/**
 * 配置对象 工具类
 */
public class ConfigUtils {
    /**
     * 加载配置对象
     * @param tClass
     * @param prefix  前缀
     * @return
     * @param <T>
     */
    public static <T> T loadConfig(Class<T> tClass,String prefix){
        return loadConfig(tClass,prefix,"");
    }

    /**
     * 补充环境 加载配置对象
     * 可以支持application.properties 或者其他环境 application-prod.properties
     * @param tClass
     * @param prefix
     * @param environment
     * @return
     * @param <T>
     */
    public static <T> T loadConfig(Class<T> tClass,String prefix,String environment){
        StringBuilder configFileBuilder = new StringBuilder("application");
        if (StrUtil.isNotBlank(environment)){
            configFileBuilder.append("-").append(environment);
        }
        configFileBuilder.append(".properties");
        Props props = new Props(configFileBuilder.toString());
        return props.toBean(tClass,prefix);
    }
}
