package com.shark.example.consumer;

import com.shark.example.common.model.User;
import com.shark.example.common.service.UserService;
import com.shark.rpc.proxy.ServiceProxyFactory;

public class SimpleConsumerExample {
    public static void main(String[] args) {

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
