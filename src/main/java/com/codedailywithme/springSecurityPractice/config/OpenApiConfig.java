package com.codedailywithme.springSecurityPractice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI defineOpenApi(){
        Server server=new Server();
        server.setUrl("http://localhost:8080");
        server.setDescription("development");
        Contact contact=new Contact();
        contact.setName("blaise");
        contact.setEmail("bigirabagaboblaise@gmail.com");
        Info information =new Info()
                .title("employee management system api")
                .version("1.0")
                .description("This api exposes endpoints to authenticate and authorize the users to our application")
                .contact(contact);
        return new OpenAPI().info(information).servers(List.of(server));
    }
}
