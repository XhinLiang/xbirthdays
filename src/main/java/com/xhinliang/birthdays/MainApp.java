package com.xhinliang.birthdays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.xhinliang.birthdays.common.db.repo"})
@EntityScan(basePackages = {"com.xhinliang.birthdays.common.db.model"})
@EnableScheduling
@EnableTransactionManagement
public class MainApp {

    public static void main(String[] args) {
        new SpringApplication(MainApp.class).run(args);
    }
}
