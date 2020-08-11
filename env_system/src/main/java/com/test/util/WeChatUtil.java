package com.test.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.Getter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * description: 微信小程序工具
 *
 * @author RenShiWei
 * Date: 2020/8/9 9:17
 **/
@Configuration
public class WeChatUtil {

    @Getter
    private static String appId;

    @Getter
    private static String appSecret;

    @Value("${applet.appId}")
    public void setAppId ( String appId ) {
        WeChatUtil.appId = appId;
    }

    @Value("${applet.appSecret}")
    public void setAppSecret ( String appSecret ) {
        WeChatUtil.appSecret = appSecret;
    }

    /**
     * description: 获取微信的session_key、openid
     *
     * @param jsCode 前端通过调用wx.login()传来的jsCode
     * @return 微信的session_key、openid的json数据
     * @author RenShiWei
     * Date: 2020/8/9 9:43
     */
    public static JSONObject getSessionKeyOrOpenId ( String jsCode ) {
        Map<String, Object> paramMap = new HashMap<>(4);
        paramMap.put("appid", appId);
        paramMap.put("secret", appSecret);
        paramMap.put("js_code", jsCode);
        paramMap.put("grant_type", "authorization_code");
        Object result = HttpUtil.get("https://api.weixin.qq.com/sns/jscode2session?", paramMap);
        return JSONUtil.parseObj(result);
    }

    /**
     * description: 获取微信用户的基本信息
     *
     * @return 微信用户的基本信息
     * @author RenShiWei
     * Date: 2020/8/9 9:44
     */
    public static JSONObject getUserInfo ( String encryptedData, String sessionKey, String iv ) {
        // 被加密的数据
        byte[] dataByte = Base64.decode(encryptedData);
        // 加密秘钥
        byte[] keyByte = Base64.decode(sessionKey);
        // 偏移量
        byte[] ivByte = Base64.decode(iv);

        try {
            // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + 1;
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, StandardCharsets.UTF_8);
                return JSONUtil.parseObj(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

