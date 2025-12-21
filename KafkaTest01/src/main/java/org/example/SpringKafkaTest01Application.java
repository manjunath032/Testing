package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot Application for WSDL-based Web Service
 */
@SpringBootApplication(scanBasePackages = "org.example")
public class SpringKafkaTest01Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringKafkaTest01Application.class, args);
    }
}
