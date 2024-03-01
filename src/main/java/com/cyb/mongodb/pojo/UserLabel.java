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
@TableName("t_userLabel")
public class UserLabel {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private String groupName;
    private String description;
}
