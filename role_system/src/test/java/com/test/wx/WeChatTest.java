package com.test.wx;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import com.test.AppRun;
import com.test.modules.security.controller.AuthController;
import com.test.response.vo.ResponseResult;
import com.test.util.SecurityUtils;
import com.test.util.WeChatUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * description: 微信测试
 *
 * @author RenShiWei
 * Date: 2020/8/9 12:51
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AppRun.class)
public class WeChatTest {

    @Autowired
    private AuthController authController;

    /**
     * description: 测试微信小程序获取openId和sessionKey
     *
     * @author RenShiWei
     * Date: 2020/8/9 21:29
     */
    @Test
    public void testWx(){
        //微信小程序的测试数据
        String encrypt = "ujdg/1Hki9Ko0x0+XSgYLQchgmmOjhN3Ow6+OS88b89PsWak1M2L4xP4tQGPLmmz1iOWADRT5G0csBNcx2Ha5v8yPgCurPWjKkcLMHHifAp4yUmmPo5W4YFJrxAQx41keO+mZmr1a2nzMPSGEVYbIT3GAEdW5tlVoXXKvEa5GsHff480h9ueyARhI70TMWx71la7Pi0FZbc3GTkDEF3ctQ==";
        String iv = "dJrqcJLH8RTM8YlnLipDww==";
        String code = "081LFD000wQc4K1BYo200eKZtL2LFD0z";

        JSONObject sessionKeyOrOpenId = WeChatUtil.getSessionKeyOrOpenId(code);

        System.out.println("sessionKeyOrOpenId：" + sessionKeyOrOpenId);
        String sessionKey = sessionKeyOrOpenId.getStr("session_key");

        JSONObject decrypt = WeChatUtil.getUserInfo(encrypt, sessionKey, iv);
        System.out.println(decrypt);

    }

    /**
     * description: 测试微信小程序登录
     *
     * @author RenShiWei
     * Date: 2020/8/9 21:29
     */
    @Test
    public void testWxLogin(){
        HttpServletRequest request = null;
        //微信小程序的测试数据
        String encryptData = "6/gUZsDVU1lCA9GAxElQc2bKtqSLbyGX6s7BiBaxMXVJGvKeU31eEHY9sNhIdYvQow/q2DjSSDMuJNrNqdAgA/Z1Gu6ZwQ6D30knJsxESB5uKaqf0+RMuXhm+bsB7wwY9SVq8LA8o61NiqflRewehOJ1wIoxM2q6kD3fskrPu/J+ascIg3gLZuzg1A3T9gKvkH1T5QIGJIexyxHs3XRg8Q==";
        String iv = "eaZLWIqmg2gUaAB0LMbQXQ==";
        String code = "0111hP0004YB5K10fz000MDwsf41hP0M";
        ResponseResult<Map<String, Object>> mapResponseResult = authController.weChatLogin(code, iv, encryptData, request);
        System.out.println(mapResponseResult);
    }

    @Test
    public void getUserDetails(){
        System.out.println(SecurityUtils.getUserDetails());
        System.out.println(SecurityUtils.getUsername());
    }





}

