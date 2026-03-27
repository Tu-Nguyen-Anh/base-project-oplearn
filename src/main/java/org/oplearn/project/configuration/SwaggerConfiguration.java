package org.oplearn.project.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfiguration {

    @Value("${server.port:8188}")
    private String serverPort;

    @Bean
    public OpenAPI customizeOpenAPI() {
        final String jwtSecuritySchemeName = "bearerAuth";

        Server localServer = new Server();
        localServer.setUrl("http://localhost:" + serverPort);
        localServer.setDescription("Server chạy tại Local");

        Server prodServer = new Server();
        prodServer.setUrl("https://api.codedeohayduoc.io.vn");
        prodServer.setDescription("Server thực tế (Production)");

        return new OpenAPI()
                .info(new Info()
                        .title("My API - SOAR Project")
                        .version("v1")
                        .description("Tài liệu API cho dự án News"))
                .servers(List.of(localServer, prodServer))
                .addSecurityItem(new SecurityRequirement().addList(jwtSecuritySchemeName))
                .components(new Components()
                        .addSecuritySchemes(jwtSecuritySchemeName,
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Nhập Access Token để gọi API")
                        )
                );
    }
}