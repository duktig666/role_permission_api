package com.test.config;

import com.test.util.SecurityUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * security的角色权限标识配置
 *
 * @author RenShiWei
 * Date: 2020/8/7 18:14
 */
@Service(value = "permissions")
public class MyPermissionConfig {

    public Boolean check(String ...permissions){
        // 获取当前用户的所有权限
        List<String> myPermissions = SecurityUtils.getUserDetails().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        // 判断当前用户的所有权限是否包含接口上定义的权限
        return myPermissions.contains("admin") || Arrays.stream(permissions).anyMatch(myPermissions::contains);
    }

}
