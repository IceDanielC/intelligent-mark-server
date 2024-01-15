package com.cyb.mongodb.controller;

import com.cyb.mongodb.dto.Result;
import com.cyb.mongodb.pojo.Role;
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

    //获取所有角色及权限
    @GetMapping("/list")
    public Result getRoleList() {
       List<Role> roleList = roleService.list();
       for (Role r : roleList){
            r.setMenuList(r.getMenus().split(","));
       }
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

}
