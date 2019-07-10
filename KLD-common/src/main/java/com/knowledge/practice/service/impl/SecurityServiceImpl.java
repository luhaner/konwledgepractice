package com.knowledge.practice.service.impl;

import com.knowledge.practice.common.util.AesUtil;
import com.knowledge.practice.common.util.RsaUtil;
import com.knowledge.practice.common.util.StringUtils;
import com.knowledge.practice.dao.mapper.KldRasKeysMapper;
import com.knowledge.practice.exception.BizException;
import com.knowledge.practice.model.RsaKeys;
import com.knowledge.practice.service.ISecurityService;
import com.knowledge.practice.vo.AesKeyVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 加解密业务
 * @since
 */
@Service
public class SecurityServiceImpl implements ISecurityService {

    private static final int AES_KEY_LENGTH = 32;
    private Logger logger = LoggerFactory.getLogger(SecurityServiceImpl.class);

    @Autowired
    private KldRasKeysMapper kldRASKeysMapper;

    @Override
    public String getRSAPublicKey(String device, String source, boolean generate) throws BizException {
        if (StringUtils.isNull(device) || StringUtils.isNull(source)) {
            return null;
        }
        String rsaKey = null;
        RsaKeys kldRASKeys = findKldRASKeysByDevice(device, source);
        if (kldRASKeys == null) {
            if (generate) {
                // 生成RSA秘钥
                try {
                    Map<String, String> keyMap = RsaUtil.genKey();
                    String publicKey = keyMap.get(RsaUtil.PUBLIC_KEY);
                    String privateKey = keyMap.get(RsaUtil.PRIVATE_KEY);
                    logger.info("publicKey=[" + publicKey + "] , privateKey=[" + privateKey + "]");
                    Date now = new Date();
                    RsaKeys keys = new RsaKeys();
                    keys.setCreateTime(now);
                    keys.setDevice(device);
                    keys.setRsaPriv(privateKey);
                    keys.setRsaPub(publicKey);
                    keys.setSource(source);
                    keys.setLastUpdateTime(now);
                    kldRASKeysMapper.insertSelective(keys);

                    rsaKey = publicKey.replaceAll("\n", "");
                } catch (Exception e) {
                    logger.error("生成RSA秘钥对失败", e);
                    throw new BizException("生成RSA秘钥对失败", e);
                }
            }
        } else {
            rsaKey = kldRASKeys.getRsaPub().replaceAll("-----BEGIN PUBLIC KEY-----", "").replaceAll("-----END PUBLIC KEY-----", "").replaceAll("\n", "");
        }
        return rsaKey;
    }

    /**
     * 根据设备ID和来源获取公钥
     *
     * @param device
     *        设备ID
     * @param source
     *        来源
     * @return
     */
    private RsaKeys findKldRASKeysByDevice(String device, String source) {
        List<RsaKeys> list = kldRASKeysMapper.findKldRsaKeysByDevice(device, source);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public String getRSAPrivateKey(String device, String source) {
        RsaKeys kldRASKeys = findKldRASKeysByDevice(device, source);
        if (kldRASKeys != null) {
            return kldRASKeys.getRsaPriv().replaceAll("-----BEGIN PRIVATE KEY-----", "").replaceAll("-----END PRIVATE KEY-----", "").replaceAll("\n", "");
        }
        return null;
    }

    @Override
    public String getRSADecryptData(String device, String source, String data) {

        String rsaKey = getRSAPrivateKey(device, source);

        if (rsaKey == null) {
            return null;
        }
        try {
            return RsaUtil.decryptByPriKey(rsaKey, data);
        } catch (Exception e) {
            logger.error("RSA参数解码异常:[{}]", e.getMessage());
        }
        return null;
    }

    @Override
    public AesKeyVO getEncryptAesKey(String source, String device) {
        try {
            String aesKey = createAesKey();
            String rsaPriKey = getRSAPrivateKey(device, source);
            if (StringUtils.isNull(aesKey) || StringUtils.isNull(rsaPriKey)) {
                logger.warn("AES秘钥[{}]或RSA私钥[{}]为NULL。", aesKey, rsaPriKey);
                return null;
            }
            String encryptAesKey = RsaUtil.encryptByPriKey(rsaPriKey, aesKey);
            return new AesKeyVO(aesKey, encryptAesKey);
        } catch (Exception e) {
            logger.error("生成AES秘钥异常", e);
        }
        return null;
    }

    /**
     * 生成随机aes Key
     */
    private String createAesKey() {
        String str = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        char[] arr = str.toCharArray();
        StringBuilder key = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < AES_KEY_LENGTH; i++) {
            int index = random.nextInt(arr.length);
            key.append(String.valueOf(arr[index]));
        }
        return key.toString();
    }

    @Override
    public String getAESDecryptData(String data, String aesKey, String iv) {
        try {
            return AesUtil.decrypt(data, aesKey, iv);
        } catch (Exception e) {
            logger.error("AES秘钥解码异常", e);
        }
        return null;
    }
}
