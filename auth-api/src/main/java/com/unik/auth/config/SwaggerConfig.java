package com.unik.auth.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Value("${api.docs.title:Authentication Service API}")
    private String apiTitle;

    @Value("${api.docs.description:Generic Authentication API documentation}")
    private String apiDescription;

    @Value("${api.docs.version:1.0.0}")
    private String apiVersion;

    @Value("${api.docs.group:auth}")
    private String apiGroup;

    @Value("${api.auth.base-path:/api/auth}")
    private String authBasePath;

    @Value("${api.token.base-path:/api/token}")
    private String tokenBasePath;

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI().info(new Info()
                .title(apiTitle)
                .description(apiDescription)
                .version(apiVersion));
    }

    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder()
                .group(apiGroup)
                .pathsToMatch(authBasePath + "/**", tokenBasePath + "/**")
                .build();
    }
}
