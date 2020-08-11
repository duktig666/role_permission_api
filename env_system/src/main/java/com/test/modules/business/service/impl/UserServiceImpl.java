package com.test.modules.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.modules.business.entity.User;
import com.test.modules.business.entity.bo.UserDetailsBO;
import com.test.modules.business.mapper.UserMapper;
import com.test.modules.business.service.IUserService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author RenShiWei
 * @since 2020-08-06
 */
@Service
@Primary
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private final UserMapper userMapper;

    public UserServiceImpl ( UserMapper userMapper ) {
        this.userMapper = userMapper;
    }

    /**
     * description: 根据账号查询
     *
     * @param account 账号
     * @return 封装用户信息，包含角色、部门、岗位信息
     * @author RenShiWei
     * Date: 2020/8/6 20:28
     */
    @Override
    public UserDetailsBO findByAccount ( String account ) {
        return userMapper.findUserDetailsByAccount(account);
    }

}
