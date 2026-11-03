package com.distribuido.raiznordestematriz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RaizNordesteMatrizApplication {

    public static void main(String[] args) {
        SpringApplication.run(RaizNordesteMatrizApplication.class, args);
    }
}
