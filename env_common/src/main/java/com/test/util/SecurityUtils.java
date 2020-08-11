package com.test.util;

import cn.hutool.json.JSONObject;
import com.test.exception.BaseException;
import com.test.response.enums.ResponseEnum;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * 获取当前登录的用户
 *
 * @author RenShiWei
 * Date: 2020/8/3 21:07
 */
@Component
public class SecurityUtils {

    /**
     * 功能描述：得到SecurityContext上下文中的UserDetails信息
     *
     * @return UserDetails
     * @author RenShiWei
     * Date: 2020/2/9 11:57
     */
    public static UserDetails getUserDetails() {
        UserDetails userDetails;
        try {
            userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(ResponseEnum.NEED_LOGIN);
        }
        return userDetails;
    }

    /**
     * 获取系统用户名称
     *
     * @return 系统用户名称
     */
    public static String getUsername() {
        Object obj = getUserDetails();
        return new JSONObject(obj).get("username", String.class);
    }

}
