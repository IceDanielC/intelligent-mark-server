package com.cyb.mongodb.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_labelInfo")
public class LabelInfo implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer imageId;
    private String labelName;
    private Integer topPx;
    private Integer leftPx;
    private Integer heightPx;
    private Integer widthPx;
    private String color;

}
