package com.test.modules.security.security;

import cn.hutool.json.JSONUtil;
import com.test.response.vo.ResponseResult;
import com.test.response.enums.ResponseEnum;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * description:用户没有访问权限是返回响应的处理类
 *
 * @author RenShiWei
 * Date: 2020/8/3 15:13
 **/
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    /**
     * description:权限不足的处理
     *
     * @author RenShiWei
     * Date: 2020/8/3 15:13
     */
    @Override
    public void handle ( HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException ) throws IOException, ServletException {
        //当用户在没有授权的情况下访问受保护的REST资源时，将调用此方法发送403响应（响应内层code403），默认到达后端的响应都为200
        response.sendError(HttpServletResponse.SC_FORBIDDEN, accessDeniedException.getMessage());
        response.setStatus(200);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(JSONUtil.toJsonStr(ResponseResult.ok(ResponseEnum.IDENTITY_NOT_POW, accessDeniedException.getMessage())));
    }
}

