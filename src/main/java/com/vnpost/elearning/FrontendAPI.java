package com.vnpost.elearning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Hello world!
 */
@ServletComponentScan
@SpringBootApplication
@EnableWebMvc
@ComponentScan({"com.vnpost.elearning","eln.common.core"})
public class FrontendAPI {
    public static void main(String[] args) {
        SpringApplication.run(FrontendAPI.class,args);
    }

}
