package com.cyb.mongodb.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyb.mongodb.pojo.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}
