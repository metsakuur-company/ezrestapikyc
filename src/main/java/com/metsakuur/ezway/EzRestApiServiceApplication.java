package com.metsakuur.ezway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration.class})
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class EzRestApiServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EzRestApiServiceApplication.class, args);
    }

}
