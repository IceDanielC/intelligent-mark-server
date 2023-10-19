package com.cyb.mongodb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

//分页查询后返回前端的分页对象
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyPage implements Serializable {

    //总记录数
    private long totalCount;

    //总页数
    private long totalPages;

    //当前页码
    private Integer curPageNum;

    //当前页所有数据
    private Object object;

}
