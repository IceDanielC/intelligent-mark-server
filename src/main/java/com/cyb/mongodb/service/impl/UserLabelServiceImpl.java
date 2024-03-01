package com.cyb.mongodb.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cyb.mongodb.dao.UserLabelMapper;
import com.cyb.mongodb.pojo.UserLabel;
import com.cyb.mongodb.service.UserLabelService;
import org.springframework.stereotype.Service;
@Service
public class UserLabelServiceImpl  extends ServiceImpl<UserLabelMapper, UserLabel> implements UserLabelService {
}
