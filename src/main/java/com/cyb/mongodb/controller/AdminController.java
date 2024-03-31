package com.cyb.mongodb.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cyb.mongodb.dto.*;
import com.cyb.mongodb.pojo.Admin;
import com.cyb.mongodb.service.AdminService;
import com.cyb.mongodb.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
            return Result.success(new LoginInfo(jwt, freshToken, user.getUsername()));
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

    // 获取所有用户, 可根据角色名称进行模糊查询
    @GetMapping("/userList")
    public Result getUserList(@RequestParam("username")String username){
        if(username.isEmpty()) {
            List<Admin> userList = adminService.list();
            return Result.success(userList);
        }else {
            QueryWrapper<Admin> adminQueryWrapper = new QueryWrapper<>();
            adminQueryWrapper.like("username",username);
            return Result.success(adminService.list(adminQueryWrapper));
        }
    }

    @PostMapping("/update/user")
    public Result updateUser(@RequestBody Admin user){
        if(user.getId() == null){
            adminService.save(user);
        }else {
            adminService.updateById(user);
        }
        return Result.success();
    }

    @DeleteMapping("/user/{id}")
    public Result deleteUser(@PathVariable("id")Integer userId){
        if(adminService.removeById(userId)) {
            return Result.success();
        }else{
            return Result.fail("删除失败");
        }
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
        return Result.success(new DoubleToken(accessToken, freshToken));
    }

    // 注册新用户
    @PostMapping("/createUser")
    public Result registerUser(@RequestBody RegisterUser user){
        Admin registerUser = new Admin();
        if(user.getIsAdmin()){
            registerUser.setRole("ADMIN");
        }else{
            registerUser.setRole("USER");
        }
        registerUser.setPassword(user.getPassword());
        registerUser.setUsername(user.getUsername());
        if(adminService.count(new QueryWrapper<Admin>().eq("username",user.getUsername()))>0){
            return Result.fail(10000,"用户名已存在");
        }
        System.out.println(registerUser);
        return Result.success();
    }

}

