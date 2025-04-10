package com.biblioteca.backend;

import com.biblioteca.backend.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

//Run this to start backend
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class BackendApplication implements CommandLineRunner {

    @Autowired
    private DatabaseService dbService;

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Override
    public void run(String... args) {
        dbService.executarScript("/scripts/schema.sql");
    }
}
