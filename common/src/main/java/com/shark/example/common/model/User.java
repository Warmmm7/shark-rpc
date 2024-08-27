package com.shark.example.common.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户实体类
 */
@Data
public class User implements Serializable {
    /**
     * 测试列实体类 用户
     */
    private String name;

}
