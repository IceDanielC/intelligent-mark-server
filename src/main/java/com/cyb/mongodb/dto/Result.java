package com.cyb.mongodb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result implements Serializable {

    private Integer code;
    private String msg;
    private Object data;

    public static Result success(){
        return new Result(200,"操作成功",null);
    }

    public static Result success(Object object){
        return new Result(200,"操作成功",object);
    }

    public static Result fail(String msg){
        return new Result(400,msg,null);
    }

    public static Result fail(int code, String msg){
        return new Result(code,msg,null);
    }

}
