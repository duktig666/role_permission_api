package com.test.modules.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 教师
 * </p>
 *
 * @author RenShiWei
 * @since 2020-08-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Teacher对象", description="教师")
public class Teacher extends Model<Teacher> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    @ApiModelProperty(value = "0为男，1为女")
    private Boolean sex;

    @ApiModelProperty(value = "年龄")
    private Integer age;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
