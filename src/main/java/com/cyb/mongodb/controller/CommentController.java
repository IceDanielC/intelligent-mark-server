package com.cyb.mongodb.controller;

import com.cyb.mongodb.dto.Result;
import com.cyb.mongodb.pojo.Comment;
import com.cyb.mongodb.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    //查询所有评论
    @GetMapping("list/{id}")
    public Result getComments(@PathVariable("id")Integer salaryId){
        return commentService.commentList(salaryId);
    }

    //新增评论
    @PostMapping("publish")
    public Result pushComment(@RequestBody Comment comment){
        return commentService.publishComment(comment);
    }

    //查询某个用户的所有评论
    @GetMapping("user")
    public Result userComments(@RequestParam("userIdentifier")String userIdentifier){
        return commentService.commentsByUser(userIdentifier);
    }

    //删除评论
    @PostMapping("delete/{id}")
    public Result userComments(@PathVariable("id")Integer id){
        return commentService.delComments(id);
    }
}
