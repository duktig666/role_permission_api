package com.test.modules.security.controller;

import com.test.modules.security.service.OnlineUserService;
import com.test.response.vo.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * description: 在线用户管理，redis处理
 *
 * @author RenShiWei
 * Date: 2020/8/3 21:07
 **/
@RestController
@RequestMapping("/auth/online")
@Api(tags = "系统：在线用户管理")
public class OnlineController {

    private final OnlineUserService onlineUserService;

    public OnlineController ( OnlineUserService onlineUserService) {
        this.onlineUserService = onlineUserService;
    }

    @ApiOperation("查询在线用户")
    @GetMapping
    public ResponseResult<Map<String, Object>> getAll( String filter, Pageable pageable){
        return ResponseResult.ok(onlineUserService.getAll(filter, pageable));
    }

    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    public ResponseResult<Void> download(HttpServletResponse response, String filter) throws IOException {
        onlineUserService.download(onlineUserService.getAll(filter), response);
        return ResponseResult.ok();
    }

    @ApiOperation("踢出用户")
    @DeleteMapping
    public ResponseResult<Void> delete(@RequestBody Set<String> keys) throws Exception {
        for (String key : keys) {
            onlineUserService.kickOut(key);
        }
        return ResponseResult.ok();
    }
}
