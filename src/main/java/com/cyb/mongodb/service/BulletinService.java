package com.cyb.mongodb.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cyb.mongodb.dto.Result;
import com.cyb.mongodb.pojo.Bulletin;

import java.util.List;

//推送的服务层接口
public interface BulletinService extends IService<Bulletin> {

    //分页查询所有推送
    public Result getBulletin(Integer currentPage);
}
