package com.cyb.mongodb.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cyb.mongodb.dao.MenuMapper;
import com.cyb.mongodb.pojo.Menu;
import com.cyb.mongodb.service.MenuService;
import org.springframework.stereotype.Service;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
}
