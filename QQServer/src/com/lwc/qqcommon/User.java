package com.lwc.qqcommon;

import java.io.Serializable;

/**
 * @author 刘文长
 * @version 1.0
 * 表示一个用户信息
 */
public class User implements Serializable {


    private static final long serialVersionUID = 1L;
    //用户名
    private String userId;
    //用户密码
    private String password;

    public User(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
