package com.test.exception;

import com.test.response.enums.ResponseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 功能描述：统一异常处理
 *
 * @author RenShiWei
 * Date: 2020/4/11 19:52
 **/
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BaseException extends RuntimeException{
    /** 异常枚举 */
    private ResponseEnum responseEnum;

}
