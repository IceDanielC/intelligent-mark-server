package com.cyb.mongodb.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyb.mongodb.dto.Result;
import com.cyb.mongodb.pojo.Admin;
import com.cyb.mongodb.pojo.Dataset;
import com.cyb.mongodb.pojo.UserLabel;
import com.cyb.mongodb.service.AdminService;
import com.cyb.mongodb.service.UserLabelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/userLabel")
public class UserLabelController {

    @Autowired
    private UserLabelService userLabelService;
    @Autowired
    private AdminService adminService;

    @GetMapping("/list")
    public Result getUserLabelPagesByUser(@RequestParam("username")String username,
                                        @RequestParam(defaultValue = "1") int current, // 默认当前页为第1页
                                        @RequestParam(defaultValue = "5") int size,
                                        @RequestParam(defaultValue = "") String nameKeyword) {
        Page<UserLabel> page = new Page<>(current, size);
        Admin user = adminService.getOne(new QueryWrapper<Admin>().eq("username", username));
        List<UserLabel> userLabels = userLabelService.getBaseMapper()
                .selectList(new QueryWrapper<UserLabel>().eq("user_id", user.getId())
                        .like("group_name", nameKeyword));
        // 按照时间降序排列
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        userLabels.sort((o1, o2) ->
        {
            try {
                return Math.toIntExact(simpleDateFormat.parse(o2.getCreateTime()).getTime() / 10000 - simpleDateFormat.parse(o1.getCreateTime()).getTime() / 10000);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
        IPage<UserLabel> userLabelPage = userLabelService
                .page(page, new QueryWrapper<UserLabel>().eq("user_id", user.getId())
                        .like("group_name", nameKeyword));
        return Result.success(userLabelPage);
    }

    // 新增或更新标签组
    @PostMapping("/update")
    public Result addUserLabel(@RequestBody UserLabel userLabel){
        String groupName = userLabel.getGroupName();
        if(userLabel.getId()==null){
            // 新增
            String username = userLabel.getUsername();
            Admin user = adminService.getOne(new QueryWrapper<Admin>().eq("username", username));
            // 创建一个SimpleDateFormat对象，用于格式化时间
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            // 获取系统当前时间
            Date date = new Date();
            // 使用SimpleDateFormat对象将时间格式化为指定格式
            String currentTime = sdf.format(date);
            userLabel.setCreateTime(currentTime);
            userLabel.setUserId(user.getId());
            if(userLabelService.count(new QueryWrapper<UserLabel>().eq("group_name",groupName))>=1){
                return Result.fail(10000,"名称已存在");
            }
            userLabelService.save(userLabel);
        }else{
            userLabelService.updateById(userLabel);
        }
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result deleteUserLabel(@PathVariable("id")Integer userId){
        userLabelService.removeById(userId);
        return Result.success();
    }
}
