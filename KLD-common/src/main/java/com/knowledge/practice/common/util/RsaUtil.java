package com.knowledge.practice.common.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA 工具类。提供加密，解密，生成密钥对等方法。
 */
public class RsaUtil {

    public static final String PUBLIC_KEY = "pubKey";
    public static final String PRIVATE_KEY = "priKey";
    /**
     * 加密算法
     */
    private static final String CIPHER_DE = "RSA";
    /**
     * 解密算法
     */
    private static final String CIPHER_EN = "RSA";
    /**
     * 密钥长度
     */
    private static final Integer KEY_LENGTH = 1024;

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;
    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    public static PublicKey getPublicKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = (new Base64()).decode(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    public static PrivateKey getPrivateKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = (new Base64()).decode(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    /**
     * * 用公钥加密 *
     * 
     * @param key
     *        加密的密钥 *
     * @param data
     *        待加密的明文数据 *
     * @return 加密后的数据 *
     * @throws Exception
     */
    public static String encryptByPubKey(String pubKey, String content) throws Exception {
        try {
            PublicKey publicKey = getPublicKey(pubKey);
            // 加密数据，分段加密
            Cipher cipher = Cipher.getInstance(CIPHER_EN);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] data = content.getBytes();
            int inputLength = data.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] encryptedData;
            try {
                int offset = 0;
                byte[] cache;
                int i = 0;
                while (inputLength - offset > 0) {
                    if (inputLength - offset > MAX_ENCRYPT_BLOCK) {
                        cache = cipher.doFinal(data, offset, MAX_ENCRYPT_BLOCK);
                    } else {
                        cache = cipher.doFinal(data, offset, inputLength - offset);
                    }
                    out.write(cache, 0, cache.length);
                    i++;
                    offset = i * MAX_ENCRYPT_BLOCK;
                }
                encryptedData = out.toByteArray();
            } catch (Exception e) {
                throw e;
            } finally {
                if (out != null) {
                    out.close();
                }
            }
            return Base64.encodeBase64String(encryptedData);
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e);
        }
    }

    /**
     * * 用私钥解密 *
     * 
     * @param key
     *        解密的密钥 *
     * @param raw
     *        已经加密的数据 *
     * @return 解密后的明文 *
     * @throws Exception
     */
    public static String decryptByPriKey(String priKey, String base64Datas) throws Exception {
        PrivateKey privateKey = getPrivateKey(priKey);
        // 解密数据，分段解密
        Cipher cipher = Cipher.getInstance(CIPHER_DE);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] data = Base64.decodeBase64(base64Datas);
        int inputLength = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] decryptedData;
        try {
            int offset = 0;
            byte[] cache;
            int i = 0;
            while (inputLength - offset > 0) {
                if (inputLength - offset > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offset, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offset, inputLength - offset);
                }
                out.write(cache);
                i++;
                offset = i * MAX_DECRYPT_BLOCK;
            }
            decryptedData = out.toByteArray();
        } catch (Exception e) {
            throw e;
        } finally {
            if (out != null) {
                out.close();
            }
        }
        return new String(decryptedData);
    }

    /**
     * 用公钥解密
     * 
     * @param rsaPubKey
     *        公钥
     * @param content
     *        待解码的字符
     * @return
     * @throws Exception
     */
    public static String decryptByPubKey(String rsaPubKey, String content) throws Exception {
        PublicKey publicKey = getPublicKey(rsaPubKey);
        // 解密数据，分段解密
        Cipher cipher = Cipher.getInstance(CIPHER_DE);
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] data = Base64.decodeBase64(content);
        int inputLength = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] decryptedData;
        try {
            int offset = 0;
            byte[] cache;
            int i = 0;
            while (inputLength - offset > 0) {
                if (inputLength - offset > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offset, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offset, inputLength - offset);
                }
                out.write(cache);
                i++;
                offset = i * MAX_DECRYPT_BLOCK;
            }
            decryptedData = out.toByteArray();
        } catch (Exception e) {
            throw e;
        } finally {
            if (out != null) {
                out.close();
            }
        }
        return new String(decryptedData);
    }

    public static Map<String, String> genKey() throws Exception {

        Map<String, String> keyMap = new HashMap<String, String>();
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA", new org.bouncycastle.jce.provider.BouncyCastleProvider());
        keyPairGen.initialize(KEY_LENGTH, new SecureRandom());
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey pubKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey priKey = (RSAPrivateKey) keyPair.getPrivate();

        byte[] pubKeyBytes = pubKey.getEncoded();
        byte[] priKeyBytes = priKey.getEncoded();
        String pubKeyStr = Base64.encodeBase64String(pubKeyBytes);
        String priKeyStr = Base64.encodeBase64String(priKeyBytes);

        keyMap.put(PUBLIC_KEY, pubKeyStr);
        keyMap.put(PRIVATE_KEY, priKeyStr);

        return keyMap;
    }

    /**
     * 用私钥加密
     * 
     * @param priKey
     *        私钥
     * @param content
     *        内容
     * @return
     * @throws Exception
     */
    public static String encryptByPriKey(String priKey, String content) throws Exception {
        try {
            PrivateKey privateKey = getPrivateKey(priKey);
            // 加密数据，分段加密
            Cipher cipher = Cipher.getInstance(CIPHER_EN);
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            byte[] data = content.getBytes();
            int inputLength = data.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] encryptedData;
            try {
                int offset = 0;
                byte[] cache;
                int i = 0;
                while (inputLength - offset > 0) {
                    if (inputLength - offset > MAX_ENCRYPT_BLOCK) {
                        cache = cipher.doFinal(data, offset, MAX_ENCRYPT_BLOCK);
                    } else {
                        cache = cipher.doFinal(data, offset, inputLength - offset);
                    }
                    out.write(cache, 0, cache.length);
                    i++;
                    offset = i * MAX_ENCRYPT_BLOCK;
                }
                encryptedData = out.toByteArray();
            } catch (Exception e) {
                throw e;
            } finally {
                if (out != null) {
                    out.close();
                }
            }
            return Base64.encodeBase64String(encryptedData);
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e);
        }
    }

    public static void main(String[] args) throws Exception {
        Map<String, String> keyMap = genKey();
        String pubKey = keyMap.get(PUBLIC_KEY);
        String priKey = keyMap.get(PRIVATE_KEY);
        System.out.println("pubKey=" + pubKey);
        System.out.println("priKey=" + priKey);

        String aaa = "{\"mobile\":\"15814415470\",\"password\":\"o123456\", \"verify\":\"123456\"}";
        String no = "QfGyOpN5Mq3Te2EFeflwhmBECvaAtizfX4oIK0g+iP4p7licncUF4rqXshbuGvSGAaS5ZH4PuGuu0G9nZFhIIl+4yEdgNYOx5oRYfzO0amfqRbuILjL0V+CGLZrK+JBy48NKAodZRLiEuLwmGcnJje/qmy/SSYHu2cbg/GLXnYc=";
        priKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALIGhNbhdpchT3jZzPRXD2/iEyDCrwc/5KET7kXna6JMYLit3csCG8a/t8UVzJfFaFvbWg+zFUQ4nJt2oRSG/b1WIw+t9mZpx2fSJIJ83vDCN0elRZQS+4zTUEgbx1VGGsWpT8mR6z5EPaAwrhyKjys0dhLqQyo3SkRX2wUAv2hjAgMBAAECgYAKSBgZ9PGNbu4RE7sjjXelxY/HP5cuOwFwUB9dyCzONc0MPXZmyBtkkiQB7O1hApSxYWzFJVmgFAjvbZ2F+vnigab5CgABk/twtdzCTScgPd+2Fzlh0xELLUOE3a8xerRadx6Er7L49kh0PA4kXobMi3jofkwdoCKbYnSLX5+J+QJBAPBUS6UKk4zgiyosKXA2D1pwTwSQu3Y2Ct7q8qctNbZ3X9zlwPVUwkGEuMWH1Sfug9bGDhbNT8l0JUksvFFd0lUCQQC9ojcEUaS8gNIm9Shs8Y/WS/HeW6G6z4BPhXOsvESh1/BZ+ehEahIk2ZrA0W9ypVDXiTLN34FExBhbc6fWNbfXAkEAwJwOdBth3cooPohePZxV+bmA2rvUQg1V8QqDpxBIuBA8OCG63+QqO7BPFaCjaPlzmDHrIMMzatU2/MppsxxqcQJBAJKHVZuwumot4DTaGnKT9WDRLgNzKnT/AVZw+ADxONcV1FKyGBdFHt/6hOmY61TbCKyDBw6tNWW9YFTJ+VY8C+ECQCIJjo7zB//SYGJtwugFZmRSlRISS7V9x8bMwcSG2syG17l7kWpcSrMrIIYwsLsv3iW5l9kII4kgDBuYnrTgIew=";
        pubKey ="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDhP2GjGWTu8gNYrOWRSlg3EDb15L+y0SVeZ7EMM9MR/9lqvYgSJXZItwNa5eyfH8zktDQGK3FJzZGwVL+5aFtGqGWpW8sZ4uahTvtd/uCT/IQFiReL3u7XTbrRHDZKBXNUNU3EbAkeqYTnpr/0zW4IMsDcYRUWvMc8s6We6ryFHwIDAQAB";

//        String encodeStr = encryptByPubKey(pubKey, aaa);
        String idno = decryptByPriKey(priKey, no);
        System.out.println(idno);
        
        System.out.println(encryptByPubKey("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCyBoTW4XaXIU942cz0Vw9v4hMgwq8HP+ShE+5F52uiTGC4rd3LAhvGv7fFFcyXxWhb21oPsxVEOJybdqEUhv29ViMPrfZmacdn0iSCfN7wwjdHpUWUEvuM01BIG8dVRhrFqU/Jkes+RD2gMK4cio8rNHYS6kMqN0pEV9sFAL9oYwIDAQAB", "120225198612035426"));

    }

}
