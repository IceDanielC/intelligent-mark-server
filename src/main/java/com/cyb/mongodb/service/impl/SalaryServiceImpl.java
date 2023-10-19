package com.cyb.mongodb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cyb.mongodb.dao.SalaryMapper;
import com.cyb.mongodb.dto.Result;
import com.cyb.mongodb.pojo.Salary;
import com.cyb.mongodb.service.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static com.cyb.mongodb.common.Constance.MEMBER_MAXSIZE;
import static com.cyb.mongodb.common.Constance.SALARY_SIZE;

@Service
public class SalaryServiceImpl extends ServiceImpl<SalaryMapper, Salary> implements SalaryService {

    @Autowired
    private SalaryMapper salaryMapper;

    @Override
    public Result getSalaries(Integer currentPage) {
        //mybatis分页
        IPage<Salary> page = query().page(new Page<>(currentPage, SALARY_SIZE));
        return Result.success(page);
    }

    @Override
    public Result searchByInc(String companyName) {
        QueryWrapper<Salary> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("c_name", companyName);
        List<Salary> salaryList = salaryMapper.selectList(queryWrapper);
        return Result.success(salaryList);
    }

    @Override
    public Result searchByPosition(String position) {
        QueryWrapper<Salary> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("position", position);
        List<Salary> salaryList = salaryMapper.selectList(queryWrapper);
        return Result.success(salaryList);
    }

    @Override
    public Result searchByCity(String city) {
        QueryWrapper<Salary> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("city", city);
        List<Salary> salaryList = salaryMapper.selectList(queryWrapper);
        return Result.success(salaryList);
    }

    @Override
    public Result revealSalary(Salary salary) {
        Date date = new Date();
        salary.setDate(date);
        salary.setCredit(0);
        salary.setView(0);
        salary.setLikes(0);
        salaryMapper.insert(salary);
        return Result.success();
    }

    @Override
    public Result searchSalaryById(Integer id) {
        Salary salary = salaryMapper.selectById(id);
//        Integer view = salary.getView();
//        salary.setView(view+1);
//        salaryMapper.updateById(salary);
        return Result.success(salary);
    }

    @Override
    public Result addView(Integer id) {
        Salary salary = salaryMapper.selectById(id);
        salary.setView(salary.getView()+1);
        int i = salaryMapper.updateById(salary);
        if(i == 1){
            return Result.success(salary);
        }
        return Result.fail("查询失败！");
    }

    @Override
    public Result campusEmploy(Integer currentPage) {
        IPage<Salary> page = query().like("emp_type", "校招").page(new Page<>(currentPage, SALARY_SIZE));
        return Result.success(page);
    }

    @Override
    public Result internshipEmploy(Integer currentPage) {
        IPage<Salary> page = query().like("emp_type", "实习").page(new Page<>(currentPage, SALARY_SIZE));
        return Result.success(page);
    }

    @Override
    public Result doLikes(Integer id, String userIdentity) {
        //先查询出用户想要点赞的那条薪资
        Salary salary = salaryMapper.selectById(id);
        //若没登录则提醒用户登录
        if(userIdentity.isEmpty()){
            return Result.fail(410,"请先登录");
        }
        //实现点赞逻辑
        Integer likes = salary.getLikes();
        salary.setLikes(likes+1);
        salaryMapper.updateById(salary);
        return Result.success();
    }

    @Override
    public Result undoLike(Integer id, String userIdentity) {
        //先查询出用户想要取消点赞的那条薪资
        Salary salary = salaryMapper.selectById(id);
        //若没登录则提醒用户登录
        if(userIdentity.isEmpty()){
            return Result.fail(410,"请先登录");
        }
        //实现取消点赞逻辑
        Integer likes = salary.getLikes();
        salary.setLikes(likes-1);
        salaryMapper.updateById(salary);
        return Result.success();
    }

}
