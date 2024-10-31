package com.metsakuur.ezway.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@Getter
@Setter
public class EzConfig {

    @Value("${ezway.ip}")
    private String ip;

    @Value("${ezway.port}")
    private int port;

    @Value("${ezway.channel}")
    private String channel;

    @Value("${ezway.uuid}")
    private String uuid;

}
