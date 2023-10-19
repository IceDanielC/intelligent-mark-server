package com.cyb.mongodb.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_salary")
public class Salary {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String cName;

    private String position;

    private String city;

    private String description;

    private String empType;

    private String qualification;

    private String industry;

    private String remark;

    private Integer view;

    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date date;

    private Integer credit;

    //点赞数
    private Integer likes;
}
