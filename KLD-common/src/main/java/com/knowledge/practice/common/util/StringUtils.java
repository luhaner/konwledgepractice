package com.knowledge.practice.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 */
public class StringUtils {

    private static final int GENDER_FLAG = 2;

    private static final String VERSION_PATTERN = "\\d+(\\.\\d+)*";

    private static Logger logger = LoggerFactory.getLogger(StringUtils.class);

    private static final int PRIVACY_NAME_MIN_LENGTH = 6;
    private static final int CARDNO_MIN_LENGTH = 5;
    private static final int PRIVACY_NAME_LEFT = 0;
    private static final int PRIVACY_NAME_MIDDLE = 1;
    private static final int PRIVACY_NAME_RIGHT = 2;
    private static final String UNKNOWN = "unknown";
    private static final String IP_SPLIT = ",";
    private static final int IP_ADDR_MAX_LENGTH = 15;
    private static final String LOCAL_0_0_0_0_0_0_0_1 = "0:0:0:0:0:0:0:1";
    private static final String LOCAL_127_0_0_1 = "127.0.0.1";
    private static final String NULL = "null";


    private StringUtils() {
    }

    public static String valueOf(Object obj) {
        return (null == obj) ? null : obj.toString();
    }

    public static boolean isNotNull(String str) {
        if (str != null && str.trim().length() > 0 && !NULL.equals(str)) {
            return true;
        }
        return false;
    }

    public static boolean isNull(String str) {
        return !isNotNull(str);
    }

    /**
     * 脱敏处理文字 如：138*******25 张** **张
     * 
     * @param type
     *        0 隐藏左边 1 隐藏中间 2 隐藏右边
     * @param data
     *        要脱敏的字符串
     * @throws Exception
     */
    public static String privacyName(String data, int type) {
        if (StringUtils.isNull(data)) {
            return "";
        }
        if (type < PRIVACY_NAME_LEFT || type > PRIVACY_NAME_RIGHT) {
            type = PRIVACY_NAME_MIDDLE;
        }
        String reparten = "***************************************************";
        switch (type) {
            case 2:
                // 替换参数
                return data.charAt(0) + reparten.substring(0, data.length() - 1);
            case 0:
                // 替换参数
                return reparten.substring(0, data.length() - 1) + data.charAt(data.length() - 1);
            case 1:
                String defaultStr = "******";
                if (data.length() < PRIVACY_NAME_MIN_LENGTH) {
                    return defaultStr;
                }
                return data.substring(0, 3) + defaultStr + data.substring(data.length() - 3, data.length() - 1);
            default:
                return "";
        }

    }

    /**
     * 银行卡号隐藏
     */
    public static String cardHidden(String cardNo) {
        if (StringUtils.isNull(cardNo)) {
            return "";
        }
        if (cardNo.length() < CARDNO_MIN_LENGTH) {
            return cardNo;
        }
        return cardNo.substring(0, 4) + "**********" + cardNo.substring(cardNo.length() - 4);
    }

