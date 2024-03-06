package com.jackbracey.prototaltest.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;

import java.util.List;

public class SwaggerConfiguration {

    @Bean
    public OpenAPI defineOpenApi() {
        Server server = new Server();
        server.setUrl("http://localhost:8080");
        server.setDescription("Development");

        Info information = new Info()
                .title("Prototal Test API")
                .version("1.0")
                .description("Prototal Test API");
        return new OpenAPI().info(information).servers(List.of(server));
    }

}
