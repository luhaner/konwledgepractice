package com.knowledge.practice.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.knowledge.practice.common.constant.ErrorCodeEnum;

/**
 * 接口统一返回结构
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultVO<T> {

    private String retCode;

    private String retMsg;

    private T data;

    public ResultVO() {
        super();
    }

    public ResultVO(ErrorCodeEnum errorCodeEnum) {
        this.retCode = errorCodeEnum.getRetCode();
        this.retMsg = errorCodeEnum.getRetMsg();
    }

    public ResultVO(String retCode, String retMsg) {
        super();
        this.retCode = retCode;
        this.retMsg = retMsg;
    }

    public ResultVO(String retCode, String retMsg, T data) {
        super();
        this.retCode = retCode;
        this.retMsg = retMsg;
        this.data = data;
    }

    public static <T> ResultVO<T> success(T data) {
        return new ResultVO<T>(ErrorCodeEnum.SUCCESS.getRetCode(), ErrorCodeEnum.SUCCESS.getRetMsg(), data);
    }

    public static <T> ResultVO<T> success() {
        return new ResultVO<T>(ErrorCodeEnum.SUCCESS.getRetCode(), ErrorCodeEnum.SUCCESS.getRetMsg());
    }

    public static <T> ResultVO<T> error(String retCode, String retMsg) {
        return new ResultVO<T>(retCode, retMsg);
    }

    public static <T> ResultVO<T> error(ErrorCodeEnum errorCodeEnum) {
        return new ResultVO<T>(errorCodeEnum.getRetCode(), errorCodeEnum.getRetMsg());
    }

    public static <T> ResultVO<T> error() {
        return new ResultVO<T>(ErrorCodeEnum.SYSTEM_EXCEPTION.getRetCode(), ErrorCodeEnum.SYSTEM_EXCEPTION.getRetMsg());
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultVO{" + "retCode='" + retCode + '\'' + ", retMsg='" + retMsg + '\'' + ", data=" + data + '}';
    }
}
