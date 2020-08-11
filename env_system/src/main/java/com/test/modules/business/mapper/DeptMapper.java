package com.test.modules.business.mapper;

import com.test.modules.business.entity.Dept;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.test.modules.business.entity.bo.DeptSmallBO;
import com.test.modules.business.entity.bo.RoleSmallBO;
import com.test.modules.business.entity.bo.UserDetailsBO;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 部门表 Mapper 接口
 * </p>
 *
 * @author RenShiWei
 * @since 2020-08-06
 */
@Component
public interface DeptMapper extends BaseMapper<Dept> {

    /**
     * description: 根据部门id查询
     * @param deptId 部门id
     * @return 封装部门信息，包含角色、部门信息
     *
     * @author RenShiWei
     * Date: 2020/8/6 21:40
     */
    @Select("SELECT * FROM dept WHERE id=#{deptId} AND is_deleted=0")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "roles",
                    many = @Many(select = "com.test.modules.business.mapper.DeptMapper.findRoleByDeptId",
                            fetchType = FetchType.LAZY)),
    })
    DeptSmallBO findDeptWithRolesById ( Integer deptId );

    /**
     * description: 根据部门id查询
     * @param deptId 部门id
     * @return 封装角色信息，包含最小化菜单信息（权限）
     *
     * @author RenShiWei
     * Date: 2020/8/6 21:40
     */
    @Select("SELECT id,name,data_scope,level,permission FROM role r LEFT JOIN role_dept rd ON r.id=rd.role_id WHERE dept_id=#{deptId}")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "menus",
                    many = @Many(select = "com.test.modules.business.mapper.RoleMapper.findMenuSmallBoByRoleId",
                            fetchType = FetchType.LAZY)),
    })
    RoleSmallBO findRoleByDeptId(Integer deptId);

}
