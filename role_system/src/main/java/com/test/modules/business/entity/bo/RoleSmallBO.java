package com.test.modules.business.entity.bo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.test.modules.business.entity.Menu;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * description: 最小化封装角色信息
 *
 * @author RenShiWei
 * Date: 2020/8/6 20:52
 **/
@Data
public class RoleSmallBO implements Serializable {

    private Long id;

    private String name;

    private Integer level;

    private String dataScope;

    private String permission;

    private Set<MenuSmallBO> menus;

}

