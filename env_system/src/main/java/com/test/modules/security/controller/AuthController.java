package com.test.modules.security.controller;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.test.annotation.AnonymousAccess;
import com.test.exception.BaseException;
import com.test.modules.business.entity.User;
import com.test.modules.security.config.SecurityProperties;
import com.test.modules.security.dto.AuthUser;
import com.test.modules.security.dto.JwtUser;
import com.test.modules.security.security.TokenProvider;
import com.test.modules.security.service.OnlineUserService;
import com.test.response.enums.ResponseEnum;
import com.test.response.vo.ResponseResult;
import com.test.util.*;
import com.wf.captcha.ArithmeticCaptcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 授权、根据token获取用户详细信息
 *
 * @author RenShiWei
 * Date: 2020/8/3 21:07
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@Api(tags = "系统：系统授权接口")
public class AuthController {

    /** 验证码过期时间 */
    @Value("${loginCode.expiration}")
    private Long expiration;

    /** 是否开启图形验证码验证 */
    @Value("${loginCode.open}")
    private boolean isOpenCode;

    /** 密码解密私钥 */
    @Value("${rsa.private_key}")
    private String privateKey;

    /** 是否设置账号单一登录 */
    @Value("${single.login:false}")
    private Boolean singleLogin;

    private final SecurityProperties properties;
    private final RedisUtils redisUtils;
    private final UserDetailsService userDetailsService;
    private final OnlineUserService onlineUserService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public AuthController ( SecurityProperties properties, RedisUtils redisUtils, UserDetailsService userDetailsService, OnlineUserService onlineUserService, TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder ) {
        this.properties = properties;
        this.redisUtils = redisUtils;
        this.userDetailsService = userDetailsService;
        this.onlineUserService = onlineUserService;
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @ApiOperation("登录授权")
    @AnonymousAccess
    @PostMapping(value = "/login")
    public ResponseResult<Map<String, Object>> login ( @Validated AuthUser authUser, HttpServletRequest request ) {
        // 密码解密
        RSA rsa = new RSA(privateKey, null);
        String password = new String(rsa.decrypt(encryptLogin(authUser.getPassword()), KeyType.PrivateKey));

        //是否开启图形验证码
        if (isOpenCode) {
            // 查询验证码
            String code = (String) redisUtils.get(authUser.getUuid());
            // 清除验证码
            redisUtils.del(authUser.getUuid());
            if (StrUtil.isBlank(code)) {
                //验证码过期或者不存在
                throw new BaseException(ResponseEnum.LOGIN_CODE_EXPIRE_OR_NOT_FOUND);
            }
            if (StrUtil.isBlank(authUser.getCode()) || !authUser.getCode().equalsIgnoreCase(code)) {
                //验证码错误
                throw new BaseException(ResponseEnum.LOGIN_CODE_ERROR);
            }
        }
        //账号密码验证（默认调用UserDetailsServiceImpl的loadUserByUsername方法）
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authUser.getUsername(), password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        //用户信息存储进上下文
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 生成令牌token
        final JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        String token = tokenProvider.createToken(authentication, jwtUser.getId());

        // 保存在线信息
        onlineUserService.save(jwtUser, token, request);
        // 返回 token 与 用户信息
        Map<String, Object> authInfo = new HashMap<String, Object>(2) {{
            put("token", properties.getTokenStartWith() + token);
            put("user", jwtUser);
        }};
        //如果开启单一登录模式
        if (singleLogin) {
            //踢掉之前已经登录的token
            onlineUserService.checkLoginOnUser(authUser.getUsername(), token);
        }

        return ResponseResult.ok(authInfo);
    }

    @PostMapping("/wx/login")
    @ApiOperation(value = "微信小程序一键登录授权", notes = " \n author：Renshiwei")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "code"),
            @ApiImplicitParam(name = "iv", value = "iv"),
            @ApiImplicitParam(name = "encryptData", value = "encryptData")
    })
    @AnonymousAccess
    public ResponseResult<Map<String, Object>> weChatLogin ( String code, String iv, String encryptData, HttpServletRequest request ) {
        JSONObject sessionKeyOrOpenId = WeChatUtil.getSessionKeyOrOpenId(code);
        //得到openId和sessionKey
        String openId = sessionKeyOrOpenId.getStr("openid");
        String sessionKey = sessionKeyOrOpenId.getStr("session_key");

        //获取手机号
        JSONObject jsonObject = WeChatUtil.getUserInfo(encryptData, sessionKey, iv);

        assert jsonObject != null;
        String phone = jsonObject.getStr("phoneNumber");

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone, phone).eq(User::getOpenId, openId);
        Integer count = new User().selectCount(queryWrapper);

        //说明后台有此账户信息，允许通过登录
        Map<String, Object> authInfo;
        if (count > 0) {
            log.info("【微信登录】openId：" + openId + "\n phone:" + phone);
            UserDetails userDetails = userDetailsService.loadUserByUsername(phone);
            //将权限存入上下文中，进行权限的判断
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            //用户信息存储进上下文
            SecurityContextHolder.getContext().setAuthentication(authentication);

            final JwtUser jwtUser = (JwtUser) userDetails;
            // 生成令牌token
            String token = tokenProvider.createToken(authentication, jwtUser.getId());
            // 保存在线信息
            onlineUserService.save(jwtUser, token, request);
            // 返回 token 与 用户信息
            authInfo = new HashMap<String, Object>(2) {{
                put("token", properties.getTokenStartWith() + token);
                put("user", jwtUser);
            }};
            //如果开启单一登录模式
            if (singleLogin) {
                //踢掉之前已经登录的token
                onlineUserService.checkLoginOnUser(phone, token);
            }
        } else {
            log.info("【微信登录失败】openId：" + openId + "\n phone:" + phone);
            return ResponseResult.ok(ResponseEnum.LOGIN_FAIL);
        }
        return ResponseResult.ok(authInfo);
    }

    @ApiOperation("获取用户信息")
    @GetMapping(value = "/info")
    @PreAuthorize("@permissions.check('admin')")
    public ResponseResult<JwtUser> getUserInfo () {
        User user = new User();
        String phone = user.selectById(SecurityUtils.getUsername()).getPhone();
        JwtUser jwtUser = (JwtUser) userDetailsService.loadUserByUsername(phone);
        return ResponseResult.ok(jwtUser);
    }

    @ApiOperation("测试获取用户信息")
    @GetMapping(value = "/test/info")
    @PreAuthorize("@permissions.check('admin')")
    public ResponseResult<UserDetails> testUserInfo () {
        return ResponseResult.ok(SecurityUtils.getUserDetails());
    }

    @AnonymousAccess
    @ApiOperation("获取验证码")
    @GetMapping(value = "/code")
    public ResponseResult<Map<String, Object>> getCode () {
        // 算术类型 https://gitee.com/whvse/EasyCaptcha
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(111, 36);
        // 几位数运算，默认是两位
        captcha.setLen(2);
        // 获取运算的结果
        String result = captcha.text();
        String uuid = properties.getCodeKey() + IdUtil.simpleUUID();
        // 保存
        redisUtils.set(uuid, result, expiration, TimeUnit.MINUTES);
        // 验证码信息
        Map<String, Object> imgResult = new HashMap<String, Object>(2) {{
            put("img", captcha.toBase64());
            put("uuid", uuid);
        }};
        return ResponseResult.ok(imgResult);
    }

    @ApiOperation("退出登录")
    @AnonymousAccess
    @DeleteMapping(value = "/logout")
    public ResponseResult<Void> logout ( HttpServletRequest request ) {
        onlineUserService.logout(tokenProvider.getToken(request));
        return ResponseResult.ok();
    }

    /**
     * description: 测试中使用，主要用于前端还未进行账号密码加密时使用
     *
     * @param password 前端未加密密码
     * @return 公钥加密后的密码
     * @author RenShiWei
     * Date: 2020/8/7 21:07
     */
    private byte[] encryptLogin ( String password ) {
        final String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBANL378k3RiZHWx5AfJqdH9xRNBmD9wGD2iRe41HdTNF8RUhNnHit5NpMNtGL0NPTSSpPjjI1kJfVorRvaQerUgkCAwEAAQ==";
        // 密码加密
        RSA rsa = new RSA(null, publicKey);
        return rsa.encrypt(StrUtil.bytes(password, CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);
    }

}
