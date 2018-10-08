package com.huanshare.configServerMysql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableConfigServer
@EnableEurekaClient
public class ConfigServerMysqlApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerMysqlApplication.class, args);
    }
}
