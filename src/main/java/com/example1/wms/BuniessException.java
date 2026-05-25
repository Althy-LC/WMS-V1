package com.example1.wms;

/**
 * @author Althy
 * @Create 2026/4/28 22:28
 * @Description 自定义异常类-捕获运行时异常做统一处理
 */
public class BuniessException extends RuntimeException{
    private Integer code;

    //带异常提示的异常处理
    public BuniessException(String msg){
        super(msg);
        this.code=500;
    }

    //带检验码和异常提示的异常处理
    public BuniessException(int code,String msg){
        super(msg);
        this.code=code;
    }

    public Integer getCode() {
        return code;
    }
}
