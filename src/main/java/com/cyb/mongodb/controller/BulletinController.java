package com.cyb.mongodb.controller;

import com.cyb.mongodb.dto.Result;
import com.cyb.mongodb.service.BulletinService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/bulletin")
public class BulletinController {

    @Autowired
    private BulletinService bulletinService;

    //分页查询所有推送
    @GetMapping("/items")
    public Result getBulletins(@RequestParam(value = "currentPage", defaultValue = "1")Integer currentPage){
        return bulletinService.getBulletin(currentPage);
    }

}
