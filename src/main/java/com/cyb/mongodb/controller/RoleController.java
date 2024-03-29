package com.cyb.mongodb.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cyb.mongodb.dto.Result;
import com.cyb.mongodb.pojo.Admin;
import com.cyb.mongodb.pojo.Role;
import com.cyb.mongodb.service.AdminService;
import com.cyb.mongodb.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private AdminService adminService;

    //获取所有角色及权限
    @GetMapping("/list")
    public Result getRoleList() {
       List<Role> roleList = roleService.list();
       for (Role r : roleList){
            r.setMenuList(r.getMenus().split(","));
       }
//       for(long i = 0;i<Long.parseLong("110000000");i++){
//
//       }
       return Result.success(roleList);
    }

    // 新增或修改角色
    @PostMapping("/update")
    public Result updateRole(@RequestBody Role role){
        if(role.getId() == null){
            StringBuilder menus = new StringBuilder();
            for(String menu : role.getMenuList()){
                menus.append(menu).append(",");
            }
            role.setMenus(String.valueOf(menus));
            roleService.save(role);
        }else{
            StringBuilder menus = new StringBuilder();
            for(String menu : role.getMenuList()){
                menus.append(menu).append(",");
            }
            role.setMenus(String.valueOf(menus));
            roleService.updateById(role);
        }
        return Result.success();
    }

    // 查询用户权限及菜单
    @GetMapping("/menus/{username}")
    public Result getRoleByName(@PathVariable("username")String username){
        Admin user = adminService.getOne(new QueryWrapper<Admin>().eq("username", username));
        Role role = roleService.getOne(new QueryWrapper<Role>().eq("role", user.getRole()));
        return Result.success(role.getMenus());
    }

}
