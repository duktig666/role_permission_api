package com.test.modules.business.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.test.modules.business.entity.Dept;
import com.test.modules.business.service.IDeptService;
import com.test.response.enums.ResponseEnum;
import com.test.response.vo.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门表表 前端控制器
 *
 * @author RenShiWei
 * @since 2020-08-06
 */
@RestController
@Slf4j
@Api(tags = "部门表模块")
@RequestMapping("/api/business/dept")
public class DeptController {

    //    @Qualifier("deptService")
    private final IDeptService iDeptService;

    public DeptController ( IDeptService iDeptService ) {
        this.iDeptService = iDeptService;
    }

    /**
     * 根据主键id查询一条部门表信息
     *
     * @param id 部门表ID
     * @return 部门表信息
     * @author RenShiWei
     * @since 2020-08-06
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "查询一条部门表信息", notes = "根据主键id查询 \n author：RenShiWei")
    @ApiImplicitParam(name = "id", value = "部门表id", paramType = "path")
    public ResponseResult<Dept> deptFindById ( @PathVariable Long id ) {
        Dept dept = iDeptService.getById(id);
        if (ObjectUtil.isNull(dept)) {
            log.error("【查看部门表失败");
            return ResponseResult.ok(ResponseEnum.DATA_NOT_FOUND);
        }
        return ResponseResult.ok(dept);
    }

    /**
     * 保存和修改公用的
     *
     * @param dept 传递的实体
     * @return 是否执行成功
     * @author RenShiWei
     * @since 2020-08-06 ${time}
     */
    @PostMapping
    @ApiOperation(value = "新增/修改部门表", notes = "如果传模板id为新增，否则为修改" + "\n author：RenShiWei")
    @ApiImplicitParam(name = "dept", value = "新增/修改的javabean（如果swagger有此参数可忽略）")
    @Transactional
    public ResponseResult<Boolean> deptSaveOrUpdate ( Dept dept ) {
        if (dept.getId() == null) {
            log.info("【新增部门表】");
        } else {
            log.info("【修改部门表】");
        }
        boolean isExecute = iDeptService.saveOrUpdate(dept);
        if (!isExecute) {
            if (dept.getId() == null) {
                log.error("【新增部门表失败】");
                return ResponseResult.ok(ResponseEnum.INSERT_OPERATION_FAIL);
            } else {
                log.error("【修改部门表失败】");
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
    @ApiOperation(value = "删除一条部门表信息", notes = "根据主键id查询 \n author：RenShiWei")
    @ApiImplicitParam(name = "id", value = "部门表id", paramType = "path")
    @Transactional
    public ResponseResult<Boolean> deptDeleteById ( @PathVariable Long id ) {
        log.info("【删除部门表】");
        boolean isDelete = iDeptService.removeById(id);
        if (!isDelete) {
            log.error("【删除部门表失败】");
            return ResponseResult.ok(ResponseEnum.DELETE_OPERATION_FAIL);
        }
        return ResponseResult.ok(isDelete);
    }

    /**
     * 批量删除对象
     *
     * @param item 实体集合ID
     * @return 0 失败 1 成功
     */
    @DeleteMapping("/BatchIds")
    @ApiOperation(value = "批量删除部门表信息", notes = "根据主键id批量删除 \n author：RenShiWei")
    @ApiImplicitParam(name = "item", value = "部门表id的json数组字符串")
    @Transactional
    public ResponseResult<Boolean> deleteBatchIds ( String item ) {
        log.info("【批量删除部门表】");
        JSONArray jsonArray = JSONUtil.parseArray(item);
        List<Long> idList = JSONUtil.toList(jsonArray, Long.class);
        boolean isDelete = iDeptService.removeByIds(idList);
        if (!isDelete) {
            log.error("【批量删除部门表失败】");
            return ResponseResult.ok(ResponseEnum.DELETE_OPERATION_FAIL);
        }
        return ResponseResult.ok(isDelete);
    }
}
