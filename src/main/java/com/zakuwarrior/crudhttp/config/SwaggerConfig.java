package com.zakuwarrior.crudhttp.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Spring CRUD API",
                version = "1.0",
                description = "Simple CRUD API for practicing Spring",
                contact = @Contact(
                        name = "zakuwarrior",
                        email = "abramov.ivic@gmail.com"
                )
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Development server")
        }
)
@Configuration
public class SwaggerConfig {
}
