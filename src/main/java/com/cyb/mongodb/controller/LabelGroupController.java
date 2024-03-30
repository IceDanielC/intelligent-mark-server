package com.cyb.mongodb.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cyb.mongodb.dto.Result;
import com.cyb.mongodb.pojo.*;
import com.cyb.mongodb.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@Slf4j
@RequestMapping("/labelGroup")
public class LabelGroupController {

    @Autowired
    private LabelGroupService labelGroupService;
    @Autowired
    private DatasetService datasetService;
    @Autowired
    private UserLabelService userLabelService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private UserLabelItemService userLabelItemService;


    // 获取某个数据集-version下所有的label
    @GetMapping("/dataset/{datasetName}/{version}")
    public Result getDatasetLabels(@PathVariable("datasetName")String datasetName,
                                   @PathVariable("version")String version){
        Dataset dataset = datasetService.getOne(new QueryWrapper<Dataset>()
                .eq("name", datasetName).eq("version", version));
        if(dataset == null) return Result.fail(404,"未找到对于数据集");
        List<LabelGroup> labelGroups = labelGroupService.
                list(new QueryWrapper<LabelGroup>().eq("dataset_id", dataset.getId()));
        return Result.success(labelGroups);
    }

    // 标签栏新增标签
    @PostMapping("/add/{datasetName}/{version}")
    public Result addDatasetLabel(@PathVariable("datasetName")String datasetName,
                                  @PathVariable("version")String version,
                                  @RequestBody LabelGroup labelGroup){
        Dataset dataset = datasetService.getOne(new QueryWrapper<Dataset>()
                .eq("name", datasetName).eq("version", version));
        labelGroup.setDatasetId(dataset.getId());
        labelGroupService.save(labelGroup);
        return Result.success();
    }

    // 删除标签
    @DeleteMapping("/delete/{labelId}")
    public Result deleteDatasetLabel(@PathVariable("labelId")Integer labelId){
        labelGroupService.removeById(labelId);
        return Result.success();
    }

    // 合并标签组
    @GetMapping("/merge/datasetLabel")
    public Result mergeLabelGroup(@RequestParam("username")String username,
                                  @RequestParam("datasetName")String datasetName,
                                  @RequestParam("datasetVersion")String datasetVersion,
                                  @RequestParam("groupName")String groupName){
        Dataset dataset = datasetService.getOne(new QueryWrapper<Dataset>().eq("username", username)
                .eq("name", datasetName).eq("version", datasetVersion));
        Integer datasetId = dataset.getId();
        // 数据集中原先的标签
        List<LabelGroup> labelGroupList = labelGroupService.list(new QueryWrapper<LabelGroup>()
                .eq("dataset_id", datasetId));
        Admin user = adminService.getOne(new QueryWrapper<Admin>().eq("username", username));
        UserLabel labelGroup = userLabelService.getOne(new QueryWrapper<UserLabel>()
                .eq("user_id", user.getId()).eq("group_name", groupName));
        // 用户标签组中的标签
        List<UserLabelItem> userLabelItemList = userLabelItemService.list(new QueryWrapper<UserLabelItem>()
                .eq("group_id", labelGroup.getId()));
        if(userLabelItemList.isEmpty()) return Result.fail(10000,"标签组为空");
        for (UserLabelItem userLabelItem : userLabelItemList) {
            for(LabelGroup l : labelGroupList){
                if(Objects.equals(l.getName(), userLabelItem.getName())){
                    labelGroupService.removeById(l.getId());
                }
            }
            LabelGroup tmp = new LabelGroup();
            tmp.setDatasetId(datasetId);
            tmp.setName(userLabelItem.getName());
            tmp.setColor(userLabelItem.getColor());
            labelGroupService.save(tmp);
        }
        return Result.success();
    }
}
