package com.ote;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class UserServiceRunner {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceRunner.class, args);
    }

    @Profile("initUserRights")
    @Bean
    public CommandLineRunner commandLineRunner() {
        return strings -> {

        };
    }


}