package com.cyb.mongodb.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyb.mongodb.dto.Result;
import com.cyb.mongodb.pojo.Dataset;
import com.cyb.mongodb.pojo.Image;
import com.cyb.mongodb.service.DatasetService;
import com.cyb.mongodb.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/dataset")
public class DatasetController {

    @Autowired
    private DatasetService datasetService;
    @Autowired
    private ImageService imageService;

    //获取登陆用户的所有数据集
    @GetMapping("/user")
    public Result getDatasetsByUser(@RequestParam("username")String username){
        List<Dataset> datasetList = datasetService.list(new QueryWrapper<Dataset>().eq("username", username));
        return Result.success(datasetList);
    }

    //分页获取登陆用户的所有数据集
    @GetMapping("/user/page")
    public Result getDatasetPagesByUser(@RequestParam("username")String username,
                                        @RequestParam(defaultValue = "1") int current, // 默认当前页为第1页
                                        @RequestParam(defaultValue = "10") int size) {

        Page<Dataset> page = new Page<>(current, size);
        // 使用 MyBatis-Plus 的分页查询方法
        IPage<Dataset> datasetPage = datasetService
                .page(page, new QueryWrapper<Dataset>().eq("username", username));
        return Result.success(datasetPage);
    }

    //根据数据集名称和version获取下面的所有图片
    @GetMapping("/images/{datasetName}/{version}")
    public Result getDatasetImages(@PathVariable("datasetName")String datasetName,
                                   @PathVariable("version")String version){
        Dataset dataset = datasetService.getOne(new QueryWrapper<Dataset>()
                .eq("name", datasetName).eq("version", version));
        if(dataset == null) return Result.success(null);
        List<Image> imageList = imageService.list(new QueryWrapper<Image>().eq("dataset_id", dataset.getId()));
        return Result.success(imageList);
    }

    //根据数据集名称和version获取下面的所有已标注图片
    @GetMapping("/images/annotated/{datasetName}/{version}")
    public Result getDatasetAnnotatedImages(@PathVariable("datasetName")String datasetName,
                                   @PathVariable("version")String version){
        Dataset dataset = datasetService.getOne(new QueryWrapper<Dataset>()
                .eq("name", datasetName).eq("version", version));
        if(dataset == null) return Result.success(null);
        List<Image> imageList = imageService.list(new QueryWrapper<Image>()
                .eq("dataset_id", dataset.getId()).eq("is_annotate","true"));
        return Result.success(imageList);
    }

    //根据数据集名称和version获取下面的所有未标注图片
    @GetMapping("/images/unAnnotated/{datasetName}/{version}")
    public Result getDatasetUnAnnotatedImages(@PathVariable("datasetName")String datasetName,
                                            @PathVariable("version")String version){
        Dataset dataset = datasetService.getOne(new QueryWrapper<Dataset>()
                .eq("name", datasetName).eq("version", version));
        if(dataset == null) return Result.success(null);
        List<Image> imageList = imageService.list(new QueryWrapper<Image>()
                .eq("dataset_id", dataset.getId()).eq("is_annotate","false"));
        return Result.success(imageList);
    }

    // 导入图片
    @PostMapping("/upload/images")
    public Result uploadImages(@RequestBody List<Image> imageList){
        boolean success = imageService.saveBatch(imageList);
        // 更新img_number
        Dataset dataset = datasetService
                .getOne(new QueryWrapper<Dataset>().eq("id", imageList.get(0).getDatasetId()));
        dataset.setImgNumber(dataset.getImgNumber()+imageList.size());
        datasetService.updateById(dataset);
        if(success) return Result.success();
        return Result.fail("上传失败");
    }
}
