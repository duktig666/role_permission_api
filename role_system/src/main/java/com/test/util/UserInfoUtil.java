package com.test.util;

import cn.hutool.json.JSONObject;
import com.test.exception.BaseException;
import com.test.modules.security.dto.JwtUser;
import com.test.response.enums.ResponseEnum;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * description:
 *
 * @author RenShiWei
 * Date: 2020/8/10 9:46
 **/
public class UserInfoUtil {

    /**
     * description: 获取在线用户的id
     * @return 用户id
     *
     * @author RenShiWei
     * Date: 2020/8/10 16:58
     */
    public static int getUserId() {
        Object obj = SecurityUtils.getUserDetails();
        return new JSONObject(obj).get("username", Integer.class);
    }

}

