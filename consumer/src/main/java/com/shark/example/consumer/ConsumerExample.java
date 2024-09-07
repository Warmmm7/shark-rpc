package com.shark.example.consumer;

import com.shark.example.common.model.User;
import com.shark.example.common.service.UserService;
import com.shark.rpc.bootstrap.ConsumerBootstrap;
import com.shark.rpc.config.RpcConfig;
import com.shark.rpc.proxy.ServiceProxyFactory;
import com.shark.rpc.utils.ConfigUtils;

/**
 * 消费者示例
 */
public class ConsumerExample {
    public static void main(String[] args) {

        ConsumerBootstrap.init();//初始化 获得配置

        //获取代理
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);

        User user = new User();
        user.setName("大鲨鱼");

        //调用
        User newUser = userService.getUser(user);

        if (newUser!= null){
            System.out.println(newUser.getName());
        }else {
            System.out.println("user == null");
        }
    }
}
