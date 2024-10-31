package com.metsakuur.ezway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration.class})
public class EzRestApiServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EzRestApiServiceApplication.class, args);
    }

}
