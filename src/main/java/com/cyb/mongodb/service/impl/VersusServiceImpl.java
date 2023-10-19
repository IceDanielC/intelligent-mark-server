package com.cyb.mongodb.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cyb.mongodb.dao.VersusMapper;
import com.cyb.mongodb.dto.Result;
import com.cyb.mongodb.pojo.Salary;
import com.cyb.mongodb.pojo.Versus;
import com.cyb.mongodb.service.VersusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.cyb.mongodb.common.Constance.SALARY_SIZE;
import static com.cyb.mongodb.common.Constance.VERSUS_SIZE;

@Service
public class VersusServiceImpl extends ServiceImpl<VersusMapper, Versus> implements VersusService {

    @Autowired
    private VersusMapper versusMapper;

    @Override
    public Result versusList(Integer currentPage) {
        IPage<Versus> page = query().page(new Page<>(currentPage, VERSUS_SIZE));
        return Result.success(page);
    }

    @Override
    public Result detailById(Integer id) {
        Versus versus = versusMapper.selectById(id);
        versus.setView(versus.getView()+1);
        versusMapper.updateById(versus);
        return Result.success(versus);
    }

    @Override
    public Result addVersus(Versus versus) {
        versus.setView(1);
        versus.setEndorse1(0);
        versus.setEndorse2(0);
        return Result.success(versusMapper.insert(versus));
    }

    @Override
    public Result endorse1(Integer id) {
        Versus versus = versusMapper.selectById(id);
        versus.setEndorse1(versus.getEndorse1()+1);
        return Result.success(versusMapper.updateById(versus));
    }

    @Override
    public Result endorse2(Integer id) {
        Versus versus = versusMapper.selectById(id);
        versus.setEndorse2(versus.getEndorse2()+1);
        return Result.success(versusMapper.updateById(versus));
    }

    @Override
    public Result versusSearch(String keyword, Integer cur) {
        IPage<Versus> page = query().like("choice1", keyword).page(new Page<>(cur, VERSUS_SIZE));
        IPage<Versus> page2 = query().like("choice2", keyword).page(new Page<>(cur, VERSUS_SIZE));
        List<Versus> records = page.getRecords();
        List<Versus> records2 = page2.getRecords();
        List<Versus> res = new ArrayList<>();
        res.addAll(records);
        res.addAll(records2);
        page.setRecords(res);
        return Result.success(page);
    }

}
