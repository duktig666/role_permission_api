package com.test.modules.security.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * description: jwt的相关用户信息
 * 继承UserDetails，方便在UserDetailsServiceImpl中返回UserDetails对象
 *
 * @author RenShiWei
 * Date: 2020/8/3 15:21
 **/
@Getter
@ToString
@AllArgsConstructor
public class JwtUser implements UserDetails {

    private final Long id;

    private final String name;

    private final String nickName;

    private final Boolean sex;

    @JsonIgnore
    private final String password;

    private final String avatar;

    private final String email;

    private final String phone;

    private final String dept;

    private final String job;

    /** 获取权限信息，存角色 */
    @JsonIgnore
    private final Collection<GrantedAuthority> authorities;

    private final boolean deleted;

    private final LocalDateTime createTime;

    @JsonIgnore
    private final LocalDateTime lastPasswordResetDate;

    /** 账号是否未过期，默认是false，记得要改一下 */
    @JsonIgnore
    @Override
    public boolean isAccountNonExpired () {
        return true;
    }

    /** 账号是否未锁定，默认是false，记得也要改一下 */
    @JsonIgnore
    @Override
    public boolean isAccountNonLocked () {
        return true;
    }

    /** 账号凭证是否未过期，默认是false，记得还要改一下 */
    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired () {
        return true;
    }


    @JsonIgnore
    @Override
    public String getPassword () {
        return password;
    }

    @Override
    public String getUsername () {
        return phone;
    }

    /**
     * description:账户是否激活
     * 数据库设置的是0为未删除，代表false，所以这里取 “非”
     *
     * @author RenShiWei
     * Date: 2020/7/10 15:01
     */
    @Override
    public boolean isEnabled () {
        return !deleted;
    }

    public Collection<String> getRoles () {
        return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
    }
}
