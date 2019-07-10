package com.knowledge.practice.common.constant;

/**
 * 基础异常码
 */
public enum ErrorCodeEnum {
    /**
     * 请求成功:000000
     */
    SUCCESS("000000", "请求成功！"),
    /**
     * 设备ID不能为空:000001
     */
    DEVICE_ID_NOT_NULL("000001", "设备ID不能为空"),
    /**
     * 参数有误
     */
    PARAMS_ERROR("900003", "参数有误！"),
    /**
     * 请您登录后再尝试
     */
    NOT_LOGIN("800001", "请您登录后再尝试！"),

    /**
     * 在其他账户登录
     */
    DEVICE_EXPIRED("111005", "您的账户已在其他设备登录"),
    /**
     * 系统异常，请稍后重试
     */
    SYSTEM_EXCEPTION("111111", "系统异常，请稍后重试");

    private String retCode;

    private String retMsg;

    ErrorCodeEnum(String retCode, String retMsg) {
        this.retCode = retCode;
        this.retMsg = retMsg;
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }

}
