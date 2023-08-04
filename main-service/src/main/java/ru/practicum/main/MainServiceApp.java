package ru.practicum.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({StatsClient.class})
public class MainServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(MainServiceApp.class, args);
    }
}
