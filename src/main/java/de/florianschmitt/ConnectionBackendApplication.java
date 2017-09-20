package de.florianschmitt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import de.florianschmitt.model.entities.BaseEntity;

@EntityScan(basePackageClasses = {BaseEntity.class, Jsr310JpaConverters.class})
@SpringBootApplication
public class ConnectionBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConnectionBackendApplication.class, args);
    }
}
