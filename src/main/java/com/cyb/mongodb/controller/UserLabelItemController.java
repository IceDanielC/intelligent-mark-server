package com.cyb.mongodb.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyb.mongodb.dto.Result;
import com.cyb.mongodb.pojo.UserLabel;
import com.cyb.mongodb.pojo.UserLabelItem;
import com.cyb.mongodb.service.UserLabelItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/userLabelItem")
public class UserLabelItemController {

    @Autowired
    private UserLabelItemService userLabelItemService;

    // 根据id分页查询标签组中的标签
    @GetMapping("/list/{id}")
    public Result getLabels(@PathVariable("id")Integer groupId,
                            @RequestParam(defaultValue = "1") int current, // 默认当前页为第1页
                            @RequestParam(defaultValue = "5") int size,
                            @RequestParam(defaultValue = "") String nameKeyword){
        Page<UserLabelItem> page = new Page<>(current, size);
        IPage<UserLabelItem> itemIPage = userLabelItemService
                .page(page, new QueryWrapper<UserLabelItem>().eq("group_id", groupId)
                .like("name", nameKeyword));
        return Result.success(itemIPage);
    }

    // 给标签组新增标签或更新标签
    @PostMapping("/update")
    public Result addLabel(@RequestBody UserLabelItem userLabelItem){
        if(userLabelItem.getId()==null){
            if(userLabelItemService.count(new QueryWrapper<UserLabelItem>()
                    .eq("name",userLabelItem.getName()))>0){
                return Result.fail(10000,"标签已存在");
            }
            userLabelItemService.save(userLabelItem);
        }else{
            userLabelItemService.updateById(userLabelItem);
        }
        return Result.success();
    }

    // 删除标签
    @DeleteMapping("/{id}")
    public Result deleteUserLabel(@PathVariable("id")Integer id){
        userLabelItemService.removeById(id);
        return Result.success();
    }

    // 批量删除
    @PostMapping ("/batch")
    public Result deleteUserLabelBatch(@RequestBody List<Integer> idList){
        userLabelItemService.removeByIds(idList);
        return Result.success();
    }
}
