package com.barberia.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);

        System.out.println("=========================================================");
        System.out.println(" Eureka Server esta corriendo en: http://localhost:8761");
        System.out.println(" Dashboard: Abre esa URL en el navegador");
        System.out.println("=========================================================");
    }
}
