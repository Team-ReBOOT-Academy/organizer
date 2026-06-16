package ru.reboot.organizer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Точка входа в программу.
 */
@SpringBootApplication
@EnableScheduling
public class TgOrganizerApplication {
    public static void main(String[] args) {
        SpringApplication.run(TgOrganizerApplication.class, args);
    }
}
