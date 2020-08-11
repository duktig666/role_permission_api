package com.test.exception.handler;

import com.test.exception.BaseException;
import com.test.response.enums.ResponseEnum;
import com.test.response.vo.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

/**
 * 功能描述：全局异常处理
 *
 * @author RenShiWei
 * Date: 2020/4/11 19:52
 **/
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 功能描述：处理自定义异常
     *
     * @param e 自定义异常
     * @return restful风格的异常信息
     * @author RenShiWei
     * Date: 2020/4/13 22:18
     */
    @ExceptionHandler(value = BaseException.class)
    public ResponseEntity<ResponseResult<ResponseEnum>> badRequestException ( BaseException e ) {
        // 打印堆栈信息
        if (e.getResponseEnum() != null) {
            log.error("BadRequestException(自定义请求异常)" + e.getResponseEnum().getMsg(), e);
        } else {
            log.error("BadRequestException(自定义请求异常)" + e.getMessage(), e);
        }
        //默认到后端的请求，状态码都为200，自定义的异常由封装的code去控制
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseResult<>(e.getResponseEnum(), e.getResponseEnum()));
    }

    /**
     * description: security的角色权限不足异常
     *
     * @param e 权限不足异常
     * @return 200状态码 403自定义code
     * @author RenShiWei
     * Date: 2020/8/7 19:52
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseResult<String>> handleAccessDeniedException ( AccessDeniedException e ) {

        return ResponseEntity.status(HttpStatus.OK).body(ResponseResult.ok(ResponseEnum.IDENTITY_NOT_POW, e.getMessage()));
    }

    /**
     * 功能描述：处理所有不可知的异常
     *
     * @param e 异常 Throwable(异常的根类)
     * @return 异常对象信息
     * @author RenShiWei
     * Date: 2020/7/10 10:54
     */
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ResponseResult<ResponseEnum>> handleException ( Throwable e ) {
        // 打印堆栈信息
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseResult.ok(ResponseEnum.SEVER_ERROR));
    }

    /**
     * description:处理所有接口数据验证异常
     *
     * @param e 接口数据验证异常
     * @return 统一异常结果处理
     * @author RenShiWei
     * Date: 2020/7/10 11:33
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseResult<String>> handleMethodArgumentNotValidException ( MethodArgumentNotValidException e ) {
        // 打印堆栈信息
        log.error("MethodArgumentNotValidException(接口数据验证异常)", e);
        String message = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        return ResponseEntity.status(HttpStatus.OK).body(ResponseResult.ok(ResponseEnum.REQUEST_PARAMETER_ERROR, message));
    }

}

