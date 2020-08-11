package com.test.modules.business.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.test.modules.business.entity.Job;
import com.test.modules.business.service.IJobService;
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
 * 岗位表 前端控制器
 *
 * @author RenShiWei
 * @since 2020-08-06
 */
@RestController
@Slf4j
@Api(tags = "岗位模块")
@RequestMapping("/api/business/job")
public class JobController {

  private final IJobService jobService;

  public JobController(IJobService jobService) {
    this.jobService = jobService;
  }

  /**
   * 根据主键id查询一条岗位信息
   *
   * @param id 岗位ID
   * @return 岗位信息
   * @author RenShiWei
   * @since 2020-08-06
   */
  @GetMapping("/{id}")
  @ApiOperation(value = "查询一条岗位信息", notes = "根据主键id查询 \n author：RenShiWei")
  @ApiImplicitParam(name = "id", value = "岗位id", paramType = "path")
  public ResponseResult<Job> jobFindById(@PathVariable Long id) {
    Job job = jobService.getById(id);
    if (ObjectUtil.isNull(job)) {
      log.error("【查看岗位失败");
      return ResponseResult.ok(ResponseEnum.DATA_NOT_FOUND);
    }
    return ResponseResult.ok(job);
  }

  /**
   * 保存和修改公用的
   *
   * @param job 传递的实体
   * @return 是否执行成功
   * @author RenShiWei
   * @since 2020-08-06 ${time}
   */
  @PostMapping
  @ApiOperation(value = "新增/修改岗位", notes = "如果传模板id为新增，否则为修改" + "\n author：RenShiWei")
  @ApiImplicitParam(name = "job", value = "新增/修改的javabean（如果swagger有此参数可忽略）")
  @Transactional
  public ResponseResult<Boolean> jobSaveOrUpdate(Job job) {
    if (job.getId() == null) {
      log.info("【新增岗位】");
    } else {
      log.info("【修改岗位】");
    }
    boolean isExecute = jobService.saveOrUpdate(job);
    if (!isExecute) {
      if (job.getId() == null) {
        log.error("【新增岗位失败】");
        return ResponseResult.ok(ResponseEnum.INSERT_OPERATION_FAIL);
      } else {
        log.error("【修改岗位失败】");
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
  @ApiOperation(value = "删除一条岗位信息", notes = "根据主键id查询 \n author：RenShiWei")
  @ApiImplicitParam(name = "id", value = "岗位id", paramType = "path")
  @Transactional
  public ResponseResult<Boolean> jobDeleteById(@PathVariable Long id) {
    log.info("【删除岗位】");
    boolean isDelete = jobService.removeById(id);
    if (!isDelete) {
      log.error("【删除岗位失败】");
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
  @ApiOperation(value = "批量删除岗位信息", notes = "根据主键id批量删除 \n author：RenShiWei")
  @ApiImplicitParam(name = "item", value = "岗位id的json数组字符串")
  @Transactional
  public ResponseResult<Boolean> deleteBatchIds(String item) {
    log.info("【批量删除岗位】");
    JSONArray jsonArray = JSONUtil.parseArray(item);
    List<Long> idList = JSONUtil.toList(jsonArray, Long.class);
    boolean isDelete = jobService.removeByIds(idList);
    if (!isDelete) {
      log.error("【批量删除岗位失败】");
      return ResponseResult.ok(ResponseEnum.DELETE_OPERATION_FAIL);
    }
    return ResponseResult.ok(isDelete);
  }
}