    /**
     * 获取当前网络ip
     * 
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (ipAddress.equals(LOCAL_127_0_0_1) || ipAddress.equals(LOCAL_0_0_0_0_0_0_0_1)) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                    ipAddress = inet.getHostAddress();
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > IP_ADDR_MAX_LENGTH) { // "***.***.***.***".length()
            // = 15
            if (ipAddress.indexOf(IP_SPLIT) > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(IP_SPLIT));
            }
        }
        return ipAddress;
    }

    /**
     * 生成指定长度的随机串
     */
    public static String getRandomCode(int length) {
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(random.nextInt(10));
        }
        return stringBuilder.toString();
    }

    /**
     * 格式化金额
     */
    public static String formatMoney(Double money) {
        if (money == null) {
            return "";
        }
        return new DecimalFormat("#.00").format(money);
    }

    /**
     * 比较版本大小
     * 
     * @param v1
     * @param v2
     * @return v1 > v2 return 1
     *         v1 == v2 return 0
     *         v1 < v2 return -1
     */
    public static int versionCompare(String v1, String v2) {
        Pattern pattern = Pattern.compile(VERSION_PATTERN);
        // 错误的版本号，统一作为最小版本
        if (StringUtils.isNull(v1) || !pattern.matcher(v1).matches()) {
            return -1;
        }
        if (StringUtils.isNull(v2) || !pattern.matcher(v2).matches()) {
            return 1;
        }
        String[] s1 = v1.split("\\.");
        String[] s2 = v2.split("\\.");

        int length = s1.length < s2.length ? s1.length : s2.length;

        for (int i = 0; i < length; i++) {
            int diff = Integer.parseInt(s1[i]) - Integer.parseInt(s2[i]);
            if (diff == 0) {
                continue;
            } else {
                return diff > 0 ? 1 : -1;
            }
        }

        return 0;
    }

    /**
     * 手机号脱敏
     *
     * @param phoneCip
     * @return
     */
    public static String phoneCipHidden(String phoneCip) {
        if (StringUtils.isNull(phoneCip)) {
            return "";
        }
        if (phoneCip.length() < CARDNO_MIN_LENGTH) {
            return phoneCip;
        }
        return phoneCip.substring(0, 3) + "****" + phoneCip.substring(phoneCip.length() - 4);
    }

    /**
     * 身份证脱敏
     *
     * @param identity
     * @return
     */
    public static String identityHidden(String identity) {
        if (StringUtils.isNull(identity)) {
            return "";
        }
        if (identity.length() < PRIVACY_NAME_MIN_LENGTH) {
            return identity;
        }
        return identity.substring(0, 6) + "********" + identity.substring(identity.length() - 4);
    }
    
    /**
     * 
     * 身份证脱敏
     * 
     * @param identity
     * @param preLen 前置显示位数
     * @param sufLen 后置显示位数
     * @return
     */
    public static String identityHidden(String identity, int preLen, int sufLen) {
        if (StringUtils.isNull(identity)) {
            return "";
        }
        if (identity.length() < PRIVACY_NAME_MIN_LENGTH) {
            return identity;
        }
        return identity.substring(0, preLen) + "********" + identity.substring(identity.length() - sufLen);
    }
    
    /**
     * 获取当前线程绑定的请求号
     */
    public static String getRequestSerialNo() {
        Object serialNoObj = ThreadLocalUtil.get("SerialNo");
        return null != serialNoObj ? serialNoObj.toString() : "";
    }

    public static String valueOf(String str, String defaultValue) {
        if (StringUtils.isNull(str) && StringUtils.isNotNull(defaultValue)) {
            return defaultValue;
        }
        return valueOf(str);
    }

    public static boolean checkNameIsLegal(String s) {
        if(isNull(s)) {
            return false;
        }
        String pattern = "[a-zA-Z0-9\u4e00-\u9fa5`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？/_-· ]+";
        return Pattern.matches(pattern, s);
    }
    
    public static boolean checkDoorIsLegal(String s) {
        if(isNull(s)) {
            return false;
        }
        String pattern = "[a-zA-Z0-9\u4e00-\u9fa5`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？/_\\-· ]+";
        return Pattern.matches(pattern, s);
    }
    
    /**
     * 根据身份编号获取生日
     *
     * @param idCard 身份编号
     * @return 生日(yyyyMMdd)
     */
    public static String getBirthByIdCard(String idCard) {
        return idCard.substring(6, 14);
    }
    
    /**
     * 根据身份编号获取性别
     *
     * @param idCard 身份编号
     * @return 性别(M-男，F-女，N-未知)
     */
    public static String getGenderByIdCard(String idCard) {
        String sGender = "N";

        String sCardNum = idCard.substring(16, 17);
        if (Integer.parseInt(sCardNum) % GENDER_FLAG != 0) {
            sGender = "M";//男
        } else {
            sGender = "F";//女
        }
        return sGender;
    }

    /**
     * 强制设值为空
     * 
     * @param value
     * @return
     */
    public static String forceSetNullWhenIsBlank(String value) {
        if (StringUtils.isNull(value)) {
            return null;
        }
        return value;
    }

    /**
     * 截取500固定长度
     * 
     * @param orignString
     * @return
     */
    public static String cutoutLength500(String orignString) {
        return cutoutLength(orignString, 500);
    }

    /**
     * 截取长度
     *
     * @param orignString
     * @param cutoutLength
     * @return
     */
    public static String cutoutLength(String orignString, int cutoutLength) {
        if (isNotNull(orignString) && orignString.length() > cutoutLength) {
            return orignString.substring(0, cutoutLength);
        }
        return orignString;
    }
}


