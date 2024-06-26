package com.cyb.mongodb.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_dataset")
public class Dataset {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String username;
    private Integer userId;
    private String version;
    private String name;
    private String annotateType;
    private Integer imgNumber;
    private Integer annotatedNumber;
    private String module;
    private String savePlace;
    private String createTime;
    private Integer size;
    @TableField(exist = false)
    private List<Dataset> children;

}
