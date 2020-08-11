package com.test.modules.business.entity.bo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * description: 最小化封装部门信息
 *
 * @author RenShiWei
 * Date: 2020/8/6 20:40
 **/
@Data
public class DeptSmallBO implements Serializable {

    private Long id;

    private String name;

    /** 根据部门查询角色信息 */
    @ApiModelProperty(hidden = true)
    private Set<RoleSmallBO> roles;

}

