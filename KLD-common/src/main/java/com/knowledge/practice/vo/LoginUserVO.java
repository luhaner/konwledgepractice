package com.knowledge.practice.vo;

import java.io.Serializable;

/**
 * 平台用户登录接口返回结构
 */
public class LoginUserVO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 平台ID(U开头)
     */
    private String userId;
    private String aes;
    private String phone;
    private String name;
    
    public LoginUserVO() {
        super();
    }
    
    public LoginUserVO(String userId, String aes, String phone, String name) {
        super();
        this.userId = userId;
        this.aes = aes;
        this.phone = phone;
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAes() {
        return aes;
    }

    public void setAes(String aes) {
        this.aes = aes;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "LoginUserVo [userId=" + userId + ", aes=" + aes + ", phone=" + phone + ", name=" + name + "]";
    }

}
