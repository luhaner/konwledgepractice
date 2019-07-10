package com.knowledge.practice.dto;

/**
 * DTO的基类
 */
public class BasicDTO {

    private String token;
    private String version;
    private String source;
    private String device;
    private String aversion;
    private String ip;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getAversion() {
        return aversion;
    }

    public void setAversion(String aversion) {
        this.aversion = aversion;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "BasicDTO [token=" + token + ", version=" + version + ", source=" + source + ", device=" + device + ", aversion=" + aversion + "]";
    }

}
