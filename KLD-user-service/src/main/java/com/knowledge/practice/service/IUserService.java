package com.knowledge.practice.service;

import com.knowledge.practice.exception.BizException;
import com.knowledge.practice.model.User;

/**
 * 用户业务接口
 */
public interface IUserService {

    /**
     * 用户登录
     *
     * @param mobile   手机号
     * @param password 登录密码
     * @param ip       登录ip
     * @param source   来源
     * @return
     * @throws BizException
     */
    User login(String mobile, String password, String ip, String source) throws BizException;

}
