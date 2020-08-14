package com.test.modules.security.config;

import com.test.annotation.AnonymousAccess;
import com.test.modules.security.security.JwtAccessDeniedHandler;
import com.test.modules.security.security.JwtAuthenticationEntryPoint;
import com.test.modules.security.security.TokenConfigurer;
import com.test.modules.security.security.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * description:Security的核心配置类
 *
 * @author RenShiWei
 * Date: 2020/8/3 14:56
 * <p>
 * '@EnableGlobalMethodSecurity' 保证post之前的注解可以使用
 * '@EnableWebSecurity' 这个注解必须加，开启Security
 **/
@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /** token提供对象 */
    private final TokenProvider tokenProvider;

    /** 默认过滤 */
    private final CorsFilter corsFilter;

    /** 认证失败处理类 */
    private final JwtAuthenticationEntryPoint authenticationErrorHandler;

    /** 权限不足处理类 */
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    private final ApplicationContext applicationContext;

    public SecurityConfig ( TokenProvider tokenProvider,
                            CorsFilter corsFilter,
                            JwtAuthenticationEntryPoint authenticationErrorHandler,
                            JwtAccessDeniedHandler jwtAccessDeniedHandler,
                            ApplicationContext applicationContext ) {
        this.tokenProvider = tokenProvider;
        this.corsFilter = corsFilter;
        this.authenticationErrorHandler = authenticationErrorHandler;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
        this.applicationContext = applicationContext;
    }

    /**
     * description: 去除 ROLE_ 前缀
     *
     * @author RenShiWei
     * Date: 2020/8/7 16:34
     */
    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults () {
        return new GrantedAuthorityDefaults("");
    }

    /**
     * description: 指定密码加密方式
     *
     * @author RenShiWei
     * Date: 2020/8/7 16:34
     */
    @Bean
    public PasswordEncoder passwordEncoder () {
        // 密码加密方式
        return new BCryptPasswordEncoder();
    }

    /**
     * description: 配置Security
     *
     * @author RenShiWei
     * Date: 2020/8/7 16:35
     */
    @Override
    protected void configure ( HttpSecurity httpSecurity ) throws Exception {
        // 搜寻匿名标记 url： @AnonymousAccess，放行允许匿名访问的接口
        Map<RequestMappingInfo, HandlerMethod> handlerMethodMap =
                applicationContext.getBean(RequestMappingHandlerMapping.class).getHandlerMethods();
        Set<String> anonymousUrls = new HashSet<>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> infoEntry : handlerMethodMap.entrySet()) {
            HandlerMethod handlerMethod = infoEntry.getValue();
            AnonymousAccess anonymousAccess = handlerMethod.getMethodAnnotation(AnonymousAccess.class);
            if (null != anonymousAccess) {
                anonymousUrls.addAll(infoEntry.getKey().getPatternsCondition().getPatterns());
            }
        }
        log.info("放行的接口(匿名访问)" + anonymousUrls);

        httpSecurity
                // 由于使用的是JWT，我们这里不需要csrf   禁用 CSRF 禁用 Spring Security 自带的跨域处理
                .csrf().disable()
                // 添加默认过滤方式，之后重写使用tokenFilter
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                // 授权异常
                .exceptionHandling()
                //认证失败处理（没有凭证，可以进行一些操作）
                .authenticationEntryPoint(authenticationErrorHandler).and()
                .exceptionHandling()
                //权限不足的处理
                .accessDeniedHandler(jwtAccessDeniedHandler)

                // 防止iframe 造成跨域
                .and()
                .headers()
                .frameOptions()
                .disable()

                // 不创建会话
                .and()
                //要使用jwt托管安全信息，所以把Session禁止掉
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                //添加多个子项来指定网址的自定义要求
                .authorizeRequests()
                // 静态资源等等
                .antMatchers(
                        HttpMethod.GET,
                        "/*.html",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/webSocket/**",
                        "/ws/**"
                ).permitAll()
                // swagger 文档
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/*/api-docs").permitAll()
                // javamelody监控
                .antMatchers("/monitoring").permitAll()
                // actuator监控
                .antMatchers("/actuator/**").permitAll()
                // 文件
                .antMatchers("/avatar/**").permitAll()
                .antMatchers("/file/**").permitAll()
                .antMatchers("/question/**").permitAll()
                // 阿里巴巴 druid
                .antMatchers("/druid/**").permitAll()
                // 放行OPTIONS请求
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // 自定义匿名访问所有url放行 ： 允许匿名和带权限以及登录用户访问
                .antMatchers(anonymousUrls.toArray(new String[0])).permitAll()
                // 剩下所有的验证都需要验证
                .anyRequest().authenticated()
                //调用配置适配器，指定token过滤器
                .and().apply(securityConfigurerAdapter());
    }

    /**
     * description: 指定自定义的token配置，指定token生成者和token过滤器
     *
     * @author RenShiWei
     * Date: 2020/8/7 16:42
     */
    private TokenConfigurer securityConfigurerAdapter () {
        return new TokenConfigurer(tokenProvider);
    }
}

