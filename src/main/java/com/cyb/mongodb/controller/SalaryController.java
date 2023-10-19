package com.cyb.mongodb.controller;

import com.cyb.mongodb.dto.Result;
import com.cyb.mongodb.pojo.Salary;
import com.cyb.mongodb.service.SalaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/salary")
public class SalaryController {

    @Autowired
    private SalaryService salaryService;

    //分页获取所有薪资信息
    @GetMapping("/items")
    public Result getAllInfo(@RequestParam(value = "currentPage", defaultValue = "1")Integer currentPage){
        return salaryService.getSalaries(currentPage);
    }

    //按公司模糊查询
    @GetMapping("/searchInc")
    public Result getByInc(@RequestParam(value = "companyName")String companyName){
        return salaryService.searchByInc(companyName);
    }

    //按岗位模糊查询
    @GetMapping("/searchPosition")
    public Result getByPosition(@RequestParam(value = "position")String position){
        return salaryService.searchByPosition(position);
    }

    //按城市模糊查询
    @GetMapping("/searchCity")
    public Result getByCity(@RequestParam(value = "city")String city){
        return salaryService.searchByCity(city);
    }

    //薪资爆料
    @PostMapping("/reveal")
    public Result revealSalary(@RequestBody Salary salary){
        return salaryService.revealSalary(salary);
    }

    //根据id查询薪资
    @GetMapping("/{id}")
    public Result salaryById(@PathVariable("id") Integer id){
        Result result = salaryService.searchSalaryById(id);
        return result;
    }

    //首次浏览增加浏览量
    @GetMapping("/view/{id}")
    public Result addView(@PathVariable("id")Integer id){
        return salaryService.addView(id);
    }

    //查询校招薪资动态(分页)
    @GetMapping("/campus/news")
    public Result campusEmploy(@RequestParam(name = "currentPage",defaultValue = "1")Integer currentPage){
        return salaryService.campusEmploy(currentPage);
    }

    //查询实习薪资动态（分页）
    @GetMapping("/internship/news")
    public Result internshipEmploy(@RequestParam(name = "currentPage",defaultValue = "1")Integer currentPage){
        return salaryService.internshipEmploy(currentPage);
    }

    //用户点赞的实现
    @PostMapping("/likes/{id}")
    public Result doLikes(@PathVariable("id") Integer id, @RequestParam("userId")String userId){
        return salaryService.doLikes(id, userId);
    }

    //取消点赞
    @PostMapping("/dislikes/{id}")
    public Result undoLikes(@PathVariable("id") Integer id, @RequestParam("userId")String userId){
        return salaryService.undoLike(id, userId);
    }
}
