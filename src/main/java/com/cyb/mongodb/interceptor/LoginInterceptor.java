package com.cyb.mongodb.interceptor;

import com.cyb.mongodb.dto.ExpireException;
import com.cyb.mongodb.dto.UnloginException;
import com.cyb.mongodb.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //放行OPTIONS请求
        String method = request.getMethod();
        if ("OPTIONS".equals(method)) {
            return true;
        }

        //获取请求头中的token
        String token = request.getHeader("authorization");
        try {
            //校验token,不抛异常就是认证成功
            Claims claims = JwtUtil.getClaimsBody(token);
            //判断token是否过期
            int i = JwtUtil.verifyToken(claims);
            if (i >= 1) {
                throw new ExpireException();
            }
        } catch (Exception e) {
            //token过期
            if (e instanceof ExpireException) throw new ExpireException();
            //token解析失败
            log.error(e.toString());
            throw new UnloginException();
        }
        return true;
    }

}
