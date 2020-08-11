package ${package.Controller};

<#--标注controller，判断是否为rset风格-->
<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#--如果继承有公共controller，导入-->
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>
<#--请求类型注解-->
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
<#--swagger相关注解-->
<#if swagger2>
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
</#if>
<#--日志-->
<#if entityLombokModel>
import lombok.extern.slf4j.Slf4j;
</#if>
<#--事务-->
import org.springframework.transaction.annotation.Transactional;
<#--实体类和service类-->
import ${package.Service}.${table.serviceName};
import ${package.Entity}.${entity};
<#--自定义的导包——！！！需要根据实际情况修改-->
import com.test.response.vo.ResponseResult;
import com.test.response.enums.ResponseEnum;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import cn.hutool.core.util.ObjectUtil;

import java.util.List;

/**
* <p>
*  ${table.comment} 前端控制器
* </p>
* @author ${author}
* @since ${date}
*/
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
<#--日志注解，采用Lombok的快捷日志-->
<#if entityLombokModel>
@Slf4j
</#if>
<#--swagger注解-->
<#if swagger2>
@Api(tags = "${table.comment}模块")
</#if>
@RequestMapping("/api/${table.entityPath}")
<#if superControllerClassPackage??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>

    private final ${table.serviceName} ${table.entityPath}Service;
    <#--采用构造器注入-->
    public ${table.controllerName}(${table.serviceName} ${table.entityPath}Service){
        this.${table.entityPath}Service= ${table.entityPath}Service;
    }

    /**
    * 根据主键id查询一条${table.comment}信息
    * @param id ${table.comment}ID
    * @return ${table.comment}信息
    *
    * @author ${author}
    * @since ${date}
    */
    @GetMapping("/{id}")
    @ApiOperation(value = "查询一条${table.comment}信息", notes = "根据主键id查询 \n author：${author}")
    @ApiImplicitParam(name = "id", value = "${table.comment}id", paramType = "path")
    public ResponseResult<${entity}> find${entity}ById(@PathVariable Long id){
        ${entity} ${table.entityPath} = ${table.entityPath}Service.getById(id);
        if(ObjectUtil.isNull(${table.entityPath})){
            log.error("【查看${table.comment}失败");
            return ResponseResult.ok(ResponseEnum.DATA_NOT_FOUND);
        }
        return ResponseResult.ok(${table.entityPath});
    }

    /**
    * 保存和修改公用的
    * @param ${table.entityPath} 传递的实体
    * @return 是否执行成功
    *
    * @author ${author}
    * @since ${date}
    */
    @PostMapping
    @ApiOperation(value = "新增/修改${table.comment}", notes = "如果传模板id为新增，否则为修改" +
    "\n author：${author}")
    @ApiImplicitParam(name = "${table.entityPath}", value = "新增/修改的javabean（如果swagger有此参数可忽略）")
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult<Boolean> save${entity}OrUpdate(${entity} ${table.entityPath}){
        if(${table.entityPath}.getId()==null){
            log.info("【新增${table.comment}】");
        }else{
            log.info("【修改${table.comment}】");
        }
        boolean isExecute= ${table.entityPath}Service.saveOrUpdate(${table.entityPath});
        if(!isExecute){
            if(${table.entityPath}.getId()==null){
                log.error("【新增${table.comment}失败】");
                return ResponseResult.ok(ResponseEnum.INSERT_OPERATION_FAIL);
            }else{
                log.error("【修改${table.comment}失败】");
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
    @ApiOperation(value = "删除一条${table.comment}信息", notes = "根据主键id查询 \n author：${author}")
    @ApiImplicitParam(name = "id", value = "${table.comment}id", paramType = "path")
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult<Boolean> delete${entity}ById(@PathVariable Long id){
        log.info("【删除${table.comment}】");
        boolean isDelete= ${table.entityPath}Service.removeById(id);
        if(!isDelete){
            log.error("【删除${table.comment}失败】");
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
    @ApiOperation(value = "批量删除${table.comment}信息", notes = "根据主键id批量删除 \n author：${author}")
    @ApiImplicitParam(name = "item", value = "${table.comment}id的json数组字符串")
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult<Boolean> deleteBatchIds(String item){
        log.info("【批量删除${table.comment}】");
        JSONArray jsonArray=JSONUtil.parseArray(item);
        List<Long> idList=JSONUtil.toList(jsonArray,Long.class);
        boolean isDelete= ${table.entityPath}Service.removeByIds(idList);
        if(!isDelete){
            log.error("【批量删除${table.comment}失败】");
            return ResponseResult.ok(ResponseEnum.DELETE_OPERATION_FAIL);
        }
        return ResponseResult.ok(isDelete);
    }

}