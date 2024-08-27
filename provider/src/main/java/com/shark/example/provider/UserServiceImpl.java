package com.shark.example.provider;

import com.shark.example.common.model.User;
import com.shark.example.common.service.UserService;

public class UserServiceImpl implements UserService {
    @Override
    public User getUser(User user) {
        System.out.println("用户名："+user.getName());
        return user;
    }
}
