package com.cyb.mongodb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cyb.mongodb.dao.CommentMapper;
import com.cyb.mongodb.dto.Result;
import com.cyb.mongodb.pojo.Comment;
import com.cyb.mongodb.pojo.Salary;
import com.cyb.mongodb.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public Result commentList(Integer salaryId) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("salary_id", salaryId);
        List<Comment> commentList = commentMapper.selectList(queryWrapper);
        return Result.success(commentList);
    }

    @Override
    public Result publishComment(Comment comment) {
        comment.setDate(new Date());
        commentMapper.insert(comment);
        return Result.success("发布评论成功");
    }

    @Override
    public Result commentsByUser(String identifier) {
        List<Comment> comments = commentMapper.selectList(new QueryWrapper<Comment>().eq("avatar", identifier));
        return Result.success(comments);
    }

    @Override
    public Result delComments(Integer id) {
        int i = commentMapper.deleteById(id);
        return Result.success(i);
    }
}
