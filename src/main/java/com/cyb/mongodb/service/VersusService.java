package com.cyb.mongodb.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cyb.mongodb.dto.Result;
import com.cyb.mongodb.pojo.Versus;

public interface VersusService extends IService<Versus> {

    //分页查询所有比薪
    Result versusList(Integer currentPage);

    //根据id查询详情
    Result detailById(Integer id);

    //发布爆料
    Result addVersus(Versus versus);

    //赞同选项1
    Result endorse1(Integer id);

    //赞同选项2
    Result endorse2(Integer id);

    //按比薪关键字模糊查询
    Result versusSearch(String keyword, Integer cur);
}
