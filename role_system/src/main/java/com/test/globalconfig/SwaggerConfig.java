package com.test.globalconfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 功能描述：Swagger的配置类
 *
 * @author RenShiWei
 * Date: 2020/4/11 10:14
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Value("${jwt.token-start-with}")
    private String tokenStartWith;

    @Value("${swagger.enabled}")
    private Boolean enabled;

    @Value("${swagger.host}")
    private String swaggerHost;

    @Bean
    public Docket createRestApi () {
        ParameterBuilder ticketPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        ticketPar
                .name(tokenHeader)
                .description("token")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .defaultValue(tokenStartWith)
                .required(false)
                .build();
        pars.add(ticketPar.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .directModelSubstitute(LocalDateTime.class, Date.class)
                .enable(enabled)
                .apiInfo(apiInfo())
                .host(swaggerHost)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.test"))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(securitySchemes())
                .globalOperationParameters(pars);
    }

    /**
     * 功能描述：配置在线文档的基本信息
     *
     * @author RenShiWei Date: 2020/7/9 10:21
     */
    private ApiInfo apiInfo () {
        return new ApiInfoBuilder()
                .title("角色权限管理测试系统")
                .description("角色权限管理测试系统")
                .version("1.0.0-SNAPSHOT")
                .build();
    }

    /**
     * description:配置swagger登录后所有接口都可以访问(暂时未成功)
     *
     * @author RenShiWei Date: 2020/7/12 16:16
     */
    private List<ApiKey> securitySchemes () {
        List<ApiKey> apiKeyList = new ArrayList<>();
        apiKeyList.add(new ApiKey("token", "token", "header"));
        return apiKeyList;
    }
}
