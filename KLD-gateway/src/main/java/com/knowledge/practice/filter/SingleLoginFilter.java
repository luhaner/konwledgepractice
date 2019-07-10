package com.knowledge.practice.filter;

import com.knowledge.practice.common.constant.ErrorCodeEnum;
import com.knowledge.practice.common.util.GsonUtil;
import com.knowledge.practice.common.util.StringUtils;
import com.knowledge.practice.service.MemCacheUtil;
import com.knowledge.practice.vo.LoginUserVO;
import com.knowledge.practice.vo.ResultVO;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import java.util.Map;

import static com.knowledge.practice.controller.AbstractController.EXPIRED_DEVICE_PRE;
import static com.knowledge.practice.controller.AbstractController.SESSION_LOGINUSER;

/**
 * 单点登录filter
 */
@Component
public class SingleLoginFilter extends ZuulFilter {

    private static Logger logger = LoggerFactory.getLogger(SingleLoginFilter.class);

    @Autowired
    private MemCacheUtil memCacheUtil;


    @Override
    public String filterType() {
        // 前置过滤器
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        // 优先级，数字越大，优先级越低
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        // 获取请求上下文
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        // 获取请求url
        String url = getRequestURI(request);
        // 获取token
        String token = request.getHeader("token");
        String device = request.getHeader("device");
        // token为空
        if (StringUtils.isNull(token)) {
            // 直接返回token失效
            responseNotLoginError(requestContext, device, token);
            return null;
        }
        // token正在失效期
        String expiredTime = StringUtils.valueOf(memCacheUtil.get(EXPIRED_DEVICE_PRE + token));
        if (StringUtils.isNotNull(expiredTime)) {
            logger.warn("账号在其他设备登录，被强制下线！ token=【{}】 下线时间：{}", token, expiredTime);
            // 账号在其他设备上登录
            responseDeviceExpiredError(requestContext, device, token);
            return null;
        }
        // token无效
        Map<String, String> map = GsonUtil.gsonToMaps(String.valueOf(memCacheUtil.get(token)));
        if (map == null) {
            // 直接返回token失效
            responseNotLoginError(requestContext, device, token);
            return null;
        }
        String json = GsonUtil.gsonString(map.get(SESSION_LOGINUSER));
        LoginUserVO vo = GsonUtil.gsonToBean(json, LoginUserVO.class);
        if (vo == null) {
            // 直接返回token失效
            responseNotLoginError(requestContext, device, token);
            return null;
        }
        return null;
    }

    private void responseNotLoginError(RequestContext ctx, String device, String token) {
        ResultVO result = new ResultVO<>(ErrorCodeEnum.NOT_LOGIN);
        String json = GsonUtil.gsonString(result);
        logger.warn("用户token失效输出:{} device:{} token:{}", json, device, token);
        responseMessage(ctx, json);
    }

    private void responseDeviceExpiredError(RequestContext ctx, String device, String token) {
        ResultVO result = new ResultVO<>(ErrorCodeEnum.DEVICE_EXPIRED);
        String json = GsonUtil.gsonString(result);
        logger.warn("设备被下线输出:{} device:{} token:{}", json, device, token);
        responseMessage(ctx, json);
    }

    private void responseMessage(RequestContext ctx, String json) {
        ctx.setSendZuulResponse(false);
        ctx.setResponseStatusCode(200);
        ctx.set("isSuccess", false);
        ctx.getResponse().setContentType("text/html;charset=UTF-8");
        ctx.setResponseBody(json);
    }

    private String getRequestURI(HttpServletRequest request) {
        // 请求的url，即为上下文目录结束位置起始，至结束
        return request.getRequestURI().substring(request.getContextPath().length());
    }
}
