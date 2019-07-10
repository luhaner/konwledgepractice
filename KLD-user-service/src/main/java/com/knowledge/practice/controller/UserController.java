package com.knowledge.practice.controller;

import com.knowledge.practice.common.constant.ErrorCodeEnum;
import com.knowledge.practice.common.util.StringUtils;
import com.knowledge.practice.dto.LoginDTO;
import com.knowledge.practice.model.User;
import com.knowledge.practice.service.IUserService;
import com.knowledge.practice.vo.LoginUserVO;
import com.knowledge.practice.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户服务
 *
 */
@RestController
@RequestMapping("kld/user")
public class UserController extends AbstractController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private IUserService userService;

    /**
     * 登录
     */
    @RequestMapping(value = "login/toLogin", method = RequestMethod.POST)
    @ResponseBody
    public ResultVO login(@RequestBody LoginDTO loginDTO) {
        String ip = loginDTO.getIp();
        String source = loginDTO.getSource();
        String device = loginDTO.getDevice();
        String mobile = loginDTO.getMobile();
        String password = loginDTO.getPassword();
        if (StringUtils.isNull(mobile) || StringUtils.isNull(password)) {
            return new ResultVO<>(ErrorCodeEnum.PARAMS_ERROR);
        }
        // 登录逻辑
        try {
            User user = userService.login(mobile, password, ip, source);

            // 设置登陆状态
            LoginUserVO info = new LoginUserVO(StringUtils.valueOf(user.getId()), null, user.getMobile(), user.getMobile());
            // 获取token
            String token = loginDTO.getToken();
            super.setLoginUser(token, source, device, info);
            if (user != null) {
                return ResultVO.success();
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            logger.error("用户登录异常！mobile=[" + mobile + "]", e);
        }
        return new ResultVO<>(ErrorCodeEnum.SYSTEM_EXCEPTION);
    }
}
