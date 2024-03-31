package com.cyb.mongodb.controller;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyb.mongodb.dto.Result;
import com.cyb.mongodb.pojo.Admin;
import com.cyb.mongodb.pojo.Dataset;
import com.cyb.mongodb.pojo.Image;
import com.cyb.mongodb.service.AdminService;
import com.cyb.mongodb.service.DatasetService;
import com.cyb.mongodb.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@Slf4j
@RequestMapping("/dataset")
public class DatasetController {

    @Autowired
    private DatasetService datasetService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private AdminService adminService;

    public void initNewDataset(Dataset dataset){
        dataset.setAnnotatedNumber(0);
        dataset.setImgNumber(0);
        Admin user = adminService.getOne(new QueryWrapper<Admin>().eq("username", dataset.getUsername()));
        dataset.setUserId(user.getId());
        // 创建一个SimpleDateFormat对象，用于格式化时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 获取系统当前时间
        Date date = new Date();
        // 使用SimpleDateFormat对象将时间格式化为指定格式
        String currentTime = sdf.format(date);
        dataset.setCreateTime(currentTime);

        dataset.setSize(0);
    }

    //获取登陆用户的所有数据集
    @GetMapping("/user")
    public Result getDatasetsByUser(@RequestParam("username")String username){
        List<Dataset> datasetList = datasetService.list(new QueryWrapper<Dataset>().eq("username", username)
                .orderByDesc("create_time"));
        return Result.success(datasetList);
    }

