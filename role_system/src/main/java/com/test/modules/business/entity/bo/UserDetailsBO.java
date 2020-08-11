package com.test.modules.business.entity.bo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.test.modules.business.entity.Job;
import com.test.modules.business.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * description: 封装用户信息，包含角色、部门、岗位信息
 *
 * @author RenShiWei
 * Date: 2020/8/6 19:57
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@JsonIgnoreProperties(value = "handler")
public class UserDetailsBO extends User {

    /** 根据用户查询其岗位信息 */
    @ApiModelProperty(hidden = true)
    private JobSmallBO job;

    /** 根据用户查询部门信息 */
    @ApiModelProperty(hidden = true)
    private DeptSmallBO dept;

}

