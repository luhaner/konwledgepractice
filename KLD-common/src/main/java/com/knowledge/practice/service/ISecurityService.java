package com.knowledge.practice.service;

import com.knowledge.practice.exception.BizException;
import com.knowledge.practice.vo.AesKeyVO;

/**
 * 加解密接口
 */
public interface ISecurityService {

    /**
     * 获取RSA 公钥
     * 
     * @param device
     *        设备号
     * @param source
     *        来源：ios || android
     * @param generate
     *        没有时是否生成
     * @return
     * @throws BizException
     */
    public String getRSAPublicKey(String device, String source, boolean generate) throws BizException;

    /**
     * 获取RSA 秘钥
     * 
     * @param device
     *        设备号
     * @param source
     *        来源：ios || android
     * @return
     */
    public String getRSAPrivateKey(String device, String source);

    /**
     * 获得RSA解码数据
     * 
     * @param device
     *        设备号
     * @param source
     *        来源：ios || android
     * @param data
     *        待解码数据
     * @return
     */
    public String getRSADecryptData(String device, String source, String data);

    /**
     * 获取AES 秘钥
     * 
     * @param source
     *        来源 ios || android
     * @param device
     *        设备号
     * @return
     */
    public AesKeyVO getEncryptAesKey(String source, String device);

    /**
     * 获取AES解码数据
     * 
     * @param data
     *        待解码数据
     * @param aesKey
     *        AES 秘钥
     * @param iv 
     *        秘钥向量
     * @return
     */
    public String getAESDecryptData(String data, String aesKey, String iv);

}
