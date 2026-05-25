package com.example1.wms;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Althy
 * @Create 2026/4/28 22:18
 * @Description 统一返回结果类
 */
@Data
public class Result<T> implements Serializable {
    private Integer code;
    private String msg;
    private T data;

    //无返回值访问成功
    public static <T> Result<T> success(){
        return success(null);
    }

    //带返回值访问成功
    public static <T> Result<T> success(T data){
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMsg("操作成功");
        result.setData(data);
        return result;
    }

    //无异常信息访问失败
    public static <T> Result<T> error(){
        return error(500,"操作失败");
    }

    //带异常信息访问失败
    public static <T> Result<T> error(String msg){
        return error(500,msg);
    }

    //带状态码，异常信息访问失败
    public static <T> Result<T> error(int code,String msg){
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

}
