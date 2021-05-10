package com.offcn.qiyieureka;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class QiyiEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(QiyiEurekaApplication.class,args);
    }

}
