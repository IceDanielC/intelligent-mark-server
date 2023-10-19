package com.cyb.mongodb.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cyb.mongodb.dto.Result;
import com.cyb.mongodb.pojo.Salary;

public interface SalaryService extends IService<Salary> {

    public Result getSalaries(Integer currentPage);

    //按公司模糊查询
    public Result searchByInc(String companyName);

    //按岗位模糊查询
    Result searchByPosition(String position);

    //按城市模糊查询
    Result searchByCity(String city);

    //薪资爆料
    Result revealSalary(Salary salary);

    //根据id查询薪资
    Result searchSalaryById(Integer id);

    //首次浏览增加浏览量
    Result addView(Integer id);

    //查询所有校招薪资
    Result campusEmploy(Integer currentPage);

    //查询所有实习薪资
    Result internshipEmploy(Integer currentPage);

    //给特定薪资点赞
    Result doLikes(Integer id,String userIdentity);

    //取消点赞
    Result undoLike(Integer id,String userIdentity);
}
