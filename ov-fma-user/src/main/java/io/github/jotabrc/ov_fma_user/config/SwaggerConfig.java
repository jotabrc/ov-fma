package io.github.jotabrc.ov_fma_user.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static io.github.jotabrc.ov_fma_user.controller.ControllerPath.PREFIX;
import static io.github.jotabrc.ov_fma_user.controller.ControllerPath.VERSION;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI custom() {
        return new OpenAPI()
                .servers(List.of(
                        new Server()
                                .url("http://gateway-service:8080")
                                .description("Gateway")
                ));
    }

    @Bean
    public OpenApiCustomizer customizer() {
        return openApi -> {
            Paths paths = openApi.getPaths();
            if (paths == null) {
                paths = new Paths();
            }
            Paths updatedPaths = new Paths();

            paths.forEach((original, item) -> {
                if (original.startsWith(PREFIX + VERSION)) {
                    String newPath = original.replaceFirst(PREFIX + VERSION, "");
                    updatedPaths.put(newPath, item);
                } else {
                    updatedPaths.put(original, item);
                }
            });
            openApi.setPaths(updatedPaths);
        };
    }
}
