package com.soa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.soa.repository")
public class ProductServiceApplication {

    public static void main(String[] args) {
        System.setProperty("javax.net.ssl.trustStore", "./first.truststore");
        System.setProperty("javax.net.ssl.trustStorePassword", "mypassword");
        System.setProperty("jsse.enableSNIExtension", "false");
        SpringApplication.run(ProductServiceApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*");
            }
        };
    }
}

