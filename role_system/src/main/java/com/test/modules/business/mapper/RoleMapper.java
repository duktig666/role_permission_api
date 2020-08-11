package com.test.modules.business.mapper;

import com.test.modules.business.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.test.modules.business.entity.bo.MenuSmallBO;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author RenShiWei
 * @since 2020-08-06
 */
@Component
public interface RoleMapper extends BaseMapper<Role> {

    @Select("SELECT id,name,permission FROM menu m LEFT JOIN role_menu rm ON rm.menu_id=m.id WHERE rm.role_id=#{roleId}")
    Set<MenuSmallBO> findMenuSmallBoByRoleId ( int roleId );

}
