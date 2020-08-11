package com.test.response.vo;

import com.test.response.enums.ResponseEnum;
import lombok.Data;

/**
 * description:对返回前端数据进行封装
 *
 * @author RenShiWei
 * Date: 2020/7/9 22:09
 **/
@Data
public class ResponseResult<T> {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 状态信息说明
     */
    private String message;

    /**
     * 返回的数据
     */
    private T data;

    public ResponseResult ( ResponseEnum responseEnum, T data ) {
        this.code = responseEnum.getCode();
        this.message = responseEnum.getMsg();
        this.data = data;
    }

    public ResponseResult ( T data ) {
        this.code = ResponseEnum.SUCCESS.getCode();
        this.message = ResponseEnum.SUCCESS.getMsg();
        this.data = data;
    }

    public ResponseResult () {
        this.code = ResponseEnum.SUCCESS.getCode();
        this.message = ResponseEnum.SUCCESS.getMsg();
    }

    public ResponseResult ( ResponseEnum responseEnum ) {
        this.code = responseEnum.getCode();
        this.message = responseEnum.getMsg();
    }

    /**
     * description: 接口调用成功，返回枚举中自定义的状态码及数据
     *
     * @param responseEnum 自定义枚举 状态码和信息
     * @param data         返回数据
     * @return 枚举中自定义的状态码及数据
     * @author RenShiWei
     * Date: 2020/7/10 19:57
     */
    public static <E> ResponseResult<E> ok ( ResponseEnum responseEnum, E data ) {
        return new ResponseResult<>(responseEnum, data);
    }

    /**
     * description: 接口调用成功，封装返回数据
     *
     * @param data 返回数据
     * @return 封装返回的数据
     * @author RenShiWei
     * Date: 2020/7/10 19:57
     */
    public static <E> ResponseResult<E> ok ( E data ) {
        return new ResponseResult<>(ResponseEnum.SUCCESS, data);
    }

    /**
     * description: 接口调用成功，只返回状态码和msg，没有返回数据
     *
     * @return 只返回状态码和msg，没有返回数据
     * @author RenShiWei
     * Date: 2020/7/10 19:57
     */
    public static <E> ResponseResult<E> ok () {
        return new ResponseResult<>();
    }

    /**
     * description: 接口调用成功，只返回状态码和msg，没有返回数据
     *
     * @return 只返回状态码和msg，没有返回数据
     * @author RenShiWei
     * Date: 2020/7/10 19:57
     */
    public static <E> ResponseResult<E> ok (ResponseEnum responseEnum) {
        return new ResponseResult<>(responseEnum);
    }

}

