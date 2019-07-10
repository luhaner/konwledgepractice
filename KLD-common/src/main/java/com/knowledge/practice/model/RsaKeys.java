package com.knowledge.practice.model;

import java.util.Date;

/**
 * RSA秘钥表
 * @since
 */
public class RsaKeys {
    private Integer id;

    private String device;

    private String source;

    private String rsaPub;

    private String rsaPriv;

    private Date createTime;

    private Date lastUpdateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device == null ? null : device.trim();
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source == null ? null : source.trim();
    }

    public String getRsaPub() {
        return rsaPub;
    }

    public void setRsaPub(String rsaPub) {
        this.rsaPub = rsaPub == null ? null : rsaPub.trim();
    }

    public String getRsaPriv() {
        return rsaPriv;
    }

    public void setRsaPriv(String rsaPriv) {
        this.rsaPriv = rsaPriv == null ? null : rsaPriv.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    
}
