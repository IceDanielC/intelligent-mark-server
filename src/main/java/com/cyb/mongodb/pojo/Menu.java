package com.cyb.mongodb.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_menu")
public class Menu {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String name;
    private String description;
}
