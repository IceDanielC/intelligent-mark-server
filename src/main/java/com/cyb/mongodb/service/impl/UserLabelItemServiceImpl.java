package com.cyb.mongodb.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cyb.mongodb.dao.UserLabelItemMapper;
import com.cyb.mongodb.pojo.UserLabelItem;
import com.cyb.mongodb.service.UserLabelItemService;
import org.springframework.stereotype.Service;

@Service
public class UserLabelItemServiceImpl extends ServiceImpl<UserLabelItemMapper, UserLabelItem> implements UserLabelItemService {
}