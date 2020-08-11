package com.test.modules.business.service.impl;

import com.test.modules.business.entity.Menu;
import com.test.modules.business.mapper.MenuMapper;
import com.test.modules.business.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统菜单 服务实现类
 * </p>
 *
 * @author RenShiWei
 * @since 2020-08-06
 */
@Service
@Primary
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

}
