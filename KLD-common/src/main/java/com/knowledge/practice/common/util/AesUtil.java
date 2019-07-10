package com.knowledge.practice.common.util;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

/**
 * AES加密工具类
 */
public class AesUtil {

    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";// 默认的加密算法
    private static final String AES_ECB_PKCS5_PADDING = "AES/ECB/PKCS5Padding";
    private static final String KEY_ALGORITHM = "AES";
    
    private static final int AES128ECB_KEY_LENGTH = 16;

    private static Logger logger = LoggerFactory.getLogger(AesUtil.class);

    
    /**
     * AES CBC加密操作
     *
     * @param content
     *        待加密内容
     * @param password
     *        加密密码
     * @return 返回Base64转码后的加密数据
     */
    public static String encrypt(String content, String password, String iv) {
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);// 创建密码器

            byte[] byteContent = content.getBytes("utf-8");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
            byte[] raw = password.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, KEY_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParameterSpec);// 初始化为加密模式的密码器

            byte[] result = cipher.doFinal(byteContent);// 加密

            return Base64.encodeBase64String(result);// 通过Base64转码返回
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        return null;
    }

    /**
     * AES CBC解密操作
     *
     * @param content
     * @param password
     * @param iv
     * @return
     */
    public static String decrypt(String content, String password, String iv) {

        try {
            // 实例化
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
            byte[] raw = password.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, KEY_ALGORITHM);
            // 使用密钥初始化，设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivParameterSpec);

            // 执行操作
            byte[] result = cipher.doFinal(Base64.decodeBase64(content));

            return new String(result, "utf-8");
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        return null;
    }

    /**
     * 使用aes-128-ecb解密
     * 
     * @param content
     * @param password
     * @return
     * @throws Exception
     */
    public static String decryptByAES128ECB(String content, String password) throws Exception {
        try {
            // 判断Key是否正确
            if (password == null || password.length() != AES128ECB_KEY_LENGTH) {
                logger.error("Key长度不是16位");
                return null;
            }
            byte[] raw = password.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(AES_ECB_PKCS5_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = new Base64().decode(content);// 先用base64解密
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original, "utf-8");
                return originalString;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                return null;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return null;
        }
    }

    /**
     * 使用aes-128-ecb加密
     * 
     * @param content
     * @param password
     * @return
     * @throws Exception
     */
    public static String encryptByAES128ECB(String content, String password) throws Exception {
        try {
            // 判断Key是否正确
            if (password == null || password.length() != AES128ECB_KEY_LENGTH) {
                logger.error("Key长度不是16位");
                return null;
            }
            byte[] raw = password.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(AES_ECB_PKCS5_PADDING);// "算法/模式/补码方式"
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(content.getBytes("utf-8"));
            return new Base64().encodeToString(encrypted);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return null;
        }
    }

    /**
     * 企数标普加密
     *
     * @param content 需要加密的内容
     * @param password  加密密码
     * @return
     */
    public static String qsbpEncrypt(String content, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG" );
            secureRandom.setSeed(password.getBytes("utf-8"));
            kgen.init(128, secureRandom);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");//创建密码器
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, key);//初始化
            byte[] result = cipher.doFinal(byteContent);
            return Base64.encodeBase64String(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 企数标普解密
     *
     * @param content  待解密内容
     * @param password 解密密钥
     * @return
     */
    public static String qsbpDecrypt(String content, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG" );
            secureRandom.setSeed(password.getBytes("utf-8"));
            kgen.init(128, secureRandom);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");//创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key);//初始化
            byte[] result = cipher.doFinal(Base64.decodeBase64(content));//base64解码
            return new String(result,"utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static void main(String[] args) throws Exception {
        String content = 
            "{\"access_token\":\"KLD_TOKEN1130730579393495637\",\"uId\":\"ltQyGvhELqZVDKV6KiDMvC84gJoUGBloZiMtku9jhzo=\"}";
        System.out.println(AesUtil.encryptByAES128ECB(content, "0102030405060708"));
        //120225198612035426 md5  :     fe9bbb88efe957c7062d4928b6844fef 
        String enctr = AesUtil.encrypt(content,"96fO4x+60dcYEm8p","0102030405060708");
        content = AesUtil.decrypt(enctr,"96fO4x+60dcYEm8p","0102030405060708");
        //String enctr = AesUtil.decryptByAES128ECB(content,"TgyaPMPQyAlRo6qg");
        // String enctr = AesUtil.decryptByAES128ECB(content,"TgyaPMPQyAlRo6qg");
        System.out.println(enctr);
        System.out.println(content);
    }
}
