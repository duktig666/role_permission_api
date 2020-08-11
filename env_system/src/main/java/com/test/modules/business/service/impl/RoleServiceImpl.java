package com.test.modules.business.service.impl;

import com.test.modules.business.entity.Role;
import com.test.modules.business.mapper.RoleMapper;
import com.test.modules.business.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author RenShiWei
 * @since 2020-08-06
 */
@Service
@Primary
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

}
