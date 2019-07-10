package com.knowledge.practice.dto;

/**
 * 登录请求参数
 */
public class LoginDTO extends BasicDTO {

    private String mobile;
    
    private String password;
    
    private String longitude;
    
    private String latitude;
    
    private String verify;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    
    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }

    @Override
    public String toString() {
        return "LoginDTO [mobile=" + mobile + ", password=" + password + ", longitude=" + longitude + ", latitude=" + latitude + "]";
    }
    
    
}
