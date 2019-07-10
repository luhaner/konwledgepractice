package com.knowledge.practice.service.impl;

import com.knowledge.practice.dao.mapper.UserMapper;
import com.knowledge.practice.exception.BizException;
import com.knowledge.practice.model.User;
import com.knowledge.practice.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(String mobile, String password, String ip, String source) throws BizException {
        return userMapper.selectByMobile(mobile, password);
    }

}
