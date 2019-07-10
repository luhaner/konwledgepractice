package com.knowledge.practice.exception;

/**
 * 业务异常类
 */
public class BizException extends Exception {

    private static final long serialVersionUID = 1L;
    private String retCode;
    
    public BizException() {
        super();
    }

    public BizException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public BizException(String arg0) {
        super(arg0);
    }
    
    public BizException(String retCode, String msg) {
        super(msg);
        this.retCode = retCode;
    }

    public String getRetCode() {
        return retCode;
    }
    
}
