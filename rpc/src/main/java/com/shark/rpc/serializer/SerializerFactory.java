package com.shark.rpc.serializer;

import com.shark.rpc.spi.SpiLoader;

/**
 * 序列化工厂
 */
public class SerializerFactory {
    /**
     * 进行序列化映射
     */
    //private static final Map<String,Serializer> KEY_SERIALIZER_MAP = new HashMap<String,Serializer>();
    /*
    static {//静态代码块加载
        KEY_SERIALIZER_MAP.put(SerializerKeys.JDK,new JdkSerializer());
        KEY_SERIALIZER_MAP.put(SerializerKeys.JSON,new JsonSerializer());
        KEY_SERIALIZER_MAP.put(SerializerKeys.HESSIAN,new HessianSerializer());
        KEY_SERIALIZER_MAP.put(SerializerKeys.KRYO,new KryoSerializer());
    }
     */
    /**
     *
     * Spi加载
     */
    static {//工厂首次加载时 调用load方法加载序列化接口所有实现类 再用getInstance获取实现类对象
        SpiLoader.load(Serializer.class);
    }

    /**
     * 默认序列化器
     */
    private static final Serializer DEFAULT_SERIALIZER = new JdkSerializer();

    /**
     * 获取序列化器实列
     */
    public static Serializer getInstance(String key){
        return SpiLoader.getInstance(Serializer.class,key);
    }
}
