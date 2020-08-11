package com.test.modules.business.mapper;

import com.test.modules.business.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.test.modules.business.entity.bo.UserDetailsBO;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author RenShiWei
 * @since 2020-08-06
 */
@Component
public interface UserMapper extends BaseMapper<User> {

    /**
     * description: 根据账号查询
     *
     * @param account 账号
     * @return 封装用户信息，包含角色、部门、岗位信息
     * @author RenShiWei
     * Date: 2020/8/6 21:40
     */
    @Select("SELECT * FROM user WHERE (phone=#{account} OR email=#{account}) AND is_deleted=0")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(id = true, column = "is_deleted", property = "deleted"),
            @Result(column = "dept_id", property = "dept",
                    one = @One(select = "com.test.modules.business.mapper.DeptMapper.findDeptWithRolesById",
                            fetchType = FetchType.EAGER)),
            @Result(column = "job_id", property = "job",
                    one = @One(select = "com.test.modules.business.mapper.JobMapper.findJobSmallBoById",
                            fetchType = FetchType.EAGER)),
    })
    UserDetailsBO findUserDetailsByAccount ( String account );

}
