package com.cyb.mongodb.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cyb.mongodb.dto.Result;
import com.cyb.mongodb.pojo.Comment;

public interface CommentService extends IService<Comment> {

    //查询该爆料所有评论
    Result commentList(Integer salaryId);

    //发表评论
    Result publishComment(Comment comment);

    //查询某个用户的所有评论
    Result commentsByUser(String identifier);

    //删除评论
    Result delComments(Integer id);
}
