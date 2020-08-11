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
import com.test.modules.business.service.ITeacherService;
import com.test.modules.business.entity.Teacher;
import com.test.response.vo.ResponseResult;
import com.test.response.enums.ResponseEnum;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import cn.hutool.core.util.ObjectUtil;

import java.util.List;

/**
* <p>
*  教师 前端控制器
* </p>
* @author RenShiWei
* @since 2020-08-10
*/
@RestController
@Slf4j
@Api(tags = "教师模块")
@RequestMapping("/api/teacher")
public class TeacherController {

    private final ITeacherService teacherService;
    public TeacherController(ITeacherService teacherService){
        this.teacherService= teacherService;
    }

    /**
    * 根据主键id查询一条教师信息
    * @param id 教师ID
    * @return 教师信息
    *
    * @author RenShiWei
    * @since 2020-08-10
    */
    @GetMapping("/{id}")
    @ApiOperation(value = "查询一条教师信息", notes = "根据主键id查询 \n author：RenShiWei")
    @ApiImplicitParam(name = "id", value = "教师id", paramType = "path")
    public ResponseResult<Teacher> findTeacherById(@PathVariable Long id){
        Teacher teacher = teacherService.getById(id);
        if(ObjectUtil.isNull(teacher)){
            log.error("【查看教师失败");
            return ResponseResult.ok(ResponseEnum.DATA_NOT_FOUND);
        }
        return ResponseResult.ok(teacher);
    }

    /**
    * 保存和修改公用的
    * @param teacher 传递的实体
    * @return 是否执行成功
    *
    * @author RenShiWei
    * @since 2020-08-10
    */
    @PostMapping
    @ApiOperation(value = "新增/修改教师", notes = "如果传模板id为新增，否则为修改" +
    "\n author：RenShiWei")
    @ApiImplicitParam(name = "teacher", value = "新增/修改的javabean（如果swagger有此参数可忽略）")
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult<Boolean> saveTeacherOrUpdate(Teacher teacher){
        if(teacher.getId()==null){
            log.info("【新增教师】");
        }else{
            log.info("【修改教师】");
        }
        boolean isExecute= teacherService.saveOrUpdate(teacher);
        if(!isExecute){
            if(teacher.getId()==null){
                log.error("【新增教师失败】");
                return ResponseResult.ok(ResponseEnum.INSERT_OPERATION_FAIL);
            }else{
                log.error("【修改教师失败】");
                return ResponseResult.ok(ResponseEnum.UPDATE_OPERATION_FAIL);
            }
        }
        return ResponseResult.ok(isExecute);
    }

    /**
    * 根据id删除对象
    * @param id  实体ID
    * @return 是否删除成功
    */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除一条教师信息", notes = "根据主键id查询 \n author：RenShiWei")
    @ApiImplicitParam(name = "id", value = "教师id", paramType = "path")
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult<Boolean> deleteTeacherById(@PathVariable Long id){
        log.info("【删除教师】");
        boolean isDelete= teacherService.removeById(id);
        if(!isDelete){
            log.error("【删除教师失败】");
            return ResponseResult.ok(ResponseEnum.DELETE_OPERATION_FAIL);
        }
        return ResponseResult.ok(isDelete);
    }

    /**
    * 批量删除对象
    * @param item 实体集合ID
    * @return  0 失败  1 成功
    */
    @DeleteMapping("/BatchIds")
    @ApiOperation(value = "批量删除教师信息", notes = "根据主键id批量删除 \n author：RenShiWei")
    @ApiImplicitParam(name = "item", value = "教师id的json数组字符串")
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult<Boolean> deleteBatchIds(String item){
        log.info("【批量删除教师】");
        JSONArray jsonArray=JSONUtil.parseArray(item);
        List<Long> idList=JSONUtil.toList(jsonArray,Long.class);
        boolean isDelete= teacherService.removeByIds(idList);
        if(!isDelete){
            log.error("【批量删除教师失败】");
            return ResponseResult.ok(ResponseEnum.DELETE_OPERATION_FAIL);
        }
        return ResponseResult.ok(isDelete);
    }

}