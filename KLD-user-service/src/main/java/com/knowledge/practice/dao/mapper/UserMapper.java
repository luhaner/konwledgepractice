package com.knowledge.practice.dao.mapper;

import com.knowledge.practice.model.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper {

    @Select("select id, mobile, password from user where mobile = #{mobile} and password = #{password} limit 1")
    User selectByMobile(@Param("mobile") String mobile, @Param("password") String password);
}
