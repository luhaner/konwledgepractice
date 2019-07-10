package com.knowledge.practice.controller;

import com.knowledge.practice.common.constant.SourceEnum;
import com.knowledge.practice.common.util.DateUtil;
import com.knowledge.practice.common.util.GsonUtil;
import com.knowledge.practice.common.util.StringUtils;
import com.knowledge.practice.service.ISecurityService;
import com.knowledge.practice.service.MemCacheUtil;
import com.knowledge.practice.vo.AesKeyVO;
import com.knowledge.practice.vo.LoginUserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * controller的父类
 */
@Controller
public class AbstractController {

    public static final String EXPIRED_DEVICE_PRE = "expired_device_";
    public static final String LOGIN_DEVICE_KEY_PRE = "login_device_";
    public static final String CURR_LOGIN_USERID_KEY_PRE = "curr_login_userid_";
    public static final String USERID_LOGIN_DEVICE_TYPE_KEY_PRE = "login_device_type_";
    private Logger logger = LoggerFactory.getLogger(AbstractController.class);
    public static final String SESSION_LOGINUSER = "SESSION_LOGINUSER";
    public static final String SESSION_ISCHECK = "SESSIONISCHECK";
    public static final String SESSION_CAPTCHA = "SESSIONCAPTCHA";
    public static final String SESSION_SMSCODE = "SESSIONSMSCODE";

    public static final String TOKEN = "token";
    public static final String VERSION = "version";
    public static final String SOURCE = "source";
    public static final String DEVICE = "device";
    public static final String AVERSION = "aversion";
    public static final String IP = "ip";
    public static final String CHANNEL = "channel";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String USER_IDENTIFY_ID = "useridentifyid";
    public static final String[] HEADER_PARAMS = new String[] { TOKEN, VERSION, SOURCE, DEVICE, AVERSION, IP, CHANNEL, ACCESS_TOKEN, USER_IDENTIFY_ID };

    @Autowired
    private ISecurityService securityService;
    @Autowired
    private MemCacheUtil memCacheUtil;

    public AesKeyVO getEncryptAesKey(String source, String device) {
        return securityService.getEncryptAesKey(source, device);
    }

    /**
     * 从http请求头获取token
     * 
     * @param request
     * @return
     */
    protected String getTokenFromHeader(HttpServletRequest request) {
        String token = request.getHeader(TOKEN);
        return token;
    }

    /**
     * 设置登陆状态
     * 
     * @param token
     * @param source
     * @param device
     * @param info 用户信息
     */
    public void setLoginUser(String token, String source, String device, LoginUserVO info) {
        // 业务需要修改成一个用户只能在一个设备上登录
        String userId = info.getUserId();
        if (SourceEnum.APP.contains(source)) {
            logger.info("账号在登录！ userId：【{}】 设备：【{}】", userId, device);
            // 非app登录 暂不处理
            String loginDeviceKey = LOGIN_DEVICE_KEY_PRE + userId;
            String loginDevice = StringUtils.valueOf(memCacheUtil.get(loginDeviceKey));
            int timeOut = getCacheTimeOutTime(source);
            if (StringUtils.isNotNull(loginDevice) && !loginDevice.equals(device)) {
                logger.info("账号在其他设备登录，需要强制下线原设备！ userId：【{}】 原设备：【{}】 新设备：【{}】", userId, loginDevice, device);
                // 判断原设备最新登录的是否还是当前的用户
                String lastUserId = StringUtils.valueOf(memCacheUtil.get(CURR_LOGIN_USERID_KEY_PRE + loginDevice));
                logger.info("原设备最新登录的用户 userId：【{}】 ", lastUserId);
                if (userId.equals(lastUserId)) {
                    // 原设备上还是登录的当前用户，需要去除掉原设备登录token
                    String oldToken = StringUtils.valueOf(memCacheUtil.get(loginDevice));
                    if (StringUtils.isNotNull(oldToken)) {
                        logger.info("旧设备token设置失效！ userId：【{}】 设备：【{}】 token:【{}】", userId, loginDevice, oldToken);
                        memCacheUtil.delete(oldToken);
                        String now = DateUtil.parseCurDateToStr(DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);
                        memCacheUtil.put(EXPIRED_DEVICE_PRE + oldToken, now, timeOut);
                        memCacheUtil.delete(loginDevice);
                    }
                }
            }
            // 保持和token的有效时间一致，可以比token的有效时间长
            memCacheUtil.put(loginDeviceKey, device, timeOut);
            memCacheUtil.put(device, token, timeOut);
            memCacheUtil.put(CURR_LOGIN_USERID_KEY_PRE + device, userId, timeOut);
            memCacheUtil.delete(EXPIRED_DEVICE_PRE + token);

            // 绑定设备
            memCacheUtil.put(USERID_LOGIN_DEVICE_TYPE_KEY_PRE + userId, source, timeOut);

        }
        setSessionValue(token, source, SESSION_LOGINUSER, info);

    }

    /**
     * 从memcache中获得登录用户数据
     * 
     * @param token
     */
    public LoginUserVO getLoginUser(String token) {
        if (StringUtils.isNull(token)) {
            return null;
        }
        String cacheValue = GsonUtil.gsonString(getSessionValue(token, SESSION_LOGINUSER));
        return GsonUtil.gsonToBean(cacheValue, LoginUserVO.class);
    }

    /**
     * 从memcache中取String类型的数据
     * 
     * @param token
     * @param key session的key
     * @return
     */
    public Object getSessionValue(String token, String key) {
        if (StringUtils.isNull(token)) {
            return null;
        }
        Map<String, Object> map = GsonUtil.gsonToMaps(String.valueOf(this.memCacheUtil.get(token)));
        if (map != null && map.containsKey(key)) {
            return map.get(key);
        } else {
            return null;
        }
    }

    /**
     * 设置memcache中的数据
     * 
     * @param token
     * @param source
     * @param key
     * @param value
     */
    public void setSessionValue(String token, String source, String key, Object value) {
        if (StringUtils.isNull(token)) {
            return;
        }
        Map<String, Object> map = GsonUtil.gsonToMaps(String.valueOf(this.memCacheUtil.get(token)));
        if (map == null) {
            map = new ConcurrentHashMap<>();
        }
        map.put(key, value);
        int timeOut = getCacheTimeOutTime(source);
        logger.info("cacheSession: token=[{}], timeOut=[{}]", token, timeOut);
        String json = GsonUtil.gsonString(map);
        memCacheUtil.put(token, json, timeOut);
    }

    private int getCacheTimeOutTime(String source) {
        // app：7天 web：30分钟
        int timeOut = 1800;
        if (SourceEnum.APP.contains(source)) {
            timeOut = 604800;
        }
        return timeOut;
    }

    /**
     * 从memcache中删除数据
     * 
     * @param token
     * @param key
     */
    public void removeSessionValue(String token, String source, String key) {
        if (StringUtils.isNull(token)) {
            return;
        }
        Map<String, Object> map = GsonUtil.gsonToMaps(String.valueOf(this.memCacheUtil.get(token)));
        if (map == null) {
            map = new ConcurrentHashMap<>();
        }
        map.remove(key);
        int timeOut = getCacheTimeOutTime(source);
        logger.info("cacheSession: token=[{}], timeOut=[{}]", token, timeOut);
        String json = GsonUtil.gsonString(map);
        memCacheUtil.put(token, json, timeOut);
    }

}
