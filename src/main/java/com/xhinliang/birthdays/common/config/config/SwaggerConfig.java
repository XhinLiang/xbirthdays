package com.xhinliang.birthdays.common.config.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author xhinliang
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private ApiInfo buildApiInfo() {
        return new ApiInfoBuilder()
            .title("Birthdays REST API Reference") //
            .version("1.0.0") //
            .license("Apache 2.0") //
            .description("REST API for anyone.") //
            .contact(new Contact("XhinLiang", "xbirthdays.top", "x@xhinliang.com")) //
            .build();
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2) //
            .apiInfo(buildApiInfo()) //
            .useDefaultResponseMessages(false) //
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(path -> path == null || path.startsWith("/api")) //
            .apis(RequestHandlerSelectors.basePackage("com.xhinliang.birthdays.api.controller"))
            .build()
            .securitySchemes(securitySchemes())
            .securityContexts(securityContexts());
    }

    private List<ApiKey> securitySchemes() {
        return newArrayList(new ApiKey("Authorization", "Authorization", "header"));
    }

    private List<SecurityContext> securityContexts() {
        return newArrayList(
            SecurityContext.builder() //
                .securityReferences(defaultAuth()) //
                .forPaths(this::notOuterPath) //
                .build()
        );
    }

    private boolean notOuterPath(String path) {
        if (StringUtils.isBlank(path)) {
            return true;
        }
        return !path.startsWith("/api/auth/login") && !path.startsWith("/api/auth/register");
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return newArrayList(
            new SecurityReference("Authorization", authorizationScopes));
    }
}
