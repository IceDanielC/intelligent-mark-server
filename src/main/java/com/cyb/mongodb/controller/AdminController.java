package com.cyb.mongodb.controller;

import com.cyb.mongodb.dto.DoubleToken;
import com.cyb.mongodb.dto.ExpireException;
import com.cyb.mongodb.dto.Result;
import com.cyb.mongodb.dto.UnloginException;
import com.cyb.mongodb.pojo.Admin;
import com.cyb.mongodb.service.AdminService;
import com.cyb.mongodb.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Slf4j
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    //管理员登录
    @RequestMapping("/login")
    public Result login(@RequestBody Admin user, HttpServletResponse response) {
        Admin admin = adminService.query().eq("username", user.getUsername())
                .eq("password", user.getPassword()).one();
        if (admin != null) {
            // 获取access_token
            String jwt = JwtUtil.getToken(Long.valueOf(admin.getId()));
            // 获取fresh_token
            String freshToken = JwtUtil.getFreshToken(Long.valueOf(admin.getId()));
            //将jwt添加在响应头的Authorization中
            response.setHeader("authorization", jwt);
            //列出了哪些首部可以作为响应的一部分暴露给外部,可以在跨域请求的情况下获取jwt的值
            response.setHeader("Access-Control-Expose-Headers", "authorization");
            return Result.success(new DoubleToken(jwt,freshToken));
        }
        return Result.fail("用户名或密码错误");
    }

    //修改密码
    @RequestMapping("/reset")
    public Result resetPwd(@RequestParam("password") String password, HttpServletRequest request) {
        String jwt = request.getHeader("authorization");
        //获取jwt中的userId
        String userId = (String) JwtUtil.getClaimsBody(jwt).get("sub");
        adminService.update().eq("id", Integer.parseInt(userId)).set("password", password).update();
        return Result.success();
    }

    //freshToken实现无感刷新
    @PostMapping("/refresh")
    public Result refreshToken(@RequestParam("token") String token) throws ExpireException, UnloginException {
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
        //获取jwt中的userId
        String userId = (String) JwtUtil.getClaimsBody(token).get("sub");
        //重新获取access_token和fresh_token
        String accessToken = JwtUtil.getToken(Long.valueOf(userId));
        String freshToken = JwtUtil.getFreshToken(Long.valueOf(userId));
        return Result.success(new DoubleToken(accessToken,freshToken));
    }

}

