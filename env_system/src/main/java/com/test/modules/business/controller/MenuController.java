package com.test.modules.business.controller;

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
import com.test.modules.business.service.IMenuService;
import com.test.modules.business.entity.Menu;
import com.test.response.vo.ResponseResult;
import com.test.response.enums.ResponseEnum;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import cn.hutool.core.util.ObjectUtil;

import java.util.List;


/**
 * <p>
 * 系统菜单表 前端控制器
 * </p>
 *
 * @author RenShiWei
 * @since 2020-08-06
 */
@RestController
@Slf4j
@Api(tags = "系统菜单模块")
@RequestMapping("/api/business/menu")
public class MenuController {

    private final IMenuService menuService;

    public MenuController ( IMenuService menuService ) {
        this.menuService = menuService;
    }

    /**
     * 根据主键id查询一条系统菜单信息
     *
     * @param id 系统菜单ID
     * @return 系统菜单信息
     * @author RenShiWei
     * @since 2020-08-06
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "查询一条系统菜单信息", notes = "根据主键id查询 \n author：RenShiWei")
    @ApiImplicitParam(name = "id", value = "系统菜单id", paramType = "path")
    public ResponseResult<Menu> menuFindById ( @PathVariable Long id ) {
        Menu menu = menuService.getById(id);
        if (ObjectUtil.isNull(menu)) {
            log.error("【查看系统菜单失败");
            return ResponseResult.ok(ResponseEnum.DATA_NOT_FOUND);
        }
        return ResponseResult.ok(menu);
    }


    /**
     * 保存和修改公用的
     *
     * @param menu 传递的实体
     * @return 是否执行成功
     * @author RenShiWei
     * @since 2020-08-06 ${time}
     */
    @PostMapping
    @ApiOperation(value = "新增/修改系统菜单", notes = "如果传模板id为新增，否则为修改" +
            "\n author：RenShiWei")
    @ApiImplicitParam(name = "menu", value = "新增/修改的javabean（如果swagger有此参数可忽略）")
    @Transactional
    public ResponseResult<Boolean> menuSaveOrUpdate ( Menu menu ) {
        if (menu.getId() == null) {
            log.info("【新增系统菜单】");
        } else {
            log.info("【修改系统菜单】");
        }
        boolean isExecute = menuService.saveOrUpdate(menu);
        if (!isExecute) {
            if (menu.getId() == null) {
                log.error("【新增系统菜单失败】");
                return ResponseResult.ok(ResponseEnum.INSERT_OPERATION_FAIL);
            } else {
                log.error("【修改系统菜单失败】");
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
    @ApiOperation(value = "删除一条系统菜单信息", notes = "根据主键id查询 \n author：RenShiWei")
    @ApiImplicitParam(name = "id", value = "系统菜单id", paramType = "path")
    @Transactional
    public ResponseResult<Boolean> menuDeleteById ( @PathVariable Long id ) {
        log.info("【删除系统菜单】");
        boolean isDelete = menuService.removeById(id);
        if (!isDelete) {
            log.error("【删除系统菜单失败】");
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
    @ApiOperation(value = "批量删除系统菜单信息", notes = "根据主键id批量删除 \n author：RenShiWei")
    @ApiImplicitParam(name = "item", value = "系统菜单id的json数组字符串")
    @Transactional
    public ResponseResult<Boolean> deleteBatchIds ( String item ) {
        log.info("【批量删除系统菜单】");
        JSONArray jsonArray = JSONUtil.parseArray(item);
        List<Long> idList = JSONUtil.toList(jsonArray, Long.class);
        boolean isDelete = menuService.removeByIds(idList);
        if (!isDelete) {
            log.error("【批量删除系统菜单失败】");
            return ResponseResult.ok(ResponseEnum.DELETE_OPERATION_FAIL);
        }
        return ResponseResult.ok(isDelete);
    }


}
