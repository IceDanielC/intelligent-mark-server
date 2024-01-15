package com.cyb.mongodb.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cyb.mongodb.dao.LabelInfoMapper;
import com.cyb.mongodb.pojo.LabelInfo;
import com.cyb.mongodb.service.LabelInfoService;
import org.springframework.stereotype.Service;

@Service
public class LabelInfoServiceImpl extends ServiceImpl<LabelInfoMapper, LabelInfo> implements LabelInfoService {
}
