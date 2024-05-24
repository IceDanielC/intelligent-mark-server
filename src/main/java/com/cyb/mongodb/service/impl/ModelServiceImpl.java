package com.cyb.mongodb.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cyb.mongodb.dao.ModelMapper;
import com.cyb.mongodb.pojo.DetectionModel;
import com.cyb.mongodb.service.ModelService;
import org.springframework.stereotype.Service;

@Service
public class ModelServiceImpl extends ServiceImpl<ModelMapper, DetectionModel> implements ModelService {
}
