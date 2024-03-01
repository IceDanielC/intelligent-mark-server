package com.cyb.mongodb.controller;

import com.cyb.mongodb.dto.Result;
import com.cyb.mongodb.pojo.Menu;
import com.cyb.mongodb.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    //获取所有菜单项
    @GetMapping("/list")
    public Result getAllMenus(){
        List<Menu> menuList = menuService.list();
        return Result.success(menuList);
    }
}
