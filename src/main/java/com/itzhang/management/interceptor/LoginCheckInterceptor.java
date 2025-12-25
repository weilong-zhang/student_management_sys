package com.itzhang.management.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.itzhang.management.constant.JwtClaimsConstant;
import com.itzhang.management.entity.result.Result;
import com.itzhang.management.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //预检请求直接放行
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK); // 返回200状态码
            return true;
        }

        //获取url
        String url = request.getRequestURL().toString();
        log.info("获取到了url地址 {}", url);

        //判断url中是否包含login，如果包含，则放行
        if (url.contains("/user/login") || url.contains("/verification/code") || url.contains("/register/byEmail")) {
            log.info("登陆和注册操作，直接放行");
            return true;
        }

        //判断请求头是否有用户信息
        if (!StringUtils.hasLength(request.getHeader("X-User-Id")) || !StringUtils.hasLength(request.getHeader("X-User-Name"))) {
            log.info("请求头中缺少用户信息");
            Result error = Result.error("Not_User");
            String notLogin = JSONObject.toJSONString(error);
            response.getWriter().write(notLogin);
            return false;
        }

        //获取请求中的令牌token
        String jwt = request.getHeader("token");

        //判断令牌是否存在，如果不存在，返回错误结果
        if (!StringUtils.hasLength(jwt)) {
            log.info("请求头token为空，请求失败");
            Result error = Result.error("Not_Login");

            //手动转换为json格式，并返回错误内容
            String notLogin = JSONObject.toJSONString(error);
            response.getWriter().write(notLogin);
            return false;
        }

        //解析token，如果解析失败，返回错误结果
        try {
            log.info("解析token");
            Claims claims = JwtUtils.parseJWT(jwt);
            Long userId = Long.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());
            log.info("当前用户手机号:{}", userId);
        } catch (Exception e) {
            //解析失败
            log.error("令牌解析失败,{}", e);
            Result error = Result.error("Not_Login");

            //手动转换为json格式，并返回错误内容

            String notLogin = JSONObject.toJSONString(error);
            response.getWriter().write(notLogin);
            return false;
        }

        //放行
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("posthandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("afterCompletion");
    }
}