    //分页获取登陆用户的所有数据集（含模糊查询）
    @GetMapping("/user/page")
    public Result getDatasetPagesByUser(@RequestParam("username")String username,
                                        @RequestParam(defaultValue = "1") int current, // 默认当前页为第1页
                                        @RequestParam(defaultValue = "10") int size,
                                        @RequestParam(defaultValue = "") String nameKeyword) {

        Page<Dataset> page = new Page<>(current, size);
        List<Dataset> datasets = datasetService.getBaseMapper()
                .selectList(new QueryWrapper<Dataset>().eq("username", username)
                        .like("name", nameKeyword));
        // Process the fetched datasets and group them by 'name'
        Map<String, List<Dataset>> groupedDatasets = datasets.stream().collect(Collectors.groupingBy(Dataset::getName));
        List<Dataset> processedDatasets = new ArrayList<>();
        for (Map.Entry<String, List<Dataset>> entry : groupedDatasets.entrySet()) {
            List<Dataset> versions = entry.getValue();
            if (versions.size() > 1) {
                // Sort the versions to ensure V1 is first
                versions.sort(Comparator.comparing(Dataset::getVersion));
                // Set the children for V1 dataset
                Dataset v1Dataset = versions.get(0);
                v1Dataset.setChildren(versions.subList(1, versions.size()));
                processedDatasets.add(v1Dataset);
            } else {
                // Only one version available, add it directly
                processedDatasets.add(versions.get(0));
            }
        }
        // 按照时间降序排列
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        processedDatasets.sort((o1, o2) ->
        {
            try {
                return Math.toIntExact(simpleDateFormat.parse(o2.getCreateTime()).getTime() / 10000 - simpleDateFormat.parse(o1.getCreateTime()).getTime() / 10000);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
        // 使用 MyBatis-Plus 的分页查询方法
        IPage<Dataset> datasetPage = datasetService
                .page(page, new QueryWrapper<Dataset>().eq("username", username));
        datasetPage.setTotal(processedDatasets.size());
        datasetPage.setRecords(processedDatasets
                .subList(size*(current-1), Math.min(size*current, processedDatasets.size())));

        return Result.success(datasetPage);
    }

    //根据数据集名称和version获取下面的所有图片
    @GetMapping("/images/{username}/{datasetName}/{version}")
    public Result getDatasetImages(@PathVariable("username")String username,
                                   @PathVariable("datasetName")String datasetName,
                                   @PathVariable("version")String version){
        Dataset dataset = datasetService.getOne(new QueryWrapper<Dataset>()
                .eq("name", datasetName).eq("version", version).eq("username",username));
        if(dataset == null) return Result.success(null);
        List<Image> imageList = imageService.list(new QueryWrapper<Image>().eq("dataset_id", dataset.getId()));
        return Result.success(imageList);
    }

    //根据数据集名称和version获取下面的所有已标注图片
    @GetMapping("/images/annotated/{username}/{datasetName}/{version}")
    public Result getDatasetAnnotatedImages(@PathVariable("username")String username,
                                            @PathVariable("datasetName")String datasetName,
                                            @PathVariable("version")String version){
        Dataset dataset = datasetService.getOne(new QueryWrapper<Dataset>()
                .eq("name", datasetName).eq("version", version).eq("username",username));
        if(dataset == null) return Result.success(null);
        List<Image> imageList = imageService.list(new QueryWrapper<Image>()
                .eq("dataset_id", dataset.getId()).eq("is_annotate","true"));
        return Result.success(imageList);
    }

    //根据数据集名称和version获取下面的所有未标注图片
    @GetMapping("/images/unAnnotated/{username}/{datasetName}/{version}")
    public Result getDatasetUnAnnotatedImages(@PathVariable("username")String username,
                                              @PathVariable("datasetName")String datasetName,
                                            @PathVariable("version")String version){
        Dataset dataset = datasetService.getOne(new QueryWrapper<Dataset>()
                .eq("name", datasetName).eq("version", version).eq("username",username));
        if(dataset == null) return Result.success(null);
        List<Image> imageList = imageService.list(new QueryWrapper<Image>()
                .eq("dataset_id", dataset.getId()).eq("is_annotate","false"));
        return Result.success(imageList);
    }

    // 向数据集中导入图片
    @PostMapping("/upload/images")
    public Result uploadImages(@RequestBody List<Image> imageList){
        boolean success = imageService.saveBatch(imageList);
        // 更新img_number
        Dataset dataset = datasetService
                .getOne(new QueryWrapper<Dataset>().eq("id", imageList.get(0).getDatasetId()));
        dataset.setImgNumber(dataset.getImgNumber()+imageList.size());
        // 更新总大小
        int totalSize = dataset.getSize();
        for (Image image : imageList) {
            totalSize += image.getSize();
        }
        dataset.setSize(totalSize);
        datasetService.updateById(dataset);
        if(success) return Result.success();
        return Result.fail("上传失败");
    }

    // 新增数据集
    @PostMapping("/create")
    public Result createDataset(@RequestBody Dataset dataset){
        if(datasetService.count(new QueryWrapper<Dataset>().eq("name",dataset.getName())
                .eq("username",dataset.getUsername())) > 0){
            return Result.fail(405,"数据集名称已存在");
        }
        initNewDataset(dataset);
        boolean save = datasetService.save(dataset);
        return Result.success(save);
    }

    // 删除数据集
    @DeleteMapping("/delete/{datasetId}")
    public Result deleteDatasetById(@PathVariable("datasetId") Integer datasetId){
        if(datasetService.removeById(datasetId)){
            return Result.success();
        }else{
            return Result.fail(405,"删除失败");
        }
    }

    // 根据数据集名称获取当前最高版本
    @GetMapping("/latestVersion/{datasetName}")
    public Result getLatestVersion(@PathVariable("datasetName") String datasetName){
        List<Dataset> datasetList = datasetService.list(new QueryWrapper<Dataset>().eq("name", datasetName));
        Dataset dataset = datasetList.get(datasetList.size() - 1);
        return Result.success(dataset.getVersion());
    }

    // 新增数据集版本
    @PostMapping("/newVersion")
    public Result createDatasetVersion(@RequestBody Dataset dataset,
                                       @RequestParam("extends")Boolean isExtends,
                                       @RequestParam("historyVersion")String historyVersion){
        if(!isExtends){
            // 不继承之前的数据集
            initNewDataset(dataset);
            datasetService.save(dataset);
        }else{
            // 继承之前版本的数据集
            Dataset preDataset = datasetService.getOne(new QueryWrapper<Dataset>().eq("username", dataset.getUsername())
                    .eq("name", dataset.getName()).eq("version", historyVersion));
            List<Image> images = imageService.list(new QueryWrapper<Image>().eq("dataset_id", preDataset.getId()));
            // 新建数据集
            initNewDataset(dataset);
            datasetService.saveOrUpdate(dataset);
            Dataset savedDataset = datasetService.getOne(new QueryWrapper<Dataset>().eq("name", dataset.getName())
                    .eq("username", dataset.getUsername()).eq("version", dataset.getVersion()));
            for (Image i : images){
                i.setIsAnnotate("false");
                i.setId(null);
                i.setDatasetId(savedDataset.getId());
            }
            // 调用上传图片的接口更新数据集-图片联动数据
            uploadImages(images);
        }
        return Result.success("新增版本成功");
    }
}
