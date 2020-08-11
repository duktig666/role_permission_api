package com.test.modules.security.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.test.exception.BaseException;
import com.test.modules.business.entity.bo.*;
import com.test.modules.business.service.IRoleService;
import com.test.modules.business.service.IUserService;
import com.test.modules.security.dto.JwtUser;
import com.test.response.enums.ResponseEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * security的用户业务处理类，主要用于用户登录的身份验证
 * @author RenShiWei
 * Date: 2020/8/3 21:07
 */
@Service("userDetailsService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserDetailsServiceImpl implements UserDetailsService {

    private final IUserService userService;

    public UserDetailsServiceImpl ( IUserService userService, IRoleService roleService) {
        this.userService = userService;
    }

    /**
     * 功能描述： 根据用户名去获取用户
     * @param username 用户名
     * @return UserDetails对象
     * @author RenShiWei
     * Date: 2020/2/9 12:04
     */
    @Override
    public UserDetails loadUserByUsername(String username){
        UserDetailsBO userDetails = userService.findByAccount(username);
        if (ObjectUtil.isEmpty(userDetails)) {
            throw new BaseException(ResponseEnum.LOGIN_FAIL);
        } else {
            return createJwtUser(userDetails);
        }
    }

    /**
     * description: 创建JwtUser
     * @param user 封装用户信息，包含角色、部门、岗位信息
     * @return 用户详细信息
     *
     * @author RenShiWei
     * Date: 2020/8/6 22:16
     */
    private UserDetails createJwtUser(UserDetailsBO user) {
        //获得用户的角色权限信息和菜单信息
        Set<RoleSmallBO> roles = user.getDept().getRoles();
        //角色权限信息
        Set<String> permissions = roles.stream()
                .filter(role -> StrUtil.isNotBlank(role.getPermission()))
                .map(RoleSmallBO::getPermission)
                .collect(Collectors.toSet());
        //角色的菜单权限信息
        permissions.addAll(
                roles.stream().flatMap(role -> role.getMenus().stream())
                .filter(menu -> StrUtil.isNotBlank(menu.getPermission()))
                .map(MenuSmallBO::getPermission).collect(Collectors.toSet())
        );

        Collection<GrantedAuthority> authorities = permissions.stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new JwtUser(
                user.getId(),
                user.getName(),
                user.getNickname(),
                user.getSex(),
                user.getPassword(),
                user.getAvatar(),
                user.getEmail(),
                user.getPhone(),
                Optional.ofNullable(user.getDept()).map(DeptSmallBO::getName).orElse(null),
                Optional.ofNullable(user.getJob()).map(JobSmallBO::getName).orElse(null),
                authorities,
                user.getDeleted(),
                user.getCreateTime(),
                user.getLastPasswordResetTime()
        );
    }
}
