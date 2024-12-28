package me.songjiyoung.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI musinsaOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("shopping API")
                        .description("쇼핑 시스템을 위한 API 문서 (송지영)")
                        .version("1.0"));
    }
}