package com.cyb.mongodb.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cyb.mongodb.dao.DatasetMapper;
import com.cyb.mongodb.pojo.Dataset;
import com.cyb.mongodb.service.DatasetService;
import org.springframework.stereotype.Service;

@Service
public class DatasetServiceImpl extends ServiceImpl<DatasetMapper, Dataset> implements DatasetService {
}
