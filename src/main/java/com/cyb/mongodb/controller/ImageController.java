package com.cyb.mongodb.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
@RequestMapping("/image")
public class ImageController {

    @Autowired
    private ImageService imageService;
    @Autowired
    private DatasetService datasetService;

    // 查看某张图片是否已经标注过了
    @GetMapping("/isAnnotated")
    public Result isImageAnnotated(@RequestParam("imageUrl")String imageUrl){
        Image image = imageService.getOne(new QueryWrapper<Image>().eq("url", imageUrl));
        if(image == null) return Result.fail("图片不存在");
        return Result.success(Boolean.parseBoolean(image.getIsAnnotate()));
    }

    // 查看某张图片是否为有效数据
    @GetMapping("/isValidated")
    public Result isValidated(@RequestParam("imageUrl")String imageUrl){
        Image image = imageService.getOne(new QueryWrapper<Image>().eq("url", imageUrl));
        if(image == null) return Result.fail("图片不存在");
        return Result.success(Boolean.parseBoolean(image.getIsValidated()));
    }

    // 获取某一数据集中的所有图片
    @GetMapping("/dataset/list")
    public Result getImagesFromDataset(@RequestParam("datasetId")Integer id){
        List<Image> imageList = imageService.list(new QueryWrapper<Image>().eq("dataset_id", id));
        return Result.success(imageList);
    }

    // 删除某个id的图片
    @DeleteMapping("/{imgId}")
    public Result deleteImageById(@PathVariable("imgId")Integer imgId){
        Image image = imageService.getById(imgId);
        Dataset dataset = datasetService.getOne(new QueryWrapper<Dataset>().eq("id", image.getDatasetId()));
        if(imageService.removeById(imgId)) {
            // 更新数据集的img_number和annotated_number
            dataset.setImgNumber(dataset.getImgNumber()-1);
            if(image.getIsAnnotate().equals("true")){
                dataset.setAnnotatedNumber(dataset.getAnnotatedNumber()-1);
            }
            datasetService.updateById(dataset);
            return Result.success();
        }
        return Result.fail("删除失败");
    }

}
