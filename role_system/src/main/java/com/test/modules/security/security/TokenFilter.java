package com.test.modules.security.security;

import com.test.modules.security.config.SecurityProperties;
import com.test.modules.security.dto.JwtUser;
import com.test.modules.security.dto.OnlineUser;
import com.test.modules.security.service.OnlineUserService;
import com.test.util.SecurityUtils;
import com.test.util.SpringContextHolder;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * description: token拦截器
 *
 * @author RenShiWei
 * Date: 2020/8/3 15:21
 **/
@Slf4j
public class TokenFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    TokenFilter ( TokenProvider tokenProvider ) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilterInternal ( HttpServletRequest request, HttpServletResponse response, FilterChain chain ) throws IOException, ServletException {
        log.info("开始进行token验证");
        String token = resolveToken(request);
        String requestRri = request.getRequestURI();
        // 验证 token 是否存在
        OnlineUser onlineUser = null;
        try {
            SecurityProperties properties = SpringContextHolder.getBean(SecurityProperties.class);
            OnlineUserService onlineUserService = SpringContextHolder.getBean(OnlineUserService.class);
            onlineUser = onlineUserService.getOne(properties.getOnlineKey() + token);
        } catch (ExpiredJwtException e) {
            log.error(e.getMessage());
        }
        if (onlineUser != null && StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
            Authentication authentication = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("set Authentication to security context for '{}', uri: {}", authentication.getName(), requestRri);
        } else {
            log.info("no valid JWT token found, uri: {}", requestRri);
        }
        chain.doFilter(request, response);
    }

    /**
     * 功能描述： 从请求头中获取token信息
     *
     * @param request 请求
     * @return token
     * @author RenShiWei
     * Date: 2020/8/3 15:21
     */
    private String resolveToken ( HttpServletRequest request ) {
        SecurityProperties properties = SpringContextHolder.getBean(SecurityProperties.class);
        String bearerToken = request.getHeader(properties.getHeader());
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(properties.getTokenStartWith())) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

