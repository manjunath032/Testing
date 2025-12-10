package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.ws.config.annotation.EnableWs;

/**
 * Spring Boot Application for WSDL-based Web Service
 */
@SpringBootApplication
@EnableWs
public class SpringWsdlApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringWsdlApplication.class, args);
    }
}

