package com.test.modules.security.security;

import cn.hutool.json.JSONUtil;
import com.test.response.vo.ResponseResult;
import com.test.response.enums.ResponseEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * description:认证失败处理类
 *
 * @author RenShiWei
 * Date: 2020/8/3 15:15
 **/
@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * 功能描述： 认证失败的凭证处理方法
     *
     * @author RenShiWei
     * Date: 2020/8/3 15:15
     */
    @Override
    public void commence ( HttpServletRequest request,
                           HttpServletResponse response,
                           AuthenticationException authException ) throws IOException {
         /*
            因为我们使用的REST API,所以我们认为到达后台的请求都是正常的，
            所以返回的HTTP状态码都是200，用接口返回的code来确定请求是否正常。
         */
        // 当用户尝试访问安全的REST资源而不提供任何凭据时，将调用此方法发送401 响应
        response.setStatus(200);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        if (authException == null) {
            response.getWriter().write(JSONUtil.toJsonStr(ResponseResult.ok(ResponseEnum.IDENTITY_NOT_PASS, "Unauthorized")));
        } else {
            response.getWriter().write(JSONUtil.toJsonStr(ResponseResult.ok(ResponseEnum.IDENTITY_NOT_PASS, authException.getMessage())));
        }
        log.error("访问未经许可，请重新登录");
    }

}

