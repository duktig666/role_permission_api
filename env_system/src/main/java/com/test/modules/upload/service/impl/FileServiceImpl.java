package com.test.modules.upload.service.impl;

import com.test.exception.BaseException;
import com.test.modules.upload.service.FileService;
import com.test.response.enums.ResponseEnum;
import com.test.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * class description：文件管理
 *
 * @author rsw
 * Date: 2020/2/5
 * Time: 14:26
 **/
@Slf4j
@Service
public class FileServiceImpl implements FileService {

    /**
     * 配置文件上传路径
     */
    @Value("${file.path}")
    private String filePath;

    @Override
    public String upload ( MultipartFile multipartFile ) {
        File file = FileUtils.upload(multipartFile, filePath);
        System.out.println(file);
        if (file == null) {
            log.error("【文件上传失败】");
            throw new BaseException(ResponseEnum.FILE_UPLOAD_FAIL);
        }
        //返回图片地址
        log.info("【图片上传成功】");
        return file.getName();
    }

    /**
     * description:加载文件资源
     *
     * @param fileName 文件名
     * @return 文件资源
     * @author RenShiWei
     * Date: 2020/7/21 9:48
     */
    @Override
    public Resource loadFileAsResource ( String fileName ) {
        String path = filePath + fileName;
        Resource resource = FileUtils.loadFileAsResource(path);
        if (resource == null) {
            log.error("【文件资源不存在】fileName：" + fileName);
            throw new BaseException(ResponseEnum.FILE_LOADING_FAIL);
        }
        return resource;
    }


}

