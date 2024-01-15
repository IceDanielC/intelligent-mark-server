package com.cyb.mongodb.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cyb.mongodb.dto.DownloadLabel;
import com.cyb.mongodb.dto.Result;
import com.cyb.mongodb.pojo.Dataset;
import com.cyb.mongodb.pojo.Image;
import com.cyb.mongodb.pojo.LabelInfo;
import com.cyb.mongodb.service.DatasetService;
import com.cyb.mongodb.service.ImageService;
import com.cyb.mongodb.service.LabelInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/labelInfo")
public class LabelInfoController {

    @Autowired
    private LabelInfoService labelInfoService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private DatasetService datasetService;

    //获取某张图片中的所有标签
    @GetMapping("/imageLabels")
    public Result getLabelsByImage(@RequestParam("imageUrl") String url){
        Image image = imageService.getOne(new QueryWrapper<Image>().eq("url", url));
        if(image == null) return Result.success(new ArrayList<>());
        List<LabelInfo> labels = labelInfoService.list(new QueryWrapper<LabelInfo>().
                eq("image_id", image.getId()));
        return Result.success(labels);
    }

    // 保存某张图片中所有的labels
    @PostMapping("/save")
    public Result saveImageLabels(@RequestParam("imageId")Integer imageId, @RequestBody List<LabelInfo> labelInfos){
        Image image = imageService.getOne(new QueryWrapper<Image>().eq("id", imageId));
        if(image == null) return Result.fail("图片不存在");
        // 删除原来的标签
        labelInfoService.remove(new QueryWrapper<LabelInfo>().eq("image_id", image.getId()));
        for(LabelInfo i : labelInfos) {
            labelInfoService.save(i);
        }
        if(!labelInfos.isEmpty()){
            image.setIsAnnotate("true");
            imageService.updateById(image);
        }else{
            image.setIsAnnotate("false");
            imageService.updateById(image);
        }
        // 更新annotatedNumber
        Dataset dataset = datasetService
                .getOne(new QueryWrapper<Dataset>().eq("id", image.getDatasetId()));
        List<Image> images = imageService.list(new QueryWrapper<Image>().eq("dataset_id", dataset.getId())
                .eq("is_annotate", "true"));
        dataset.setAnnotatedNumber(images.size());
        datasetService.updateById(dataset);
        return Result.success("操作成功");
    }

    // 保存图像标注文件YOLO格式
    @PostMapping("/download/yolo")
    public ResponseEntity<byte[]> downloadYOLO(@RequestBody List<DownloadLabel> labelInfos){
        String content = labelInfos.stream()
                .map(label -> String.format("%d %f %f %f %f",
                        label.getLabelIndex(),
                        label.getX(),
                        label.getY(),
                        label.getWidth(),
                        label.getHeight()))
                .reduce((line1, line2) -> line1 + "\n" + line2)
                .orElse("");

        byte[] buf = content.getBytes();

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=labels.txt")
                .contentLength(buf.length)
                .header("Content-Type", "application/x-download")
                .body(buf);
    }
}
