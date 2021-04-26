package com.ecust.projectsubmit.adapter.entity;


/**
 * @author xuexiang
 * @since 2018/8/6 下午6:14
 */
public class LoginInfo {

    private User user;

    private String token;

    public User getUser() {
        return user;
    }

    public LoginInfo setUser(User user) {
        this.user = user;
        return this;
    }

    public String getToken() {
        return token;
    }

    public LoginInfo setToken(String token) {
        this.token = token;
        return this;
    }
}
