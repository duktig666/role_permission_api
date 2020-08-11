package com.test.modules.business.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import com.test.modules.business.service.IRoleService;
import com.test.modules.business.entity.Role;
import com.test.response.vo.ResponseResult;
import com.test.response.enums.ResponseEnum;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import cn.hutool.core.util.ObjectUtil;

import java.util.List;

/**
 * <p>
 * 角色表表 前端控制器
 * </p>
 *
 * @author RenShiWei
 * @since 2020-08-06
 */
@RestController
@Slf4j
@Api(tags = "角色表模块")
@RequestMapping("/api/role")
public class RoleController {

    private final IRoleService roleService;

    public RoleController ( IRoleService roleService ) {
        this.roleService = roleService;
    }

    /**
     * 根据主键id查询一条角色表信息
     *
     * @param id 角色表ID
     * @return 角色表信息
     * @author RenShiWei
     * @since 2020-08-06
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "查询一条角色表信息", notes = "根据主键id查询 \n author：RenShiWei")
    @ApiImplicitParam(name = "id", value = "角色表id", paramType = "path")
    @PreAuthorize("@permissions.check('admin')")
    public ResponseResult<Role> roleFindById ( @PathVariable Long id ) {
        Role role = roleService.getById(id);
        if (ObjectUtil.isNull(role)) {
            log.error("【查看角色表失败");
            return ResponseResult.ok(ResponseEnum.DATA_NOT_FOUND);
        }
        return ResponseResult.ok(role);
    }


    /**
     * 保存和修改公用的
     *
     * @param role 传递的实体
     * @return 是否执行成功
     * @author RenShiWei
     * @since 2020-08-06 ${time}
     */
    @PostMapping
    @ApiOperation(value = "新增/修改角色表", notes = "如果传模板id为新增，否则为修改" +
            "\n author：RenShiWei")
    @ApiImplicitParam(name = "role", value = "新增/修改的javabean（如果swagger有此参数可忽略）")
    @Transactional
    public ResponseResult<Boolean> roleSaveOrUpdate ( Role role ) {
        if (role.getId() == null) {
            log.info("【新增角色表】");
        } else {
            log.info("【修改角色表】");
        }
        boolean isExecute = roleService.saveOrUpdate(role);
        if (!isExecute) {
            if (role.getId() == null) {
                log.error("【新增角色表失败】");
                return ResponseResult.ok(ResponseEnum.INSERT_OPERATION_FAIL);
            } else {
                log.error("【修改角色表失败】");
                return ResponseResult.ok(ResponseEnum.UPDATE_OPERATION_FAIL);
            }
        }
        return ResponseResult.ok(isExecute);
    }

    /**
     * 根据id删除对象
     *
     * @param id 实体ID
     * @return 是否删除成功
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除一条角色表信息", notes = "根据主键id查询 \n author：RenShiWei")
    @ApiImplicitParam(name = "id", value = "角色表id", paramType = "path")
    @Transactional
    public ResponseResult<Boolean> roleDeleteById ( @PathVariable Long id ) {
        log.info("【删除角色表】");
        boolean isDelete = roleService.removeById(id);
        if (!isDelete) {
            log.error("【删除角色表失败】");
            return ResponseResult.ok(ResponseEnum.DELETE_OPERATION_FAIL);
        }
        return ResponseResult.ok(isDelete);
    }

    /**
     * 批量删除对象
     *
     * @param item 实体集合ID
     * @return 0 失败  1 成功
     */
    @DeleteMapping("/BatchIds")
    @ApiOperation(value = "批量删除角色表信息", notes = "根据主键id批量删除 \n author：RenShiWei")
    @ApiImplicitParam(name = "item", value = "角色表id的json数组字符串")
    @Transactional
    public ResponseResult<Boolean> deleteBatchIds ( String item ) {
        log.info("【批量删除角色表】");
        JSONArray jsonArray = JSONUtil.parseArray(item);
        List<Long> idList = JSONUtil.toList(jsonArray, Long.class);
        boolean isDelete = roleService.removeByIds(idList);
        if (!isDelete) {
            log.error("【批量删除角色表失败】");
            return ResponseResult.ok(ResponseEnum.DELETE_OPERATION_FAIL);
        }
        return ResponseResult.ok(isDelete);
    }


}
