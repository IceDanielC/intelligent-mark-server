package com.cyb.mongodb.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

//管理员账户
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_admin")
public class Admin implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String username;
    private String password;

}
