package com.cyb.mongodb.controller;

import com.cyb.mongodb.dto.Result;
import com.cyb.mongodb.pojo.Versus;
import com.cyb.mongodb.service.VersusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/versus")
public class VersusController {

    @Autowired
    private VersusService versusService;

    //分页查询所有比薪
    @GetMapping("list")
    public Result versusList(@RequestParam(value = "currentPage",defaultValue = "1")Integer currentPage){
        return versusService.versusList(currentPage);
    }

    //根据Id查询详情
    @GetMapping("detail/{id}")
    public Result detailById(@PathVariable("id")Integer id){
        return versusService.detailById(id);
    }

    //新增爆料
    @PostMapping("add")
    public Result addVersus(@RequestBody Versus versus){
        return versusService.addVersus(versus);
    }

    //支持某个爆料的选项1
    @PostMapping("endorse1/{id}")
    public Result endorse1(@PathVariable("id")Integer id){
        return versusService.endorse1(id);
    }

    //支持某个爆料的选项1
    @PostMapping("endorse2/{id}")
    public Result endorse2(@PathVariable("id")Integer id){
        return versusService.endorse2(id);
    }

    //比薪搜索
    @GetMapping("search")
    public Result search(@RequestParam("keyword")String keyword,
                         @RequestParam(value = "currentPage",defaultValue = "1")Integer currentPage){
        return versusService.versusSearch(keyword,currentPage);
    }
}
