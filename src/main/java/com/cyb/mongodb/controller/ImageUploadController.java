package com.cyb.mongodb.controller;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.cyb.mongodb.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@Slf4j
@RequestMapping("/upload")
public class ImageUploadController {
    @PostMapping("/image")
    public Result uploadImage(@RequestParam MultipartFile file) throws IOException {
//        //获取文件原始名称
//        String originalFilename = file.getOriginalFilename();
//        //获取文件的类型
//        // String type = FileUtil.extName(originalFilename);
//        //获取文件大小
//        long size = file.getSize();
//        //设置下载的文件路径
//        String url = "http://localhost:9090/file/" + originalFilename;
//        return Result.success(url);

        String endpoint = "yours";
        String accessKeyId = "yours";
        String accessKeySecret = "yours";
        String bucketName = "yours";
        //获取文件原始名称
        String objectName = file.getOriginalFilename();
        String fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")) + objectName;

        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, file.getInputStream());
            // 设置该属性可以返回response。如果不设置，则返回的response为空。
            putObjectRequest.setProcess("true");
            //创建PutObject请求。
            PutObjectResult result = ossClient.putObject(putObjectRequest);
            return Result.success(result.getResponse().getUri());
        }catch (Exception err) {
            System.out.println(err.getMessage());
        }finally {
            if(ossClient != null){
                ossClient.shutdown();
            }
        }
        return Result.fail("上传失败");
    }
}
