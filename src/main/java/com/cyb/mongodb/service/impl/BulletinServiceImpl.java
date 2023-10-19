package com.cyb.mongodb.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cyb.mongodb.dao.BulletinMapper;
import com.cyb.mongodb.dto.Result;
import com.cyb.mongodb.pojo.Bulletin;
import com.cyb.mongodb.service.BulletinService;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.cyb.mongodb.common.Constance.MEMBER_MAXSIZE;

@Service
public class BulletinServiceImpl extends ServiceImpl<BulletinMapper, Bulletin> implements BulletinService {

    @Override
    public Result getBulletin(Integer currentPage) {
        //mybatis分页
        IPage<Bulletin> page = query().orderByDesc("view").page(new Page<>(currentPage, MEMBER_MAXSIZE));
        return Result.success(page);
    }


}
