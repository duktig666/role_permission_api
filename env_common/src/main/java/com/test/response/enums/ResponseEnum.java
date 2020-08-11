package com.test.response.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * description:返回信息枚举
 *
 * @author RenShiWei
 * Date: 2020/7/9 21:41
 **/
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ResponseEnum {


    /**
     * 成功（默认返回状态码）
     */
    SUCCESS(0, "SUCCESS"),

    /**
     * 全局未知异常
     */
    SEVER_ERROR(500, "服务器异常,请重试"),
    REQUEST_PARAMETER_ERROR(500, "请求参数异常,请重试"),

    /*
     * 登录异常
     */
    NEED_LOGIN(403, "Need to login!"),
    LOGIN_FAIL(401, "登录失败，账号或者密码错误"),
    LOGIN_EXPIRE(401, "登录过期"),
    IDENTITY_NOT_PASS(401, "访问未经许可，请重新登录"),
    IDENTITY_NOT_POW(403, "您的用户权限不足"),
    LOGIN_FAIL_RELOGIN(402, "登录失败，请重试"),
    LOGIN_FAIL_CODE(405, "验证码错误"),
    NO_USER(405, "没有该用户"),
    REGISTER_FAIL(400, "注册失败，手机号已经存在"),
    LOGIN_CODE_EXPIRE_OR_NOT_FOUND(401, "验证码不存在或已过期"),
    LOGIN_CODE_ERROR(401, "验证码错误"),

    /*
     * 统一操作异常
     */
    OPERATION_FAIL(500, "操作失败！"),
    SELECT_OPERATION_FAIL(500, "查询操作失败！"),
    UPDATE_OPERATION_FAIL(500, "更新操作失败！"),
    DELETE_OPERATION_FAIL(500, "删除操作失败！"),
    INSERT_OPERATION_FAIL(500, "新增操作失败！"),

    /*
     * 文件异常
     */
    FILE_OVERSTEP_SIZE(500, "文件超出规定大小"),
    FILE_UPLOAD_FAIL(500, "文件上传失败"),
    FILE_LOADING_FAIL(404, "文件不存在，加载失败"),
    FILE_REQUEST_FAIL(400, "文件类型不支持查看"),
    FILE_TYPE_IMAGE_FAIL(500, "请上传图片类型的文件"),

    /*
     * 业务异常
     */
    DATA_NOT_FOUND(404, "没有数据了~~~"),
    /** 当前用户非此条通知的通知人员 */
    USER_NOT_NOTICE(404, "您非通知接收人，暂时不能查看~~~"),

    ;
    private int code;
    private String msg;


}
