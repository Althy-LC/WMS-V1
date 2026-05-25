package com.example1.wms;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Althy
 * @Create 2026/4/28 22:18
 * @Description 全局异常处理类
 */

@RestControllerAdvice
@Slf4j
public class GlobalException {

    //业务异常
    @ExceptionHandler(BuniessException.class)
    public Result<?> handleBunessException(BuniessException e){
        log.warn("业务异常:{}",e.getMessage());
        return Result.error(e.getCode(),e.getMessage());
    }

    //参数校验异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleValidException(MethodArgumentNotValidException e){
        String msg = e.getBindingResult().getFieldError().getDefaultMessage();
        log.warn("参数校验异常",msg);
        return Result.error(400,msg);
    }

    // 空指针异常
    @ExceptionHandler(NullPointerException.class)
    public  Result<?> handleNullPointException(NullPointerException e){
        log.error("空指针异常"+e);
        return Result.error("空指针异常请检查参数是否正确");
    }

    //运行时异常
    @ExceptionHandler(RuntimeException.class)
    public Result<?> handleRuntimeException(RuntimeException e){
        log.error("运行时异常"+e);

            String rawMsg = e.getMessage();
            if (rawMsg != null && rawMsg.contains("problem: ")) {
                String yourMsg = rawMsg.substring(rawMsg.indexOf("problem: ") + 9);
                return Result.error(yourMsg);
            }

            return Result.error("系统运行异常");
        }

    //兜底
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e){
        log.error("未知异常",e);
        return Result.error("服务器未知异常");
    }

}
