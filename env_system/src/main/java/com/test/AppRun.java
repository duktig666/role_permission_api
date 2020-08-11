package com.test;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * description:Security的核心配置类
 *
 * @author RenShiWei
 * Date: 2020/8/3 14:56
 *
 * '@EnableTransactionManagement'开启事务支持
 **/
@MapperScan("com.test.modules.business.mapper")
@SpringBootApplication
@EnableTransactionManagement
public class AppRun {

    public static void main(String[] args) {
        SpringApplication.run(AppRun.class, args);
    }

}
