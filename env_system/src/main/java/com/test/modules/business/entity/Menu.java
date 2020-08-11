package com.test.modules.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统菜单
 * </p>
 *
 * @author RenShiWei
 * @since 2020-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Menu对象", description="系统菜单")
public class Menu extends Model<Menu> {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "菜单名称")
    private String name;

    @ApiModelProperty(value = "组件名称")
    private String componentName;

    @ApiModelProperty(value = "组件")
    private String component;

    @ApiModelProperty(value = "上级菜单ID（0为顶级菜单，默认为0）")
    private Long lastId;

    @ApiModelProperty(value = "排序（默认为0，不排序）")
    private Integer sort;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "链接地址")
    private String path;

    @ApiModelProperty(value = "权限")
    private String permission;

    @ApiModelProperty(value = "类型")
    private Integer type;

    @ApiModelProperty(value = "是否外链（0为不外链，1为外链，默认为0）")
    @TableField("is_link")
    private Boolean link;

    @ApiModelProperty(value = "是否缓存（0为不缓存，1位缓存，默认为0）")
    @TableField("is_cache")
    private Boolean cache;

    @ApiModelProperty(value = "是否隐藏（0为不隐藏，1为隐藏，默认为0）")
    @TableField("is_hidden")
    private Boolean hidden;

    @ApiModelProperty(value = "创建日期")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更细时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "是否删除（0为未删除，1为删除，默认为0）")
    @TableField("is_deleted")
    private Boolean deleted;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
