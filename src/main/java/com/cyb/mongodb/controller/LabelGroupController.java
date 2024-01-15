package com.cyb.mongodb.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cyb.mongodb.dto.Result;
import com.cyb.mongodb.pojo.Dataset;
import com.cyb.mongodb.pojo.LabelGroup;
import com.cyb.mongodb.service.DatasetService;
import com.cyb.mongodb.service.LabelGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/labelGroup")
public class LabelGroupController {

    @Autowired
    private LabelGroupService labelGroupService;
    @Autowired
    private DatasetService datasetService;

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
}
