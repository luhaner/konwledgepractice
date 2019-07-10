package com.knowledge.practice.vo;
/**
 * AES秘钥
 */
public class AesKeyVO {

    private String aesKey;
    
    private String encryptAesKey;
    
    public AesKeyVO(String aesKey, String encryptAesKey) {
        super();
        this.aesKey = aesKey;
        this.encryptAesKey = encryptAesKey;
    }

    public AesKeyVO() {
        super();
    }

    public String getAesKey() {
        return aesKey;
    }

    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
    }

    public String getEncryptAesKey() {
        return encryptAesKey;
    }

    public void setEncryptAesKey(String encryptAesKey) {
        this.encryptAesKey = encryptAesKey;
    }

    @Override
    public String toString() {
        return "AesKeyVO [aesKey=" + aesKey + ", encryptAesKey=" + encryptAesKey + "]";
    }
    
}
