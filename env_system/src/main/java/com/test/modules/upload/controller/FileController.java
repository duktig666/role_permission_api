package com.test.modules.upload.controller;

import com.test.exception.BaseException;
import com.test.modules.upload.service.FileService;
import com.test.response.enums.ResponseEnum;
import com.test.response.vo.ResponseResult;
import com.test.util.FileUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * class description：关于文件的Controller类
 *
 * @author rsw
 * Date: 2020/2/5
 * Time: 14:24
 **/
@Slf4j
@Api(tags = "文件管理模块")
@RestController
@RequestMapping("/api/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @ApiOperation("文件上传（任意类型文件）")
    @PostMapping(value = "/uploadFile")
    public ResponseResult<String> uploadFile ( @RequestPart MultipartFile file ) {
        return ResponseResult.ok(fileService.upload(file));
    }

    @ApiOperation("文件上传（图片）")
    @PostMapping(value = "/uploadImage")
    public ResponseResult<String> uploadImage ( @RequestPart MultipartFile file ) {
        if (!FileUtils.isImage(file)) {
            log.error("【图片上传失败-文件类型不符合】");
            throw new BaseException(ResponseEnum.FILE_TYPE_IMAGE_FAIL);
        }
        System.out.println(file.getClass().toString());
        return ResponseResult.ok(fileService.upload(file));
    }

    @GetMapping("/download/{fileName:.+}")
    @ApiOperation("文件获取=>ALL")
    @ApiImplicitParam(name = "fileName", value = "文件名", paramType = "path")
    public ResponseEntity<Resource> downloadFile ( @PathVariable String fileName, HttpServletRequest request ) {
        //加载文件资源
        Resource resource = fileService.loadFileAsResource(fileName);

        //设置response参数
        String contentType = null;
        try {
            assert resource != null;
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.warn("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);

    }

}

