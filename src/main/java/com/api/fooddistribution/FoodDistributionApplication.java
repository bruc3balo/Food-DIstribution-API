package com.api.fooddistribution;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.CrossOrigin;

import static com.api.fooddistribution.global.GlobalService.authService;

@SpringBootApplication
@EnableScheduling
@CrossOrigin("*")
@Slf4j
public class FoodDistributionApplication {


    public static void main(String[] args) {
        SpringApplication.run(FoodDistributionApplication.class, args);
    }

    @Bean
    CommandLineRunner run() {
        return args -> {
              //authService.defaults();
        };
    }

}
