package com.test.modules.business.service;

import com.test.modules.business.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.test.modules.business.entity.bo.UserDetailsBO;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author RenShiWei
 * @since 2020-08-06
 */
public interface IUserService extends IService<User> {

    /**
     * description: 根据账号查询
     * @param account 账号
     * @return 封装用户信息，包含角色、部门、岗位信息
     *
     * @author RenShiWei
     * Date: 2020/8/6 20:28
     */
    UserDetailsBO findByAccount( String account);

}
